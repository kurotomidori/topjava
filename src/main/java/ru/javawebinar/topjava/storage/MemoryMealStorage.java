package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealStorage implements Storage<Meal> {

    private final ConcurrentMap<Integer, Meal> storage = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger();

    public MemoryMealStorage() {
        for (Meal m : MealsUtil.meals) {
            this.add(m);
        }
    }

    @Override
    public Meal add(Meal m) {
        m.setId(counter.incrementAndGet());
        storage.put(m.getId(), m);
        return m;
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public Meal update(Meal m) {
        return storage.computeIfPresent(m.getId(), (key, oldValue) -> m);
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
