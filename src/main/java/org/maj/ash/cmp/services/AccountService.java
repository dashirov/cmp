package org.maj.ash.cmp.services;

import org.maj.ash.cmp.dao.AccountServiceDao;
import org.maj.ash.cmp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.SortedSet;

/**
 * Created by shamikm78 on 10/21/16.
 */
@Component
public class AccountService {
    @Autowired
    private AccountServiceDao accountServiceDao;

    /**
     * System deployed to a client needs to be initialized with client information first.
     * @param business
     * @return
     */
    public MSAAccount initializeDeployment(MSAAccount business) {
        if (business.getId() == null)
          return  accountServiceDao.saveMSAAccount(business);
        else
            throw new IllegalArgumentException("Business " + business.getName() + " has already been initialized");
    }

    /**
     * Subsequently MSA account can be modified, as long as its identifier is not changed all other mods are allowed
     * @param business
     * @return
     */
    public MSAAccount saveMSAAccount(MSAAccount business){
        if (business.getId() != null)
            return accountServiceDao.saveMSAAccount(business);
        else
            throw new IllegalArgumentException("Business " + business.getName() + " has to pass its ID to be saved");
    }

    /**
     * Any account can be subjugated to another to form a hierarchy of any sort. MSA account is always at its root.
     * @param parent
     * @param businessUnit
     */
    public BusinessUnit addBusinessUnit(Account parent, BusinessUnit businessUnit){
        if (businessUnit.getId()==null)
           accountServiceDao.saveBusinessUnit(businessUnit);
        if (parent.getId() == null)
            throw new IllegalArgumentException("Parent account must pass its ID");

        parent.addChildAccount(businessUnit.getId());
        businessUnit.setParentAccount(parent.getId());

        accountServiceDao.saveBusinessUnit(businessUnit);
        accountServiceDao.saveAccount(parent);

        return businessUnit;

    }

    /**
     * New products can be added to any account at any point in the account hierarchy, including MSA account at the root
     * @param parent
     * @param product
     */
    public Product addProduct(Account parent, Product product){
        if (product.getCode() == null)
           accountServiceDao.saveProduct(product);
        product.setParentAccount(parent.getId());
        parent.addProduct(product.getCode());

        accountServiceDao.saveAccount(parent);
        accountServiceDao.saveProduct(product);
        return product;
    }

    /**
     * Product can be moved from one account to another after they were created
     * @param parent
     * @param product
     */
    public void moveProduct(Account parent, Product product){
        if (parent.getId() == null)
            throw new IllegalArgumentException("Parent account must have an ID");
        Account currentParent = accountServiceDao.retrieveAccount(product.getParentAccount());
        currentParent.removeProduct(product.getCode());
        parent.addProduct(product.getCode());
        product.setParentAccount(parent.getId());
        accountServiceDao.saveAccount(currentParent);
        accountServiceDao.saveAccount(parent);
        accountServiceDao.saveProduct(product);
    }

    /**
     * sub-accounts can be moved around
     * @param parent
     * @param businessUnit
     */
    public void moveBusinessUnit(Account parent, BusinessUnit businessUnit){
        if (parent.getId()==null)
            throw new IllegalArgumentException("Parent account must have an ID");

        Account currentParent = accountServiceDao.retrieveAccount(businessUnit.getParentAccount());
        if (currentParent != null)
            currentParent.removeChildAccount(businessUnit.getId());

        parent.addChildAccount(businessUnit.getId());
        businessUnit.setParentAccount(parent.getId());
        accountServiceDao.saveAccount(currentParent);
        accountServiceDao.saveAccount(parent);
        accountServiceDao.saveAccount(businessUnit);
    }

    public SortedSet<Product> listAccountProducts(Account account){
        return accountServiceDao.listAccountProducts(account);
    }

    public SortedSet<Product> listAccountProducts(Long accountId){
        return listAccountProducts(accountServiceDao.retrieveAccount(accountId));
    }

    public Account retrieveAccount(Long accountId){
        return accountServiceDao.retrieveAccount(accountId);
    }

    public SortedSet<Account> listAccountSubAccounts(Long accountId){
        return accountServiceDao.listAccounts(accountId);
    }

    public SortedSet<Campaign> listAccountCampaigns(Long accountId){
        return accountServiceDao.listAccountCampaigns(accountId);
    }

    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Long accountId){
        return accountServiceDao.listAccountCampaignsInHierarchy(accountId);
    }

    public Campaign retrieveCampaign(String campaignCode){
        return accountServiceDao.retrieveCampaign(campaignCode);
    }
    public Campaign saveCampaign(Campaign campaign){
        return accountServiceDao.saveCampaign(campaign);
    }

    public Product retrieveProduct(String productCode){
        return accountServiceDao.retrieveProduct(productCode);
    }
    public Product saveProduct(Product product){
        return accountServiceDao.saveProduct(product);
    }
}
