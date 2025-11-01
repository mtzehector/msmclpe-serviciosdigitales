/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.exception;

/**
 *
 * @author gabriel.rios
 */
public class RenapoBDTUInfoNotMacthingException extends LocalBusinessException {
    public final static String KEY = "msg346";
    public final static String INVALID_CURP = "msg357";
    public final static String INVALID_NSS = "msg358";
    
    public RenapoBDTUInfoNotMacthingException() {
        super(KEY);
    }
    
    public RenapoBDTUInfoNotMacthingException(String causa) {
        super(causa);
    }
}
