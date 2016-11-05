package org.maj.ash.cmp.init;

import com.googlecode.objectify.ObjectifyService;
import org.maj.ash.cmp.App;
import org.maj.ash.cmp.model.*;
import org.maj.ash.cmp.model.stats.AcquisitionGuidance;
import org.maj.ash.cmp.model.stats.AcquisitionMeasurements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by shamikm78 on 10/21/16.
 */

@Component
public class OfyHelper implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void register() {
        LOGGER.info("Ofy: Registering POJO classes");
        ObjectifyService.register(Account.class);
        ObjectifyService.register(MSAAccount.class);
        ObjectifyService.register(BusinessUnit.class);
        ObjectifyService.register(Campaign.class);
        ObjectifyService.register(Marketplace.class);
        ObjectifyService.register(Product.class);
        ObjectifyService.register(AcquisitionGuidance.class);
        ObjectifyService.register(AcquisitionMeasurements.class);
        LOGGER.info("Ofy: All POJO classes registered");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // This will be invoked as part of a warmup request, or the first user
        // request if no warmup request was invoked.
        register();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}
