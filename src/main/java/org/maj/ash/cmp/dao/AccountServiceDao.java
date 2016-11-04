package org.maj.ash.cmp.dao;

import org.maj.ash.cmp.model.*;

import java.util.SortedSet;


/**
 * Created by shamikm78 on 10/21/16.
 */
public interface AccountServiceDao {
    /**
     *
     * PERSISTING POJOs
     *
     */
    Account saveAccount(Account account);
    Account retrieveAccount(Long accountId);

    MSAAccount saveMSAAccount(MSAAccount msaAccount);
    MSAAccount retrieveMSAAccount(Long msaAccountId);

    BusinessUnit saveBusinessUnit(BusinessUnit businessUnit);
    BusinessUnit retrieveBusinessUnit(Long businessUnitId);

    Product saveProduct(Product product);
    Product retrieveProduct(String productCode);

    Campaign saveCampaign(Campaign campaign);
    Campaign retrieveCampaign(String campaignId);



    /**
     *
     * RETRIEVING PRODUCTS OF A GIVEN ACCOUNT
     *
     */
    // products directly assigned to an account (via account object)
    SortedSet<Product> listAccountProducts(Account account);

    // products directly assigned to an account (via account id)
    SortedSet<Product> listAccountProducts(Long accountId);

    // entire product line in the account hierarchy (via account object)
    SortedSet<Product> listAccountProductsInHierarchy(Account account);


    // entire product live in the account hierarchy (via account id)
    SortedSet<Product> listAccountProductsInHierarchy(Long accountId);



    /**
     *
     * RETRIEVING CHILD ACCOUNTS OF A GIVEN ACCOUNT
     *
     */

    // list direct sub-accounts (via account object)
    SortedSet<Account> listAccounts(Account account);
    // list direct sub-accounts (via account id)
    SortedSet<Account> listAccounts(Long accountId);
    // list entire sub-account hierarchy under a parent account (via account object)
    SortedSet<Account> listAccountsInHierarchy(Account account);
    // list entire sub-account hierarchy under a parent account (via account Id)
    SortedSet<Account> listAccountsInHierarchy(Long accountId);



    /**
     *
     * RETRIEVING MARKETING CAMPAIGNS OF A GIVEN ACCOUNT
     *
     */
    // list marketing campaigns of products in a given account (via account object)
    SortedSet<Campaign> listAccountCampaigns(Account account);
    // list marketing campaigns of products in a given account (via account id)
    SortedSet<Campaign> listAccountCampaigns(Long accountId);
    // list marketing campaigns of products in a account hierarchy of a given account (via account object)
    SortedSet<Campaign> listAccountCampaignsInHierarchy(Account account);
    // list marketing campaigns of products in a account hierarchy of a given account (via account id)
    SortedSet<Campaign> listAccountCampaignsInHierarchy(Long accountId);

    /**
     *
     * RETRIEVING MARKETING CAMPAIGNS OF A GIVEN PRODUCT
     *
     */
    // list marketing campaigns of a given product (via product object)
    SortedSet<Campaign> listProductCampaigns(Product product);
    // list marketing campaigns of a given product (via product code)
    SortedSet<Campaign> listProductCampaigns(String productCode);
}
