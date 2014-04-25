/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCPacientesVisualizar.java
 *
 * Created on 21/08/2012, 11:43:47
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import br.bcn.admclin.dao.dbris.AREAS_ATENDIMENTO;
import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFAreasAtendimentoVisualizar extends javax.swing.JInternalFrame {
    private static final long serialVersionUID = 1L;

    /** Creates new form JIFCPacientesVisualizar */
    public JIFAreasAtendimentoVisualizar() {
        initComponents();
        PreenchendoTabela();
        iniciarClasse();
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void iniciarClasse() {
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
        ArrayList<Areas_atendimento> lista = AREAS_ATENDIMENTO.getConsultar();
        for (int i = 0; i < lista.size() ; i++) {
            modelo.addRow(new String[] { String.valueOf(lista.get(i).getId_areas_atendimento()), lista.get(i).getNome() });
        }
    }

    public void botaoNovo() {
        this.dispose();
        janelaPrincipal.internalFrameAreasDeAtendimento = null;

        janelaPrincipal.internalFrameAreasDeAtendimento = new JIFAreasAtendimento("novo", 0, null);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAreasDeAtendimento);
        janelaPrincipal.internalFrameAreasDeAtendimento.setVisible(true);

        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameAreasDeAtendimento.getWidth();
        int aIFrame = janelaPrincipal.internalFrameAreasDeAtendimento.getHeight();

        janelaPrincipal.internalFrameAreasDeAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
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

        jPanel2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Todas as \u00E1reas de Atendimento", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, null));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "intervalo diario id", "Nome" }) {
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
        jBNovoRegistro.setText("Cadastrar nova Área de Atendimento");
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
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
        this.dispose();
        janelaPrincipal.internalFrameAreasDeAtendimentoVisualizar = null;

        int id_areas_atendimento = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        String nomeArea = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);

        janelaPrincipal.internalFrameAreasDeAtendimento = new JIFAreasAtendimento("editar", id_areas_atendimento, nomeArea);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAreasDeAtendimento);
        janelaPrincipal.internalFrameAreasDeAtendimento.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameAreasDeAtendimento.getWidth();
        int aIFrame = janelaPrincipal.internalFrameAreasDeAtendimento.getHeight();

        janelaPrincipal.internalFrameAreasDeAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2); // TODO
                                                                                                                      // add
                                                                                                                      // your
                                                                                                                      // handling
                                                                                                                      // code
                                                                                                                      // here:
    }// GEN-LAST:event_jTable1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBNovoRegistro;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
