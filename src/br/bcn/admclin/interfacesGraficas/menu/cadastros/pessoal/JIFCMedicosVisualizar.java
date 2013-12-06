/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCPacientesVisualizar.java
 *
 * Created on 21/08/2012, 11:43:47
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.MEDICOS;
import br.bcn.admclin.dao.model.Medicos;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCMedicosVisualizar extends javax.swing.JInternalFrame {
    private static final long serialVersionUID = 1L;
    public static List<Medicos> listaMedicos = new ArrayList<Medicos>();
    private Connection con = null;

    /** Creates new form JIFCPacientesVisualizar */
    public JIFCMedicosVisualizar() {
        initComponents();
        iniciarClasse();
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void iniciarClasse() {
        jTFPesquisaNome.setDocument(new DocumentoSemAspasEPorcento(64));
        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        // modificando tamanho das colunas da tabela
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(420);
        // colocando texto nas pesquisas
        // aumentando tamanho da linha
        jTable1.setRowHeight(20);
    }

    /** Atualiza a tabela e os objetos de acordo com o banco de dados. */
    public void atualizarTabela() {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        String[] nomesParaPesquisar = null;
        nomesParaPesquisar = MetodosUteis.formatarParaPesquisarNome(jTFPesquisaNome.getText());

        if (nomesParaPesquisar != null) {
            String sql = null;
            if (nomesParaPesquisar.length == 1)
                sql = nomesParaPesquisar[0] + "%";
            if (nomesParaPesquisar.length == 2)
                sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%";
            if (nomesParaPesquisar.length == 3)
                sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%" + nomesParaPesquisar[2] + "%";
            if (nomesParaPesquisar.length == 4)
                sql =
                    nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%" + nomesParaPesquisar[2] + "%"
                        + nomesParaPesquisar[3] + "%";

            ResultSet resultSet = MEDICOS.getConsultar(con, sql);

            listaMedicos.removeAll(listaMedicos);
            try {
                while (resultSet.next()) {
                    if (resultSet.getInt("medicoid") > 0) {
                        // colocando dados na tabela
                        modelo.addRow(new String[] { Integer.toString(resultSet.getInt("medicoid")),
                            resultSet.getString("nome"), resultSet.getString("nascimento") });
                        // colocando dados nos objetos
                        Medicos medicosModelo = new Medicos();
                        medicosModelo.setMedicoId(resultSet.getInt("medicoid"));
                        medicosModelo.setEmId(resultSet.getInt("emid"));
                        medicosModelo.setUsuarioId(resultSet.getInt("usuarioid"));
                        medicosModelo.setDat(resultSet.getDate("dat"));
                        medicosModelo.setNome(resultSet.getString("nome"));
                        medicosModelo.setCrm(resultSet.getString("crm"));
                        medicosModelo.setUfcrm(resultSet.getString("ufcrm"));
                        medicosModelo.setNascimento(resultSet.getString("nascimento"));
                        medicosModelo.setTelefone(resultSet.getString("telefone"));
                        medicosModelo.setCelular(resultSet.getString("celular"));
                        medicosModelo.setEndereco(resultSet.getString("endereco"));
                        medicosModelo.setBairro(resultSet.getString("bairro"));
                        medicosModelo.setCep(resultSet.getString("cep"));
                        medicosModelo.setCidade(resultSet.getString("cidade"));
                        medicosModelo.setUf(resultSet.getString("uf"));
                        medicosModelo.setEmail(resultSet.getString("email"));
                        listaMedicos.add(medicosModelo);
                    }

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador",
                    "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            Conexao.fechaConexao(con);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Médicos",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(false);
        }
    }

    public void botaoNovo() {
        this.dispose();
        janelaPrincipal.internalFrameMedicosVisualizar = null;

        janelaPrincipal.internalFrameMedicos = new JIFCMedicos("novo", 0);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameMedicos);
        janelaPrincipal.internalFrameMedicos.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameMedicos.getWidth();
        int aIFrame = janelaPrincipal.internalFrameMedicos.getHeight();

        janelaPrincipal.internalFrameMedicos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTFPesquisaNome = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBNovoRegistro = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Médicos",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFPesquisaNome.setForeground(new java.awt.Color(153, 153, 153));
        jTFPesquisaNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFPesquisaNomeKeyPressed(evt);
            }
        });

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemPesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel3Layout.createSequentialGroup()
                    .addComponent(jTFPesquisaNome, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                    .addGap(18, 18, 18).addComponent(jLabel24).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addComponent(jTFPesquisaNome));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Medico ID", "Nome", "Nascimento" }) {
            private static final long serialVersionUID = 1L;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false };

            @SuppressWarnings("rawtypes")
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

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Cadastrar novo Médico");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel2Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel2Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBNovoRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
                    .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel2Layout
                    .createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBNovoRegistro)
                    .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBNovoRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBNovoRegistroActionPerformed
        botaoNovo();
    }// GEN-LAST:event_jBNovoRegistroActionPerformed

    private void jBNovoRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBNovoRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoNovo();
        }
    }// GEN-LAST:event_jBNovoRegistroKeyReleased

    private void jTFPesquisaNomeKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            atualizarTabela();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jTFPesquisaNomeKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() == -1) {
            jTable1.addRowSelectionInterval(0, 0);
        }

        int medicoId = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));

        janelaPrincipal.internalFrameMedicos = new JIFCMedicos("editar", medicoId);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameMedicos);
        janelaPrincipal.internalFrameMedicos.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameMedicos.getWidth();
        int aIFrame = janelaPrincipal.internalFrameMedicos.getHeight();

        janelaPrincipal.internalFrameMedicos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

        this.dispose();
        janelaPrincipal.internalFrameMedicosVisualizar = null; // TODO add your handling code here:
    }// GEN-LAST:event_jTable1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBNovoRegistro;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTFPesquisaNome;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
