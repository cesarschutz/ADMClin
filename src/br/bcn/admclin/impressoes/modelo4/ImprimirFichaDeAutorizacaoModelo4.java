/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * ATENÃO!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * SE DER ERRO VERIFICAR SE É POSSIVEL COPIAR UM ARQUIVO PARA O CMINHO C:/ SE PEDIR AUTORIZAÇÃO É ESSE O ERRO
 * TEMOS QUE CONFIGURAR O WINDOWNS PARA PERMITIR ACESSO A PASTA C:/
 */
package br.bcn.admclin.impressoes.modelo4;

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
public class ImprimirFichaDeAutorizacaoModelo4 {

    // variaveis que vamos utilizar para a construção do pdf
    Connection con = null;
    int handle_at;

    public ImprimirFichaDeAutorizacaoModelo4(int handle_at) {
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

    // variaveis da tabela atendimentos
    private String nome_paciente, nascimento_paciente, sexo_paciente, telefone_paciente, peso_paciente,
                    altura_paciente, celular_paciente, handle_paciente, cpf_paciente, data_exame_pronto;
    private String nome_convenio, nome_medico_sol, valor_pago_paciente;
    private String data_atendimento, cidade, endereco_paciente;

    private void buscarInformaçõesDoAtendimento() throws SQLException {
        // buscando as informações do atendimento

        ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_at);
        while (resultSet.next()) {
            resultSet.getString("handle_paciente");
            handle_paciente = String.valueOf(resultSet.getInt("handle_paciente"));
            cpf_paciente = resultSet.getString("cpf_paciente");
            nome_paciente = resultSet.getString("nomePac");
            peso_paciente = resultSet.getString("peso");
            altura_paciente = resultSet.getString("altura");
            nascimento_paciente = resultSet.getString("nascimento_paciente");
            sexo_paciente = resultSet.getString("sexo_paciente");
            telefone_paciente = resultSet.getString("telefone_paciente");
            celular_paciente = resultSet.getString("celular_paciente");
            cidade = resultSet.getString("cidade");
            endereco_paciente = resultSet.getString("endereco_paciente");

            nome_convenio = resultSet.getString("nomeConv");
            nome_medico_sol = resultSet.getString("nomeMed");
            MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_exame_pronto"));
            data_atendimento = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_atendimento"));
            data_exame_pronto = MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("data_exame_pronto")) + " " + MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_exame_pronto"));
            
        }
    }

    // variaveis dos exames
    private List<String> listaDeNomeDeExamesDoAtendimento = new ArrayList<String>();
    private List<String> listaDeLadoDeExamesDoAtendimento = new ArrayList<String>();
    private List<String> listaDeMaterialDeExamesDoAtendimento = new ArrayList<String>();

    private void buscandoOsExamesDoAtendimento() throws SQLException {
        ResultSet resultSetExames = ATENDIMENTO_EXAMES.getConsultarExamesDeUmAtendimento(con, handle_at);
        double valor_pago_paciente = 0;
        while (resultSetExames.next()) {
            listaDeNomeDeExamesDoAtendimento.add(resultSetExames.getString("NomeExame"));
            listaDeLadoDeExamesDoAtendimento.add(resultSetExames.getString("lado"));
            listaDeMaterialDeExamesDoAtendimento.add(resultSetExames.getString("material"));
            valor_pago_paciente += resultSetExames.getDouble("valor_paciente");
        }
        this.valor_pago_paciente = String.valueOf(valor_pago_paciente);
    }

    private void criarFichaPdf() throws DocumentException, FileNotFoundException {
        Document document = new Document(PageSize.A4); // colocar as margens
        PdfWriter.getInstance(document, new FileOutputStream("C:\\ADMClin\\fichaDeAtendimento\\ficha" + handle_at
            + ".pdf"));
        document.open();
        
        
        
        

        
        Font fontUnidade = FontFactory.getFont("Calibri", 8, Font.NORMAL);
        Font fontNomeDaEmpresa = FontFactory.getFont("Calibri", 17, Font.BOLD);
        Font fontFicha = FontFactory.getFont("Calibri", 12, Font.BOLD);
        Font fontNormal = FontFactory.getFont("Calibri", 9, Font.NORMAL);
        Font fontNormalNegrito = FontFactory.getFont("Calibri", 9, Font.BOLD);
        //Font fontNormal = FontFactory.getFont("Calibri", 10, Font.NORMAL);
        //Font fontCabecalhoExame = FontFactory.getFont("Calibri", 11, Font.BOLD);
        PdfPCell cell;

        // tabela de cabeçalho
        PdfPTable tableCabecalho = new PdfPTable(1);
        tableCabecalho.setWidths(new int[] { 100 });
        tableCabecalho.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("CIDI", fontNomeDaEmpresa));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableCabecalho.addCell(cell);
        cell = new PdfPCell(new Phrase("unidade 2", fontUnidade));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tableCabecalho.addCell(cell);
        cell = new PdfPCell(new Phrase("Ficha de Atendimento – Anamnese de RM", fontFicha));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        tableCabecalho.addCell(cell);
        
        //tabela com informações do atendimento
        PdfPTable tabelaDadosDoPaciente = new PdfPTable(3);
        tabelaDadosDoPaciente.setWidths(new int[] { 33,33,34 });
        tabelaDadosDoPaciente.setWidthPercentage(100);
        
        //cod paciente
        cell = new PdfPCell(new Phrase("Cód. do Paciente: " + handle_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //data atual
        cell = new PdfPCell(new Phrase("Data: " + data_atendimento, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //convênio
        cell = new PdfPCell(new Phrase("Convênio: " + nome_convenio, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //atendente
        cell = new PdfPCell(new Phrase("Atendente: " + USUARIOS.nomeUsuario, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(3);
        tabelaDadosDoPaciente.addCell(cell);
        
        //nome paciente
        cell = new PdfPCell(new Phrase("Nome: " + nome_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDadosDoPaciente.addCell(cell);
        
        //CPF
        cell = new PdfPCell(new Phrase("CPF: " + cpf_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //nascimento
        cell = new PdfPCell(new Phrase("Nascimento: " + nascimento_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //idade
        String idade = "";
        try {
            idade = MetodosUteis.calculaIdade(nascimento_paciente, "dd/MM/yyy");
        } catch (Exception e) {
            // TODO: handle exception
        }
        cell = new PdfPCell(new Phrase("Idade: " + idade, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //Sexo Peso
        String sexoPreenchido = "Sexo: ( )F ( )M     Peso: ";
        if(sexo_paciente.equals("M")){
            sexoPreenchido = "Sexo: ( )F (x)M     Peso: ";
        }
        if(sexo_paciente.equals("F")){
            sexoPreenchido = "Sexo: (x)F ( )M     Peso: ";
        }
        cell = new PdfPCell(new Phrase(sexoPreenchido + peso_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //Altura
        cell = new PdfPCell(new Phrase("Altura: " + altura_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(3);
        tabelaDadosDoPaciente.addCell(cell);
        
        //endereco
        cell = new PdfPCell(new Phrase("Endereço: " + endereco_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDadosDoPaciente.addCell(cell);
        
        //Cidade
        cell = new PdfPCell(new Phrase("Cidade: " + cidade, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //telefone
        cell = new PdfPCell(new Phrase("Telefone: " + telefone_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Celular: " + celular_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDadosDoPaciente.addCell(cell);
        
        //exame
        cell = new PdfPCell(new Phrase("Exame: " + listaDeNomeDeExamesDoAtendimento.get(0), fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDadosDoPaciente.addCell(cell);
        
        //Medico Solicitante
        cell = new PdfPCell(new Phrase("Médico Solicitante: " + nome_medico_sol, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDadosDoPaciente.addCell(cell);
        
        //valor pago pelo paciente
        cell = new PdfPCell(new Phrase("Valor pago pelo Paciente: " + MetodosUteis.colocarZeroEmCampoReais(valor_pago_paciente).replace(".", ","), fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(3);
        tabelaDadosDoPaciente.addCell(cell);
        
        
        
        //Tabela de textos
        PdfPTable tabelaDeTextos = new PdfPTable(2);
        tabelaDeTextos.setWidths(new int[] { 50,50 });
        tabelaDeTextos.setWidthPercentage(100);
        
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase("- Histórico de Paciente:", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        for (int i = 0; i < 20; i++) {
          //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        }
        
        //linhas em branco
        cell = new PdfPCell(new Phrase("", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Cirurgia? Qual?", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("_______________________________________________________________________________________________________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Quanto tempo?", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("_______________________________________________________________________________________________________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Tratamento? (  ) Quimioterápico  (  )Radioterápico", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Exames Anteriores: (  ) RM (  ) TC (  ) RX (  ) Ecografia (  ) Mamografia (   )Outros (  ) Não", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase("Possui alguns dos seguintes itens:", fontNormalNegrito));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase("(  ) Gestante ou suspeita de gravidez", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) D.M", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Marcapasso Cardíaco/ Clipe de aneurisma", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Válvula ou Cirurgia cardíaca", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Pontes dentárias, móveis ou implantes dentários", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Bronquite/Asma", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Prótese ou Aparelho de Audição", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) DVP – Derivação ventricular", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Prótese peniana", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Insuficiência renal ou hepática", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Desfibrilador cardíaco, Neuro estimulador", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Eletrodos implantados no corpo", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Bombas de insulina", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Hipertensão arterial", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) DIU – Tempo Implantação __/__/____", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) STENT – Prótese", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Maquiagem definitiva ou tatuagem", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Hipotireoidismo/Hipertireoidismo", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Estilhaço metálicos ou bala na cabeça, olhos ou pele", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Lente de contato", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Adesivos de medicamentos", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Adereços ", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Alergia à medicação ou alimentos? Qual? _______________________________________________________________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);

            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase("Em Alguns casos será necessária injeção endovenosa do contraste paramagnético (gadolínio) do lote ___________ , validade _____/____/____ e quantidade em ml _______________. Reações alérgicas variadas a esta substância pode ocorrer." +
" Estou ciente que, caso tenha algum problema prévio, deverei informar à equipe médica responsável pelo exame antes da injeção do meio de contraste.", fontNormalNegrito));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase("(  ) Autorizo a injeção endovenosa no meio de contraste.", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("(  ) Não autorizo a injeção endovenosa no meio de contraste.", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        
        
        cell = new PdfPCell(new Phrase("Declaro que as informações acima foram por mim prestadas, eximindo clinica e seus prepostos de qualquer responsabilidade pela exatidão das mesmas.", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Farroupilha, _____de _________________ de 20____.", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        for (int i = 0; i < 3; i++) {
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
        }
        
        cell = new PdfPCell(new Phrase("___________________________________________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Assinatura Paciente ou Responsável.", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
            //linhas em branco
            cell = new PdfPCell(new Phrase("", fontNormal));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setColspan(2);
            tabelaDeTextos.addCell(cell);
            

        cell = new PdfPCell(new Phrase("_______________________________________________________________________________________________________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
      
        cell = new PdfPCell(new Phrase("_________", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("CIDI – CENTRO DE INVESTIGAÇÃO DIAGNÓTICA POR IMAGEM                Protocolo para retirada de Exames", fontNormalNegrito));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Rua Independência, 620, Centro – Farroupilha - RS      Tel: (54) 3035-1877", fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Seu exame será entregue mediante este Protocolo ou Documento de identidade.", fontNormalNegrito));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Protocolo nº: " + handle_at + "                                     Data de Retirada: " + data_exame_pronto, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(2);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Paciente: " + nome_paciente, fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Exame: " + listaDeNomeDeExamesDoAtendimento.get(0), fontNormal));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        tabelaDeTextos.addCell(cell);
        
        document.add(tableCabecalho);
        document.add(tabelaDadosDoPaciente);
        document.add(tabelaDeTextos);
        //document.add(table);
        //document.add(tableExames);
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
