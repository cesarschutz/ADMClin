/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCTabelasAdicionarUmMaterialAUmExame.java
 *
 * Created on 11/07/2012, 13:52:45
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

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

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.MATERIAIS;
import br.bcn.admclin.dao.TABELAS;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.model.Tabelas;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCTabelasAdicionarUmMaterialAUmExame extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private Connection con = null;
    public String handle_convenio = null;
    public List<Integer> listaCodMateriais = new ArrayList<Integer>();
    java.sql.Date dataDeHojeEmVariavelDate = null;
    String handle_exame = null;
    String nomeExame = null;

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

    /** Creates new form JIFCTabelasAdicionarUmMaterialAUmExame */
    public JIFCTabelasAdicionarUmMaterialAUmExame(String nomeTabela, String handle_convenio, String handle_exame,
        String nomeExame) {
        initComponents();
        this.handle_convenio = handle_convenio;
        this.handle_exame = handle_exame;
        this.nomeExame = nomeExame;
        iniciarClasse();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Exame: " + nomeExame,
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    @SuppressWarnings("unchecked")
    public void iniciarClasse() {
        // preenchendo os materiais
        con = Conexao.fazConexao();
        ResultSet resultSet = MATERIAIS.getConsultar(con);
        listaCodMateriais.removeAll(listaCodMateriais);
        jCBMateriais.addItem("Selecione um Material");
        listaCodMateriais.add(0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("handle_material") > 0) {
                    jCBMateriais.addItem(resultSet.getString("nome"));
                    listaCodMateriais.add(resultSet.getInt("handle_material"));
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Materiais. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
        jTFQuantidade.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    }

    public void botaoCancelar() {
        this.dispose();
        // janelaPrincipal.internalFrameTabelasAdicionarMaterialAoExame = null;
        janelaPrincipal.internalFrameTabelas.setVisible(true);
    }

    public void botaoOk() {

        boolean materialSelecionado = false;
        boolean qtdSelecionada = false;

        if (jCBMateriais.getSelectedIndex() != 0) {
            materialSelecionado = true;
        }

        if (!"".equals(jTFQuantidade.getText())) {
            qtdSelecionada = true;
        }

        if (materialSelecionado && qtdSelecionada) {
            con = Conexao.fazConexao();
            Tabelas tabelaModelo = new Tabelas();
            tabelaModelo.setUsuarioId(USUARIOS.usrId);
            tabelaModelo.setDat(dataDeHojeEmVariavelDate);
            tabelaModelo.sethandle_convenio(Integer.valueOf(handle_convenio));

            tabelaModelo.sethandle_exame(Integer.valueOf(handle_exame));

            tabelaModelo.setCofFilme(Double.valueOf(String.valueOf(
                JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 6)).replace(",", ".")));

            tabelaModelo.setCofCh1(Double.valueOf(String.valueOf(
                JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 4)).replace(",", ".")));
            tabelaModelo.setCofCh2(Double.valueOf(String.valueOf(
                JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 5)).replace(",", ".")));

            tabelaModelo.setCod_exame(String.valueOf(
                JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 2)).replace(",", "."));
            tabelaModelo.setSinonimo(String.valueOf(
                JIFCTabelas.jTable1.getValueAt(JIFCTabelas.jTable1.getSelectedRow(), 1)).replace(",", "."));
            String handle_material = String.valueOf(listaCodMateriais.get(jCBMateriais.getSelectedIndex()));
            tabelaModelo.sethandle_material(Integer.valueOf(handle_material));
            tabelaModelo.setQtdMaterial(Integer.valueOf(jTFQuantidade.getText()));
            tabelaModelo.setVAI_MATERIAIS_POR_PADRAO(-1);
            boolean cadastro = TABELAS.cadastrarExameAUmaTabela(con, tabelaModelo);
            Conexao.fechaConexao(con);

            if (cadastro) {
                janelaPrincipal.internalFrameTabelas.atualizarMateriasisDeUmExame();
                botaoCancelar();
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
        jBOk = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTFQuantidade = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jCBMateriais = new javax.swing.JComboBox();

        setTitle("Adicionar Material");

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

        jBOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBOk.setText("Salvar");
        jBOk.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBOkActionPerformed(evt);
            }
        });
        jBOk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBOkKeyReleased(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "aaaa",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel2.setText("Material");

        jTFQuantidade.setText("1");

        jLabel1.setText("Quantidade");

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
                            .addComponent(jLabel2)
                            .addComponent(jCBMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 354,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTFQuantidade).addComponent(jLabel1))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2).addComponent(jLabel1))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBMateriais, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(
                layout.createSequentialGroup().addComponent(jBCancelar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBOk)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jBOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                            Short.MAX_VALUE)
                        .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 464) / 2, (screenSize.height - 170) / 2, 464, 170);
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar(); // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBOkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBOkActionPerformed
        botaoOk(); // TODO add your handling code here:
    }// GEN-LAST:event_jBOkActionPerformed

    private void jBOkKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBOkKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoOk();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBOkKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBOk;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBMateriais;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFQuantidade;
    // End of variables declaration//GEN-END:variables
}
