package org.maj.ash.cmp.utility;


import org.maj.ash.cmp.model.Account;

import java.util.Comparator;

public class AccountComparator implements Comparator<Account> {

	@Override
	public int compare(Account o1, Account o2) {
		return o2.getId().compareTo(o1.getId());
	}

}
