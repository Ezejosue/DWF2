package org.fase2.dwf2.dto.Login;

import lombok.Data;
import org.fase2.dwf2.enums.Role;

@Data
public class RegisterRequestDto {
    private String name;
    private String dui;
    private String email;
    private String password;
    private Role role;
}
