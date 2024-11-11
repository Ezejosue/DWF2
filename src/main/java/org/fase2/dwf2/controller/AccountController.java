package org.fase2.dwf2.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fase2.dwf2.dto.Account.AccountRequestDto;
import org.fase2.dwf2.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Path("/{email}")
    public Response getAccount(@PathParam("email") String email) {
        List<AccountRequestDto> accounts = accountService.findByUserEmail(email);
        return Response.ok(accounts).build();
    }

    @POST
    public Response createAccount(AccountRequestDto accountRequestDto) {
        AccountRequestDto account = accountService.createAccount(accountRequestDto);
        return Response.ok(account).build();
    }

    @POST
    @Path("/deposit")
    public Response depositFunds(AccountRequestDto accountRequestDto) {
        System.out.println("AccountRequestDto: " + accountRequestDto);
        if (accountRequestDto.getAccountNumber() == null || accountRequestDto.getBalance() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account number and amount are required").build();
        }

        try {
            AccountRequestDto updatedAccount = accountService.depositFunds(
                    accountRequestDto.getAccountNumber(),
                    accountRequestDto.getBalance()
            );
            return Response.ok(updatedAccount).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/withdraw")
    public Response withdrawFunds(AccountRequestDto accountRequestDto) {
        System.out.println("AccountRequestDto: " + accountRequestDto);
        if (accountRequestDto.getAccountNumber() == null || accountRequestDto.getBalance() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Account number and amount are required").build();
        }

        try {
            AccountRequestDto updatedAccount = accountService.withdrawFunds(
                    accountRequestDto.getAccountNumber(),
                    accountRequestDto.getBalance()
            );
            return Response.ok(updatedAccount).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


}
