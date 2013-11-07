/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCPacientesVisualizar.java
 *
 * Created on 21/08/2012, 11:43:47
 */
package menu.cadastros.pessoal.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;

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

import menu.cadastros.pessoal.dao.responsaveis_tecnicosDAO;
import menu.cadastros.pessoal.model.responsaveis_tecnicosMODEL;

/**
 *
 * @author BCN
 */
public class JIFCResponsaveisTecnicosVisualizar extends javax.swing.JInternalFrame {
    public static List<responsaveis_tecnicosMODEL> listaResponsaveisTecnicos = new ArrayList<responsaveis_tecnicosMODEL>();
    private Connection con = null;
    /** Creates new form JIFCPacientesVisualizar */
    public JIFCResponsaveisTecnicosVisualizar() {
        initComponents();
        atualizarTabela();
        iniciarClasse();
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void iniciarClasse(){
        jTFPesquisaNome.setDocument(new documentoSemAspasEPorcento(64));
        //selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        //modificando tamanho das colunas da tabela
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(300);
        //colocando texto nas pesquisas
    }
    
    /**Atualiza a tabela e os objetos de acordo com o banco de dados.*/
    public void atualizarTabela(){
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = responsaveis_tecnicosDAO.getConsultar(con);
        listaResponsaveisTecnicos.removeAll(listaResponsaveisTecnicos);
        try{
            while(resultSet.next()){
                //colocando dados na tabela
                modelo.addRow(new String[] {Integer.toString(resultSet.getInt("rtid")),resultSet.getString("nome"),resultSet.getString("conselho")}); 
                //colocando dados nos objetos
                responsaveis_tecnicosMODEL responsavelTecnicoModel = new responsaveis_tecnicosMODEL();
                responsavelTecnicoModel.setRtId(resultSet.getInt("rtid"));
                responsavelTecnicoModel.setUsuarioId(resultSet.getInt("usuarioid"));
                responsavelTecnicoModel.setData(resultSet.getDate("dat"));
                responsavelTecnicoModel.setNome(resultSet.getString("nome"));
                responsavelTecnicoModel.setConselho(resultSet.getString("conselho"));
                responsavelTecnicoModel.setRegistroConselho(resultSet.getString("registro_conselho"));
                responsavelTecnicoModel.setCpf(resultSet.getString("cpf"));
                responsavelTecnicoModel.setUf(resultSet.getString("uf"));
                responsavelTecnicoModel.setRegistroAns(resultSet.getString("registro_ans"));
                listaResponsaveisTecnicos.add(responsavelTecnicoModel);
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Responsáveis Técnicos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jLabel24.setEnabled(false);
    }
  
    
    public void botaoNovo(){
        this.dispose();
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar = null;
        
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos = new JIFCResponsaveisTecnicos("novo", 0)  ;
                        janelaPrincipal.janelaPrincipal.jDesktopPane1.add(janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos);
                        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.setVisible(true);
                        int lDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
                        int aDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
                        int lIFrame = janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.getWidth();     
                        int aIFrame = janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.getHeight();     

                        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Pacientes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

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

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/imagemPesquisar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jTFPesquisaNome, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
            .addComponent(jTFPesquisaNome)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RT ID", "Nome", "Conselho"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Cadastrar novo Responsável Técnico");
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
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBNovoRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBNovoRegistro)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFPesquisaNomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFPesquisaNomeFocusGained

    }//GEN-LAST:event_jTFPesquisaNomeFocusGained

    private void jTFPesquisaNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFPesquisaNomeFocusLost
        String verificar = jTFPesquisaNome.getText().replace(" ", "");
        if( "".equals(verificar)){
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            int i =0;
            while(i<listaResponsaveisTecnicos.size()){
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] {Integer.toString(listaResponsaveisTecnicos.get(i).getRtId()),listaResponsaveisTecnicos.get(i).getNome(),listaResponsaveisTecnicos.get(i).getConselho()}); 
                i++;
            }
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Responsáveis Técnicos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
    }//GEN-LAST:event_jTFPesquisaNomeFocusLost

    private void jTFPesquisaNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFPesquisaNomeKeyReleased
        jTFPesquisaNome.setText(jTFPesquisaNome.getText().toUpperCase());
        //limpa a tabela
        jTable1.getSelectionModel().clearSelection();
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        //coloca os objetos que tiverem aquele prefixo
        int i =0;
        while(i<listaResponsaveisTecnicos.size()){
            if(listaResponsaveisTecnicos.get(i).getNome().startsWith(jTFPesquisaNome.getText().toUpperCase())){
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.addRow(new String[] {Integer.toString(listaResponsaveisTecnicos.get(i).getRtId()),listaResponsaveisTecnicos.get(i).getNome(),listaResponsaveisTecnicos.get(i).getConselho()}); 
            }
        i++;
        }
        //ativando ou nao de acordo com pesquisa
        if("".equals(jTFPesquisaNome.getText())){
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Responsáveis Técnicos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(false);
        }else{
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Responsáveis Técnicos(s) encontrado(s) pela Pesquisa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(true);
        }
    }//GEN-LAST:event_jTFPesquisaNomeKeyReleased

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained

    }//GEN-LAST:event_jTable1FocusGained

    private void jBNovoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNovoRegistroActionPerformed
botaoNovo();
    }//GEN-LAST:event_jBNovoRegistroActionPerformed

    private void jBNovoRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBNovoRegistroKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoNovo();  
}
    }//GEN-LAST:event_jBNovoRegistroKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if(jTable1.getSelectedRow()==-1){
           jTable1.addRowSelectionInterval(0, 0);
        }
        
        int responsavelTecnicoId = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        
        
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos = new JIFCResponsaveisTecnicos("editar", responsavelTecnicoId)  ;
                        janelaPrincipal.janelaPrincipal.jDesktopPane1.add(janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos);
                        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.setVisible(true);
                        int lDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
                        int aDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
                        int lIFrame = janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.getWidth();     
                        int aIFrame = janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.getHeight();     

                        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );

                        
        this.dispose();
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar = null;        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

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
