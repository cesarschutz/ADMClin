/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.financeiro.relatorios.demed;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class GerarDmedMODEL {
    
    private String nomePaciente, nomeResponsavel, cpfPaciente, cpfResponsavel;
    private int handle_at;
    private double valorPago;
    private Date data;

    public int getHandle_at() {
        return handle_at;
    }

    public void setHandle_at(int handle_at) {
        this.handle_at = handle_at;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getCpfPaciente() {
        if ("   .   .   -  ".equals(cpfPaciente)) {
            cpfPaciente = "";
        }
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public String getCpfResponsavel() {
        if ("   .   .   -  ".equals(cpfResponsavel)) {
            cpfResponsavel = "";
        }
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
    
}
