/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo2.notaFiscal;

/**
 *
 * @author BCN
 */
public class ExameModel {
    private String cod_exame, nome, valor_correto_paciente, modalidade;

    public String getCod_exame() {
        return cod_exame;
    }

    public void setCod_exame(String cod_exame) {
        this.cod_exame = cod_exame;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor_correto_paciente() {
        return valor_correto_paciente;
    }

    public void setValor_correto_paciente(String valor_correto_paciente) {
        this.valor_correto_paciente = valor_correto_paciente;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }
}
