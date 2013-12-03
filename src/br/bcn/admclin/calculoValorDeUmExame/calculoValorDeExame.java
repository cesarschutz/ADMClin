/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ATENÇÃO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * futuramente ter função que calcule somente os valores dos materiais de um exame (para armazenar no banco de dados)
 * 
 */
package br.bcn.admclin.calculoValorDeUmExame;

import br.bcn.admclin.dao.Conexao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 
 * @author CeSaR
 */
public class calculoValorDeExame {
    private Connection con = null;

    public int handle_convenio;
    public int handle_exame;
    public Date dataDoExame;

    public boolean calculaValorDosMateriais;

    public List<Integer> listaHandle_Material = new ArrayList<Integer>();
    public List<String> listaNomeMaterial = new ArrayList<String>();
    public List<String> listaCodigoMaterial = new ArrayList<String>();
    public List<Integer> listaQuantidadeMaterial = new ArrayList<Integer>();
    public List<Double> listaValorMaterial = new ArrayList<Double>();

    public calculoValorDeExame(int handle_convenio, int handle_exame, Date dataDoExame, boolean calculaValorDosMateriais) {
        this.handle_convenio = handle_convenio;
        this.handle_exame = handle_exame;
        this.dataDoExame = dataDoExame;
        this.calculaValorDosMateriais = calculaValorDosMateriais;
        calcularValorTotalDoExame();
    }

    public calculoValorDeExame(int handle_convenio, int handle_exame, Date dataDoExame,
        boolean calculaValorDosMateriais, double descontoPaciente) {
        this.handle_convenio = handle_convenio;
        this.handle_exame = handle_exame;
        this.dataDoExame = dataDoExame;
        this.calculaValorDosMateriais = calculaValorDosMateriais;
        this.porcentDescontoPaciente = descontoPaciente;
        calcularValorTotalDoExame();
    }

    // valor somado de todos os materiais utilizados nesse exame
    private double valorTotalMateriais = 0;

    /**
     * Metodo que verifica os handle_material e quantidade dos materiais presentes no exame
     */
    private void getMateriaisDoExame() throws SQLException {

        ResultSet resultSet = DAO.getConsultarMateriaisDoExame(con, handle_convenio, handle_exame);
        // zerando as listas
        listaHandle_Material.removeAll(listaHandle_Material);
        listaQuantidadeMaterial.removeAll(listaQuantidadeMaterial);
        while (resultSet.next()) {
            // colocando dados nos objetos
            listaHandle_Material.add(resultSet.getInt("handle_material"));
            listaQuantidadeMaterial.add(resultSet.getInt("qtdMaterial"));
            listaNomeMaterial.add(resultSet.getString("nomeMaterial"));
            listaCodigoMaterial.add(resultSet.getString("codigo"));
        }
    }

    /**
     * metodo que verifica os valores dos materiais que o exame utiliza
     */
    private void getValoresDosMateriais() throws SQLException {
        listaValorMaterial.removeAll(listaValorMaterial);

        for (int i = 0; i < listaHandle_Material.size(); i++) {
            double ultimoValorEncontrado = 0;
            ResultSet resultSet =
                DAO.getConsultarValoresMateriaisDoExame(con, listaHandle_Material.get(i), dataDoExame);
            while (resultSet.next()) {
                // colocando dados nos objetos
                ultimoValorEncontrado = Double.valueOf(resultSet.getString("valor"));
            }
            // se achouum valoradiciona na lista, se nao achar nao adicona para dar erro
            // aqui ocore se seleciono uma data que o material ainda nao tem valor
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ATENÇÃO""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (ultimoValorEncontrado != 0) {
                listaValorMaterial.add(ultimoValorEncontrado);
            } else {
                listaValorMaterial.add(-1.0);
            }
        }
    }

    /**
     * Metodo que calcula o valor total dos materiais do exame (metodo chamado no metodo principal da clase (metodo
     * principal é chamado das outras classes para calcular o valor deum exame)
     */
    private void calcularValorTotalDosMateriais() throws Exception {

        con = Conexao.fazConexao();
        getMateriaisDoExame();
        getValoresDosMateriais();

        // agora que temos os valores nas listas, podemos calcular o valor total dos materiais
        for (int i = 0; i < listaHandle_Material.size(); i++) {
            // se valor nao for nulo
            if (listaValorMaterial.get(i) >= 0) {
                // valor total eh igual a ele mesmo mais
                // valor material vezes a quantidade
                int quantidadeMaterial = listaQuantidadeMaterial.get(i);
                double valorMaterial = listaValorMaterial.get(i);

                // transformando em valorExato (para ter certeza do calculo certo
                BigDecimal valorTotalDoMaterial =
                    new BigDecimal((valorMaterial * quantidadeMaterial)).setScale(2, RoundingMode.HALF_EVEN);

                // somando o resultado com o valor total dos materiais
                valorTotalMateriais = valorTotalMateriais + valorTotalDoMaterial.doubleValue();

                // aqui vamos colocar a lista de materiais
                String valorMaterialString =
                    String.valueOf(new BigDecimal((valorMaterial)).setScale(2, RoundingMode.HALF_EVEN));
                listaDeMateriais =
                    listaDeMateriais + listaCodigoMaterial.get(i) + "&" + listaHandle_Material.get(i) + "&"
                        + listaNomeMaterial.get(i) + "&" + valorMaterialString + "&" + quantidadeMaterial + "&"
                        + valorTotalDoMaterial + "#";
            }

        }

        // arredondando o valor total dos materiais
        valorTotalMateriais = new BigDecimal(valorTotalMateriais).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        // informando ao usuario caso tenha material sem valor
        // informarMateriaisSemValorAoUsuario();
    }

    // nessa lista vai
    // codigoMaterial&handleMaterial&nomeMaterial&valorUnitario&quantidade&valorTotal#
    public String listaDeMateriais = "";

    /*
     * Agora que temos o valor total dos materiais (ou 0 caso o ususario selecionou uma data que o material nao tinha
     * valor e o usuario sera avisado!!!!!
     */
    /*
     * variaveis iniciais
     */
    public double chConvenio, filmeConvenio;
    public double ch1Exame, ch2Exame, filmeExame;
    public double porcentPaciente, porcentConvenio, porcentDescontoPaciente;
    public double redutor;

    /*
     * metodo busca o redutor do convenio
     */
    private void getPorcentagemPacienteConvenio() throws SQLException {
        ResultSet resultSet = DAO.getConsultarPorcentagemDePagamentoDeClienteEConvenio(con, handle_convenio);
        while (resultSet.next()) {
            // colocando dados nos objetos
            porcentPaciente = Double.valueOf(resultSet.getString("porcentPaciente"));
            porcentConvenio = Double.valueOf(resultSet.getString("porcentConvenio"));
        }
    }

    /**
     * variavel referente a chConvenio * o ch1Exame variavel referente a chConvenio * o ch2Exame variavel referente a
     * filmeConvenio * o filmeExame
     */
    public double ch1Total, ch2Total, filmeTotal;

    /**
     * metodo que pega a porcentagem do convenio e do paciente daquele convenio sendo consultado
     */
    private void getRedutorDoConvenioo() throws SQLException {
        ResultSet resultSet = DAO.getRedutorDoConvenio(con, handle_convenio);
        while (resultSet.next()) {
            // colocando dados nos objetos
            try {
                redutor = Double.valueOf(resultSet.getString("redutor"));
            } catch (Exception e) {
                redutor = 0;
            }

        }
    }

    /**
     * metodo que pega o valor de CH do convenio de acordo com a data selecionada
     */
    private void getCHDoConvenio() {
        try {
            ResultSet resultSet = DAO.getConsultarValorDoCHDoConvenio(con, handle_convenio, dataDoExame);
            int j = 0;
            while (resultSet.next()) {
                // colocando dados nos objetos
                chConvenio = Double.valueOf(resultSet.getString("valor"));
                j++;
            }
            // se nao entrou no while o valor fica valendo 1
            // isso para o caso de nao ter preenchido, ou o exame for uma data que nao tenha ainda nenhum valor!!
            if (j == 0) {
                chConvenio = 1;
            }
        } catch (Exception e) {
            // CAI AQUI SE SER ERRO NA BUSCA
            chConvenio = 1;
            JOptionPane.showMessageDialog(null, "Erro ao consultar valor de CH do Convênio. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * metodo que pega o valor de FILME do convenio de acordo com a data selecionada
     */
    private void getFILMEDoConvenio() {
        try {
            ResultSet resultSet = DAO.getConsultarValorDoFILMEDoConvenio(con, handle_convenio, dataDoExame);
            int j = 0;
            while (resultSet.next()) {
                // colocando dados nos objetos
                filmeConvenio = Double.valueOf(resultSet.getString("valor"));
                j++;
            }
            // se nao entrou no while o valor fica valendo 1
            // isso para o caso de nao ter preenchido, ou o exame for uma data que nao tenha ainda nenhum valor!!
            if (j == 0) {
                filmeConvenio = 1;
            }
        } catch (Exception e) {
            // CAI AQUI SE SER ERRO NA BUSCA
            filmeConvenio = 1;
            JOptionPane.showMessageDialog(null,
                "Erro ao consultar valor de Filme do Convênio. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * metodo que pega os valores deste exame, ch1, ch2 e filme
     */
    private void getValoresExame() throws SQLException {
        ResultSet resultSet = DAO.getConsultarValoresDoExame(con, handle_convenio, handle_exame);
        while (resultSet.next()) {
            // colocando dados nos objetos
            ch1Exame = Double.valueOf(resultSet.getString("cofch1"));
            ch2Exame = Double.valueOf(resultSet.getString("cofch2"));
            filmeExame = Double.valueOf(resultSet.getString("coefFilme"));
        }
    }

    /**
     * ESSE É O CARA É ELE QUE CHAMAMOS PARA VERIFICA O VALOR DE UM EXAME E ELE CHAMARA TODOS OS OUTROS DESSA CLASSE OS
     * OUTROS METODOS SERVEM SOMENTE PARA DAR SUPORTE A ESTE QUE É O QUE REALMENTE UTILIZAMOS QUANDO INSTANCIAMOS ESSA
     * CLASSE!!!!!
     */

    private void calcularValorTotalDoExame() {
        /*
         * Calcaulamos o valor total dos materiais e isso ficara na variavel valorTotalMateriais
         */
        boolean valorTotalMateriaisOk = false;
        // se for pra calcular os materiais executa o metodo
        // se nao for para calcular os valores dos materiais entao valortotalMateriais recebe 0
        if (calculaValorDosMateriais) {
            try {
                calcularValorTotalDosMateriais();
                valorTotalMateriaisOk = true;
            } catch (Exception e) {
                Conexao.fechaConexao(con);
                JOptionPane.showMessageDialog(null,
                    "Erro ao verificar Valor total dos Materiais. Procure o Administrador.", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                valorTotalMateriais = 0;
            }
        } else {
            valorTotalMateriaisOk = true;
            valorTotalMateriais = 0;
        }

        /*
         * vamos chamar os metodos para buscar os valores necessarios para o calculo
         */
        boolean todosValoresOk = false;
        // se conseguiu calcular o valor total de materiais
        if (valorTotalMateriaisOk) {
            try {
                con = Conexao.fazConexao();
                getPorcentagemPacienteConvenio();
                getRedutorDoConvenioo();
                getCHDoConvenio();
                getFILMEDoConvenio();
                getValoresExame();
                // se chegou aqui é pq deu tudo certo
                todosValoresOk = true;
            } catch (Exception e) {
                // cai aqui se der algum erro ao buscar os valores
                JOptionPane.showMessageDialog(null, "Erro ao consultar Valor de Exame. Procure o Administrador.",
                    "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                Conexao.fechaConexao(con);
            }
        }

        /*
         * Agora que temos todas as variaveis (valores) necessarios para o calculo, vamos realizar a formula apos fazer
         * a valor teremos a variavel valorTotalDoExame com o valor, com este valor calculamos a parte do paciente e a
         * parte do convenio (de acordo com as porcentagens pesquisadas no banco)
         */
        // se tudo estiver preenchido (todos os valores necessarios para a formula)
        if (todosValoresOk) {
            ch1Total = ch1Exame * chConvenio;
            ch2Total = ch2Exame * chConvenio;
            filmeTotal = filmeExame * filmeConvenio;

            valorExame =
                new BigDecimal(ch1Total + ch2Total + filmeTotal + valorTotalMateriais).setScale(2,
                    RoundingMode.HALF_EVEN).doubleValue();
            valorConvenio =
                new BigDecimal(valorExame * (porcentConvenio / 100)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            valorPaciente =
                new BigDecimal(valorExame * (porcentPaciente / 100)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

            valor_correto_convenio =
                new BigDecimal((valorExame - (valorExame * (redutor / 100))) * (porcentConvenio / 100)).setScale(2,
                    RoundingMode.HALF_EVEN).doubleValue();
            valor_correto_paciente =
                new BigDecimal((valorExame - (valorExame * (redutor / 100))) * (porcentPaciente / 100)).setScale(2,
                    RoundingMode.HALF_EVEN).doubleValue();

            // se o desconto for maior que 0 calculamos o desconto
            if (porcentDescontoPaciente > 0) {
                valor_desconto =
                    new BigDecimal((valor_correto_paciente * (porcentDescontoPaciente / 100))).setScale(2,
                        RoundingMode.HALF_EVEN).doubleValue();
                valor_correto_paciente =
                    new BigDecimal(valor_correto_paciente - valor_desconto).setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
            } else {
                valor_desconto = 0;
            }

            valor_correto_exame =
                new BigDecimal(valor_correto_paciente + valor_correto_convenio).setScale(2, RoundingMode.HALF_EVEN)
                    .doubleValue();
        }
    }

    public double valor_correto_exame;
    public double valor_correto_paciente;
    public double valor_correto_convenio;
    public double valorExame;
    public double valorPaciente;
    public double valorConvenio;
    public double valor_desconto;
}
