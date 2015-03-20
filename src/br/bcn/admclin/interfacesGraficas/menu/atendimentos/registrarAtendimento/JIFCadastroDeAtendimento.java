package br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento;


import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXDatePicker;

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteLetras;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.VerificacaoDeMatricula;
import br.bcn.admclin.calculoValorDeUmExame.CalculoValorDeExame;
import br.bcn.admclin.dao.dbris.AREAS_ATENDIMENTO;
import br.bcn.admclin.dao.dbris.ATENDIMENTOS;
import br.bcn.admclin.dao.dbris.ATENDIMENTO_EXAMES;
import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.EXAMES;
import br.bcn.admclin.dao.dbris.MEDICOS;
import br.bcn.admclin.dao.dbris.NAGENDAMENTOS;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.dao.model.Atendimento_Exames;
import br.bcn.admclin.dao.model.Atendimentos;
import br.bcn.admclin.dao.model.NagendamentosExames;
import br.bcn.admclin.impressoes.modelo1.ImprimirBoletoDeRetiradaModelo1;
import br.bcn.admclin.impressoes.modelo1.ImprimirFichaDeAutorizacaoModelo1;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirFichaEBoletoDeRetiradaModelo2;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirFichaEBoletoDeRetiradaModelo3;
import br.bcn.admclin.impressoes.modelo2e3.ImprimirNotaFiscalDoPacienteModelo2;
import br.bcn.admclin.impressoes.modelo4.ImprimirFichaDeAutorizacaoModelo4;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos.TratamentoParaRegistrarAtendimentoApartirDeAgendamento;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JCheckBox;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCadastroDeAtendimento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public List<Double> listaPorcentagemPaciente = new ArrayList<Double>();
    public List<Double> listaPorcentagemConvenio = new ArrayList<Double>();

    public List<Integer> listaHandleConvenio = new ArrayList<Integer>();

    public ArrayList<Areas_atendimento> listaAreasDeAtendimento = new ArrayList<Areas_atendimento>();

    public List<Integer> listaHandleExames = new ArrayList<Integer>();
    public List<Integer> listaDuracaoExames = new ArrayList<Integer>();
    public List<Integer> listaVaiMateriaisPorPadrao = new ArrayList<Integer>();

    private Connection con = null;
    public static int handle_paciente;
    public static int handle_medico_sol;
    int handle_at;

    java.sql.Date dataDeHojeEmVariavelDate = null;

    public void pegandoDataDoSistema() {
        // pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format(hoje.getTime());
        try {
            dataDeHojeEmVariavelDate = new java.sql.Date(format.parse(dataDeHoje).getTime());
        } catch (ParseException ex) {
        }
    }

    String horarioLivreOuOcupado;

    boolean veioDeAgendamento = false;
    /**
     * Creates new form JIFagendamento
     * 
     * @wbp.parser.constructor
     */
    public JIFCadastroDeAtendimento(String horarioLivreOuOcupado, int handle_at, boolean veioDeAgendamento) {

        initComponents();
        this.veioDeAgendamento = veioDeAgendamento;
        if(!veioDeAgendamento){
            jCBAreasDoAgendamento.setVisible(false);
            jLAreasDoAgendamento.setVisible(false);
        }
        veioDaPesquisa = false;
        con = Conexao.fazConexao();

        jXDPEntregaDoExame.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDPEntregaDoExame.setLinkDate(System.currentTimeMillis(), "Hoje");
        JXDPDataAtendimento.setFormats(new String[] { "E dd/MM/yyyy" });
        JXDPDataAtendimento.setLinkDate(System.currentTimeMillis(), "Hoje");
        pegandoDataDoSistema();

        Dimension tamanhoBotaoDesconto = new Dimension(121, 30);
        jTBDesconto.setPreferredSize(tamanhoBotaoDesconto);
        jTBDesconto.setMinimumSize(tamanhoBotaoDesconto);
        jTBDesconto.setMaximumSize(tamanhoBotaoDesconto);

        this.handle_at = handle_at;
        this.horarioLivreOuOcupado = horarioLivreOuOcupado;

        jTFHANDLE_PACIENTE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFHANDLE_MEDICO_SOL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfDuracaoAtendimento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        tirandoBarraDeTitulo();

        // jtable
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        for (int i = 6; i < 22; i++) {
            jTable1.getColumnModel().getColumn(i).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(i).setMinWidth(0);
            jTable1.getTableHeader().getColumnModel().getColumn(i).setMaxWidth(0);
            jTable1.getTableHeader().getColumnModel().getColumn(i).setMinWidth(0);
        }

        jTable1.getColumnModel().getColumn(2).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(65);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(55);

        // preencher convenios
        preenchendoOsConvênios();
        preenchendoAreasDeAtendimento();

        if ("livre".equals(horarioLivreOuOcupado)) {
            jBAtualizar.setVisible(false);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); 
            Date date = new Date(); 
            jTFhoraAtendimento.setText(dateFormat.format(date));

            con = Conexao.fazConexao();
            reservandoHorarioCasoSejaUmHorarioLivre();

            // caso venha de um agendamento, ele preenche os campos com as informações do agendamento
            if(veioDeAgendamento){
                preenchendoOsDadosApartirDoAgendamento();  
            }
            

            // bloquando o menu (pq fizemos uma reserva nos agendamentos e soh vai deletar a reservar ao salvar o
            // agendamento ou cancelar
            janelaPrincipal.internalFrameJanelaPrincipal.desativandoOMenu();

            jBImprimirBoletoDeRetirada.setVisible(false);
            jBImprimirFicha.setVisible(false);
            jBImprimirNotaFiscal.setVisible(false);

            
        } /*else if ("ocupado".equals(horarioLivreOuOcupado)) {
            jBSalvar.setVisible(false);
            // preenchendo os campos daquele convenio
            con = Conexao.fazConexao();
            preenchendoOsCamposDoAtendimentoCasoForEditarUmAtendimento();

            // aqui vamos sumir o botao imprimir nota fiscal do modelo de impressao 1 (pois nao imprime nota)
            if (janelaPrincipal.modeloDeImpressao == 1 ) {
                jBImprimirNotaFiscal.setVisible(false);
            }
            // agora sumir o botao imprimir boleto de retirada do modelo 2 (sai junto com a ficha)
            if (janelaPrincipal.modeloDeImpressao == 2 || janelaPrincipal.modeloDeImpressao == 3) {
                jBImprimirBoletoDeRetirada.setVisible(false);
            }
            // aqui vamos sumir o botao imprimir nota fiscal e o boleto de retirada que sai junto com a ficha
            if(janelaPrincipal.modeloDeImpressao == 4){
                jBImprimirNotaFiscal.setVisible(false);
                jBImprimirBoletoDeRetirada.setVisible(false);
            }
        }
        */

        // focus no paciente
        jTFPaciente.requestFocusInWindow();

        Conexao.fechaConexao(con);
    }

    // ok

    // essa variavel fica treu quando vem da pesquisa para quando precisarmos saber se veio da pesquisa ou nao
    public static boolean veioDaPesquisa = false;

    public java.util.Date formDataStrgToJava(String data) {
        /*
         * Função de conversão de uma data do tipo STRING dd-MM-yyyy para formato date do java.util
         */
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");// Formato da data que virá do banco
        java.util.Date date = null;
        try {
            date = (java.util.Date) formatter.parse(data);// convertendo o formato para Date
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return date;
    }
    
    // esse metodo contrutor serve para abrir a classe apartir da edição de atendimentos
    public JIFCadastroDeAtendimento(int handle_at, String data, String hora) {
        initComponents();
        con = Conexao.fazConexao();
        veioDaPesquisa = true;
        this.handle_at = handle_at;
        // buscando o handle_agenda do atendimento
        /*
        ResultSet resultSet = ATENDIMENTOS.getConsultarAgendaDeUmAtendimento(con, handle_at);
        try {
            while (resultSet.next()) {
                handle_agenda = resultSet.getInt("handle_agenda");
                //jTFAgenda.setText(resultSet.getString("nome"));

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os dados deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        */

        jXDPEntregaDoExame.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDPEntregaDoExame.setLinkDate(System.currentTimeMillis(), "Hoje");
        JXDPDataAtendimento.setFormats(new String[] { "E dd/MM/yyyy" });
        JXDPDataAtendimento.setLinkDate(System.currentTimeMillis(), "Hoje");
        pegandoDataDoSistema();

        Dimension tamanhoBotaoDesconto = new Dimension(121, 30);
        jTBDesconto.setPreferredSize(tamanhoBotaoDesconto);
        jTBDesconto.setMinimumSize(tamanhoBotaoDesconto);
        jTBDesconto.setMaximumSize(tamanhoBotaoDesconto);

        jTFHANDLE_PACIENTE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFHANDLE_MEDICO_SOL.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfDuracaoAtendimento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        tirandoBarraDeTitulo();

        // jtable
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        for (int i = 6; i < 22; i++) {
            jTable1.getColumnModel().getColumn(i).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(i).setMinWidth(0);
            jTable1.getTableHeader().getColumnModel().getColumn(i).setMaxWidth(0);
            jTable1.getTableHeader().getColumnModel().getColumn(i).setMinWidth(0);
        }

        jTable1.getColumnModel().getColumn(2).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(65);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(55);

        // preencher convenios
        preenchendoOsConvênios();
        preenchendoAreasDeAtendimento();

        jBSalvar.setVisible(false);
        // preenchendo os campos daquele convenio
        con = Conexao.fazConexao();
        preenchendoOsCamposDoAtendimentoCasoForEditarUmAtendimentoQuandoVemDaPesquisaDeAtendimentos(handle_at, hora, data);
        // aqui vamos sumir o botao imprimir nota fiscal do modelo de impressao 1 (pois nao imprime nota)
        if (janelaPrincipal.modeloDeImpressao == 1) {
            jBImprimirNotaFiscal.setVisible(false);
        }
        // agora sumir o botao imprimir boleto de retirada do modelo 2 (sai junto com a ficha)
        if (janelaPrincipal.modeloDeImpressao == 2 || janelaPrincipal.modeloDeImpressao == 3) {
            jBImprimirBoletoDeRetirada.setVisible(false);
        }
     // aqui vamos sumir o botao imprimir nota fiscal e o boleto de retirada que sai junto com a ficha 
        if(janelaPrincipal.modeloDeImpressao == 4){
            jBImprimirNotaFiscal.setVisible(false);
            jBImprimirBoletoDeRetirada.setVisible(false);
        }

        // focus no paciente
        jTFPaciente.requestFocusInWindow();

        Conexao.fechaConexao(con);
    }

    
    public void preenchendoOsDadosApartirDoAgendamento() {
        for (String nomeArea : TratamentoParaRegistrarAtendimentoApartirDeAgendamento.listaIdAreasDeAtendimento) {
            jCBAreasDoAgendamento.addItem(nomeArea);
        }
                
        try {
            // preencher data e hora
            JXDPDataAtendimento.setDate(TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getDIA());            

            // paciente
            jTFPaciente.setText(TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getPACIENTE());

            //preenchendo convenio
            for (int x = 0; x < listaHandleConvenio.size(); x++) {
                if (listaHandleConvenio.get(x) == TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getHANDLE_CONVENIO()) {
                    jCBConvenio.setSelectedIndex(x);
                }
            }
                
            //preenchendo area de atendimento
            for (int x = 0; x < listaAreasDeAtendimento.size(); x++) {
                if (listaAreasDeAtendimento.get(x).getId_areas_atendimento() == TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getListaExames().get(0).getID_AREAS_ATENDIMENTO()) {
                    jCBAreaDeAtendimento.setSelectedIndex(x);
                    jCBAreasDoAgendamento.setSelectedItem(jCBAreaDeAtendimento.getSelectedItem());
                }
            }
            /*
             * 
             * nao precisao mais disso pois acima quando mechemeos na area do agendamento ele ja preenche os exames!
             * 
            // exames
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            ArrayList<NagendamentosExames> listaDeNovosExames = TratamentoParaRegistrarAtendimentoApartirDeAgendamento.montaListaDeExamesDeUmaArea(listaAreasDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()).getId_areas_atendimento());
            jTFhoraAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(listaDeNovosExames.get(0).getHORA()));
            for (NagendamentosExames exame : listaDeNovosExames) {
                // calculando valor do exame
                CalculoValorDeExame calculoValorExame = new CalculoValorDeExame(TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getHANDLE_CONVENIO(),
                    exame.getHANDLE_EXAME(), TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getDIA(), false, porcentagemDeDesconto);
                
                // adicionando na tabela o exame com o valor calculo de acordo com a data!!!
                modelo.addRow(new Object[] { exame.getHANDLE_EXAME(),
                    exame.getNomeExame(),
                    MetodosUteis.transformarMinutosEmHorario(exame.getDURACAO()),
                    MetodosUteis.colocarZeroEmCampoReais(calculoValorExame.valor_correto_exame), "", "",
                    calculoValorExame.chConvenio, calculoValorExame.filmeConvenio, calculoValorExame.ch1Exame,
                    calculoValorExame.ch2Exame, calculoValorExame.filmeExame, "", calculoValorExame.redutor,
                    calculoValorExame.porcentDescontoPaciente, calculoValorExame.porcentConvenio,
                    calculoValorExame.porcentPaciente, calculoValorExame.valorExame,
                    calculoValorExame.valorConvenio, calculoValorExame.valorPaciente,
                    calculoValorExame.valor_correto_convenio, calculoValorExame.valor_correto_paciente,
                    calculoValorExame.valor_desconto });
                
                duracaoDoAtendimento += exame.getDURACAO();
            }
            jtfDuracaoAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(duracaoDoAtendimento));
            calcularValoresApartirDaTabela();
            */
            
        } catch (Exception e) {
            this.dispose();
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os dados do Agendamento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        
    }
    

    public boolean reservandoHorarioCasoSejaUmHorarioLivre() {
        pegandoUmHandle_atDoBanco();

        Atendimentos atendimentoMODEL = new Atendimentos();

        atendimentoMODEL.setUSUARIOID(USUARIOS.usrId);
        atendimentoMODEL.setDAT(dataDeHojeEmVariavelDate);

        atendimentoMODEL.setHANDLE_PACIENTE(0);
        atendimentoMODEL.setHANDLE_MEDICO_SOL(0);
        atendimentoMODEL.setHANDLE_CONVENIO(0);
        //atendimentoMODEL.setHORA_ATENDIMENTO(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(JIFUmaAgenda.jTable1.getValueAt(tabelaSelecionada.getSelectedRow(), 0))));
        atendimentoMODEL.setHANDLE_AT(handle_at);
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date diaDoAtendimento = null;
            //diaDoAtendimento = new java.sql.Date(format.parse(jTFDia.getText()).getTime());
            atendimentoMODEL.setDATA_ATENDIMENTO(diaDoAtendimento);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
        }
        con = Conexao.fazConexao();
        boolean cadastro = ATENDIMENTOS.setCadastrar(con, atendimentoMODEL);
        Conexao.fechaConexao(con);

        if (!cadastro) {
            JOptionPane.showMessageDialog(null,
                "Erro ao reservar Horário para o Atendimento. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }else{
            return true;
        }

    }

    private void preenchendoAreasDeAtendimento(){
        listaAreasDeAtendimento = AREAS_ATENDIMENTO.getConsultar();
        for (int i = 0; i < listaAreasDeAtendimento.size() ; i++) {
            jCBAreaDeAtendimento.addItem(listaAreasDeAtendimento.get(i).getNome());
        }
    }
    
    // ok
    public void pegandoUmHandle_atDoBanco() {
        // pegando o handle_ap para salvar no banco
        try {
            con = Conexao.fazConexao();
            handle_at = ATENDIMENTOS.getHandleAP(con);
            con = Conexao.fazConexao();
            ATENDIMENTOS.setSomarHandleAP(con);
            Conexao.fechaConexao(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao somar handle_at. Procure o Administrador." + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } 

    }
/*
    public void preenchendoOsCamposDoAtendimentoCasoForEditarUmAtendimento() {
        // buscando as informações do atendimento

        int handle_atendimento =
            Integer.valueOf(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 5)));
        ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_atendimento);
        try {
            while (resultSet.next()) {
                jTFPaciente.setText(resultSet.getString("nomepac"));
                jTFHANDLE_PACIENTE.setText(resultSet.getString("handle_paciente"));
                handle_paciente = resultSet.getInt("handle_paciente");

                jTFMedicoSol.setText(resultSet.getString("nomemed"));
                jTFHANDLE_MEDICO_SOL.setText(resultSet.getString("handle_medico_sol"));
                handle_medico_sol = resultSet.getInt("handle_medico_sol");

                jXDatePicker1.setDate(resultSet.getDate("data_exame_pronto"));
                jtfHoraEntregaExame.setText(MetodosUteis.transformarMinutosEmHorario(resultSet
                    .getInt("hora_exame_pronto")));
                jTAObservacao.setText(resultSet.getString("observacao"));

                jTFMatricula.setText(resultSet.getString("matricula_convenio"));
                jTFComplemento.setText(resultSet.getString("complemento"));

                for (int x = 0; x < listaHandleConvenio.size(); x++) {
                    if (listaHandleConvenio.get(x) == resultSet.getInt("handle_convenio")) {
                        jCBConvenio.setSelectedIndex(x);
                        resultSet.getInt("handle_convenio");
                        //jCBModalidade.setSelectedItem(resultSet.getString("modalidade"));
                    }
                }

                if ("S".equals(resultSet.getString("flag_imprimiu")) || resultSet.getInt("flag_laudo") == 1) {
                    bloquearExames();
                }

                jtfDuracaoAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(resultSet
                    .getInt("duracao_atendimento")));
                duracaoDoAtendimento = resultSet.getInt("duracao_atendimento");

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os dados deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        jTFAgenda.setText(JIFUmaAgenda.jTextField1.getText());
        jTFDia.setText(String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue())
            .substring(4, 14));
        jTFHora.setText((String) JIFUmaAgenda.jTable1.getValueAt(tabelaSelecionada.getSelectedRow(), 0));

        // preenchendo os exames do atendimento
        // exames

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSetExames = ATENDIMENTO_EXAMES.getConsultarExamesDeUmAtendimento(con, handle_atendimento);
        try {
            while (resultSetExames.next()) {
                modelo.addRow(new String[] { Integer.toString(resultSetExames.getInt("handle_exame")),
                    resultSetExames.getString("nomeExame"),
                    MetodosUteis.transformarMinutosEmHorario(resultSetExames.getInt("duracao")),
                    resultSetExames.getString("valor_correto_exame"), resultSetExames.getString("lado"),
                    resultSetExames.getString("material"), resultSetExames.getString("ch_convenio"),
                    resultSetExames.getString("filme_convenio"), resultSetExames.getString("ch1_exame"),
                    resultSetExames.getString("ch2_exame"), resultSetExames.getString("filme_exame"),
                    resultSetExames.getString("lista_materiais"), resultSetExames.getString("redutor"),
                    resultSetExames.getString("desconto_paciente"), resultSetExames.getString("porcentagem_convenio"),
                    resultSetExames.getString("porcentagem_paciente"), resultSetExames.getString("valor_exame"),
                    resultSetExames.getString("valor_convenio"), resultSetExames.getString("valor_paciente"),
                    resultSetExames.getString("valor_correto_convenio"),
                    resultSetExames.getString("valor_correto_paciente"), resultSetExames.getString("valor_desconto"),
                    String.valueOf(resultSetExames.getInt("atendimento_exame_id")) });

                try {
                    double porcentagem_desconto = Double.valueOf(resultSetExames.getString("desconto_paciente"));
                    if (porcentagem_desconto > 0) {
                        jTBDesconto.setSelected(true);
                        jTBDesconto.setText(porcentagem_desconto + "% de Desconto");
                        porcentagemDeDesconto = porcentagem_desconto;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os Exames deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // fechando a conexao

        calcularValoresApartirDaTabela();
    }
    */
    /*
     * Este metodo bloqueia as opções de edição de um exame, isso ocorre quando o atendente ja imprimiu algo ou algum
     * dos exames ja possui laudo.
     */
    private void bloquearExames() {
        jCBAreaDeAtendimento.setEnabled(false);
        jCBConvenio.setEnabled(false);
        jCBExame.setEnabled(false);
        jBIncluirExame.setEnabled(false);
        jTable1.setEnabled(false);
        jTBDesconto.setEnabled(false);
    }

    /*
     * este metodo é chamado quando vamos editar um atendimento apartir da janela de pesquisa de atendimentos, e nao da
     * agenda (por isos possue algumas alterações)
     */
    // variavel para que se flag imprimiu vem marcado, quando salvar salva S
    boolean flag_imprimu = false;

    
    public void preenchendoOsCamposDoAtendimentoCasoForEditarUmAtendimentoQuandoVemDaPesquisaDeAtendimentos(
        int handle_at, String hora, String data) {
        // buscando as informações do atendimento

        int handle_atendimento = handle_at;
        ResultSet resultSet = ATENDIMENTOS.getConsultarDadosDeUmAtendimento(con, handle_atendimento);
        try {
            while (resultSet.next()) {
                jTFPaciente.setText(resultSet.getString("nomepac"));
                jTFHANDLE_PACIENTE.setText(resultSet.getString("handle_paciente"));
                handle_paciente = resultSet.getInt("handle_paciente");
                
                if(resultSet.getInt("paciente_pagou") == 0){
                	jCBPacientePagou.setSelected(false);
                }else{
                	jCBPacientePagou.setSelected(true);
                }

                jTFMedicoSol.setText(resultSet.getString("nomemed"));
                jTFHANDLE_MEDICO_SOL.setText(resultSet.getString("handle_medico_sol"));
                handle_medico_sol = resultSet.getInt("handle_medico_sol");

                jXDPEntregaDoExame.setDateInMillis(resultSet.getDate("data_exame_pronto").getTime());
                jtfHoraEntregaExame.setText(MetodosUteis.transformarMinutosEmHorario(resultSet
                    .getInt("hora_exame_pronto")));
                jTAObservacao.setText(resultSet.getString("observacao"));

                jTFMatricula.setText(resultSet.getString("matricula_convenio"));
                jTFComplemento.setText(resultSet.getString("complemento"));

                for (int x = 0; x < listaHandleConvenio.size(); x++) {
                    if (listaHandleConvenio.get(x) == resultSet.getInt("handle_convenio")) {
                        jCBConvenio.setSelectedIndex(x);
                        resultSet.getInt("handle_convenio");
                        //jCBModalidade.setSelectedItem(resultSet.getString("modalidade"));
                    }
                }

                if ("S".equals(resultSet.getString("flag_imprimiu"))) {
                    flag_imprimu = true;
                }

                if (resultSet.getInt("flag_laudo") == 1) {
                    bloquearExames();
                }

                jtfDuracaoAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(resultSet
                    .getInt("duracao_atendimento")));
                duracaoDoAtendimento = resultSet.getInt("duracao_atendimento");

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os dados deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // fechando a conexao
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");  
        Date date = null;
        try {
           date = (Date)formatter.parse(data);
        } catch (ParseException e1) {
        } 
       
        JXDPDataAtendimento.setDate(date);
        jTFhoraAtendimento.setText(hora);

        // preenchendo os exames do atendimento
        // exames
        con = Conexao.fazConexao();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        ResultSet resultSetExames = ATENDIMENTO_EXAMES.getConsultarExamesDeUmAtendimento(con, handle_atendimento);
        try {
            while (resultSetExames.next()) {
                modelo.addRow(new String[] { Integer.toString(resultSetExames.getInt("handle_exame")),
                    resultSetExames.getString("nomeExame"),
                    MetodosUteis.transformarMinutosEmHorario(resultSetExames.getInt("duracao")),
                    resultSetExames.getString("valor_correto_exame"), resultSetExames.getString("lado"),
                    resultSetExames.getString("material"), resultSetExames.getString("ch_convenio"),
                    resultSetExames.getString("filme_convenio"), resultSetExames.getString("ch1_exame"),
                    resultSetExames.getString("ch2_exame"), resultSetExames.getString("filme_exame"),
                    resultSetExames.getString("lista_materiais"), resultSetExames.getString("redutor"),
                    resultSetExames.getString("desconto_paciente"), resultSetExames.getString("porcentagem_convenio"),
                    resultSetExames.getString("porcentagem_paciente"), resultSetExames.getString("valor_exame"),
                    resultSetExames.getString("valor_convenio"), resultSetExames.getString("valor_paciente"),
                    resultSetExames.getString("valor_correto_convenio"),
                    resultSetExames.getString("valor_correto_paciente"), resultSetExames.getString("valor_desconto") });

                try {
                    double porcentagem_desconto = Double.valueOf(resultSetExames.getString("desconto_paciente"));
                    if (porcentagem_desconto > 0) {
                        jTBDesconto.setSelected(true);
                        jTBDesconto.setText(porcentagem_desconto + "% de Desconto");
                        porcentagemDeDesconto = porcentagem_desconto;
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os Exames deste Atendimento. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // fechando a conexao

        calcularValoresApartirDaTabela();
    }
    

    // ok
    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    // ok
    @SuppressWarnings("unchecked")
    public void preenchendoOsConvênios() {
        // preenchendo as Classes de Exames

        ResultSet resultSet = CONVENIO.getConsultar(con);
        listaHandleConvenio.removeAll(listaHandleConvenio);
        listaPorcentagemConvenio.removeAll(listaPorcentagemConvenio);
        listaPorcentagemPaciente.removeAll(listaPorcentagemPaciente);

        jCBConvenio.addItem("Selecione um Convênio");
        listaHandleConvenio.add(0);
        listaPorcentagemConvenio.add(0.0);
        listaPorcentagemPaciente.add(0.0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("handle_convenio") > 0) {
                    jCBConvenio.addItem(resultSet.getString("nome"));
                    int handle_convenio = resultSet.getInt("handle_convenio");
                    listaHandleConvenio.add(handle_convenio);

                    double porcentagemPaciente = Double.valueOf(resultSet.getString("porcentPaciente"));
                    double porcentagemConvenio = Double.valueOf(resultSet.getString("porcentConvenio"));

                    listaPorcentagemConvenio.add(porcentagemConvenio);
                    listaPorcentagemPaciente.add(porcentagemPaciente);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Convênios. Procure o administrador." + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // ok
    public void botaoPesquisarPaciente() {

        if (jTFPaciente.getText().length() >= 3) {

            janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente = new JIFAtendimentoSelecionarUmPaciente(jTFPaciente.getText().toUpperCase());
            //if (veioDaPesquisa) {
                janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente);

                int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
                int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.getHeight();

                janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.setLocation(lDesk / 2 - lIFrame / 2, aDesk
                    / 2 - aIFrame / 2);
                janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.setVisible(true);
            /*} else {
                JIFAgendaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente);

                int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
                int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.getHeight();

                janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.setLocation(lDesk / 2 - lIFrame / 2, aDesk
                    / 2 - aIFrame / 2);
                janelaPrincipal.internalFrameAtendimentoSelecionarUmPaciente.setVisible(true);
            }*/

            this.setVisible(false);
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }

    }

    public void botaoPesquisarMedicoSolicitante() {

        if (jTFMedicoSol.getText().length() >= 3) {
            janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante = new JIFAtendimentoSelecionarUmMedicoSolicitante(jTFMedicoSol.getText());
            //if (veioDaPesquisa) {
                janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante);
                janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(true);

                int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
                int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.getHeight();

                janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setLocation(lDesk / 2 - lIFrame
                    / 2, aDesk / 2 - aIFrame / 2);
            /*} else {
                JIFAgendaPrincipal.jDesktopPane1
                    .add(janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante);
                janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setVisible(true);

                int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
                int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.getHeight();

                janelaPrincipal.internalFrameAtendimentoSelecionarUmMedicoSolicitante.setLocation(lDesk / 2 - lIFrame
                    / 2, aDesk / 2 - aIFrame / 2);
            }*/

            this.setVisible(false);
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }

    }

    // ok
    public boolean verificarSeFoiTudoPreenchido() {

        boolean exameOK;
        if (jTable1.getRowCount() > 0) {
            exameOK = true;
        } else {
            exameOK = false;
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Inclua no mínimo um Exame");
        }

        boolean convenioOK;
        if (jCBConvenio.getSelectedIndex() > 0) {
            convenioOK = true;
        } else {
            convenioOK = false;
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione o Convênio");
        }

        boolean handle_pacienteOk = false;
        if (jTFHANDLE_PACIENTE.getText().length() > 0) {
            handle_pacienteOk = true;
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione o Paciente");
        }

        boolean handle_medico_solOk = false;
        if (jTFHANDLE_MEDICO_SOL.getText().length() > 0) {
            handle_medico_solOk = true;
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione o Médico Solicitante");
        }

        /*
        boolean matriculaOk = false;
        if (jTFMatricula.getText().length() > 0) {
            matriculaOk = true;
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Preencha a Matrícula");
        }
        */

        boolean hora_exame_prontoOk = false;

        if ("  :  ".equals(jtfHoraEntregaExame.getText())) {
            hora_exame_prontoOk = true;
        } else {
            hora_exame_prontoOk = MetodosUteis.verificarSeHoraEstaCorreta(jtfHoraEntregaExame.getText());
            if (hora_exame_prontoOk == false) {
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Hora de entrega do Exame Inválida");
            }
        }

        if (handle_medico_solOk && handle_pacienteOk && convenioOK && exameOK && hora_exame_prontoOk) {
            return true;
        } else {
            return false;
        }
    }

    // metodo que verifica se a matricula é valida ou nao de acordo com o modelo de verificação
    private boolean verificarSeMatriculaEValida() {
        int modeloDeValidacaoMatricula = 0;

        // buscando o modelo de verificaçao da matricula
        con = Conexao.fazConexao();
        ResultSet resultSet =
            ATENDIMENTOS.getConsultarModeloDeValidacaoMatriculaDoConvenio(con,
                listaHandleConvenio.get(jCBConvenio.getSelectedIndex()));
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                modeloDeValidacaoMatricula = resultSet.getInt("validacao_matricula");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível verificar o Modelo de Validação da Matrícula. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
            return false;
        }
        Conexao.fechaConexao(con);

        // aqui vamos verificar se a matricula é valida
        if (modeloDeValidacaoMatricula == 0) {
            return true;
        } else if (modeloDeValidacaoMatricula == 1) {
            // 1 é verificação do tp ipê
            boolean matriculaValida = VerificacaoDeMatricula.verificarModelo1(jTFMatricula.getText());
            if (!matriculaValida) {
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario
                    .setText("Matrícula Inválida, verifique se a matrícula foi digitada corretamente.");
            }
            return matriculaValida;
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Modelo de Validação da Matrícula não é válido. Procure o Administrador.");
            return false;
        }
    }

    // ok
    public void botaoSalvar() {
        boolean cadastro = false;
        if ( verificarSeFoiTudoPreenchido() && verificarSeMatriculaEValida() && verificaMedicoComAlerta()) {

            // se pegou o valor do handle_ap ele cadastra
            if (handle_at > 0) {
                con = Conexao.fazConexao();
                deletarExamesDeUmAtendimento();
                // cadastrando atendimento na tabela atendimentos
                Atendimentos atendimento = new Atendimentos();
                atendimento.setUSUARIOID(USUARIOS.usrId);
                atendimento.setDAT(dataDeHojeEmVariavelDate);
                atendimento.setHANDLE_AT(handle_at);
                
                if(jCBPacientePagou.isSelected()){
                	atendimento.setPaciente_pagou(1);
                }else{
                	atendimento.setPaciente_pagou(0);
                }

                
                Date dataSelecionada = JXDPDataAtendimento.getDate();
                // criando um formato de data
                SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
                // colocando data selecionado no formato criado acima
                String data = dataFormatada.format(dataSelecionada);
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    java.sql.Date diaAtendimento = null;
                    diaAtendimento = new java.sql.Date(format.parse(data).getTime());
                    atendimento.setDATA_ATENDIMENTO(diaAtendimento);
                } catch (ParseException ex) {
                }
                atendimento.setHORA_ATENDIMENTO(MetodosUteis.transformarHorarioEmMinutos(jTFhoraAtendimento.getText()));
                atendimento.setHANDLE_PACIENTE(handle_paciente);
                atendimento.setHANDLE_MEDICO_SOL(handle_medico_sol);
                atendimento.setHANDLE_CONVENIO(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()));

                atendimento.setOBSERVACAO(jTAObservacao.getText());

                atendimento.setDURACAO_ATENDIMENTO(duracaoDoAtendimento);
                atendimento.setMATRICULA_CONVENIO(jTFMatricula.getText());
                atendimento.setCOMPLEMENTO(jTFComplemento.getText());

                Date dataSelecionada2 = jXDPEntregaDoExame.getDate();
                // criando um formato de data
                SimpleDateFormat dataFormatada2 = new SimpleDateFormat("dd/MM/yyyy");
                // colocando data selecionado no formato criado acima
                String data2 = dataFormatada2.format(dataSelecionada2);
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    java.sql.Date diaExamePronto = null;
                    diaExamePronto = new java.sql.Date(format.parse(data2).getTime());
                    atendimento.setDATA_EXAME_PRONTO(diaExamePronto);
                } catch (ParseException ex) {
                }

                int hora_exame_pronto = 0;
                // setando a hora de entrega do exame
                if (!"  :  ".equals(jtfHoraEntregaExame.getText())) {
                    hora_exame_pronto = MetodosUteis.transformarHorarioEmMinutos(jtfHoraEntregaExame.getText());
                }
                atendimento.setHORA_EXAME_PRONTO(hora_exame_pronto);
                atendimento.setID_AREAS_ATENDIMENTO(listaAreasDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()).getId_areas_atendimento());
                //atendimento.setMODALIDADE(String.valueOf(jCBModalidade.getSelectedItem()));
                
                if(veioDeAgendamento){
                	atendimento.setId_agendamento(TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getNAGENID());
                }else{
                	atendimento.setId_agendamento(0);
                }
                con = Conexao.fazConexao();
                if(jBSalvar.isVisible()){
                	cadastro = ATENDIMENTOS.setUpdate(con, atendimento);
                }else{
                	cadastro = ATENDIMENTOS.setUpdateQuandoForAtualizar(con, atendimento);
                }
                

                if (cadastro) {
                    if(veioDeAgendamento){
                        NAGENDAMENTOS.atualizarAgendamentoAposVirarAtendimento(con, TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento, jTFPaciente.getText());
                    }
 
                    // cadastrando os exames na tabela atendimento_exames
                    for (int i = 0; i < jTable1.getRowCount(); i++) {
                        Atendimento_Exames atendimentoExame = new Atendimento_Exames();
                        atendimentoExame.setHANDLE_AT(handle_at);
                        atendimentoExame.setHANDLE_EXAME(Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 0))));
                        atendimentoExame.setDURACAO(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(jTable1
                            .getValueAt(i, 2))));
                        atendimentoExame.setLADO(String.valueOf(jTable1.getValueAt(i, 4)));
                        atendimentoExame.setMATERIAL(String.valueOf(jTable1.getValueAt(i, 5)));
                        atendimentoExame.setVALOR_EXAME(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 16))));
                        atendimentoExame.setCH_CONVENIO(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 6))));
                        atendimentoExame.setFILME_CONVENIO(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 7))));
                        atendimentoExame.setCH1_EXAME(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 8))));
                        atendimentoExame.setCH2_EXAME(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 9))));
                        atendimentoExame.setFILME_EXAME(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 10))));
                        atendimentoExame.setLISTA_MATERIAIS(String.valueOf(jTable1.getValueAt(i, 11)));
                        atendimentoExame.setREDUTOR(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 12))));
                        atendimentoExame
                            .setDESCONTO_PACIENTE(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 13))));
                        atendimentoExame.setPORCENTAGEM_CONVENIO(Double.valueOf(String.valueOf(jTable1
                            .getValueAt(i, 14))));
                        atendimentoExame.setPORCENTAGEM_PACIENTE(Double.valueOf(String.valueOf(jTable1
                            .getValueAt(i, 15))));
                        atendimentoExame
                            .setVALOR_CORRETO_EXAME(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 3))));
                        atendimentoExame.setVALOR_CONVENIO(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 17))));
                        atendimentoExame.setVALOR_PACIENTE(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 18))));
                        atendimentoExame.setVALOR_CORRETO_CONVENIO(Double.valueOf(String.valueOf(jTable1.getValueAt(i,
                            19))));
                        atendimentoExame.setVALOR_CORRETO_PACIENTE(Double.valueOf(String.valueOf(jTable1.getValueAt(i,
                            20))));
                        atendimentoExame.setVALOR_DESCONTO(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 21))));

                        atendimentoExame.setNUMERO_SEQUENCIA(String.valueOf(i + 1));
                        if (atendimentoExame.getNUMERO_SEQUENCIA().length() < 2) {
                            atendimentoExame.setNUMERO_SEQUENCIA("0" + atendimentoExame.getNUMERO_SEQUENCIA());
                        }
                        con = Conexao.fazConexao();
                        cadastro = ATENDIMENTO_EXAMES.setCadastrar(con, atendimentoExame);
                    }
                }

                // fechando a conexao
                Conexao.fechaConexao(con);

                if (cadastro) {
                    janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
                    jBSalvar.setVisible(false);
                    jBAtualizar.setVisible(false);

                    // ativando botoes de impressao
                    // aqui vamos sumir o botao imprimir nota fiscal do modelo de impressao 1 (pois nao imprime nota)
                    if (janelaPrincipal.modeloDeImpressao == 1) {
                        jBImprimirFicha.setVisible(true);
                        jBImprimirBoletoDeRetirada.setVisible(true);
                    }
                    // agora sumir o botao imprimir boleto de retirada do modelo 2 (sai junto com a ficha)
                    if (janelaPrincipal.modeloDeImpressao == 2 || janelaPrincipal.modeloDeImpressao == 3) {
                        jBImprimirFicha.setVisible(true);
                        if(jCBPacientePagou.isSelected()){
                        	jBImprimirNotaFiscal.setVisible(true);
                        }
                        
                    }
                    // aqui vamos sumir o botao imprimir nota fiscal e o boleto de retirada que sai junto com a ficha
                    if(janelaPrincipal.modeloDeImpressao == 4){
                        jBImprimirFicha.setVisible(true);
                    }
                    
                    if(veioDeAgendamento){
                        ((DefaultComboBoxModel<String>) jCBAreasDoAgendamento.getModel()).removeElementAt(jCBAreasDoAgendamento.getSelectedIndex());
                        jCBAreasDoAgendamento.setSelectedIndex(0);
                    }

                    // desabilitando os campos do atendimento para nao poder editar
                    jTFPaciente.setEnabled(false);
                    jTFMedicoSol.setEnabled(false);
                    jBPesquisaMedico.setEnabled(false);
                    jBPesquisaPaciente.setEnabled(false);

                    jXDPEntregaDoExame.setEnabled(false);
                    jtfHoraEntregaExame.setEnabled(false);
                    jTAObservacao.setEnabled(false);

                    jCBAreaDeAtendimento.setEnabled(false);
                    jCBConvenio.setEnabled(false);
                    jCBExame.setEnabled(false);
                    jTFMatricula.setEnabled(false);
                    jTFComplemento.setEnabled(false);
                    jBIncluirExame.setEnabled(false);
                    jTable1.setEnabled(false);
                    jTBDesconto.setEnabled(false);
                    JXDPDataAtendimento.setEnabled(false);
                    jTFhoraAtendimento.setEnabled(false);
                    jCBPacientePagou.setEnabled(false);
                    cadastrouNovoAtendimento = true;
                    
                    
                }
            } else {
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                    "Erro ao cadastrar atendimento.Procure o Administrador.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    boolean cadastrouNovoAtendimento = false;

    // meodo utilizado no evento do botao salvar!!!!!

    // ok
    /*
    public boolean verificarSeHaTempoParaRealizarOAtendimento() {
        try {
            // pegando duração do exame
            int duracaodoAtendimento = MetodosUteis.transformarHorarioEmMinutos(jtfDuracaoAtendimento.getText());
            // pegando duração da agenda
            int minutosLinha0 =
                MetodosUteis.transformarHorarioEmMinutos((String) JIFUmaAgenda.jTable1.getValueAt(0, 0));
            int minutosLinha1 =
                MetodosUteis.transformarHorarioEmMinutos(String.valueOf(JIFUmaAgenda.jTable1.getValueAt(1, 0)));

            int duracaoAgenda = minutosLinha1 - minutosLinha0;

            // verificando quantos horarios ira ocupadar da agenda, para verifica se eles existem
            // se nao existirem linhas (horarios) suficientes ou o flag status for diferente de nulo ou "" ou a pintura
            // for de umintervalo qualuqer
            int qtdDeHorariosQueOcuparaDaAgenda = (duracaodoAtendimento / duracaoAgenda) - 1;

            if (qtdDeHorariosQueOcuparaDaAgenda >= 0 && (duracaodoAtendimento % duracaoAgenda > 0)) {
                qtdDeHorariosQueOcuparaDaAgenda++;
            }

            // se nao for utiliza mais de uma linha da agenda
            // para aqui e rotna que esta ok (string vazia)
            if (qtdDeHorariosQueOcuparaDaAgenda == 0 || qtdDeHorariosQueOcuparaDaAgenda == -1) {
                return true;
            }

            // se for utilizar mais de uma linha da agenda
            // faz verificação para ver se nao ta pintado e senao temum agendamento ou atendimento
            // se tiver algum retorna erro
            int linhaSelecionada = tabelaSelecionada.getSelectedRow();
            int j = 1;
            for (int i = linhaSelecionada; i < linhaSelecionada + qtdDeHorariosQueOcuparaDaAgenda; i++) {

                // verificando se nao eh nulo ou vazio o campo nome
                // se for retorna q nao ha tempo para o exame
                if (!"null".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow() + j,
                    0)))
                    && !""
                        .equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow() + j, 0)))
                    && !"*".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow() + j,
                        0)))) {
                    return false;
                }

                // verificando se nao proximas linhas o flag de pintura eh diferente de nulo ou ""
                // se for diferente retona que nao ha espaço para o agendamento
                // se NAO for diferente retorna q nao ha tempo para o exame
                if (("1"
                    .equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow() + j, 3))) || "2"
                    .equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow() + j, 3))))) {
                    return false;
                }

                j++;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
    */

    // meodo utilizado no evento do botao salvar!!!!!
    // ok
    /*
    public String verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada() {

        boolean existeAtendimentoNesteHorario = false;
        boolean naoHaTempoParaFazerOAtendimento = false;

        // verifica se tem algum agendamento ali se nao estivermos editando (pq ae obviu que ja tem um ali)
        if ("livre".equals(horarioLivreOuOcupado)) {
            // se linha tiver pintada de azul sabemos que tem um agendamento ali
            if ("3".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 3)))) {
                existeAtendimentoNesteHorario = true;
            }
        }

        // retorno para verificar se ha tempo para fazer o exame
        if (!verificarSeHaTempoParaRealizarOAtendimento()) {
            naoHaTempoParaFazerOAtendimento = true;
        }

        // retornando a string
        if (existeAtendimentoNesteHorario && naoHaTempoParaFazerOAtendimento) {
            return "Já existe um agendamento neste horário e não há tempo suficiente para este atendimento. Deseja Continuar?";
        } else if (existeAtendimentoNesteHorario) {
            return "Já existe um agendamento neste horário. Deseja Continuar?";
        } else if (naoHaTempoParaFazerOAtendimento) {
            return "Não há tempo suficiente para este atendimento. Deseja Continuar?";
        } else {
            return "";
        }

    }
    */

    /*
    public void atualizarTabelasDaAgenda() {
        // atualizando a janela de uma agenda
        Icon iconeAgendado =
            new ImageIcon(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/menuAgendar.png")));
        Icon iconeAgendadoExt =
            new ImageIcon(getToolkit().createImage(
                getClass().getResource("/br/bcn/admclin/imagens/menuAgendarExtendido.png")));
        Icon iconeAtendimento =
            new ImageIcon(getToolkit().createImage(
                getClass().getResource("/br/bcn/admclin/imagens/menuAtendimento.png")));
        Icon iconeAtendmentoExt =
            new ImageIcon(getToolkit().createImage(
                getClass().getResource("/br/bcn/admclin/imagens/menuAtendimentoExtendido.png")));
        JIFUmaAgenda.listaAgendamentosDaAgenda.clear();
        con = Conexao.fazConexao();
        JIFUmaAgenda.preenchendoOsAtendimentosDestaAgenda(JIFUmaAgenda.jTable2, iconeAtendimento, iconeAtendmentoExt,
            con);
        JIFUmaAgenda.preenchendoOsAtendimentosDestaAgenda(JIFUmaAgenda.jTable3, iconeAtendimento, iconeAtendmentoExt,
            con);
        JIFUmaAgenda.preenchendoOsAtendimentosDestaAgenda(JIFUmaAgenda.jTable4, iconeAtendimento, iconeAtendmentoExt,
            con);
        JIFUmaAgenda.preenchendoOsAtendimentosDestaAgenda(JIFUmaAgenda.jTable5, iconeAtendimento, iconeAtendmentoExt,
            con);
        JIFUmaAgenda.preenchendoOsAtendimentosDestaAgenda(JIFUmaAgenda.jTable6, iconeAtendimento, iconeAtendmentoExt,
            con);

        JIFUmaAgenda.preenchendoOsAgendamentosDestaAgenda(JIFUmaAgenda.jTable2, iconeAgendado, iconeAgendadoExt, con);
        JIFUmaAgenda.preenchendoOsAgendamentosDestaAgenda(JIFUmaAgenda.jTable3, iconeAgendado, iconeAgendadoExt, con);
        JIFUmaAgenda.preenchendoOsAgendamentosDestaAgenda(JIFUmaAgenda.jTable4, iconeAgendado, iconeAgendadoExt, con);
        JIFUmaAgenda.preenchendoOsAgendamentosDestaAgenda(JIFUmaAgenda.jTable5, iconeAgendado, iconeAgendadoExt, con);
        JIFUmaAgenda.preenchendoOsAgendamentosDestaAgenda(JIFUmaAgenda.jTable6, iconeAgendado, iconeAgendadoExt, con);

        Conexao.fechaConexao(con);
    }
    */

    public void botaoAtualizar() {
            botaoSalvar();
            botaoCancelar();
    }

    private boolean deletarOAtendimento() {
        con = Conexao.fazConexao();
        boolean deletou = ATENDIMENTOS.setDeletar(con, handle_at);
        Conexao.fechaConexao(con);
        return deletou;
    }

    private boolean deletarExamesDeUmAtendimento() {
        con = Conexao.fazConexao();
        boolean deletou = ATENDIMENTO_EXAMES.setDeletarTodosDeUmAtendimento(con, handle_at);
        Conexao.fechaConexao(con);
        return deletou;
    }

    
    public void botaoCancelar() {

        if (veioDaPesquisa) {
            // fechando esta janela
            janelaPrincipal.internalFramePesquisarAtendimentos.setVisible(true);
            this.dispose();
            janelaPrincipal.internalFrameAtendimento = null;
            // abrindo a janela pesquisa
        } else if (veioDeAgendamento){
            // fechando esta janela
            janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
            this.dispose();
            janelaPrincipal.internalFrameAtendimento = null;
            // abrindo a janela pesquisa
            janelaPrincipal.internalFrameListaDeAgendamentos.setVisible(true);
        } else{
            this.dispose();
            janelaPrincipal.internalFrameAtendimento = null;
        }
    }
    

    public static int duracaoDoAtendimento = 0;

    public void botaoIncluirExame() {
        if (jCBExame.isEnabled() && jCBExame.getSelectedIndex() != 0) {

            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

            int handle_exame = listaHandleExames.get(jCBExame.getSelectedIndex());

            String nomeExame = (String) jCBExame.getSelectedItem();

            String duracaoExame =
                MetodosUteis.transformarMinutosEmHorario(listaDuracaoExames.get(jCBExame.getSelectedIndex()));
            duracaoDoAtendimento = duracaoDoAtendimento + listaDuracaoExames.get(jCBExame.getSelectedIndex());
            jtfDuracaoAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(duracaoDoAtendimento));

            // incluindo valor total
            // pegando a data da tabela que foi clicado para pesquisar os valores dos exames com ela
            // String dataString =
            // String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4,14);
            //String dataString = jTFDia.getText();
            //SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            //java.sql.Date dataDoExame = null;
            //try {
            //    dataDoExame = new java.sql.Date(fmt.parse(dataString).getTime());
            //} catch (ParseException ex) {
            //    dataDoExame = dataDeHojeEmVariavelDate;
            //    JOptionPane.showMessageDialog(null,
            //        "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!", "ERRO",
            //        javax.swing.JOptionPane.ERROR_MESSAGE);
            //}
            
            
            Date dataSelecionada = JXDPDataAtendimento.getDate();
            java.sql.Date dataDoExame = null;
            // criando um formato de data
            SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
            // colocando data selecionado no formato criado acima
            String data = dataFormatada.format(dataSelecionada);
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date diaAtendimento = null;
                diaAtendimento = new java.sql.Date(format.parse(data).getTime());
                dataDoExame = diaAtendimento;
            } catch (ParseException ex) {
                dataDoExame = dataDeHojeEmVariavelDate;
                JOptionPane.showMessageDialog(null,
                    "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            
            
            CalculoValorDeExame calculoValorExame =
                new CalculoValorDeExame(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()),
                    listaHandleExames.get(jCBExame.getSelectedIndex()), dataDoExame, false, porcentagemDeDesconto);

            // colocando o valor do exame
            double chConvenio, filmeConvenio;
            double ch1Exame, ch2Exame, filmeExame;

            chConvenio = calculoValorExame.chConvenio;
            filmeConvenio = calculoValorExame.filmeConvenio;

            ch1Exame = calculoValorExame.ch1Exame;
            ch2Exame = calculoValorExame.ch2Exame;
            filmeExame = calculoValorExame.filmeExame;

            modelo.addRow(new Object[] { handle_exame, nomeExame, duracaoExame,
                MetodosUteis.colocarZeroEmCampoReais(calculoValorExame.valor_correto_exame), "", "", chConvenio,
                filmeConvenio, ch1Exame, ch2Exame, filmeExame, "", calculoValorExame.redutor,
                calculoValorExame.porcentDescontoPaciente, calculoValorExame.porcentConvenio,
                calculoValorExame.porcentPaciente, calculoValorExame.valorExame, calculoValorExame.valorConvenio,
                calculoValorExame.valorPaciente, calculoValorExame.valor_correto_convenio,
                calculoValorExame.valor_correto_paciente, calculoValorExame.valor_desconto });
            calcularValoresApartirDaTabela();

            // aqui caso o exame va material por padrao chamamos o metodo, mas antes temos que selecionar a linha
            if (listaVaiMateriaisPorPadrao.get(jCBExame.getSelectedIndex()) == 1) {
                int ultimaLinha = jTable1.getRowCount() - 1;
                jTable1.addRowSelectionInterval(ultimaLinha, ultimaLinha);

                // chamando o metodo para somar os valores dos materiais
                jTable1.setValueAt("CM", jTable1.getSelectedRow(), 5);
                if (jTable1.getValueAt(jTable1.getSelectedRow(), 16) != null
                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != ""
                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != "null") {
                    mudarValorDeExameCasoMudeComMaterialOuComContraste(true);
                }
            }
        }
    }

    // varre a tabela somando os valores e preenche os textfield
    public static void calcularValoresApartirDaTabela() {
        double valorTotalExames = 0, valorConvenio = 0, valorPacienteComDesconto = 0;
        if (jTable1.getRowCount() > 0) {
            duracaoDoAtendimento = 0;
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                // somando o valor total
                valorTotalExames =
                    new BigDecimal(valorTotalExames + Double.valueOf(String.valueOf(jTable1.getValueAt(i, 3))))
                        .setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                // somando o valor convenio
                valorConvenio =
                    new BigDecimal(valorConvenio + Double.valueOf(String.valueOf(jTable1.getValueAt(i, 19)))).setScale(
                        2, RoundingMode.HALF_EVEN).doubleValue();
                // somando o valor paciente com desconto
                valorPacienteComDesconto =
                    new BigDecimal(valorPacienteComDesconto + Double.valueOf(String.valueOf(jTable1.getValueAt(i, 20))))
                        .setScale(2, RoundingMode.HALF_EVEN).doubleValue();

                jTFValorTotal.setText(String.valueOf(valorTotalExames));
                jTFValorConvenio.setText(String.valueOf(valorConvenio));
                jTFValorPaciente.setText(String.valueOf(valorPacienteComDesconto));

                // colocando a duração
                int duracaoDoExameSelecionado =
                    MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(i, 2));
                duracaoDoAtendimento = duracaoDoAtendimento + duracaoDoExameSelecionado;
                jtfDuracaoAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(duracaoDoAtendimento));
            }
        } else {
            jTFValorTotal.setText("");
            jTFValorConvenio.setText("");
            jTFValorPaciente.setText("");
            jtfDuracaoAtendimento.setText("00:00");
            duracaoDoAtendimento = 0;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBCancelar = new javax.swing.JButton();
        jBSalvar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtfDuracaoAtendimento = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTAObservacao = new javax.swing.JTextArea(new DocumentoSemAspasEPorcento(500));
        jLabel6 = new javax.swing.JLabel();
        jXDPEntregaDoExame = new org.jdesktop.swingx.JXDatePicker();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jtfHoraEntregaExame = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##:##"));
        jBAtualizar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jBPesquisaPaciente = new javax.swing.JButton();
        jTFHANDLE_PACIENTE = new javax.swing.JTextField();
        jTFPaciente = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFMedicoSol = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jBPesquisaMedico = new javax.swing.JButton();
        jTFHANDLE_MEDICO_SOL = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jCBConvenio = new javax.swing.JComboBox();
       
        jBIncluirExame = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jCBAreaDeAtendimento = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jTFMatricula = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(30), null, 0);
        jLabel18 = new javax.swing.JLabel();
        jTFComplemento = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(32), null, 0);
        jLabel8 = new javax.swing.JLabel();
        jCBExame = new javax.swing.JComboBox();
        jBImprimirNotaFiscal = new javax.swing.JButton();
        jBImprimirBoletoDeRetirada = new javax.swing.JButton();
        jBImprimirFicha = new javax.swing.JButton();
        jTFValorConvenio = new javax.swing.JTextField();
        jTFValorTotal = new javax.swing.JTextField();
        jTFValorPaciente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTBDesconto = new javax.swing.JToggleButton();

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBCancelarKeyPressed(evt);
            }
        });

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });
        jBSalvar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarFocusLost(evt);
            }
        });
        jBSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBSalvarKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Atendimento",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jtfDuracaoAtendimento.setText("00:00");
        jtfDuracaoAtendimento.setEnabled(false);

        jLabel10.setText("Duração total");

        jLabel11.setText("Observação");

        jTAObservacao.setColumns(20);
        jTAObservacao.setRows(5);
        jScrollPane1.setViewportView(jTAObservacao);

        jLabel6.setText("Entrega do Exame");
        
        JLabel lblDataAtendimento = new JLabel();
        lblDataAtendimento.setText("Data Atendimento");
        
        JXDPDataAtendimento = new JXDatePicker();
        JXDPDataAtendimento.setFormats(new String[] {"E dd/MM/yyyy"});
        
        jTFhoraAtendimento = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##:##"));
        jCBAreasDoAgendamento = new JComboBox();
        jCBAreasDoAgendamento.addActionListener(new ActionListener() {
            boolean primeiraVez = true;
            public void actionPerformed(ActionEvent arg0) {
                
                if(!primeiraVez && jCBAreasDoAgendamento.getSelectedItem() != ""){                    
                    jCBAreaDeAtendimento.setSelectedItem(jCBAreasDoAgendamento.getSelectedItem());
                    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                    ArrayList<NagendamentosExames> listaDeNovosExames = TratamentoParaRegistrarAtendimentoApartirDeAgendamento.montaListaDeExamesDeUmaArea(listaAreasDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()).getId_areas_atendimento());
                    jTFhoraAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(listaDeNovosExames.get(0).getHORA()));
                    for (NagendamentosExames exame : listaDeNovosExames) {
                        // calculando valor do exame
                        CalculoValorDeExame calculoValorExame = new CalculoValorDeExame(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()),
                            exame.getHANDLE_EXAME(), TratamentoParaRegistrarAtendimentoApartirDeAgendamento.agendamento.getDIA(), false, porcentagemDeDesconto);
                        
                        // adicionando na tabela o exame com o valor calculo de acordo com a data!!!
                        modelo.addRow(new Object[] { exame.getHANDLE_EXAME(),
                            exame.getNomeExame(),
                            MetodosUteis.transformarMinutosEmHorario(exame.getDURACAO()),
                            MetodosUteis.colocarZeroEmCampoReais(calculoValorExame.valor_correto_exame), "", "",
                            calculoValorExame.chConvenio, calculoValorExame.filmeConvenio, calculoValorExame.ch1Exame,
                            calculoValorExame.ch2Exame, calculoValorExame.filmeExame, "", calculoValorExame.redutor,
                            calculoValorExame.porcentDescontoPaciente, calculoValorExame.porcentConvenio,
                            calculoValorExame.porcentPaciente, calculoValorExame.valorExame,
                            calculoValorExame.valorConvenio, calculoValorExame.valorPaciente,
                            calculoValorExame.valor_correto_convenio, calculoValorExame.valor_correto_paciente,
                            calculoValorExame.valor_desconto });
                        
                        duracaoDoAtendimento += exame.getDURACAO();
                    }
                    jtfDuracaoAtendimento.setText(MetodosUteis.transformarMinutosEmHorario(duracaoDoAtendimento));
                    
                    calcularValoresApartirDaTabela();
                    
                    
                    
                
                    //vamos deixar a tela normal caso ele mude de area e tenha salvo o atendimento
                    if(!jBSalvar.isVisible()){
                        if(reservandoHorarioCasoSejaUmHorarioLivre()){                            
                            
                          //vamos deixar a janela pronta para registrar um novo atendimento (de outra area no caso)
                            jBSalvar.setVisible(true);

                            // ativando botoes de impressao
                            // aqui vamos sumir o botao imprimir nota fiscal do modelo de impressao 1 (pois nao imprime nota)
                            if (janelaPrincipal.modeloDeImpressao == 1) {
                                jBImprimirFicha.setVisible(false);
                                jBImprimirBoletoDeRetirada.setVisible(false);
                            }
                            // agora sumir o botao imprimir boleto de retirada do modelo 2 (sai junto com a ficha)
                            if (janelaPrincipal.modeloDeImpressao == 2 || janelaPrincipal.modeloDeImpressao == 3) {
                                jBImprimirFicha.setVisible(false);
                                jBImprimirNotaFiscal.setVisible(false);
                            }
                            // aqui vamos sumir o botao imprimir nota fiscal e o boleto de retirada que sai junto com a ficha
                            if(janelaPrincipal.modeloDeImpressao == 4){
                                jBImprimirFicha.setVisible(false);
                            }

                            // desabilitando os campos do atendimento para nao poder editar
                            jTFPaciente.setEnabled(true);
                            jTFMedicoSol.setEnabled(true);
                            jBPesquisaMedico.setEnabled(true);
                            jBPesquisaPaciente.setEnabled(true);

                            jXDPEntregaDoExame.setEnabled(true);
                            jtfHoraEntregaExame.setEnabled(true);
                            jTAObservacao.setEnabled(true);

                            jCBAreaDeAtendimento.setEnabled(true);
                            jCBConvenio.setEnabled(true);
                            jCBExame.setEnabled(true);
                            jTFMatricula.setEnabled(true);
                            jTFComplemento.setEnabled(true);
                            jBIncluirExame.setEnabled(true);
                            jTable1.setEnabled(true);
                            jTBDesconto.setEnabled(true);
                            JXDPDataAtendimento.setEnabled(true);
                            jTFhoraAtendimento.setEnabled(true);
                            janelaPrincipal.internalFrameJanelaPrincipal.desativandoOMenu();
                        }else{
                            janelaPrincipal.internalFrameAtendimento.dispose();
                            janelaPrincipal.internalFrameAtendimento = null;
                        }
                        
                        
                    }
                }else if(jCBAreasDoAgendamento.getSelectedItem() == ""){
                    ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
                    jTable1.updateUI();
                    duracaoDoAtendimento = 0;
                    jtfDuracaoAtendimento.setText("00:00");
                    jTFValorTotal.setText("");
                    jTFValorConvenio.setText("");
                    jTFValorPaciente.setText("");
                }
                primeiraVez = false;
            }
        });
        
        jLAreasDoAgendamento = new JLabel("Áreas do Agendamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(18)
                                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18)
                                    .addComponent(jXDPEntregaDoExame, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addComponent(jtfHoraEntregaExame, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 140, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jLabel10)
                                        .addComponent(lblDataAtendimento, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(jtfDuracaoAtendimento, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(JXDPDataAtendimento, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(ComponentPlacement.RELATED)
                                            .addComponent(jTFhoraAtendimento, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)))))
                            .addContainerGap())
                        .addGroup(Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLAreasDoAgendamento, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jCBAreasDoAgendamento, GroupLayout.PREFERRED_SIZE, 332, GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jXDPEntregaDoExame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfHoraEntregaExame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblDataAtendimento)
                        .addComponent(JXDPDataAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTFhoraAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jtfDuracaoAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(13)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jCBAreasDoAgendamento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLAreasDoAgendamento)))
        );
        jPanel1.setLayout(jPanel1Layout);

        jBAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBAtualizar.setText("Atualizar");
        jBAtualizar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarActionPerformed(evt);
            }
        });
        jBAtualizar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAtualizarFocusLost(evt);
            }
        });
        jBAtualizar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBAtualizarKeyPressed(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Paciente",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jBPesquisaPaciente
            .setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/Lupa.png"))); // NOI18N
        jBPesquisaPaciente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisaPacienteActionPerformed(evt);
            }
        });
        jBPesquisaPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBPesquisaPacienteFocusLost(evt);
            }
        });
        jBPesquisaPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBPesquisaPacienteKeyPressed(evt);
            }
        });

        jTFHANDLE_PACIENTE.setEnabled(false);
        jTFHANDLE_PACIENTE.setFocusable(false);

        jTFPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPacienteFocusLost(evt);
            }
        });
        jTFPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFPacienteKeyPressed(evt);
            }
        });

        jLabel4.setText("Paciente");

        jLabel5.setText("Medico Solicitante");

        jTFMedicoSol.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFMedicoSolFocusLost(evt);
            }
        });
        jTFMedicoSol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFMedicoSolKeyPressed(evt);
            }
        });

        jBPesquisaMedico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/Lupa.png"))); // NOI18N
        jBPesquisaMedico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisaMedicoActionPerformed(evt);
            }
        });
        jBPesquisaMedico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBPesquisaMedicoFocusLost(evt);
            }
        });
        jBPesquisaMedico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBPesquisaMedicoKeyPressed(evt);
            }
        });

        jTFHANDLE_MEDICO_SOL.setEnabled(false);
        jTFHANDLE_MEDICO_SOL.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout
            .setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel2Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel2Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel2Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addGroup(
                                                    jPanel2Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jTFMedicoSol,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFHANDLE_MEDICO_SOL,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 69,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBPesquisaMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(
                                    jPanel2Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addGroup(
                                                    jPanel2Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jTFPaciente,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFHANDLE_PACIENTE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 69,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBPesquisaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                            javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel2Layout
            .setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel2Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel2Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(
                                    jPanel2Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTFPaciente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTFHANDLE_PACIENTE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jBPesquisaPaciente, javax.swing.GroupLayout.DEFAULT_SIZE, 46,
                                    Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel2Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(
                                    jPanel2Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jTFMedicoSol, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTFHANDLE_MEDICO_SOL,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jBPesquisaMedico, javax.swing.GroupLayout.DEFAULT_SIZE, 46,
                                    Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exames do Atendimento",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "HANDLE EXAME", "Exame(s)", "Duração", "Valor", "Lado", "Material", "chConvenio",
            "filmeConvenio", "ch1Exame", "ch2Exame", "filmeExame", "lista de materiais", "redutor",
            "desconto_paciente", "porcentagem_convenio", "porcentagem_paciente", "valor_exame", "valor_convenio",
            "valor_paciente", "valor_correto_convenio", "valor_correto_paciente", "valor_desconto" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel7.setText("Convênio");

        jCBConvenio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBConvenioActionPerformed(evt);
            }
        });

        jBIncluirExame.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/imagemSetaParaBaixo.png"))); // NOI18N
        jBIncluirExame.setText("Incluir Exame");
        jBIncluirExame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBIncluirExameActionPerformed(evt);
            }
        });
        jBIncluirExame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBIncluirExameKeyPressed(evt);
            }
        });

        jLabel16.setText("Área de Atendimento");

        jCBAreaDeAtendimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAreaDeAtendimentoActionPerformed(evt);
            }
        });

        jLabel17.setText("Matrícula");

        jLabel18.setText("Complemento");

        jLabel8.setText("Exame");

        jCBExame.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jLabel18)
                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
                                .addComponent(jTFMatricula, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                .addComponent(jCBConvenio, 0, 349, Short.MAX_VALUE)
                                .addComponent(jTFComplemento, GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel16, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jCBAreaDeAtendimento, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(jCBExame, 0, 382, Short.MAX_VALUE))
                        .addComponent(jBIncluirExame, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
                    .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jTFMatricula, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel18)
                        .addComponent(jTFComplemento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jCBConvenio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jCBAreaDeAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(jLabel8)
                        .addComponent(jCBExame, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jBIncluirExame)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
        );
        jPanel3.setLayout(jPanel3Layout);

        jBImprimirNotaFiscal.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/imprimirNotaFiscal.png"))); // NOI18N
        jBImprimirNotaFiscal.setText("Imprimir Nota Fiscal");
        jBImprimirNotaFiscal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImprimirNotaFiscalActionPerformed(evt);
            }
        });

        jBImprimirBoletoDeRetirada.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/imrpimirBoletoRetirada.png"))); // NOI18N
        jBImprimirBoletoDeRetirada.setText("Imprimir boleto de Retirada");
        jBImprimirBoletoDeRetirada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImprimirBoletoDeRetiradaActionPerformed(evt);
            }
        });
        jBImprimirBoletoDeRetirada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBImprimirBoletoDeRetiradaKeyPressed(evt);
            }
        });

        jBImprimirFicha.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/imprimirFicha.png"))); // NOI18N
        jBImprimirFicha.setText("Imprimir Ficha");
        jBImprimirFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBImprimirFichaActionPerformed(evt);
            }
        });
        jBImprimirFicha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBImprimirFichaKeyPressed(evt);
            }
        });

        jTFValorConvenio.setText("1.000,00");
        jTFValorConvenio.setEnabled(false);

        jTFValorTotal.setText("1.000,00");
        jTFValorTotal.setEnabled(false);

        jTFValorPaciente.setText("1.000,00");
        jTFValorPaciente.setEnabled(false);

        jLabel15.setText("Paciente: R$");

        jLabel19.setText("Convênio: R$");

        jLabel20.setText("Valor Total: R$");

        jTBDesconto.setText("Aplicar Desconto");
        jTBDesconto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTBDescontoActionPerformed(evt);
            }
        });
        
        jCBPacientePagou = new JCheckBox("Pac. Pagou");
        jCBPacientePagou.setSelected(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        				.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(jTBDesconto, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel20)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFValorTotal, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(jLabel15)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFValorPaciente, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel19)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFValorConvenio, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
        					.addGap(1))))
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jBCancelar)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBSalvar)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBAtualizar)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jCBPacientePagou)
        			.addPreferredGap(ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
        			.addComponent(jBImprimirNotaFiscal)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBImprimirBoletoDeRetirada)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBImprimirFicha))
        		.addComponent(jTFMensagemParaUsuario, GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel19)
        						.addComponent(jTFValorConvenio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel20)
        						.addComponent(jTFValorTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel15)
        						.addComponent(jTFValorPaciente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTBDesconto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(1))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(10)
        					.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jTFMensagemParaUsuario, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        					.addComponent(jBImprimirNotaFiscal, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBImprimirBoletoDeRetirada, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBImprimirFicha, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBSalvar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBCancelar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBAtualizar))
        				.addComponent(jCBPacientePagou))
        			.addContainerGap(37, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean getValidacaoIpe(){
    	int modeloDeValidacaoMatricula = 0;

        // buscando o modelo de verificaçao da matricula
        con = Conexao.fazConexao();
        ResultSet resultSet =
            ATENDIMENTOS.getConsultarModeloDeValidacaoMatriculaDoConvenio(con,
                listaHandleConvenio.get(jCBConvenio.getSelectedIndex()));
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                modeloDeValidacaoMatricula = resultSet.getInt("validacao_matricula");
                if(modeloDeValidacaoMatricula == 1){
                	Conexao.fechaConexao(con);
                	return true;
                }
            }
            Conexao.fechaConexao(con);
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível verificar o Modelo de Validação da Matrícula. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            Conexao.fechaConexao(con);
            return false;
        }
        
    }
    
    private boolean verificaMedicoComAlerta(){
    	//se ja selecionou o medico verifica, se nao return true
    	if(jTFHANDLE_MEDICO_SOL.getText().length() > 0){
    		if (getValidacaoIpe()) {
                
                if(MEDICOS.getConsultarSeMedicoPertenceAoIpe(jTFMedicoSol.getText())){
                	return true;
                }else{
                	int resposta = JOptionPane.showConfirmDialog(null, "Médico selecionado não pertence ao Convênio Ipê. Deseja Continuar?");
                	if(resposta == JOptionPane.YES_OPTION){
                		return true;
                	}else{
                		return false;
                	}
                }
                
            } else{
            	return true;
            }
    	}else{
    		return true;
    	}
    }
    
    public void verificaMedicoSemAlerta(){
    	
    	if(jTFHANDLE_MEDICO_SOL.getText().length() > 0){
    		if (getValidacaoIpe()) {
                if(!MEDICOS.getConsultarSeMedicoPertenceAoIpe(jTFMedicoSol.getText())){
                	jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Médico não pertence ao convênio Ipê.");
                }else{
                	jTFMensagemParaUsuario.setText("");
                }
            }else{
            	jTFMensagemParaUsuario.setText("");
            }
    	}else{
    		jTFMensagemParaUsuario.setText("");
    	}
    }
    
    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        if ("livre".equals(horarioLivreOuOcupado) && !cadastrouNovoAtendimento) {
            deletarOAtendimento();
            janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
        }
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarActionPerformed

        // atualizando a tabela
        //atualizarTabelasDaAgenda();

        // String de retorno quando verificou se nao ha tempo para o agendamento ou se ja existe um agendamento naquele
        // horario
        //String retorno = verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada();

        //if (!"".equals(retorno)) {
        //    int resposta =
        //        JOptionPane.showConfirmDialog(null, retorno, "Atenção", JOptionPane.YES_NO_OPTION,
        //            JOptionPane.WARNING_MESSAGE);
        //    if (resposta == JOptionPane.YES_OPTION) {
                botaoSalvar();
        //    }
        //} else {
        //    botaoSalvar();
        //}

    }// GEN-LAST:event_jBSalvarActionPerformed

    private void jBPesquisaPacienteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteActionPerformed
        botaoPesquisarPaciente();
    }// GEN-LAST:event_jBPesquisaPacienteActionPerformed

    private void jBPesquisaPacienteFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteFocusLost
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jBPesquisaPacienteFocusLost

    private void jCBConvenioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBConvenioActionPerformed
        preenchendoOsExames();
        Dimension x = new Dimension(18, 27);
        jCBExame.setPreferredSize(x);

    }// GEN-LAST:event_jCBConvenioActionPerformed

    @SuppressWarnings("unchecked")
    public void preenchendoOsExames() {
        // zerando a tabela
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        duracaoDoAtendimento = 0;
        jtfDuracaoAtendimento.setText("00:00");
        jTFValorTotal.setText("");
        jTFValorConvenio.setText("");
        jTFValorPaciente.setText("");
        jCBExame.setEnabled(false);
        jCBExame.removeAllItems();

        if (jCBConvenio.getSelectedIndex() != 0) {
            int indexDoConvenio = jCBConvenio.getSelectedIndex();
            int handle_convenio = listaHandleConvenio.get(indexDoConvenio);
            
            //verifica se o medico esta correto em relacao ao convenio ipe
            verificaMedicoSemAlerta();

            // preenchendo os exames
            con = Conexao.fazConexao();
            ResultSet resultSet;
                resultSet = EXAMES.getConsultarExamesPorConvenio(con, handle_convenio, listaAreasDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()).getId_areas_atendimento());
            listaHandleExames.removeAll(listaHandleExames);
            listaDuracaoExames.removeAll(listaDuracaoExames);
            listaVaiMateriaisPorPadrao.removeAll(listaVaiMateriaisPorPadrao);

            jCBExame.removeAllItems();
            jCBExame.addItem("Selecione um Exame");

            listaHandleExames.add(0);
            listaDuracaoExames.add(0);
            listaVaiMateriaisPorPadrao.add(0);
            try {
                while (resultSet.next()) {
                    if (resultSet.getInt("vai_materiais_por_padrao") >= 0) {
                        jCBExame.addItem(resultSet.getString("nome"));
                        listaHandleExames.add(resultSet.getInt("handle_exame"));
                        listaDuracaoExames.add(resultSet.getInt("duracao"));
                        listaVaiMateriaisPorPadrao.add(resultSet.getInt("vai_materiais_por_padrao"));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                    "Não foi possivel preencher os Exames deste Convênio. Procure o administrador.", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            } finally {
                Conexao.fechaConexao(con);
                jCBExame.setEnabled(true);
            }
        }
    }

    private void jBSalvarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jBSalvarFocusLost

    private void jBAtualizarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jBAtualizarFocusLost

    private void jBIncluirExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBIncluirExameActionPerformed
        try {
            botaoIncluirExame();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Selecione um exame");
        }
         // TODO add your handling code here:
    }// GEN-LAST:event_jBIncluirExameActionPerformed

    private void jBIncluirExameKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBIncluirExameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                botaoIncluirExame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Selecione um exame");
            }
        }
    }// GEN-LAST:event_jBIncluirExameKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.isEnabled()) {
            if (evt.getButton() == MouseEvent.BUTTON3) {
                int col = jTable1.columnAtPoint(evt.getPoint());
                int row = jTable1.rowAtPoint(evt.getPoint());
                if (col != -1 && row != -1) {
                    jTable1.setColumnSelectionInterval(col, col);
                    jTable1.setRowSelectionInterval(row, row);

                    // abrindo o popUp
                    ImageIcon iconeCancelarLado =
                        new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/popUpCancelar.png"));
                    ImageIcon iconDireito =
                        new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/popUpDireito.png"));
                    ImageIcon iconEsquerdo =
                        new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/popUpEsquerdo.png"));
                    ImageIcon iconCancelarContraste =
                        new javax.swing.ImageIcon(getClass().getResource(
                            "/br/bcn/admclin/imagens/popUpCancelarContraste.png"));
                    ImageIcon iconComContraste =
                        new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/contrast.png"));
                    ImageIcon iconComMaterial =
                        new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/materiais.png"));
                    ImageIcon iconAlterarValorExame =
                        new javax.swing.ImageIcon(getClass().getResource(
                            "/br/bcn/admclin/imagens/popUpAlterarValorDeExame.png"));

                    if (jTable1.getSelectedColumn() == 4) {
                        // cria o primeiro item do menu e atribui uma ação pra ele
                        JMenuItem menuLadoDireito = new JMenuItem("Direito", iconDireito);
                        menuLadoDireito.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jTable1.setValueAt("DIREITO", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                            }
                        });

                        JMenuItem menuLadoEsquerdo = new JMenuItem("Esquerdo", iconEsquerdo);
                        menuLadoEsquerdo.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jTable1.setValueAt("ESQUERDO", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                            }
                        });

                        JMenuItem menuSemLado = new JMenuItem("Cancelar", iconeCancelarLado);
                        menuSemLado.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jTable1.setValueAt("", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                            }
                        });

                        JPopupMenu popupMenuBotaoDireito = new JPopupMenu();
                        popupMenuBotaoDireito.add(menuLadoDireito);
                        popupMenuBotaoDireito.add(menuLadoEsquerdo);
                        popupMenuBotaoDireito.addSeparator();
                        popupMenuBotaoDireito.add(menuSemLado);
                        // mostra na tela
                        int x = evt.getX();
                        int y = evt.getY();
                        popupMenuBotaoDireito.show(jTable1, x, y);
                    }

                    if (jTable1.getSelectedColumn() == 5) {
                        JMenuItem menuComMaterial = new JMenuItem("Com Material", iconComMaterial);
                        menuComMaterial.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jTable1.setValueAt("CM", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                                if (jTable1.getValueAt(jTable1.getSelectedRow(), 16) != null
                                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != ""
                                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != "null") {
                                    mudarValorDeExameCasoMudeComMaterialOuComContraste(true);
                                }
                            }
                        });

                        JMenuItem menuComContraste = new JMenuItem("Com Contraste", iconComContraste);
                        menuComContraste.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jTable1.setValueAt("CC", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                                if (jTable1.getValueAt(jTable1.getSelectedRow(), 16) != null
                                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != ""
                                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != "null") {
                                    mudarValorDeExameCasoMudeComMaterialOuComContraste(true);
                                }

                            }
                        });

                        JMenuItem menuCancelar = new JMenuItem("Cancelar", iconCancelarContraste);
                        menuCancelar.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jTable1.setValueAt("", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                                if (jTable1.getValueAt(jTable1.getSelectedRow(), 16) != null
                                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != ""
                                    || jTable1.getValueAt(jTable1.getSelectedRow(), 16) != "null") {
                                    mudarValorDeExameCasoMudeComMaterialOuComContraste(false);
                                }

                            }
                        });
                        // cria o menu popup e adiciona os 3 itens
                        JPopupMenu popupMenuBotaoDireito = new JPopupMenu();
                        popupMenuBotaoDireito.add(menuComMaterial);
                        popupMenuBotaoDireito.add(menuComContraste);
                        popupMenuBotaoDireito.addSeparator();
                        popupMenuBotaoDireito.add(menuCancelar);
                        // mostra na tela
                        int x = evt.getX();
                        int y = evt.getY();
                        popupMenuBotaoDireito.show(jTable1, x, y);
                    }

                    if (jTable1.getSelectedColumn() == 1) {
                        JMenuItem menuAlterarValorDeExame =
                            new JMenuItem("Alterar Valor de Exame", iconAlterarValorExame);
                        menuAlterarValorDeExame.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // metodo alterar valor de exame e e recalcula os valores totais
                                alterarValorDeExame();
                                calcularValoresApartirDaTabela();

                            }
                        });

                        JMenuItem menuCancelar = new JMenuItem("Excluir Exame", iconCancelarContraste);
                        menuCancelar.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // apagar exame e recalcular
                                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                                modelo.removeRow(jTable1.getSelectedRow());
                                calcularValoresApartirDaTabela();

                            }
                        });
                        // cria o menu popup e adiciona os 3 itens
                        JPopupMenu popupMenuBotaoDireito = new JPopupMenu();
                        popupMenuBotaoDireito.add(menuAlterarValorDeExame);
                        // popupMenuBotaoDireito.addSeparator();
                        popupMenuBotaoDireito.add(menuCancelar);
                        // mostra na tela
                        int x = evt.getX();
                        int y = evt.getY();
                        popupMenuBotaoDireito.show(jTable1, x, y);
                    }

                }
            }
        }

    }// GEN-LAST:event_jTable1MouseClicked

    private void alterarValorDeExame() {
        if (jTBDesconto.isSelected()) {
            retirarDesconto();
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal,
                "Seu Desconto foi Cancelado, aplique novamente o Desconto caso necessario.");
        }

        this.setVisible(false);
        String nomeExame = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1));
        double porcentagem_paciente = listaPorcentagemPaciente.get(jCBConvenio.getSelectedIndex());
        double porcentagem_convenio = listaPorcentagemConvenio.get(jCBConvenio.getSelectedIndex());
        janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento =
            new jIFAlterarValorDeExame(nomeExame, jTable1.getSelectedRow(), porcentagem_convenio, porcentagem_paciente);

        //if (veioDaPesquisa) {
            janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento);
            janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.setVisible(true);

            int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
            int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
            int lIFrame = janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.getWidth();
            int aIFrame = janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.getHeight();

            janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk
                / 2 - aIFrame / 2);
        //} else {
        //    JIFAgendaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento);
        //    janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.setVisible(true);
        //    
        //    int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
        //    int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
        //    int lIFrame = janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.getWidth();
        //    int aIFrame = janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.getHeight();
        //
        //    janelaPrincipal.internalFrameAlterarValorDeExamesNoAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk
        //        / 2 - aIFrame / 2);
        //}
    }

    public void mudarValorDeExameCasoMudeComMaterialOuComContraste(boolean somarValoresDeMateriais) {
        // pegando a data da tabela que foi clicado para pesquisar os valores dos exames com ela
        Date dataSelecionada = JXDPDataAtendimento.getDate();
        java.sql.Date dataDoExame = null;
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data = dataFormatada.format(dataSelecionada);
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date diaAtendimento = null;
            diaAtendimento = new java.sql.Date(format.parse(data).getTime());
            dataDoExame = diaAtendimento;
        } catch (ParseException ex) {
            dataDoExame = dataDeHojeEmVariavelDate;
            JOptionPane.showMessageDialog(null,
                "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        CalculoValorDeExame calculoValorExame;
        calculoValorExame =
            new CalculoValorDeExame(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()), Integer.valueOf(String
                .valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0))), dataDoExame, somarValoresDeMateriais,
                porcentagemDeDesconto);

        jTable1.setValueAt(calculoValorExame.valor_correto_exame, jTable1.getSelectedRow(), 3);

        jTable1.setValueAt(calculoValorExame.chConvenio, jTable1.getSelectedRow(), 6);
        jTable1.setValueAt(calculoValorExame.filmeConvenio, jTable1.getSelectedRow(), 7);
        jTable1.setValueAt(calculoValorExame.ch1Exame, jTable1.getSelectedRow(), 8);
        jTable1.setValueAt(calculoValorExame.ch2Exame, jTable1.getSelectedRow(), 9);
        jTable1.setValueAt(calculoValorExame.filmeExame, jTable1.getSelectedRow(), 10);
        jTable1.setValueAt(calculoValorExame.listaDeMateriais, jTable1.getSelectedRow(), 11);

        jTable1.setValueAt(calculoValorExame.redutor, jTable1.getSelectedRow(), 12);
        jTable1.setValueAt(calculoValorExame.porcentDescontoPaciente, jTable1.getSelectedRow(), 13);
        jTable1.setValueAt(calculoValorExame.porcentConvenio, jTable1.getSelectedRow(), 14);
        jTable1.setValueAt(calculoValorExame.porcentPaciente, jTable1.getSelectedRow(), 15);
        jTable1.setValueAt(calculoValorExame.valorExame, jTable1.getSelectedRow(), 16);
        jTable1.setValueAt(calculoValorExame.valorConvenio, jTable1.getSelectedRow(), 17);
        jTable1.setValueAt(calculoValorExame.valorPaciente, jTable1.getSelectedRow(), 18);
        jTable1.setValueAt(calculoValorExame.valor_correto_convenio, jTable1.getSelectedRow(), 19);
        jTable1.setValueAt(calculoValorExame.valor_correto_paciente, jTable1.getSelectedRow(), 20);
        jTable1.setValueAt(calculoValorExame.valor_desconto, jTable1.getSelectedRow(), 21);

        calcularValoresApartirDaTabela();
    }

    private void jBSalvarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // String de retorno quando verificou se nao ha tempo para o agendamento ou se ja existe um agendamento
            // naquele horario
            //String retorno = verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada();

            //if (!"".equals(retorno)) {
            //    int resposta =
            //        JOptionPane.showConfirmDialog(null, retorno, "Atenção", JOptionPane.YES_NO_OPTION,
            //            JOptionPane.WARNING_MESSAGE);
            //    if (resposta == JOptionPane.YES_OPTION) {
            //        botaoSalvar();
            //    }
            //} else {
                botaoSalvar();
            //}
        }
    }// GEN-LAST:event_jBSalvarKeyPressed

    private void jBAtualizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarActionPerformed
        botaoAtualizar();
    }// GEN-LAST:event_jBAtualizarActionPerformed

    private void jBAtualizarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizar();
        }
    }// GEN-LAST:event_jBAtualizarKeyPressed

    @SuppressWarnings("unchecked")
    private void jCBAreaDeAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBModalidadeActionPerformed

        preenchendoOsExames();
        
        /*
        //zerando a tabela
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        duracaoDoAtendimento = 0;
        jtfDuracaoAtendimento.setText("00:00");
        jTFValorTotal.setText("");
        jTFValorConvenio.setText("");
        jTFValorPaciente.setText("");
        jCBExame.setEnabled(false);
        jCBExame.removeAllItems();

        int indexDoConvenio = jCBConvenio.getSelectedIndex();
        int handle_convenio = listaHandleConvenio.get(indexDoConvenio);

        // preenchendo os exames
        con = Conexao.fazConexao();
        System.out.println("convenio " + handle_convenio);
        System.out.println("area " + listaAreasDeAtendimento.size());
        ResultSet resultSet = EXAMES.getConsultarExamesPorConvenio(con, handle_convenio, listaAreasDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()).getId_areas_atendimento());
        listaHandleExames.removeAll(listaHandleExames);
        listaDuracaoExames.removeAll(listaDuracaoExames);
        listaVaiMateriaisPorPadrao.removeAll(listaVaiMateriaisPorPadrao);

        jCBExame.removeAllItems();
        jCBExame.addItem("Selecione um Exame");

        listaHandleExames.add(0);
        listaDuracaoExames.add(0);
        listaVaiMateriaisPorPadrao.add(0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("vai_materiais_por_padrao") >= 0) {
                    jCBExame.addItem(resultSet.getString("nome"));
                    listaHandleExames.add(resultSet.getInt("handle_exame"));
                    listaDuracaoExames.add(resultSet.getInt("duracao"));
                    listaVaiMateriaisPorPadrao.add(resultSet.getInt("vai_materiais_por_padrao"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível preencher os Exames deste Convênio. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
            
        }
        */

        jCBExame.setEnabled(true);
        Dimension x = new Dimension(18, 27);
        jCBExame.setPreferredSize(x);

    }// GEN-LAST:event_jCBModalidadeActionPerformed

    private void jBPesquisaMedicoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBPesquisaMedicoActionPerformed
        botaoPesquisarMedicoSolicitante();
    }// GEN-LAST:event_jBPesquisaMedicoActionPerformed

    private void jBPesquisaMedicoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBPesquisaMedicoFocusLost
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jBPesquisaMedicoFocusLost

    private void jTFPacienteKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPacienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisarPaciente();
        }
        jTFHANDLE_PACIENTE.setText("");
        handle_paciente = 0;
    }// GEN-LAST:event_jTFPacienteKeyPressed

    private void jTFPacienteFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPacienteFocusLost
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFPacienteFocusLost

    private void jTFMedicoSolFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFMedicoSolFocusLost
        
    }// GEN-LAST:event_jTFMedicoSolFocusLost

    private void jBPesquisaPacienteKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisarPaciente();
        }
    }// GEN-LAST:event_jBPesquisaPacienteKeyPressed

    private void jBPesquisaMedicoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBPesquisaMedicoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisarMedicoSolicitante();
        }
    }// GEN-LAST:event_jBPesquisaMedicoKeyPressed

    private void jTFMedicoSolKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFMedicoSolKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisarMedicoSolicitante();
        }
        jTFHANDLE_MEDICO_SOL.setText("");
        handle_medico_sol = 0;
    }// GEN-LAST:event_jTFMedicoSolKeyPressed

    private void jBImprimirFichaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBImprimirFichaActionPerformed
        botaoImprimirFicha();
    }// GEN-LAST:event_jBImprimirFichaActionPerformed

    private void jBImprimirBoletoDeRetiradaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBImprimirBoletoDeRetiradaActionPerformed
        botaoImprimirBoletoDeRetirada();
    }// GEN-LAST:event_jBImprimirBoletoDeRetiradaActionPerformed

    private void jBImprimirFichaKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBImprimirFichaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoImprimirFicha();
        }
    }// GEN-LAST:event_jBImprimirFichaKeyPressed

    private void jBImprimirBoletoDeRetiradaKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBImprimirBoletoDeRetiradaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoImprimirBoletoDeRetirada();
        }
    }// GEN-LAST:event_jBImprimirBoletoDeRetiradaKeyPressed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyPressed
        if ("livre".equals(horarioLivreOuOcupado) && !cadastrouNovoAtendimento) {
            deletarOAtendimento();
            janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
        }
        //botaoCancelar();
        janelaPrincipal.internalFrameAtendimento.dispose();
        janelaPrincipal.internalFrameAtendimento = null;
    }// GEN-LAST:event_jBCancelarKeyPressed

    private void jTBDescontoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTBDescontoActionPerformed
        if (jTBDesconto.isSelected()) {
            criarJoptionPaneParaDefinirAPorcentagemDeDesconto();
            jTBDesconto.setText(porcentagemDeDesconto + "% de Desconto");
            // se tabela nao etiver vazia ele começa a calcular os descontos
            if (jTable1.getRowCount() > 0) {
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    double valorPacienteAntigo =
                        new BigDecimal(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 20)))).setScale(2,
                            RoundingMode.HALF_EVEN).doubleValue();
                    double valorCorretoConvenio =
                        new BigDecimal(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 19)))).setScale(2,
                            RoundingMode.HALF_EVEN).doubleValue();
                    double valorPacienteNovo =
                        new BigDecimal(valorPacienteAntigo - (valorPacienteAntigo * (porcentagemDeDesconto / 100)))
                            .setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                    double valorCorretoExameNovo =
                        new BigDecimal(valorPacienteNovo + valorCorretoConvenio).setScale(2, RoundingMode.HALF_EVEN)
                            .doubleValue();

                    jTable1.setValueAt(porcentagemDeDesconto, i, 13);
                    jTable1.setValueAt(valorPacienteNovo, i, 20);
                    jTable1.setValueAt(valorCorretoExameNovo, i, 3);
                    double valor_desconto =
                        new BigDecimal(valorPacienteAntigo - valorPacienteNovo).setScale(2, RoundingMode.HALF_EVEN)
                            .doubleValue();
                    jTable1.setValueAt(valor_desconto, i, 21);
                    // calcula os jtextfield com valores totais apartir da tabela
                    calcularValoresApartirDaTabela();
                }
            }
        } else {
            retirarDesconto();
        }
    }// GEN-LAST:event_jTBDescontoActionPerformed

    @SuppressWarnings("rawtypes")
    private void jBImprimirNotaFiscalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBImprimirNotaFiscalActionPerformed
        jCBAreaDeAtendimento.setEnabled(false);
        jCBConvenio.setEnabled(false);
        jCBExame.setEnabled(false);
        jBIncluirExame.setEnabled(false);
        jTable1.setEnabled(false);
        jTBDesconto.setEnabled(false);
        jBAtualizar.setVisible(false);
        JXDPDataAtendimento.setVisible(false);

        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            boolean imprimiu = false;

            @Override
            protected Object doInBackground() throws Exception {
                ImprimirNotaFiscalDoPacienteModelo2 imprimir = new ImprimirNotaFiscalDoPacienteModelo2(handle_at);
                imprimiu = imprimir.imprimir();

                if (imprimiu) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();
    }// GEN-LAST:event_jBImprimirNotaFiscalActionPerformed

    public void retirarDesconto() {
        jTBDesconto.setSelected(false);
        jTBDesconto.setText("Aplicar Desconto");
        porcentagemDeDesconto = 0;
        // se tabela nao etiver vazia ele começa a calcular os descontos
        if (jTable1.getRowCount() > 0) {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                double valorPaciente =
                    new BigDecimal(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 20)))).setScale(2,
                        RoundingMode.HALF_EVEN).doubleValue();
                double valor_desconto =
                    new BigDecimal(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 21)))).setScale(2,
                        RoundingMode.HALF_EVEN).doubleValue();
                double valor_paciente_novo =
                    new BigDecimal(valorPaciente + valor_desconto).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                double valorCorretoConvenio =
                    new BigDecimal(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 19)))).setScale(2,
                        RoundingMode.HALF_EVEN).doubleValue();
                double valorCorretoExameNovo =
                    new BigDecimal(valor_paciente_novo + valorCorretoConvenio).setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();

                jTable1.setValueAt(porcentagemDeDesconto, i, 13);
                jTable1.setValueAt(valor_paciente_novo, i, 20);
                jTable1.setValueAt(valorCorretoExameNovo, i, 3);

                jTable1.setValueAt(0, i, 21);
                // calcula os jtextfield com valores totais apartir da tabela
                calcularValoresApartirDaTabela();
            }
        }
    }

    public double porcentagemDeDesconto = 0;

    private void criarJoptionPaneParaDefinirAPorcentagemDeDesconto() {
        // Cria campo onde o usuario entra com a senha
        JTextField textField = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###.##"));
        Dimension tamanhoDoTextField = new Dimension(60, 30);
        textField.setPreferredSize(tamanhoDoTextField);
        textField.setMinimumSize(tamanhoDoTextField);
        textField.setMaximumSize(tamanhoDoTextField);

        // Cria um rótulo para o campo
        JLabel rotulo = new JLabel("Digite a porcentagem de Desconto:");

        // Coloca o rótulo e a caixa de entrada numa JPanel:
        JPanel entUsuario = new JPanel();
        entUsuario.add(rotulo);
        entUsuario.add(textField);

        // Mostra o rótulo e a caixa de entrada de password para o usuario fornecer a senha:
        JOptionPane.showConfirmDialog(janelaPrincipal.internalFrameJanelaPrincipal, entUsuario, "Definir Desconto",
            JOptionPane.PLAIN_MESSAGE);
        textField.requestFocusInWindow();

        // Captura o desconto:
        String desconto = textField.getText();
        try {
            porcentagemDeDesconto = Double.valueOf(desconto);
            if (porcentagemDeDesconto > 100.00) {
                criarJoptionPaneParaDefinirAPorcentagemDeDesconto();
            }
        } catch (Exception e) {
            criarJoptionPaneParaDefinirAPorcentagemDeDesconto();
        }

    }

    // metodo para imprimir a ficha de sala
    @SuppressWarnings("rawtypes")
    private void botaoImprimirFicha() {
        jCBAreaDeAtendimento.setEnabled(false);
        jCBConvenio.setEnabled(false);
        jCBExame.setEnabled(false);
        jBIncluirExame.setEnabled(false);
        jTable1.setEnabled(false);
        jTBDesconto.setEnabled(false);
        JXDPDataAtendimento.setEnabled(false);
        jBAtualizar.setVisible(false);

        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                boolean abriuFicha = false;
                // abrindo impressao de ficha de acordo com o modelo de impressao
                if (janelaPrincipal.modeloDeImpressao == 1) {
                    ImprimirFichaDeAutorizacaoModelo1 imprimirFicha = new ImprimirFichaDeAutorizacaoModelo1(handle_at);
                    abriuFicha = imprimirFicha.salvarEImprimirFicha();
                } else if (janelaPrincipal.modeloDeImpressao == 2) {
                    // imprimi a ficha e junto imprime o codigo de barras
                    ImprimirFichaEBoletoDeRetiradaModelo2 imprimirFicha =
                        new ImprimirFichaEBoletoDeRetiradaModelo2(handle_at);
                    abriuFicha = imprimirFicha.imprimir();
                } else if (janelaPrincipal.modeloDeImpressao == 3) {
                    ImprimirFichaEBoletoDeRetiradaModelo3 imprimirFicha =
                        new ImprimirFichaEBoletoDeRetiradaModelo3(handle_at);
                    abriuFicha = imprimirFicha.imprimir();
                } else if (janelaPrincipal.modeloDeImpressao == 4){
                    ImprimirFichaDeAutorizacaoModelo4 imprimirFicha = new ImprimirFichaDeAutorizacaoModelo4(handle_at);
                    abriuFicha = imprimirFicha.salvarEImprimirFicha();
                }

                // se deu tudo certo na impressao entra nesse if
                if (abriuFicha) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    if (ATENDIMENTOS.getMarcarStatus1(con, handle_at)) {
                        // verifica no banco se tem algum numero ja no status um. se tiver retorna false e se nao tiver
                        // retorna true
                        // se for true marcamos o status1 como 1 que imprimiu se nao soh imprimi e pronto
                        ATENDIMENTOS.setUpdateStatus1(con, handle_at, "1");
                    }
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };

        worker.execute();

    }

    // metodo para imprimir o boleto de retirada
    @SuppressWarnings("rawtypes")
    private void botaoImprimirBoletoDeRetirada() {
        // bloqueio apra evitar fraude!!
        jCBAreaDeAtendimento.setEnabled(false);
        jCBConvenio.setEnabled(false);
        jCBExame.setEnabled(false);
        jBIncluirExame.setEnabled(false);
        jTable1.setEnabled(false);

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                ImprimirBoletoDeRetiradaModelo1 imprimirBoletoDeRetirada =
                    new ImprimirBoletoDeRetiradaModelo1(handle_at);
                boolean abriuBoletoDeRetirada = imprimirBoletoDeRetirada.salvarEIMprimirBoletoDeRetirada();
                if (abriuBoletoDeRetirada) {
                    // aqui colocar o flag_imprimiu como "S"
                    con = Conexao.fazConexao();
                    ATENDIMENTOS.setUpdateFlagImprimiu(con, handle_at);
                    Conexao.fechaConexao(con);
                }
                return null;
            }

            @Override
            protected void done() {

            }
        };

        worker.execute();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAtualizar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBImprimirBoletoDeRetirada;
    private javax.swing.JButton jBImprimirFicha;
    private javax.swing.JButton jBImprimirNotaFiscal;
    private javax.swing.JButton jBIncluirExame;
    private javax.swing.JButton jBPesquisaMedico;
    private javax.swing.JButton jBPesquisaPaciente;
    private javax.swing.JButton jBSalvar;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBConvenio;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBExame;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBAreaDeAtendimento;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTAObservacao;
    private javax.swing.JToggleButton jTBDesconto;
    private javax.swing.JTextField jTFComplemento;
    public static javax.swing.JTextField jTFHANDLE_MEDICO_SOL;
    public static javax.swing.JTextField jTFHANDLE_PACIENTE;
    private javax.swing.JTextField jTFMatricula;
    public static javax.swing.JTextField jTFMedicoSol;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFPaciente;
    public static javax.swing.JTextField jTFValorConvenio;
    public static javax.swing.JTextField jTFValorPaciente;
    public static javax.swing.JTextField jTFValorTotal;
    public static javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDPEntregaDoExame;
    public static javax.swing.JTextField jtfDuracaoAtendimento;
    public static javax.swing.JTextField jtfHoraEntregaExame;
    public JXDatePicker JXDPDataAtendimento;
    private JFormattedTextField jTFhoraAtendimento;
    private JLabel jLAreasDoAgendamento;
    private JComboBox<String> jCBAreasDoAgendamento;
    private JCheckBox jCBPacientePagou;
}
