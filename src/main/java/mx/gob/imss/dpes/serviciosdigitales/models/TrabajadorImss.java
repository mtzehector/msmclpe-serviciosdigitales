/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.serviciosdigitales.models;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpOut;

/**
 *
 * @author juan.garfias
 */
public class TrabajadorImss extends BaseModel {

    @Getter
    @Setter
    private String delegacion;
    @Getter
    @Setter
    private String matricula;
    @Getter
    @Setter
    private String estatus;
    @Getter
    @Setter
    private String curp;
    @Getter
    @Setter
    private RenapoCurpOut datosCurp;
}
