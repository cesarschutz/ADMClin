package br.bcn.admclin.dao.model;

public class Nagendadayblock {

	private int AGENDADBID;
	private int NAGDID; //handle agenda
	private int WEEKDAY;
	private int HORARIO;
	
	public int getAGENDADBID() {
		return AGENDADBID;
	}
	public void setAGENDADBID(int aGENDADBID) {
		AGENDADBID = aGENDADBID;
	}
	public int getNAGDID() {
		return NAGDID;
	}
	public void setNAGDID(int nAGDID) {
		NAGDID = nAGDID;
	}
	public int getWEEKDAY() {
		return WEEKDAY;
	}
	public void setWEEKDAY(int wEEKDAY) {
		WEEKDAY = wEEKDAY;
	}
	public int getHORARIO() {
		return HORARIO;
	}
	public void setHORARIO(int hORARIO) {
		HORARIO = hORARIO;
	}	
}
