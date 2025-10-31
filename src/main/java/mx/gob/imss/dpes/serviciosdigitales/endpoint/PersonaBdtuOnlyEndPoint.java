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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequest;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequestIn;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequestOut;
import mx.gob.imss.dpes.serviciosdigitales.service.ComparaBdtuService;
import mx.gob.imss.dpes.serviciosdigitales.service.LoadBdtuService;

/**
 *
 * @author gabriel.rios
 */
@Path("/personabdtuonly")
@RequestScoped
public class PersonaBdtuOnlyEndPoint extends BaseGUIEndPoint<BDTURequest, BDTURequest, BDTURequest> {

    @Inject
    private ComparaBdtuService bdtuService;
    
    @Inject
    private LoadBdtuService loadBdtuService;
    
    
    @POST    
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(BDTURequestIn rq) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>PersonaBdtuOnlyEndPoint  Antes de ejecutar el servicio bdtu 1: " + rq.getCurp() + " - " + rq.getNss());
        BDTURequest request = new BDTURequest(new BDTURequestIn(rq.getCurp(),  rq.getNss()), new BDTURequestOut());
        Message<BDTURequest> message = new Message<>(request);
        ServiceDefinition[] steps = {bdtuService};
        log.log(Level.INFO, ">>>>>>>>>PersonaBdtuOnlyEndPoint  Antes de ejecutar el servicio bdtu 1.1: " + rq.getCurp() + " - " +  rq.getNss());

        return toResponse(bdtuService.executeSteps(steps, message));
    }
    
    @GET 
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{curp}/{nss}")
    public Response loadByCurpAndNss(@PathParam("curp") String curp,@PathParam("nss") String nss) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>PersonaBdtuOnlyEndPoint  loadByCurpAndNss. curp= " + curp + " - " + nss);
        BDTURequestIn requestIn = new BDTURequestIn();
        requestIn.setCurp(curp);
        requestIn.setNss(nss);
        BDTURequest request = new BDTURequest(requestIn, new BDTURequestOut());
        Message<BDTURequest> message = new Message<>(request);
        ServiceDefinition[] steps = {loadBdtuService};
        Message<BDTURequest> finalMessage = loadBdtuService.execute(message);
        BDTURequestOut requestOut = finalMessage.getPayload().getBdtuOut();
        log.log(Level.INFO, ">>>>>>>>>PersonaBdtuOnlyEndPoint.loadByCurpAndNss  requestOut="+requestOut);
        return toResponse(new Message(requestOut));
    }
}
