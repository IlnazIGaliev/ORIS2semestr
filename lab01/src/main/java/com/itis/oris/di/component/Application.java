package com.itis.oris.di.component;

import com.itis.oris.di.annotation.Component;
import com.itis.oris.di.model.Fruit;
import com.itis.oris.di.model.FruitType;
import com.itis.oris.di.model.Store;

@Component
public class Application {

    //@Inject
    private StoreService service;

    public Application(StoreService service) {
        this.service = service;
        System.out.println("StoreService добавлен");
    }

    public void run() {

        service.add("I");
        service.add("II");

        Store storeI = service.findByName("I");
        service.addFruit(storeI, new Fruit("Яблоко", FruitType.APPLE), 1000);
        service.addFruit(storeI, new Fruit("Бананы", FruitType.BANANA), 2000);

        Store storeII = service.findByName("II");
        service.addFruit(storeI, new Fruit("Яблоко", FruitType.APPLE), 3000);
        service.addFruit(storeI, new Fruit("Апельсины", FruitType.ORANGE), 2000);

        service.getAll().forEach(System.out::println);
    }

}
