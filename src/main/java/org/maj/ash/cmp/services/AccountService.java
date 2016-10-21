package org.maj.ash.cmp.services;

import org.maj.ash.cmp.dao.AccountServiceDao;
import org.maj.ash.cmp.model.MSAAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by shamikm78 on 10/21/16.
 */
@Component
public class AccountService {
    @Autowired
    private AccountServiceDao accountServiceDao;

    public MSAAccount createMSAAccount(MSAAccount msaAccount){
        return accountServiceDao.saveMSAAccount(msaAccount);
    }
}
