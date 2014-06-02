/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.pesquisarAtendimentos;

/**
 * 
 * @author Cesar Schutz
 */
public class EditarAtendimentosMODEL {
    private String data_atendimento, hora, paciente, medico_solicitante, crm, statusA;
    private int handle_at, flagFaturado;

    public int getFlagFaturado() {
        return flagFaturado;
    }

    public void setFlagFaturado(int flagFaturado) {
        this.flagFaturado = flagFaturado;
    }

    public String getData_atendimento() {
        return data_atendimento;
    }

    public void setData_atendimento(String data_atendimento) {
        this.data_atendimento = data_atendimento;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMedico_solicitante() {
        return medico_solicitante;
    }

    public void setMedico_solicitante(String medico_solicitante) {
        this.medico_solicitante = medico_solicitante;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public int getHandle_at() {
        return handle_at;
    }

    public void setHandle_at(int handle_at) {
        this.handle_at = handle_at;
    }

    public String getStatusA() {
        return statusA;
    }

    public void setStatusA(String statusA) {
        this.statusA = statusA;
    }
}
