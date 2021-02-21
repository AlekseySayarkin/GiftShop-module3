package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * This interface provides with ability to
 * transfer {@code GiftCertificate} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Integer>,
        JpaSpecificationExecutor<GiftCertificate>, RevisionRepository<GiftCertificate, Integer, Integer> {

    @Override
    @Modifying
    @Query("update GiftCertificate g set g.isActive = false where g.id = :certificateId")
    void deleteById(Integer certificateId);
}
