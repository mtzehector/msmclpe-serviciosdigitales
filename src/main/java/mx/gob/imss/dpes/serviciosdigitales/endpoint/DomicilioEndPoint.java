/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.AlternateFlowException;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import mx.gob.imss.dpes.serviciosdigitales.service.DomicilioLoadService;
import mx.gob.imss.dpes.serviciosdigitales.service.DomicilioService;
import mx.gob.imss.dpes.serviciosdigitales.service.DomicilioUpdateService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author eduardo.loyo
 */
@Path("/domicilio")
@RequestScoped
public class DomicilioEndPoint extends BaseGUIEndPoint<Domicilio, Domicilio, Domicilio> {

    @Inject
    private DomicilioService create;
    @Inject
    private DomicilioUpdateService update;
    @Inject
    private DomicilioLoadService load;

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cliente Servicios digitales domicilio",
            description = "Obtener el id de la persistencia de los datos de domicilio")
    @Override
    public Response create(Domicilio request) throws BusinessException, AlternateFlowException {
        log.log(Level.INFO, ">>>> DomicilioEndPoint create Request: {0}", request);
        Message<Domicilio> message = new Message<>(request);
        ServiceDefinition[] steps = {create};
        return toResponse(create.executeSteps(steps, message));
    }
    
    
    @PUT
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cliente Servicios digitales domicilio",
            description = "Obtener el id de la persistencia de los datos de domicilio")
    @Override
    public Response update(Domicilio request) throws BusinessException, AlternateFlowException {
        log.log(Level.INFO, ">>>> DomicilioEndPoint update Request= {0}", request.toString());
        Message<Domicilio> message = new Message<>(request);
        ServiceDefinition[] steps = {update};
        return toResponse(create.executeSteps(steps, message));
    }
    
    @POST
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/obtener")
    public Response load(Domicilio request) throws BusinessException, AlternateFlowException{
        log.log(Level.INFO, ">>>> DomicilioEndPoint load Datos de entrada: {0}", request.toString());
        Message<Domicilio> message = new Message<>(request);
        ServiceDefinition[] steps = {load};
        return toResponse(load.executeSteps(steps, message));
    }

}
