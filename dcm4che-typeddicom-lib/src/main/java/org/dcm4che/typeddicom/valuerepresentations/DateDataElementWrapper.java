package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;
import org.dcm4che3.data.DatePrecision;

import java.util.Date;

public interface DateDataElementWrapper extends DataElementWrapper {
    default Date getDate() {
        return getAttributes().getDate(getPrivateCreator(), getTag());
    }

    default Date getDate(Date defaultValue) {
        return getAttributes().getDate(getPrivateCreator(), getTag(), defaultValue);
    }

    default Date getDate(Date defaultValue, DatePrecision datePrecision) {
        return getAttributes().getDate(getPrivateCreator(), getTag(), defaultValue, datePrecision);
    }

    default void setDate(Date date) {
        getAttributes().setDate(getPrivateCreator(), getTag(), getValueRepresentation(), date);
    }

    default void setDate(Date date, DatePrecision datePrecision) {
        getAttributes().setDate(getPrivateCreator(), getTag(), getValueRepresentation(), datePrecision, date);
    }

    interface Setter<B extends Builder<B, ?>, D extends DateDataElementWrapper> extends org.dcm4che.typeddicom.Setter<B, D> {
        default B asDate(Date date) {
            getDataElementWrapper().setDate(date);
            return getBuilder();
        }
        
        default B asDate(Date date, DatePrecision datePrecision) {
            getDataElementWrapper().setDate(date, datePrecision);
            return getBuilder();
        }
    }
}
