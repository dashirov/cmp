package org.maj.ash.cmp.dao;

import com.googlecode.objectify.Key;
import org.maj.ash.cmp.model.*;
import org.springframework.stereotype.Component;

import java.util.SortedSet;
import java.util.TreeSet;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by shamikm78 on 10/21/16.
 */
@Component
public class AccountServiceDaoGAEDS implements AccountServiceDao {


    @Override
    public Account saveAccount(Account account) {
        ofy().save().entity(account).now();
        return account;
    }

    @Override
    public Account retrieveAccount(Long accountId) {
        return  ofy().load().type(Account.class).id(accountId).now();
    }

    @Override
    public MSAAccount saveMSAAccount(MSAAccount msaAccount) {
        return (MSAAccount) saveAccount(msaAccount);
    }

    @Override
    public MSAAccount retrieveMSAAccount(Long msaAccountId) {
        return  ofy().load().type(MSAAccount.class).id(msaAccountId).now();
    }

    @Override
    public BusinessUnit saveBusinessUnit(BusinessUnit businessUnit) {
        return (BusinessUnit) saveAccount(businessUnit);
    }

    @Override
    public BusinessUnit retrieveBusinessUnit(Long businessUnitId) {
        return  ofy().load().type(BusinessUnit.class).id(businessUnitId).now();
    }

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
        ofy().save().entity(campaign).now();
        return campaign;
    }

    @Override
    public Campaign retrieveCampaign(String campaignId) {
        return ofy().load().type(Campaign.class).id(campaignId).now();
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


}
