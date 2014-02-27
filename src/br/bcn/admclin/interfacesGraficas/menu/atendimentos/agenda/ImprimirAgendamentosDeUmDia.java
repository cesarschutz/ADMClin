package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agenda;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.A_Agendamentos;

public class ImprimirAgendamentosDeUmDia {

    Connection con;
    String nomeAgenda;
    Date data;
    String dataString;
    int handle_agenda;
    ArrayList<A_Agendamentos> listaAgendamentos = new ArrayList<A_Agendamentos>();

    public ImprimirAgendamentosDeUmDia(String data, int handle_agenda, String nomeAgenda) throws ParseException {
        this.dataString = data;
        this.nomeAgenda = nomeAgenda;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        this.data = new java.sql.Date(format.parse(data).getTime());
        this.handle_agenda = handle_agenda;
    }

    String caminho;

    private void criandoAPastaParaSalvarOArquivo() {
        if (OSvalidator.isWindows()) {
            caminho = USUARIOS.pasta_raiz + "\\agenda\\";
        } else {
            caminho = USUARIOS.pasta_raiz + "/agenda/";
        }
        File dir = new File(caminho);
        dir.mkdirs();
    }

    public void gerarRelatorio() {
        try {
            criandoAPastaParaSalvarOArquivo();
            getConsultarAgendamentos();
            criandoFatura();
            abrirFichaPDF();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal,
                "Erro ao elaborar Relatório. Procure o Administrador.");
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void getConsultarAgendamentos() throws SQLException {
        con = Conexao.fazConexao();
        ResultSet resultSet = null;
        PreparedStatement stmtQuery =
            con.prepareStatement("select handle_ap, nomePaciente, nomeExame, hora, lado from a_agendamentos where dia = ? and handle_agenda = ?");
        stmtQuery.setDate(1, data);
        stmtQuery.setInt(2, handle_agenda);
        resultSet = stmtQuery.executeQuery();
        while (resultSet.next()) {
            A_Agendamentos agendamento = new A_Agendamentos();
            agendamento.setHANDLE_AP(resultSet.getInt("handle_ap"));
            agendamento.setNomePaciente(resultSet.getString("nomePaciente"));
            agendamento.setNomeExame(resultSet.getString("nomeExame") + " - " + resultSet.getString("lado"));
            agendamento.setHORA(resultSet.getInt("hora"));
            listaAgendamentos.add(agendamento);
        }
    }

    public void criandoFatura() throws FileNotFoundException, DocumentException {
        Rectangle rect = new Rectangle(PageSize.A4);
        Document document = new Document(rect, 20, 20, 20, 20); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream(caminho + "AgendamentosDoDia.pdf"));
        document.open();

        Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
        Font font9 = FontFactory.getFont("Calibri", 9, Font.NORMAL);
        Font fontNegrito9 = FontFactory.getFont("Calibri", 9, Font.BOLD);
        
        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable tablePrincipal = new PdfPTable(1);
        tablePrincipal.setWidths(new int[] { 100 });
        tablePrincipal.setWidthPercentage(100);

        // colocando o cabeçalho
        cell = new PdfPCell(new Phrase("Agendamentos", fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Agenda: " + nomeAgenda, fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando a data atual
        cell = new PdfPCell(new Phrase("Data: " + dataString, fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // colocando linha em branco
        cell = new PdfPCell(new Phrase("", font9));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);

        // adicionando tabela ao documento
        document.add(tablePrincipal);
        
        
        //adicionando os titulos das colunas
        PdfPTable tabelaCabecalho = new PdfPTable(4);
        tabelaCabecalho.setWidths(new int[] { 9, 9, 41, 41 });
        tabelaCabecalho.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("NÚMERO", fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("HORA", fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("PACIENTE", fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);

        cell = new PdfPCell(new Phrase("EXAME", fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);
        
        document.add(tabelaCabecalho);
        
        int handle_ap = 0;
        //adicionando os exames
        for (int i = 0; i < listaAgendamentos.size(); i++) {
            PdfPTable tabelaAgendamentos = new PdfPTable(4);
            tabelaAgendamentos.setWidths(new int[] { 9, 9, 41, 41 });
            tabelaAgendamentos.setWidthPercentage(100);
            
            String nomePaciente;
            String hora;
            if(handle_ap != listaAgendamentos.get(i).getHANDLE_AP()){
                //adicona uma linha pois vai trocar de paciente
                cell = new PdfPCell(new Phrase("", font9));
                cell.setBorder(Rectangle.ALIGN_BOTTOM);
                cell.setColspan(4);
                tabelaAgendamentos.addCell(cell);
                
                handle_ap = listaAgendamentos.get(i).getHANDLE_AP();
                nomePaciente = listaAgendamentos.get(i).getNomePaciente();
                hora = MetodosUteis.transformarMinutosEmHorario(listaAgendamentos.get(i).getHORA());
            }else{
                nomePaciente = "";
                hora = "";
            }
            
            cell = new PdfPCell(new Phrase(String.valueOf(i), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaAgendamentos.addCell(cell);

            cell = new PdfPCell(new Phrase(hora, font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabelaAgendamentos.addCell(cell);
            
            cell = new PdfPCell(new Phrase(nomePaciente, font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaAgendamentos.addCell(cell);

            cell = new PdfPCell(new Phrase(listaAgendamentos.get(i).getNomeExame(), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaAgendamentos.addCell(cell);
            
            document.add(tabelaAgendamentos);
        }
        
        
        
        //fechando o documento
        document.close();
        
        
    }

    private void abrirFichaPDF() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if (OSvalidator.isWindows()) {
            runtime.exec("cmd /c \"" + caminho + "AgendamentosDoDia.pdf");
        } else if (OSvalidator.isMac()) {
            runtime.exec("open " + caminho + "AgendamentosDoDia.pdf");
        } else {
            runtime.exec("gnome-open " + caminho + "AgendamentosDoDia.pdf");
        }
    }
}
