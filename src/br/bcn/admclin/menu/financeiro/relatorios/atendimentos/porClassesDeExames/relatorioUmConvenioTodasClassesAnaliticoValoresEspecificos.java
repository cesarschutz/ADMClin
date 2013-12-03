/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.menu.financeiro.relatorios.atendimentos.porClassesDeExames;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

/**
 *
 * @author BCN
 */
public class relatorioUmConvenioTodasClassesAnaliticoValoresEspecificos {
    
    private Date dataInicial = null, dataFinal  = null;
    private String dataInicialString, dataFinalString;
    private Connection con = null;
    private int handle_convenio;

    public relatorioUmConvenioTodasClassesAnaliticoValoresEspecificos(Date dataInicial, Date dataFinal, int handle_convenio) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.handle_convenio = handle_convenio;
        
        
        //passando as datas para string
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dataInicialString = format.format( dataInicial.getTime() );
        dataFinalString = format.format( dataFinal.getTime() );
    }
    
    String caminho;
    private void criandoAPastaParaSalvarOArquivo(){
        if (OSvalidator.isWindows()) {
            caminho = USUARIOS.pasta_raiz + "\\relatorioDeAtendimentos\\";
        }else{
            caminho = USUARIOS.pasta_raiz + "/relatorioDeAtendimentos/";
        }
        File dir = new File(caminho);  
        boolean result = dir.mkdirs();
    }
    
    public boolean gerarRelatorio(){
        try {
            con = Conexao.fazConexao();
            criandoAPastaParaSalvarOArquivo();
            consultarAtendimentos();
            criandoFatura();
            abrirFichaPDF();
            return true;
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao elaborar Relatório. Procure o Administrador.");
            return false;
        } finally {
            Conexao.fechaConexao(con);
        }
    }
    
    //metodo que busca os atendimentos de acordo com a classe
    public List<relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosMODEL> listaDeAtendimentos = new ArrayList<relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosMODEL>();
    private void consultarAtendimentos() throws SQLException{
        listaDeAtendimentos.removeAll(listaDeAtendimentos);
        ResultSet resultSet = relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosDAO.getConsultarAtendimentos(con, dataInicial, dataFinal, handle_convenio);
        while(resultSet.next()){
            relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosMODEL atendimento = new relatorioUmConvenioTodasClassesAnaliticoValoresEspecificosMODEL();
            atendimento.setData(resultSet.getDate("data_atendimento"));
            atendimento.setHandle_at(resultSet.getInt("handle_at"));
            atendimento.setPaciente(resultSet.getString("nomePaciente"));
            atendimento.setConvenio(resultSet.getString("nomeConvenio"));
            atendimento.setExame(resultSet.getString("nomeExame"));
            atendimento.setCh_convenio(resultSet.getString("ch_convenio"));
            atendimento.setFilme_convenio(resultSet.getString("filme_convenio"));
            atendimento.setCh1_exame(resultSet.getString("ch1_exame"));
            atendimento.setCh2_exame(resultSet.getString("ch2_exame"));
            atendimento.setFilme_exame(resultSet.getString("filme_exame"));
            atendimento.setLista_material(resultSet.getString("lista_materiais"));
            atendimento.setClasseDeExame(resultSet.getString("nomeClasseDeExame"));
            atendimento.setRedutor(resultSet.getString("redutor"));
            atendimento.setDesconto_paciente(resultSet.getString("valor_desconto"));
            atendimento.setValor_correto_exame(resultSet.getString("valor_correto_exame"));
            atendimento.setMaterial(resultSet.getString("material"));
            if ("CC".equals(resultSet.getString("material"))) {
                atendimento.setExame(resultSet.getString("nomeExame") + " - C/ Contraste");
            } else if("CM".equals(resultSet.getString("material"))){
                atendimento.setExame(resultSet.getString("nomeExame") + " - C/ Material");
            }
            listaDeAtendimentos.add(atendimento);
        }
        
    }
    
    public void criandoFatura() throws FileNotFoundException, DocumentException{
        Rectangle rect = new Rectangle(PageSize.A4.rotate());
        Document document = new Document(rect, 20, 20, 20, 20); //colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream(caminho + "relatórioUmConvênioTodasClassesDeExamesAnalíticoValoresEspecíficos"+handle_convenio+".pdf"));
        document.open();
        
        //fontes utilizadas
        Font fontNegrito15 = FontFactory.getFont("Calibri", 15, Font.BOLD);
        Font fontNegrito11 = FontFactory.getFont("Calibri", 11, Font.BOLD);
        Font font9 = FontFactory.getFont("Calibri", 9, Font.NORMAL);
        Font fontNegrito8 = FontFactory.getFont("Calibri", 8, Font.BOLD);
        
        PdfPCell cell;
        
        
        //tabela de cabeçalho
        PdfPTable tablePrincipal = new PdfPTable(1);
        tablePrincipal.setWidths(new int[]{ 100 });
        tablePrincipal.setWidthPercentage(100);
        
        //colocando o 
        cell = new PdfPCell(new Phrase("Relatório de Atendimentos Analítico com Valores Específicos por Classes de Exames de um Convênio", fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        //colocando o convenio
        cell = new PdfPCell(new Phrase("Convênio: " + listaDeAtendimentos.get(0).getConvenio(), fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        //colocando a data atual
        Calendar hoje1 = Calendar.getInstance();
        SimpleDateFormat format3 = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje2 = format3.format( hoje1.getTime() );
        
        cell = new PdfPCell(new Phrase("Data: " + dataDeHoje2, fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        //colocando o periodo
        cell = new PdfPCell(new Phrase("Período: " + dataInicialString + " à " + dataFinalString, fontNegrito11));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        //colocando linha em branco
        cell = new PdfPCell(new Phrase("", fontNegrito11));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tablePrincipal.addCell(cell);
        
        //adicionando tabela ao documento
        document.add(tablePrincipal);
        
        
        
        
        
        
        
        
        
        
        
        
        
        //adicionando tabela com o cabeçalho (informações das colunas)
        //tabela de cabeçalho
        PdfPTable tabelaCabecalho = new PdfPTable(10);
        tabelaCabecalho.setWidths(new int[]{ 7,6,25,19,6,8,10,6,5,8 });
        tabelaCabecalho.setWidthPercentage(100);
        
        cell = new PdfPCell(new Phrase("DATA", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("CÓDIGO", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("PACIENTE", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("EXAME", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("VALOR CH", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("VALOR FILME", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("VALOR MATERIAL", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("REDUTOR", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("DESC.", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("TOTAL", fontNegrito8));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabelaCabecalho.addCell(cell);
        
        cell = new PdfPCell(new Phrase("", fontNegrito8));
        cell.setBorder(Rectangle.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(10);
        tabelaCabecalho.addCell(cell);
        
        //adicionando tabela ao documento
        document.add(tabelaCabecalho);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        String classeDeExameAtual = "";
        Double chTotalDaClasse=0.0, filmeTotalDaClasse=0.0, materialTotalDaClasse=0.0, descontoTotalDaClasse=0.0, valorTotalDaClasse=0.0;
        Double chTotalDoRelatorio=0.0, filmeTotalDoRelatorio=0.0, materialTotalDoRelatorio=0.0, descontoTotalDoRelatorio=0.0, valorTotalDoRelatorio=0.0;
        int qtdDeExamesDaClasse=0, qtdDeExamesNoRelatorio = 0;
        for (int i = 0; i < listaDeAtendimentos.size(); i++) {
            //aqui se a classe de exame mudar colocamos o nome da classe           
            if (!classeDeExameAtual.equals(listaDeAtendimentos.get(i).getClasseDeExame())) {
                //se a qtd total de exames for maior que 1
                //isso para que a primeira classe nao va os resultados totais
                //nao pode te os totais antes de aprensentar a primeira classe
                if (qtdDeExamesDaClasse > 0) {
                   //colocando os valores totais da classe
                    PdfPTable tabelaTotaisDaClasse = new PdfPTable(10);
                    tabelaTotaisDaClasse.setWidths(new int[]{ 7,6,25,19,6,8,10,6,5,8 });
                    tabelaTotaisDaClasse.setWidthPercentage(100);

                    //classe e total de exames
                    cell = new PdfPCell(new Phrase(classeDeExameAtual + " - " + qtdDeExamesDaClasse + " Exames", fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(4);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total ch
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(chTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total filme
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(filmeTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total material
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(materialTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //coluna do redutor vazia
                    cell = new PdfPCell(new Phrase("", fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total desconto
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(descontoTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total total total
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);
                    
                    //adicionando linhas em branco
                    //classe e total de exames
                    for (int j = 0; j < 5; j++) {
                        cell = new PdfPCell(new Phrase("", fontNegrito8));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                        cell.setColspan(10);
                        tabelaTotaisDaClasse.addCell(cell);
                    }
                    

                   document.add(tabelaTotaisDaClasse); 
                }
                
                
                //zerando os totais da classe
                chTotalDaClasse = 0.0;
                filmeTotalDaClasse = 0.0;
                materialTotalDaClasse = 0.0;
                descontoTotalDaClasse = 0.0;
                valorTotalDaClasse = 0.0;
                qtdDeExamesDaClasse = 0;
                
                //colocando a nova classe que sera apartir de agora
                classeDeExameAtual = listaDeAtendimentos.get(i).getClasseDeExame();
                
                PdfPTable tabelaClasseDeExame = new PdfPTable(1);
                tabelaClasseDeExame.setWidths(new int[]{ 100 });
                tabelaClasseDeExame.setWidthPercentage(100);
                
                cell = new PdfPCell(new Phrase("Classe de Exame: " + listaDeAtendimentos.get(i).getClasseDeExame(), fontNegrito8));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                tabelaClasseDeExame.addCell(cell);
                
                document.add(tabelaClasseDeExame);
            }
            
            
            
            
            
            
            
            
            
            
            
            //adicionar os atendimentos
            PdfPTable tabelaDosExames = new PdfPTable(10);
            tabelaDosExames.setWidths(new int[]{ 7,6,25,19,6,8,10,6,5,8 });
            tabelaDosExames.setWidthPercentage(100);
            
            //colocando a data do atendimento
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            String dataCerta = fmt.format(listaDeAtendimentos.get(i).getData());
            
            cell = new PdfPCell(new Phrase(dataCerta, font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaDosExames.addCell(cell);

            //colocando o handle_at do atendimento (codigo)
            cell = new PdfPCell(new Phrase(String.valueOf(listaDeAtendimentos.get(i).getHandle_at()), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaDosExames.addCell(cell);

            //colocando o nome do paciente
            cell = new PdfPCell(new Phrase(listaDeAtendimentos.get(i).getPaciente(), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaDosExames.addCell(cell);


            //colocando o exame
            cell = new PdfPCell(new Phrase(listaDeAtendimentos.get(i).getExame(), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            tabelaDosExames.addCell(cell);

            //colocar valor ch
            
            Double total_ch = calcularValorDeCh(listaDeAtendimentos.get(i).getCh_convenio(), listaDeAtendimentos.get(i).getCh1_exame(), listaDeAtendimentos.get(i).getCh2_exame());
            chTotalDaClasse = new BigDecimal(chTotalDaClasse + total_ch).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            chTotalDoRelatorio = new BigDecimal(chTotalDoRelatorio + total_ch).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

            
            cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(total_ch)).replace(".", ","), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaDosExames.addCell(cell);

            //colocando valor filme
            
            Double total_filme = calcularValorDeFilme(listaDeAtendimentos.get(i).getFilme_convenio(), listaDeAtendimentos.get(i).getFilme_exame());
            filmeTotalDaClasse = new BigDecimal(filmeTotalDaClasse + total_filme).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            filmeTotalDoRelatorio = new BigDecimal(filmeTotalDoRelatorio + total_filme).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            
            cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(total_filme)).replace(".", ","), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaDosExames.addCell(cell);

            //colocando valor materiais
            
            Double total_materiais = calcularValorDosMateriais(listaDeAtendimentos.get(i).getLista_material());
            materialTotalDaClasse = new BigDecimal(materialTotalDaClasse + total_materiais).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            materialTotalDoRelatorio = new BigDecimal(materialTotalDoRelatorio + total_materiais).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            
            cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(total_materiais)).replace(".", ","), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaDosExames.addCell(cell);

            //colocando o redutor
            //como pode dar erro se estiver vazio, trato isso, se tiver vazio o redutor é 0.00
            String redutor;
            try {
                redutor = MetodosUteis.colocarZeroEmCampoReais(listaDeAtendimentos.get(i).getRedutor());
            } catch (Exception e) {
                redutor = "0.00";
            }
            cell = new PdfPCell(new Phrase(redutor, font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaDosExames.addCell(cell);
            
            //colocando o desconto
            Double desconto = 0.0;
            try {
                desconto = Double.valueOf(listaDeAtendimentos.get(i).getDesconto_paciente());
            } catch (Exception e) {
                desconto = 0.00;
            }
            
            descontoTotalDaClasse = new BigDecimal(descontoTotalDaClasse + desconto).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            descontoTotalDoRelatorio = new BigDecimal(descontoTotalDoRelatorio + desconto).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            
            cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(desconto)).replace(".", ","), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaDosExames.addCell(cell);

            //colocando o total
            
            Double valorTotalDoExame = new BigDecimal(total_ch + total_filme + total_materiais).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            Double desconto_paciente = 0.0;
            try {
                desconto_paciente = Double.valueOf(listaDeAtendimentos.get(i).getDesconto_paciente());
            } catch (Exception e) {
                desconto_paciente = 0.0;
            }
            Double valorTotalDoExameComRedutorEDescontoPaciente = new BigDecimal((valorTotalDoExame - (valorTotalDoExame * (Double.valueOf(redutor)/100))) - desconto_paciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            
            //se o total for menor ou igual a 0 entao pegamos o valor_correto_exame 
            //pq foi alterado o valor pelo usuario!!!
            if (valorTotalDoExameComRedutorEDescontoPaciente <= 0) {
                valorTotalDoExameComRedutorEDescontoPaciente = Double.valueOf(listaDeAtendimentos.get(i).getValor_correto_exame());
            }
            
            valorTotalDaClasse = new BigDecimal(valorTotalDaClasse + valorTotalDoExameComRedutorEDescontoPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            valorTotalDoRelatorio = new BigDecimal(valorTotalDoRelatorio + valorTotalDoExameComRedutorEDescontoPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            
            cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorTotalDoExameComRedutorEDescontoPaciente)).replace(".", ","), font9));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tabelaDosExames.addCell(cell);

            //colocando a qtd
            qtdDeExamesDaClasse += 1;
            qtdDeExamesNoRelatorio += 1;
            
            //adicionando tabela ao documento
            document.add(tabelaDosExames);
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        //colocando os valores totais da ultima classe
                    PdfPTable tabelaTotaisDaClasse = new PdfPTable(11);
                    tabelaTotaisDaClasse.setWidths(new int[]{ 7,6,20,11,13,6,8,10,6,5,8 });
                    tabelaTotaisDaClasse.setWidthPercentage(100);

                    //classe e total de exames
                    cell = new PdfPCell(new Phrase(classeDeExameAtual + " - " + qtdDeExamesDaClasse + " Exames", fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(5);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total ch
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(chTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total filme
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(filmeTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total material
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(materialTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //coluna do redutor vazia
                    cell = new PdfPCell(new Phrase("", fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total desconto
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(descontoTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);

                    //total total total
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorTotalDaClasse)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaTotaisDaClasse.addCell(cell);
                    
                    document.add(tabelaTotaisDaClasse);
        
                    
                    
                    
                    
                    
                    
        
                    
                    
        //colocando o valor total de todas as classe
                    //colocando os valores totais da classe
                    PdfPTable tabelaValoresTotaisDeTodasAsClasses = new PdfPTable(11);
                    tabelaValoresTotaisDeTodasAsClasses.setWidths(new int[]{ 7,6,20,11,13,6,8,10,6,5,8 });
                    tabelaValoresTotaisDeTodasAsClasses.setWidthPercentage(100);
                    
                    //colocando linhas em branco
                    for (int i = 0; i < 5; i++) {
                        cell = new PdfPCell(new Phrase("", fontNegrito8));
                        cell.setBorder(Rectangle.NO_BORDER);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                        cell.setColspan(11);
                        tabelaValoresTotaisDeTodasAsClasses.addCell(cell);
                    }

                    //classe e total de exames
                    cell = new PdfPCell(new Phrase("Total de Exame: " + qtdDeExamesNoRelatorio, fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(5);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);

                    //total ch
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(chTotalDoRelatorio)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);

                    //total filme
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(filmeTotalDoRelatorio)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);

                    //total material
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(materialTotalDoRelatorio)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);

                    //coluna do redutor vazia
                    cell = new PdfPCell(new Phrase("", fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);

                    //total desconto
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(descontoTotalDoRelatorio)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);

                    //total total total
                    cell = new PdfPCell(new Phrase(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorTotalDoRelatorio)).replace(".", ","), fontNegrito8));
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    tabelaValoresTotaisDeTodasAsClasses.addCell(cell);
                    
                    document.add(tabelaValoresTotaisDeTodasAsClasses);
                    
                    
                    
        
        //fechando o documento
        document.close();
        
        
        
    }
    
    
    //calculando o CH
    private Double calcularValorDeCh(String ch_convenioString, String ch1_exameString, String ch2_exameString){
        Double ch_convenio, ch1_exame, ch2_exame;
        //pegando o ch do convenio, se der erro é pq nao tem nada, deixamos 1 para multiplicar pelo ch do convenio
            try {
                ch_convenio = Double.valueOf(ch_convenioString);
                if (ch_convenio == 0) {
                    ch_convenio = 1.0;
                }
            } catch (Exception e) {
                ch_convenio = 1.0;
            }
            
            //se tive vazio (erro ao converter) colocamos valor 0, pois estga vazio, nao tem valor
            //isso acontecera no caso de o valor do exame ter sido alterado pela atendente (ae os ch ficam vazios, dara o erro e sei que é 0)
            try {
                ch1_exame = Double.valueOf(ch1_exameString);
            } catch (Exception e) {
                ch1_exame = 0.0;
            }
            
            try {
                ch2_exame = Double.valueOf(ch2_exameString);
            } catch (Exception e) {
                ch2_exame = 0.0;
            }
            
            //retorna o valor total de CH calculado
            return new BigDecimal((ch1_exame + ch2_exame) * ch_convenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
    
    //metodo para calcular o valor do filme
    private Double calcularValorDeFilme(String filme_convenioString, String filme_exameString){
        Double filme_convenio, filme_exame;
        try {
            filme_convenio = Double.valueOf(filme_convenioString);
        } catch (Exception e) {
            filme_convenio = 1.0;
        }
        
        try {
            filme_exame = Double.valueOf(filme_exameString);
        } catch (Exception e) {
            filme_exame = 0.0;
        }
        
        return new BigDecimal(filme_convenio * filme_exame).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
    
    //metodo para somar os valores do exame
    //na lista estara assim codigoMaterial&handleMaterial&nomeMaterial&valorUnitario&quantidade&valorTotal#
    private Double calcularValorDosMateriais(String listaDeMateriais){
        String materiais[];
        Double valorTotalDeTodosOsMateriais = 0.0;
        try {
            materiais = listaDeMateriais.split("#");
            for (int i = 0; i < materiais.length; i++) {
                String[] dadosDoMaterial = materiais[i].split("&");
                Double valorTotalDoMaterial = Double.valueOf(dadosDoMaterial[5]);
                valorTotalDeTodosOsMateriais = valorTotalDeTodosOsMateriais + valorTotalDoMaterial;
            }
            return valorTotalDeTodosOsMateriais;
        } catch (Exception e) {
            return 0.0;
        }
        
    }
    
    /*
     * Metodo que abri um arquivo pdf (que acamos de criar)
     */
     private void abrirFichaPDF() throws IOException{
            Runtime runtime = Runtime.getRuntime();
            if (OSvalidator.isWindows()) {
                runtime.exec("cmd /c \"" + caminho+ "relatórioUmConvênioTodasClassesDeExamesAnalíticoValoresEspecíficos"+handle_convenio+".pdf");
            } else if(OSvalidator.isMac()){
                runtime.exec("open " + caminho + "relatórioUmConvênioTodasClassesDeExamesAnalíticoValoresEspecíficos"+handle_convenio+".pdf");
            }else{
                runtime.exec("gnome-open " + caminho + "relatórioUmConvênioTodasClassesDeExamesAnalíticoValoresEspecíficos"+handle_convenio+".pdf");
            }
    }
}
