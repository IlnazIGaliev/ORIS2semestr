package com.itis.oris.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Market {
    private Map<Product,Integer> products =  new HashMap<>();
    private List<Order> orders =  new ArrayList<>();
    private List<ImportProduct> imports  = new ArrayList<>();
}
