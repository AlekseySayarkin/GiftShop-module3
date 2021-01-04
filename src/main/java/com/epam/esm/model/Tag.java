package com.epam.esm.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tags", schema = "GiftShop")
@Audited
public class Tag implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
