package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.DatePrecision;

import java.util.Date;

public interface DateDataElementWrapper extends DataElementWrapper {
    default Date getDate() {
        return getAttributes().getDate(getTag());
    }

    default Date getDate(Date defaultValue) {
        return getAttributes().getDate(getTag(), defaultValue);
    }

    default Date getDate(Date defaultValue, DatePrecision datePrecision) {
        return getAttributes().getDate(getTag(), defaultValue, datePrecision);
    }

    default void setDate(Date date) {
        getAttributes().setDate(getTag(), getValueRepresentation(), date);
    }

    default void setDate(Date date, DatePrecision datePrecision) {
        getAttributes().setDate(getTag(), getValueRepresentation(), datePrecision, date);
    }

}
