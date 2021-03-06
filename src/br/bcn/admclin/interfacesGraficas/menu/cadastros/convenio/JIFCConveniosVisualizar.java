/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCPacientesVisualizar.java
 *
 * Created on 21/08/2012, 11:43:47
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCConveniosVisualizar extends javax.swing.JInternalFrame {
    private static final long serialVersionUID = 1L;
    private Connection con = null;

    /** Creates new form JIFCPacientesVisualizar */
    public JIFCConveniosVisualizar() {
        initComponents();
        PreenchendoTabela();
        iniciarClasse();
        tirandoBarraDeTitulo();

    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void iniciarClasse() {
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
    }

    public void PreenchendoTabela() {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = CONVENIO.getConsultar(con);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                if (resultSet.getInt("handle_convenio") > 0) {
                    modelo.addRow(new String[] { Integer.toString(resultSet.getInt("handle_convenio")),
                        resultSet.getString("nome") });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void botaoNovo() {
        this.dispose();
        janelaPrincipal.internalFrameConvenioVisualizar = null;

        janelaPrincipal.internalFrameConvenios = new JIFCConvenios("novo", 0);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameConvenios);
        janelaPrincipal.internalFrameConvenios.setVisible(true);

        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameConvenios.getWidth();
        int aIFrame = janelaPrincipal.internalFrameConvenios.getHeight();

        janelaPrincipal.internalFrameConvenios.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBNovoRegistro = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Convênios",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "HANDLE_CONVENIO", "Nome" }) {
            private static final long serialVersionUID = 1L;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false };

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
        jBNovoRegistro.setText("Cadastrar novo Convênio");
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
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel2Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .addComponent(jBNovoRegistro, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout.createSequentialGroup().addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked

        int handle_convenio = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));

        this.dispose();
        janelaPrincipal.internalFrameConvenioVisualizar = null;
        janelaPrincipal.internalFrameConvenios = new JIFCConvenios("editar", handle_convenio);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameConvenios);
        janelaPrincipal.internalFrameConvenios.setVisible(true);

        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameConvenios.getWidth();
        int aIFrame = janelaPrincipal.internalFrameConvenios.getHeight();

        janelaPrincipal.internalFrameConvenios.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

    }// GEN-LAST:event_jTable1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBNovoRegistro;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
