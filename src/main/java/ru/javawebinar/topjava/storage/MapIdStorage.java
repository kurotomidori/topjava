package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapIdStorage implements Storage {
    private final ConcurrentMap<String, Meal> storage = new ConcurrentHashMap<>();

    public MapIdStorage() {
        for (Meal m : MealsUtil.meals) {
            this.add(m);        }

    }

    @Override
    public synchronized void add(Meal m) {
        m.setId(UUID.randomUUID().toString());
        storage.put(m.getId(), m);
    }

    @Override
    public synchronized void delete(String id) {
        storage.remove(id);
    }

    @Override
    public synchronized void update(Meal m) {
        storage.put(m.getId(), m);
    }

    @Override
    public synchronized Meal get(String id){
        return storage.get(id);
    }

    @Override
    public synchronized List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
