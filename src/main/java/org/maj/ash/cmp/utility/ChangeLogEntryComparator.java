package org.maj.ash.cmp.utility;


import org.maj.ash.cmp.model.ChangeLogEntry;

import java.util.Comparator;

public class ChangeLogEntryComparator implements Comparator<ChangeLogEntry> {

	@Override
	public int compare(ChangeLogEntry o1, ChangeLogEntry o2) {
		return o2.effectiveDate.compareTo(o1.effectiveDate);
	}

}
