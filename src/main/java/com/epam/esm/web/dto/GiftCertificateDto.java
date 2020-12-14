package com.epam.esm.web.dto;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    private int id;
    private String name;
    private String description;
    private double price;
    private ZonedDateTime createDate;
    private ZonedDateTime lastUpdateDate;
    private int duration;
    private Set<TagDto> tags = new HashSet<>();

    public static GiftCertificateDto of(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setId(giftCertificate.getId());
        giftCertificateDto.setName(giftCertificateDto.getName());
        giftCertificateDto.setDescription(giftCertificateDto.getDescription());
        giftCertificateDto.setPrice(giftCertificateDto.getPrice());
        giftCertificateDto.setCreateDate(giftCertificate.getCreateDate());
        giftCertificateDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        giftCertificateDto.setDescription(giftCertificateDto.getDescription());
        Set<TagDto> tagsDto = new HashSet<>();
        for (Tag tag: giftCertificate.getTags()) {
            tagsDto.add(TagDto.of(tag));
        }
        giftCertificateDto.setTags(tagsDto);

        return giftCertificateDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(ZonedDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }
}
