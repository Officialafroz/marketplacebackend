package com.elevata.marketplace;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

@Order(1)
public class ModularityTests {
    static ApplicationModules modules = ApplicationModules.of(MarketplaceApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }
}
