package com.epam.esm.dao.service.impl;

import com.epam.esm.dao.service.PersistenceService;
import com.epam.esm.dao.sort.SortBy;
import com.epam.esm.dao.sort.SortType;
import com.epam.esm.model.BaseModel;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@Scope("prototype")
@EntityScan(basePackages = "com.epam.esm.model")
public class PersistenceServiceImpl<T extends BaseModel> implements PersistenceService<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> type;

    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getModelByName(String query, String name) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        typedQuery.setParameter("name", name);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<T> getModelsByUserId(String query, int id, int page, int size, SortType sortType, SortBy by) {
        query += "order by " + by + " " + sortType;
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        typedQuery.setParameter("userId", id);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public T getModelById(int id) {
        return entityManager.find(type, id);
    }

    @Override
    public List<T> getAllModels(String query) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        return typedQuery.getResultList();
    }

    @Override
    public List<T> getAllModelsByPage(String query, int page, int size, SortType sortType, SortBy by) {
        query += "order by " + by + " " + sortType;
        TypedQuery<T> typedQuery = entityManager.createQuery(query, type);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public int getLastPage(String query, int size) {
        Query typedQuery = entityManager.createQuery(query);
        Long count = (Long) typedQuery.getSingleResult();

        int pages = count.intValue()/size;
        if (count % size > 0) {
            pages++;
        }

        return pages;
    }

    @Override
    public T add(T model) {
        entityManager.persist(model);
        entityManager.flush();
        return model;
    }

    @Override
    public void delete(int modelId) {
        T model = getModelById(modelId);
        entityManager.remove(model);
    }

    @Override
    public T update(T model) {
        model = entityManager.merge(model);
        entityManager.flush();

        return model;
    }
}
