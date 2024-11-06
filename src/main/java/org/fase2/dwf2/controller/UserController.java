package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response getAllUsers() {
        List<UserDto> users = userService.findAll();
        return Response.ok(users).build();
    }
}
