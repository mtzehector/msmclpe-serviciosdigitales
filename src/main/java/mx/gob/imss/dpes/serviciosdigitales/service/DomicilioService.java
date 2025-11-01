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
public class DomicilioService extends ServiceDefinition<Domicilio, Domicilio> {

    @Inject
    @RestClient
    private DomicilioClient client;
    
    @Inject
    CreateBitacoraService createBitacoraService;

    @Override
    public Message<Domicilio> execute(Message<Domicilio> request) throws BusinessException {
        log.log(Level.INFO, ">>>DomicilioService Request hacia Domicilio request={0}", request);
        String jsonStr = JSon2Str(request.getPayload());
        log.log(Level.INFO, ">>>DomicilioService Request hacia Domicilio JSon=''{0}''", jsonStr);
        try {
            Response response = client.create(request.getPayload());

            if (response.getStatus() == 200) {
                Domicilio domicilio = response.readEntity(Domicilio.class);
                //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),domicilio,8L)));
                jsonStr = getJSonStr(domicilio);
                log.log(Level.INFO, ">>>DomicilioService Response djson=''{0}''", jsonStr);
                return new Message<>(domicilio);
            }
        } catch (Exception e) {
            e.printStackTrace();
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(),8L, true)));
           
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

    protected static String getJSonStr(Domicilio domicilio) {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = Obj.writeValueAsString(domicilio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    protected static String JSon2Str(Object target) {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = Obj.writeValueAsString(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

}
