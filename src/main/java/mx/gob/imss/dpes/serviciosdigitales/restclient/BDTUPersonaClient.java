/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.serviciosdigitales.exception.ServiciosDigitalesExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author gabriel.rios
 */

@Path("/personas/personaRenapoServiciosDigaltes")
@RegisterRestClient
@RegisterProvider(ServiciosDigitalesExceptionMapper.class)
public interface BDTUPersonaClient {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{curp}/{nss}")
    public Response load(@PathParam("curp") String curp,@PathParam("nss") String nss);
}

