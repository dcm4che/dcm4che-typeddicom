package com.agfa.typeddicom.valuerepresentations;

import com.agfa.typeddicom.DataElementWrapper;
import org.dcm4che3.data.DatePrecision;
import org.dcm4che3.data.DatePrecisions;

import java.util.Date;

public interface DateDataElementMultiWrapper extends DataElementWrapper {
    default Date[] getDates() {
        return getAttributes().getDates(getTag());
    }

    default Date[] getDates(DatePrecisions datePrecisions) {
        return getAttributes().getDates(getTag(), datePrecisions);
    }

    default Date getDate(int index, Date defaultValue) {
        return getAttributes().getDate(getTag(), index, defaultValue);
    }

    default Date getDate(int index, Date defaultValue, DatePrecision datePrecision) {
        return getAttributes().getDate(getTag(), index, defaultValue, datePrecision);
    }

    default Date getDate(int index, DatePrecision datePrecision) {
        return getAttributes().getDate(getTag(), index, datePrecision);
    }

    default void setDates(Date[] dates) {
        getAttributes().setDate(getTag(), getValueRepresentation(), dates);
    }

    default void setDates(Date[] dates, DatePrecision datePrecision) {
        getAttributes().setDate(getTag(), getValueRepresentation(), datePrecision, dates);
    }
}
