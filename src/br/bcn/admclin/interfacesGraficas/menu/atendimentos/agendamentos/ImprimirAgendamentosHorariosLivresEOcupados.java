package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.MultiSplitLayout.Split;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.NAGENDASDESC;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Nagenda;
import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;
import br.bcn.admclin.dao.model.Nagendasdesc;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ImprimirAgendamentosHorariosLivresEOcupados {

ArrayList<Nagendamentos> listaAgendamentosRecebidos;
	
	public ImprimirAgendamentosHorariosLivresEOcupados(ArrayList<Nagendamentos> listaDeAgendamentos, String data, ArrayList<Nagendasdesc> listaDeAgendas) {
		this.listaAgendamentosRecebidos = listaDeAgendamentos;
		try {
			criarPdf(data, listaDeAgendas);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório de Agendamentos");
		}
		
	}
		
	private void criarPdf(String data, ArrayList<Nagendasdesc> listaDeAgendas) throws Exception{
		
		//criamos a apsta para salvar o arquivo!!
		String caminho;
	    if (OSvalidator.isWindows()) {
	        caminho = USUARIOS.pasta_raiz + "\\relatorioDeAgendamentos\\";
	    } else {
	        caminho = USUARIOS.pasta_raiz + "/relatorioDeAgendamentos/";
	    }
	    File dir = new File(caminho);
	    dir.mkdirs();
	    
	    
	    //cria o pdf
	    Rectangle rect = new Rectangle(PageSize.A4);
        Document document = new Document(rect, 20, 20, 20, 20); // colocar as margens
        File arquivo = new File(caminho + "relatorioDeAgendamentos.pdf");
        if(arquivo.exists()){
        	arquivo.delete();
        }
        PdfWriter.getInstance(document, new FileOutputStream(caminho + "relatorioDeAgendamentos.pdf"));
        document.open();
        
        for (Nagendasdesc agenda : listaDeAgendas) {
        	if(agenda.getNagdid() != 0){
        		
        		//vamos consultar os horarios dos turnos da agenda de acordo com o seu weekday
        		String[] diaMesAno = data.split("/");
        		
        		Calendar calendario = new GregorianCalendar(Integer.valueOf(diaMesAno[2]), Integer.valueOf(diaMesAno[1]) - 1, Integer.valueOf(diaMesAno[0]));  
        	    int weekDay = calendario.get(Calendar.DAY_OF_WEEK);
        	    
        	    //buscar os turnos da agenda
        	    //ira retornar um array de inteiros
        	    //com todos os turnos e o periodo
        	    ArrayList<Nagenda> turnosDaAgenda = NAGENDASDESC.getTurnosDaAgenda(agenda.getNagdid());
        	    ArrayList<Nagenda> turnosDaAgendaComWeekDayCorreto = new ArrayList<>();
        	    for (int i = 0; i < turnosDaAgenda.size(); i++) {
        	    	if(turnosDaAgenda.get(i).getWeekday() == weekDay){
        	    		turnosDaAgendaComWeekDayCorreto.add(turnosDaAgenda.get(i));
					}
				}
        	    //paramos caso retorne mais de um turno de acordo com o weekday, pq dai algo tem errado
        	    if(turnosDaAgendaComWeekDayCorreto.size() != 1){
        	    	JOptionPane.showMessageDialog(null, "Erro ao consultar Informações da Agenda. Procure o Administrador.");
        	    	break;
        	    }
        		
        		int startTurno1 = turnosDaAgendaComWeekDayCorreto.get(0).getStart1();
        		int endTurno1 = turnosDaAgendaComWeekDayCorreto.get(0).getEnd1();
        		int startTurno2 = turnosDaAgendaComWeekDayCorreto.get(0).getStart2();
        		int endTurno2 = turnosDaAgendaComWeekDayCorreto.get(0).getEnd2();
        		int startTurno3 = turnosDaAgendaComWeekDayCorreto.get(0).getStart3();
        		int endTurno3 = turnosDaAgendaComWeekDayCorreto.get(0).getEnd3();
        		int startTurno4 = turnosDaAgendaComWeekDayCorreto.get(0).getStart4();
        		int endTurno4 = turnosDaAgendaComWeekDayCorreto.get(0).getEnd4();
        		
        		int periodoAgenda = turnosDaAgendaComWeekDayCorreto.get(0).getDuracao();
        		
        		//cria o cabeçalho
                document.add(criaCabecalho(data, agenda.getName()));
                document.add(criaCabecalhoDosExames());	    
                document.add(imprimirLinhaEmBranco());	
        		
        		for (int i = 0; i < 4; i++) {
        			int horaInicialDoTurno;
                	int horaFinalDoTurno;
                	
                	//definie as horas que vao serem trabalhas
					if(i == 0){
						horaInicialDoTurno = startTurno1;
	                	horaFinalDoTurno = endTurno1;
	                	document.add(imprimirLinha("1"));
					}else if(i == 1){
						horaInicialDoTurno = startTurno2;
	                	horaFinalDoTurno = endTurno2;
	                	document.add(imprimirLinha("2"));
					}else if(i == 2){
						horaInicialDoTurno = startTurno3;
	                	horaFinalDoTurno = endTurno3;
	                	document.add(imprimirLinha("3"));
					}else{
						horaInicialDoTurno = startTurno4;
	                	horaFinalDoTurno = endTurno4;
	                	document.add(imprimirLinha("4"));
					}
					
					if(horaInicialDoTurno != 0 || horaFinalDoTurno != 0){
						//agora ira imprimir o turno
						for (int horaParaImprimir = horaInicialDoTurno; horaParaImprimir <= horaFinalDoTurno; horaParaImprimir = horaParaImprimir + periodoAgenda) {
		                	String nomePaciente = "";
		                	String nomeExame    = "";
		                	for (Nagendamentos agendamento : listaAgendamentosRecebidos) {
		        				for (NagendamentosExames exame : agendamento.getListaExames()) {
		        					if(exame.getNAGDID() == agenda.getNagdid() && exame.getHORA() == horaParaImprimir){
		        						nomePaciente = agendamento.getPACIENTE();
		        						nomeExame    = exame.getNomeExame();
		        					}
		        				}
		        			}
		                	document.add(imprimirLinhaDeHorario(MetodosUteis.transformarMinutosEmHorario(horaParaImprimir), nomePaciente, nomeExame));
						}						
					}
				}
                document.add(Chunk.NEXTPAGE);
        	} 
		}	
		//fecha o documento
		document.close();
		abrirFichaPDF(caminho);
	}
	
	private PdfPTable criaCabecalho(String data, String agenda) throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 10, Font.BOLD);
		
		// tabela de cabeçalho
        PdfPTable tablePrincipal = new PdfPTable(1);
        tablePrincipal.setWidths(new int[] { 100 });
        tablePrincipal.setWidthPercentage(100);
        
        cell = new PdfPCell(new Phrase("Relatório de Agendamentos", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Data: " + data, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Agenda: " + agenda, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        return tablePrincipal;
	}
	
	private PdfPTable criaCabecalhoDosExames() throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 9, Font.BOLD);
		
		PdfPTable tabelaAgendamento = new PdfPTable(3);
		tabelaAgendamento.setWidths(new int[] { 7,46,47 });
		tabelaAgendamento.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("Hora", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Paciente", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Exame", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        return tabelaAgendamento;
		
	}
	
	private PdfPTable imprimirLinhaDeHorario(String hora, String paciente, String exame) throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 9, Font.NORMAL);
		
		PdfPTable tabelaAgendamento = new PdfPTable(3);
		tabelaAgendamento.setWidths(new int[] { 7,46,47 });
		tabelaAgendamento.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase(hora, font));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase(paciente, font));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase(exame, font));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
       
        
        return tabelaAgendamento;
		
	}
	
	private PdfPTable imprimirLinhaEmBranco() throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 10, Font.BOLD);
		
		// tabela de cabeçalho
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidths(new int[] { 100 });
        tabela.setWidthPercentage(100);
        
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabela.addCell(cell);
        
        cell = new PdfPCell(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabela.addCell(cell);
        
        return tabela;
	}
	
	private PdfPTable imprimirLinha(String numeroTurno) throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 9, Font.NORMAL);
		
		// tabela de cabeçalho
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidths(new int[] { 100 });
        tabela.setWidthPercentage(100);
        
        cell = new PdfPCell(new Phrase("TURNO " + numeroTurno, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tabela.addCell(cell);
        
        return tabela;
	}
	
	private void abrirFichaPDF(String caminho) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if (OSvalidator.isWindows()) {
            runtime.exec("cmd /c \"" + caminho + "relatorioDeAgendamentos.pdf\"");
        } else if (OSvalidator.isMac()) {
            runtime.exec("open " + caminho + "relatorioDeAgendamentos.pdf");
        } else {
            runtime.exec("gnome-open " + caminho + "relatorioDeAgendamentos.pdf");
        }
    }
}
