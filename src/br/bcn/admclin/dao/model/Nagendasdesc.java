package br.bcn.admclin.dao.model;

import java.util.ArrayList;

public class Nagendasdesc {

    private int nagdid;
    private String name;
    private String descricao;
    private int ativa;
    private int id_areas_atendimento;
    
    
    public int getNagdid() {
        return nagdid;
    }
    public void setNagdid(int nagdid) {
        this.nagdid = nagdid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String desc) {
        this.descricao = desc;
    }
    public int getAtiva() {
        return ativa;
    }
    public void setAtiva(int ativa) {
        this.ativa = ativa;
    }
    public int getId_areas_atendimento() {
        return id_areas_atendimento;
    }
    public void setId_areas_atendimento(int id_areas_atendimento) {
        this.id_areas_atendimento = id_areas_atendimento;
    }  
}
