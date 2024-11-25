package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.EmployeeAction.EmployeeActionRequestDto;
import org.fase2.dwf2.dto.EmployeeAction.EmployeeActionResponseDto;
import org.fase2.dwf2.service.EmployeeActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Path("/api/actions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeActionController {

    private final EmployeeActionService actionService;

    @Autowired
    public EmployeeActionController(EmployeeActionService actionService) {
        this.actionService = actionService;
    }

    @POST
    @Path("/create")
    public Response createAction(EmployeeActionRequestDto requestDto) {
        try {
            EmployeeActionResponseDto responseDto = actionService.createAction(requestDto);
            return Response.ok(responseDto).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create action.").build();
        }
    }

    @GET
    @Path("/pending")
    public Response getPendingActions() {
        try {
            return Response.ok(actionService.getPendingActions()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to retrieve actions.").build();
        }
    }

    @GET
    @Path("/count/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countPendingEmployeeActions() {
        long count = actionService.countPendingEmployeeActions();
        return Response.ok(Map.of("count", count)).build();
    }

}