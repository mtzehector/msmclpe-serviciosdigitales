package mx.gob.imss.dpes.serviciosdigitales.service;

import java.util.logging.Level;
import mx.gob.imss.dpes.common.model.Message;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.persona.model.PersonaPensionado;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.serviciosdigitales.assembler.PersonaAssembler;
import mx.gob.imss.dpes.serviciosdigitales.exception.PersonaNoRegistradaException;
import mx.gob.imss.dpes.serviciosdigitales.restclient.PersonaClient;
import mx.gob.imss.dpes.serviciosdigitales.restclient.PersonaEnrolamientoClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import mx.gob.imss.dpes.support.util.ServiceLogger;

@Provider
public class ReadPersonaService extends ServiceDefinition<Persona, Persona> {

//    @Inject
//    @RestClient
//    private PersonaEnrolamientoClient personaEnrolamientoClient;

    @Inject
    CreateBitacoraService createBitacoraService;

    @Inject
    @RestClient
    private PersonaClient personaClient;

    @Inject
    PersonaAssembler pa;

    @Override
    public Message<Persona> execute(Message<Persona> request) throws BusinessException {
        log.log(Level.INFO, ">>> ReadPersonaService Request hacia /personas Servicios Digitales: {0}", request.getPayload().getCurp());
        Response response = null;
        try {
            response = personaClient.getPersona(request.getPayload().getCurp());
            //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),response)));

        } catch (RuntimeException e) {
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(), true)));
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());

            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: response=\n{0}", response);

            if (e.getMessage().contains("Unknown error, status code 400") || e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "ServiciosDigitales-rest");
                ExceptionUtils.throwServiceException("ServiciosDigitales-rest");
            }
            if (e.getMessage().contains("Unknown error, status code 500")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Persona No Registrada");
                throw new PersonaNoRegistradaException();
            }
            throw new UnknowException();
        }

        return response(response, request);
    }

    @Override
    protected Message<Persona> onOk(Response response, Message<Persona> request) {
//        Persona persona = response.readEntity(Persona.class);
        PersonaPensionado pp = response.readEntity(PersonaPensionado.class);
        log.log(Level.INFO, "CONSULTA NUEVA, PERSONA A BASE LOCAL JGV PersonaPensionado: {0}", pp);
        Persona persona = pa.assemble(pp);
        log.log(Level.INFO, "CONSULTA NUEVA, PERSONA A BASE LOCAL JGV Persona: {0}", persona);
        request.setPayload(persona);
        return request;
    }

}
