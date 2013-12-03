/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.menu.financeiro.relatorios.faturarConvenio.arquivoTxtDoIpe;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.DADOS_EMPRESA;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.CONVENIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import menu.financeiro.relatorios.faturarConvenio.faturarConvenio.atendimentoModel;

/**
 *
 * @author BCN
 */
public class GerarArquivoTxtDaFatura {

    private int handle_convenio = 0;
    private String nome, tipo;
    private int grupo_id;
    private Date dataInicial = null, dataFinal  = null;
    private List<atendimentoModel> listaDeAtendimentos = new ArrayList<atendimentoModel>();
    
    public GerarArquivoTxtDaFatura(String tp, Date dataInicial, Date dataFinal, String nome, int handle_convenio, List<atendimentoModel> listaAtendimentos){
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.handle_convenio = handle_convenio;
        this.nome = nome;
        this.tipo = tp;
        this.listaDeAtendimentos = listaAtendimentos; 
    }
    
    public GerarArquivoTxtDaFatura(String tp, Date dataInicial, Date dataFinal, String nome, int grupo_id, int x, List<atendimentoModel> listaAtendimentos){
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.grupo_id = grupo_id;
        this.nome = nome;
        this.tipo = tp;
        this.listaDeAtendimentos = listaAtendimentos; 
    }
    
    String caminho = "";
    private void criandoAPastaParaSalvarOArquivo(){
        if (OSvalidator.isWindows()) {
            if (tipo.equals("grupo")) {
                caminho = USUARIOS.pasta_raiz + "\\FaturasDeConveniosPorGrupo\\ArquivoTexto";
            }else{
                caminho = USUARIOS.pasta_raiz + "\\FaturasDeConvenios\\ArquivoTexto";
            }
            
        }else {
            if (tipo.equals("grupo")) {
                caminho = USUARIOS.pasta_raiz + "/FaturasDeConveniosPorGrupo/ArquivoTexto";
            }else{
                caminho = USUARIOS.pasta_raiz + "/FaturasDeConvenios/ArquivoTexto";
            }
        }
        File dir = new File(caminho);   
        boolean result = dir.mkdirs();
    }
    
    private String cnpjEmpresa, nomeEmpresa;
    private int nro_prestador_ipe;
    private void buscarInformacoesDaEmpresaNoBanco() throws SQLException{
        ResultSet resultSet = DADOS_EMPRESA.getConsultar(con);
        while(resultSet.next()){
            cnpjEmpresa = resultSet.getString("cnpj");
            nomeEmpresa = resultSet.getString("nome");
            nro_prestador_ipe = resultSet.getInt("nro_prestador_ipe");
        }
    }
    
    //varre os atendimentos enviados por parametro e busca os exames destes atendimentos
    private void buscaExames() throws SQLException{
        listaDeExames.removeAll(listaDeExames);
        for (atendimentoModel atendimento : listaDeAtendimentos) {
            buscarAtendimentosNoBanco(atendimento.getHandle_at());
        }
    }
    
    //busca os atendimentos e cria as notas
    private List<exameMODEL> listaDeExames = new ArrayList<exameMODEL>();
    int totalDeLançamentos = 0;
    private void buscarAtendimentosNoBanco(int handle_at) throws SQLException{
        ResultSet resultSet;
        if (tipo.equals("grupo")) {
            resultSet = GerarArquivoTxtDaFaturaDAO.getConsultarAtendimentosPorPeriodoEGrupo(con, dataInicial, dataFinal, grupo_id, handle_at);
        } else{
            resultSet = GerarArquivoTxtDaFaturaDAO.getConsultarAtendimentosPorPeriodoEConvenio(con, dataInicial, dataFinal, handle_convenio, handle_at);
        }
        
        while(resultSet.next()){
            exameMODEL exame = new exameMODEL();
            exame.setHandle_at(resultSet.getString("handle_at"));
            exame.setMatricula(resultSet.getString("matricula_convenio"));
            exame.setCrm(resultSet.getString("crm"));
            int falta = 8 - exame.getCrm().length();
            for (int i = 0; i < falta; i++) {
                exame.setCrm("0" + exame.getCrm());
            }
            
            String data = resultSet.getString("data_atendimento");
            String[] dataDivida = data.split("-");
            
            exame.setDia(dataDivida[2]);
            exame.setCod_exame(resultSet.getString("cod_exame"));
            exame.setQtd("00001");
            exame.setNome_paciente(resultSet.getString("nome"));
            exame.setValor_correto_convenio(Double.valueOf(resultSet.getString("valor_correto_convenio")));
            exame.setAno(dataDivida[0]);
            exame.setMes(dataDivida[1]);
            
            //colocando o valor dos materiais
            String listaMateriais = resultSet.getString("lista_materiais");
            if (!"".equals(listaMateriais) && !" ".equals(listaMateriais) && listaMateriais!= null) {                
                String[] material = listaMateriais.split("#");
                double valor_materiais = 0.0;
                for (int i = 0; i < material.length; i++) {
                    String[] dadosDoMaterial = material[i].split("&");
                    valor_materiais = valor_materiais + Double.valueOf(dadosDoMaterial[5]);
                }
                double porcentagemConvenio = Double.valueOf(resultSet.getString("porcentconvenio")) / 100;
                exame.setValor_materiais(new BigDecimal(valor_materiais * porcentagemConvenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
                
                //aqui vamos arrumar a quantidade caso seja exame 32200005
                if ((exame.getValor_correto_convenio() - exame.getValor_materiais()) < 1) {
                    double qtd = exame.getValor_materiais() / Double.valueOf(resultSet.getString("valor"));
                    double qtdArrumada = new BigDecimal(qtd).setScale(0, RoundingMode.HALF_EVEN).doubleValue();
                    int qtdInteira = (int) qtdArrumada;
                    String qtdString = String.valueOf(qtdInteira);
                    //colocando os zeros faltantes
                    int faltaa = 5 - qtdString.length();
                    for (int i = 0; i < faltaa; i++) {
                        qtdString = "0" + qtdString;
                    }
                    exame.setQtd(qtdString);
                    exame.setCod_exame("32200005");
                }else{
                    //aqui caso nao for esse codigo temos que criar um novo exame para os materiais
                    //primeiro adicionamos este exame e dps vamos adicionar igual porem com o codigo de material e quantidade de material
                    //precisamos zere o valor corrteo convenio para nao duplicar, pegar somente o valor dos materiais
                    double qtd = exame.getValor_materiais() / Double.valueOf(resultSet.getString("valor"));
                    double qtdArrumada = new BigDecimal(qtd).setScale(0, RoundingMode.HALF_EVEN).doubleValue();
                    int qtdInteira = (int) qtdArrumada;
                    String qtdString = String.valueOf(qtdInteira);
                    //colocando os zeros faltantes
                    int faltaa = 5 - qtdString.length();
                    for (int i = 0; i < faltaa; i++) {
                        qtdString = "0" + qtdString;
                    }
                    
                    exameMODEL linhaDeMateriais = new exameMODEL();
                    linhaDeMateriais.setHandle_at(exame.getHandle_at());
                    linhaDeMateriais.setMatricula(exame.getMatricula());
                    linhaDeMateriais.setCrm(exame.getCrm());
                    linhaDeMateriais.setDia(exame.getDia());
                    linhaDeMateriais.setNome_paciente(exame.getNome_paciente());
                    linhaDeMateriais.setValor_correto_convenio(0);
                    linhaDeMateriais.setValor_materiais(0);
                    linhaDeMateriais.setAno(exame.getAno());
                    linhaDeMateriais.setMes(exame.getMes());
                    
                    linhaDeMateriais.setQtd(qtdString);
                    linhaDeMateriais.setCod_exame("32200005");
                    
                    listaDeExames.add(linhaDeMateriais);
                    
                    totalDeLançamentos++;
                }
            }else{
                exame.setValor_materiais(0.0);
            }
            
            
            
            listaDeExames.add(exame);
            //variavel utilizada para criar o header (tem que saber o total de lançamentos
            totalDeLançamentos++;
        }
    }
    
    int numeroNota = 1;
    private void colocarNumeroDaNotaNaLista(){
        int numeroLinha = 0;
        for (int i = 0; i < listaDeExames.size(); i++) {
            
            //para saber se ainda cabe paciente nesta nota
            if (numeroLinha < 20) {
                //verificar se o proximo paciente cabe no que resta
                if (verificaSeProximoCabeNaNota(i, numeroLinha)) {
                    numeroLinha++;
                    listaDeExames.get(i).setNn(String.valueOf(numeroNota));
                    listaDeExames.get(i).setRef(String.valueOf(numeroLinha));
                } else {
                    numeroLinha = 1;
                    numeroNota++;
                    listaDeExames.get(i).setNn(String.valueOf(numeroNota));
                    listaDeExames.get(i).setRef(String.valueOf(numeroLinha));
                }
            }
        }
        
    }
    private boolean verificaSeProximoCabeNaNota(int index, int numeroLinha){
        int n = 0;
        String matricula1 = listaDeExames.get(index).getMatricula();
        for (int i = index+1; i < listaDeExames.size(); i++) {
            String matricula2 = listaDeExames.get(i).getMatricula();
            if (matricula2.equals(matricula1))  n++;
            else                                break;
        }
        int totalLinhas = (numeroLinha + n + 1);
        if (totalLinhas < 21)       
            return true;
        else                        
            return false;
    }
    
    //criando o header
    String header;
    private void criaHeader(){
        //arrumando a quantidade de notas para ter 4 digitos
        String qtdNotas = String.valueOf(numeroNota);
        if(String.valueOf(numeroNota).length() < 4){
            int falta = 4 - String.valueOf(numeroNota).length();
            for (int i = 0; i < falta; i++) {
                qtdNotas = "0" + qtdNotas;
            } 
        }
        
        //arrumando a quantidade de lançamentos para ter 5 digitos
        String qtdLancamentos = String.valueOf(totalDeLançamentos);
        if(String.valueOf(totalDeLançamentos).length() < 5){
            int falta = 5 - String.valueOf(totalDeLançamentos).length();
            for (int i = 0; i < falta; i++) {
                qtdLancamentos = "0" + qtdLancamentos;
            } 
        }
        String numeroPrestadorIpe = String.valueOf(nro_prestador_ipe);
        if(numeroPrestadorIpe.length() < 8){
            int falta = 6 - numeroPrestadorIpe.length();
            for (int i = 0; i < falta; i++) {
                numeroPrestadorIpe = "0" + numeroPrestadorIpe;
            } 
        }
        numeroPrestadorIpe = "10" + numeroPrestadorIpe;
        
        header = "SMH" + cnpjEmpresa + qtdNotas + qtdLancamentos + numeroPrestadorIpe + nomeEmpresa;
        
        if (header.length() < 81) {
            int falta = 81 - header.length();
            for (int i = 0; i < falta; i++) {
                header = header + " ";
            } 
        }
    }
    
    
    private void escrever() throws IOException{
        String nota = "", headerNota = "";
        double valorTotalNota=0.0, valorTotalMateriais=0.0;
        int qtdLancamentosDaNota = 0;
        FileWriter arquivo;
        arquivo = new FileWriter(new File(caminho + "\\smh"+String.valueOf(nro_prestador_ipe)+".035"));
        
        //escrevendo o header
        arquivo.write(header + "\r\n");
        
        
        //escrevendo as notas
        for (int i = 1; i <= numeroNota; i++) {
            for (int j = 0; j < listaDeExames.size(); j++) {
                //se a nota da lista for a mesma nota que estamos montando entra aqui
                if (Integer.valueOf(listaDeExames.get(j).getNn()) == i) {
                    //aqui vamos arrumar a referencia, colocar dois digitos
                    if (listaDeExames.get(j).getRef().length() < 2) {
                        listaDeExames.get(j).setRef("0" + listaDeExames.get(j).getRef());
                    }
                    
                    //arrumando o crm para ter 8 digitos
                    String crmMedico = listaDeExames.get(j).getCrm();
                    if(crmMedico.length() < 6){
                        int falta = 6 - crmMedico.length();
                        for (int h = 0; h < falta; h++) {
                            crmMedico = "00" + crmMedico;
                        } 
                    }
                    
                    //aqui vamos arrumar o dia caso tenho somente um digito
                    if (listaDeExames.get(j).getDia().length() < 2) {
                        listaDeExames.get(j).setDia("0" + listaDeExames.get(j).getDia());
                    }
                    
                    //arrumando o cod_exame (tirando os . e -)
                    listaDeExames.get(j).setCod_exame(listaDeExames.get(j).getCod_exame().replace(".", ""));
                    listaDeExames.get(j).setCod_exame(listaDeExames.get(j).getCod_exame().replace("-", ""));
                    
                    //arrumando o tamanho do nome
                    String nomePaciente = listaDeExames.get(j).getNome_paciente();
                    if (nomePaciente.length() < 43) {
                        int falta = 43 - nomePaciente.length();
                        for (int h = 0; h < falta; h++) {
                            nomePaciente = nomePaciente + " ";
                        }
                    } else if(nomePaciente.length() > 43){
                        nomePaciente = nomePaciente.substring(0,43);
                    }
                    
                    //arumando o nome do arquivo
                    String nomeDoArquivo = listaDeExames.get(j).getHandle_at() + ".pdf";
                    if (nomeDoArquivo.length() < 50) {
                        int falta = 50 - nomeDoArquivo.length();
                        for (int h = 0; h < falta; h++) {
                            nomeDoArquivo = nomeDoArquivo + " ";
                        }
                    } else if(nomeDoArquivo.length() > 50){
                        nomeDoArquivo = nomeDoArquivo.substring(0,50);
                    }
                    
                    //fazendo a nota
                    nota = nota + listaDeExames.get(j).getRef() + listaDeExames.get(j).getMatricula() + crmMedico + listaDeExames.get(j).getDia() + listaDeExames.get(j).getCod_exame() +
                            listaDeExames.get(j).getQtd() + nomePaciente + nomeDoArquivo + "\r\n";
                    valorTotalNota = valorTotalNota + listaDeExames.get(j).getValor_correto_convenio();
                    valorTotalMateriais = valorTotalMateriais + listaDeExames.get(j).getValor_materiais();
                    qtdLancamentosDaNota++;
                } 
                //se a nota da lista for maior da que estamos montando ele da um break e vai para a proxima noto
                else if(Integer.valueOf(listaDeExames.get(j).getNn()) > i){
                    //escrevendo o header da nota montada
                    String qtdLancamentosNotaString = String.valueOf(qtdLancamentosDaNota);
                    if (qtdLancamentosNotaString.length() < 2) {
                        qtdLancamentosNotaString = "0" + qtdLancamentosNotaString;
                    }
                    //arrumando o valor total da nota
                    String valorTotalNotaString = MetodosUteis.colocarZeroEmCampoReais(String.valueOf(new BigDecimal(valorTotalNota).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
                    valorTotalNotaString = valorTotalNotaString.replace(".", "");
                    if (valorTotalNotaString.length() < 13) {
                        int falta = 13 - valorTotalNotaString.length();
                        for (int h = 0; h < falta; h++) {
                            valorTotalNotaString = "0" + valorTotalNotaString;
                        }
                    }
                    
                    //arrumando o valor total de materiais
                    String valorTotalMateriaisString = MetodosUteis.colocarZeroEmCampoReais(String.valueOf(new BigDecimal(valorTotalMateriais).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
                    valorTotalMateriaisString = valorTotalMateriaisString.replace(".", "");
                    if (valorTotalMateriaisString.length() < 9) {
                        int falta = 9 - valorTotalMateriaisString.length();
                        for (int h = 0; h < falta; h++) {
                            valorTotalMateriaisString = "0" + valorTotalMateriaisString;
                        }
                    }
                    //pegando o ano com dois digitos
                    String ano = listaDeExames.get(j).getAno().substring(2);
                    //arrumando o numero da nota para 5 casas
                    int numeroNotaMenosUm = Integer.valueOf(listaDeExames.get(j).getNn()) - 1;
                    String numeroNotaString = String.valueOf(numeroNotaMenosUm);
                    if (numeroNotaString.length() < 5) {
                        int falta = 5 - numeroNotaString.length();
                        for (int h = 0; h < falta; h++) {
                            numeroNotaString = "0" + numeroNotaString;
                        }
                    }
                    
                    String espacosEmBranco = "                            ";
                    headerNota = "0035" + qtdLancamentosNotaString + cnpjEmpresa + valorTotalNotaString + valorTotalMateriaisString + "10" + 
                            listaDeExames.get(j).getMes() + ano + numeroNotaString + espacosEmBranco + "\r\n";
                    arquivo.write(headerNota);
                    headerNota = "";
                    qtdLancamentosDaNota = 0;
                    valorTotalMateriais = 0.0;
                    valorTotalNota = 0.0;
                    
                    //imprimindo a nota montada
                    arquivo.write(nota);
                    nota = "";
                    break;
                }
            }
        }
        
        
        //escrevendo o ultimo header da nota montada
        String qtdLancamentosNotaString = String.valueOf(qtdLancamentosDaNota);
                    if (qtdLancamentosNotaString.length() < 2) {
                        qtdLancamentosNotaString = "0" + qtdLancamentosNotaString;
                    }
                    //arrumando o valor total da nota
                    String valorTotalNotaString = MetodosUteis.colocarZeroEmCampoReais(String.valueOf(new BigDecimal(valorTotalNota - valorTotalMateriais).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
                    valorTotalNotaString = valorTotalNotaString.replace(".", "");
                    if (valorTotalNotaString.length() < 13) {
                        int falta = 13 - valorTotalNotaString.length();
                        for (int h = 0; h < falta; h++) {
                            valorTotalNotaString = "0" + valorTotalNotaString;
                        }
                    }
                    
                    //arrumando o valor total de materiais
                    String valorTotalMateriaisString = MetodosUteis.colocarZeroEmCampoReais(String.valueOf(new BigDecimal(valorTotalMateriais).setScale(2, RoundingMode.HALF_EVEN).doubleValue()));
                    valorTotalMateriaisString = valorTotalMateriaisString.replace(".", "");
                    if (valorTotalMateriaisString.length() < 9) {
                        int falta = 9 - valorTotalMateriaisString.length();
                        for (int h = 0; h < falta; h++) {
                            valorTotalMateriaisString = "0" + valorTotalMateriaisString;
                        }
                    }
                    //pegando o ano com dois digitos
                    String ano = listaDeExames.get(0).getAno().substring(2);
                    //arrumando o numero da nota para 5 casas
                    String numeroNotaString = String.valueOf(numeroNotaParaImprimir);
                    if (numeroNotaString.length() < 5) {
                        int falta = 5 - numeroNotaString.length();
                        for (int h = 0; h < falta; h++) {
                            numeroNotaString = "0" + numeroNotaString;
                        }
                    }
                    
                    String espacosEmBranco = "                            ";
                    headerNota = "0035" + qtdLancamentosNotaString + cnpjEmpresa + valorTotalNotaString + valorTotalMateriaisString + "10" + 
                            listaDeExames.get(0).getMes() + ano + numeroNotaString + espacosEmBranco + "\r\n";
                    numeroNotaParaImprimir = numeroNotaParaImprimir + 1;
        arquivo.write(headerNota);
        
        //escrevendo a ultima nota
        arquivo.write(nota);
        
        
        arquivo.close();  
        
    }
    
    int numeroNotaParaImprimir;
    private boolean getNumeroNota(){
        try {
            int numeroNotaS = 0;
            con = Conexao.fazConexao();
            if (tipo.equals("grupo")) {
                ResultSet rs = CONVENIO.getConsultarDadosDeUmGrupo(con, grupo_id);
                while(rs.next()){
                    numeroNotaS = rs.getInt("numero_nota");
                }
            }else{
                ResultSet rs = CONVENIO.getConsultarDadosDeUmConvenio(con, handle_convenio);
                while(rs.next()){
                    numeroNotaS = rs.getInt("numero_nota");
                }
            }
            
            try {
                numeroNotaParaImprimir = numeroNotaS;
            } catch (Exception e) {
                numeroNotaParaImprimir = 1;
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar número da nota. Procure o Administrador.","Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
    }
    
    
    private Connection con = null;
    public void criarTxt(){
        if (getNumeroNota()) {
            try {
                con = Conexao.fazConexao();
                criandoAPastaParaSalvarOArquivo();
                buscarInformacoesDaEmpresaNoBanco();
                buscaExames();
                colocarNumeroDaNotaNaLista();
                criaHeader();
                System.out.println(listaDeExames.size());
                escrever();
                validarNumeroNota();
                Conexao.fechaConexao(con);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(janelaPrincipal.janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao gerar arquivo TXT desta fatura. Procure o Administrador." + e, "Erro", JOptionPane.ERROR_MESSAGE);
                Conexao.fechaConexao(con);
            }
        }
    }   
    
    private void validarNumeroNota(){
        //salvar o numero da nota e salvar os valores no banco!
           if (tipo.equals("grupo")) {
                boolean cadastrou = false;
                do{
                    cadastrou = CONVENIO.setUpdateNumeroNotaGrupo(con, numeroNotaParaImprimir, grupo_id);
                }while(!cadastrou);

            }else{
               boolean cadastrou = false;
                do{
                    cadastrou = CONVENIO.setUpdateNumeroNotaConvenio(con, numeroNotaParaImprimir, handle_convenio);
                }while(!cadastrou);
            } 
    }
}
