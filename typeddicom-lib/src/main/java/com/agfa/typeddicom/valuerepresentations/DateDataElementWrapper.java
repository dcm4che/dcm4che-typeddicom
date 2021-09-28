package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.DatePrecision;

import java.util.Date;

public interface DateDataElementWrapper extends DataElementWrapper {
    default Date getDate(Date defaultValue) {
        return getDate(defaultValue, new DatePrecision());
    }

    default Date getDate(Date defaultValue, DatePrecision datePrecision) {
        return getValueRepresentation().toDate(getValue(), getTimeZone(), 0, false, defaultValue, datePrecision);
    }
}
