package org.maj.ash.cmp.controller;

import org.maj.ash.cmp.model.Account;
import org.maj.ash.cmp.model.MSAAccount;
import org.maj.ash.cmp.model.Product;
import org.maj.ash.cmp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.SortedSet;

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
        return accountService.initializeDeployment(msaAccount);
    }

    @RequestMapping(value="/account/{accountId}/products", method = RequestMethod.GET)
    @ResponseBody
    public SortedSet<Product> listAccountProducts(@PathVariable Long accountId){
        return accountService.listAccountProducts(accountId);
    }

    @RequestMapping(value="/account/{accountId}", method = RequestMethod.GET)
    @ResponseBody
    public Account getAccount(@PathVariable Long accountId){
        return accountService.retrieveAccount(accountId);
    }

    @RequestMapping(value = "/account/{accountId}/accounts", method = RequestMethod.GET)
    @ResponseBody
    public SortedSet<Account> listAccountSubAccounts(@PathVariable Long accountId){
        return accountService.listAccountSubAccounts(accountId);
    }
}
