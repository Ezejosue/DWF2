package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.entities.Branch;
import org.fase2.dwf2.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/branches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GET
    @Path("/unassigned")
    public Response getUnassignedBranches() {
        List<Branch> unassignedBranches = branchService.getUnassignedBranches();
        return Response.ok(unassignedBranches).build();
    }

    @PUT
    @Path("/{branchId}/assign/{managerId}")
    public Response assignManagerToBranch(@PathParam("branchId") Long branchId, @PathParam("managerId") Long managerId) {
        try {
            Branch updatedBranch = branchService.assignManagerToBranch(branchId, managerId);
            return Response.ok(updatedBranch).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
