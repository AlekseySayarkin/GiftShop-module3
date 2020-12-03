package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImp;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class TagServiceImpTest {

    private TagDao tagDao;
    private TagService tagService;

    @BeforeEach
    public void init() {
        tagDao = Mockito.mock(SQLTagDaoImpl.class);
        tagService = new TagServiceImp(tagDao, new TagValidatorImpl());
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItById() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.getTag(given.getId())).thenReturn(given);

        Tag actual = tagService.getTag(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(tagDao).getTag(given.getId());
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItByName() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.getTag(given.getName())).thenReturn(given);

        Tag actual = tagService.getTag(given.getName());
        Assertions.assertEquals(given, actual);
        Mockito.verify(tagDao).getTag(given.getName());
    }


    @Test
    void whenAddTags_thenCorrectlyReturnThem() throws ServiceException {
        List<Tag> expected = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            expected.add(new Tag(i, "Tag " + i));
        }

        Mockito.when(tagDao.getAllTags()).thenReturn(expected);

        List<Tag> actual = tagService.getAllTags();
        Assertions.assertEquals(expected, actual);
        Mockito.verify(tagDao).getAllTags();
    }

    @Test
    void whenTryAddVoidTag_thenThrowException() {
        Tag tag = new Tag();

        try {
            tagService.addTag(tag);
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to validate: tag name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteNonExistingTag_thenThrowException() {
        Tag tag = new Tag(1, "spa");

        try {
            tagService.deleteTag(tag.getId());
        } catch (ServiceException e) {
            Assertions.assertEquals("Failed to delete tag because it id ("
                    + tag.getId() +") is not found", e.getMessage());
        }
        Mockito.verify(tagDao).deleteTag(tag.getId());
    }
}
