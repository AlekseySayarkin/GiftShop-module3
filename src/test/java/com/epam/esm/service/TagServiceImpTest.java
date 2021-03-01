package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImp;
import com.epam.esm.service.util.impl.PaginationValidatorImpl;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class TagServiceImpTest {

    @InjectMocks
    private TagServiceImp tagService;

    @Mock
    private TagRepository tagDao;

    @BeforeEach
    public void init() {
        var tagValidator = new TagValidatorImpl();
        var paginationValidator = new PaginationValidatorImpl();

        tagService = new TagServiceImp(tagDao, tagValidator, paginationValidator);
    }

    @Test
    void whenGetTag_thenCorrectlyReturnItById() throws ServiceException {
        var given = new Tag(1, "spa");

        Mockito.when(tagDao.findById(given.getId())).thenReturn(Optional.of(given));

        Tag actual = tagService.getTagById(given.getId());
        Assertions.assertEquals(given, actual);
        Mockito.verify(tagDao).findById(given.getId());
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
            Assertions.assertEquals(
                    "Failed to delete tag because it id (" + tag.getId() +") is not found", e.getMessage()
            );
        }
        Mockito.verify(tagDao).deleteById(tag.getId());
    }
}
