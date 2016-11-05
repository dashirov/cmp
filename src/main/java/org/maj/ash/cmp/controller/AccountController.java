package org.maj.ash.cmp.controller;

import org.maj.ash.cmp.model.*;
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

    @RequestMapping(value = "/account/{accountId}/addBusinessUnit", method = RequestMethod.POST)
    @ResponseBody
    public BusinessUnit addBusinessUnit(@PathVariable Long         accountId,
                                           @RequestBody  BusinessUnit businessUnit) {
        Account parent = accountService.retrieveAccount(accountId);
        return accountService.addBusinessUnit(parent, businessUnit);

    }

    @RequestMapping(value="/account/{accountId}/addProduct", method = RequestMethod.POST)
    @ResponseBody
    public Product addProduct(@PathVariable Long accountId,
                              @RequestBody Product product){
        Account parent = accountService.retrieveAccount(accountId);
        return accountService.addProduct(parent,product);
    }

    @RequestMapping(value = "/account/{accountId}/product/{productCode}/addCampaign", method=RequestMethod.POST)
    @ResponseBody
    public Campaign addCampaign(@PathVariable Long accountId,
                                @PathVariable String productCode,
                                @RequestBody Campaign campaign){
        // TODO: ask shamik if this is the right place to do this:
        Account parent = accountService.retrieveAccount(accountId);
        Product product = accountService.retrieveProduct(productCode);
        if (!(parent.getProducts().contains(productCode) && product.getParentAccount()==accountId))
            throw new IllegalArgumentException("Account " + accountId + "and Product "+productCode + "are not related");

        campaign.setProduct(productCode);
        product.addCampaign(campaign.getCode());

        accountService.saveProduct(product);
        return  accountService.saveCampaign(campaign);
    }

    @RequestMapping(value = "/marketplace/addMarketplace", method = RequestMethod.POST)
    @ResponseBody
    public Marketplace addMarketplace(@RequestBody Marketplace marketplace){
        return accountService.saveMarketplace(marketplace);
    }

    @RequestMapping(value = "/marketplace", method = RequestMethod.GET)
    @ResponseBody
    public SortedSet<Marketplace> listMarketplaces(){
        return accountService.listMarketplaces();
    }

    @RequestMapping(value = "/marketplace/{marketplaceId}", method = RequestMethod.GET)
    @ResponseBody
    public Marketplace getMarketplace(@PathVariable Long marketplaceId){
        return accountService.retrieveMarketplace(marketplaceId);
    }

    @RequestMapping(value = "/marketplace/{marketplaceId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Marketplace markMarketplaceForDeletion(@PathVariable Long markeplaceId){
        return accountService.markMarketplaceForTermination(markeplaceId);

    }

}
