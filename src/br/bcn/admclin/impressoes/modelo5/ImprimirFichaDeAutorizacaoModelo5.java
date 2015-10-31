/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * ATENÃO!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * SE DER ERRO VERIFICAR SE É POSSIVEL COPIAR UM ARQUIVO PARA O CMINHO C:/ SE PEDIR AUTORIZAÇÃO É ESSE O ERRO
 * TEMOS QUE CONFIGURAR O WINDOWNS PARA PERMITIR ACESSO A PASTA C:/
 */
package br.bcn.admclin.impressoes.modelo5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.DADOS_EMPRESA;
import br.bcn.admclin.dao.dbris.USUARIOS;

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

/**
 * 
 * @author Cesar Schutz
 */
public class ImprimirFichaDeAutorizacaoModelo5 {

    // variaveis que vamos utilizar para a construção do pdf
    Connection con = null;
    int handle_at;

    public ImprimirFichaDeAutorizacaoModelo5(int handle_at) {
        this.handle_at = handle_at;
    }

    private void criandoAPastaParaSalvarOArquivo() {
        File dir = new File("C:\\ADMClin\\fichaDeAtendimento");
        dir.mkdirs();
    }

    @SuppressWarnings("finally")
    public boolean salvarEImprimirFicha() {
        boolean conseguiuAbrirAFicha = false;
        con = Conexao.fazConexao();
        try {
            criandoAPastaParaSalvarOArquivo();
            // pegando informações do atgendimento
            buscarInformacoesDaEmpresa();
            buscarInformaçõesDoAtendimento();
            buscandoOsExamesDoAtendimento();
            criarFichaPdf();
            abrirFichaPDF();
            conseguiuAbrirAFicha = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Ficha de Atendimento. Procure o Administrador."
                + " 000001" + e, "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (DocumentException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Ficha de Atendimento. Procure o Administrador."
                + " 000002", "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            // esse erro pode ocorrer caso o arquivo esteja aberto
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Ficha de Atendimento. Procure o Administrador."
                + " 000003", "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Abrir Ficha de Atendimento. Procure o Administrador."
                + " 000001", "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
            return conseguiuAbrirAFicha;
        }
    }

    private String nomeEmpresa;

    private void buscarInformacoesDaEmpresa() throws SQLException {
    	int id_dados_empresa = DADOS_EMPRESA.getConsultarIdDadosEmpresaDeUmAtendimento(con, handle_at);
        ResultSet resultSet = DADOS_EMPRESA.getConsultar(con, id_dados_empresa);
        while (resultSet.next()) {
            nomeEmpresa = resultSet.getString("nome");
        }
    }

    // variaveis da tabela atendimentos
    private String nome_paciente, nascimento_paciente, sexo_paciente, telefone_paciente, peso_paciente,
                    altura_paciente;
    private String nome_convenio, matricula_convenio, complemento, nome_medico_sol, crm_medico_sol;
    private String data_atendimento, hora_atendimento, observacao, cidade;

    private void buscarInformaçõesDoAtendimento() throws SQLException {
        // buscando as informações do atendimento

        ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_at);
        while (resultSet.next()) {
            resultSet.getString("handle_paciente");
            nome_paciente = resultSet.getString("nomePac");
            peso_paciente = resultSet.getString("peso");
            altura_paciente = resultSet.getString("altura");
            nascimento_paciente = resultSet.getString("nascimento_paciente");
            sexo_paciente = resultSet.getString("sexo_paciente");
            telefone_paciente = resultSet.getString("telefone_paciente");
            cidade = resultSet.getString("cidade");

            nome_convenio = resultSet.getString("nomeConv");
            matricula_convenio = resultSet.getString("matricula_convenio");
            complemento = resultSet.getString("complemento");
            nome_medico_sol = resultSet.getString("nomeMed");
            crm_medico_sol = resultSet.getString("crmMedico");
            MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_exame_pronto"));
            data_atendimento = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_atendimento"));
            hora_atendimento = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento"));
            observacao = resultSet.getString("observacao");
        }
    }

    // variaveis dos exames
    private List<String> listaDeNomeDeExamesDoAtendimento = new ArrayList<String>();
    private List<String> listaDeLadoDeExamesDoAtendimento = new ArrayList<String>();
    private List<String> listaDeMaterialDeExamesDoAtendimento = new ArrayList<String>();

    private void buscandoOsExamesDoAtendimento() throws SQLException {
        ResultSet resultSetExames = ATENDIMENTO_EXAMES.getConsultarExamesDeUmAtendimento(con, handle_at);
        while (resultSetExames.next()) {
            listaDeNomeDeExamesDoAtendimento.add(resultSetExames.getString("NomeExame"));
            listaDeLadoDeExamesDoAtendimento.add(resultSetExames.getString("lado"));
            listaDeMaterialDeExamesDoAtendimento.add(resultSetExames.getString("material"));
        }
    }

    private void criarFichaPdf() throws DocumentException, FileNotFoundException {
        Document document = new Document(PageSize.A4); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream("C:\\ADMClin\\fichaDeAtendimento\\ficha" + handle_at
            + ".pdf"));
        document.open();

        Font fontCabecalho = FontFactory.getFont("Calibri", 12, Font.BOLD);
        Font fontNormal = FontFactory.getFont("Calibri", 10, Font.NORMAL);
        Font fontCabecalhoExame = FontFactory.getFont("Calibri", 11, Font.BOLD);
        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable tableCabecalho = new PdfPTable(2);
        tableCabecalho.setWidths(new int[] { 50, 50 });
        tableCabecalho.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Ficha de Atendimento", fontCabecalho));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableCabecalho.addCell(cell);
        cell = new PdfPCell(new Phrase(nomeEmpresa, fontCabecalho));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tableCabecalho.addCell(cell);

        // TABELA COM 4 COLUNAS
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[] { 25, 25, 25, 25 });
        table.setWidthPercentage(100);

        // preenchendo dados do atendimento

        cell = new PdfPCell(new Phrase("Data: " + data_atendimento + " " + hora_atendimento, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Código: " + handle_at, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Atendente: " + USUARIOS.usrId + " - " + USUARIOS.nomeUsuario, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);

        // linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        table.addCell(cell);
        // linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        table.addCell(cell);

        // preenchendo dados do paciente

        cell = new PdfPCell(new Phrase("Paciente: " + nome_paciente + "\nCidade: " + cidade, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Nascimento: " + nascimento_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);

        String idade;
        if (!"  /  /    ".equals(nascimento_paciente)) {
            idade = MetodosUteis.calculaIdade(nascimento_paciente, "dd/MM/yyyy");
        } else {
            idade = "";
        }

        cell = new PdfPCell(new Phrase("Idade: " + idade, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);

        // proxima linha do paciente

        cell = new PdfPCell(new Phrase("Telefone: " + telefone_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Sexo: " + sexo_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Peso: " + peso_paciente + " Kg", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Altura: " + altura_paciente + " Cm", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);

        // linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        table.addCell(cell);

        // preenchendo dados do medico

        cell = new PdfPCell(new Phrase("Médico: " + nome_medico_sol, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("CRM: " + crm_medico_sol, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);

        // linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        table.addCell(cell);

        // preenchendo dados do convenio

        cell = new PdfPCell(new Phrase("Convênio: " + nome_convenio, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Matrícula: " + matricula_convenio, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);

        // preenchendo o complemento

        cell = new PdfPCell(new Phrase("Complemento: " + complemento, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);

        // linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        table.addCell(cell);

        // preenchendo exames

        // TABELA para os exames
        PdfPTable tableExames = new PdfPTable(5);
        tableExames.setWidths(new float[] { 20, 35, 12, 13, 20 });
        tableExames.setWidthPercentage(100);

        // primeiro o cabeçalho
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        tableExames.addCell(cell);

        cell = new PdfPCell(new Phrase("Nome do Exame", fontCabecalhoExame));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
        tableExames.addCell(cell);

        cell = new PdfPCell(new Phrase("Lado", fontCabecalhoExame));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        cell = new PdfPCell(new Phrase("Material", fontCabecalhoExame));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        // preenchendo os exames na tabela de exames depois vamos colocar essa tabela como linha da outra para ficar
        // centralizada

        for (int i = 0; i < listaDeNomeDeExamesDoAtendimento.size(); i++) {
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            tableExames.addCell(cell);

            cell = new PdfPCell(new Phrase(listaDeNomeDeExamesDoAtendimento.get(i), fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
            tableExames.addCell(cell);

            cell = new PdfPCell(new Phrase(listaDeLadoDeExamesDoAtendimento.get(i), fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tableExames.addCell(cell);

            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tableExames.addCell(cell);
        }

        // linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        tableExames.addCell(cell);

        // preenchendo observacao ( vai na tabela exames com collspan pq ocupa somente uma linha!!!! )

        cell = new PdfPCell(new Phrase("Observação:", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        // proxima linha da observação
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        cell = new PdfPCell(new Phrase(observacao, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(4);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        // se observação estiver vazia deixa um espaço maior em branco
        if ("".equals(observacao)) {
            // 10 linhas em branco
            for (int i = 0; i < 10; i++) {
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(5);
                tableExames.addCell(cell);
            }
        } else {
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(5);
            tableExames.addCell(cell);
        }

        // preenchendo História Clínica ( vai na tabela exames com collspan pq ocupa somente uma linha!!!! )

        cell = new PdfPCell(new Phrase("História Clínica:", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        // 20 linhas em branco
        for (int i = 0; i < 30; i++) {
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(5);
            tableExames.addCell(cell);
        }

        // preenchendo Relatório ( vai na tabela exames com collspan pq ocupa somente uma linha!!!! )

        cell = new PdfPCell(new Phrase("Relatório:", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableExames.addCell(cell);

        // 20 linhas em branco
        for (int i = 0; i < 30; i++) {
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(5);
            tableExames.addCell(cell);
        }

        // preenchendo Autorização

        cell =
            new PdfPCell(new Phrase("Autorização para que o médico solicitante tenha acesso aos laudos\n"
                + "e as imagens dos exames acima relacionados via internet.", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tableExames.addCell(cell);

        cell = new PdfPCell(new Phrase("(  ) Autorizo     (  ) Não Autorizo", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tableExames.addCell(cell);

        // 8 linhas em branco
        for (int i = 0; i < 8; i++) {
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(5);
            tableExames.addCell(cell);
        }

        cell = new PdfPCell(new Phrase("__________________________________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tableExames.addCell(cell);

        cell = new PdfPCell(new Phrase("Assinatura", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tableExames.addCell(cell);

        // criando o documento
        document.add(tableCabecalho);
        document.add(table);
        document.add(tableExames);
        document.close();

    }

    /*
     * Metodo que abri um arquivo pdf (que acamos de criar)
     */
    private void abrirFichaPDF() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("cmd /c start C:\\ADMClin\\fichaDeAtendimento\\ficha" + handle_at + ".pdf");
    }

}
