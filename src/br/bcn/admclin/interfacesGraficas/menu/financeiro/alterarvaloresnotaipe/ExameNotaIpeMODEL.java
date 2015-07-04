package br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe;

import java.sql.Date;

public class ExameNotaIpeMODEL {
	private String ficha;
	private String paciente;
	private String matricula;
	private String exame;
	private String dia;
	private String valor;
	private int atendimtno_exame_id;
	private int handleConvenio;
	private int handleExame;
	private Date dataExame;
	private String NUMERO_REF_NOTA_IPE;

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getExame() {
		return exame;
	}

	public void setExame(String exame) {
		this.exame = exame;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getAtendimtno_exame_id() {
		return atendimtno_exame_id;
	}

	public void setAtendimtno_exame_id(int atendimtno_exame_id) {
		this.atendimtno_exame_id = atendimtno_exame_id;
	}

	public String getNUMERO_REF_NOTA_IPE() {
		return NUMERO_REF_NOTA_IPE;
	}

	public void setNUMERO_REF_NOTA_IPE(String nUMERO_REF_NOTA_IPE) {
		NUMERO_REF_NOTA_IPE = nUMERO_REF_NOTA_IPE;
	}

	public int getHandleConvenio() {
		return handleConvenio;
	}

	public void setHandleConvenio(int handleConvenio) {
		this.handleConvenio = handleConvenio;
	}

	public int getHandleExame() {
		return handleExame;
	}

	public void setHandleExame(int handleExame) {
		this.handleExame = handleExame;
	}

	public Date getDataExame() {
		return dataExame;
	}

	public void setDataExame(Date dataExame) {
		this.dataExame = dataExame;
	}

}
