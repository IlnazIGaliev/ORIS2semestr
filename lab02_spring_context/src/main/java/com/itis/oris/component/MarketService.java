package com.itis.oris.component;

import com.itis.oris.model.ImportProduct;
import com.itis.oris.model.Market;
import com.itis.oris.model.Order;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

public class MarketService {

    private final Market market;

    public MarketService(Market market) {
        this.market = market;
    }

    public void doOrder(Order order){
        Integer count = market.getProducts().get(order.getProduct());
        if (count < order.getCount() || count == null) {
            throw new NoSuchElementException("Product not found");
        }

        market.getOrders().add(order);
        market.getProducts().put(order.getProduct(), --count);
    }

    public void doImport(ImportProduct importProduct){
        Integer count = market.getProducts().get(importProduct.getProduct());
        if (count == null) {
            count = 0;
        }

        market.getProducts().put(importProduct.getProduct(), count + importProduct.getCount());

        market.getImports().add(importProduct);
    }

    public void printProducts(){
        market.getProducts()
                .forEach((key, value) ->
                        System.out.println(key + ": " + value)
                );
    }
}
