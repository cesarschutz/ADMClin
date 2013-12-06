/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCTabelasAdicionarExames.java
 *
 * Created on 03/07/2012, 14:51:39
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.calculoValorDeUmExame.CalculoValorDeExame;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.TABELAS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCTabelas extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public String nomeConvenio = null;
    public String handle_convenio = null;
    Connection con = null;

    /** Creates new form JIFCTabelasAdicionarExames */
    public JIFCTabelas(String nomeConvenio, String handle_convenio) {
        initComponents();
        this.nomeConvenio = nomeConvenio;
        this.handle_convenio = handle_convenio;
        iniciarClasse();
        preencherModalidades();
        atualizarTabela1();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    @SuppressWarnings("unchecked")
    public void preencherModalidades() {
        con = Conexao.fazConexao();
        ResultSet resultSet = TABELAS.getConsultarModalidades(con);
        try {
            while (resultSet.next()) {
                jCBModalidades.addItem(resultSet.getString("modalidade"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Modalidades. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void iniciarClasse() {
        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");

        // alinhando conteudo da coluna de uma tabela
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        jTable1.getColumnModel().getColumn(1).setCellRenderer(centralizado);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(6).setCellRenderer(direita);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(direita);

        jTable1.setAutoCreateRowSorter(true);
        jTable2.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
        jTable2.setRowHeight(20);
        jTFNomeDaTabela.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTFNomeDaTabela.setText(nomeConvenio);

        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);

        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        jTable2.getColumnModel().getColumn(3).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(3).setMinWidth(0);
        jTable2.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
        jTable2.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);

        jTable1.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(300);
        jTable2.getColumnModel().getColumn(1).setPreferredWidth(300);

    }

    public void atualizarTabela1() {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        String modalidade = String.valueOf(jCBModalidades.getSelectedItem());
        ResultSet resultSet = TABELAS.getConsultarExamesDaTabela(con, Integer.parseInt(handle_convenio), modalidade);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                modelo
                    .addRow(new String[] { Integer.toString(resultSet.getInt("handle_exame")),
                        resultSet.getString("sinonimo"), resultSet.getString("cod_exame"), resultSet.getString("nome"),
                        MetodosUteis.colocarZeroEmCampoReais(resultSet.getString("cofch1")).replace(".", ","),
                        resultSet.getString("cofch2").replace(".", ","),
                        resultSet.getString("coeffilme").replace(".", ",") });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar os Exames. Procure o administrador",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    public void atualizarMateriasisDeUmExame() {
        ((DefaultTableModel) jTable2.getModel()).setNumRows(0);
        jTable2.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        con = Conexao.fazConexao();
        String handle_exame = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        ResultSet resultSet = TABELAS.getConsultarDadosDeUmExame(con, Integer.parseInt(handle_convenio), handle_exame);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                if (Integer.valueOf(resultSet.getInt("handle_material")) != 0) {
                    modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_material")),
                        resultSet.getString("nome"), Integer.toString(resultSet.getInt("qtdmaterial")),
                        Integer.toString(resultSet.getInt("tabelaId")) });
                }
            }
            jBAdicionarMaterial.requestFocusInWindow();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possível atualizar os dados deste Exame. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }

        jBCancelar.setEnabled(true);
        jBRemoverExame.setEnabled(true);
        jBAdicionarMaterial.setEnabled(true);
        jBEditarCoeficientes.setEnabled(true);
        jBRemoverMaterial.setEnabled(true);
        jBAdicionarExame.setEnabled(true);
        jXDatePicker1.setEnabled(true);
        jCBCalcularMateriais.setEnabled(true);
        jBCalcularValorDoExame.setEnabled(true);

    }

    /** Fecha a janela */
    public void botaoRetornar() {
        this.dispose();
        janelaPrincipal.internalFrameTabelas = null;

        janelaPrincipal.internalFrameTabelasVisualizar = new JIFCTabelasVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameTabelasVisualizar);
        janelaPrincipal.internalFrameTabelasVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameTabelasVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameTabelasVisualizar.getHeight();

        janelaPrincipal.internalFrameTabelasVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    public void botaoCancelar() {
        jTable1.getSelectionModel().clearSelection();
        ((DefaultTableModel) jTable2.getModel()).setNumRows(0);
        jTable2.updateUI();
        jBAdicionarExame.setEnabled(true);
        jXDatePicker1.setEnabled(false);
        jCBCalcularMateriais.setEnabled(false);
        jBCalcularValorDoExame.setEnabled(false);
        jBCancelar.setEnabled(false);
        jBAdicionarMaterial.setEnabled(false);
        jBEditarCoeficientes.setEnabled(false);
        jBRemoverExame.setEnabled(false);
        jBRemoverMaterial.setEnabled(false);
        jBAdicionarExame.requestFocusInWindow();
    }

    public void botaoCadastrarMaterial() {
        String handle_exame = (String) JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 0);
        String nomeExame = (String) JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 3);
        this.setVisible(false);

        janelaPrincipal.internalFrameTabelasAdicionarUmMaterial =
            new JIFCTabelasAdicionarUmMaterialAUmExame(nomeConvenio, handle_convenio, handle_exame, nomeExame);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameTabelasAdicionarUmMaterial);
        janelaPrincipal.internalFrameTabelasAdicionarUmMaterial.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameTabelasAdicionarUmMaterial.getWidth();
        int aIFrame = janelaPrincipal.internalFrameTabelasAdicionarUmMaterial.getHeight();

        janelaPrincipal.internalFrameTabelasAdicionarUmMaterial.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
            - aIFrame / 2);

    }

    public void botaoApagarExame() {
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente apagar esse Exame?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            // fazer o delete de acordo com o codigo
            String handle_exame = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            con = Conexao.fazConexao();
            boolean deleto = TABELAS.setDeletarUmExame(con, handle_exame, handle_convenio);
            Conexao.fechaConexao(con);
            // atualizar tabela
            if (deleto) {
                botaoCancelar();
                atualizarTabela1();
            }
        }
    }

    public void botaoApagarMaterial() {
        if (jTable2.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente apagar esse Material?", "ATENÇÃO", 0);
            if (resposta == JOptionPane.YES_OPTION) {
                // fazer o delete de acordo com o codigo
                String handle_material = (String) jTable2.getValueAt(jTable2.getSelectedRow(), 0);
                String tabelaId = (String) jTable2.getValueAt(jTable2.getSelectedRow(), 3);
                con = Conexao.fazConexao();
                boolean deleto = TABELAS.setDeletarUmMaterialDeUmExame(con, handle_material, tabelaId);
                Conexao.fechaConexao(con);
                // atualizar tabela
                if (deleto) {
                    atualizarMateriasisDeUmExame();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um Material");
        }
    }

    public void botaoCadastrarExame() {
        this.setVisible(false);
        janelaPrincipal.internalFrameTabelasAdicionarUmExame =
            new JIFCTabelasAdicionarUMExame(nomeConvenio, handle_convenio);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameTabelasAdicionarUmExame);
        janelaPrincipal.internalFrameTabelasAdicionarUmExame.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameTabelasAdicionarUmExame.getWidth();
        int aIFrame = janelaPrincipal.internalFrameTabelasAdicionarUmExame.getHeight();

        janelaPrincipal.internalFrameTabelasAdicionarUmExame.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame
            / 2);
    }

    public void botaoEditarCoeficientes() {
        this.setVisible(false);
        String handle_exame = (String) JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 0);
        String nomeExame = (String) JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 3);
        janelaPrincipal.internalFrameTabelasEditarCoefExame =
            new JIFCTabelasEditarCoeficientesDeUmExame(handle_convenio, nomeConvenio, handle_exame, nomeExame);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameTabelasEditarCoefExame);

        String linhaCh1 = String.valueOf(jTable1.getSelectedRow());
        String textoCh1 = String.valueOf(jTable1.getValueAt(Integer.valueOf(linhaCh1), 4));
        JIFCTabelasEditarCoeficientesDeUmExame.jTFCofCh1.setText(textoCh1);

        String linhaCh2 = String.valueOf(jTable1.getSelectedRow());
        String textoCh2 = String.valueOf(jTable1.getValueAt(Integer.valueOf(linhaCh2), 5));
        JIFCTabelasEditarCoeficientesDeUmExame.jTFCofCh2.setText(textoCh2);

        String linhaFilme = String.valueOf(jTable1.getSelectedRow());
        String textoFilme = String.valueOf(jTable1.getValueAt(Integer.valueOf(linhaFilme), 6));
        JIFCTabelasEditarCoeficientesDeUmExame.jTFCofFilme.setText(textoFilme);

        String codigo = String.valueOf(jTable1.getValueAt(Integer.valueOf(linhaFilme), 2));
        JIFCTabelasEditarCoeficientesDeUmExame.jTFCodigo_exame.setText(codigo);

        String sinonimo = String.valueOf(jTable1.getValueAt(Integer.valueOf(linhaFilme), 1));
        JIFCTabelasEditarCoeficientesDeUmExame.jTFSinonimo.setText(sinonimo);

        janelaPrincipal.internalFrameTabelasEditarCoefExame.setVisible(true);

        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameTabelasEditarCoefExame.getWidth();
        int aIFrame = janelaPrincipal.internalFrameTabelasEditarCoefExame.getHeight();

        janelaPrincipal.internalFrameTabelasEditarCoefExame.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame
            / 2);

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBCancelar = new javax.swing.JButton();
        jBEditarCoeficientes = new javax.swing.JButton();
        jBRemoverExame = new javax.swing.JButton();
        jBAdicionarExame = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jCBModalidades = new javax.swing.JComboBox();
        jBRetornar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jBAdicionarMaterial = new javax.swing.JButton();
        jBRemoverMaterial = new javax.swing.JButton();
        jTFNomeDaTabela = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jBCalcularValorDoExame = new javax.swing.JButton();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jtfValorDoExame = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCBCalcularMateriais = new javax.swing.JComboBox();

        setTitle("Tabelas");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exame(s)",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "handle_exame", "Sinonimo", "Código", "Exame(s)", "CH1", "CH2", "Coef. Filme" }) {
            private static final long serialVersionUID = 1L;
            Class[] types = new Class[] { java.lang.String.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

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
        jScrollPane1.setViewportView(jTable1);

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/cancelar.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
        jBCancelar.setEnabled(false);
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

        jBEditarCoeficientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBEditarCoeficientes.setText("Editar Coef.");
        jBEditarCoeficientes.setEnabled(false);
        jBEditarCoeficientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarCoeficientesActionPerformed(evt);
            }
        });
        jBEditarCoeficientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBEditarCoeficientesKeyReleased(evt);
            }
        });

        jBRemoverExame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBRemoverExame.setText("Remover Exame");
        jBRemoverExame.setEnabled(false);
        jBRemoverExame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRemoverExameActionPerformed(evt);
            }
        });
        jBRemoverExame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBRemoverExameKeyReleased(evt);
            }
        });

        jBAdicionarExame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBAdicionarExame.setText("Adicionar Exame");
        jBAdicionarExame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAdicionarExameActionPerformed(evt);
            }
        });
        jBAdicionarExame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAdicionarExameKeyReleased(evt);
            }
        });

        jLabel3.setText("Modalidade");

        jCBModalidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBModalidadesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                jPanel1Layout
                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jBAdicionarExame, javax.swing.GroupLayout.DEFAULT_SIZE, 157,
                                                Short.MAX_VALUE)
                                            .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jBRemoverExame, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBEditarCoeficientes, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCBModalidades, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jCBModalidades, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jBAdicionarExame)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBEditarCoeficientes)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBRemoverExame)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBCancelar))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jBRetornar.setBackground(new java.awt.Color(113, 144, 224));
        jBRetornar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBRetornar.setText("Voltar");
        jBRetornar.setAlignmentY(0.0F);
        jBRetornar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBRetornar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRetornarActionPerformed(evt);
            }
        });
        jBRetornar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBRetornarKeyPressed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Material(s)",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Handle_material", "Material(s)", "Quantidade", "tabelaId" }) {
            private static final long serialVersionUID = 1L;
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
                java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable2FocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jBAdicionarMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBAdicionarMaterial.setText("Adicionar Material");
        jBAdicionarMaterial.setEnabled(false);
        jBAdicionarMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAdicionarMaterialActionPerformed(evt);
            }
        });
        jBAdicionarMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAdicionarMaterialKeyReleased(evt);
            }
        });

        jBRemoverMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBRemoverMaterial.setText("Remover Material");
        jBRemoverMaterial.setEnabled(false);
        jBRemoverMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRemoverMaterialActionPerformed(evt);
            }
        });
        jBRemoverMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBRemoverMaterialKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel2Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBAdicionarMaterial, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBRemoverMaterial, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel2Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel2Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel2Layout.createSequentialGroup().addComponent(jBAdicionarMaterial)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBRemoverMaterial).addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap()));

        jTFNomeDaTabela.setEditable(false);
        jTFNomeDaTabela.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFNomeDaTabela.setText("NOME DA TABELA");
        jTFNomeDaTabela.setFocusable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calcular Valor do Exame",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jBCalcularValorDoExame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/calcular.png"))); // NOI18N
        jBCalcularValorDoExame.setText("Calcular");
        jBCalcularValorDoExame.setEnabled(false);
        jBCalcularValorDoExame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCalcularValorDoExameActionPerformed(evt);
            }
        });
        jBCalcularValorDoExame.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBCalcularValorDoExameFocusLost(evt);
            }
        });

        jXDatePicker1.setEnabled(false);

        jtfValorDoExame.setEnabled(false);

        jLabel1.setText("Data");

        jLabel2.setText("Valor do Exame");

        jLabel4.setText("Calcular Materiais?");

        jCBCalcularMateriais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não", "Sim" }));
        jCBCalcularMateriais.setEnabled(false);

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
                            .addComponent(jBCalcularValorDoExame, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel3Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                                javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel3Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(
                                                jPanel3Layout.createSequentialGroup().addComponent(jLabel2)
                                                    .addGap(0, 0, Short.MAX_VALUE)).addComponent(jtfValorDoExame)))
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCBCalcularMateriais, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel3Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jtfValorDoExame, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jCBCalcularMateriais, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jBCalcularValorDoExame, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
            .addComponent(jBRetornar, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                layout
                    .createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTFNomeDaTabela, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jBRetornar, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFNomeDaTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE)));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 909) / 2, (screenSize.height - 587) / 2, 909, 587);
    }// </editor-fold>//GEN-END:initComponents

    private void jBRetornarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBRetornarActionPerformed
        /**
         * botaoRetornar(); }//GEN-LAST:event_jBRetornarActionPerformed
         */
        botaoRetornar();
    }

    private void jBRetornarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBRetornarKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoRetornar();
        }
    }// GEN-LAST:event_jBRetornarKeyPressed

    private void jBAdicionarExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAdicionarExameActionPerformed
        botaoCadastrarExame();
    }// GEN-LAST:event_jBAdicionarExameActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar(); // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }

    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBAdicionarMaterialActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAdicionarMaterialActionPerformed
        botaoCadastrarMaterial();
    }// GEN-LAST:event_jBAdicionarMaterialActionPerformed

    private void jBAdicionarMaterialKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAdicionarMaterialKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCadastrarMaterial();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBAdicionarMaterialKeyReleased

    private void jBRemoverExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBRemoverExameActionPerformed
        botaoApagarExame();
    }// GEN-LAST:event_jBRemoverExameActionPerformed

    private void jBRemoverExameKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBRemoverExameKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarExame();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBRemoverExameKeyReleased

    private void jTable2FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTable2FocusGained

    }// GEN-LAST:event_jTable2FocusGained

    private void jBRemoverMaterialActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBRemoverMaterialActionPerformed
        botaoApagarMaterial();
    }// GEN-LAST:event_jBRemoverMaterialActionPerformed

    private void jBRemoverMaterialKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBRemoverMaterialKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarMaterial();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBRemoverMaterialKeyReleased

    private void jBEditarCoeficientesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBEditarCoeficientesActionPerformed
        botaoEditarCoeficientes();
    }// GEN-LAST:event_jBEditarCoeficientesActionPerformed

    private void jBAdicionarExameKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAdicionarExameKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCadastrarExame();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBAdicionarExameKeyReleased

    private void jBEditarCoeficientesKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBEditarCoeficientesKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoEditarCoeficientes();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBEditarCoeficientesKeyReleased

    private void jBCalcularValorDoExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCalcularValorDoExameActionPerformed
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date data = null;
        Date dataSelecionada = jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data2 = dataFormatada.format(dataSelecionada);

        try {
            data = new java.sql.Date(format.parse(data2).getTime());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Preencha a data corretamente");
        }

        // calculando o valor e mostrando ao usuario
        if (jCBCalcularMateriais.getSelectedIndex() == 0) {
            CalculoValorDeExame calcularValorExame =
                new CalculoValorDeExame(Integer.valueOf(handle_convenio), Integer.valueOf(String.valueOf(jTable1
                    .getValueAt(jTable1.getSelectedRow(), 0))), data, false);
            jtfValorDoExame.setText(MetodosUteis.colocarZeroEmCampoReais(calcularValorExame.valor_correto_exame));
        } else {
            CalculoValorDeExame calcularValorExame =
                new CalculoValorDeExame(Integer.valueOf(handle_convenio), Integer.valueOf(String.valueOf(jTable1
                    .getValueAt(jTable1.getSelectedRow(), 0))), data, true);
            jtfValorDoExame.setText(MetodosUteis.colocarZeroEmCampoReais(calcularValorExame.valor_correto_exame));
        }

    }// GEN-LAST:event_jBCalcularValorDoExameActionPerformed

    private void jBCalcularValorDoExameFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBCalcularValorDoExameFocusLost
        jtfValorDoExame.setText(""); // TODO add your handling code here:
    }// GEN-LAST:event_jBCalcularValorDoExameFocusLost

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        atualizarMateriasisDeUmExame();
    }// GEN-LAST:event_jTable1MouseClicked

    private void jCBModalidadesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBModalidadesActionPerformed
        botaoCancelar();
        atualizarTabela1();// TODO add your handling code here:
    }// GEN-LAST:event_jCBModalidadesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAdicionarExame;
    public static javax.swing.JButton jBAdicionarMaterial;
    private javax.swing.JButton jBCalcularValorDoExame;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBEditarCoeficientes;
    private javax.swing.JButton jBRemoverExame;
    private javax.swing.JButton jBRemoverMaterial;
    public static javax.swing.JButton jBRetornar;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBCalcularMateriais;
    @SuppressWarnings("rawtypes")
    public static javax.swing.JComboBox jCBModalidades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTFNomeDaTabela;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JTextField jtfValorDoExame;
    // End of variables declaration//GEN-END:variables
}
