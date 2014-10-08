package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;

public class imprimirAgendamentosHorariosOcupados {

	ArrayList<Nagendamentos> listaAgendamentosRecebidos;
	
	public imprimirAgendamentosHorariosOcupados(ArrayList<Nagendamentos> listaDeAgendamentos, int handle_agendaRecebida, int handleAreaDeAtendimentoRecebida, String data,String agenda, String areaDeAtendimento) {
		this.listaAgendamentosRecebidos = listaDeAgendamentos;
		filtrarAgendamentosDeAcordoComOsFiltros(handle_agendaRecebida, handleAreaDeAtendimentoRecebida);
		try {
			criarPdf(data,agenda,areaDeAtendimento);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório de Agendamentos");
		}
		
	}
	
	private void filtrarAgendamentosDeAcordoComOsFiltros(int handle_agendaRecebida, int handleAreaDeAtendimentoRecebida){
		//se for todas as agendas ele filtra pela area de atendimento
		//se a area de atendimento tb for zero ele nao faz nada
		if(handle_agendaRecebida == 0){
			if(handleAreaDeAtendimentoRecebida != 0){
				//vamos retirar todos os exames que nao forem da area de atendimento selecionada
				for (Nagendamentos agendamento : listaAgendamentosRecebidos) {
					for (int i = 0; i< agendamento.getListaExames().size(); i++) {
						//se o exame nao é da area selecionada ele retira da lista
						if(agendamento.getListaExames().get(i).getID_AREAS_ATENDIMENTO() != handleAreaDeAtendimentoRecebida){
							agendamento.getListaExames().remove(agendamento.getListaExames().get(i));
						}
					}
				}
			}
		}else{
			for (Nagendamentos agendamento : listaAgendamentosRecebidos) {
				for (int i = 0; i< agendamento.getListaExames().size(); i++) {
					//se o exame nao é da area selecionada ele retira da lista
					if(agendamento.getListaExames().get(i).getNAGDID() != handle_agendaRecebida){
						agendamento.getListaExames().remove(agendamento.getListaExames().get(i));
					}
				}
			}
		}
		
		
		//agora vamos retirar todos os agendamentos que estão nulos
		for (Nagendamentos agendamento : listaAgendamentosRecebidos) {
			if(agendamento.getListaExames().isEmpty() || agendamento.getListaExames() == null){
				listaAgendamentosRecebidos.remove(agendamento);
			}
			
		}
	}
	
	private void criarPdf(String data, String agenda, String areDeAtendimento) throws Exception{
		
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
        
        //cria o cabeçalho
        document.add(criaCabecalho(data, agenda, areDeAtendimento));
        document.add(criaCabecalhoDoAgendamento());	    
        document.add(imprimirLinhaEmBranco());	  
		
		
		
		for (Nagendamentos agendamento : listaAgendamentosRecebidos) {
			document.add(imprimirAgendamento(agendamento));
			for (NagendamentosExames exame : agendamento.getListaExames()) {
				document.add(imprimeExame(exame));
			}
			document.add(imprimirLinha());	
		}
		
		//fecha o documento
		document.close();
		abrirFichaPDF(caminho);
	}
	
	private PdfPTable criaCabecalho(String data, String agenda, String areDeAtendimento) throws Exception{
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
        
        cell = new PdfPCell(new Phrase("Área de Atendimento: " + areDeAtendimento, font));
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
	
	private PdfPTable criaCabecalhoDoAgendamento() throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 9, Font.BOLD);
		
		PdfPTable tabelaAgendamento = new PdfPTable(4);
		tabelaAgendamento.setWidths(new int[] { 25,25,25,25 });
		tabelaAgendamento.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("Paciente", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Telefone", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Celular", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Convênio", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        return tabelaAgendamento;
		
	}
	
	private PdfPTable imprimirAgendamento(Nagendamentos agendamento) throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 9, Font.NORMAL);
		
		PdfPTable tabelaAgendamento = new PdfPTable(4);
		tabelaAgendamento.setWidths(new int[] { 25,25,25,25 });
		tabelaAgendamento.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase(agendamento.getPACIENTE(), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase(agendamento.getTELEFONE(), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase(agendamento.getCELULAR(), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        cell = new PdfPCell(new Phrase(agendamento.getNOME_CONVENIO(), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaAgendamento.addCell(cell);
        
        return tabelaAgendamento;
		
	}
	
	private PdfPTable imprimeExame(NagendamentosExames exame) throws Exception {
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 8, Font.NORMAL);
		
		PdfPTable tabelaExame = new PdfPTable(4);
		tabelaExame.setWidths(new int[] { 2,15,48,35 });
		tabelaExame.setWidthPercentage(100);
		
		cell = new PdfPCell(new Phrase("", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaExame.addCell(cell);
		
		cell = new PdfPCell(new Phrase("-> Horário: " + MetodosUteis.transformarMinutosEmHorario(exame.getHORA()), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaExame.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Exame: " + exame.getNomeExame(), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaExame.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Sala: " + exame.getNomeAgenda(), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaExame.addCell(cell);
        
        
        return tabelaExame;
		
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
	
	private PdfPTable imprimirLinha() throws Exception{
		PdfPCell cell;
		Font font = FontFactory.getFont("Calibri", 10, Font.BOLD);
		
		// tabela de cabeçalho
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidths(new int[] { 100 });
        tabela.setWidthPercentage(100);
        
        cell = new PdfPCell(new Phrase("_________________________________________________________________________________________________", font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
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
