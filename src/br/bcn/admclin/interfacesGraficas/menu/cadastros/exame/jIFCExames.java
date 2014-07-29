/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.exame;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteNumerosELetras;
import br.bcn.admclin.dao.dbris.AREAS_ATENDIMENTO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.EXAMES;
import br.bcn.admclin.dao.dbris.TABELAS;
import br.bcn.admclin.dao.dbris.TB_CLASSESDEEXAMES;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.dao.model.Exames;

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
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * @author Cesar Schutz
 */
public class jIFCExames extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public static List<Exames> listaExames = new ArrayList<Exames>();
    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    public List<Integer> listaHANDLE_CLASSESDEEXAMES = new ArrayList<Integer>();
    int HANDLE_EXAME;
    ArrayList<Integer> listaIdAreaDeAtendimento= new ArrayList<Integer>();

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

    /**
     * Creates new form jIFCExamesCORRETO
     */
    public jIFCExames() {
        initComponents();
        iniciarClasse();
        atualizarTabela();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    @SuppressWarnings("unchecked")
    public void iniciarClasse() {
        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        // colocando texto nas pesquisas
        jTFPesquisaNome.setText("");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);

        // preenchendo as Classes de Exames
        con = Conexao.fazConexao();
        ResultSet resultSet = TB_CLASSESDEEXAMES.getConsultar(con);
        listaHANDLE_CLASSESDEEXAMES.removeAll(listaHANDLE_CLASSESDEEXAMES);
        jCBDescricaoClasse.addItem("Selecione uma Classe");
        listaHANDLE_CLASSESDEEXAMES.add(0);
        try {
            while (resultSet.next()) {
                jCBDescricaoClasse.addItem(resultSet.getString("nome"));
                listaHANDLE_CLASSESDEEXAMES.add(resultSet.getInt("HANDLE_CLASSEDEEXAME"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher as Classes de Exames. Procure o administrador." + e, "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }

        jBCancelar.setVisible(false);
        jBSalvarRegistro.setVisible(false);
        jBAtualizarRegistro.setVisible(false);
        jBApagarRegistro.setVisible(false);

        pegandoDataDoSistema();
        
        preencheAreasDeAtendimento();
    }
    
    private void preencheAreasDeAtendimento() {
        ArrayList<Areas_atendimento> areas = AREAS_ATENDIMENTO.getConsultar();
        for (Areas_atendimento areas_atendimento : areas) {
            jCBAreasDeAtendimento.addItem(areas_atendimento.getNome());
            int id_area_atendimento = areas_atendimento.getId_areas_atendimento();
            listaIdAreaDeAtendimento.add(id_area_atendimento);
        } 
    }

    public void atualizarTabela() {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = EXAMES.getConsultar(con);
        listaExames.removeAll(listaExames);
        try {
            while (resultSet.next()) {
                Exames exame = new Exames();
                // colocando dados na tabela
                if (resultSet.getInt("handle_exame") > 0) {
                    modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_exame")),
                        resultSet.getString("nome") });
                    // colocando dados nos objetos
                    exame.setHANDLE_EXAME(resultSet.getInt("handle_exame"));
                    exame.setDuracao(resultSet.getInt("duracao"));
                    exame.setUsuarioId(resultSet.getInt("usuarioid"));
                    exame.setData(resultSet.getDate("dat"));
                    exame.setNOME(resultSet.getString("nome"));
                    exame.setQtdHoras(resultSet.getString("qtdhoras"));
                    exame.setLaudo(resultSet.getString("laudo"));
                    exame.setHANDLE_CLASSEDEEXAME(resultSet.getInt("handle_classedeexame"));
                    exame.setId_areas_atendimento(resultSet.getInt("id_areas_atendimento"));
                    exame.setArea_do_corpo(resultSet.getInt("area_do_corpo"));
                    exame.setDieta(resultSet.getString("dieta"));
                    listaExames.add(exame);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Exames",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jLabel24.setEnabled(false);
    }

    /* verifica se imprime laudo foi selecionado* */
    public boolean verificarSeLaudoFoiSelecionado() {
        boolean selecionado = false;
        if (jRBNao.isSelected() || jRBSim.isSelected()) {
            selecionado = true;
        } else {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Informe s" + "e Exame Formula Laudo");
        }
        return selecionado;
    }

    public void botaoNovo() {
        // limpandoOsCampos
        jTFNome.setText("");
        jTFDuracao.setText("");
        jCBDescricaoClasse.setSelectedIndex(0);
        jTFHorasUteis.setText("");
        jRBNao.setSelected(true);
        jRBSim.setSelected(false);

        // ativando os campos
        jTFNome.setEnabled(true);
        jTADieta.setEnabled(true);
        jCBAreasDeAtendimento.setEnabled(true);
        jCBParteDoCorpo.setEnabled(true);
        jTFDuracao.setEnabled(true);
        jCBDescricaoClasse.setEnabled(true);
        jTFHorasUteis.setEnabled(true);
        jRBNao.setEnabled(true);
        jRBSim.setEnabled(true);

        jTable1.setEnabled(false);

        jBNovoRegistro.setVisible(false);
        jBCancelar.setVisible(true);
        jBSalvarRegistro.setVisible(true);

        jTFPesquisaNome.setEnabled(false);
        jTable1.setEnabled(false);

        jTFPesquisaNome.setText("");
        atualizarTabela();
    }

    /** Volta a tela Inicial do programa. */
    public void botaoCancelar() {
        jBNovoRegistro.setVisible(true);
        jBNovoRegistro.requestFocusInWindow();

        // desativando os campos
        jTFNome.setEnabled(false);
        jTADieta.setEnabled(false);
        jCBAreasDeAtendimento.setEnabled(false);
        jCBParteDoCorpo.setEnabled(false);
        jCBAreasDeAtendimento.setSelectedIndex(0);
        jCBParteDoCorpo.setSelectedIndex(0);
        jTFDuracao.setEnabled(false);
        jCBDescricaoClasse.setEnabled(false);
        jTFHorasUteis.setEnabled(false);
        jRBNao.setEnabled(false);
        jRBSim.setEnabled(false);

        // limpandoOsCampos
        jTFMensagemParaUsuario.setText("");
        jTFNome.setText("");
        jTADieta.setText("");
        jTFDuracao.setText("");
        jCBDescricaoClasse.setSelectedIndex(0);
        jTFHorasUteis.setText("");
        jRBNao.setSelected(true);
        jRBSim.setSelected(false);

        jBSalvarRegistro.setVisible(false);
        jBApagarRegistro.setVisible(false);
        jBAtualizarRegistro.setVisible(false);
        jBCancelar.setVisible(false);

        jTable1.setEnabled(true);
        // limpar seleção da tabela
        jTable1.clearSelection();

        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFDuracao.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorasUteis.setBackground(new java.awt.Color(255, 255, 255));

        jTFPesquisaNome.setEnabled(true);
    }

    /** Salva uma nova classe de exame na banco de dados. */
    public void botaoSalvarRegistro() {
        boolean duracaoOk = false;
        boolean nomeOK = false;
        boolean horasUteisOk = false;
        boolean comboBoxOk = false;
        boolean radioButtonOk = false;

        comboBoxOk = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBDescricaoClasse, jTFMensagemParaUsuario);
        radioButtonOk = verificarSeLaudoFoiSelecionado();
        duracaoOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFDuracao, jTFMensagemParaUsuario, "    ");
        nomeOK = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        horasUteisOk =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorasUteis, jTFMensagemParaUsuario, "   ");

        if (nomeOK && duracaoOk && horasUteisOk && comboBoxOk && radioButtonOk) {
            con = Conexao.fazConexao();
            Exames exameModel = new Exames();
            exameModel.setNOME(jTFNome.getText().toUpperCase());
            exameModel.setDuracao(Integer.valueOf(jTFDuracao.getText()));
            boolean existe = EXAMES.getConsultarParaSalvar(con, exameModel);
            Conexao.fechaConexao(con);
            if (EXAMES.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Nome de Exame já existe!", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // fazer a inserção no banco
                    con = Conexao.fazConexao();
                    exameModel.setQtdHoras(jTFHorasUteis.getText());
                    exameModel.setUsuarioId(USUARIOS.usrId);
                    exameModel.setData(dataDeHojeEmVariavelDate);
                    exameModel.setHANDLE_CLASSEDEEXAME(listaHANDLE_CLASSESDEEXAMES.get(jCBDescricaoClasse
                        .getSelectedIndex()));
                    if (jRBNao.isSelected()) {
                        exameModel.setLaudo("N");
                    } else {
                        exameModel.setLaudo("S");
                    }
                    exameModel.setId_areas_atendimento(listaIdAreaDeAtendimento.get(jCBAreasDeAtendimento.getSelectedIndex()));
                    exameModel.setArea_do_corpo(jCBParteDoCorpo.getSelectedIndex());
                    exameModel.setDieta(jTADieta.getText());
                    boolean cadastro = EXAMES.setCadastrar(con, exameModel);
                    Conexao.fechaConexao(con);
                    if (cadastro) {
                        botaoCancelar();
                        atualizarTabela();
                    }
                }
            }
        }
    }

    /*
     * Atualiza uma classe de exame no banco de dados.
     */
    public void botaoAtualizarRegistro() {
        boolean duracaoOk = false;
        boolean nomeOK = false;
        boolean horasUteisOk = false;
        boolean comboBoxOk = false;
        boolean radioButtonOk = false;

        comboBoxOk = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBDescricaoClasse, jTFMensagemParaUsuario);
        radioButtonOk = verificarSeLaudoFoiSelecionado();
        duracaoOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFDuracao, jTFMensagemParaUsuario, "    ");
        nomeOK = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        horasUteisOk =
            MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorasUteis, jTFMensagemParaUsuario, "   ");

        if (nomeOK && duracaoOk && horasUteisOk && comboBoxOk && radioButtonOk) {
            // fazendo um if para verificar se descricao ou referencia ja existem

            con = Conexao.fazConexao();
            Exames exameModel = new Exames();
            exameModel.setNOME(jTFNome.getText().toUpperCase());
            exameModel.setDuracao(Integer.valueOf(jTFDuracao.getText()));
            exameModel.setHANDLE_EXAME(HANDLE_EXAME);
            boolean existe = EXAMES.getConsultarParaAtualizar(con, exameModel);
            Conexao.fechaConexao(con);
            if (EXAMES.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Descrição já existe", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    con = Conexao.fazConexao();
                    exameModel.setUsuarioId(USUARIOS.usrId);
                    exameModel.setData(dataDeHojeEmVariavelDate);
                    exameModel.setDuracao(Integer.valueOf(jTFDuracao.getText()));
                    exameModel.setNOME(jTFNome.getText().toUpperCase());
                    exameModel.setQtdHoras(jTFHorasUteis.getText());
                    if (jRBNao.isSelected()) {
                        exameModel.setLaudo("N");
                    } else {
                        exameModel.setLaudo("S");
                    }
                    exameModel.setHANDLE_CLASSEDEEXAME(listaHANDLE_CLASSESDEEXAMES.get(jCBDescricaoClasse
                        .getSelectedIndex()));
                    exameModel.setHANDLE_EXAME(HANDLE_EXAME);
                    exameModel.setId_areas_atendimento(listaIdAreaDeAtendimento.get(jCBAreasDeAtendimento.getSelectedIndex()));
                    exameModel.setArea_do_corpo(jCBParteDoCorpo.getSelectedIndex());
                    exameModel.setDieta(jTADieta.getText());
                    boolean atualizo = EXAMES.setUpdate(con, exameModel);
                    if (atualizo) {
                        botaoCancelar();
                        atualizarTabela();
                    }
                }
            }
        }
    }

    /** Apaga a classe de exame selecionada do banco de dados. */
    public void botaoApagarRegistro() {

        con = Conexao.fazConexao();
        boolean utilizada = TABELAS.verificarSeExameEstaSendoUtilizado(con, HANDLE_EXAME);
        if (utilizada) {
            JOptionPane.showMessageDialog(null, "Este Exame não pode ser deletado pois é utilizado em alguma Tabela.",
                "ATENÇÃO", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Exame?", "ATENÇÃO", 0);
            if (resposta == JOptionPane.YES_OPTION) {
                // fazer o delete de acordo com o codigo
                con = Conexao.fazConexao();
                Exames exameModel = new Exames();
                exameModel.setHANDLE_EXAME(HANDLE_EXAME);
                boolean deleto = EXAMES.setDeletar(con, exameModel);
                Conexao.fechaConexao(con);
                if (deleto) {
                    botaoCancelar();
                }
            }

        }

    }

    /**
     * 
     * 
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(101), null, 0);
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCBDescricaoClasse = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFHorasUteis = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("###"));
        jLabel10 = new javax.swing.JLabel();
        jRBSim = new javax.swing.JRadioButton();
        jRBNao = new javax.swing.JRadioButton();
        jTFDuracao = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("####"));
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTFPesquisaNome = new javax.swing.JTextField(new DocumentoSomenteNumerosELetras(64), null, 0);
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBApagarRegistro = new javax.swing.JButton();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBNovoRegistro = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Exames",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informações do Exame",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel4.setText("Nome");

        jTFNome.setEnabled(false);
        jTFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNomeFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNomeFocusLost(evt);
            }
        });
        jTFNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFNomeKeyReleased(evt);
            }
        });

        jLabel5.setText("Duração ( em minutos )");

        jLabel7.setText("Classe:");

        jCBDescricaoClasse.setEnabled(false);
        jCBDescricaoClasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBDescricaoClasseActionPerformed(evt);
            }
        });

        jLabel9.setText("Quant. Horas Úteis");

        jTFHorasUteis.setEnabled(false);
        jTFHorasUteis.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFHorasUteisFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFHorasUteisFocusLost(evt);
            }
        });

        jLabel10.setText("Formula Laudo?");

        buttonGroup1.add(jRBSim);
        jRBSim.setText("Sim");
        jRBSim.setEnabled(false);

        buttonGroup1.add(jRBNao);
        jRBNao.setText("Não");
        jRBNao.setEnabled(false);

        jTFDuracao.setEnabled(false);
        jTFDuracao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFDuracaoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFDuracaoFocusLost(evt);
            }
        });
        
        lblreaDeAtendimento = new JLabel();
        lblreaDeAtendimento.setText("Área de Atendimento");
        
        jCBAreasDeAtendimento = new JComboBox();
        jCBAreasDeAtendimento.setEnabled(false);
        
        JLabel lblParteDoCorpo = new JLabel("Parte do corpo");
        
        jCBParteDoCorpo = new JComboBox();
        jCBParteDoCorpo.setEnabled(false);
        jCBParteDoCorpo.setModel(new DefaultComboBoxModel(new String[] {"Outros Exames", "Crânio Face", "Região Cervical", "Região Escapular", "Membro Superior", "Região Torácica", "Região Abdominal", "Região Pélvica", "Membro Inferior"}));
        
        JScrollPane scrollPane = new JScrollPane();
        
        JLabel lblDieta = new JLabel("Dieta:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        				.addComponent(jTFNome, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        				.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(jTFDuracao, Alignment.LEADING)
        						.addComponent(jLabel5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        					.addGap(18)
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblreaDeAtendimento, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jCBAreasDeAtendimento, 0, 266, Short.MAX_VALUE)))
        				.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(jTFHorasUteis, Alignment.LEADING)
        						.addComponent(jLabel9, Alignment.LEADING))
        					.addGap(18)
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel2Layout.createSequentialGroup()
        							.addComponent(jLabel10)
        							.addGap(18)
        							.addComponent(lblParteDoCorpo))
        						.addGroup(jPanel2Layout.createSequentialGroup()
        							.addComponent(jRBSim)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(jRBNao)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jCBParteDoCorpo, 0, 195, Short.MAX_VALUE))))
        				.addComponent(jCBDescricaoClasse, Alignment.TRAILING, 0, 395, Short.MAX_VALUE)
        				.addComponent(lblDieta)
        				.addComponent(jLabel4)
        				.addComponent(jLabel7))
        			.addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jLabel4)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel5)
        						.addComponent(lblreaDeAtendimento))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFDuracao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addGroup(jPanel2Layout.createSequentialGroup()
        					.addGap(20)
        					.addComponent(jCBAreasDeAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jLabel7)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jCBDescricaoClasse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel9)
        				.addComponent(jLabel10)
        				.addComponent(lblParteDoCorpo))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jTFHorasUteis, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jRBSim)
        				.addComponent(jRBNao)
        				.addComponent(jCBParteDoCorpo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(lblDieta)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
        );
        
        jTADieta = new JTextArea();
        jTADieta.setEnabled(false);
        scrollPane.setViewportView(jTADieta);
        jPanel2.setLayout(jPanel2Layout);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Exames",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFPesquisaNome.setForeground(new java.awt.Color(153, 153, 153));
        jTFPesquisaNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFPesquisaNomeFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPesquisaNomeFocusLost(evt);
            }
        });
        jTFPesquisaNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFPesquisaNomeKeyReleased(evt);
            }
        });

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemPesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel4Layout.createSequentialGroup()
                    .addComponent(jTFPesquisaNome, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addGap(18, 18, 18).addComponent(jLabel24).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addComponent(jTFPesquisaNome));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "HANDLE_EXAME", "Nome" }) {
            private static final long serialVersionUID = 1L;
            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

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
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel3Layout
                    .createSequentialGroup()
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addContainerGap()));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        			.addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
        				.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)))
        );
        jPanel1.setLayout(jPanel1Layout);

        jBApagarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBApagarRegistro.setText("Apagar");
        jBApagarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBApagarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBApagarRegistroActionPerformed(evt);
            }
        });
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyPressed(evt);
            }
        });

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBAtualizarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarRegistroActionPerformed(evt);
            }
        });
        jBAtualizarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAtualizarRegistroFocusLost(evt);
            }
        });
        jBAtualizarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyPressed(evt);
            }
        });

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/cancelar.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
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

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarRegistroActionPerformed(evt);
            }
        });
        jBSalvarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarRegistroFocusLost(evt);
            }
        });
        jBSalvarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyPressed(evt);
            }
        });

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Novo");
        jBNovoRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNovoRegistroActionPerformed(evt);
            }
        });
        jBNovoRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBNovoRegistroKeyReleased(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addComponent(jTFMensagemParaUsuario, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        			.addComponent(jBNovoRegistro)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBSalvarRegistro)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBAtualizarRegistro)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBApagarRegistro)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jBCancelar)
        			.addContainerGap())
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 552, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jTFMensagemParaUsuario, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jBNovoRegistro)
        				.addComponent(jBAtualizarRegistro)
        				.addComponent(jBApagarRegistro)
        				.addComponent(jBSalvarRegistro)
        				.addComponent(jBCancelar))
        			.addGap(12))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNomeFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNomeFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jTFNomeFocusGained

    private void jTFNomeFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNomeFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        if (ok) {
            jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTFNomeFocusLost

    private void jTFNomeKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFNomeKeyReleased

    }// GEN-LAST:event_jTFNomeKeyReleased

    private void jCBDescricaoClasseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBDescricaoClasseActionPerformed

    }// GEN-LAST:event_jCBDescricaoClasseActionPerformed

    private void jTFHorasUteisFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFHorasUteisFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("###");
        jTFHorasUteis.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jTFHorasUteisFocusGained

    private void jTFHorasUteisFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFHorasUteisFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFHorasUteis, 3, jTFMensagemParaUsuario);
        if (ok) {
            jTFHorasUteis.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTFHorasUteisFocusLost

    private void jTFPesquisaNomeFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeFocusGained
        if ("".equals(jTFPesquisaNome.getText())) {
            jTFPesquisaNome.setText("");
        }
    }// GEN-LAST:event_jTFPesquisaNomeFocusGained

    private void jTFPesquisaNomeFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeFocusLost

    }// GEN-LAST:event_jTFPesquisaNomeFocusLost

    private void jTFPesquisaNomeKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeKeyReleased
        // limpa a tabela
        jTable1.getSelectionModel().clearSelection();
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        // coloca os objetos que tiverem aquele prefixo
        int i = 0;
        while (i < listaExames.size()) {
            if (listaExames.get(i).getNOME().startsWith(jTFPesquisaNome.getText().toUpperCase())) {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] { Integer.toString(listaExames.get(i).getHANDLE_EXAME()),
                    listaExames.get(i).getNOME() });
            }
            i++;
        }
        // ativando ou nao de acordo com pesquisa
        if ("".equals(jTFPesquisaNome.getText())) {
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Exames",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(false);
        } else {
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Exame(s) encontrado(s) pela Pesquisa", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(true);
        }
    }// GEN-LAST:event_jTFPesquisaNomeKeyReleased

    private void jBNovoRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBNovoRegistroActionPerformed
        botaoNovo();
    }// GEN-LAST:event_jBNovoRegistroActionPerformed

    private void jBNovoRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBNovoRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoNovo();
        }
    }// GEN-LAST:event_jBNovoRegistroKeyReleased

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }// GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFDuracao.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorasUteis.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jBAtualizarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();
        }
    }// GEN-LAST:event_jBAtualizarRegistroKeyPressed

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }// GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBApagarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();
        }
    }// GEN-LAST:event_jBApagarRegistroKeyPressed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar(); // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyPressed

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro(); // TODO add your handling code here:
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFDuracao.setBackground(new java.awt.Color(255, 255, 255));
        jTFHorasUteis.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jBSalvarRegistroKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        }
    }// GEN-LAST:event_jBSalvarRegistroKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked

        if (jTable1.isEnabled()) {
            if (jTable1.getSelectedRow() == -1) {
                jTable1.addRowSelectionInterval(0, 0);
            }

            HANDLE_EXAME = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
            preenchendoExameSelecionadoParaEditar(); // TODO add your handling code here:

            jBAtualizarRegistro.setVisible(true);
            jBApagarRegistro.setVisible(true);
            jBCancelar.setVisible(true);
            jBNovoRegistro.setVisible(false);
            jTADieta.setEnabled(true);

            jTFNome.requestFocusInWindow();
        }

    }// GEN-LAST:event_jTable1MouseClicked

    private void jTFDuracaoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFDuracaoFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFDuracao, 4, jTFMensagemParaUsuario);
        if (ok) {
            jTFDuracao.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTFDuracaoFocusLost

    private void jTFDuracaoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFDuracaoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("####");
        jTFDuracao.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jTFDuracaoFocusGained

    public void preenchendoExameSelecionadoParaEditar() {
        int indexDoArray = 0;
        int cont = 0;
        while (cont < listaExames.size()) {
            int HANDLE_EXAMESObjetos = listaExames.get(cont).getHANDLE_EXAME();
            if (HANDLE_EXAME == HANDLE_EXAMESObjetos) {
                // preenchendo com 00 a duracao
                String duracao = String.valueOf(listaExames.get(cont).getDuracao());
                if (duracao.length() == 1)
                    duracao = "000" + duracao;
                if (duracao.length() == 2)
                    duracao = "00" + duracao;
                if (duracao.length() == 3)
                    duracao = "0" + duracao;
                jTFDuracao.setText(duracao);
                jTADieta.setText(listaExames.get(cont).getDieta());

                jTFNome.setText(listaExames.get(cont).getNOME());
                for (int i = 0; i < listaHANDLE_CLASSESDEEXAMES.size(); i++) {
                    if (listaHANDLE_CLASSESDEEXAMES.get(i) == listaExames.get(cont).getHANDLE_CLASSEDEEXAME()) {

                        indexDoArray = i;
                    }
                }
                jCBDescricaoClasse.setSelectedIndex(indexDoArray);
                jTFHorasUteis.setText(listaExames.get(cont).getQtdHoras());
                if ("N".equals(listaExames.get(cont).getLaudo())) {
                    jRBNao.setSelected(true);
                    jRBSim.setSelected(false);
                }
                if ("S".equals(listaExames.get(cont).getLaudo())) {
                    jRBSim.setSelected(true);
                    jRBNao.setSelected(false);
                }
                
                for (int x = 0; x < listaIdAreaDeAtendimento.size(); x++) {
                    if (listaIdAreaDeAtendimento.get(x) == listaExames.get(cont).getId_areas_atendimento()) {
                        jCBAreasDeAtendimento.setSelectedIndex(x);
                    }
                }
                
                jCBParteDoCorpo.setSelectedIndex(listaExames.get(cont).getArea_do_corpo());
            }
            cont++;
        }

        // ativando os campos
        jTFNome.setEnabled(true);
        jTFDuracao.setEnabled(true);
        jCBDescricaoClasse.setEnabled(true);
        jCBAreasDeAtendimento.setEnabled(true);
        jCBParteDoCorpo.setEnabled(true);
        jTFHorasUteis.setEnabled(true);
        jRBNao.setEnabled(true);
        jRBSim.setEnabled(true);

        jTFPesquisaNome.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBNovoRegistro;
    private javax.swing.JButton jBSalvarRegistro;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBDescricaoClasse;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRBNao;
    private javax.swing.JRadioButton jRBSim;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTFDuracao;
    private javax.swing.JTextField jTFHorasUteis;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFPesquisaNome;
    private javax.swing.JTable jTable1;
    private JLabel lblreaDeAtendimento;
    private JComboBox jCBAreasDeAtendimento;
    private JComboBox jCBParteDoCorpo;
    private JTextArea jTADieta;
}
