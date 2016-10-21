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
import org.maj.ash.cmp.model.MSAAccount;

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
    public void createMSAAccount(){
        MSAAccount account = new MSAAccount();
        account.setName("Shamik");
        account.setDescription("Test");
        AccountServiceDao accountServiceDao = new AccountServiceDaoGAEDS();
        accountServiceDao.saveMSAAccount(account);
    }


}
