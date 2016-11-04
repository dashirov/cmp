package org.maj.ash.cmp;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;
import org.maj.ash.cmp.dao.AccountServiceDao;
import org.maj.ash.cmp.dao.AccountServiceDaoGAEDS;
import org.maj.ash.cmp.model.Account;
import org.maj.ash.cmp.model.BusinessUnit;
import org.maj.ash.cmp.model.MSAAccount;
import org.maj.ash.cmp.model.Product;
import org.maj.ash.cmp.model.enums.ProductStatus;
import org.maj.ash.cmp.services.AccountService;

import java.util.Date;

/**
 * Created by shamikm78 on 10/21/16.
 */
public class AccountServiceTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    protected Closeable session;


    @BeforeClass
    public static void setUpBeforeClass() {
        // Reset the Factory so that all translators work properly.
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(Account.class);
        ObjectifyService.register(MSAAccount.class);
        ObjectifyService.register(BusinessUnit.class);
        ObjectifyService.register(Product.class);

    }

    @Before
    public void setUp() {
        this.session = ObjectifyService.begin();
        this.helper.setUp();
    }

    @After
    public void tearDown() {
        AsyncCacheFilter.complete();
        this.session.close();
        this.helper.tearDown();
    }

    @Test
    public void createAccount(){
        MSAAccount account = new MSAAccount();
        account.setName("Shamash, Inc.");
        account.setDescription("Shamash Master Service Agreement");
        AccountServiceDao accountServiceDao = new AccountServiceDaoGAEDS();
        accountServiceDao.saveAccount(account);
        Assert.assertNotNull(account.getId());
    }

    @Test
    public void createBsuinessUnit(){
        AccountServiceDao accountServiceDao = new AccountServiceDaoGAEDS();
        MSAAccount account = new MSAAccount();
        account.setName("Shamash, Inc.");
        account.setDescription("Shamash Master Service Agreement");
        accountServiceDao.saveMSAAccount(account);

        BusinessUnit subaccount = new BusinessUnit();

        subaccount.setName("Affiliate Networks");
        subaccount.setDescription("All activities related to affiliate acquisition marketing");
        // TODO: run in transaction
        subaccount.setParentAccount(account.getId());
        accountServiceDao.saveAccount(subaccount);
        account.addChildAccount(subaccount.getId());
        accountServiceDao.saveAccount(account);
        // end linkage transaction

    }

    /**
     * A product can be added to any account, either business unit or master service agreement per E.B.
     *
     */
    @Test
    public void addProductToAccount(){
        AccountServiceDao  accountServiceDao = new AccountServiceDaoGAEDS();
        // first create an MSA account
        MSAAccount  account = new MSAAccount();
        account.setName("Shamash, Inc.");
        account.setDescription("Shamash Master Service Agreement");
        // save to get an Id
        account = accountServiceDao.saveMSAAccount(account);

        // Then create a product
        Product product = new Product();
        product.setName("Gerbil Run iOS Free");
        product.setCode("GRF");
        product.setDescription("iOS game, free to end user with in-game purchases and feature unlock capabilities");
        product.setStatus(new Date(), ProductStatus.NEW);

        // link product with account
        product.setParentAccount(account.getId());
        account.addProduct(product.getCode());

        // Save changes
        accountServiceDao.saveAccount(account);
        accountServiceDao.saveProduct(product);

        // create a sub-account
        BusinessUnit someBusinessUnit = new BusinessUnit();
        someBusinessUnit.setParentAccount(account.getId());
        someBusinessUnit.setDescription("Mobile Applications - iOS and Android");
        someBusinessUnit.setName("Mobile");
        // save it
        accountServiceDao.saveAccount(someBusinessUnit);
        // link to parent
        account.addChildAccount(someBusinessUnit.getId());
        someBusinessUnit.setParentAccount(account.getId());
        // save changes
        accountServiceDao.saveAccount(account);
        accountServiceDao.saveAccount(someBusinessUnit);

        // move product from one account to another
        product.setParentAccount(someBusinessUnit.getId());
        someBusinessUnit.addProduct(product.getCode());
        account.removeProduct(product.getCode());

        // save changes
        accountServiceDao.saveAccount(account);
        accountServiceDao.saveAccount(someBusinessUnit);
        accountServiceDao.saveProduct(product);

        Assert.assertTrue("MSA lists all BU products products",
                accountServiceDao.getAllProducts(account).containsAll(accountServiceDao.getAllProducts(someBusinessUnit)));
        Assert.assertTrue("MSA object references no products directly",account.getProducts().isEmpty());

    }

    /**
    public void initBasics(){
        AccountService service = new AccountService();
        MSAAccount business = new MSAAccount();
        business.setName("My Business");
        business.setDescription("Description");
        service.initializeDeployment(business);
    }
    */

}
