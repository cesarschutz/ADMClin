/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.cadastros.convenio.internalFrames;

import br.bcn.admclin.dao.Conexao;
import janelaPrincipal.janelaPrincipal;

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

import menu.cadastros.convenio.dao.conveniosDAO;

/**
 *
 * @author BCN
 */
public class JIFGruposConveniosVisualizar extends javax.swing.JInternalFrame {
    private Connection con = null;
    /**
     * Creates new form JIFGruposConveniosVisualizar
     */
    public JIFGruposConveniosVisualizar() {
        initComponents();
        tirandoBarraDeTitulo();
        iniciarClasse();
        PreenchendoTabela();
    }

    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void iniciarClasse(){
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
        //selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        
        jTable1.getColumnModel().getColumn( 1 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 1 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 1 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 1 ).setMinWidth( 0 );
    }
    
    public void PreenchendoTabela(){
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = conveniosDAO.getConsultarGruposDeConvenios(con);
        try{
            while(resultSet.next()){
                //colocando dados na tabela
                modelo.addRow(new String[] {Integer.toString(resultSet.getInt("grupo_id")), Integer.toString(resultSet.getInt("gera_arquivo_texto")), resultSet.getString("nome")});
            } 
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }
    
    public void botaoNovo(){
        this.dispose();
        janelaPrincipal.internalFrameGruposConveniosVisualizar = null;
        
        janelaPrincipal.internalFrameGruposDeConvenios = new JIFGruposConvenios();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameGruposDeConvenios);
        janelaPrincipal.internalFrameGruposDeConvenios.setVisible(true);
        
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameGruposDeConvenios.getWidth();
        int aIFrame = janelaPrincipal.internalFrameGruposDeConvenios.getHeight();
        
        janelaPrincipal.internalFrameGruposDeConvenios.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBNovoRegistro = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Grupos de Convênios", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "GrupoConvenioId", "geraArquivoTexto", "Nome"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Cadastrar novo Grupo de Convênios");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addComponent(jBNovoRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBNovoRegistro))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBNovoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNovoRegistroActionPerformed
        botaoNovo();
    }//GEN-LAST:event_jBNovoRegistroActionPerformed

    private void jBNovoRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBNovoRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoNovo();
        }
    }//GEN-LAST:event_jBNovoRegistroKeyReleased

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        this.dispose();
        janelaPrincipal.internalFrameGruposConveniosVisualizar = null;
        
        int grupo_id = Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
        int gera_arquivo_texto = Integer.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString());
        String nome = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
        
        janelaPrincipal.internalFrameGruposDeConvenios = new JIFGruposConvenios(grupo_id, gera_arquivo_texto, nome);
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameGruposDeConvenios);
        janelaPrincipal.internalFrameGruposDeConvenios.setVisible(true);
        
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameGruposDeConvenios.getWidth();
        int aIFrame = janelaPrincipal.internalFrameGruposDeConvenios.getHeight();
        
        janelaPrincipal.internalFrameGruposDeConvenios.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }//GEN-LAST:event_jTable1MouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBNovoRegistro;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
