package org.maj.ash.cmp;

import com.googlecode.objectify.ObjectifyService;
import org.maj.ash.cmp.config.AppConfig;
import org.maj.ash.cmp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry Point
 *
 */
@Import(AppConfig.class)
@EnableScheduling
public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Value("${app.name}")
    public String name;

    public void run(String... args) throws Exception {
        LOGGER.info("Starting up {}", name);
        resister();
    }

    private void resister() {
        LOGGER.info("Registering");
        ObjectifyService.register(MSAAccount.class);
        ObjectifyService.register(BusinessUnit.class);
        ObjectifyService.register(Campaign.class);
        ObjectifyService.register(Marketplace.class);
        ObjectifyService.register(MSAAccount.class);
        ObjectifyService.register(Product.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
