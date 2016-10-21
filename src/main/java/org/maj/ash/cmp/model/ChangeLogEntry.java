package org.maj.ash.cmp.model;

import java.util.Date;

/**
 * Created by dashirov on 10/16/16.
 */
public class ChangeLogEntry<T> implements Comparable<ChangeLogEntry<T>>{
    public Date effectiveDate;
    public T value;

    public ChangeLogEntry() {
        super();
    }

    public ChangeLogEntry(Date effectiveDate, T value) {
        this.effectiveDate = effectiveDate;
        this.value = value;
    }

    @Override
    public int compareTo(ChangeLogEntry<T> other) {
        // reverse order to pick first from iterator / array / sorted list instead of last
        return other.effectiveDate.compareTo(this.effectiveDate);
    }

}
