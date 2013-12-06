/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FazendoNovo.java
 *
 * Created on 16/07/2012, 17:31:25
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

import br.bcn.admclin.ClasseAuxiliares.documentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.jTextFieldDinheiroReaisCom5CasasDecimais;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.EXAMES;
import br.bcn.admclin.dao.TABELAS;
import br.bcn.admclin.dao.model.Tabelas;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCTabelasAdicionarUMExame extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public String nomeConvenio = null;
    public String handle_convenio = null;
    public List<Integer> listaHandle_Exame = new ArrayList<Integer>();
    private Connection con = null;
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

    /** Creates new form FazendoNovo */
    public JIFCTabelasAdicionarUMExame(String nomeConvenio, String handle_convenio) {
        initComponents();
        this.nomeConvenio = nomeConvenio;
        this.handle_convenio = handle_convenio;
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabela: " + nomeConvenio,
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        iniciarClasse();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    @SuppressWarnings("unchecked")
    public void iniciarClasse() {
        jCBExames.requestFocusInWindow();
        jTFCofCh1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFCofCh2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFCofFilme.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        // preenchendo os Exames no ComboBox
        con = Conexao.fazConexao();
        String modalidade = String.valueOf(JIFCTabelas.jCBModalidades.getSelectedItem());
        ResultSet resultSet = EXAMES.getConsultarPorModalidade(con, modalidade);
        listaHandle_Exame.removeAll(listaHandle_Exame);
        jCBExames.addItem("Selecione um Exame");
        listaHandle_Exame.add(0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("handle_exame") > 0) {
                    jCBExames.addItem(resultSet.getString("nome"));
                    listaHandle_Exame.add(resultSet.getInt("handle_exame"));
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Exames. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }

    }

    public void botaoOkExame() {

        boolean exameExiste = false;
        int j = 0;
        while (j < JIFCTabelas.jTable1.getRowCount()) {
            int codExameACadastrar = listaHandle_Exame.get(jCBExames.getSelectedIndex());
            String codTabela = String.valueOf(JIFCTabelas.jTable1.getValueAt(j, 0));
            int codTabelaCorreto = Integer.valueOf(codTabela);
            if (codExameACadastrar == codTabelaCorreto) {
                exameExiste = true;
            }
            j++;
        }

        if (exameExiste) {
            JOptionPane.showMessageDialog(null, "Este exame já está cadastrado nesta Tabela.");
        } else {

            boolean exameSelecionadoOk = false;
            if (jCBExames.getSelectedIndex() != 0) {
                exameSelecionadoOk = true;
            } else {
                JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Selecione um Exame");
            }

            if (exameSelecionadoOk) {
                boolean cadastro = false;
                con = Conexao.fazConexao();
                Tabelas tabelaModelo = new Tabelas();
                tabelaModelo.setUsuarioId(USUARIOS.usrId);
                tabelaModelo.setDat(dataDeHojeEmVariavelDate);
                tabelaModelo.sethandle_convenio(Integer.valueOf(handle_convenio));
                tabelaModelo.sethandle_exame(listaHandle_Exame.get(jCBExames.getSelectedIndex()));
                tabelaModelo.setSinonimo(jTFSinonimo.getText());
                tabelaModelo.setCofFilme(Double.valueOf(jTFCofFilme.getText().replace(",", ".")));
                tabelaModelo.setCofCh1(Double.valueOf(jTFCofCh1.getText().replace(",", ".")));
                tabelaModelo.setCofCh2(Double.valueOf(jTFCofCh2.getText().replace(",", ".")));
                tabelaModelo.sethandle_material(0);
                tabelaModelo.setQtdMaterial(0);
                tabelaModelo.setCod_exame(jTFCodigo_exame.getText());
                tabelaModelo.setVAI_MATERIAIS_POR_PADRAO(jCBVaiMateriaisPorPadrao.getSelectedIndex());
                cadastro = TABELAS.cadastrarExameAUmaTabela(con, tabelaModelo);
                Conexao.fechaConexao(con);

                if (cadastro) {
                    botaoCancelar();
                    DefaultTableModel modelo = (DefaultTableModel) JIFCTabelas.jTable1.getModel();
                    modelo.addRow(new String[] { String.valueOf(listaHandle_Exame.get(jCBExames.getSelectedIndex())),
                        jTFSinonimo.getText(), jTFCodigo_exame.getText(), String.valueOf(jCBExames.getSelectedItem()),
                        jTFCofCh1.getText(), jTFCofCh2.getText(), jTFCofFilme.getText() });

                }
            } else {

            }
        }
    }

    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameTabelasAdicionarUmExame = null;
        janelaPrincipal.internalFrameTabelas.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCBExames = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTFCofCh2 = new jTextFieldDinheiroReaisCom5CasasDecimais(new DecimalFormat("0.00000")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(11);
            }
        };
        jTFCofCh1 = new jTextFieldDinheiroReaisCom5CasasDecimais(new DecimalFormat("0.00000")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(11);
            }
        };
        jLabel3 = new javax.swing.JLabel();
        jTFCofFilme = new jTextFieldDinheiroReaisCom5CasasDecimais(new DecimalFormat("0.00000")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(11);
            }
        };
        jTFCodigo_exame = new javax.swing.JTextField(new documentoSemAspasEPorcento(16), null, 0);
        jLabel4 = new javax.swing.JLabel();
        jTFSinonimo = new javax.swing.JTextField(new documentoSemAspasEPorcento(100), null, 0);
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCBVaiMateriaisPorPadrao = new javax.swing.JComboBox();
        jBOkExame = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exame",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jCBExames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBExamesActionPerformed(evt);
            }
        });

        jLabel2.setText("CH 2");

        jLabel1.setText("CH 1");

        jTFCofCh2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCofCh2FocusGained(evt);
            }
        });

        jTFCofCh1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCofCh1FocusGained(evt);
            }
        });

        jLabel3.setText("Cof. Filme");

        jTFCofFilme.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCofFilmeFocusGained(evt);
            }
        });

        jLabel4.setText("Código");

        jLabel5.setText("Sinônimo");

        jLabel6.setText("Colocar Materiais por padrão?");

        jCBVaiMateriaisPorPadrao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não", "Sim" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBExames, 0, 345, Short.MAX_VALUE)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel1))
                                    .addGap(10, 10, 10)
                                    .addGroup(
                                        jPanel1Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTFCofCh1, javax.swing.GroupLayout.DEFAULT_SIZE, 287,
                                                Short.MAX_VALUE)
                                            .addComponent(jTFCofFilme, javax.swing.GroupLayout.DEFAULT_SIZE, 287,
                                                Short.MAX_VALUE)
                                            .addComponent(jTFCofCh2, javax.swing.GroupLayout.DEFAULT_SIZE, 287,
                                                Short.MAX_VALUE)))
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4).addComponent(jLabel5))
                                    .addGap(16, 16, 16)
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTFSinonimo).addComponent(jTFCodigo_exame)))
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCBVaiMateriaisPorPadrao, javax.swing.GroupLayout.PREFERRED_SIZE, 94,
                                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addComponent(jCBExames, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jCBVaiMateriaisPorPadrao, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCodigo_exame, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFSinonimo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                    .addGap(18, 18, 18)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCofCh1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCofCh2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCofFilme, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))));

        jBOkExame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBOkExame.setText("Salvar");
        jBOkExame.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBOkExame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBOkExameActionPerformed(evt);
            }
        });
        jBOkExame.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBOkExameFocusLost(evt);
            }
        });
        jBOkExame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBOkExameKeyReleased(evt);
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
        jBCancelar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBCancelarFocusLost(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBCancelarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup().addComponent(jBCancelar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBOkExame)
                    .addContainerGap(185, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jBOkExame, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(53, Short.MAX_VALUE)));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 393) / 2, (screenSize.height - 353) / 2, 393, 353);
    }// </editor-fold>//GEN-END:initComponents

    private void jBOkExameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBOkExameActionPerformed

        botaoOkExame();
    }// GEN-LAST:event_jBOkExameActionPerformed

    private void jBOkExameFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBOkExameFocusLost
        jTFCofCh2.setBackground(new java.awt.Color(255, 255, 255));
        jTFCofFilme.setBackground(new java.awt.Color(255, 255, 255));
        jTFCofCh1.setBackground(new java.awt.Color(255, 255, 255)); // TODO add your handling code here:
                                                                    // jTFCofCh2.setBackground(new java.awt.Color(255,
                                                                    // 255, 255)); // TODO add your handling code here:
                                                                    // jTFCofFilme.setBackground(new java.awt.Color(255,
                                                                    // 255, 255)); // TODO add your handling code here:
                                                                    // jTFMensagemParaUsuario.setText("");
                                                                    // }//GEN-LAST:event_jBOkExameFocusLost
    }

    private void jBOkExameKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBOkExameKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoOkExame();
        }
    }// GEN-LAST:event_jBOkExameKeyReleased

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBCancelarFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarFocusLost

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jTFCofCh1FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCofCh1FocusGained

    }// GEN-LAST:event_jTFCofCh1FocusGained

    private void jTFCofCh2FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCofCh2FocusGained

    }// GEN-LAST:event_jTFCofCh2FocusGained

    private void jTFCofFilmeFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCofFilmeFocusGained

    }// GEN-LAST:event_jTFCofFilmeFocusGained

    private void jCBExamesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCBExamesActionPerformed
        String exame = String.valueOf(jCBExames.getSelectedItem());
        if (!"Selecione um Exame".equals(exame)) {
            jTFSinonimo.setText(exame);
        }

    }// GEN-LAST:event_jCBExamesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBOkExame;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBExames;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBVaiMateriaisPorPadrao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFCodigo_exame;
    private javax.swing.JTextField jTFCofCh1;
    private javax.swing.JTextField jTFCofCh2;
    javax.swing.JTextField jTFCofFilme;
    private javax.swing.JTextField jTFSinonimo;
    // End of variables declaration//GEN-END:variables
}
