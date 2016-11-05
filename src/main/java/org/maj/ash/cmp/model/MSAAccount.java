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

    // items below for customized communications with marketplaces / vendors / etc.
    private String mailingAddress;
    private String mainTelephone;
    private String mainFax;
    private String mainEmail;
    private Byte[] logo;

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getMainTelephone() {
        return mainTelephone;
    }

    public void setMainTelephone(String mainTelephone) {
        this.mainTelephone = mainTelephone;
    }

    public String getMainFax() {
        return mainFax;
    }

    public void setMainFax(String mainFax) {
        this.mainFax = mainFax;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public void setMainEmail(String mainEmail) {
        this.mainEmail = mainEmail;
    }

    public Byte[] getLogo() {
        return logo;
    }

    public void setLogo(Byte[] logo) {
        this.logo = logo;
    }

    public MSAAccount() {
        super();
        this.setType(AccountType.MSA);
    }
}
