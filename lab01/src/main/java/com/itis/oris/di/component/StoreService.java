package com.itis.oris.di.component;

import com.itis.oris.di.annotation.Component;
import com.itis.oris.di.model.Base;
import com.itis.oris.di.model.Fruit;
import com.itis.oris.di.model.FruitType;
import com.itis.oris.di.model.Store;

import java.util.List;

@Component
public class StoreService {
    private final Base base = new Base();

    public StoreService() {}

    public void add(String name) {
        base.getStores().add(new Store(name));
    }

    public void addFruit(Store store, Fruit fruit, Integer count) {
        store.getFruits().put(fruit, count);
    }

    public List<Store> getAll() {
        return base.getStores();
    }

    public Store findByName(String name) {
        return base.getStores().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public long getCountFruitByStore(String name, FruitType type) {
        return base.getStores().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElseThrow()
                .getFruits()
                .entrySet().stream().filter(e -> e.getKey().getType() == type)
                .count();
    }
}
