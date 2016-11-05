package org.maj.ash.cmp.services;

import org.maj.ash.cmp.dao.AccountServiceDao;
import org.maj.ash.cmp.model.*;
import org.maj.ash.cmp.model.enums.ProductStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.SortedSet;

/**
 * Created by shamikm78 on 10/21/16.
 */
@Component
public class AccountService {
    @Autowired
    private AccountServiceDao accountServiceDao;

    // TODO: Shamik, I need this for testing until I figure out how to debug autowired shit
    public void setAccountServiceDao(AccountServiceDao accountServiceDao) {
        this.accountServiceDao = accountServiceDao;
    }


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

        if (product.getStatusChangeLog().getChangeLogs().size()==0)
            product.setStatus(new Date(), ProductStatus.NEW);

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

    public SortedSet<Product> listAccountProductsInHierarchy(Account account){
        return accountServiceDao.listAccountProductsInHierarchy(account);
    }

    public SortedSet<Product> listAccountProductsInHierarchy(Long accountId){
        return listAccountProductsInHierarchy(accountServiceDao.retrieveAccount(accountId));
    }

    public Account retrieveAccount(Long accountId){
        return accountServiceDao.retrieveAccount(accountId);
    }

    public SortedSet<Account> listAccounts(Long accountId){
        return accountServiceDao.listAccounts(accountId);
    }

    public SortedSet<Account> listAccounts(Account account){
        return accountServiceDao.listAccounts(account);
    }

    public SortedSet<Account> listAccountsInHierarchy(Long accountId){
        return accountServiceDao.listAccountsInHierarchy(accountId);
    }

    public SortedSet<Account> listAccountsInHierarchy(Account account){
        return accountServiceDao.listAccountsInHierarchy(account);
    }

    public SortedSet<Campaign> listAccountCampaigns(Account account){
        return accountServiceDao.listAccountCampaigns(account);
    }

    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Account account){
        return accountServiceDao.listAccountCampaignsInHierarchy(account);
    }

    public SortedSet<Campaign> listAccountCampaigns(Long accountId){
        return listAccountCampaigns(accountServiceDao.retrieveAccount(accountId));
    }

    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Long accountId){
        return listAccountCampaignsInHierarchy(accountServiceDao.retrieveAccount(accountId));
    }

    public Campaign retrieveCampaign(String campaignCode){
        return accountServiceDao.retrieveCampaign(campaignCode);
    }

    public Campaign addCampaign(Product product, Campaign campaign ){
        if (!campaign.getCode().startsWith(product.getCode()))
            throw new IllegalArgumentException("By convention, campaign code start with product code");

        product.addCampaign(campaign.getCode());
        campaign.setProduct(product.getCode());
        accountServiceDao.saveCampaign(campaign);
        accountServiceDao.saveProduct(product);
        return campaign;
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


    public Marketplace addMarketplace(Marketplace marketplace){
        // TODO: check for duplicate names?
        return accountServiceDao.saveMarketplace(marketplace);
    }

    public Marketplace saveMarketplace(Marketplace marketplace){
        return accountServiceDao.saveMarketplace(marketplace);
    }

    public SortedSet<Marketplace> listMarketplaces(){
        return accountServiceDao.listMarketplaces();
    }

    public Marketplace retrieveMarketplace(Long marketplaceId){
        return accountServiceDao.retrieveMarketplace(marketplaceId);
    }

    public Marketplace markMarketplaceForTermination(Long marketplaceId){
        return accountServiceDao.markMarketplaceForTermination(marketplaceId);
    }
}
