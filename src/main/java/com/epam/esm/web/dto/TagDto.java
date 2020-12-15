package com.epam.esm.web.dto;

import com.epam.esm.model.Tag;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

public class TagDto extends RepresentationModel<TagDto> {

    private int id;
    private String name;

    public TagDto() {
    }

    public TagDto(String name) {
        this.name = name;
    }

    public TagDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<TagDto> of(List<Tag> tags) {
        return tags.stream().map(TagDto::of).collect(Collectors.toList());
    }

    public static TagDto of(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
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
}
