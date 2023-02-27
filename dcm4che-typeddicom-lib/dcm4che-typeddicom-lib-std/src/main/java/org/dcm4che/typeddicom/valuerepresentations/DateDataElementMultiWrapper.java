package org.dcm4che.typeddicom.valuerepresentations;

import org.dcm4che.typeddicom.Builder;
import org.dcm4che.typeddicom.DataElementWrapper;
import org.dcm4che3.data.DatePrecision;
import org.dcm4che3.data.DatePrecisions;

import java.util.Date;

public interface DateDataElementMultiWrapper extends DataElementWrapper {
    default Date[] getDates() {
        return getAttributes().getDates(getPrivateCreator(), getTag());
    }

    default Date[] getDates(DatePrecisions datePrecisions) {
        return getAttributes().getDates(getPrivateCreator(), getTag(), datePrecisions);
    }

    default Date getDate(int index, Date defaultValue) {
        return getAttributes().getDate(getPrivateCreator(), getTag(), index, defaultValue);
    }

    default Date getDate(int index, Date defaultValue, DatePrecision datePrecision) {
        return getAttributes().getDate(getPrivateCreator(), getTag(), index, defaultValue, datePrecision);
    }

    default Date getDate(int index, DatePrecision datePrecision) {
        return getAttributes().getDate(getPrivateCreator(), getTag(), index, datePrecision);
    }

    default void setDates(Date... dates) {
        getAttributes().setDate(getPrivateCreator(), getTag(), getValueRepresentation(), dates);
    }

    default void setDates(DatePrecision datePrecision, Date... dates) {
        getAttributes().setDate(getPrivateCreator(), getTag(), getValueRepresentation(), datePrecision, dates);
    }

    interface Setter<B extends Builder<B, ?>, D extends DateDataElementMultiWrapper> extends org.dcm4che.typeddicom.Setter<B, D> {
        default B asDates(Date... dates) {
            getDataElementWrapper().setDates(dates);
            return getBuilder();
        }

        default B asDates(DatePrecision datePrecision, Date... dates) {
            getDataElementWrapper().setDates(datePrecision, dates);
            return getBuilder();
        }
    }
}
