/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.exception;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.ServiceNotAvailableException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;





/**
 *
 * @author gabriel.rios
 */


@Provider
public class ServiciosDigitalesExceptionMapper implements  ResponseExceptionMapper<Exception> { 
    protected static Logger log = Logger.getLogger(ServiciosDigitalesExceptionMapper.class.getName());
  
    public ServiciosDigitalesExceptionMapper(){
      log.log(Level.SEVERE, ">>>ServiciosDigitalesExceptionMapper.ServiciosDigitalesExceptionMapper()");
    
  }
    
  @Override
  public Exception toThrowable(Response response) {
    int status = response.getStatus();                     
    log.log(Level.SEVERE, ">>>ServiciosDigitalesExceptionMapper.toThrowable status="+status);
       
    String msg = getBody(response); 
    RuntimeException re ;
    switch (status) {
      case 404:
      case 502:
      case 504:
          re =  new ValidationException("Servicio NO disponible.");
          break;
      case 500:
          re =  new ValidationException("Error interno en Servidor.");
          break;
      case 400: 
        re =  new ValidationException(msg);         
        break;
      default:
        re = new WebApplicationException(status);   
        break;
    }
    log.log(Level.SEVERE, ">>>ServiciosDigitalesExceptionMapper.toThrowable re.getMessage="+re.getMessage());
    return re;
  }
  
  
  
    private String getBody(Response response) {
        String body = response.readEntity(String.class);
        log.log(Level.SEVERE, ">>>ServiciosDigitalesExceptionMapper.getBody exception.message="+body);
        return body;
    }
    

}
