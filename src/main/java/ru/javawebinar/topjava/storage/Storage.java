package ru.javawebinar.topjava.storage;

import java.util.List;

public interface Storage<T> {

    T add(T element);

    void delete(int id);

    T update(T element);

    T get(int id);

    List<T> getAll();
}
