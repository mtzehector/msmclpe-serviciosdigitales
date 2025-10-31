/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.service;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpIn;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpRequest;
import mx.gob.imss.dpes.serviciosdigitales.exception.CurpInvalidStatusException;
import mx.gob.imss.dpes.serviciosdigitales.exception.RenapoCurpException;
import mx.gob.imss.dpes.renapo.service.RenapoCurpService;
import mx.gob.imss.dpes.serviciosdigitales.models.TrabajadorImss;
import mx.gob.imss.dpes.support.util.ServiceLogger;

/**
 *
 * @author eduardo.montesh
 */
@Provider
public class ObtenDatosRenapo extends ServiceDefinition<TrabajadorImss, TrabajadorImss> {

    @Inject
    RenapoCurpService renapo;
    
    @Inject
    CreateBitacoraService createBitacoraService;


    @Override
    public Message<TrabajadorImss> execute(Message<TrabajadorImss> request) throws BusinessException {
        log.log(Level.INFO, ">>>>ObtenDatosRenapo Request: {0}", request.getPayload());
        TrabajadorImss trabajadorImss = request.getPayload();
        RenapoCurpRequest renapoRequest = new RenapoCurpRequest();
        RenapoCurpIn in = new RenapoCurpIn();
        in.setCurp(request.getPayload().getCurp());
        renapoRequest.setRenapoCurpIn(in);
        Message<RenapoCurpRequest> response = renapo.execute(new Message<>(renapoRequest));
        log.log(Level.INFO, ">>>>  ObtenDatosRenapo response= {0}", response);

        if (!(response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("AN")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("AH")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("CRA")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("RCN")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("RCC"))) {

            List parameters = new LinkedList();
            parameters.add(response.getPayload().getRenapoCurpOut().getDesEstatusCURP());
            createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(trabajadorImss,response.getPayload().getRenapoCurpOut().getDesEstatusCURP(),true)));
            throw new CurpInvalidStatusException(parameters);
        }

        if (Message.isExito(response)) {
            request.getPayload().setDatosCurp(response.getPayload().getRenapoCurpOut());
            //createBitacoraService.createInterfaz(new Message<>(ServiceLogger.log(trabajadorImss,request.getPayload())));
            return request;
        }
        log.log(Level.SEVERE, "!!!   ERROR >>>>ObtenDatosRenapo Request: {0}", request.getPayload());
        return response(null, ServiceStatusEnum.EXCEPCION, new RenapoCurpException(), null);
    }

}
