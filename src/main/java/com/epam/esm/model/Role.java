package com.epam.esm.model;

import org.hibernate.envers.Audited;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Roles")
@Audited
public class Role implements BaseModel {

    public enum Permission {
        TAGS_READ("tags:read"),
        TAGS_WRITE("tags:write"),
        CERTIFICATES_READ("certificates:read"),
        CERTIFICATES_WRITE("certificates:write"),
        USERS_READ("users:read"),
        USERS_WRITE("users:write"),
        ORDERS_READ("orders:read"),
        ORDERS_WRITE("orders:write");

        private final String permission;

        Permission(String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }
    }

    public enum RoleType {
        USER(Set.of(
                Permission.TAGS_READ,
                Permission.CERTIFICATES_READ,
                Permission.ORDERS_READ,
                Permission.ORDERS_WRITE
            )
        ),
        ADMIN(Set.of(
                Permission.USERS_READ,
                Permission.TAGS_READ,
                Permission.CERTIFICATES_READ,
                Permission.ORDERS_READ,
                Permission.USERS_WRITE,
                Permission.TAGS_WRITE,
                Permission.CERTIFICATES_WRITE,
                Permission.ORDERS_WRITE
            )
        );

        private final Set<Permission> permissions;

        RoleType(Set<Permission> permissions) {
            this.permissions = permissions;
        }

        public Set<Permission> getPermissions() {
            return permissions;
        }

        public Set<SimpleGrantedAuthority> getAuthorities() {
            return getPermissions().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                    .collect(Collectors.toSet());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private RoleType role;

    private final static int USER_ROLE_ID = 1;
    public static final int ADMIN_ROLE_ID = 2;

    public static Role getUserRole() {
        Role role = new Role();
        role.setId(USER_ROLE_ID);
        role.setRoleType(RoleType.USER);

        return role;
    }

    public static Role getAdminRole() {
        Role role = new Role();
        role.setId(ADMIN_ROLE_ID);
        role.setRoleType(RoleType.ADMIN);

        return role;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleType getRoleType() {
        return role;
    }

    public void setRoleType(RoleType role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return id == role1.id && role.equals(role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                '}';
    }
}
