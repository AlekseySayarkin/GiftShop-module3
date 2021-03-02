package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImp;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImpTest {

    private TagServiceImp tagService;

    @Mock
    private TagRepository tagDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        var tagValidator = new TagValidatorImpl();
        var paginationValidator = new PaginationValidatorImpl();

        tagService = new TagServiceImp(tagDao, tagValidator, paginationValidator);
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItById() throws ServiceException {
        var given = new Tag(1, "spa");

        when(tagDao.findById(given.getId())).thenReturn(Optional.of(given));

        var actual = tagService.getTagById(given.getId());
        assertEquals(given, actual);
        verify(tagDao).findById(given.getId());
    }

    @Test
    void whenTryAddVoidTag_thenThrowException() {
        var tag = new Tag();

        try {
            tagService.addTag(tag);
        } catch (ServiceException e) {
            assertEquals("Failed to validate: tag name is empty", e.getMessage());
        }
    }

    @Test
    void whenTryDeleteNonExistingTag_thenThrowException() {
        var tag = new Tag(1, "spa");

        try {
            tagService.deleteTag(tag.getId());
        } catch (ServiceException e) {
            assertEquals("Failed to delete tag because it id (" + tag.getId() +") is not found", e.getMessage());
        }
        verify(tagDao).deleteById(tag.getId());
    }
}
