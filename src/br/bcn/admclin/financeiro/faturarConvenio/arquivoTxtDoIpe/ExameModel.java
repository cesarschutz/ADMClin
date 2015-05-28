/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.financeiro.faturarConvenio.arquivoTxtDoIpe;

/**
 * Classe feita para ser utilizada no arquivo TXT do IPE, para criar o arquivo TXT de faturamento do IPE!
 * 
 * @author Cesar Schutz
 */
public class ExameModel {

    // variaveis necessarias para a linha de lançamento
    private String matricula; // matricula_convenio //matricula do paciente no convenio
    private String crm; // crm do medico solicitante
    private String dia; // dia do atendimento
    private String cod_exame; // cod do exame na tabela "tabelas"
    private String qtd; // sempre 1
    private String nome_paciente; // nome do beneficiario
    private String handle_at; // handle_at do atendimento

    // variaveis necessarias para o designativo
    private double valor_correto_convenio; // valor a ser cobrado do convenio
    private double valor_materiais; // valor total dos materiais e medicamentos utilizados
	private String mes; // mes dos exames
    private String ano; // ano dos exames
    private String nn; // numero da nota que o exame se encontra
    private String ref; // numero da linha na nota
    private int ATENDIMENTO_EXAME_ID; //id - para quando construir a nota do ipe atualizar o numero da nota e o numero de referencia (linha) da nota

    public String getHandle_at() {
        return handle_at;
    }

    public void setHandle_at(String handle_at) {
        this.handle_at = handle_at;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getCod_exame() {
        return cod_exame;
    }

    public void setCod_exame(String cod_exame) {
        this.cod_exame = cod_exame;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public String getNome_paciente() {
        return nome_paciente;
    }

    public void setNome_paciente(String nome_paciente) {
        this.nome_paciente = nome_paciente;
    }

    public double getValor_correto_convenio() {
        return valor_correto_convenio;
    }

    public void setValor_correto_convenio(double valor_correto_convenio) {
        this.valor_correto_convenio = valor_correto_convenio;
    }

    public double getValor_materiais() {
        return valor_materiais;
    }

    public void setValor_materiais(double valor_materiais) {
        this.valor_materiais = valor_materiais;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }
    
    public int getATENDIMENTO_EXAME_ID() {
		return ATENDIMENTO_EXAME_ID;
	}

	public void setATENDIMENTO_EXAME_ID(int aTENDIMENTO_EXAME_ID) {
		ATENDIMENTO_EXAME_ID = aTENDIMENTO_EXAME_ID;
	}

}
