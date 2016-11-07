package org.maj.ash.cmp.dao;

import com.googlecode.objectify.Key;
import org.maj.ash.cmp.model.*;
import org.maj.ash.cmp.model.enums.MarketplaceStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by shamikm78 on 10/21/16.
 */
@Component
@Profile("gae")
public class AccountServiceDaoGAEDS implements AccountServiceDao {
    /**
     * Given an acount POJO, simply save it. Account is either MSA or BU account.
     * @param account
     * Account ( or any subclasses i.e. MSAAccount or BusinessUnit ) POJO. With or without ID. If Id is not passed, a new
     * database record will be made. If passed, existing record will be updated.
     * @return
     * returns the object updated aand synched to data store. If a new was created, it will contain auto key (Id)
     */
    @Override
    public Account saveAccount(Account account) {
        ofy().save().entity(account).now();
        return account;
    }

    /**
     * Given an account Id, load the POJO from the data store
     * @param accountId
     * Long account Id
     * @return
     * Account POJO
     */
    @Override
    public Account retrieveAccount(Long accountId) {
        return  ofy().load().type(Account.class).id(accountId).now();
    }

    /**
     * Instead of using Account Superclass, save operation on the MSAAccount subclass
     * @param msaAccount
     * MSAAccount POJO (with or without id). If id wasn't in the data store or if one wasn't supplied, a new record will
     * be created, otherwise existing record will be updated
     * @return
     */
    @Override
    public MSAAccount saveMSAAccount(MSAAccount msaAccount) {
        return (MSAAccount) saveAccount(msaAccount);
    }

    /**
     * Retrieve operation on MSAAccount
     * @param msaAccountId
     * Long MSAAccount Id
     * @return
     * MSAAccount POJO loaded from the backend datastore
     */
    @Override
    public MSAAccount retrieveMSAAccount(Long msaAccountId) {
        return  ofy().load().type(MSAAccount.class).id(msaAccountId).now();
    }

    /**
     * Same as Account and MSAAccount, persist BusinessUnit object in the data store backend
     * @param businessUnit
     * BusinessUnit POJO (with or without Id). If Id wasn't supplied or found inthe data store backend, a new object will
     * be placed into the data store, otherwise existing record will be updated
     * @return
     * Updated BusinessUnit POJO
     */
    @Override
    public BusinessUnit saveBusinessUnit(BusinessUnit businessUnit) {
        return (BusinessUnit) saveAccount(businessUnit);
    }

    /**
     * Retrieve BusinessUnit from the data store
     * @param businessUnitId
     * Long BusinessUnit Id
     * @return
     * BusinessUnit POJO loaded from the backend data store
     */
    @Override
    public BusinessUnit retrieveBusinessUnit(Long businessUnitId) {
        return  ofy().load().type(BusinessUnit.class).id(businessUnitId).now();
    }

    /**
     * Persist product POJO in the backend data store. String productCode IS REQUIRED.
     * @param product
     * Product POJO to persist. If record already exists it will be updated, otherwise new record will be created
     * @return
     * Product POJO synched with the backend data store
     */
    @Override
    public  Product saveProduct(Product product){
        Key productKey=ofy().save().entity(product).now();
        return product;
    }

    /**
     * Given a product code, pull its object from datastore
     * @param productCode
     * @return
     */
    @Override
    public Product retrieveProduct(String productCode) {
        return ofy().load().type(Product.class).id(productCode).now();
    }

    @Override
    public Campaign saveCampaign(Campaign campaign) {
        if (campaign.getCode() == null)
            throw new IllegalArgumentException("Campaign requires uniqeue campaign code");
        if (campaign.getMarketplace() == null)
            throw new IllegalArgumentException("Campaign must be associated with a marketplace");
        else {
            Marketplace marketplace = ofy().load().type(Marketplace.class).id(campaign.getMarketplace()).now();
            if (marketplace == null)
                throw new IllegalArgumentException("No such marketplace, register it first.");
        }

        if (campaign.getProduct() == null)
            throw new IllegalArgumentException("Campaign must be associated with a product");
        else {
            Product product = ofy().load().type(Product.class).id(campaign.getProduct()).now();
            if (product==null)
                throw new IllegalArgumentException("No such product, register it first.");
        }

        if (campaign.getStatusChangeLog() == null || campaign.getStatusChangeLog().getChangeLogs().size()==0)
            throw new IllegalArgumentException("Campaign status changelog not initialized");

        ofy().save().entity(campaign).now();
        return campaign;
    }

    @Override
    public Campaign retrieveCampaign(String campaignId) {
        return ofy().load().type(Campaign.class).id(campaignId).now();
    }

    @Override
    public Marketplace saveMarketplace(Marketplace marketplace) {
        ofy().save().entity(marketplace).now();
        return marketplace;
    }

    @Override
    public Marketplace retrieveMarketplace(Long marketplaceId) {
        return ofy().load().type(Marketplace.class).id(marketplaceId).now();
    }

    /**
     * Given an account id, pull its object from datastore and then pull its products as a sorted list
     * @param accountId
     * @return
     */
    @Override
    public SortedSet<Product> listAccountProducts(Long accountId){
        return listAccountProducts(retrieveAccount(accountId));
    }

    /**
     * Given an account pull its products from the data store as a sorted list
     * @param account
     * @return
     */
    public SortedSet<Product> listAccountProducts(Account account){
        return new TreeSet<>( ofy().load().type(Product.class).ids(account.getProducts().toArray(new String[account.getProducts().size()])).values());
    }
    /**
     * Given an account pull its own products as well as any sub-account products from the data store as a sorted list
     * @param account
     * @return
     */
    @Override
    public SortedSet<Product> listAccountProductsInHierarchy(Account account) {
        SortedSet<Product> out = listAccountProducts(account);
        for (Account a: listAccountsInHierarchy(account)
             ) {
            out.addAll(listAccountProducts(a));
        }
        return out;
    }

    public SortedSet<Product> listAccountProductsInHierarchy(Long accountId){
        return listAccountProductsInHierarchy(retrieveAccount(accountId));
    }
    /**
     * Given an account Id, pull its direct sub-accounts as a sorted set
     * @param accountId
     * @return
     */
    @Override
    public SortedSet<Account> listAccounts(Long accountId) {
        return listAccounts(retrieveAccount(accountId));
    }

    /**
     * Given a parent account return its direct sub-accounts as a sorted set
     * @param account
     * @return
     */
    @Override
    public SortedSet<Account> listAccounts(Account account) {
        return new TreeSet<>( ofy().load().type(Account.class).ids(account.getChildAccounts().toArray(new Long[account.getChildAccounts().size()])).values());
    }

    /**
     * Pull account by Id and get its entire sub-account hierarchy as a sorted set
     * @param accountId
     * @return
     */
    public SortedSet<Account> listAccountsInHierarchy(Long accountId) {
        return listAccountsInHierarchy(retrieveAccount(accountId));
    }

    @Override
    public SortedSet<Campaign> listAccountCampaigns(Account account) {
        // account -> products ->> campaigns
        SortedSet<Campaign> out = new TreeSet<>();
        for (Product product: listAccountProducts(account)
             ) {
            out.addAll(listProductCampaigns(product));
        }
        return out;
    }

    @Override
    public SortedSet<Campaign> listAccountCampaigns(Long accountId) {
        return listAccountCampaigns(retrieveAccount(accountId));
    }

    @Override
    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Account account) {
        SortedSet<Campaign> out = new TreeSet<>(listAccountCampaigns(account));
        for (Account a: listAccountsInHierarchy(account)
             ) {
            out.addAll(listAccountCampaigns(a));
        }
        return out;
    }

    @Override
    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Long accountId) {
        return listAccountCampaignsInHierarchy(retrieveAccount(accountId));
    }

    @Override
    public SortedSet<Campaign> listProductCampaigns(Product product) {
        return new TreeSet<>( ofy().load().type(Campaign.class).ids(product.getCampaigns().toArray(new String[product.getCampaigns().size()])).values());
    }

    @Override
    public SortedSet<Campaign> listProductCampaigns(String productCode) {
        return listProductCampaigns(ofy().load().type(Product.class).id(productCode).now());
    }

    /**
     * Given an account, pull its entire sub-account hierarchy as a single sorted set - asynchronously and recursively
     * @param account
     * @return
     */
    public SortedSet<Account> listAccountsInHierarchy(Account account){
        SortedSet<Account> out = new TreeSet<>();
        for (Account a: ofy().load().type(Account.class).ids(account.getChildAccounts().toArray(new Long[account.getChildAccounts().size()])).values()
             ) {
            out.add(a);
            out.addAll(listAccountsInHierarchy(a));
        }
        return out;
    }

    public SortedSet<Marketplace> listMarketplaces(){
        SortedSet<Marketplace> out = new TreeSet<>();
        Iterable<Marketplace> vendors=ofy().load().type(Marketplace.class);
        for (Marketplace vendor: vendors
             ) {
            out.add(vendor);
        }
        return out;
    }

    public Marketplace markMarketplaceForTermination(Long marketplaceId){
        Marketplace marketplace = ofy().load().type(Marketplace.class).id(marketplaceId).now();
        return markMarketplaceForTermination(marketplace);
    }

    public Marketplace markMarketplaceForTermination(Marketplace marketplace){
        marketplace.setStatus(MarketplaceStatus.TERMINATED);
        saveMarketplace(marketplace);
        return marketplace;
    }

    public Marketplace markMarketplaceForTermination(Date effectiveDate, Marketplace marketplace){
        marketplace.setStatus(effectiveDate,MarketplaceStatus.TERMINATED);
        saveMarketplace(marketplace);
        return marketplace;
    }


}
