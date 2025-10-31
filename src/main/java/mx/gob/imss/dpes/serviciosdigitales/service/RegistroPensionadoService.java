/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequest;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.BDTURequestOut;
import mx.gob.imss.dpes.interfaces.userdata.model.UserData;
import mx.gob.imss.dpes.interfaces.userdata.model.UserRequest;
import mx.gob.imss.dpes.serviciosdigitales.restclient.RegistroPensionadoClient;
import mx.gob.imss.dpes.support.util.ServiceLogger;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */
@Provider
public class RegistroPensionadoService extends ServiceDefinition<BDTURequest, BDTURequest> {

    @Inject
    @RestClient
    RegistroPensionadoClient service;

    @Inject
    CreateBitacoraService createBitacoraService;

    @Override
    public Message<BDTURequest> execute(Message<BDTURequest> request) throws BusinessException {

        UserRequest userRq = new UserRequest();
        userRq.setCurp(request.getPayload().getBdtuIn().getCurp());
        userRq.setNss(request.getPayload().getBdtuIn().getNss());
        try {
            Response response = service.byCurpAndNss(userRq);

            UserData u = response.readEntity(UserData.class);

            log.log(Level.INFO, "UserData u :\n {0}", u);

            request.getPayload().setUserData(u);
            
             log.log(Level.INFO, "RegistroPensionadoService request.getPayload().getBdtuOut :\n {0}", request.getPayload());
            if (request.getPayload().getBdtuOut().getNombre() == null) {
                BDTURequestOut aux = new BDTURequestOut();
                aux.setNombre(u.getNOMBRE());
                aux.setPrimerApellido(u.getPRIMER_APELLIDO());
                aux.setSegundoApellido(u.getSEGUNDO_APELLIDO());

                aux.setCurp(u.getCVE_CURP());
                aux.setNss(u.getNUM_NSS());
                //aux.setSexo();
                //aux.setFechaNacimiento(u.);
                request.getPayload().setBdtuOut(aux);
                log.log(Level.INFO, "UserData u request.getPayload() :\n {0}", request.getPayload());

            }
            //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(userRq,u)));

        } catch (Exception e) {
            e.printStackTrace();
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(), true)));
            throw new UnknowException();
        }
        return request;
    }

}
