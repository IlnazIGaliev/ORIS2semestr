package com.itis.oris.config;

import com.itis.oris.component.MarketService;
import com.itis.oris.model.Market;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.itis.oris.component")
public class Config {

    @Bean
    public MarketService getService() {
        Market market = new Market();
        return new MarketService(market);
    }
}
