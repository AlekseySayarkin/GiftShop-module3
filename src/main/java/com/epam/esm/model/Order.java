package com.epam.esm.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private int id;
    private double cost;
    private ZonedDateTime createDate;
    private User user;
    private List<GiftCertificate> giftCertificateList;

    public Order() {
    }

    public Order(int id, double cost, ZonedDateTime createDate, User user, List<GiftCertificate> giftCertificateList) {
        this.id = id;
        this.cost = cost;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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

    public List<GiftCertificate> getGiftCertificateList() {
        return giftCertificateList;
    }

    public void setGiftCertificateList(List<GiftCertificate> giftCertificateList) {
        this.giftCertificateList = giftCertificateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Double.compare(order.cost, cost) == 0 && createDate.equals(order.createDate) && user.equals(order.user) && giftCertificateList.equals(order.giftCertificateList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, createDate, user, giftCertificateList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cost=" + cost +
                ", createDate=" + createDate +
                ", user=" + user +
                ", giftCertificateList=" + giftCertificateList +
                '}';
    }
}
