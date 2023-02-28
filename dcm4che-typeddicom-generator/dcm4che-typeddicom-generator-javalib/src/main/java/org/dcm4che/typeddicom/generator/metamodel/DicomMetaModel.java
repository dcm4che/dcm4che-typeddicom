package org.dcm4che.typeddicom.generator.metamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class DicomMetaModel {
    private final Set<ValueRepresentationMetaInfo> valueRepresentations = new HashSet<>();
    private final Set<DataElementMetaInfo> dataElements = new HashSet<>();
    private final Set<ModuleMetaInfo> modules = new HashSet<>();
    private final Set<InformationObjectDefinitionMetaInfo> iods = new HashSet<>();

    public Set<ValueRepresentationMetaInfo> getValueRepresentations() {
        return valueRepresentations;
    }

    public void addValueRepresentations(Collection<ValueRepresentationMetaInfo> valueRepresentations) {
        this.valueRepresentations.addAll(valueRepresentations);
    }

    public Set<DataElementMetaInfo> getDataElements() {
        return dataElements;
    }

    public void addDataElements(Collection<? extends DataElementMetaInfo> dataElements) {
        this.dataElements.addAll(dataElements);
    }

    public Set<ModuleMetaInfo> getModules() {
        return modules;
    }

    public void addModules(Collection<? extends ModuleMetaInfo> modules) {
        this.modules.addAll(modules);
    }

    public Set<InformationObjectDefinitionMetaInfo> getIods() {
        return iods;
    }


    public void addIods(Collection<? extends InformationObjectDefinitionMetaInfo> iods) {
        this.iods.addAll(iods);
    }

}
