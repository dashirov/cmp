package org.maj.ash.cmp.controller;

import org.maj.ash.cmp.model.MSAAccount;
import org.maj.ash.cmp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shamikm78 on 10/21/16.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rest")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/account/createMSA",method = RequestMethod.POST)
    @ResponseBody
    public MSAAccount createMSAAccount(@RequestBody MSAAccount msaAccount){
        return accountService.createMSAAccount(msaAccount);
    }
}
