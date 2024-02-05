package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MealMemoryStorage implements Storage<Meal> {

    private final ConcurrentMap<Integer, Meal> storage = new ConcurrentHashMap<>();

    private Integer counter = 0;

    public MealMemoryStorage() {
        for (Meal m : MealsUtil.meals) {
            this.add(m);
        }
    }

    @Override
    public synchronized Meal add(Meal m) {
        m.setId(counter++);
        storage.put(m.getId(), m);
        return m;
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public Meal update(Meal m) {
        if (storage.containsKey(m.getId())) {
            storage.put(m.getId(), m);
            return m;
        } else {
            return null;
        }
    }

    @Override
    public Meal get(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
