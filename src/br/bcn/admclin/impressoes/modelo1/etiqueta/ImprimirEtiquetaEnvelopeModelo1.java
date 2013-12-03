/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.impressoes.modelo1.etiqueta;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.ATENDIMENTOS;
import br.bcn.admclin.dao.Conexao;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Cesar Schutz
 */
public class ImprimirEtiquetaEnvelopeModelo1 {

    // variaveis que vamos utilizar para a construção do pdf
    Connection con = null;
    int handle_at;

    public ImprimirEtiquetaEnvelopeModelo1(int handle_at) {
        this.handle_at = handle_at;
    }

    private void criandoAPastaParaSalvarOArquivo() {
        File dir = new File("C:\\ADMClin\\etiquetas");
        dir.mkdirs();
    }

    @SuppressWarnings("finally")
    public boolean salvarEIMprimirEtiqueta() {
        boolean conseguiuSalvarEAbrirEtiqueta = false;
        con = Conexao.fazConexao();
        try {
            criandoAPastaParaSalvarOArquivo();
            // pegando informações do atgendimento
            buscarInformaçõesDoAtendimento();
            criarEtiquetaPdf();
            abrirEtiquetaPDF();
            conseguiuSalvarEAbrirEtiqueta = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Etiqueta. Procure o Administrador." + " 000001"
                + e, "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (DocumentException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível Criar Etiqueta. Procure o Administrador." + " 000002", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            // esse erro pode ocorrer caso o arquivo esteja aberto
            JOptionPane.showMessageDialog(null,
                "Não foi possível Criar Etiqueta. Procure o Administrador." + " 000003", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível Abrir Etiqueta. Procure o Administrador." + " 000004", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
            return conseguiuSalvarEAbrirEtiqueta;
        }
    }

    // variaveis da tabela atendimentos
    private String nome_paciente, nome_medico_sol;
    private String modalidade;
    private String data_atendimento, hora_atendimento;

    private void buscarInformaçõesDoAtendimento() throws SQLException {
        // buscando as informações do atendimento

        ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_at);
        while (resultSet.next()) {
            nome_paciente = resultSet.getString("nomePac");
            nome_medico_sol = resultSet.getString("nomeMed");

            data_atendimento = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_atendimento"));
            hora_atendimento = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento"));
            modalidade = resultSet.getString("modalidade");

        }
    }

    private void criarEtiquetaPdf() throws DocumentException, FileNotFoundException {
        Rectangle rect = new Rectangle(246, 110);
        Document document = new Document(rect, 0, 0, 0, 0); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream("C:\\ADMClin\\etiquetas\\Etiqueta" + handle_at + ".pdf"));
        document.open();

        Font fontNegrito10 = FontFactory.getFont("Calibri", 10, Font.BOLD);
        Font font9 = FontFactory.getFont("Calibri", 9);
        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable table = new PdfPTable(1);
        table.setWidths(new int[] { 100 });
        table.setWidthPercentage(100);

        for (int i = 0; i < 9; i++) {
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(nome_paciente, fontNegrito10));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Méd. - " + nome_medico_sol, font9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("# " + handle_at + " - " + data_atendimento + " " + hora_atendimento, font9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Modalidade - " + modalidade, font9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        // criando o documento
        document.add(table);
        document.close();
    }

    private void abrirEtiquetaPDF() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c start C:\\ADMClin\\etiquetas\\Etiqueta" + handle_at + ".pdf");
    }
}
