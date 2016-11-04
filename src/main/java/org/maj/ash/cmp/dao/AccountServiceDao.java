package org.maj.ash.cmp.dao;

import org.maj.ash.cmp.model.Account;
import org.maj.ash.cmp.model.BusinessUnit;
import org.maj.ash.cmp.model.MSAAccount;
import org.maj.ash.cmp.model.Product;

import java.util.SortedSet;


/**
 * Created by shamikm78 on 10/21/16.
 */
public interface AccountServiceDao {
    Account saveAccount(Account account);
    Account retrieveAccount(Long accountId);

    MSAAccount saveMSAAccount(MSAAccount msaAccount);
    MSAAccount retrieveMSAAccount(Long msaAccountId);

    BusinessUnit saveBusinessUnit(BusinessUnit businessUnit);
    BusinessUnit retrieveBusinessUnit(Long businessUnitId);

    Product saveProduct(Product product);
    Product retrieveProduct(String productCode);

    // products directly assigned to an account (via account object)
    SortedSet<Product> getProducts(Account account);

    // products directly assigned to an account (via account id)
    SortedSet<Product> getProducts(Long accountId);

    // entire product live in the account hierarchy (via account object)
    SortedSet<Product> getAllProducts(Account account);


    // entire product live in the account hierarchy (via account id)
    SortedSet<Product> getAllProducts(Long accountId);

    SortedSet<Account> getSubAccounts(Account account);
    SortedSet<Account> getSubAccounts(Long accountId);


}
