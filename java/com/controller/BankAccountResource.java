package com.controller;


import com.model.ValidationResponse;
import com.service.BankAccountService;
import javax.ws.rs.*;	
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bank-account")
public class BankAccountResource {

//    @Inject
//    private BankAccountService bankAccountService;

    @GET
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)   
    
    public Response validateBankAccount(@QueryParam("accountNumber") String accountNumber) {
    	BankAccountService bankAccountService = new BankAccountService();
    	
        String validationResult = bankAccountService.validate(accountNumber);
        
        return Response.ok(new ValidationResponse(validationResult)).build();
    }

   
}

