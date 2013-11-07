/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * ATENÃO!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * SE DER ERRO VERIFICAR SE É POSSIVEL COPIAR UM ARQUIVO PARA O CMINHO C:/ SE PEDIR AUTORIZAÇÃO É ESSE O ERRO
 * TEMOS QUE CONFIGURAR O WINDOWNS PARA PERMITIR ACESSO A PASTA C:/
 */
package impressoes.modelo1.boletoDeRetirada;

import ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.DADOS_EMPRESA;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import conexao.Conexao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;

import menu.atendimentos.agenda.atendimentos.dao.ATENDIMENTOS_DAO;
import menu.atendimentos.agenda.atendimentos.dao.ATENDIMENTO_EXAMES_DAO;

/**
 *
 * @author BCN
 */
public class ImprimirBoletoDeRetiradaModelo1 {

    //variaveis que vamos utilizar para a construção do pdf
    Connection con = null;
    int handle_at;
    
    
    
    public ImprimirBoletoDeRetiradaModelo1(int handle_at) {
        this.handle_at = handle_at;
    }
    
    private void criandoAPastaParaSalvarOArquivo(){
        File dir = new File("C:\\ADMClin\\boletosDeRetirada");  
        boolean result = dir.mkdirs();
    }
    
    public boolean salvarEIMprimirBoletoDeRetirada(){
        boolean conseguiuSalvarEAbrirFicha = false;
        con = Conexao.fazConexao();
        try {
            criandoAPastaParaSalvarOArquivo();
            //pegando informações do atgendimento
            buscarInformacoesDaEmpresa();
           buscarInformaçõesDoAtendimento(); 
           buscandoOsExamesDoAtendimento();
           criarFichaPdf();
           abrirFichaPDF();
           conseguiuSalvarEAbrirFicha = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Etiqueta. Procure o Administrador." + " 000001","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (DocumentException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Etiqueta. Procure o Administrador." + " 000002","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException e) {
            //esse erro pode ocorrer caso o arquivo esteja aberto
            JOptionPane.showMessageDialog(null, "Não foi possível Criar Etiqueta. Procure o Administrador." + " 000003","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível Abrir Etiqueta. Procure o Administrador." + " 000001","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
          Conexao.fechaConexao(con); 
          return conseguiuSalvarEAbrirFicha;
        }
    }
    
    private String nomeEmpresa, telefoneEmpresa, enderecoEmpresa;
    private void buscarInformacoesDaEmpresa() throws SQLException{
        ResultSet resultSet = DADOS_EMPRESA.getConsultar(con);
        while(resultSet.next()){
            nomeEmpresa = resultSet.getString("nome");
            telefoneEmpresa = resultSet.getString("telefone");
            enderecoEmpresa = resultSet.getString("endereco");
        }
    }
    
    
    //variaveis da tabela atendimentos
    private String nome_paciente;
    private String data_entrega_exame, hora_exame_pronto;
    private String data_atendimento, hora_atendimento;
    private String EXAME_ENTREGUE_AO_PACIENTE, LAUDO_ENTREGUE_AO_PACIENTE;
    private void buscarInformaçõesDoAtendimento() throws SQLException{
        //buscando as informações do atendimento
        
        ResultSet resultSet = ATENDIMENTOS_DAO.getConsultarDadosDeUmAtendimento(con,handle_at);
        while(resultSet.next()){
            nome_paciente = resultSet.getString("nomePac");
            
            data_entrega_exame = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_exame_pronto"));
            data_atendimento = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_atendimento"));
            hora_atendimento = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento"));
            hora_exame_pronto = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_exame_pronto"));
            
            EXAME_ENTREGUE_AO_PACIENTE = resultSet.getString("EXAME_ENTREGUE_AO_PACIENTE");
            LAUDO_ENTREGUE_AO_PACIENTE = resultSet.getString("LAUDO_ENTREGUE_AO_PACIENTE");

        }
    }
    
    
    //variaveis dos exames
    private List<String> listaDeNomeDeExamesDoAtendimento = new ArrayList<String>();
    private List<String> listaDeLadoDeExamesDoAtendimento = new ArrayList<String>();
    private List<String> listaDeMaterialDeExamesDoAtendimento = new ArrayList<String>();
    private void buscandoOsExamesDoAtendimento() throws SQLException{
        ResultSet resultSetExames = ATENDIMENTO_EXAMES_DAO.getConsultarExamesDeUmAtendimento(con,handle_at);
        while(resultSetExames.next()){
            listaDeNomeDeExamesDoAtendimento.add(resultSetExames.getString("NomeExame"));
            listaDeLadoDeExamesDoAtendimento.add(resultSetExames.getString("lado"));
            listaDeMaterialDeExamesDoAtendimento.add(resultSetExames.getString("material"));
        }
    }
    
    private void criarFichaPdf() throws DocumentException, FileNotFoundException{
        Rectangle rect = new Rectangle(165.0f, 220.37f);
        Document document = new Document(rect, 0, 0, 0, 0); //colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream("C:\\ADMClin\\boletosDeRetirada\\boletoDeRetirada"+handle_at+".pdf"));
        document.open();
        
        Font fontNegrito7  =  FontFactory.getFont("Calibri", 7, Font.BOLD);
        Font fontNegrito9 = FontFactory.getFont("Calibri", 9, Font.BOLD);
        Font fontNegrito15 = FontFactory.getFont("Calibri", 15, Font.BOLD);
        Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
        Font font9 = FontFactory.getFont("Calibri", 9, Font.NORMAL);
        Font font7   =  FontFactory.getFont("Calibri", 7, Font.NORMAL);
        
        PdfPCell cell;
        
        
        //tabela de cabeçalho
        PdfPTable table = new PdfPTable(1);
        table.setWidths(new int[]{ 100 });
        table.setWidthPercentage(100);
        
        //colocando o RETIRADA DE EXAME
        cell = new PdfPCell(new Phrase("RETIRADA DE EXAME(S)",fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        
        if("S".equals(EXAME_ENTREGUE_AO_PACIENTE) && !"S".equals(LAUDO_ENTREGUE_AO_PACIENTE)){
            //colocando o RETIRADA DE EXAME
            cell = new PdfPCell(new Phrase("**EXAME JÁ ENTREGUE**",font7));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(cell);
        }
        
        if("S".equals(LAUDO_ENTREGUE_AO_PACIENTE) && !"S".equals(EXAME_ENTREGUE_AO_PACIENTE)){
            //colocando o RETIRADA DE EXAME
            cell = new PdfPCell(new Phrase("**LAUDO JÁ ENTREGUE**",font7));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            table.addCell(cell);
        }
        
        
        
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        //Preenchendo codigo do atendimento
        cell = new PdfPCell(new Phrase("Código Atendimento: "+handle_at,font9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        
        //Preenchendo data do atendimento
        cell = new PdfPCell(new Phrase("Data Atendimento: "+data_atendimento,font9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        //Preenchendo paciente
        cell = new PdfPCell(new Phrase("Paciente: "+nome_paciente,font9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        
        //preenchendo o cabeçalho dos exames
        cell = new PdfPCell(new Phrase("Exame(s)",fontNegrito7));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        
        ////preenchendo os exames
        for (int i = 0; i < listaDeNomeDeExamesDoAtendimento.size(); i++) {
        
            String lado = listaDeLadoDeExamesDoAtendimento.get(i);
            String exame = "";
            if ("".equals(lado)) {
                exame = listaDeNomeDeExamesDoAtendimento.get(i);
            }else{
                exame = listaDeNomeDeExamesDoAtendimento.get(i) + " - LADO: " + lado;
            }
            cell = new PdfPCell(new Phrase(exame,font7));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_JUSTIFIED);
            table.addCell(cell);
            
            
            
        }
        
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        //preenchendo a retirada
        cell = new PdfPCell(new Phrase("Data de Entrega do(s) exame(s)", fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(cell);
        //preenchendo a retirada
        cell = new PdfPCell(new Phrase(data_entrega_exame+" "+ hora_exame_pronto, fontNegrito9));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table.addCell(cell);
        
        
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        //linha em branco
        cell = new PdfPCell(new Phrase("-"));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        
        
        //que ficara imporesso para a proxima impressao
        
        //criando os dados da empresa
        cell = new PdfPCell(new Phrase(nomeEmpresa,fontNegrito15));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        //proxima linha do cabeçalho
        cell = new PdfPCell(new Phrase("Fone: " + telefoneEmpresa,fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        //proxima linha do cabeçalho
        cell = new PdfPCell(new Phrase(enderecoEmpresa,font7));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        //linha em branco
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        table.addCell(cell);
        
        //criando o documento
        document.add(table);     
        document.close();
        
    }
    
    /*
     * Metodo que abri um arquivo pdf (que acamos de criar)
     */
     private void abrirFichaPDF() throws IOException{
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("cmd /c start C:\\ADMClin\\boletosDeRetirada\\boletoDeRetirada"+handle_at+".pdf");
    }
            
    
    
     public static int calculaIdade(String dataNasc, String pattern){
        DateFormat sdf = new SimpleDateFormat(pattern);
        Date dataNascInput = null;
        try {
            dataNascInput= sdf.parse(dataNasc);
        } catch (Exception e) {}
        
        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(dataNascInput);
        
        // Cria um objeto calendar com a data atual
        Calendar today = Calendar.getInstance();
        
       // Obtém a idade baseado no ano
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        
        dateOfBirth.add(Calendar.YEAR, age);
        
        if (today.before(dateOfBirth)) {
            age--;
        }
        return age;
        
    }
    
}
