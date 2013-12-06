/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCadastrarEditarIntervaloPorHorario.java
 *
 * Created on 15/08/2012, 15:35:09
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.AGENDAS;
import br.bcn.admclin.dao.A_INTERVALOSDIARIOS;
import br.bcn.admclin.dao.A_INTERVALOSDIARIOSN;
import br.bcn.admclin.dao.model.A_intervalosDiarios;
import br.bcn.admclin.dao.model.A_intervalosDiariosN;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFIntervaloDiario extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    public List<Integer> listaCodAgendas = new ArrayList<Integer>();

    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    String novoOuEditar = null;
    public static int intervaloDiarioId = 0;

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

    /** Creates new form JIFCadastrarEditarIntervaloPorHorario */
    @SuppressWarnings("static-access")
    public JIFIntervaloDiario(String novoOuEditar, int intervaloDiarioId) {
        initComponents();
        jTFNome.setDocument(new DocumentoSemAspasEPorcento(64));
        jTADescricao.setDocument(new DocumentoSemAspasEPorcento(500));
        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Ir para data atual");

        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        this.novoOuEditar = novoOuEditar;
        pegandoDataDoSistema();

        if ("novo".equals(novoOuEditar)) {
            jBEditar.setVisible(false);
            jBDeletar.setVisible(false);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Intervalo Diário",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
        if ("editar".equals(novoOuEditar)) {
            jBSalvar.setVisible(false);
            this.intervaloDiarioId = intervaloDiarioId;
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Intervalo Diário",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            preenchendoDadosDoIntervalo();
            preenchendoTabela();
        }
        preenchendoTodasAsAgendasNoComboBox();

        jTFHorarioInicial.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFHorarioFinal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void preenchendoDadosDoIntervalo() {
        // colocando os valores
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSDIARIOSN.getConsultarDadosDeUmIntervaloPorHorario(con, intervaloDiarioId);
        try {
            while (resultSet.next()) {
                // colocando dados na nos campos
                jTFNome.setText(resultSet.getString("nome"));
                jTADescricao.setText(resultSet.getString("descricao"));
                jXDatePicker1.setDate(resultSet.getDate("diadointervalo"));
                String horarioInicial = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicial"));
                jTFHorarioInicial.setText(horarioInicial);
                String horarioFinal = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinal"));
                jTFHorarioFinal.setText(horarioFinal);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher dados do Intervalo Diário. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void preenchendoTabela() {
        boolean preencherTodasAgendas = true;
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSDIARIOS.getConsultarAgendasDeUmIntervaloDiario(con, intervaloDiarioId);
        try {
            while (resultSet.next()) {
                preencherTodasAgendas = false;
                // colocando dados na tabela
                modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_agenda")),
                    resultSet.getString("nome") });
            }

            if (preencherTodasAgendas) {
                modelo.addRow(new String[] { "0", "Todas as Agendas" });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher as Agendas do intervalo. Procure o administrador" + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public boolean verificandoSeTudoFoiPreenchido() {
        boolean AgendaSelecionada = false;
        boolean nomeOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 1, jTFMensagemParaUsuario);
        boolean horarioInicialOk =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorarioInicial, jTFMensagemParaUsuario, "  :  ");
        boolean horarioFinalOk =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorarioFinal, jTFMensagemParaUsuario, "  :  ");

        if (jTable1.getRowCount() == 0) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione pelo menos uma Agenda");
            AgendaSelecionada = false;
        } else {
            AgendaSelecionada = true;
        }

        // verificando horario de inicio e fim se esta correto
        if (horarioFinalOk && horarioInicialOk) {
            horarioFinalOk = MetodosUteis.verificarSeHoraEstaCorreta(jTFHorarioFinal, jTFMensagemParaUsuario);
            horarioInicialOk = MetodosUteis.verificarSeHoraEstaCorreta(jTFHorarioInicial, jTFMensagemParaUsuario);

            if (MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()) >= MetodosUteis
                .transformarHorarioEmMinutos(jTFHorarioFinal.getText())) {
                horarioInicialOk = false;
                horarioFinalOk = false;
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Horário Inválido");
                jTFHorarioFinal.setBackground(new java.awt.Color(255, 170, 170));
                jTFHorarioInicial.setBackground(new java.awt.Color(255, 170, 170));
            }
        }

        if (nomeOk && horarioFinalOk && horarioInicialOk && AgendaSelecionada) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void preenchendoTodasAsAgendasNoComboBox() {
        con = Conexao.fazConexao();
        ResultSet resultSet = AGENDAS.getConsultar(con);
        listaCodAgendas.removeAll(listaCodAgendas);
        jCBAgendas.addItem("Todas as Agendas");
        listaCodAgendas.add(0);
        try {
            while (resultSet.next()) {
                jCBAgendas.addItem(resultSet.getString("nome"));
                listaCodAgendas.add(resultSet.getInt("handle_agenda"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Agendas. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameIntervalosDiarios = null;

        janelaPrincipal.internalFrameIntervalosDiariosVisualizar = new JIFIntervaloDiarioVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameIntervalosDiariosVisualizar);
        janelaPrincipal.internalFrameIntervalosDiariosVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameIntervalosDiariosVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameIntervalosDiariosVisualizar.getHeight();

        janelaPrincipal.internalFrameIntervalosDiariosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
            - aIFrame / 2);
    }

    public void botaoSalvar() {
        if (verificandoSeTudoFoiPreenchido()) {
            if (jTable1.getRowCount() > 0) {
                con = Conexao.fazConexao();
                A_intervalosDiariosN intervaloDiarioNModel = new A_intervalosDiariosN();
                intervaloDiarioNModel.setNome(jTFNome.getText().toUpperCase());
                boolean existe = A_INTERVALOSDIARIOSN.getConsultarParaSalvarRegistro(con, intervaloDiarioNModel);
                Conexao.fechaConexao(con);
                if (A_INTERVALOSDIARIOSN.conseguiuConsulta) {
                    if (existe) {
                        JOptionPane.showMessageDialog(null, "Intervalo por Período já existe", "ATENÇÃO",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // fazer a inserção no banco
                        con = Conexao.fazConexao();
                        intervaloDiarioNModel.setDat(dataDeHojeEmVariavelDate);
                        intervaloDiarioNModel.setUsuarioId(USUARIOS.usrId);
                        intervaloDiarioNModel.setDescricao(jTADescricao.getText());
                        intervaloDiarioNModel.setHorarioInicial(MetodosUteis
                            .transformarHorarioEmMinutos(jTFHorarioInicial.getText()));
                        intervaloDiarioNModel.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal
                            .getText()));

                        Date dataSelecionada = jXDatePicker1.getDate();
                        // criando um formato de data
                        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
                        // colocando data selecionado no formato criado acima
                        String diaDoIntervalo = dataFormatada.format(dataSelecionada);
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            java.sql.Date diaDoIntervaloCorrreto =
                                new java.sql.Date(format.parse(diaDoIntervalo).getTime());
                            intervaloDiarioNModel.setDiadoIntervalo(diaDoIntervaloCorrreto);
                        } catch (ParseException e) {
                            JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
                        }

                        boolean cadastro = A_INTERVALOSDIARIOSN.setCadastrar(con, intervaloDiarioNModel);
                        Conexao.fechaConexao(con);
                        if (cadastro) {
                            // pegando id do intervalo cadastrado
                            con = Conexao.fazConexao();
                            A_intervalosDiariosN intervaloDiarioNMODEL = new A_intervalosDiariosN();
                            intervaloDiarioNMODEL.setNome(jTFNome.getText().toUpperCase());
                            int idIntervalo =
                                A_INTERVALOSDIARIOSN.getConsultarIdDeUmNomeCadastrado(con, intervaloDiarioNMODEL);

                            // salvando as agendas
                            A_intervalosDiarios intervaloDiarioModel = new A_intervalosDiarios();
                            intervaloDiarioModel.setA_intervaloDiarioNId(idIntervalo);

                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while (i < numeroDeLinhasNaTabela) {
                                intervaloDiarioModel.setAgendaId(Integer.valueOf((String) jTable1.getValueAt(i, 0)));
                                A_INTERVALOSDIARIOS.setCadastrar(con, intervaloDiarioModel);
                                i++;
                            }

                            Conexao.fechaConexao(con);

                            botaoCancelar();
                        }
                    }
                }
            }
        }
    }

    public void botaoIncluir() {
        if (jCBAgendas.getSelectedIndex() == 0) {
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.addRow(new String[] { String.valueOf(listaCodAgendas.get(jCBAgendas.getSelectedIndex())),
                String.valueOf(jCBAgendas.getSelectedItem()) });
        } else {

            if (jTable1.getRowCount() > 0) {
                if (Integer.valueOf((String) jTable1.getValueAt(0, 0)) == 0) {
                    ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
                    jTable1.updateUI();
                }
            }

            if (verificandoSeAgendaJaEstaCadastrada(listaCodAgendas.get(jCBAgendas.getSelectedIndex()))) {

            } else {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] { String.valueOf(listaCodAgendas.get(jCBAgendas.getSelectedIndex())),
                    String.valueOf(jCBAgendas.getSelectedItem()) });
            }

        }

    }

    public boolean verificandoSeAgendaJaEstaCadastrada(int agendaId) {
        boolean AgendaJaFoiCadastrada = false;

        if (jTable1.getRowCount() > 0) {
            int i = 0;
            int numeroDeLinhasNaTabela = jTable1.getRowCount();

            while (i < numeroDeLinhasNaTabela) {

                int agendaIdDaTabela = Integer.valueOf((String) jTable1.getValueAt(i, 0));
                int agendaIdSendoCadastrada = listaCodAgendas.get(jCBAgendas.getSelectedIndex());

                if (agendaIdDaTabela == agendaIdSendoCadastrada) {
                    AgendaJaFoiCadastrada = true;
                }

                i++;
            }
        }
        return AgendaJaFoiCadastrada;

    }

    public void botaoDeletar() {
        int resposta =
            JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Intervalo Diário?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            con = Conexao.fazConexao();
            A_INTERVALOSDIARIOS.setDeletar(con, intervaloDiarioId);
            A_INTERVALOSDIARIOSN.setDeletar(con, intervaloDiarioId);
            Conexao.fechaConexao(con);

            botaoCancelar();
        }

    }

    public void botaoAtualizar() {
        if (verificandoSeTudoFoiPreenchido()) {
            if (jTable1.getRowCount() > 0) {
                con = Conexao.fazConexao();
                A_intervalosDiariosN intervaloDiarioNModel = new A_intervalosDiariosN();
                intervaloDiarioNModel.setNome(jTFNome.getText().toUpperCase());
                intervaloDiarioNModel.setA_intervaloDiarioId(intervaloDiarioId);
                boolean existe = A_INTERVALOSDIARIOSN.getConsultarParaAtualizarRegistro(con, intervaloDiarioNModel);
                Conexao.fechaConexao(con);
                if (A_INTERVALOSDIARIOSN.conseguiuConsulta) {
                    if (existe) {
                        JOptionPane.showMessageDialog(null, "Intervalo por Horário já existe", "ATENÇÃO",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // fazer a inserção no banco
                        con = Conexao.fazConexao();
                        intervaloDiarioNModel.setDat(dataDeHojeEmVariavelDate);
                        intervaloDiarioNModel.setUsuarioId(USUARIOS.usrId);
                        intervaloDiarioNModel.setDescricao(jTADescricao.getText());
                        intervaloDiarioNModel.setHorarioInicial(MetodosUteis
                            .transformarHorarioEmMinutos(jTFHorarioInicial.getText()));
                        intervaloDiarioNModel.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal
                            .getText()));

                        Date dataSelecionada = jXDatePicker1.getDate();
                        // criando um formato de data
                        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
                        // colocando data selecionado no formato criado acima
                        String diaDoIntervalo = dataFormatada.format(dataSelecionada);
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            java.sql.Date diaDoIntervaloCorrreto =
                                new java.sql.Date(format.parse(diaDoIntervalo).getTime());
                            intervaloDiarioNModel.setDiadoIntervalo(diaDoIntervaloCorrreto);
                        } catch (ParseException e) {
                            JOptionPane.showMessageDialog(null, "Erro com a data. Procure o Administrador.");
                        }

                        boolean cadastro = A_INTERVALOSDIARIOSN.setAtualizar(con, intervaloDiarioNModel);
                        Conexao.fechaConexao(con);
                        if (cadastro) {
                            // deletando as agendas
                            con = Conexao.fazConexao();
                            A_INTERVALOSDIARIOS.setDeletar(con, intervaloDiarioId);

                            // cadastrando novas agendas
                            A_intervalosDiarios intervaloDiarioModel = new A_intervalosDiarios();
                            intervaloDiarioModel.setA_intervaloDiarioNId(intervaloDiarioId);

                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while (i < numeroDeLinhasNaTabela) {
                                intervaloDiarioModel.setAgendaId(Integer.valueOf((String) jTable1.getValueAt(i, 0)));
                                A_INTERVALOSDIARIOS.setCadastrar(con, intervaloDiarioModel);
                                i++;
                            }

                            Conexao.fechaConexao(con);

                            botaoCancelar();
                        }
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBSalvar = new javax.swing.JButton();
        jBEditar = new javax.swing.JButton();
        jBDeletar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTFNome = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFHorarioInicial = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##:##"));
        jTFHorarioFinal = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##:##"));
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jCBAgendas = new javax.swing.JComboBox();
        jBIncluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTADescricao = new javax.swing.JTextArea(new DocumentoSemAspasEPorcento(500));
        jLabel5 = new javax.swing.JLabel();

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarKeyReleased(evt);
            }
        });

        jBEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBEditar.setText("Editar");
        jBEditar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarActionPerformed(evt);
            }
        });
        jBEditar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBEditarFocusLost(evt);
            }
        });
        jBEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBEditarKeyReleased(evt);
            }
        });

        jBDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBDeletar.setText("Apagar");
        jBDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletarActionPerformed(evt);
            }
        });
        jBDeletar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBDeletarKeyReleased(evt);
            }
        });

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBCancelarKeyReleased(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "aaa",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Horário Inicial");

        jLabel2.setText("Horário Final");

        jLabel3.setText("Nome");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agendas que utilizam o Intervalo",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jBIncluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaBaixo.png"))); // NOI18N
        jBIncluir.setText("Incluir");
        jBIncluir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jBIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBIncluirActionPerformed(evt);
            }
        });
        jBIncluir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBIncluirKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "AgendaId", "Agendas Cadastradas" }) {
            private static final long serialVersionUID = 1L;
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBAgendas, 0, 349, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                            .addComponent(jBIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
                    .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jCBAgendas, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBIncluir)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addContainerGap()));

        jLabel4.setText("Dia");

        jTADescricao.setColumns(20);
        jTADescricao.setRows(5);
        jScrollPane2.setViewportView(jTADescricao);

        jLabel5.setText("Descrição");

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
                                .addComponent(jLabel1)
                                .addGroup(
                                    jPanel2Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2).addComponent(jLabel4).addComponent(jLabel3)
                                                .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(
                                            jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(
                                                    jPanel2Layout
                                                        .createParallelGroup(
                                                            javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jTFHorarioInicial,
                                                            javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTFHorarioFinal,
                                                            javax.swing.GroupLayout.Alignment.LEADING,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 53,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jTFNome, javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                    .addGap(13, 13, 13)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel1)
                                            .addComponent(jTFHorarioInicial, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jTFHorarioFinal, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
            .addGroup(
                layout.createSequentialGroup().addComponent(jBCancelar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBSalvar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBEditar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBDeletar)
                    .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(
                            layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jBSalvar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBEditar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBDeletar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBIncluirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBIncluirActionPerformed
        botaoIncluir();
    }// GEN-LAST:event_jBIncluirActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarActionPerformed
        botaoSalvar();
    }// GEN-LAST:event_jBSalvarActionPerformed

    private void jBSalvarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvar();
        }
    }// GEN-LAST:event_jBSalvarKeyReleased

    private void jBSalvarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorarioFinal.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorarioInicial.setBackground(new java.awt.Color(255, 255, 255)); // TODO add your handling code here:
    }// GEN-LAST:event_jBSalvarFocusLost

    private void jBIncluirKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBIncluirKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoIncluir();
        }
    }// GEN-LAST:event_jBIncluirKeyReleased

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTable1FocusGained
        int linha = jTable1.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.removeRow(linha);

        jBIncluir.requestFocusInWindow();
    }// GEN-LAST:event_jTable1FocusGained

    private void jBDeletarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBDeletarActionPerformed
        botaoDeletar();
    }// GEN-LAST:event_jBDeletarActionPerformed

    private void jBDeletarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBDeletarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoDeletar();
        }
    }// GEN-LAST:event_jBDeletarKeyReleased

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBEditarActionPerformed
        botaoAtualizar();
    }// GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBEditarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizar();
        }
    }// GEN-LAST:event_jBEditarKeyReleased

    private void jBEditarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBEditarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorarioFinal.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorarioInicial.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBEditarFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBDeletar;
    public static javax.swing.JButton jBEditar;
    private javax.swing.JButton jBIncluir;
    public static javax.swing.JButton jBSalvar;
    @SuppressWarnings("rawtypes")
    public static javax.swing.JComboBox jCBAgendas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTADescricao;
    public static javax.swing.JTextField jTFHorarioFinal;
    public static javax.swing.JTextField jTFHorarioInicial;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    public static javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables
}
