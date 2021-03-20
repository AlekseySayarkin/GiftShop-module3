package com.epam.esm.repository;

import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface provides with ability to
 * transfer {@code Order} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    @Query(value =
            "SELECT tags.ID, tags.Name, count(tags.Name) AS count FROM Orders " +
            "INNER JOIN OrderCertificate ON OrderCertificate.OrderId = Orders.id " +
            "INNER JOIN GiftCertificates ON CertificateId = GiftCertificates.id " +
            "INNER JOIN CertificateTag ON CertificateTag.CertificateId = GiftCertificates.id " +
            "INNER JOIN tags on CertificateTag.tagId = tags.id " +
            "WHERE userId IN ( " +
            "   SELECT userId FROM ( " +
            "       SELECT Sum(Cost) sumCost, userId " +
            "       FROM Orders " +
            "       GROUP BY userId " +
            "       order by sumCost desc " +
            "       LIMIT 1 " +
            "   ) AS ids " +
            ") " +
            "GROUP BY tags.ID " +
            "ORDER BY count DESC LIMIT 1", nativeQuery = true)
    Tag getMostFrequentTagFromHighestCostUser();

    @Override
    @Modifying
    @Query("update Order o set o.isActive = false where o.id = :orderId")
    void deleteById(Integer orderId);
}
