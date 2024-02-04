package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    void add(Meal m);

    void delete(String id);

    void update(Meal m);

    Meal get(String id);

    List<Meal> getAll();
}
