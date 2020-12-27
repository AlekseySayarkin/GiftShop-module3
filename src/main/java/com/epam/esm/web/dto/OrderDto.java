package com.epam.esm.web.dto;

import com.epam.esm.model.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderDto extends RepresentationModel<OrderDto> {

    private int id;
    private double cost;
    private ZonedDateTime createDate;
    private EntityModel<UserDto> user;
    private Set<EntityModel<GiftCertificateDto>> giftCertificateList = new HashSet<>();

    public static OrderDto of(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCost(order.getCost());
        orderDto.setCreateDate(order.getCreateDate());
        orderDto.setUser(EntityModel.of(UserDto.of(order.getUser())));
        Set<EntityModel<GiftCertificateDto>> giftCertificateDto = new HashSet<>();
        order.getGiftCertificateList().forEach(g ->
                giftCertificateDto.add(EntityModel.of(GiftCertificateDto.of(g))));
        orderDto.setGiftCertificateList(giftCertificateDto);

        return orderDto;
    }

    public static List<OrderDto> of(List<Order> orders) {
        return orders.stream().map(OrderDto::of).collect(Collectors.toList());
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

    public EntityModel<UserDto> getUser() {
        return user;
    }

    public void setUser(EntityModel<UserDto> user) {
        this.user = user;
    }

    public Set<EntityModel<GiftCertificateDto>> getGiftCertificateList() {
        return giftCertificateList;
    }

    public void setGiftCertificateList(Set<EntityModel<GiftCertificateDto>> giftCertificateList) {
        this.giftCertificateList = giftCertificateList;
    }
}
