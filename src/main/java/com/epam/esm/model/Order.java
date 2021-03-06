package com.epam.esm.model;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Orders")
@Audited
@Where(clause = "Active = true")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Cost")
    private double totalCost;

    @Column(name = "CreateDate")
    private ZonedDateTime createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "OrderCertificate",
            joinColumns = @JoinColumn(name = "orderId"),
            inverseJoinColumns = @JoinColumn(name = "certificateId")
    )
    private Set<GiftCertificate> giftCertificateList = new HashSet<>();

    @Column(name = "Active")
    private boolean isActive;

    public Order() {
    }

    public Order(int id, double totalCost, ZonedDateTime createDate, User user, Set<GiftCertificate> giftCertificateList) {
        this.id = id;
        this.totalCost = totalCost;
        this.createDate = createDate;
        this.user = user;
        this.giftCertificateList = giftCertificateList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double cost) {
        this.totalCost = cost;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GiftCertificate> getGiftCertificateList() {
        return giftCertificateList;
    }

    public void setGiftCertificateList(Set<GiftCertificate> giftCertificateList) {
        this.giftCertificateList = giftCertificateList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Double.compare(order.totalCost, totalCost) == 0 &&
                createDate.equals(order.createDate) && user.equals(order.user) &&
                giftCertificateList.equals(order.giftCertificateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalCost, createDate, giftCertificateList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cost=" + totalCost +
                ", createDate=" + createDate +
                ", user=" + user +
                ", giftCertificateList=" + giftCertificateList +
                '}';
    }
}
