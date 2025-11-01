/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.serviciosdigitales.exception.RenapoBDTUInfoNotMacthingException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequest;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequestIn;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequestOut;
import mx.gob.imss.dpes.serviciosdigitales.restclient.BDTUPersonaClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import mx.gob.imss.dpes.support.util.ServiceLogger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.montesh
 */
@Provider
public class ComparaBdtuService extends ServiceDefinition<BDTURequest, BDTURequest> {

    @Inject
    @RestClient
    BDTUPersonaClient bdtuPersonaClient;
    
    @Inject
    CreateBitacoraService createBitacoraService;

    final static String DATE_FORMAT = "dd/MM/yyyy";
    final static DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    public Message<BDTURequest> execute(Message<BDTURequest> request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>ComparaBdtuService  Entra al servicio compara BDTU ");

        BDTURequestIn in = new BDTURequestIn();

        in.setCurp(request.getPayload().getBdtuIn().getCurp());

        String nss = String.format("%011d", Long.parseLong(request.getPayload().getBdtuIn().getNss()) );
        
        log.log(Level.INFO, ">>>>>>>>>ComparaBdtuService  Antes de ejecutar el servicio bdtu 2: " 
                + request.getPayload().getBdtuIn().getCurp() + " - " + nss);
        
        Response response = null;
        try {
            response = bdtuPersonaClient.load(request.getPayload().getBdtuIn().getCurp(),nss);
        } catch (Exception e) {
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),e.getMessage(),true)));
            
            log.log(Level.SEVERE, ">>>>>>>ComparaBdtuService  !!!--- ERROR: Exception message=\n{0}", e.getMessage());

            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                ExceptionUtils.throwServiceException("BDTU");
            }

            if (e.getMessage().contains("Unknown error, status code 400")) {
                throw new RenapoBDTUInfoNotMacthingException("msg355");
            }

            if (e.getMessage().contains("Unknown error, status code 500")) {
                throw new RenapoBDTUInfoNotMacthingException("msg356");
            }

            throw new UnknowException();
        }
        BDTURequestOut out = response.readEntity(BDTURequestOut.class);
        //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload().getBdtuIn(),out)));
        log.log(Level.INFO, ">>>ComparaBdtuService  out="+out);
        request.getPayload().setBdtuOut(out);
        return request;
    }

}
