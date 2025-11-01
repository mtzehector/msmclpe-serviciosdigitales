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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.AlternateFlowException;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.serviciosdigitales.service.ReadPersonaService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author antonio
 */
@Path("/persona")
@RequestScoped
public class PersonaEndPoint extends BaseGUIEndPoint<Persona, Persona, Persona>{
  
  @Inject  
  ReadPersonaService service;
  
  @GET
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Obtener una persona del sistema de enrolamiento mediante el CURP",
            description = "Obtener una persona del sistema de enrolamiento mediante el CURP")
  @Override
  public Response load(Persona request) throws BusinessException, AlternateFlowException{
    log.log(Level.INFO, ">>> serviciosDigitales PersonaEndPoint Datos de entrada= {0}", request.toString() );
    Message<Persona> message = new Message<>(request);
    ServiceDefinition[] steps = {service};
    return toResponse ( service.executeSteps(steps, message) ) ;
  }

}
