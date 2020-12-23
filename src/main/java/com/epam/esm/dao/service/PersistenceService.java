package com.epam.esm.dao.service;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;

import java.util.List;

public interface PersistenceService<T> {

    void setType(Class<T> type);
    T getModelByName(String query, String name);
    List<T> getModelsByUserId(String query, int id, int page, int size, SortType sortType, SortBy by);
    T getModelById(int id);
    List<T> getAllModels(String query);
    List<T> getAllModelsByPage(String query, int page, int size, SortType sortType, SortBy by);
    int getLastPage(String query, int size);
    T add(T model);
    void delete(int modelId);
    T update(T model);
}
