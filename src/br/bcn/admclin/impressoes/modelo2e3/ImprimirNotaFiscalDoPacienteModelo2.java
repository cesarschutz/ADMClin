/*
 * quando trocar o formulario deve se dar 6 linhas para fica na posição correta
 * o nome do compartilhamento da impressora é \\\\localhost\\EPLX300
 */
package br.bcn.admclin.impressoes.modelo2e3;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.ESCPrinter;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public class ImprimirNotaFiscalDoPacienteModelo2 {
    // instancia da classe
    private Connection con = null;
    private ESCPrinter imprimir;    
    private PacienteModel paciente = new PacienteModel();
    private int handle_at;
    public List<ExameModel> listaDeExames = new ArrayList<ExameModel>();
    private String nomeDoArquivo = janelaPrincipal.internalFrameJanelaPrincipal.codigoParaImpressoesLinux + "NOTAFISCAL";

    public ImprimirNotaFiscalDoPacienteModelo2(int handle_at) {
        this.handle_at = handle_at;
    }
    
    /**
     * metodo antigo
     * 
    private void instanciarImpressora(){
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            imprimir = new ESCPrinter(nomeDoArquivo, true);
        }else{
        	String[] list = {"1", "2"}; 
        	JComboBox jcb = new JComboBox<>();  
        	JOptionPane.showMessageDialog( null, jcb, "Selecione a impressora desejada:", JOptionPane.QUESTION_MESSAGE);
        	String impressoraParaImpressao = USUARIOS.impressora_nota_fiscal + jcb.getSelectedItem().toString();
        	JOptionPane.showMessageDialog(null, impressoraParaImpressao);
        	
            imprimir = new ESCPrinter(USUARIOS.impressora_nota_fiscal, true);
        }
    }
    */
    
    private void instanciarImpressora(){
        
    	//preenche as impressoras no combo box
        	String[] list = {"1", "2"}; 
        	JComboBox<String> jcb = new JComboBox<>();  
        	for (String impressora : buscarImpressoras()) {
				jcb.addItem(impressora);
			}
        	
        	JOptionPane.showMessageDialog( null, jcb, "Selecione a impressora desejada:", JOptionPane.QUESTION_MESSAGE);
        	
        	//cria o nome da impressora
        	String impressoraParaImpressao = USUARIOS.impressora_nota_fiscal + jcb.getSelectedItem().toString();
        	
        	//instancia a impressora
            imprimir = new ESCPrinter(USUARIOS.impressora_nota_fiscal, true);

    }
    
    private ArrayList<String> buscarImpressoras(){
    	ArrayList<String> listaRetorno = new ArrayList<>();
    	
    	DocFlavor dflavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    	 PrintService[] impressoras = PrintServiceLookup.lookupPrintServices(dflavor, null);
         for(PrintService ps : impressoras){
             listaRetorno.add(ps.getName());
         }
         return listaRetorno;
    }
    
    private void imprimirNotaCasoSejaLinux() throws IOException{
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
            Runtime.getRuntime().exec("lpr -P " + USUARIOS.impressora_nota_fiscal + " " + nomeDoArquivo);  
        }
    }

    public boolean imprimir() {
        boolean imprimiu = false;
        con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
        try {
            instanciarImpressora();
            getDadosPaciente();
            getExamesRealizados();
            imprimirNotaNew();
            imprimirNotaCasoSejaLinux();
            imprimiu = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao imprimir Nota Fiscal. Procure o Administrador.", "Erro",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            br.bcn.admclin.dao.dbris.Conexao.fechaConexao(con);
            imprimir.close();
        }
        return imprimiu;
    }

    private void imprimirNotaNew() {
        imprimir.initialize();
        int i = 0;
        int numExamesImpressos = 0;
        for (i = 1; i < (listaDeExames.size() + 1); i++) {
            if (numExamesImpressos == 0) {
                imprimirCabec();
            }

            imprimirExame(i);
            numExamesImpressos++;

            if (numExamesImpressos == 4 || i == listaDeExames.size()) {
                imprimirTotal(numExamesImpressos);
                numExamesImpressos = 0;
            }
        }
        
        imprimir.close();
    }

    private void imprimirCabec() {

    	imprimir.lineFeed();
    	imprimir.lineFeed();
    	imprimir.lineFeed();
    	imprimir.lineFeed();
    	imprimir.lineFeed();
    	imprimir.lineFeed();
        // imprimindo a ficha
        // ficha da casa 1 ateh a 50
        imprimir.print("    FICHA: " + arrumarTamanhoDaString(String.valueOf(handle_at), 47));

        // colocando o espaçamento para ir o nome do paciente
        imprimir.lineFeed(); // linha 7 (começa a imprimir na 6)

        // aqui vamos imprimir a data
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format(hoje.getTime());
        // data da casa 62 por isso os espaços e no maximo 15 casas
        imprimir.print("                                                                "
            + arrumarTamanhoDaString(dataDeHoje, 13));

        imprimir.lineFeed(); // linha 8
        imprimir.lineFeed(); // linha 9

        // colocando o dados do paciente
        // nome colocamos 8 espaços em branco e dps o nome poe ter no maximo 45 casas (acaba na casa 53)
        imprimir.print("          " + arrumarTamanhoDaString(paciente.getNome(), 43));
        imprimir.lineFeed();
        // o endereço tem 8 espaços em branco e no maximo 45 casas (acaba na casa 53)
        // o bairro tem 4 casas em branco e pode ter 20 casas ( acaba na 78 )
        imprimir.print("          " + arrumarTamanhoDaString(paciente.getEndereco(), 43) + "      "
            + arrumarTamanhoDaString(paciente.getBairro(), 18));
        imprimir.lineFeed();
        // cidade tem 8 casas em branco e no maximo 25 casas (acaba na 33)
        // estado tem 5 casas em branco e no maximo 15 (acaba na 53)
        // cep tem 3 espaços em branco e no maximo 22 casas ( acaba na 78)
        imprimir.print("          " + arrumarTamanhoDaString(paciente.getCidade(), 23) + "       "
            + arrumarTamanhoDaString(paciente.getEstado(), 13) + "     " + arrumarTamanhoDaString(paciente.getCep(), 19));
        imprimir.lineFeed();
        // cpf ou cnpj tem 8 casas em branco mais 25 casas (acaba na 33)
        // inscricao estadual tem 8 espaços e no maximo 12 casas (acaba na 53)
        // cond. pagamento tem 9 espaços en no maximo 15 (acaba na 78)
        imprimir.print("          " + arrumarTamanhoDaString(paciente.getCpf_cnpj(), 23) + "          "
            + arrumarTamanhoDaString(paciente.getInscricao_estadual(), 12) + "         "
            + arrumarTamanhoDaString("À VISTA", 13));
        imprimir.lineFeed();
        imprimir.lineFeed();

    }

    // podemos imprimir no maximo 6 exames
    // linhas 14,15,16,17
    double valor_total_da_nota = 0.0;

    private void imprimirExame(int i) {
        // colocando o nome
        String nome_e_codigo =
            listaDeExames.get(i - 1).getCod_exame() + " - "
                + listaDeExames.get(i - 1).getNome();
        imprimir.print("   " + arrumarTamanhoDaString(nome_e_codigo, 45));

        // imprimindo a quantidade
        imprimir.print(arrumarStringAlinhadaADireita(String.valueOf("01"), 6));

        // imprimindo valor unitario do exame
        imprimir.print(arrumarStringAlinhadaADireita(
            MetodosUteis.colocarZeroEmCampoReais(String.valueOf(listaDeExames.get(i - 1).getValor_correto_paciente()))
                .replace(".", ","), 11));

        // imprimindo valor total do exame
        imprimir.print(arrumarStringAlinhadaADireita(
            MetodosUteis.colocarZeroEmCampoReais(String.valueOf(listaDeExames.get(i - 1).getValor_correto_paciente()))
                .replace(".", ","), 11));

        valor_total_da_nota =
            new BigDecimal(valor_total_da_nota + Double.valueOf(listaDeExames.get(i - 1).getValor_correto_paciente()))
                .setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        imprimir.lineFeed();
    }

    private void imprimirTotal(int i) {
        int numLF = 4 - i;

        for (int j = 0; j < numLF; j++) {
            imprimir.lineFeed();

        }

        imprimir.lineFeed();
        imprimir.lineFeed();

        // arrumando o valor da nota
        String valorTotalDaNota =
            MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valor_total_da_nota)).replace(".", ",");
        imprimir.print("                                                            "
            + arrumarStringAlinhadaADireita(valorTotalDaNota, 16));
        valor_total_da_nota = 0.0;

        for (int j = 0; j < 4; j++) {
            imprimir.lineFeed();
        }
    }

    private void getDadosPaciente() throws SQLException {
        ResultSet resultSet = ImprimirNotaFiscalDAO.getConsultarPaciente(con, handle_at);
        while (resultSet.next()) {
            paciente.setNome(resultSet.getString("nome"));
            paciente.setEndereco(resultSet.getString("endereco"));
            paciente.setBairro(resultSet.getString("bairro"));
            paciente.setCidade(resultSet.getString("cidade"));
            paciente.setCep(resultSet.getString("cep"));
            paciente.setEstado(resultSet.getString("uf"));
            paciente.setCpf_cnpj(resultSet.getString("cpf"));
            paciente.setInscricao_estadual(resultSet.getString("rg"));
        }
    }

    private void getExamesRealizados() throws SQLException {
        ResultSet resultSet = ImprimirNotaFiscalDAO.getConsultarExames(con, handle_at);
        listaDeExames.clear();
        while (resultSet.next()) {
            ExameModel exame = new ExameModel();
            exame.setCod_exame(resultSet.getString("cod_exame"));
            exame.setNome(resultSet.getString("nome"));
            exame.setValor_correto_paciente(resultSet.getString("valor_correto_paciente"));

            listaDeExames.add(exame);
        }
    }

    private String arrumarTamanhoDaString(String stringASerArrumada, int numeroDeCasasQueDeveTer) {
        if (stringASerArrumada.length() > numeroDeCasasQueDeveTer) { // se string for maior que o numero de casas
            // cortamos ela de acordo com o numero de casas
            stringASerArrumada = stringASerArrumada.substring(0, numeroDeCasasQueDeveTer);

        } else if (stringASerArrumada.length() < numeroDeCasasQueDeveTer) { // se string for menor que o numero de casas

            // colocamos as casas que faltam
            int numeroDeCasasQueFaltam = numeroDeCasasQueDeveTer - stringASerArrumada.length();
            for (int i = 0; i < numeroDeCasasQueFaltam; i++) {
                stringASerArrumada = stringASerArrumada + " ";
            }

        }

        return stringASerArrumada;
    }

    private String arrumarStringAlinhadaADireita(String stringASerArrumada, int numeroDeCasas) {
        String espacosParaFicarAlinhadoCorretamente = "";

        if (stringASerArrumada.length() > numeroDeCasas) { // se string for maior que o numero de casas
            // cortamos ela de acordo com o numero de casas
            stringASerArrumada = stringASerArrumada.substring(0, numeroDeCasas);
        } else if (stringASerArrumada.length() < numeroDeCasas) {
            int numeroDeCasasQueFaltam = numeroDeCasas - stringASerArrumada.length();
            for (int i = 0; i < numeroDeCasasQueFaltam; i++) {
                espacosParaFicarAlinhadoCorretamente = espacosParaFicarAlinhadoCorretamente + " ";
            }
            stringASerArrumada = espacosParaFicarAlinhadoCorretamente + stringASerArrumada;
        }
        return stringASerArrumada;
    }

}
