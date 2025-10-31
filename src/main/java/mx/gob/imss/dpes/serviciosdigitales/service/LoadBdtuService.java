/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RenapoBDTUException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
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
 * @author gabriel.rios
 */
@Provider
public class LoadBdtuService extends ServiceDefinition<BDTURequest, BDTURequest> {

    @Inject
    @RestClient
    BDTUPersonaClient bdtuPersonaClient;

    @Inject
    CreateBitacoraService createBitacoraService;

    final static String DATE_FORMAT = "dd/MM/yyyy";
    final static DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    public Message<BDTURequest> execute(Message<BDTURequest> request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>>>>LoadBdtuService  Entra al servicio compara BDTU ");

        BDTURequestIn in = new BDTURequestIn();

        in.setCurp(request.getPayload().getBdtuIn().getCurp());

        String nss = String.format("%011d", Long.parseLong(request.getPayload().getBdtuIn().getNss()));

        log.log(Level.INFO, ">>>>>>>>>LoadBdtuService  Antes de ejecutar el servicio bdtu: "
                + request.getPayload().getBdtuIn().getCurp() + " - " + nss);

        Response response = null;
        try {
            response = bdtuPersonaClient.load(request.getPayload().getBdtuIn().getCurp(), nss);
        } 
        catch (UndeclaredThrowableException ex) {
            ex.printStackTrace();
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),"Servicio BDTU no disponible o inalcanzable",true)));
            ExceptionUtils.throwServiceException("BDTU");
        }
        catch (Exception e) {
            String msg = e.getMessage();
            log.log(Level.SEVERE, ">>>>>>>LoadBdtuService  !!!--- ERROR: INIT Exception message={0}", msg);
            e.printStackTrace();
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(),e.getMessage(),true)));
            if (msg!=null && (msg.contains("El NSS no fue localizado en BDTU") || msg.contains("Los datos estadisticos no coinciden entre RENAPO y BDTU"))) {
               throw new RenapoBDTUException(RenapoBDTUException.INVALID_NSS);
            }
            if (msg!=null && (msg.contains("La CURP no se localizo en RENAPO") || msg.contains("La CURP se encuentra en un estatus Baja"))) {
                throw new RenapoBDTUException(RenapoBDTUException.INVALID_CURP);
            }
            if (msg!=null && msg.contains("Servicio NO disponible")) {
                ExceptionUtils.throwServiceException("BDTU");

            }
            throw new UnknowException();

        }
        
        BDTURequestOut out = response.readEntity(BDTURequestOut.class);
        //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload().getBdtuIn(),out)));
        log.log(Level.INFO, ">>>LoadBdtuService  out=" + out);
        request.getPayload().setBdtuOut(out);
        return request;
    }

}
