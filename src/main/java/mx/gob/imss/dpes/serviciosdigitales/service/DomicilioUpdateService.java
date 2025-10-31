/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import mx.gob.imss.dpes.support.util.ServiceLogger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class DomicilioUpdateService extends ServiceDefinition<Domicilio, Domicilio> {

    @Inject
    @RestClient
    private DomicilioClient client;
    
     @Inject
    CreateBitacoraService createBitacoraService;


    @Override
    public Message<Domicilio> execute(Message<Domicilio> request) throws BusinessException {
        log.log(Level.INFO, ">>>DomicilioUpdateService Request hacia Domicilio request={0}", request);
        String jsonStr = DomicilioService.getJSonStr(request.getPayload());
        log.log(Level.INFO, ">>>DomicilioUpdateService Request hacia Domicilio domicilio json=''{0}''", jsonStr);
        try {
            Response response = client.update(request.getPayload());
            if (response.getStatus() == 204) {
                //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),response,8L)));
                log.log(Level.INFO, ">>>DomicilioUpdateService Response 204, void");
                return new Message<>();
            }
        } catch (Exception e) {
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(),8L, true)));
            log.log(Level.SEVERE, ">>>DomicilioUpdateService getLocalizedMessage=''{0}''", e.getLocalizedMessage());
            if (e.getMessage().contains("Unknown error, status code 415")) {
                log.log(Level.SEVERE, ">>>DomicilioUpdateService return void, query success.Pending...");
                return new Message<>();
            } else {
                e.printStackTrace();
                log.log(Level.SEVERE, ">>>DomicilioUpdateService message=''{0}''", e.getMessage());
            }

        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}
