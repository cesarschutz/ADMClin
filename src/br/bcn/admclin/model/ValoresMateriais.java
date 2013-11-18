
package br.bcn.admclin.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class ValoresMateriais {
    private double valor;
    private int valoresMateriaisId, handle_material, usuarioid;
    private Date data, dataAValer;

    public Date getDataAValer() {
        return dataAValer;
    }

    public void setDataAValer(Date dataAValer) {
        this.dataAValer = dataAValer;
    }

    public int getHandle_material() {
        return handle_material;
    }

    public void setHandle_material(int materialId) {
        this.handle_material = materialId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getValoresMateriaisId() {
        return valoresMateriaisId;
    }

    public void setValoresMateriaisId(int valoresMateriaisId) {
        this.valoresMateriaisId = valoresMateriaisId;
    }

    public int getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(int usuarioid) {
        this.usuarioid = usuarioid;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
}
