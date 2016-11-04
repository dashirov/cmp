package org.maj.ash.cmp.model;

/**
 * Created by dashirov on 10/16/16.
 */

import com.googlecode.objectify.annotation.Subclass;
import org.maj.ash.cmp.model.enums.AccountType;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;


@Subclass(index=true)
public class MSAAccount extends Account {
    public MSAAccount() {
        super();
        this.setType(AccountType.MSA);
    }
}
