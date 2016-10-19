package org.maj.ash.cmp.model;

/**
 * @author shamik.majumdar
 */

import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.maj.ash.cmp.model.enums.AccountType;


import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Account {
    @Id
    private Long id;
    private AccountType type;
    private String name;
    private String description;

    @Container
    private SortedSet<Long> childAccounts = new TreeSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SortedSet<Long> getChildAccounts() {
        return childAccounts;
    }

    public void addChildAccount(Long businessUnitId) {
        childAccounts.add(businessUnitId);
    }

}