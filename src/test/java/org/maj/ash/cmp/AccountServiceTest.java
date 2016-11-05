package org.maj.ash.cmp;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableMap;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;
import org.junit.*;
import org.maj.ash.cmp.dao.AccountServiceDao;
import org.maj.ash.cmp.dao.AccountServiceDaoGAEDS;
import org.maj.ash.cmp.model.*;
import org.maj.ash.cmp.model.enums.CampaignStatus;
import org.maj.ash.cmp.model.enums.CampaignType;
import org.maj.ash.cmp.model.enums.MarketplaceStatus;
import org.maj.ash.cmp.model.enums.ProductStatus;
import org.maj.ash.cmp.services.AccountService;
import org.yaml.snakeyaml.error.Mark;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.BiConsumer;

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
        ObjectifyService.register(Marketplace.class);
        ObjectifyService.register(Campaign.class);

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
                accountServiceDao.listAccountProductsInHierarchy(account).containsAll(accountServiceDao.listAccountProductsInHierarchy(someBusinessUnit)));
        Assert.assertTrue("MSA object references no products directly",account.getProducts().isEmpty());

    }


    /**
     * Test:
     * 1. create an MSA account
     * 2. Load 10-15 marketplaces
     * 3. Load 2-3 products
     * 4. create a few business units
     * 5. Move products around
     * 6. Create a few marketing campaigns
     * 7. Move products around again. Make sure campaigns move as well, products are discovered in the hierarchy... etc.
     *
     */

    @Test
    public void initBasics(){
        AccountServiceDao  accountServiceDao = new AccountServiceDaoGAEDS();
        AccountService service = new AccountService();
        service.setAccountServiceDao(accountServiceDao); // should have been autowired.

        MSAAccount business = new MSAAccount();
        business.setName("My Business");
        business.setDescription("Description");
        service.initializeDeployment(business);
        Assert.assertEquals("Expecting MSAAccount ID to be 1", 1L, business.getId().longValue());

        String[] semMarketplaces = {"Facebook", "Instagram", "Google", "Yahoo Gemini", "Yahoo Japan", "Twitter", "LinkedIn"};
        String[] affMArketplaces = {"Casale Media", "Media Crossing", "Yieldmo"};

        Map<String,Marketplace> marketplaces = new HashMap<>();

        for (String name : semMarketplaces
             ) {
            Marketplace m = new Marketplace();
            m.setName(name);
            m.setStatus(MarketplaceStatus.ACTIVE);

            Marketplace m1 = service.addMarketplace(m);
            Assert.assertEquals("Objects are synched clones", m, m1);

            if (name.equals("Yieldmo")) {
                m = service.markMarketplaceForTermination(m.getId());
                Assert.assertEquals("Expected status to be flipped to TERMINATED", m.getStatus(), MarketplaceStatus.TERMINATED);
            }
            marketplaces.put(m.getName(),m);
        }
        Map<String,String> products =     ImmutableMap.of("CCF", "Canned Cat Food",
                                                          "CDF", "Canned Dog Food",
                                                          "ENS", "Ensure");

        products.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String productCode, String name) {
                Product p = new Product();
                p.setCode(productCode);
                p.setName(name);
                Product p1 = service.addProduct(business,p);
                Assert.assertEquals("p1 and p are clones of each other", p1, p);
                Assert.assertEquals("product is linked upto its target parent account", p.getParentAccount(),business.getId());
                Assert.assertTrue("parent account owns the product",business.getProducts().contains(p.getCode()));

            }
        });

        Map<String,BusinessUnit> bus = new HashMap<>();
        String[] businessUnits = {"Pet owner markets","Geriatric markets"};
        for (String name: businessUnits
             ) {
            BusinessUnit bu = new BusinessUnit();
            bu.setName(name);
            service.addBusinessUnit(business,bu);
            Assert.assertEquals("business unit is linked up to its parent account", bu.getParentAccount(), business.getId());
            Assert.assertTrue("MSA account lists business unit as a child account", business.getChildAccounts().contains(bu.getId()));
            bus.put(name,bu); // for future horseplay
        }

        // Not a service test
        Assert.assertTrue("business owns 3 specific products, directly", business.getProducts().containsAll(products.keySet()));
        // Service test
        SortedSet<Product> prd = service.listAccountProducts(business);
        Assert.assertEquals("business owns 3 products",3,prd.size());
        for (Product p: prd
             ) {
            Assert.assertTrue("business owns exacty the products we created", products.containsKey(p.getCode()));
            String name = products.get(p.getCode());
            Assert.assertEquals("Names in and out match", name, p.getName() );
        }

        service.moveProduct(bus.get("Pet owner markets"), service.retrieveProduct("CCF"));
        service.moveProduct(service.retrieveAccount(bus.get("Pet owner markets").getId()), service.retrieveProduct("CDF"));
        service.moveProduct(bus.get("Geriatric markets"), service.retrieveProduct("ENS"));

        prd = service.listAccountProducts(business);
        Assert.assertTrue(prd.isEmpty());

        prd = service.listAccountProductsInHierarchy(business);
        Assert.assertEquals(3,prd.size());

        BusinessUnit bu1 = bus.get("Pet owner markets");
        BusinessUnit bu2 = bus.get("Geriatric markets");

        // Traversal by Long ID
        Assert.assertEquals(2,service.listAccountProductsInHierarchy(bu1.getId()).size());
        // Traversal by BusinessUnit object
        Assert.assertEquals(1,service.listAccountProductsInHierarchy(bu2).size());
        // Tip by Long ID
        Assert.assertEquals(2,service.listAccountProducts(bu1.getId()).size());
        // Tip by BusinessUnit object
        Assert.assertEquals(1,service.listAccountProducts(bu2).size());


        Marketplace mGoogle = marketplaces.get("Google");
        Product pCCF = service.retrieveProduct("CCF");
        Campaign cCCFdrm001 = new Campaign();
        cCCFdrm001.setProduct(pCCF.getCode());
        cCCFdrm001.setMarketplace(mGoogle.getId());
        cCCFdrm001.setStatus(new Date(), CampaignStatus.NEW);
        cCCFdrm001.setType(CampaignType.CPC);
        cCCFdrm001.setDescription("Google | Canned Cat Food | US | Search Keywords");
        cCCFdrm001.setCode(cCCFdrm001.getProduct() + "^drm001");
        service.addCampaign(pCCF,cCCFdrm001); // addCampaign will link product to campaign, saveCampaign won't
        Assert.assertEquals(pCCF.getCode(), cCCFdrm001.getProduct()); // this may fail in the cloud...
        Assert.assertTrue(pCCF.getCampaigns().contains(cCCFdrm001.getCode()));
        cCCFdrm001.setStatus(new Date(),CampaignStatus.ACTIVE);
        service.saveCampaign(cCCFdrm001);



        Product dogfood = service.retrieveProduct("CDF");
        Campaign anotherCampaign = new Campaign();
        anotherCampaign.setProduct(dogfood.getCode());
        anotherCampaign.setMarketplace(marketplaces.get("Facebook").getId());
        anotherCampaign.setType(CampaignType.CPC);
        anotherCampaign.setStatus(new Date(),CampaignStatus.NEW);
        anotherCampaign.setDescription("Dogfood on Russian market via Facebook search");
        // Not setting product service should take care of that. Can we not set the code in the future?
        anotherCampaign.setCode("CDF^drm001");
        service.addCampaign(dogfood,anotherCampaign);

        Assert.assertEquals(anotherCampaign.getProduct(),dogfood.getCode());
        Assert.assertTrue(dogfood.getCampaigns().contains(anotherCampaign.getCode()));


        BusinessUnit eCommerce = new BusinessUnit();
        eCommerce.setName("eCommerece");
        eCommerce.setDescription("Online sales");
        service.addBusinessUnit(bu1,eCommerce);

        Assert.assertEquals(eCommerce.getParentAccount(),bu1.getId());
        Assert.assertNotNull(eCommerce.getId());
        Assert.assertTrue(bu1.getChildAccounts().contains(eCommerce.getId()));


        service.moveProduct(eCommerce,pCCF);
        service.moveProduct(eCommerce,dogfood);

        Assert.assertEquals(3,service.listAccountProductsInHierarchy(business).size());
        Assert.assertEquals(3,service.listAccountProductsInHierarchy(business).size());
        Assert.assertEquals(2,service.listAccountProductsInHierarchy(bu1).size());
        Assert.assertEquals(2,service.listAccountProducts(eCommerce).size());

        Assert.assertEquals(2,service.listAccountCampaigns(eCommerce).size());
        Assert.assertEquals(2,service.listAccountCampaignsInHierarchy(1L).size());
        Assert.assertEquals(2,service.listAccountCampaignsInHierarchy(business).size());

        Assert.assertEquals(3,service.listAccountsInHierarchy(1L).size());
        Assert.assertEquals(3,service.listAccountsInHierarchy(business).size());
        Assert.assertEquals(2,service.listAccounts(business).size());
        Assert.assertEquals(2,service.listAccounts(1L).size());







    }





}
