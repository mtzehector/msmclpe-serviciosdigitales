/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.exception;

import java.util.List;

/**
 *
 * @author antonio
 */
public class PersonaNoRegistradaException extends LocalBusinessException {

    private final static String KEY = "msg001";

    public PersonaNoRegistradaException() {
        super(KEY);
    }

    public PersonaNoRegistradaException(List parameters) {
        super(KEY);
        super.addParameters(parameters);

    }

    public PersonaNoRegistradaException(String causa) {
        super(causa);
    }

}
