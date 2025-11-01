package mx.gob.imss.dpes.serviciosdigitales.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoRequest;
import mx.gob.imss.dpes.serviciosdigitales.service.CreateSelloElectronicoService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;

@Path("/selloElectronico")
@RequestScoped
public class SelloElectronicoEndPoint
    extends BaseGUIEndPoint<SelloElectronicoRequest, SelloElectronicoRequest, SelloElectronicoRequest> {

  @Inject
  private CreateSelloElectronicoService service;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Obtener sello electrónico.",
      description = "Obtener sello electrónico mediante una cadena original.")
  public Response create(SelloElectronicoRequest request) throws BusinessException {
    return toResponse(service.execute(new Message<>(request)));
  }
}
