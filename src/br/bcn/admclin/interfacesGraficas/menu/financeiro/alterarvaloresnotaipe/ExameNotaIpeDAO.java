package br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe;

import java.util.ArrayList;

public class ExameNotaIpeDAO {
	
	public static ArrayList<ExameNotaIpeMODEL> getExamesPorNota(int numeroNota){
		ArrayList<ExameNotaIpeMODEL> listaqExames = new ArrayList<>();
		ExameNotaIpeMODEL exame = new ExameNotaIpeMODEL();
		exame.setDia("02");
		exame.setExame("exameeeee");
		exame.setFicha("123456");
		exame.setMatricula("654687484684");
		exame.setPaciente("Cesar Schutz");
		exame.setValor("65.44");
		listaqExames.add(exame);
		
		ExameNotaIpeMODEL exameDois = new ExameNotaIpeMODEL();
		exameDois.setDia("02");
		exameDois.setExame("EXAME REALIZADO");
		exameDois.setFicha("6585");
		exameDois.setMatricula("11111111");
		exameDois.setPaciente("Cesar Fagundes");
		exameDois.setValor("45.02");
		listaqExames.add(exameDois);
		
		return listaqExames;
	}
}
