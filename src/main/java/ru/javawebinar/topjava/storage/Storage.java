package ru.javawebinar.topjava.storage;

import java.util.List;

public interface Storage<T> {

    T add(T element);

    void delete(Integer id);

    T update(T element);

    T get(Integer id);

    List<T> getAll();
}
