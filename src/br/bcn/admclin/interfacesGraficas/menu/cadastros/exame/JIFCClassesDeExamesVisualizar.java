/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCPacientesVisualizar.java
 *
 * Created on 21/08/2012, 11:43:47
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.exame;

import br.bcn.admclin.ClasseAuxiliares.documentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.TB_CLASSESDEEXAMES;
import br.bcn.admclin.dao.model.Tb_ClassesDeExames;

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
public class JIFCClassesDeExamesVisualizar extends javax.swing.JInternalFrame {
    private static final long serialVersionUID = 1L;
    public static List<Tb_ClassesDeExames> listaClassesDeExames = new ArrayList<Tb_ClassesDeExames>();
    private Connection con = null;

    /** Creates new form JIFCPacientesVisualizar */
    public JIFCClassesDeExamesVisualizar() {
        initComponents();
        atualizarTabela();
        iniciarClasse();
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void iniciarClasse() {
        jTable1.setRowHeight(20);
        jTFPesquisaNome.setDocument(new documentoSemAspasEPorcento(64));
        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        // colocando texto nas pesquisas
    }

    /** Atualiza a tabela e os objetos de acordo com o banco de dados, e em seguida dispara metodo cancelar! */
    public void atualizarTabela() {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = TB_CLASSESDEEXAMES.getConsultar(con);
        listaClassesDeExames.removeAll(listaClassesDeExames);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                modelo.addRow(new String[] { Integer.toString(resultSet.getInt("cod")), resultSet.getString("nome") });
                // colocando dados nos objetos
                Tb_ClassesDeExames classeDeExameModelo = new Tb_ClassesDeExames();
                classeDeExameModelo.setCod(resultSet.getInt("cod"));
                classeDeExameModelo.setUsuarioid(resultSet.getInt("usuarioid"));
                classeDeExameModelo.setData(resultSet.getDate("dat"));
                classeDeExameModelo.setDescricao(resultSet.getString("nome"));
                classeDeExameModelo.setModIdx(resultSet.getInt("modidx"));
                listaClassesDeExames.add(classeDeExameModelo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador" + e,
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todas as Classes de Exames",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jLabel24.setEnabled(false);
    }

    public void botaoNovo() {
        this.dispose();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar = null;

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames = new JIFCClassesDeExames("novo", 0);
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.add(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames);
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.setVisible(true);
        int lDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.getWidth();
        int aIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.getHeight();

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
            - aIFrame / 2);
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todas as Classes de Exames",
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

        }, new String[] { "Classe de Exame Id", "Descrição" }) {
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
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Cadastrar nova Classe de Exame");
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBNovoRegistro)
                    .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFPesquisaNomeFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeFocusGained

    }// GEN-LAST:event_jTFPesquisaNomeFocusGained

    private void jTFPesquisaNomeFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeFocusLost
        String verificar = jTFPesquisaNome.getText().replace(" ", "");
        if ("".equals(verificar)) {
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            int i = 0;
            while (i < listaClassesDeExames.size()) {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] { Integer.toString(listaClassesDeExames.get(i).getCod()),
                    listaClassesDeExames.get(i).getDescricao() });
                i++;
            }
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos as Classes de Exames",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
    }// GEN-LAST:event_jTFPesquisaNomeFocusLost

    private void jTFPesquisaNomeKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPesquisaNomeKeyReleased
        jTFPesquisaNome.setText(jTFPesquisaNome.getText().toUpperCase());
        // limpa a tabela
        jTable1.getSelectionModel().clearSelection();
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        // coloca os objetos que tiverem aquele prefixo
        int i = 0;
        while (i < listaClassesDeExames.size()) {
            if (listaClassesDeExames.get(i).getDescricao().startsWith(jTFPesquisaNome.getText().toUpperCase())) {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] { Integer.toString(listaClassesDeExames.get(i).getCod()),
                    listaClassesDeExames.get(i).getDescricao() });
            }
            i++;
        }
        // ativando ou nao de acordo com pesquisa
        if ("".equals(jTFPesquisaNome.getText())) {
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todas as Classes de Exames",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(false);
        } else {
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Classe(s) de Exame(s) encontrado(s) pela Pesquisa", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(true);
        }
    }// GEN-LAST:event_jTFPesquisaNomeKeyReleased

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTable1FocusGained
        if (jTable1.getSelectedRow() == -1) {
            jTable1.addRowSelectionInterval(0, 0);
        }

        int ClasseDeExameId = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames =
            new JIFCClassesDeExames("editar", ClasseDeExameId);
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.add(br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames);
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.setVisible(true);
        int lDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.getWidth();
        int aIFrame = br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.getHeight();

        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2
            - aIFrame / 2);

        this.dispose();
        br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar = null;
    }// GEN-LAST:event_jTable1FocusGained

    private void jBNovoRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBNovoRegistroActionPerformed
        botaoNovo();
    }// GEN-LAST:event_jBNovoRegistroActionPerformed

    private void jBNovoRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBNovoRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoNovo();
        }
    }// GEN-LAST:event_jBNovoRegistroKeyReleased

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
