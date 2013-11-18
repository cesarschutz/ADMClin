package br.bcn.admclin.model;

import java.sql.Date;

/**
 * Classe MODEL da tabela MATERIAIS.
 * @author BCN
 */
public class Materiais {
    
    private int handle_material, usuarioId;
    private String nome, codigo;
    private Date data;

   
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getHandle_material() {
        return handle_material;
    }

    public void setHandle_material(int materialId) {
        this.handle_material = materialId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuario) {
        this.usuarioId = usuario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
