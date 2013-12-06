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

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.model.Usuario;
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
 * @author BCN
 */
public class JIFCUsuariosVisualizar extends javax.swing.JInternalFrame {
    private static final long serialVersionUID = 1L;
    public static List<Usuario> listaUsuarios = new ArrayList<Usuario>();
    private Connection con = null;
    /** Creates new form JIFCPacientesVisualizar */
    public JIFCUsuariosVisualizar() {
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
        jTFPesquisaNome.setDocument(new DocumentoSemAspasEPorcento(64));
        //selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        //modificando tamanho das colunas da tabela
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(170);
        //colocando texto nas pesquisas
        jTable1.setRowHeight(20);
    }
    
    /**Atualiza a tabela e os objetos de acordo com o banco de dados, e em seguida dispara metodo cancelar!*/
    public void atualizarTabela(){
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = null;
        try {
            resultSet = USUARIOS.getConsultar(con);
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao consultar Usuários. Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        listaUsuarios.removeAll(listaUsuarios);            
        try{ 
            while(resultSet.next()){
                //colocando dados na tabela
                modelo.addRow(new String[] {Integer.toString(resultSet.getInt("USRID")),resultSet.getString("NM_USUARIO"),resultSet.getString("DS_UNIDADE")}); 
                //colocando dados nos objetos
                Usuario usuarioModel = new Usuario();
                usuarioModel.setUsrid(resultSet.getInt("USRID"));
                usuarioModel.setUsuario(resultSet.getString("NM_USUARIO"));
                usuarioModel.setDescricao(resultSet.getString("DS_UNIDADE"));
                usuarioModel.setSenha(resultSet.getString("PW_SENHA"));
                usuarioModel.setEstatus(resultSet.getString("FG_ESTATUS"));
                usuarioModel.setEmail(resultSet.getString("EMAIL"));
                usuarioModel.setEnvia_email(resultSet.getString("ENVIA_EMAILS"));
                usuarioModel.setImpressora_ficha(resultSet.getString("IMPRESSORA_FICHA"));
                usuarioModel.setImpressora_etiqueta_envelope(resultSet.getString("IMPRESSORA_ETIQUETA_ENVELOPE"));
                usuarioModel.setImpressora_nota_fiscal(resultSet.getString("IMPRESSORA_NOTA_FISCAL"));
                usuarioModel.setPasta_raiz(resultSet.getString("PASTA_RAIZ"));
                usuarioModel.setImpressora_codigo_de_barras(resultSet.getString("IMPRESSORA_CODIGO_DE_BARRAS"));
                listaUsuarios.add(usuarioModel);
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela.Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Usuários", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jLabel24.setEnabled(false);
    }
    
    
    public void botaoNovo(){
        this.dispose();
        janelaPrincipal.internalFrameUsuariosVisualizar = null;
        
        janelaPrincipal.internalFrameUsuarios = new JIFCUsuarios("novo", 0)  ;
                        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameUsuarios);
                        janelaPrincipal.internalFrameUsuarios.setVisible(true);
                        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
                        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
                        int lIFrame = janelaPrincipal.internalFrameUsuarios.getWidth();     
                        int aIFrame = janelaPrincipal.internalFrameUsuarios.getHeight();     

                        janelaPrincipal.internalFrameUsuarios.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
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

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemPesquisar.png"))); // NOI18N

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
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTFPesquisaNome)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuario Id", "Usuário", "Unidade"
            }
        ) {
            private static final long serialVersionUID = 1L;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Cadastrar novo Usuário");
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBNovoRegistro)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            while(i<listaUsuarios.size()){
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] {Integer.toString(listaUsuarios.get(i).getUsrid()),listaUsuarios.get(i).getUsuario(),listaUsuarios.get(i).getDescricao()}); 
                i++;
            }
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Usuários", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
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
        while(i<listaUsuarios.size()){
            if(listaUsuarios.get(i).getUsuario().startsWith(jTFPesquisaNome.getText().toUpperCase())){
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.addRow(new String[] {Integer.toString(listaUsuarios.get(i).getUsrid()),listaUsuarios.get(i).getUsuario(),listaUsuarios.get(i).getDescricao()});
            }
        i++;
        }
        //ativando ou nao de acordo com pesquisa
        if("".equals(jTFPesquisaNome.getText())){
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todos os Usuários", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jLabel24.setEnabled(false);
        }else{
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Usuários(s) encontrado(s) pela Pesquisa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
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

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        if (jTable1.getSelectedRow() >= 0) {
            int usuarioId = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));

        janelaPrincipal.internalFrameUsuarios = new JIFCUsuarios("editar", usuarioId)  ;
                        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameUsuarios);
                        janelaPrincipal.internalFrameUsuarios.setVisible(true);
                        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
                        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
                        int lIFrame = janelaPrincipal.internalFrameUsuarios.getWidth();     
                        int aIFrame = janelaPrincipal.internalFrameUsuarios.getHeight();     

                        janelaPrincipal.internalFrameUsuarios.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );

                        
        this.dispose();
        janelaPrincipal.internalFrameUsuariosVisualizar = null;
        }
        
        
    }//GEN-LAST:event_jTable1MouseReleased

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
