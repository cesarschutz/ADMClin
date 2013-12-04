package menu.atendimentos.agenda.internalFrames;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.bcn.admclin.menu.atendimentos.agenda.pinturaDeUmaAgenda.ColorirLinhaJTableInicial;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFQuatroAgendas.java
 *
 * Created on 06/08/2012, 18:04:52
 */
/**
 * 
 * @author Cesar Schutz
 */
public class JIFQuatroAgendas extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;

    /** Creates new form JIFQuatroAgendas */
    public JIFQuatroAgendas() {
        initComponents();
        tirandoBordaEBarraDeTitulo();
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        // definindo tamaho das colunas
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable2.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable4.getColumnModel().getColumn(1).setPreferredWidth(250);

        // aumentando tamanho da linha
        jTable1.setRowHeight(30);
        jTable2.setRowHeight(30);
        jTable3.setRowHeight(30);
        jTable4.setRowHeight(30);

        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // pintando as linhas
        TableCellRenderer tcr = new ColorirLinhaJTableInicial();
        TableColumn columnTable1 = jTable1.getColumnModel().getColumn(0);
        TableColumn columnTable11 = jTable1.getColumnModel().getColumn(1);
        TableColumn columnTable12 = jTable1.getColumnModel().getColumn(2);
        columnTable1.setCellRenderer(tcr);
        columnTable11.setCellRenderer(tcr);
        columnTable12.setCellRenderer(tcr);

        TableColumn columnTable2 = jTable2.getColumnModel().getColumn(0);
        TableColumn columnTable21 = jTable2.getColumnModel().getColumn(1);
        TableColumn columnTable22 = jTable2.getColumnModel().getColumn(2);
        columnTable2.setCellRenderer(tcr);
        columnTable21.setCellRenderer(tcr);
        columnTable22.setCellRenderer(tcr);

        TableColumn columnTable3 = jTable3.getColumnModel().getColumn(0);
        TableColumn columnTable31 = jTable3.getColumnModel().getColumn(1);
        TableColumn columnTable32 = jTable3.getColumnModel().getColumn(2);
        columnTable3.setCellRenderer(tcr);
        columnTable31.setCellRenderer(tcr);
        columnTable32.setCellRenderer(tcr);

        TableColumn columnTable4 = jTable4.getColumnModel().getColumn(0);
        TableColumn columnTable41 = jTable4.getColumnModel().getColumn(1);
        TableColumn columnTable42 = jTable4.getColumnModel().getColumn(2);
        columnTable4.setCellRenderer(tcr);
        columnTable41.setCellRenderer(tcr);
        columnTable42.setCellRenderer(tcr);

    }

    public void tirandoBordaEBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "serial" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "08:00", null, null },
            { "08:30", "Vanete Lima", null }, { "09:00", null, null }, { "09:30", "Erica", null },
            { "10:00", null, null }, { "10:30", null, null }, { "11:00", "Cesar Fagundes", null },
            { "11:30", null, null }, { null, null, null }, { "14:00", null, null },
            { "14:30", "Theodoro Duarte", null }, { "15:00", null, null }, { "15:30", null, null },
            { "16:00", null, null }, { "16:30", null, null }, { "17:00", null, null }, { "17:30", null, null } },
            new String[] { "Horario", "Paciente", "Status" }) {
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false };

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField1.setText("jTextField1");

        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField2.setText("jTextField2");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "08:00", null, null },
            { "08:30", "Cristiano Schoroder", null }, { "09:00", null, null }, { "09:30", "Theodoro Duarte", null },
            { "10:00", null, null }, { "10:30", "Felipe Cunha", null }, { "11:00", null, null },
            { "11:30", null, null }, { null, null, null }, { "14:00", "Joao Neto", null }, { "14:30", null, null },
            { "15:00", null, null }, { "15:30", "Amanda Vargas", null }, { "16:00", null, null },
            { "16:30", null, null }, { "17:00", "Vanete Lima", null }, { "17:30", "Cristiano Martins", null } },
            new String[] { "Horario", "Paciente", "Status" }) {
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false };

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTextField3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField3.setText("jTextField3");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "08:00", null, null },
            { "08:30", "Cesar Fagundes", "" }, { "09:00", "Felipe Cunha", null }, { "09:30", "Joao Neto", null },
            { "10:00", null, null }, { "10:30", null, null }, { "11:00", null, null }, { "11:30", null, null },
            { null, null, null }, { "14:00", null, null }, { "14:30", "Dalila de Oliveira", null },
            { "15:00", null, null }, { "15:30", "Felipe Cunha", null }, { "16:00", null, null },
            { "16:30", null, null }, { "17:00", "Theodoro Duate", null }, { "17:30", null, null } }, new String[] {
            "Horario", "Paciente", "Status" }) {
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false };

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jTextField4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField4.setText("jTextField4");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "08:00", "Vanete Lima", null },
            { "08:30", null, null }, { "09:00", "Cesar Fagundes", null }, { "09:30", "Dalila de Oliveira", null },
            { "10:00", null, null }, { "10:30", "Felipe Cunha", null }, { "11:00", null, null },
            { "11:30", null, null }, { null, null, null }, { "14:00", "Joao Neto", null },
            { "14:30", "Theodoro Duarte", null }, { "15:00", null, null }, { "15:30", "Maria Lucia", null },
            { "16:00", null, null }, { "16:30", "Cristiano Schoroder", null }, { "17:00", null, null },
            { "17:30", "Pedro Quiroga", null } }, new String[] { "Horario", "Paciente", "Status" }) {
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false };

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addContainerGap()
                .addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane4, 0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
                .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531,
                                        Short.MAX_VALUE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 531,
                                        Short.MAX_VALUE))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 531,
                                        Short.MAX_VALUE))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 531,
                                        Short.MAX_VALUE))).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    public static javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField2;
    public static javax.swing.JTextField jTextField3;
    public static javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
