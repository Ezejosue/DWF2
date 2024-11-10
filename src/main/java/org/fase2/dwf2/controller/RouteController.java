package org.fase2.dwf2.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;

@Path("/")
public class RouteController {

//    @GetMapping({"/", "/login"})
//    public String login() {
//        System.out.println("Login page");
//        return "login";
//    }
    @GET
    @Path("/")
    public Response getRoot() {
        return Response.ok(new File("src/main/resources/static/login.html")).build();
    }

    @GET
    @Path("/login")
    public Response getLogin() {
        return Response.ok(new File("src/main/resources/static/login.html")).build();
    }

    @GET
    @Path("/unauthorized")
    public Response getUnauthorized() {
        return Response.ok(new File("src/main/resources/static/unauthorized.html")).build();
    }

    @GET
    @Path("/client/dashboard")
    public Response getClientDashboard() {
        return Response.ok(new File("src/main/resources/static/client/dashboard.html")).build();
    }

    @GET
    @Path("/dependiente/dashboard")
    public Response getDependienteDashboard() {
        return Response.ok(new File("src/main/resources/static/client/dependiente/dashboard.html")).build();
    }

    @GET
    @Path("/cajero/dashboard")
    public Response getCajeroDashboard() {
        return Response.ok(new File("src/main/resources/static/client/cajero/dashboard.html")).build();
    }

    @GET
    @Path("/gerente_general/dashboard")
    public Response getGerenteGeneralDashboard() {
        return Response.ok(new File("src/main/resources/static/client/gerente-general/dashboard.html")).build();
    }

    @GET
    @Path("/gerente_sucursal/dashboard")
    public Response getGerenteSucursalDashboard() {
        return Response.ok(new File("src/main/resources/static/client/gerente-sucursal/dashboard.html")).build();
    }
}
