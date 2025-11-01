/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.config;

/**
 *
 * @author antonio
 */
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.CorreoEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.DomicilioEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.PersonaEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.SelloElectronicoEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.TrabajadorImssEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.PersonaBdtuEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.endpoint.PersonaBdtuOnlyEndPoint.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.CreateCorreoService.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.CreateSelloElectronicoService.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.DomicilioLoadService.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.DomicilioService.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.ObtenDatosRenapo.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.ReadPersonaService.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.TrabajadorActivoService.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.exception.ServiciosDigitalesExceptionMapper.class);
        resources.add(mx.gob.imss.dpes.serviciosdigitales.service.LoadBdtuService.class);
        
    }

}
