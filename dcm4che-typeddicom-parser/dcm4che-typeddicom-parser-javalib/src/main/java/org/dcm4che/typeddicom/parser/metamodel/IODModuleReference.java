package org.dcm4che.typeddicom.parser.metamodel;

import java.util.Objects;

public final class IODModuleReference {
    private final ModuleMetaInfo moduleMetaInfo;

    public IODModuleReference(ModuleMetaInfo moduleMetaInfo) {
        this.moduleMetaInfo = moduleMetaInfo;
    }

    public ModuleMetaInfo moduleMetaInfo() {
        return moduleMetaInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (IODModuleReference) obj;
        return Objects.equals(this.moduleMetaInfo, that.moduleMetaInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleMetaInfo);
    }

    @Override
    public String toString() {
        return "IODModuleReference[" +
                "moduleMetaInfo=" + moduleMetaInfo + ']';
    }

}
