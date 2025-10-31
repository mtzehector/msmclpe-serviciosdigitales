package mx.gob.imss.dpes.serviciosdigitales.service;

import java.util.logging.Level;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.CorreoSinAdjuntos;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import mx.gob.imss.dpes.serviciosdigitales.restclient.CorreoClient;
import mx.gob.imss.dpes.support.util.ServiceLogger;

@Provider
public class CreateCorreoService extends ServiceDefinition<Correo, Correo> {

    @Inject
    @RestClient
    private CorreoClient client;
    
    @Inject
    CreateBitacoraService createBitacoraService;

    @Override
    public Message<Correo> execute(Message<Correo> request) throws BusinessException {
        request.getPayload().setRemitente("noreply.prestamos@imss.gob.mx");

        if (request.getPayload().getCorreoPara().get(0) == null) {
            log.log(Level.SEVERE, "No se definio el correo");
            return request;
        }

        if (request.getPayload().getAdjuntos().isEmpty()) {
            CorreoSinAdjuntos correoSinAdjuntos = new CorreoSinAdjuntos();
            correoSinAdjuntos.setRemitente(request.getPayload().getRemitente());
            correoSinAdjuntos.setCorreoPara(request.getPayload().getCorreoPara());
            correoSinAdjuntos.setAsunto(request.getPayload().getAsunto());
            correoSinAdjuntos.setCuerpoCorreo(request.getPayload().getCuerpoCorreo());

            log.log(Level.INFO, "Request hacia CorreoElectronico Servicios Digitales Correo Para: {0}", correoSinAdjuntos.getCorreoPara());
            try {
                Response response = client.createSinAdjuntos(correoSinAdjuntos);
                if (response.getStatus() == 200 || response.getStatus() == 204) {
                    //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(correoSinAdjuntos, response)));

                    return request;
                }
            } catch (Exception e) {
                e.printStackTrace();
                createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(), true)));
                log.log(Level.SEVERE, ">>>CreateCorreoService");
            }

        }
        log.log(Level.INFO, "Request hacia CorreoElectronico Servicios Digitales Correo Para: {0}", request.getPayload().getCorreoPara());
        try {
            Response response = client.create(request.getPayload());

            if (response.getStatus() == 200 || response.getStatus() == 204) {
                //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), response)));
                return request;
            }
        } catch (Exception e) {
            e.printStackTrace();
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(), true)));
            log.log(Level.SEVERE, ">>>CreateCorreoService execute");
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);

    }

}
