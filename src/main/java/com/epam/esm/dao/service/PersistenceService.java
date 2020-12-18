package com.epam.esm.dao.service;

import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.BaseModel;

import java.util.List;

public interface PersistenceService<T> {

    void setType(Class<T> type);
    T getModelByName(String query, String name);
    T getModelById(int id);
    List<T> getAllModels(String query);
    List<T> getAllModelsByPage(String query, int page, int size, SortType sortType, SortBy by);
    int getLastPage(String query, int size);
    int addModel(T model);
    void deleteModel(int modelId);
    T updateModel(T model);
}
