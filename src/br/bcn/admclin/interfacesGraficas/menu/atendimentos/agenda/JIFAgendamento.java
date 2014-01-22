package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agenda;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteLetras;
import br.bcn.admclin.calculoValorDeUmExame.CalculoValorDeExame;
import br.bcn.admclin.dao.dbris.AGENDAS;
import br.bcn.admclin.dao.dbris.A_AGENDAMENTOS;
import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.EXAMES;
import br.bcn.admclin.dao.dbris.PACIENTES;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.A_Agendamentos;
import br.bcn.admclin.dao.model.Pacientes;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Cesar Schutz
 */
public class JIFAgendamento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public List<Double> listaPorcentagemPaciente = new ArrayList<Double>();
    public List<Double> listaPorcentagemConvenio = new ArrayList<Double>();

    public List<Integer> listaHandleConvenio = new ArrayList<Integer>();

    public List<Integer> listaHandleExames = new ArrayList<Integer>();
    public List<Integer> listaDuracaoExames = new ArrayList<Integer>();
    public List<Integer> listaVaiMateriaisPorPadrao = new ArrayList<Integer>();

    private Connection con = null;
    public static int handle_paciente;
    int handle_ap;
    int handle_agenda = 0;
    JInternalFrame internalFrame;
    JTable tabelaSelecionada;

    public static String telefonePacienteSelecionado;
    public static String celularPacienteSelecionado;

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

    /**
     * Creates new form JIFagendamento
     */
    public JIFAgendamento(String horarioLivreOuOcupado, int handle_ap, int handle_agenda, JTable tabelaSelecionada,
        JInternalFrame internalFrame) {

        initComponents();
        pegandoDataDoSistema();

        this.handle_agenda = handle_agenda;
        this.handle_ap = handle_ap;
        this.internalFrame = internalFrame;
        this.tabelaSelecionada = tabelaSelecionada;
        this.horarioLivreOuOcupado = horarioLivreOuOcupado;

        jTFHANDLE_PACIENTE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfDuracaoAgendamento.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        tirandoBarraDeTitulo();

        // jtable
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        jTable1.getColumnModel().getColumn(6).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(6).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);

        jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(7).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);

        jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(8).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMinWidth(0);

        jTable1.getColumnModel().getColumn(9).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(9).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(9).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(9).setMinWidth(0);

        jTable1.getColumnModel().getColumn(10).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(10).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(10).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(10).setMinWidth(0);

        jTable1.getColumnModel().getColumn(11).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(11).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(11).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(11).setMinWidth(0);

        jTable1.getColumnModel().getColumn(2).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(65);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(55);

        if ("livre".equals(horarioLivreOuOcupado)) {
            jBDeletarAgendamento.setVisible(false);
            jBAtualizar.setVisible(false);

            // preencher agenda, data e hora
            jTFAgenda.setText(JIFUmaAgenda.jTextField1.getText());
            jTFDia.setText(String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(
                4, 14));
            jTFHora.setText((String) JIFUmaAgenda.jTable1.getValueAt(tabelaSelecionada.getSelectedRow(), 0));

            // preencher convenios
            preenchendoOsConvênios();
            preenchendoAsModalidades();

            reservandoHorarioCasoSejaUmHorarioLivre();

            // bloquando o menu (pq fizemos uma reserva nos agendamentos e soh vai deletar a reservar ao salvar o
            // agendamento ou cancelar
            janelaPrincipal.internalFrameJanelaPrincipal.desativandoOMenu();

        } else if ("ocupado".equals(horarioLivreOuOcupado)) {
            jBSalvar.setVisible(false);

            // prenchendo os convenios
            preenchendoOsConvênios();
            preenchendoAsModalidades();
            // preenchendo os campos daquele convenio
            preenchendoOsCamposDoAgendamentoCasoForEditarUmAgendamento();
            if (!"".equals(jTFHANDLE_PACIENTE.getText())) {
                jTFTelefone.setEnabled(true);
                jTFCelular.setEnabled(true);
            } else {
                jTFNascimento.setEnabled(true);
                jTFCpfPaciente.setEnabled(true);
                jTFTelefone.setEnabled(true);
                jTFCelular.setEnabled(true);
            }

        }

        // focus no paciente
        jTFPaciente.requestFocusInWindow();
    }

    public void reservandoHorarioCasoSejaUmHorarioLivre() {
        pegandoUmHandle_apDoBanco();

        A_Agendamentos agendamentoMODEL = new A_Agendamentos();
        con = Conexao.fazConexao();
        agendamentoMODEL.setUSUARIOID(USUARIOS.usrId);
        agendamentoMODEL.setDat(dataDeHojeEmVariavelDate);

        agendamentoMODEL.setHANDLE_AGENDA(handle_agenda);
        agendamentoMODEL.setHORA(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(JIFUmaAgenda.jTable1
            .getValueAt(tabelaSelecionada.getSelectedRow(), 0))));
        agendamentoMODEL.setHANDLE_AP(handle_ap);
        agendamentoMODEL.setNomePaciente("*");
        agendamentoMODEL.setHANDLE_PACIENTE(0);
        agendamentoMODEL.setHANDLE_CONVENIO(0);
        agendamentoMODEL.setHANDLE_EXAME(0);
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date diaDoAgendamento = null;
            diaDoAgendamento = new java.sql.Date(format.parse(jTFDia.getText()).getTime());
            agendamentoMODEL.setDia(diaDoAgendamento);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
        }
        boolean cadastro = A_AGENDAMENTOS.setCadastrar(con, agendamentoMODEL);
        if (!cadastro) {
            JOptionPane.showMessageDialog(null, "Erro ao reservar Horário. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void preenchendoOsCamposDoAgendamentoCasoForEditarUmAgendamento() {
        // buscando informações do agendamento no banco
        boolean primeiraVezNoFor = true;
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = A_AGENDAMENTOS.getConsultarDadosDeUmAgendamento(con, handle_ap);
        try {
            while (resultSet.next()) {

                // preencher agenda, data e hora
                jTFAgenda.setText(JIFUmaAgenda.jTextField1.getText());
                jTFDia.setText(String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue())
                    .substring(4, 14));
                jTFHora.setText((String) JIFUmaAgenda.jTable1.getValueAt(tabelaSelecionada.getSelectedRow(), 0));

                // preenchendo o restante
                jTAObservacao.setText(resultSet.getString("observacao"));

                // paciente
                jTFPaciente.setText(resultSet.getString("nomePaciente"));
                jTFNascimento
                    .setText(MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("nascimento")));
                jTFTelefone.setText(resultSet.getString("telefone"));
                telefonePacienteSelecionado = jTFTelefone.getText();
                if (!"(  )     -    ".equals(resultSet.getString("celular")))
                    jTFCelular.setText(resultSet.getString("celular"));
                celularPacienteSelecionado = jTFCelular.getText();
                jTFHANDLE_PACIENTE.setText(resultSet.getString("HANDLE_PACIENTE"));
                handle_paciente = Integer.valueOf(resultSet.getString("HANDLE_PACIENTE"));

                // convenio
                // se for primeira vez faz se nao, nao faz para nao limpar a tabela de exame
                // pq quando o comboBox tem i itemListener ativado ele limpa a tabela
                // isso para se mudar o convenio zerar a tabela e garantir que nao va colocar dois exame de convenios
                // diferentes
                if (primeiraVezNoFor) {
                    for (int x = 0; x < listaHandleConvenio.size(); x++) {
                        if (listaHandleConvenio.get(x) == resultSet.getInt("handle_convenio")) {
                            jCBConvenio.setSelectedIndex(x);
                            jCBModalidade.setSelectedItem(resultSet.getString("modalidade"));
                        }
                    }
                    primeiraVezNoFor = false;
                }

                duracaoDoAgendamento = resultSet.getInt("duracaoDoAgendamento");

                jtfDuracaoAgendamento.setText(MetodosUteis.transformarMinutosEmHorario(resultSet
                    .getInt("duracaoDoAgendamento")));
                if (!"   .   .   -  ".equals(resultSet.getString("cpfPaciente")))
                    jTFCpfPaciente.setText(resultSet.getString("cpfPaciente"));

                // exames

                // calculando o valor do exame

                // pegando a data da tabela que foi clicado para pesquisar os valores dos exames com ela
                String dataString =
                    String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date dataDoExame = null;
                try {
                    dataDoExame = new java.sql.Date(fmt.parse(dataString).getTime());
                } catch (ParseException ex) {
                    dataDoExame = dataDeHojeEmVariavelDate;
                    JOptionPane.showMessageDialog(null,
                        "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!", "ERRO",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                }
                // calculando valor do exame
                double valorDoExame;
                if ("CM".equals(resultSet.getString("material")) || "CC".equals(resultSet.getString("material"))) {
                    valorDoExame =
                        new CalculoValorDeExame(resultSet.getInt("handle_convenio"), resultSet.getInt("handle_exame"),
                            dataDoExame, true).valor_correto_exame;
                } else {
                    valorDoExame =
                        new CalculoValorDeExame(resultSet.getInt("handle_convenio"), resultSet.getInt("handle_exame"),
                            dataDoExame, false).valor_correto_exame;
                }
                // formatando com duas casas apos a virgula no valor do exame
                valorDoExame = new BigDecimal(valorDoExame).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

                // adicionando na tabela o exame com o valor calculo de acordo com a data!!!
                modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_exame")),
                    resultSet.getString("nomeExame"),
                    MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("duracaoDoExame")),
                    MetodosUteis.colocarZeroEmCampoReais(valorDoExame), resultSet.getString("lado"),
                    resultSet.getString("material"), resultSet.getString("ch_convenio"),
                    resultSet.getString("filme_convenio"), resultSet.getString("ch1_exame"),
                    resultSet.getString("ch2_exame"), resultSet.getString("filme_exame"),
                    resultSet.getString("lista_materiais") });

                // calculando valor total do agendamento
                valorTotal = 0;
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    valorTotal = valorTotal + Double.valueOf(String.valueOf(jTable1.getValueAt(i, 3)));
                }
                valorTotal = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                jTFValorTotal.setText(MetodosUteis.colocarZeroEmCampoReais(valorTotal));
                // preenchendo os valores do convenio e do paciente
                double valorPaciente =
                    listaPorcentagemPaciente.get(jCBConvenio.getSelectedIndex()) * (valorTotal / 100);
                double valorConvenio =
                    listaPorcentagemConvenio.get(jCBConvenio.getSelectedIndex()) * (valorTotal / 100);

                valorPaciente = new BigDecimal(valorPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                valorConvenio = new BigDecimal(valorConvenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

                jTFValorConvenio.setText(MetodosUteis.colocarZeroEmCampoReais(valorConvenio));
                jTFValorPaciente.setText(MetodosUteis.colocarZeroEmCampoReais(valorPaciente));
            }
        } catch (SQLException e) {
            this.dispose();
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher os dados do Agendamento. Procure o administrador." + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    @SuppressWarnings("unchecked")
    public void preenchendoOsConvênios() {
        // preenchendo as Classes de Exames
        con = Conexao.fazConexao();
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
            jCBConvenio.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Convênios. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    @SuppressWarnings("unchecked")
    public void preenchendoAsModalidades() {
        con = Conexao.fazConexao();
        ResultSet resultSet = AGENDAS.getConsultarDadosDeUmaAgenda(con, handle_agenda);
        try {
            while (resultSet.next()) {
                // preenchendo as modalidades
                if (resultSet.getInt("modalidade_cr") == 1) {
                    jCBModalidade.addItem("CR");
                }

                if (resultSet.getInt("modalidade_ct") == 1) {
                    jCBModalidade.addItem("CT");
                }

                if (resultSet.getInt("modalidade_dr") == 1) {
                    jCBModalidade.addItem("DR");
                }

                if (resultSet.getInt("modalidade_dx") == 1) {
                    jCBModalidade.addItem("DX");
                }

                if (resultSet.getInt("modalidade_mg") == 1) {
                    jCBModalidade.addItem("MG");
                }

                if (resultSet.getInt("modalidade_mr") == 1) {
                    jCBModalidade.addItem("MR");
                }

                if (resultSet.getInt("modalidade_nm") == 1) {
                    jCBModalidade.addItem("NM");
                }

                if (resultSet.getInt("modalidade_ot") == 1) {
                    jCBModalidade.addItem("OT");
                }

                if (resultSet.getInt("modalidade_rf") == 1) {
                    jCBModalidade.addItem("RF");
                }

                if (resultSet.getInt("modalidade_od") == 1) {
                    jCBModalidade.addItem("OD");
                }

                if (resultSet.getInt("modalidade_us") == 1) {
                    jCBModalidade.addItem("US");
                }

                if (resultSet.getInt("modalidade_do") == 1) {
                    jCBModalidade.addItem("DO");
                }

                if (resultSet.getInt("modalidade_tr") == 1) {
                    jCBModalidade.addItem("TR");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível preencher as Modalidades da Agenda. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void botaoPesquisar() {

        if (jTFPaciente.getText().length() >= 3) {
            janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente =
                new JIFAgendamentoSelecionarUmPaciente(jTFPaciente.getText().toUpperCase());
            JIFAgendaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente);
            janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente.setVisible(true);
            int lDesk = JIFAgendaPrincipal.jDesktopPane1.getWidth();
            int aDesk = JIFAgendaPrincipal.jDesktopPane1.getHeight();
            int lIFrame = janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente.getWidth();
            int aIFrame = janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente.getHeight();

            janelaPrincipal.internalFrameAgendamentoSelecionarUmPaciente.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
                - aIFrame / 2);

            this.setVisible(false);
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Mínimo 3 caracteres para pesquisa");
        }

    }

    public boolean verificarSeFoiTudoPreenchido() {

        boolean exameOK;
        if (jTable1.getRowCount() > 0) {
            exameOK = true;
        } else {
            exameOK = false;
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Inclua no mínimo um exame");
        }

        boolean convenioOK;
        if (jCBConvenio.getSelectedIndex() > 0) {
            convenioOK = true;
        } else {
            convenioOK = false;
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione o convênio");
        }

        boolean nomePacienteOK = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFPaciente, jTFMensagemParaUsuario);
        boolean telefoneOK =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFTelefone, jTFMensagemParaUsuario, "(  )     -    ");

        if (nomePacienteOK && telefoneOK && convenioOK && exameOK) {
            return true;
        } else {
            return false;
        }
    }

    public void pegandoUmHandle_apDoBanco() {
        // pegando o handle_ap para salvar no banco
        con = Conexao.fazConexao();
        handle_ap = A_AGENDAMENTOS.getHandleAP(con);
        A_AGENDAMENTOS.setSomarHandleAP(con);
        Conexao.fechaConexao(con);
    }

    public void botaoSalvar() {
        boolean cadastro = false;
        if (verificarSeFoiTudoPreenchido()) {
            if (handle_paciente != 0) {
                // antes de salvar o agendamento, verificar se telefone ou celular foi alterado e atualizar no paciente
                if (!jTFTelefone.getText().equals(telefonePacienteSelecionado)
                    || !jTFCelular.getText().equals(celularPacienteSelecionado)) {
                    // aqui atualizar paciente
                    con = Conexao.fazConexao();

                    Pacientes pacienteModel = new Pacientes();
                    pacienteModel.setUsuarioId(USUARIOS.usrId);
                    pacienteModel.setData(dataDeHojeEmVariavelDate);
                    pacienteModel.setHandle_paciente(handle_paciente);
                    pacienteModel.setTelefone(jTFTelefone.getText());
                    pacienteModel.setCelular(jTFCelular.getText());

                    PACIENTES.setUpdateTelefone(con, pacienteModel);
                    Conexao.fechaConexao(con);
                }
            }

            // se pegou o valor do handle_ap ele cadastra
            if (handle_ap > 0) {
                deletar();
                // cadastrar o numero de exames que tiver
                // cada inserção é uma linha no banco de dados com os mesmos dados
                // porem o handle_exame e nome exame diferente e duracao do exame diferente tb
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    // cadastrar o agendamento
                    // fazer a inserção no banco
                    A_Agendamentos agendamentoMODEL = new A_Agendamentos();
                    con = Conexao.fazConexao();
                    agendamentoMODEL.setUSUARIOID(USUARIOS.usrId);
                    agendamentoMODEL.setDat(dataDeHojeEmVariavelDate);

                    // setando dia do agendamento

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        java.sql.Date diaDoAgendamento = null;
                        diaDoAgendamento = new java.sql.Date(format.parse(jTFDia.getText()).getTime());
                        agendamentoMODEL.setDia(diaDoAgendamento);
                    } catch (ParseException ex) {
                    }

                    // setando nascimento do paciente
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        java.sql.Date diaDoNascimentoDoPaciente = null;
                        diaDoNascimentoDoPaciente = new java.sql.Date(format.parse(jTFNascimento.getText()).getTime());
                        agendamentoMODEL.setNascimento(diaDoNascimentoDoPaciente);
                    } catch (ParseException ex) {
                    }

                    agendamentoMODEL.setHANDLE_AGENDA(handle_agenda);
                    agendamentoMODEL.setHORA(MetodosUteis.transformarHorarioEmMinutos(jTFHora.getText()));
                    agendamentoMODEL.setHANDLE_PACIENTE(handle_paciente);
                    agendamentoMODEL.setHANDLE_CONVENIO(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()));

                    agendamentoMODEL.setHANDLE_EXAME(Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 0))));
                    agendamentoMODEL.setDuracaoAgendamento(MetodosUteis
                        .transformarHorarioEmMinutos(jtfDuracaoAgendamento.getText()));
                    agendamentoMODEL.setDURACAODOEXAME(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(jTable1
                        .getValueAt(i, 2))));
                    agendamentoMODEL.setHANDLE_AP(handle_ap);

                    agendamentoMODEL.setNomePaciente(jTFPaciente.getText());
                    agendamentoMODEL.setCpfPaciente(jTFCpfPaciente.getText());
                    agendamentoMODEL.setTelefone(jTFTelefone.getText());
                    agendamentoMODEL.setCelular(jTFCelular.getText());
                    agendamentoMODEL.setNomeConvenio(jCBConvenio.getSelectedItem().toString());
                    agendamentoMODEL.setNomeExame((String) jTable1.getValueAt(i, 1));
                    agendamentoMODEL.setObservacao(jTAObservacao.getText());

                    agendamentoMODEL.setLado(String.valueOf(jTable1.getValueAt(i, 4)));
                    agendamentoMODEL.setMaterial(String.valueOf(jTable1.getValueAt(i, 5)));

                    agendamentoMODEL.setCh_convenio(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 6))));
                    agendamentoMODEL.setFilme_convenio(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 7))));
                    agendamentoMODEL.setCh1_exame(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 8))));
                    agendamentoMODEL.setCh2_exame(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 9))));
                    agendamentoMODEL.setFilme_exame(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 10))));
                    agendamentoMODEL.setLista_materiais(String.valueOf(jTable1.getValueAt(i, 11)));

                    agendamentoMODEL.setModalidade(String.valueOf(jCBModalidade.getSelectedItem()));

                    cadastro = A_AGENDAMENTOS.setCadastrar(con, agendamentoMODEL);
                    Conexao.fechaConexao(con);
                }

                if (cadastro) {
                    // se cadastro faz isso
                    janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
                    botaoCancelar();
                }
            }
        }
    }

    // meodo utilizado no evento do botao salvar!!!!!
    public boolean verificarSeHaTempoParaRealizarOAgendamento() {
        try {
            // pegando duração do exame
            int duracaodoAgendamento = MetodosUteis.transformarHorarioEmMinutos(jtfDuracaoAgendamento.getText());
            // pegando duração da agenda
            int minutosLinha0 =
                MetodosUteis.transformarHorarioEmMinutos((String) JIFUmaAgenda.jTable1.getValueAt(0, 0));
            int minutosLinha1 =
                MetodosUteis.transformarHorarioEmMinutos(String.valueOf(JIFUmaAgenda.jTable1.getValueAt(1, 0)));

            int duracaoAgenda = minutosLinha1 - minutosLinha0;

            // verificando quantos horarios ira ocupadar da agenda, para verifica se eles existem
            // se nao existirem linhas (horarios) suficientes ou o flag status for diferente de nulo ou "" ou a pintura
            // for de umintervalo qualuqer
            int qtdDeHorariosQueOcuparaDaAgenda = (duracaodoAgendamento / duracaoAgenda) - 1;

            if (qtdDeHorariosQueOcuparaDaAgenda >= 0 && (duracaodoAgendamento % duracaoAgenda > 0)) {
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

    // meodo utilizado no evento do botao salvar!!!!!
    public String verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada() {

        boolean existeAgendamentoNesteHorario = false;
        boolean naoHaTempoParaFazerOAgendamento = false;

        // verifica se tem algum agendamento ali se nao estivermos editando (pq ae obviu que ja tem um ali)
        if ("livre".equals(horarioLivreOuOcupado)) {
            // se linha tiver pintada de azul sabemos que tem um agendamento ali
            if ("3".equals(String.valueOf(tabelaSelecionada.getValueAt(tabelaSelecionada.getSelectedRow(), 3)))) {
                existeAgendamentoNesteHorario = true;
            }
        }

        // retorno para verificar se ha tempo para fazer o exame
        if (!verificarSeHaTempoParaRealizarOAgendamento()) {
            naoHaTempoParaFazerOAgendamento = true;
        }

        // retornando a string
        if (existeAgendamentoNesteHorario && naoHaTempoParaFazerOAgendamento) {
            return "Já existe um agendamento neste horário e não há tempo suficiente para este agendamento. Deseja Continuar?";
        } else if (existeAgendamentoNesteHorario) {
            return "Já existe um agendamento neste horário. Deseja Continuar?";
        } else if (naoHaTempoParaFazerOAgendamento) {
            return "Não há tempo suficiente para este agendamento. Deseja Continuar?";
        } else {
            return "";
        }

    }

    public void atualizarTabelasDaAgenda() {
        // atualizando a janela de uma agenda
        Icon iconeAgendado =
            new ImageIcon(getToolkit().createImage(
                getClass().getResource("/br/bcn/admclin/imagens/menuAgendar.png")));
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

    public void botaoAtualizar() {
        // atualizando a tabela
        atualizarTabelasDaAgenda();

        // String de retorno quando verificou se nao ha tempo para o agendamento ou se ja existe um agendamento naquele
        // horario
        String retorno = verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada();

        if (!"".equals(retorno)) {
            int resposta =
                JOptionPane.showConfirmDialog(null, retorno, "Atenção", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                botaoSalvar();
            }
        } else {
            botaoSalvar();
        }

    }

    public boolean deletar() {
        con = Conexao.fazConexao();
        boolean deletou = A_AGENDAMENTOS.setDeletar(con, handle_ap);
        Conexao.fechaConexao(con);
        return deletou;
    }

    public void botaoApagar() {
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Exame?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            if (deletar()) {
                atualizarTabelasDaAgenda();
                botaoCancelar();
            }
        }
    }

    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameAgendamento = null;

        internalFrame.setVisible(true);

        JIFAgendaPrincipal.jComboBox1.setEnabled(true);
        JIFAgendaPrincipal.jComboBox2.setEnabled(true);
        JIFAgendaPrincipal.jComboBox3.setEnabled(true);
        JIFAgendaPrincipal.jComboBox4.setEnabled(true);
        JIFAgendaPrincipal.jXDatePicker1.setEnabled(true);

        // sumindo legenda da agenda
        JIFAgendaPrincipal.sumirLegenda(true);

        atualizarTabelasDaAgenda();
    }

    public void guardarInformacoesDoAgendamentoCancelado() {
        JIFUmaAgenda.listaAgendamentoCanceladoPorUltimo.clear();
        for (int i = -1; i < jTable1.getRowCount(); i++) {
            A_Agendamentos agendamentoMODEL = new A_Agendamentos();

            // setando dia do agendamento

            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date diaDoAgendamento = null;
                diaDoAgendamento = new java.sql.Date(format.parse(jTFDia.getText()).getTime());
                agendamentoMODEL.setDia(diaDoAgendamento);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
            }

            // setando nascimento do paciente
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                java.sql.Date diaDoNascimentoDoPaciente = null;
                diaDoNascimentoDoPaciente = new java.sql.Date(format.parse(jTFNascimento.getText()).getTime());
                agendamentoMODEL.setNascimento(diaDoNascimentoDoPaciente);
            } catch (ParseException ex) {

            }

            agendamentoMODEL.setHANDLE_AGENDA(handle_agenda);
            agendamentoMODEL.setHORA(MetodosUteis.transformarHorarioEmMinutos(jTFHora.getText()));
            try {
                agendamentoMODEL.setHANDLE_PACIENTE(Integer.valueOf(jTFHANDLE_PACIENTE.getText()));
            } catch (Exception e) {

            }

            agendamentoMODEL.setHANDLE_CONVENIO(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()));

            try {
                agendamentoMODEL.setHANDLE_EXAME(Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 0))));
            } catch (Exception e) {

            }

            agendamentoMODEL.setDuracaoAgendamento(MetodosUteis.transformarHorarioEmMinutos(jtfDuracaoAgendamento
                .getText()));

            try {
                agendamentoMODEL.setDURACAODOEXAME(MetodosUteis.transformarHorarioEmMinutos(String.valueOf(jTable1
                    .getValueAt(i, 2))));
            } catch (Exception e) {

            }

            agendamentoMODEL.setHANDLE_AP(handle_ap);

            agendamentoMODEL.setNomePaciente(jTFPaciente.getText());
            agendamentoMODEL.setCpfPaciente(jTFCpfPaciente.getText());
            agendamentoMODEL.setTelefone(jTFTelefone.getText());
            agendamentoMODEL.setCelular(jTFCelular.getText());
            agendamentoMODEL.setModalidade(String.valueOf(jCBModalidade.getSelectedItem()));
            agendamentoMODEL.setNomeConvenio(jCBConvenio.getSelectedItem().toString());
            try {
                agendamentoMODEL.setNomeExame((String) jTable1.getValueAt(i, 1));
            } catch (Exception e) {

            }

            agendamentoMODEL.setObservacao(jTAObservacao.getText());

            try {
                agendamentoMODEL.setLado(String.valueOf(jTable1.getValueAt(i, 4)));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setCh_convenio(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 6))));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setFilme_convenio(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 7))));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setCh1_exame(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 8))));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setCh2_exame(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 9))));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setFilme_exame(Double.valueOf(String.valueOf(jTable1.getValueAt(i, 10))));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setLista_materiais(String.valueOf(jTable1.getValueAt(i, 11)));
            } catch (Exception e) {
            }

            try {
                agendamentoMODEL.setMaterial(String.valueOf(jTable1.getValueAt(i, 5)));
            } catch (Exception e) {

            }

            JIFUmaAgenda.listaAgendamentoCanceladoPorUltimo.add(agendamentoMODEL);
        }
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Agendamento copiado");
    }

    public int duracaoDoAgendamento = 0;
    public double valorTotal = 0;

    public void botaoIncluirExame() {
        if (jCBExame.isEnabled() && jCBExame.getSelectedIndex() != 0) {

            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

            int handle_exame = listaHandleExames.get(jCBExame.getSelectedIndex());

            String nomeExame = (String) jCBExame.getSelectedItem();

            String duracaoExame =
                MetodosUteis.transformarMinutosEmHorario(listaDuracaoExames.get(jCBExame.getSelectedIndex()));
            duracaoDoAgendamento = duracaoDoAgendamento + listaDuracaoExames.get(jCBExame.getSelectedIndex());
            jtfDuracaoAgendamento.setText(MetodosUteis.transformarMinutosEmHorario(duracaoDoAgendamento));

            // incluindo valor total
            // pegando a data da tabela que foi clicado para pesquisar os valores dos exames com ela
            String dataString =
                String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date dataDoExame = null;
            try {
                dataDoExame = new java.sql.Date(fmt.parse(dataString).getTime());
            } catch (ParseException ex) {
                dataDoExame = dataDeHojeEmVariavelDate;
                JOptionPane.showMessageDialog(null,
                    "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!", "ERRO",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            CalculoValorDeExame calculoValorExame =
                new CalculoValorDeExame(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()),
                    listaHandleExames.get(jCBExame.getSelectedIndex()), dataDoExame, false);
            valorTotal += calculoValorExame.valor_correto_exame;
            valorTotal = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            jTFValorTotal.setText(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorTotal)));

            // colocando o valor do exame
            double chConvenio, filmeConvenio;
            double ch1Exame, ch2Exame, filmeExame;

            chConvenio = calculoValorExame.chConvenio;
            filmeConvenio = calculoValorExame.filmeConvenio;

            ch1Exame = calculoValorExame.ch1Exame;
            ch2Exame = calculoValorExame.ch2Exame;
            filmeExame = calculoValorExame.filmeExame;

            // colocando o valor do exame
            modelo.addRow(new Object[] { handle_exame, nomeExame, duracaoExame,
                MetodosUteis.colocarZeroEmCampoReais(calculoValorExame.valor_correto_exame), "", "", chConvenio,
                filmeConvenio, ch1Exame, ch2Exame, filmeExame, "" });

            // preenchendo os valores do convenio e do paciente
            double valorPaciente = (calculoValorExame.porcentPaciente / 100) * valorTotal;
            double valorConvenio = (calculoValorExame.porcentConvenio / 100) * valorTotal;

            valorPaciente = new BigDecimal(valorPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            valorConvenio = new BigDecimal(valorConvenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

            jTFValorConvenio.setText(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorConvenio)));
            jTFValorPaciente.setText(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorPaciente)));

            // aqui caso o exame va material por padrao chamamos o metodo, mas antes temos que selecionar a linha
            if (listaVaiMateriaisPorPadrao.get(jCBExame.getSelectedIndex()) == 1) {
                int ultimaLinha = jTable1.getRowCount() - 1;
                jTable1.addRowSelectionInterval(ultimaLinha, ultimaLinha);

                // chamando o metodo para somar os valores dos materiais
                jTable1.setValueAt("CM", jTable1.getSelectedRow(), 5);
                mudarValorDeExameCasoMudeComMaterialOuComContraste(true);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    @SuppressWarnings("rawtypes")
    private void initComponents() {

        jBCancelar = new javax.swing.JButton();
        jBSalvar = new javax.swing.JButton();
        jBDeletarAgendamento = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFAgenda = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFDia = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFHora = new javax.swing.JTextField();
        jtfDuracaoAgendamento = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTAObservacao = new javax.swing.JTextArea(new DocumentoSemAspasEPorcento(500));
        jBAtualizar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jBPesquisaPaciente = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFTelefone = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jTFNascimento = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##/##/####"));
        jTFCelular = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jTFHANDLE_PACIENTE = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTFPaciente = new javax.swing.JTextField(new DocumentoSomenteLetras(64), null, 0);
        jLabel4 = new javax.swing.JLabel();
        jTFCpfPaciente = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###.###.###-##"));
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jCBExame = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCBConvenio = new javax.swing.JComboBox();
        jBIncluirExame = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jCBModalidade = new javax.swing.JComboBox();
        jCheckBoxOT = new javax.swing.JCheckBox();
        jbImportarAgendamento = new javax.swing.JButton();
        jBArmazenarAgendamento = new javax.swing.JButton();
        jTFValorConvenio = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTFValorPaciente = new javax.swing.JTextField();
        jTFValorTotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
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

        jBDeletarAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBDeletarAgendamento.setText("Apagar");
        jBDeletarAgendamento.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBDeletarAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletarAgendamentoActionPerformed(evt);
            }
        });
        jBDeletarAgendamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBDeletarAgendamentoKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Agendamento",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Agenda");

        jTFAgenda.setText("jTextField1");
        jTFAgenda.setEnabled(false);

        jLabel2.setText("Dia");

        jTFDia.setText("08/08/2012");
        jTFDia.setEnabled(false);

        jLabel3.setText("Horário");

        jTFHora.setText("jTextField1");
        jTFHora.setEnabled(false);

        jtfDuracaoAgendamento.setText("00:00");
        jtfDuracaoAgendamento.setEnabled(false);

        jLabel10.setText("Duração total");

        jLabel11.setText("Observação");

        jTAObservacao.setColumns(20);
        jTAObservacao.setRows(5);
        jScrollPane1.setViewportView(jTAObservacao);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
            .setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1).addComponent(jLabel2))
                                        .addGap(42, 42, 42)
                                        .addGroup(
                                            jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jTFDia, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel3)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFHora, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel10)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jtfDuracaoAgendamento,
                                                            javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                                                .addComponent(jTFAgenda, javax.swing.GroupLayout.DEFAULT_SIZE, 319,
                                                    Short.MAX_VALUE)))
                                .addGroup(
                                    jPanel1Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 322,
                                            Short.MAX_VALUE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFAgenda, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfDuracaoAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTFDia, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(jTFHora, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel1Layout.createSequentialGroup().addComponent(jLabel11)
                                    .addGap(0, 88, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                    .addContainerGap()));

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

        jBPesquisaPaciente.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/Lupa.png"))); // NOI18N
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBPesquisaPacienteKeyReleased(evt);
            }
        });

        jLabel6.setText("Telefone");

        jLabel9.setText("Celular");

        jTFTelefone.setEnabled(false);

        jTFNascimento.setEnabled(false);

        jTFCelular.setEnabled(false);

        jTFHANDLE_PACIENTE.setEnabled(false);
        jTFHANDLE_PACIENTE.setFocusable(false);

        jLabel5.setText("Nascimento");

        jTFPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFPacienteFocusGained(evt);
            }

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

        jTFCpfPaciente.setEnabled(false);

        jLabel15.setText("CPF");

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
                                                .addGroup(
                                                    jPanel2Layout
                                                        .createSequentialGroup()
                                                        .addComponent(jLabel6)
                                                        .addGap(31, 31, 31)
                                                        .addComponent(jTFTelefone,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel9)
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTFCelular,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 13, Short.MAX_VALUE))
                                                .addGroup(
                                                    jPanel2Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel2Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel4).addComponent(jLabel15))
                                                        .addGap(32, 32, 32)
                                                        .addGroup(
                                                            jPanel2Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(
                                                                    jPanel2Layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(jTFCpfPaciente,
                                                                            javax.swing.GroupLayout.DEFAULT_SIZE, 75,
                                                                            Short.MAX_VALUE)
                                                                        .addGap(147, 147, 147)
                                                                        .addComponent(jTFHANDLE_PACIENTE,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 54,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(jTFPaciente,
                                                                    javax.swing.GroupLayout.DEFAULT_SIZE, 276,
                                                                    Short.MAX_VALUE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBPesquisaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(
                                    jPanel2Layout
                                        .createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTFNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 79,
                                            javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 246, Short.MAX_VALUE)))
                        .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel2Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel2Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jBPesquisaPaciente, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                javax.swing.GroupLayout.Alignment.LEADING,
                                jPanel2Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(jTFPaciente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTFHANDLE_PACIENTE, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15)
                                            .addComponent(jTFCpfPaciente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(jTFNascimento, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel2Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTFTelefone, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jTFCelular, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exames do Agendamento",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel3.setMaximumSize(new java.awt.Dimension(189, 162));
        jPanel3.setMinimumSize(new java.awt.Dimension(189, 162));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "HANDLE EXAME", "Exame(s)", "Duração", "Valor", "Lado", "Material", "chConvenio",
            "filmeConvenio", "ch1Exame", "ch2Exame", "filmeExame", "listaMateriais" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false, false, false,
                false, false };

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

        jCBExame.setEnabled(false);
        jCBExame.setMaximumSize(new java.awt.Dimension(28, 20));

        jLabel8.setText("Exame");

        jLabel7.setText("Convênio");

        jCBConvenio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBConvenioActionPerformed(evt);
            }
        });

        jBIncluirExame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaBaixo.png"))); // NOI18N
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

        jLabel16.setText("Modalidade");

        jCBModalidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBModalidadeActionPerformed(evt);
            }
        });

        jCheckBoxOT.setText("OT");
        jCheckBoxOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxOTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel3Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel3Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(
                                                jPanel3Layout.createSequentialGroup().addComponent(jLabel8)
                                                    .addGap(32, 32, 32))
                                            .addGroup(
                                                jPanel3Layout
                                                    .createSequentialGroup()
                                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGap(10, 10, 10)))
                                    .addGroup(
                                        jPanel3Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jCBConvenio, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                            .addComponent(jCBExame, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)))
                            .addComponent(jBIncluirExame, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 93,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCheckBoxOT))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel3Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jCBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxOT))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBConvenio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBExame, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBIncluirExame).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addContainerGap()));

        jbImportarAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/paste.png"))); // NOI18N
        jbImportarAgendamento.setText("Colar Agendamento");
        jbImportarAgendamento.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jbImportarAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbImportarAgendamentoActionPerformed(evt);
            }
        });
        jbImportarAgendamento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jbImportarAgendamentoFocusLost(evt);
            }
        });
        jbImportarAgendamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbImportarAgendamentoKeyPressed(evt);
            }
        });

        jBArmazenarAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/copy.png"))); // NOI18N
        jBArmazenarAgendamento.setText("Copiar Agendamento");
        jBArmazenarAgendamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBArmazenarAgendamentoActionPerformed(evt);
            }
        });
        jBArmazenarAgendamento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBArmazenarAgendamentoFocusLost(evt);
            }
        });
        jBArmazenarAgendamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBArmazenarAgendamentoKeyPressed(evt);
            }
        });

        jTFValorConvenio.setText("1.000,00");
        jTFValorConvenio.setEnabled(false);

        jLabel14.setText("Convênio: R$");

        jLabel13.setText("Paciente: R$");

        jTFValorPaciente.setText("1.000,00");
        jTFValorPaciente.setEnabled(false);

        jTFValorTotal.setText("1.000,00");
        jTFValorTotal.setEnabled(false);

        jLabel12.setText("Valor Total: R$");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorConvenio, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jBCancelar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBSalvar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBAtualizar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBDeletarAgendamento)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112,
                                        Short.MAX_VALUE)
                                    .addComponent(jBArmazenarAgendamento)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jbImportarAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 175,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 868,
                                Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel12)
                                            .addComponent(jTFValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13)
                                            .addComponent(jTFValorPaciente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14)
                                            .addComponent(jTFValorConvenio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBSalvar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBAtualizar, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(jBDeletarAgendamento, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(jbImportarAgendamento, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(jBArmazenarAgendamento, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(19, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        if ("livre".equals(horarioLivreOuOcupado)) {
            deletar();
            janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
        }
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarActionPerformed

        // atualizando a tabela
        atualizarTabelasDaAgenda();

        // String de retorno quando verificou se nao ha tempo para o agendamento ou se ja existe um agendamento naquele
        // horario
        String retorno = verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada();

        if (!"".equals(retorno)) {
            int resposta =
                JOptionPane.showConfirmDialog(null, retorno, "Atenção", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                botaoSalvar();
            }
        } else {
            botaoSalvar();
        }

    }// GEN-LAST:event_jBSalvarActionPerformed

    private void jBDeletarAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBDeletarAgendamentoActionPerformed
        botaoApagar();
    }// GEN-LAST:event_jBDeletarAgendamentoActionPerformed

    private void jBPesquisaPacienteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteActionPerformed
        botaoPesquisar();

    }// GEN-LAST:event_jBPesquisaPacienteActionPerformed

    private void jBPesquisaPacienteKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBPesquisaPacienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisar();
        }
    }// GEN-LAST:event_jBPesquisaPacienteKeyReleased

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
        duracaoDoAgendamento = 0;
        jtfDuracaoAgendamento.setText("00:00");
        jTFValorTotal.setText("");
        valorTotal = 0;
        jTFValorConvenio.setText("");
        jTFValorPaciente.setText("");

        if (jCBConvenio.getSelectedIndex() != 0) {
            int indexDoConvenio = jCBConvenio.getSelectedIndex();
            int handle_convenio = listaHandleConvenio.get(indexDoConvenio);

            // preenchendo os exames
            con = Conexao.fazConexao();

            ResultSet resultSet;
            if (jCheckBoxOT.isSelected()) {
                resultSet = EXAMES.getConsultarExamesPorConvenio(con, handle_convenio, "OT");
            } else {
                resultSet =
                    EXAMES.getConsultarExamesPorConvenio(con, handle_convenio,
                        String.valueOf(jCBModalidade.getSelectedItem()));
            }

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
        } else {
            jCBExame.setEnabled(false);
            jCBExame.removeAllItems();
            jtfDuracaoAgendamento.setText("00:00");
        }
    }

    private void jBSalvarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFPaciente.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarFocusLost

    private void jBAtualizarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFPaciente.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBAtualizarFocusLost

    private void jBIncluirExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBIncluirExameActionPerformed
        botaoIncluirExame(); // TODO add your handling code here:
    }// GEN-LAST:event_jBIncluirExameActionPerformed

    private void jBIncluirExameKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBIncluirExameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoIncluirExame();
        }
    }// GEN-LAST:event_jBIncluirExameKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked

        if (evt.getButton() == MouseEvent.BUTTON3) {
            int col = jTable1.columnAtPoint(evt.getPoint());
            int row = jTable1.rowAtPoint(evt.getPoint());
            if (col != -1 && row != -1) {
                jTable1.setColumnSelectionInterval(col, col);
                jTable1.setRowSelectionInterval(row, row);

                // abrindo o popUp
                ImageIcon iconeCancelarLado =
                    new javax.swing.ImageIcon(getClass().getResource(
                        "/br/bcn/admclin/imagens/popUpCancelar.png"));
                ImageIcon iconDireito =
                    new javax.swing.ImageIcon(getClass().getResource(
                        "/br/bcn/admclin/imagens/popUpDireito.png"));
                ImageIcon iconEsquerdo =
                    new javax.swing.ImageIcon(getClass().getResource(
                        "/br/bcn/admclin/imagens/popUpEsquerdo.png"));
                ImageIcon iconCancelarContraste =
                    new javax.swing.ImageIcon(getClass().getResource(
                        "/br/bcn/admclin/imagens/popUpCancelarContraste.png"));
                ImageIcon iconComContraste =
                    new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/contrast.png"));
                ImageIcon iconComMaterial =
                    new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/materiais.png"));

                if (jTable1.getSelectedColumn() == 4) {
                    // cria o primeiro item do menu e atribui uma ação pra ele
                    JMenuItem menuLadoDireito = new JMenuItem("Direito", iconDireito);
                    menuLadoDireito.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jTable1.setValueAt("Direito", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                        }
                    });

                    JMenuItem menuLadoEsquerdo = new JMenuItem("Esquerdo", iconEsquerdo);
                    menuLadoEsquerdo.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jTable1.setValueAt("Esquerdo", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
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
                            mudarValorDeExameCasoMudeComMaterialOuComContraste(true);

                        }
                    });

                    JMenuItem menuComContraste = new JMenuItem("Com Contraste", iconComContraste);
                    menuComContraste.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jTable1.setValueAt("CC", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                            mudarValorDeExameCasoMudeComMaterialOuComContraste(true);
                        }
                    });

                    JMenuItem menuCancelar = new JMenuItem("Cancelar", iconCancelarContraste);
                    menuCancelar.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jTable1.setValueAt("", jTable1.getSelectedRow(), jTable1.getSelectedColumn());
                            mudarValorDeExameCasoMudeComMaterialOuComContraste(false);
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

            }
        } else if (jTable1.getSelectedColumn() < 4) {
            int linhaSelecionada = jTable1.getSelectedRow();
            int duracaoDoExameSelecionado =
                MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(linhaSelecionada, 2));
            double valorDoExameSelecionado = Double.valueOf(String.valueOf(jTable1.getValueAt(linhaSelecionada, 3)));

            duracaoDoAgendamento = duracaoDoAgendamento - duracaoDoExameSelecionado;

            valorTotal = valorTotal - valorDoExameSelecionado;
            valorTotal = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            jTFValorTotal.setText(MetodosUteis.colocarZeroEmCampoReais(valorTotal));
            // preenchendo os valores do convenio e do paciente
            double valorPaciente = (listaPorcentagemPaciente.get(jCBConvenio.getSelectedIndex()) / 100) * valorTotal;
            double valorConvenio = (listaPorcentagemConvenio.get(jCBConvenio.getSelectedIndex()) / 100) * valorTotal;

            valorPaciente = new BigDecimal(valorPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            valorConvenio = new BigDecimal(valorConvenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

            jTFValorConvenio.setText(MetodosUteis.colocarZeroEmCampoReais(valorConvenio));
            jTFValorPaciente.setText(MetodosUteis.colocarZeroEmCampoReais(valorPaciente));

            jtfDuracaoAgendamento.setText(MetodosUteis.transformarMinutosEmHorario(duracaoDoAgendamento));
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.removeRow(linhaSelecionada);
        }

    }// GEN-LAST:event_jTable1MouseClicked

    public void mudarValorDeExameCasoMudeComMaterialOuComContraste(boolean somarValoresDeMateriais) {
        // pegando a data da tabela que foi clicado para pesquisar os valores dos exames com ela
        String dataString =
            String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4, 14);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date dataDoExame = null;
        try {
            dataDoExame = new java.sql.Date(fmt.parse(dataString).getTime());
        } catch (ParseException ex) {
            dataDoExame = dataDeHojeEmVariavelDate;
            JOptionPane.showMessageDialog(null,
                "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        CalculoValorDeExame calculoValorExame;

        calculoValorExame =
            new CalculoValorDeExame(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()), Integer.valueOf(String
                .valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0))), dataDoExame, somarValoresDeMateriais);

        jTable1.setValueAt(calculoValorExame.valor_correto_exame, jTable1.getSelectedRow(), 3);

        jTable1.setValueAt(calculoValorExame.chConvenio, jTable1.getSelectedRow(), 6);
        jTable1.setValueAt(calculoValorExame.filmeConvenio, jTable1.getSelectedRow(), 7);
        jTable1.setValueAt(calculoValorExame.ch1Exame, jTable1.getSelectedRow(), 8);
        jTable1.setValueAt(calculoValorExame.ch2Exame, jTable1.getSelectedRow(), 9);
        jTable1.setValueAt(calculoValorExame.filmeExame, jTable1.getSelectedRow(), 10);
        jTable1.setValueAt(calculoValorExame.listaDeMateriais, jTable1.getSelectedRow(), 11);

        // agora vamos recalcular o valor total
        valorTotal = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            double valorExame;
            // se for a linha selecionada pega valor do exame do calculoa que fez
            // pois na tabela ainda nao esta o valor correto
            // se nao pega o valor da tabela!!!
            if (jTable1.getSelectedRow() == i) {
                valorExame = calculoValorExame.valor_correto_exame;
            } else {
                valorExame = Double.valueOf(String.valueOf(jTable1.getValueAt(i, 3)));
            }
            valorTotal = valorTotal + valorExame;
        }
        valorTotal = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        jTFValorTotal.setText(String.valueOf(valorTotal));

        // separando valores de paciente e convenio
        double valorPaciente = (calculoValorExame.porcentPaciente / 100) * valorTotal;
        double valorConvenio = (calculoValorExame.porcentConvenio / 100) * valorTotal;

        valorPaciente = new BigDecimal(valorPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        valorConvenio = new BigDecimal(valorConvenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

        jTFValorConvenio.setText(String.valueOf(valorConvenio));
        jTFValorPaciente.setText(String.valueOf(valorPaciente));
    }

    private void jTFPacienteFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPacienteFocusLost
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFPacienteFocusLost

    private void jBSalvarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // String de retorno quando verificou se nao ha tempo para o agendamento ou se ja existe um agendamento
            // naquele horario
            String retorno = verificandoSeHaAlgumAgendamentoOuAtendimentoNaLinhaSelecionada();

            if (!"".equals(retorno)) {
                int resposta =
                    JOptionPane.showConfirmDialog(null, retorno, "Atenção", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (resposta == JOptionPane.YES_OPTION) {
                    botaoSalvar();
                }
            } else {
                botaoSalvar();
            }
        }
    }// GEN-LAST:event_jBSalvarKeyPressed

    private void jBDeletarAgendamentoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBDeletarAgendamentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagar();
        }
    }// GEN-LAST:event_jBDeletarAgendamentoKeyPressed

    private void jBAtualizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarActionPerformed
        botaoAtualizar();
    }// GEN-LAST:event_jBAtualizarActionPerformed

    private void jBAtualizarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizar();
        }
    }// GEN-LAST:event_jBAtualizarKeyPressed

    private void jbImportarAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbImportarAgendamentoActionPerformed
        importarAgendamentoCancelado();
    }// GEN-LAST:event_jbImportarAgendamentoActionPerformed

    private void jbImportarAgendamentoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jbImportarAgendamentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            importarAgendamentoCancelado();
        }
    }// GEN-LAST:event_jbImportarAgendamentoKeyPressed

    private void jBArmazenarAgendamentoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBArmazenarAgendamentoActionPerformed
        guardarInformacoesDoAgendamentoCancelado();
    }// GEN-LAST:event_jBArmazenarAgendamentoActionPerformed

    private void jBArmazenarAgendamentoKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBArmazenarAgendamentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            guardarInformacoesDoAgendamentoCancelado();
        }
    }// GEN-LAST:event_jBArmazenarAgendamentoKeyPressed

    private void jBArmazenarAgendamentoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBArmazenarAgendamentoFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jBArmazenarAgendamentoFocusLost

    private void jbImportarAgendamentoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jbImportarAgendamentoFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jbImportarAgendamentoFocusLost

    private void jTFPacienteKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPacienteKeyPressed
        if (jTFHANDLE_PACIENTE.getText().length() > 1) {
            jTFNascimento.setText("");
            jTFTelefone.setText("");
            jTFCelular.setText("");
            jTFCpfPaciente.setText("");
            jTFHANDLE_PACIENTE.setText("");
            handle_paciente = 0;
        }
        jTFNascimento.setEnabled(true);
        jTFCpfPaciente.setEnabled(true);
        jTFTelefone.setEnabled(true);
        jTFCelular.setEnabled(true);

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jTFPacienteKeyPressed

    private void jTFPacienteFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPacienteFocusGained

    }// GEN-LAST:event_jTFPacienteFocusGained

    @SuppressWarnings("unchecked")
    private void jCBModalidadeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBModalidadeActionPerformed
        // zerando a tabela
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        duracaoDoAgendamento = 0;
        jtfDuracaoAgendamento.setText("00:00");
        jTFValorTotal.setText("");
        valorTotal = 0;
        jTFValorConvenio.setText("");
        jTFValorPaciente.setText("");
        jCBExame.setEnabled(false);
        jCBExame.removeAllItems();

        int indexDoConvenio = jCBConvenio.getSelectedIndex();
        int handle_convenio = listaHandleConvenio.get(indexDoConvenio);

        // preenchendo os exames
        con = Conexao.fazConexao();
        ResultSet resultSet =
            EXAMES.getConsultarExamesPorConvenio(con, handle_convenio, String.valueOf(jCBModalidade.getSelectedItem()));
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

        Dimension x = new Dimension(18, 27);
        jCBExame.setPreferredSize(x);

    }// GEN-LAST:event_jCBModalidadeActionPerformed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("livre".equals(horarioLivreOuOcupado)) {
                deletar();
                janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
            }
            botaoCancelar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarKeyPressed

    @SuppressWarnings("unchecked")
    private void jCheckBoxOTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBoxOTActionPerformed
        if (jCheckBoxOT.isSelected()) {
            jCBModalidade.setEnabled(false);
            if (jCBConvenio.getSelectedIndex() != 0) {
                int indexDoConvenio = jCBConvenio.getSelectedIndex();
                int handle_convenio = listaHandleConvenio.get(indexDoConvenio);

                // preenchendo os exames
                con = Conexao.fazConexao();

                ResultSet resultSet = EXAMES.getConsultarExamesPorConvenio(con, handle_convenio, "OT");

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
        } else {
            jCBModalidade.setEnabled(true);
            if (jCBConvenio.getSelectedIndex() != 0) {
                int indexDoConvenio = jCBConvenio.getSelectedIndex();
                int handle_convenio = listaHandleConvenio.get(indexDoConvenio);

                // preenchendo os exames
                con = Conexao.fazConexao();

                ResultSet resultSet =
                    EXAMES.getConsultarExamesPorConvenio(con, handle_convenio,
                        String.valueOf(jCBModalidade.getSelectedItem()));

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
    }// GEN-LAST:event_jCheckBoxOTActionPerformed

    public void importarAgendamentoCancelado() {
        if (!JIFUmaAgenda.listaAgendamentoCanceladoPorUltimo.isEmpty()) {
            // limpando dados do paciente pq se for vazio nao vai apagar
            jTFNascimento.setText("");
            jTFCelular.setText("");
            jTFTelefone.setText("");

            boolean primeiraVezNoFor = true;
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            for (A_Agendamentos agendamentoImportado : JIFUmaAgenda.listaAgendamentoCanceladoPorUltimo) {
                // preenchendo o restante
                jTAObservacao.setText(agendamentoImportado.getObservacao());

                // paciente
                jTFPaciente.setText(agendamentoImportado.getNomePaciente());
                if (!"   .   .   -  ".equals(agendamentoImportado.getCpfPaciente()))
                    jTFCpfPaciente.setText(agendamentoImportado.getCpfPaciente());
                try {
                    if (!"  /  /    ".equals(String.valueOf(agendamentoImportado.getNascimento())))
                        jTFNascimento.setText(MetodosUteis.converterDataParaMostrarAoUsuario(agendamentoImportado
                            .getNascimento().toString()));
                } catch (Exception e) {

                }
                if (!"(  )     -    ".equals(agendamentoImportado.getTelefone()))
                    jTFTelefone.setText(agendamentoImportado.getTelefone());
                telefonePacienteSelecionado = jTFTelefone.getText();
                if (!"(  )     -    ".equals(agendamentoImportado.getCelular()))
                    jTFCelular.setText(agendamentoImportado.getCelular());
                celularPacienteSelecionado = jTFCelular.getText();
                jTFHANDLE_PACIENTE.setText(String.valueOf(agendamentoImportado.getHANDLE_PACIENTE()));
                handle_paciente = Integer.valueOf(agendamentoImportado.getHANDLE_PACIENTE());

                // convenio
                // se for primeira vez faz se nao, nao faz para nao limpar a tabela de exame
                // pq quando o comboBox tem i itemListener ativado ele limpa a tabela
                // isso para se mudar o convenio zerar a tabela e garantir que nao va colocar dois exame de convenios
                // diferentes
                if (primeiraVezNoFor) {
                    for (int x = 0; x < listaHandleConvenio.size(); x++) {
                        if (listaHandleConvenio.get(x) == agendamentoImportado.getHANDLE_CONVENIO()) {
                            jCBConvenio.setSelectedIndex(x);
                            jCBModalidade.setSelectedItem(String.valueOf(agendamentoImportado.getModalidade()));
                        }
                    }
                    primeiraVezNoFor = false;
                }

                duracaoDoAgendamento = agendamentoImportado.getDuracaoAgendamento();
                jtfDuracaoAgendamento.setText(MetodosUteis.transformarMinutosEmHorario(agendamentoImportado
                    .getDuracaoAgendamento()));

                // exames
                if (agendamentoImportado.getNomeExame() != null) {
                    // pegando a data da tabela que foi clicado para pesquisar os valores dos exames com ela
                    String dataString =
                        String.valueOf(tabelaSelecionada.getColumnModel().getColumn(0).getHeaderValue()).substring(4,
                            14);
                    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                    java.sql.Date dataDoExame = null;
                    try {
                        dataDoExame = new java.sql.Date(fmt.parse(dataString).getTime());
                    } catch (ParseException ex) {
                        dataDoExame = dataDeHojeEmVariavelDate;
                        JOptionPane.showMessageDialog(null,
                            "Não foi possível verificar a data do exame, o mesmo será calculado com a data atual!",
                            "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    // calculando o valor do exame
                    double valorDoExame;
                    CalculoValorDeExame calculoValorExame;
                    if ("CM".equals(agendamentoImportado.getMaterial())
                        || "CC".equals(agendamentoImportado.getMaterial())) {
                        calculoValorExame =
                            new CalculoValorDeExame(agendamentoImportado.getHANDLE_CONVENIO(),
                                agendamentoImportado.getHANDLE_EXAME(), dataDoExame, true);
                    } else {
                        calculoValorExame =
                            new CalculoValorDeExame(agendamentoImportado.getHANDLE_CONVENIO(),
                                agendamentoImportado.getHANDLE_EXAME(), dataDoExame, false);
                    }

                    // formatando com duas casas apos a virgula no valor do exame
                    valorDoExame =
                        new BigDecimal(calculoValorExame.valor_correto_exame).setScale(2, RoundingMode.HALF_EVEN)
                            .doubleValue();
                    modelo.addRow(new Object[] { Integer.toString(agendamentoImportado.getHANDLE_EXAME()),
                        agendamentoImportado.getNomeExame(),
                        MetodosUteis.transformarMinutosEmHorario(agendamentoImportado.getDURACAODOEXAME()),
                        MetodosUteis.colocarZeroEmCampoReais(valorDoExame), agendamentoImportado.getLado(),
                        agendamentoImportado.getMaterial(), agendamentoImportado.getCh_convenio(),
                        agendamentoImportado.getFilme_convenio(), agendamentoImportado.getCh1_exame(),
                        agendamentoImportado.getCh2_exame(), agendamentoImportado.getFilme_exame(),
                        agendamentoImportado.getLista_materiais() });
                }

                // calculando valor total do agendamento
                valorTotal = 0;
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    valorTotal = valorTotal + Double.valueOf(String.valueOf(jTable1.getValueAt(i, 3)));
                }
                valorTotal = new BigDecimal(valorTotal).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                jTFValorTotal.setText(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorTotal)));
                // preenchendo os valores do convenio e do paciente
                double valorPaciente =
                    listaPorcentagemPaciente.get(jCBConvenio.getSelectedIndex()) * (valorTotal / 100);
                double valorConvenio =
                    listaPorcentagemConvenio.get(jCBConvenio.getSelectedIndex()) * (valorTotal / 100);

                valorPaciente = new BigDecimal(valorPaciente).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
                valorConvenio = new BigDecimal(valorConvenio).setScale(2, RoundingMode.HALF_EVEN).doubleValue();

                jTFValorConvenio.setText(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorConvenio)));
                jTFValorPaciente.setText(MetodosUteis.colocarZeroEmCampoReais(String.valueOf(valorPaciente)));

                // se handle paciente for igual a 0 segnifica que não é do banco de dados entao libero tudo
                // se não é um paciente do banco entao soh libero nome e telefones
                if ("0".equals(jTFHANDLE_PACIENTE.getText()) || "".equals(jTFHANDLE_PACIENTE.getText())) {
                    jTFPaciente.setEnabled(true);
                    jTFNascimento.setEnabled(true);
                    jTFCpfPaciente.setEnabled(true);
                    jTFTelefone.setEnabled(true);
                    jTFCelular.setEnabled(true);
                } else {
                    jTFPaciente.setEnabled(true);
                    jTFNascimento.setEnabled(false);
                    jTFCpfPaciente.setEnabled(false);
                    jTFTelefone.setEnabled(true);
                    jTFCelular.setEnabled(true);
                }

            }
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Não há agendamento copiado");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBArmazenarAgendamento;
    private javax.swing.JButton jBAtualizar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBDeletarAgendamento;
    private javax.swing.JButton jBIncluirExame;
    private javax.swing.JButton jBPesquisaPaciente;
    private javax.swing.JButton jBSalvar;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBConvenio;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBExame;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBModalidade;
    private javax.swing.JCheckBox jCheckBoxOT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTAObservacao;
    public static javax.swing.JTextField jTFAgenda;
    public static javax.swing.JTextField jTFCelular;
    public static javax.swing.JTextField jTFCpfPaciente;
    public static javax.swing.JTextField jTFDia;
    public static javax.swing.JTextField jTFHANDLE_PACIENTE;
    public static javax.swing.JTextField jTFHora;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNascimento;
    public static javax.swing.JTextField jTFPaciente;
    public static javax.swing.JTextField jTFTelefone;
    private javax.swing.JTextField jTFValorConvenio;
    private javax.swing.JTextField jTFValorPaciente;
    private javax.swing.JTextField jTFValorTotal;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbImportarAgendamento;
    public static javax.swing.JTextField jtfDuracaoAgendamento;
    // End of variables declaration//GEN-END:variables

}
