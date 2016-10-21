package org.maj.ash.cmp.dao;

import org.maj.ash.cmp.model.MSAAccount;
import org.springframework.stereotype.Component;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by shamikm78 on 10/21/16.
 */
@Component
public class AccountServiceDaoGAEDS implements AccountServiceDao {


    @Override
    public MSAAccount saveMSAAccount(MSAAccount account) {
        ofy().save().entity(account).now();
        return account;
    }
}
