package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Loan.LoanRequestDto;
import org.fase2.dwf2.dto.Loan.LoanResponseDto;
import org.fase2.dwf2.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GET
    public Response getAllLoans() {
        List<LoanResponseDto> loans = loanService.getAllLoans();
        return Response.ok(loans).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLoan(LoanRequestDto loanRequestDto) {
        try {
            System.out.println("Creating loan: " + loanRequestDto);
            LoanResponseDto loan = loanService.createLoan(loanRequestDto);
            System.out.println("Loan created successfully: " + loan);
            return Response.ok(loan).build();
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log the full exception stack trace
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create loan").build();
        }
    }


}

