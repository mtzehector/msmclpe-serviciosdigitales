/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.assembler;

import java.text.SimpleDateFormat;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.common.enums.SexoEnum;
import mx.gob.imss.dpes.interfaces.persona.model.PersonaPensionado;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.bdtu.Sexo;

/**
 *
 * @author juan.garfias
 */
@Provider
public class PersonaAssembler extends BaseAssembler<PersonaPensionado, Persona> {

    public Persona assemble(PersonaPensionado persona) {
        Persona pensionado = new Persona();

        pensionado.setNombre(persona.getNombre());
        pensionado.setPrimerApellido(persona.getPrimerApellido());
        pensionado.setSegundoApellido(persona.getSegundoApellido());
        pensionado.setCurp(persona.getCveCurp());
        pensionado.setNss(persona.getNumNss());

        Sexo sexo = new Sexo();
        sexo.setIdSexo(persona.getCveSexo().longValue());
        pensionado.setSexo(sexo);
        String pattern = "d/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(persona.getFecNacimiento());

        pensionado.setFechaNacimiento(date);

        return pensionado;

    }

}
