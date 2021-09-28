package com.agfa.typeddicom.valuerepresentations;

import org.dcm4che3.data.DatePrecision;
import org.dcm4che3.data.DatePrecisions;

import java.util.Date;

public interface DateDataElementMultiWrapper extends DataElementWrapper {
    default Date[] getDates() {
        return getDates(new DatePrecisions());
    }

    default Date[] getDates(DatePrecisions datePrecisions) {
        return getValueRepresentation().toDates(getValue(), getTimeZone(), false, datePrecisions);
    }

    default Date getDate(int index, Date defaultValue) {
        return getDate(index, defaultValue, new DatePrecision());
    }

    default Date getDate(int index, Date defaultValue, DatePrecision datePrecision) {
        return getValueRepresentation().toDate(getValue(), getTimeZone(), index, false, defaultValue, datePrecision);
    }
}
