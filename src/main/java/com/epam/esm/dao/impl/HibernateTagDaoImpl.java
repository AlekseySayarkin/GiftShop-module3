package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.model.Tag;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
@EntityScan(basePackageClasses = Tag.class)
public class HibernateTagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag getTag(String name) throws DaoException {
        try {
            TypedQuery<Tag> query = entityManager
                    .createQuery("SELECT t FROM Tag t WHERE t.name=:name", Tag.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new DaoException(String.format("Failed to get tag with name = {%s}", name),
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getTag(int id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public List<Tag> getAllTags() {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t ORDER BY t.id", Tag.class);
        return query.getResultList();
    }

    @Override
    public List<Tag> getAllTagsByPage(int page, int size) {
        TypedQuery<Tag> query = entityManager.createQuery("SELECT t FROM Tag t ORDER BY t.id", Tag.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public int getLastPage(int size) {
        Query query = entityManager.createQuery("SELECT count(t.id) FROM Tag t");
        Long count = (Long) query.getSingleResult();

        int pages = count.intValue()/size;
        if (count % size > 0) {
            pages++;
        }

        return pages;
    }

    @Override
    public int addTag(Tag tag) throws DaoException {
        try{
            entityManager.persist(tag);
            entityManager.flush();
            return tag.getId();
        } catch (PersistenceException e) {
            throw new DaoException(String.format("Failed to add tag with name = {%s}.", tag.getName()),
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }

    @Override
    public boolean deleteTag(int tagId) {
        try {
            Tag toDelete = getTag(tagId);
            entityManager.remove(toDelete);
            return true;
        } catch (NoResultException | IllegalArgumentException e) {
            return false;
        }
    }
}
