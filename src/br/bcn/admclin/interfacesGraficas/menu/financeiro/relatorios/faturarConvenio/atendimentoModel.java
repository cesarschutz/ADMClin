/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio;

import java.sql.Date;

/**
 * 
 * @author Cesar Schutz
 */
public class atendimentoModel {

    private int handle_at, handle_convenio, flag_laudo, flag_faturado;
    private String nomePaciente, matricula_convenio, nascimentoPaciente, crmMedico, hora, nomeMedico;
    private Date data_atendimento;
    private int ID_AREAS_ATENDIMENTO;

    
    public int getID_AREAS_ATENDIMENTO() {
		return ID_AREAS_ATENDIMENTO;
	}

	public void setID_AREAS_ATENDIMENTO(int iD_AREAS_ATENDIMENTO) {
		ID_AREAS_ATENDIMENTO = iD_AREAS_ATENDIMENTO;
	}

	public int getHandle_at() {
        return handle_at;
    }

    public void setHandle_at(int handle_at) {
        this.handle_at = handle_at;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getCrmMedico() {
        return crmMedico;
    }

    public void setCrmMedico(String crmMedico) {
        this.crmMedico = crmMedico;
    }

    public String getMatricula_convenio() {
        return matricula_convenio;
    }

    public void setMatricula_convenio(String matricula_convenio) {
        this.matricula_convenio = matricula_convenio;
    }

    public Date getData_atendimento() {
        return data_atendimento;
    }

    public void setData_atendimento(Date data_atendimento) {
        this.data_atendimento = data_atendimento;
    }

    public String getNascimentoPaciente() {
        return nascimentoPaciente;
    }

    public void setNascimentoPaciente(String nascimentoPaciente) {
        this.nascimentoPaciente = nascimentoPaciente;
    }

    public int getHandle_convenio() {
        return handle_convenio;
    }

    public void setHandle_convenio(int handle_convenio) {
        this.handle_convenio = handle_convenio;
    }

    public int getFlag_laudo() {
        return flag_laudo;
    }

    public void setFlag_laudo(int flag_laudo) {
        this.flag_laudo = flag_laudo;
    }

    public int getFlag_faturado() {
        return flag_faturado;
    }

    public void setFlag_faturado(int flag_faturado) {
        this.flag_faturado = flag_faturado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }
}
