/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.atendimentos.agenda.model;

import java.sql.Date;

/**
 *
 * @author CeSaR
 */
public class pacientesMODEL {
    
    Date dat;
    int HANDLE_PACIENTE,usuarioId;
    String telefone,celular;

    public int getHANDLE_PACIENTE() {
        return HANDLE_PACIENTE;
    }

    public void setHANDLE_PACIENTE(int HANDLE_PACIENTE) {
        this.HANDLE_PACIENTE = HANDLE_PACIENTE;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
}
