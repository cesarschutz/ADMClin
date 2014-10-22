package br.bcn.admclin.dao.model;

import java.sql.Date;

/**
 * Modelo de Exame.
 * @author BCN
 */
public class Exames {
    private int HANDLE_EXAME, HANDLE_CLASSEDEEXAME, usuarioId, duracao, id_areas_atendimento, area_do_corpo, flag_desativado;
    private String NOME, qtdHoras, laudo, dieta;
    Date data;

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public int getHANDLE_EXAME() {
        return HANDLE_EXAME;
    }

    public void setHANDLE_EXAME(int HANDLE_EXAME) {
        this.HANDLE_EXAME = HANDLE_EXAME;
    }

    public String getLaudo() {
        return laudo;
    }

    public void setLaudo(String laudo) {
        this.laudo = laudo;
    }
    
    public String getQtdHoras() {
        return qtdHoras;
    }

    public void setQtdHoras(String qtdHoras) {
        this.qtdHoras = qtdHoras;
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

    public int getHANDLE_CLASSEDEEXAME() {
        return HANDLE_CLASSEDEEXAME;
    }

    public void setHANDLE_CLASSEDEEXAME(int HANDLE_CLASSEDEEXAME) {
        this.HANDLE_CLASSEDEEXAME = HANDLE_CLASSEDEEXAME;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getId_areas_atendimento() {
        return id_areas_atendimento;
    }

    public void setId_areas_atendimento(int id_areas_atendimento) {
        this.id_areas_atendimento = id_areas_atendimento;
    }

    public int getArea_do_corpo() {
        return area_do_corpo;
    }

    public void setArea_do_corpo(int area_do_corpo) {
        this.area_do_corpo = area_do_corpo;
    }

	public String getDieta() {
		return dieta;
	}

	public void setDieta(String dieta) {
		try {
			this.dieta = dieta.replaceAll("\\n", "\\[\\]");
		} catch (NullPointerException e) {
			this.dieta = dieta;
		}
	}

	public int getFlag_desativado() {
		return flag_desativado;
	}

	public void setFlag_desativado(int flag_desativado) {
		this.flag_desativado = flag_desativado;
	}
}
