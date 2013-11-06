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
public class a_agendamentosMODEL {
    
    private Date dia,nascimento,dat;
    private int HANDLE_AGENDA,HANDLE_AP,HORA,HANDLE_PACIENTE,HANDLE_CONVENIO,HANDLE_EXAME,USUARIOID,DURACAODOEXAME,duracaoAgendamento;
    private String nomePaciente,telefone,celular,nomeConvenio,nomeExame,observacao,cpfPaciente,lado, material, modalidade, ch_convenio, filme_convenio, ch1_exame, ch2_exame, filme_exame, lista_materiais;

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    
    public int getDURACAODOEXAME() {
        return DURACAODOEXAME;
    }

    public void setDURACAODOEXAME(int DURACAODOEXAME) {
        this.DURACAODOEXAME = DURACAODOEXAME;
    }

    public int getHANDLE_AGENDA() {
        return HANDLE_AGENDA;
    }

    public void setHANDLE_AGENDA(int HANDLE_AGENDA) {
        this.HANDLE_AGENDA = HANDLE_AGENDA;
    }

    public int getHANDLE_AP() {
        return HANDLE_AP;
    }

    public void setHANDLE_AP(int HANDLE_AP) {
        this.HANDLE_AP = HANDLE_AP;
    }

    public int getHANDLE_CONVENIO() {
        return HANDLE_CONVENIO;
    }

    public void setHANDLE_CONVENIO(int HANDLE_CONVENIO) {
        this.HANDLE_CONVENIO = HANDLE_CONVENIO;
    }

    public int getHANDLE_EXAME() {
        return HANDLE_EXAME;
    }

    public void setHANDLE_EXAME(int HANDLE_EXAME) {
        this.HANDLE_EXAME = HANDLE_EXAME;
    }

    public int getHANDLE_PACIENTE() {
        return HANDLE_PACIENTE;
    }

    public void setHANDLE_PACIENTE(int HANDLE_PACIENTE) {
        this.HANDLE_PACIENTE = HANDLE_PACIENTE;
    }

    public int getHORA() {
        return HORA;
    }

    public void setHORA(int HORA) {
        this.HORA = HORA;
    }

    public int getUSUARIOID() {
        return USUARIOID;
    }

    public void setUSUARIOID(int USUARIOID) {
        this.USUARIOID = USUARIOID;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getNomeConvenio() {
        return nomeConvenio;
    }

    public void setNomeConvenio(String nomeConvenio) {
        this.nomeConvenio = nomeConvenio;
    }

    public String getNomeExame() {
        return nomeExame;
    }

    public void setNomeExame(String nomeExame) {
        this.nomeExame = nomeExame;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getDuracaoAgendamento() {
        return duracaoAgendamento;
    }

    public void setDuracaoAgendamento(int duracaoAgendamento) {
        this.duracaoAgendamento = duracaoAgendamento;
    }

    public String getLado() {
        return lado;
    }

    public void setLado(String lado) {
        this.lado = lado;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getCh_convenio() {
        return ch_convenio;
    }

    public void setCh_convenio(String ch_convenio) {
        this.ch_convenio = ch_convenio;
    }

    public String getFilme_convenio() {
        return filme_convenio;
    }

    public void setFilme_convenio(String filme_convenio) {
        this.filme_convenio = filme_convenio;
    }

    public String getCh1_exame() {
        return ch1_exame;
    }

    public void setCh1_exame(String ch1_exame) {
        this.ch1_exame = ch1_exame;
    }

    public String getCh2_exame() {
        return ch2_exame;
    }

    public void setCh2_exame(String ch2_exame) {
        this.ch2_exame = ch2_exame;
    }

    public String getFilme_exame() {
        return filme_exame;
    }

    public void setFilme_exame(String filme_exame) {
        this.filme_exame = filme_exame;
    }

    public String getLista_materiais() {
        return lista_materiais;
    }

    public void setLista_materiais(String lista_materiais) {
        this.lista_materiais = lista_materiais;
    }
}
