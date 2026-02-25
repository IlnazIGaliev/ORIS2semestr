package com.itis.oris.component;

import com.itis.oris.model.Category;
import com.itis.oris.model.Order;
import com.itis.oris.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("App")
public class Application {

    private final MarketService service;

    public Application(MarketService service) {
        this.service = service;
    }

    public void run() {
        try {
            service.doOrder(new Order(
                    new Product(
                            "Computer",
                            "3418764173",
                            Category.PC,
                            BigDecimal.valueOf(50000)
                    ), 10, "Client №1"
            ));
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
