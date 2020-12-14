package com.epam.esm.dao;

import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.dao.impl.HibernateGiftCertificateDaoImpl;
import com.epam.esm.dao.impl.HibernateTagDaoImpl;
import com.epam.esm.dao.util.PersistenceUtilImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class HibernateGiftCertificateDaoImplTest {

    private GiftCertificateDAO giftCertificateDAO;
    private TagDao tagDao;
    private int limit;
    private int offset;

    @BeforeEach
    public void init() throws DaoException {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/ClearTables.sql")
                .addScript("db/Tags.sql")
                .addScript("db/GiftCertificates.sql")
                .addScript("db/CertificateDetails.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        tagDao = new HibernateTagDaoImpl(new PersistenceUtilImpl<>());
        giftCertificateDAO = new HibernateGiftCertificateDaoImpl(
                new PersistenceUtilImpl<>());

        tagDao.addTag(new Tag("spa"));
        tagDao.addTag(new Tag("relax"));
        tagDao.addTag(new Tag("tourism"));

        limit = 50;
        offset = 0;
    }

    private List<GiftCertificate> initCertificates(int size) throws DaoException {
        List<GiftCertificate> given = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            given.add(certificate);
        }

        return given;
    }

    private GiftCertificate initCertificate() throws DaoException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Tour to Greece");
        certificate.setDescription("Certificate description");
        certificate.setPrice(99.99);

        certificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setDuration(10);

        Set<Tag> tags = new HashSet<>();
        tags.add(tagDao.getTag("tourism"));
        tags.add(tagDao.getTag("relax"));
        certificate.setTags(tags);

        return certificate;
    }

    private void assertEqualsWithoutUpdateDate(GiftCertificate actual, GiftCertificate expected) {
        Assertions.assertEquals(actual.getId(), expected.getId());
        Assertions.assertEquals(actual.getName(), expected.getName());
        Assertions.assertEquals(actual.getPrice(), expected.getPrice(), 0.0001);
        Assertions.assertEquals(actual.getDuration(), expected.getDuration());
        Assertions.assertEquals(actual.getTags(), expected.getTags());
        Assertions.assertTrue(equalDates(actual.getCreateDate(), expected.getCreateDate()));
    }

    private void assertEquals(List<GiftCertificate> expected, List<GiftCertificate> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private void assertEquals(GiftCertificate actual, GiftCertificate expected) {
        Assertions.assertEquals(actual.getId(), expected.getId());
        Assertions.assertEquals(actual.getName(), expected.getName());
        Assertions.assertEquals(actual.getPrice(), expected.getPrice(), 0.0001);
        Assertions.assertEquals(actual.getDuration(), expected.getDuration());
        Assertions.assertEquals(actual.getTags(), expected.getTags());

        Assertions.assertTrue(equalDates(actual.getCreateDate(), expected.getCreateDate()));
        Assertions.assertTrue(equalDates(actual.getLastUpdateDate(), expected.getLastUpdateDate()));
    }

    private boolean equalDates(ZonedDateTime first, ZonedDateTime second) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String firstDate = ZonedDateTime.ofInstant(first.toInstant(), ZoneOffset.of("-06:00")).format(formatter);
        String secondDate = ZonedDateTime.ofInstant(second.toInstant(), ZoneOffset.UTC).format(formatter);

        return firstDate.equals(secondDate);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyReturnItById() throws DaoException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        GiftCertificate actual = giftCertificateDAO.getGiftCertificate(given.getId());
        assertEquals(given, actual);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyReturnItByName() throws DaoException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        GiftCertificate actual = giftCertificateDAO.getGiftCertificate(given.getName());
        assertEquals(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemByTagName() throws DaoException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getGiftCertificateByTagName(
                "relax", limit, offset);
        assertEquals(given, actual);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyDeleteIt() throws DaoException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        giftCertificateDAO.deleteAllCertificateTagRelations(given.getId());
        boolean actual = giftCertificateDAO.deleteGiftCertificate(given.getId());
        Assertions.assertTrue(actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThem() throws DaoException {
        List<GiftCertificate> given = initCertificates(20);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getGiftCertificatesByPage(limit, offset);
        assertEquals(given, actual);
    }

    @Test
    void whenAddGiftCertificate_thenCorrectlyUpdateIt() throws DaoException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        given.setName("new name");
        given.setDescription("new description");
        given.setPrice(199.99);
        given.setDuration(99);
        given.getTags().add(tagDao.getTag("spa"));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        giftCertificateDAO.updateGiftCertificate(given);
        GiftCertificate actual = giftCertificateDAO.getGiftCertificate(given.getId());
        assertEqualsWithoutUpdateDate(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByNameAsc() throws DaoException {
        List<GiftCertificate> given = initCertificates(10);

        for (int i = 0; i < given.size(); i++) {
            given.get(i).setName(String.valueOf(i));
            given.get(i).setId(giftCertificateDAO.addGiftCertificate(given.get(i)));

            for (Tag tag:  given.get(i).getTags()) {
                giftCertificateDAO.createCertificateTagRelation(given.get(i).getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificatesSortedByName(
                true, limit, offset);
        assertEquals(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByNameDesc() throws DaoException {
        List<GiftCertificate> given = initCertificates(10);

        for (int i = 0; i < given.size(); i++) {
            given.get(i).setName(String.valueOf(i));
            given.get(i).setId(giftCertificateDAO.addGiftCertificate(given.get(i)));

            for (Tag tag:  given.get(i).getTags()) {
                giftCertificateDAO.createCertificateTagRelation(given.get(i).getId(), tag.getId());
            }
        }

        Collections.reverse(given);
        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificatesSortedByName(
                false, limit, offset);
        assertEquals(given, actual);
    }

    @Test
    void whenAddGiftCertificates_thenCorrectlyReturnThemByContent() throws DaoException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getGiftCertificatesByContent(
                "Certificate description", limit, offset);
        assertEquals(given, actual);
    }
}
