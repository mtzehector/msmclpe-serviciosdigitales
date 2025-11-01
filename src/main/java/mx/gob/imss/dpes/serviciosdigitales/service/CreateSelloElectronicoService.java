package mx.gob.imss.dpes.serviciosdigitales.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoRequest;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.domicilio.model.Domicilio;
import mx.gob.imss.dpes.serviciosdigitales.restclient.SelloElectronicoClient;
import mx.gob.imss.dpes.support.util.ServiceLogger;

@Provider
public class CreateSelloElectronicoService extends ServiceDefinition<SelloElectronicoRequest, SelloElectronicoResponse> {

    @Inject
    @RestClient
    private SelloElectronicoClient selloElectronicoClient;

    @Inject
    CreateBitacoraService createBitacoraService;

    @Override
    public Message<SelloElectronicoResponse> execute(Message<SelloElectronicoRequest> request) throws BusinessException {
        log.log(Level.INFO, ">>> CreateSelloElectronicoService Request hacia SelloElectronico Servicios Digitales json[SelloElectronicoRequest]=\n''{0}''", getJSonStr(request.getPayload()));
        try {
            Response sello = selloElectronicoClient.create(request.getPayload());

            if (sello.getStatus() == 200) {
                SelloElectronicoResponse selloResponse = sello.readEntity(SelloElectronicoResponse.class);
                createBitacoraService.createInterfaz(new Message<>(ServiceLogger.creaObjetoBI(
                        request.getPayload(),
                        selloResponse,
                        "/util/firmaElectronica/selloDigital",
                        true,null
                )
                )
                );
                return new Message<>(selloResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(request.getPayload(), e.getMessage(), 7L, true)));
            log.log(Level.SEVERE, ">>>CreateSelloElectronicoService");
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);

    }

    /*
  public String generarCadenaOriginal(SelloElectronicoRequest selloElectronicoRequest) {
    return new StringBuilder("{\"cadenaOriginal\":\"||")
        .append(SelloElectronicoEnum.NSS.getDescripcion()).append(":").append(selloElectronicoRequest.getNss()).append("|")
        .append(SelloElectronicoEnum.FOLIOPRESTAMO.getDescripcion()).append(":").append(selloElectronicoRequest.getFolioNegocio()).append("|")
        .append(SelloElectronicoEnum.FECSOLICITUDPRESTAMO.getDescripcion()).append(":").append(selloElectronicoRequest.getFecCreacion()).append("|")
        .append(SelloElectronicoEnum.TIPOCREDITO.getDescripcion()).append(":").append(selloElectronicoRequest.getTipoCredito())
        .append("||\",\"rfc\":\".\"}").toString();
  }

  public String generarRefSello(SelloElectronicoResponse selloElectronicoResponse) {
    return new StringBuilder("{")
        .append("\"").append(SelloElectronicoEnum.REF_SELLO_DIGITAL.getDescripcion()).append("\":\"")
        .append(selloElectronicoResponse.getSello()).append("\",")
        .append("\"").append(SelloElectronicoEnum.NUM_SEQ_NOTARIA.getDescripcion()).append("\":\"")
        .append(selloElectronicoResponse.getId()).append("\",")
        .append("\"").append(SelloElectronicoEnum.REF_NUM_SERIE_CERTIFICADO.getDescripcion()).append("\":\"")
        .append(selloElectronicoResponse.getNoSerie()).append("\"")
        .append("}").toString();
  }
  * */
    protected static String getJSonStr(SelloElectronicoRequest request) {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = Obj.writeValueAsString(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
