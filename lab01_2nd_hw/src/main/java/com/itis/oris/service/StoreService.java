package com.itis.oris.service;

import org.springframework.stereotype.Service;

@Service
public class StoreService {

    public StoreService() {
        System.out.println("Store Service created");
    }

    public String getMessage() {
        return "Service works via Spring Context";
    }
}