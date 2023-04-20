package org.dcm4che.typeddicom.parser;

import org.apache.commons.text.StringEscapeUtils;
import org.dcm4che.typeddicom.parser.metamodel.*;
import org.dcm4che.typeddicom.parser.table.*;
import org.dcm4che.typeddicom.parser.utils.KeywordUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class parses the 3th part of the DICOM Standard XML
 * (https://dicom.nema.org/medical/dicom/current/source/docbook/part03/part03.xml)
 */
public class DicomPart03Handler extends MemorizeTablesDicomPartHandler {
    private static final String FUNCTIONAL_GROUP_MACRO_REFERENCE = "FUNCTIONAL GROUP MACRO";
    private final Set<ModuleMetaInfo> modules;
    private final Map<String, Set<DataElementMetaInfo>> dataElementMetaInfos;
    private final Set<InformationObjectDefinitionMetaInfo> iods;
    private final Set<ModuleTable> moduleTables = new HashSet<>();
    private final List<TableEntry> sequenceAncestors = new ArrayList<>();
    private final Map<String, MacroTable> macroTables = new HashMap<>();
    private final Map<String, MacroMetaInfo> macros = new HashMap<>();
    private final List<String> columns = new LinkedList<>();
    private MacroTable currentMacro = null;
    private ModuleTable currentModule;
    private boolean inAttributesTableBody = false;
    private String currentTableId = "";
    // should only be not null in a sequence row
    private String lastReference;
    private String lastReferenceInFirstColumn;
    private boolean isInTableRow = false;
    private String tableEntryHref = null;

    public DicomPart03Handler(Map<String, Set<DataElementMetaInfo>> dataElementMetaInfos) {
        this(new HashSet<>(), new HashSet<>(), dataElementMetaInfos);
    }

    public DicomPart03Handler(Set<ModuleMetaInfo> modules, Set<InformationObjectDefinitionMetaInfo> iods, Map<String, Set<DataElementMetaInfo>> dataElementMetaInfos) {
        this.modules = modules;
        this.iods = iods;
        this.dataElementMetaInfos = dataElementMetaInfos;
    }

    private static int sequenceDepth(String name) {
        int i = 0;
        if (name == null) {
            return 0;
        }
        for (char c : name.toCharArray()) {
            if (c != '>') {
                return i;
            }
            i++;
        }
        return i;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("table".equals(qName)) {
            this.currentTableId = attributes.getValue("xml:id");
        } else if ("caption".equals(qName)) {
            startRecordingText();
        } else if ((this.currentModule != null || this.currentMacro != null) && "tbody".equals(qName)) {
            this.inAttributesTableBody = true;
        } else if (inAttributesTableBody && "tr".equals(qName)) {
            isInTableRow = true;
            this.columns.clear();
        } else if (isInTableRow && "td".equals(qName)) {
            lastReference = null;
            startRecordingHTML();
        } else if ("th".equals(qName)) {
            startRecordingText();
        } else if ("xref".equals(qName)) {
            lastReference = attributes.getValue("linkend");
        } else if (isInTableRow && tableEntryHref == null && "para".equals(qName)) {
            tableEntryHref = getUrlFromXmlId(attributes.getValue("xml:id"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("caption".equals(qName)) {
            // check for table caption of module definitions
            String recordedText = getRecordedText();
            // ignore the weird stuff happening in https://dicom.nema.org/medical/dicom/current/output/html/part03.html#table_C.36.25-2
            // because it would ruin the module derived from https://dicom.nema.org/medical/dicom/current/output/html/part03.html#table_C.36.25-1
            // (same caption) as well as the example tables
            if (!currentTableId.equals("table_C.36.25-2") &&
                    !recordedText.contains("Example Module Table") &&
                    !recordedText.contains("Example Macro Attributes") &&
                    !recordedText.contains("Example Module Table Without The Use of An Attribute Macro")) {
                if (recordedText.endsWith(" Module Attributes") || recordedText.endsWith(" Module Table")) {
                    String moduleName = recordedText.replace(" Attributes", "").trim();
                    this.currentModule = new ModuleTable(
                            this.currentSectionId,
                            moduleName,
                            getUrlFromXmlId(this.currentSectionId)
                    );
                } else if (recordedText.contains("Macro Attributes")) {
                    String macroName = recordedText.replace(" Attributes", "").trim();
                    this.currentMacro = new MacroTable(macroName, currentTableId, getUrlFromXmlId(currentTableId));
                }
            }
        } else if ((this.currentModule != null || this.currentMacro != null) && "table".equals(qName)) {
            // table ends (add module meta info to set)
            if (this.currentModule != null) {
                if (this.currentModule.getTableEntries() != null) {
                    this.moduleTables.add(currentModule);
                }
                this.currentModule = null;
            } else {
                if (!currentMacro.getTableEntries().isEmpty()) {
                    this.macroTables.put(currentMacro.getTableId(), currentMacro);
                }
                this.currentMacro = null;
            }
            this.sequenceAncestors.clear();
        } else if ("tbody".equals(qName)) {
            // table body ends
            this.inAttributesTableBody = false;
        } else if (isInTableRow && "tr".equals(qName)) {
            handleEndOfTableRow();
            isInTableRow = false;
            tableEntryHref = null;
        } else if (isInTableRow && "td".equals(qName)) {
            if (columns.isEmpty()) {
                lastReferenceInFirstColumn = lastReference;
            }
            handleEndOfTableCell();
        } else if ("th".equals(qName)) {
            String columnHead = getRecordedText();
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public String getBaseHrefUrl() {
        return getDicomStandardHtmlUrl() + "/part03.html";
    }

    @Override
    protected String getLabelPrefix() {
        return "DICOM Standard Part 3";
    }

    @Override
    public void endDocument() {
        Map<String, ModuleMetaInfo> moduleMap = new HashMap<>();
        for (ModuleTable moduleTable : moduleTables) {
            ModuleMetaInfo module = new ModuleMetaInfo(moduleTable);
            for (TableEntry moduleTableEntry : moduleTable.getTableEntries()) {
                module.addDataElementMetaInfo(resolveMacrosRecursively(moduleTableEntry, new Context(module.getName(), module.getHref())));
            }
            modules.add(module);
            putReferenceForAllSuperSectionsIfNoClash(moduleMap, module.getSectionId(), module);
        }

        for (Table table : this.getTables()) {
            if (table.getCaption().endsWith(" IOD Modules")) {
                String name = table.getCaption().replaceAll("Modules", "").trim();
                InformationObjectDefinitionMetaInfo iod = new InformationObjectDefinitionMetaInfo(
                        name,
                        KeywordUtils.sanitizeAsJavaIdentifier(name),
                        table.getHref(),
                        table.getSectionId());
                for (int r = 0; r < table.getRows(); r++) {
                    TableCell referenceCell = table.getTableCell(r, "Reference");
                    if (referenceCell != null) {
                        String reference = extractModuleReference(referenceCell.getContent());
                        ModuleMetaInfo moduleMetaInfo = moduleMap.get(reference);
                        iod.getModuleReferences().add(new IODModuleReference(moduleMetaInfo));
                    }
                }
                iods.add(iod);
            }
        }
    }

    private void putReferenceForAllSuperSectionsIfNoClash(Map<String, ModuleMetaInfo> moduleMap, String sectionId, ModuleMetaInfo module) {
        // force if leave reference
        if (module.getSectionId().equals(sectionId)) {
            moduleMap.put(sectionId, module);
        }
        if (moduleMap.containsKey(sectionId)) {
            // don't remove if it is leave reference (happens at https://dicom.nema.org/medical/dicom/current/output/html/part03.html#sect_C.36.25)
            if (moduleMap.get(sectionId) != null && !sectionId.equals(moduleMap.get(sectionId).getSectionId())) {
                moduleMap.put(sectionId, null);
            }
        } else {
            moduleMap.put(sectionId, module);
        }
        int lastDot = sectionId.lastIndexOf('.');
        if (lastDot >= 0) {
            putReferenceForAllSuperSectionsIfNoClash(moduleMap, sectionId.substring(0, lastDot), module);
        }
    }

    private void handleEndOfTableRow() {
        handleEndOfAttributesTableRow();
    }

    private void handleEndOfAttributesTableRow() {
        removeHTMLTagsFromColumn(0);
        for (int i = 1; i < this.columns.size() - 1; i++) {
            removeHTMLTagsFromColumn(i);
        }
        String name = StringEscapeUtils.unescapeHtml4(columns.get(0).trim());
        int currentSequenceDepth = sequenceDepth(name);
        name = name.substring(currentSequenceDepth).trim();
        TableEntry currentTableEntry;
        boolean validStandardInclude = name.startsWith("Include") &&
                lastReferenceInFirstColumn != null &&
                lastReferenceInFirstColumn.startsWith("table_");
        boolean validAttribute = columns.size() > 1 && columns.get(1).matches("\\([0-9A-F]{2}[0-9A-Fx]{2},[0-9A-F]{2}[0-9A-Fx]{2}\\)");
        boolean validFunctionalGroupMacroInclude = name.startsWith("Include one or more Functional Group Macros");
        if (columns.size() == 3 && validAttribute) {
            currentTableEntry = new AttributeTableEntry(tableEntryHref, name, columns.get(1), columns.get(2));
        } else if (columns.size() == 4 && validAttribute) {
            currentTableEntry = new AttributeTableEntry(
                    tableEntryHref,
                    name,
                    columns.get(1),
                    columns.get(2),
                    columns.get(3)
            );
        } else if (columns.size() == 2 && validStandardInclude) {
            currentTableEntry = new MacroTableEntry(tableEntryHref, lastReferenceInFirstColumn, columns.get(1));
        } else if (columns.size() == 1 && validStandardInclude) {
            currentTableEntry = new MacroTableEntry(tableEntryHref, lastReferenceInFirstColumn);
        } else if (validFunctionalGroupMacroInclude) {
            currentTableEntry = new MacroTableEntry(tableEntryHref, FUNCTIONAL_GROUP_MACRO_REFERENCE);
        } else {
            System.out.println("Invalid Row: " + Arrays.toString(columns.toArray()));
            return;
        }

        addTableEntry(currentTableEntry, currentSequenceDepth);

        sequenceAncestors.add(currentTableEntry);
    }

    private String extractModuleReference(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("(?s).*?<a href=\"" + getDicomStandardHtmlUrl() + "/part03.html#([^\"]*)\">.*$", "$1");
    }

    private Iterable<DataElementMetaInfo> resolveMacrosRecursively(TableEntry tableEntry, Context context) {
        if (tableEntry instanceof MacroTableEntry) {
            MacroTableEntry macroTableEntry = (MacroTableEntry) tableEntry;
            String tableId = macroTableEntry.getTableId();
            Set<MacroTable> matchingMacroTables = getMatchingMacroTables(tableId);
            if (matchingMacroTables.isEmpty()) {
                System.out.println("Invalid macro key: " + tableId);
                return Collections.emptyList();
            }
            MacroMetaInfo macroMetaInfo = macros.get(tableId);
            if (macroMetaInfo == null) {
                macroMetaInfo = new MacroMetaInfo(tableId);
                macros.put(macroMetaInfo.getTableId(), macroMetaInfo);
            }
            for (MacroTable macroTable : matchingMacroTables) {
                for (TableEntry macroSubEntry : macroTable.getTableEntries()) {
                    ContextEntry newContextEntry = new ContextEntry(macroTable.getName(), macroTable.getHref());
                    // Stop at recursive macros
                    if (!context.getContext().contains(newContextEntry)) {
                        macroMetaInfo.addDataElementMetaInfo(resolveMacrosRecursively(
                                macroSubEntry,
                                new Context(newContextEntry)
                        ));
                    }
                }
            }
            return macroMetaInfo.getSubDataElementMetaInfos();
        } else if (tableEntry instanceof AttributeTableEntry) {
            AttributeTableEntry attributeTableEntry = (AttributeTableEntry) tableEntry;
            Set<DataElementMetaInfo> dataElementMetaInfos = this.dataElementMetaInfos.get(attributeTableEntry.getTag());
            if (dataElementMetaInfos == null) {
                System.out.println("Invalid attribute tag: " + attributeTableEntry.getTag());
                return Collections.emptyList();
            }
            List<DataElementMetaInfo> attributeMetaInfos = new ArrayList<>();
            for (DataElementMetaInfo dataElementMetaInfo : dataElementMetaInfos) {
                AdditionalAttributeInfo additionalAttributeInfo = new AdditionalAttributeInfo(
                        attributeTableEntry.getName(),
                        attributeTableEntry.getType(),
                        attributeTableEntry.getAttributeDescription()
                );
                Context newContext = new Context(
                        context,
                        attributeTableEntry.getName(),
                        attributeTableEntry.getHref()
                );
                dataElementMetaInfo.addAdditionalAttributeInfoForContext(additionalAttributeInfo, newContext);
                for (TableEntry subTableEntry : attributeTableEntry.getSubTableEntries()) {
                    dataElementMetaInfo.addDataElementMetaInfo(resolveMacrosRecursively(
                            subTableEntry,
                            newContext
                    ));
                }
                attributeMetaInfos.add(dataElementMetaInfo);
            }

            return attributeMetaInfos;
        }
        return Collections.emptyList();
    }
    
    // Usually only one macro matches, but there is also the Functional Groups Macros which get combined to one special macro
    private Set<MacroTable> getMatchingMacroTables(String tableId) {
        Set<MacroTable> matchingMacroTables;
        if (FUNCTIONAL_GROUP_MACRO_REFERENCE.equals(tableId)) {
            matchingMacroTables = macroTables.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("table_C.7.6.16"))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toSet());
        } else {
            MacroTable matchingMacroTable = macroTables.get(tableId);
            if (matchingMacroTable == null) {
                matchingMacroTables = Collections.emptySet();
            } else {
                matchingMacroTables = Collections.singleton(matchingMacroTable);
            }
        }
        return matchingMacroTables;
    }

    private void removeHTMLTagsFromColumn(Integer i) {
        if (i == null || i >= this.columns.size()) {
            return;
        }
        this.columns.set(i, this.columns.get(i).replaceAll("</?[^>]*>", "").trim());
    }

    private void addTableEntry(final TableEntry currentTableEntry, int currentSequenceDepth) {
        while (currentSequenceDepth < sequenceAncestors.size()) {
            sequenceAncestors.remove(sequenceAncestors.size() - 1);
        }
        if (currentSequenceDepth > sequenceAncestors.size()) {
            System.out.println("Help");
        }
        if (currentSequenceDepth == 0) {
            if (currentModule != null) {
                currentModule.getTableEntries().add(currentTableEntry);
            } else if (currentMacro != null) {
                currentMacro.getTableEntries().add(currentTableEntry);
            }
        } else {
            sequenceAncestors.get(currentSequenceDepth - 1).getSubTableEntries().add(currentTableEntry);
        }
    }

    private void handleEndOfTableCell() {
        this.columns.add(getRecordedHTML().trim());
    }

    public Set<ModuleMetaInfo> getModules() {
        return modules;
    }

    public Set<InformationObjectDefinitionMetaInfo> getIODs() {
        return iods;
    }
}
