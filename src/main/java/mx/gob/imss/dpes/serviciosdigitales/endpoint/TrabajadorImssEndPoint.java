/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.endpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.serviciosdigitales.models.TrabajadorImss;
import mx.gob.imss.dpes.serviciosdigitales.service.ObtenDatosRenapo;
import mx.gob.imss.dpes.serviciosdigitales.service.TrabajadorActivoService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author juan.garfias
 */
@Path("/trabajadorImss")
@RequestScoped
public class TrabajadorImssEndPoint extends BaseGUIEndPoint<TrabajadorImss, BaseModel, BaseModel> {

    @Inject
    private TrabajadorActivoService trabajadorActivoService;
  
    @Inject
    private ObtenDatosRenapo renapo;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(TrabajadorImss request) throws BusinessException {

        ServiceDefinition[] steps = {trabajadorActivoService, renapo};
        Message<TrabajadorImss> response = trabajadorActivoService.executeSteps(steps, new Message<>(request));

        return toResponse(trabajadorActivoService.execute(new Message<>(request)));
    }
}
