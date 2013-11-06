/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.cadastros.pessoal.model;

import java.sql.Date;

/**
 *
 * @author BCN
 */
public class responsaveis_tecnicosMODEL {
  
    private int rtId, usuarioId;
    private String nome,conselho,registroConselho,cpf,uf,registroAns;
    private Date data;
    public String getConselho() {
        return conselho;
     }

    public void setConselho(String conselho) {
        this.conselho = conselho;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistroAns() {
        return registroAns;
    }

    public void setRegistroAns(String registroAns) {
        this.registroAns = registroAns;
    }

    public String getRegistroConselho() {
        return registroConselho;
    }

    public void setRegistroConselho(String registroConselho) {
        this.registroConselho = registroConselho;
    }

    public int getRtId() {
        return rtId;
    }

    public void setRtId(int rtId) {
        this.rtId = rtId;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
}