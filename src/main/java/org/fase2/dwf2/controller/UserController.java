package org.fase2.dwf2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.LoginRequestDto;
import org.fase2.dwf2.dto.LoginResponseDto;
import org.fase2.dwf2.dto.RegisterRequestDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GET
    @Operation(summary = "Get all users", description = "Returns a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public Response getAllUsers() {
        List<UserDto> users = userService.findAll();
        return Response.ok(users).build();
    }

    @POST
    @Path("/login")
    @Operation(summary = "User login", description = "Authenticates a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public Response login(LoginRequestDto loginRequest) {
        Optional<UserDto> user = userService.findByEmail(loginRequest.getEmail());

        if (user.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

        UserDto userDto = user.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), userDto.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setMessage("Login successful");
        responseDto.setRole(userDto.getRole().name());

        return Response.ok(responseDto).build();
    }

    @POST
    @Path("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Email is already in use")
    })
    public Response register(RegisterRequestDto registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is already in use").build();
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        UserDto newUser = userService.save(registerRequest);
        return Response.status(Response.Status.CREATED).entity(newUser).build();
    }
}