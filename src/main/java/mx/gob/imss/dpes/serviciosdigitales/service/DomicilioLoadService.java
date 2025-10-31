/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import java.io.InputStream;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import mx.gob.imss.dpes.serviciosdigitales.restclient.DomicilioClient;
import mx.gob.imss.dpes.support.util.LoggerUtils;
import mx.gob.imss.dpes.support.util.ServiceLogger;
import org.eclipse.microprofile.rest.client.inject.RestClient;


/**
 *
 * @author eduardo.loyo
 */
@Provider
public class DomicilioLoadService extends ServiceDefinition<Domicilio, Domicilio> {

    @Inject
    @RestClient
    private DomicilioClient client;
    
    @Inject
    CreateBitacoraService createBitacoraService;

    @Override
    public Message<Domicilio> execute(Message<Domicilio> request) throws BusinessException {
        log.log(Level.INFO, ">>>DomicilioLoadService Request hacia request.getPayload()={0}", request.getPayload());
        log.log(Level.INFO, ">>>DomicilioLoadService client.getClass()={0}",client.getClass());
        log.log(Level.INFO, ">>>DomicilioLoadService client.getClass().getName={0}",client.getClass().getName());
            
        String jsonStr = LoggerUtils.JSon2Str(request.getPayload());
        Response response = null;
        try {
            response = client.load(request.getPayload().getIdDomicilio());
            client.getClass().getName();
            if (response.getStatus() == 200) {
                Domicilio domicilio = response.readEntity(Domicilio.class);
                //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),domicilio,8L)));
                log.log(Level.INFO, ">>>DomicilioLoadService Response domicilio={0}", domicilio);
                return new Message<>(domicilio);
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),e.getMessage(),8L,true)));
            
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
    
   
}
