package com.epam.esm.web.dto;

import com.epam.esm.model.Role;
import org.springframework.hateoas.RepresentationModel;

public class RoleDto extends RepresentationModel<RoleDto> {

    private int id;
    private Role.RoleType role;

    public static RoleDto of(Role role) {
        var roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setRole(role.getRoleType());

        return roleDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role.RoleType getRole() {
        return role;
    }

    public void setRole(Role.RoleType role) {
        this.role = role;
    }
}
