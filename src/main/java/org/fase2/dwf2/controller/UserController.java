package org.fase2.dwf2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Login.LoginRequestDto;
import org.fase2.dwf2.dto.Login.LoginResponseDto;
import org.fase2.dwf2.dto.Login.RegisterRequestDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

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

        // Setear SecurityContextHolder y autenticar el usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Debug log to verify role
        authentication.getAuthorities().forEach(authority -> {
            System.out.println("Assigned Role: " + authority.getAuthority());
        });

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

    @PUT
    @Path("/{email}")
    @Operation(summary = "Update user", description = "Updates a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "An error occurred while updating the user")
    })
    public Response updateUser(@PathParam("email") String email, RegisterRequestDto registerRequest) {
        try {
            UserDto updatedUser = userService.updateUserByEmail(email, registerRequest);
            return Response.ok(updatedUser).build();
        } catch (UsernameNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while updating the user").build();
        }
    }

    @DELETE
    @Path("/{email}")
    @Operation(summary = "Delete user", description = "Deletes a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response deleteUser(@PathParam("email") String email) {
        userService.deleteUserByEmail(email);
        return Response.noContent().build();
    }

    @GET
    @Path("/{email}")
    @Operation(summary = "Get user profile", description = "Returns the profile of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Response getUserProfile(@PathParam("email") String email) {
        UserDto user = userService.getUserProfile(email);
        return Response.ok(user).build();
    }

    @GET
    @Path("/user/roles")
    @Operation(summary = "Get all user roles", description = "Returns a list of all user roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User roles retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User's roles not found")
    })
    public Response getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            return Response.ok("Roles: " + roles).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
    }

    @GET
    @Path("/user/test")
    public Response test() {
        return Response.ok("Test").build();
    }
}
