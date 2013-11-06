/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.cadastros.convenio.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class conveniosFilmeMODEL {
    
    private int convenioFilmeId, usuarioId, handle_convenio;
    private Date dat, dataAValer;
    private String valor;

    public int convenioFilmeId() {
        return convenioFilmeId;
    }

    public void setConvenioChId(int convenioChId) {
        this.convenioFilmeId = convenioChId;
    }

    public int getConvenioFilmeId() {
        return convenioFilmeId;
    }

    public int getHandle_convenio() {
        return handle_convenio;
    }

    public void setHandle_convenio(int handle_convenio) {
        this.handle_convenio = handle_convenio;
    }

    public void setConvenioFilmeId(int convenioFilmeId) {
        this.convenioFilmeId = convenioFilmeId;
    }
    
    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public Date getDataAValer() {
        return dataAValer;
    }

    public void setDataAValer(Date dataAValer) {
        this.dataAValer = dataAValer;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
