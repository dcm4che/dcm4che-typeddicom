package com.agfa.typeddicom;

import com.agfa.typeddicom.metamodel.*;
import com.agfa.typeddicom.table.*;
import com.agfa.typeddicom.utils.KeywordUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;

/**
 * TODO describe this class
 *
 * @author Niklas Roth (niklas.roth@agfa.com)
 */
public class DicomPart03Handler extends MemorizeTablesDicomPartHandler {
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
    private String currentSectionId;
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
        if ("section".equals(qName)) {
            this.currentSectionId = attributes.getValue("xml:id");
        } else if ("table".equals(qName)) {
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
        if ("section".equals(qName)) {
            this.currentSectionId = null;
        } else if ("caption".equals(qName)) {
            // check for table caption of module definitions
            String recordedText = getRecordedText();
            // ignore the weird stuff happening in http://dicom.nema.org/medical/dicom/current/output/html/part03.html#table_C.36.25-2
            // because it would ruin the module derived from http://dicom.nema.org/medical/dicom/current/output/html/part03.html#table_C.36.25-1
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
        return "http://dicom.nema.org/medical/dicom/current/output/html/part03.html";
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
                        table.getHref()
                );
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
            // don't remove if it is leave reference (happens at http://dicom.nema.org/medical/dicom/current/output/html/part03.html#sect_C.36.25)
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
        boolean validInclude = name.startsWith("Include") &&
                lastReferenceInFirstColumn != null &&
                lastReferenceInFirstColumn.startsWith("table_");
        boolean validAttribute = columns.size() > 1 && columns.get(1).matches("\\([0-9A-F]{2}[0-9A-Fx]{2},[0-9A-F]{2}[0-9A-Fx]{2}\\)");
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
        } else if (columns.size() == 2 && validInclude) {
            currentTableEntry = new MacroTableEntry(tableEntryHref, lastReferenceInFirstColumn, columns.get(1));
        } else if (columns.size() == 1 && validInclude) {
            currentTableEntry = new MacroTableEntry(tableEntryHref, lastReferenceInFirstColumn);
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
        String moduleRef = html.replaceAll("(?s).*?<a href=\"http://dicom.nema.org/medical/dicom/current/output/html/part03.html#([^\"]*)\">.*$", "$1");
        return moduleRef;
    }

    private Iterable<DataElementMetaInfo> resolveMacrosRecursively(TableEntry tableEntry, Context context) {
        if (tableEntry instanceof MacroTableEntry macroTableEntry) {
            MacroMetaInfo macroMetaInfo = macros.get(((MacroTableEntry) tableEntry).getTableId());
            if (macroMetaInfo == null) {
                MacroTable macroTable = macroTables.get(macroTableEntry.getTableId());
                if (macroTable == null) {
                    System.out.println("Invalid macro key: " + macroTableEntry.getTableId());
                    return Collections.emptyList();
                }
                macroMetaInfo = new MacroMetaInfo(macroTable.getTableId());
                macros.put(macroMetaInfo.getTableId(), macroMetaInfo);
                for (TableEntry macroSubEntry : macroTable.getTableEntries()) {
                    macroMetaInfo.addDataElementMetaInfo(resolveMacrosRecursively(
                            macroSubEntry,
                            new Context(context, macroTable.getName(), macroTable.getHref())
                    ));
                }
            }
            return macroMetaInfo.getSubDataElementMetaInfos();
        } else if (tableEntry instanceof AttributeTableEntry attributeTableEntry) {
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

    private void removeHTMLTagsFromColumn(Integer i) {
        if (i == null) {
            return;
        } else if (i >= this.columns.size()) {
            return;
        }
        this.columns.set(i, this.columns.get(i).replaceAll("</?[^>]*>", ""));
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
        this.columns.add(getRecordedHTML());
    }

    public Set<ModuleMetaInfo> getModules() {
        return modules;
    }

    public Set<InformationObjectDefinitionMetaInfo> getIODs() {
        return iods;
    }
}
