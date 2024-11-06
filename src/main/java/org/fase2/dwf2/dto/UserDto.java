package org.fase2.dwf2.dto;

import lombok.Data;
import org.fase2.dwf2.enums.Role;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String dui;
    private String email;
    private String password;
    private Role role;
}
