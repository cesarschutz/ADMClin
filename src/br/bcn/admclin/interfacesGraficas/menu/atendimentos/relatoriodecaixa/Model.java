package br.bcn.admclin.interfacesGraficas.menu.atendimentos.relatoriodecaixa;

import java.util.Date;

public class Model {
	private Date data;
	private int handle_at;
	private String convenio, paciente, valor_convenio, valor_paciente, valor_total, ususario;
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getHandle_at() {
		return handle_at;
	}
	public void setHandle_at(int handle_at) {
		this.handle_at = handle_at;
	}
	public String getConvenio() {
		return convenio;
	}
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	public String getPaciente() {
		return paciente;
	}
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	public String getValor_convenio() {
		return valor_convenio;
	}
	public void setValor_convenio(String valor_convenio) {
		this.valor_convenio = valor_convenio;
	}
	public String getValor_paciente() {
		return valor_paciente;
	}
	public void setValor_paciente(String valor_paciente) {
		this.valor_paciente = valor_paciente;
	}
	public String getValor_total() {
		return valor_total;
	}
	public void setValor_total(String valor_total) {
		this.valor_total = valor_total;
	}
	public String getUsusario() {
		return ususario;
	}
	public void setUsusario(String ususario) {
		this.ususario = ususario;
	}
	
	
}
