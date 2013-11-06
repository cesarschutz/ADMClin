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

import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.documentoSomenteLetras;
import conexao.Conexao;
import janelaPrincipal.janelaPrincipal;
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
import menu.cadastros.pessoal.dao.pacientesDAO;
import menu.cadastros.pessoal.model.pacientesMODEL;

/**
 *
 * @author BCN
 */
public class JIFCPacientesVisualizar extends javax.swing.JInternalFrame {
    public static List<pacientesMODEL> listaPacientes = new ArrayList<pacientesMODEL>();
    private Connection con = null;
    /** Creates new form JIFCPacientesVisualizar */
    public JIFCPacientesVisualizar() {
        initComponents();
        iniciarClasse();
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void iniciarClasse(){
    
        //selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //aumentando tamanho da linha
        jTable1.setRowHeight(20);
        //deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        //modificando tamanho das colunas da tabela
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(420);
    }
    
    /**Atualiza a tabela e os objetos de acordo com o banco de dados.*/
    public void atualizarTabela(){
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        String[] nomesParaPesquisar = null;
        nomesParaPesquisar = MetodosUteis.formatarParaPesquisarNome(jTFPesquisaNome.getText());
        
        if(nomesParaPesquisar != null){
            String sql = null;
            if(nomesParaPesquisar.length == 1) sql = nomesParaPesquisar[0] + "%";
            if(nomesParaPesquisar.length == 2) sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%";
            if(nomesParaPesquisar.length == 3) sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%" + nomesParaPesquisar[2] + "%";
            if(nomesParaPesquisar.length == 4) sql = nomesParaPesquisar[0] + "%" + nomesParaPesquisar[1] + "%" + nomesParaPesquisar[2] + "%"+ nomesParaPesquisar[3] + "%";
            ResultSet resultSet = pacientesDAO.getConsultar(con,sql);
        listaPacientes.removeAll(listaPacientes);
        try{
            while(resultSet.next()){
                //colocando dados na tabela
                modelo.addRow(new String[] {Integer.toString(resultSet.getInt("handle_paciente")),resultSet.getString("nome"),resultSet.getString("nascimento")}); 
                //colocando dados nos objetos
                pacientesMODEL pacienteModelo = new pacientesMODEL();
                pacienteModelo.setHandle_paciente(resultSet.getInt("handle_paciente"));
                pacienteModelo.setUsuarioId(resultSet.getInt("usuarioid"));
                pacienteModelo.setData(resultSet.getDate("dat"));
                pacienteModelo.setNome(resultSet.getString("nome"));
                pacienteModelo.setCpf(resultSet.getString("cpf"));
                pacienteModelo.setNascimento(resultSet.getString("nascimento"));
                pacienteModelo.setResponsavel(resultSet.getString("responsavel"));
                pacienteModelo.setCpfResponsavel(resultSet.getString("cpfresponsavel"));
                pacienteModelo.setSexo(resultSet.getString("sexo"));
                pacienteModelo.setPeso(resultSet.getString("peso"));
                pacienteModelo.setAltura(resultSet.getString("altura"));
                pacienteModelo.setTelefone(resultSet.getString("telefone"));
                pacienteModelo.setCelular(resultSet.getString("celular"));
                pacienteModelo.setEndereco(resultSet.getString("endereco"));
                pacienteModelo.setBairro(resultSet.getString("bairro"));
                pacienteModelo.setCep(resultSet.getString("cep"));
                pacienteModelo.setCidade(resultSet.getString("cidade"));
                pacienteModelo.setUf(resultSet.getString("uf"));
                pacienteModelo.setRg(resultSet.getString("rg"));
                pacienteModelo.setProfissao(resultSet.getString("profissao"));
                pacienteModelo.setEmail(resultSet.getString("email"));
                pacienteModelo.setCor(resultSet.getString("cor"));
                pacienteModelo.setEstadoCivil(resultSet.getString("estadocivil"));
                pacienteModelo.setObs(resultSet.getString("obs"));
                pacienteModelo.setTelefone_responsavel(resultSet.getString("telefone_responsavel"));
                listaPacientes.add(pacienteModelo);
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador","ATENÇÃO!",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
        
    }
    }
    
    public void botaoNovo(){
        this.dispose();
        janelaPrincipal.internalFramePacienteVisualizar = null;
        
        janelaPrincipal.internalFramePaciente = new JIFCPacientes("novo", 0)  ;
                        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFramePaciente);
                        janelaPrincipal.internalFramePaciente.setVisible(true);
                        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
                        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
                        int lIFrame = janelaPrincipal.internalFramePaciente.getWidth();     
                        int aIFrame = janelaPrincipal.internalFramePaciente.getHeight();     

                        janelaPrincipal.internalFramePaciente.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
                        
                        JIFCPacientes.jBAtualizarRegistro.setVisible(false);
                        JIFCPacientes.jBApagarRegistro.setVisible(false);
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
        jTFPesquisaNome = new javax.swing.JTextField(new documentoSomenteLetras(64), null, 0);
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jBNovoRegistro = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pacientes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jTFPesquisaNome.setForeground(new java.awt.Color(153, 153, 153));
        jTFPesquisaNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFPesquisaNomeKeyPressed(evt);
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
                "HANDLE_PACIENTE", "Nome", "Nascimento"
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
        jScrollPane2.setViewportView(jTable1);

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Cadastrar novo Paciente");
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
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
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
       
        int handle_paciente = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        
        
        janelaPrincipal.internalFramePaciente = new JIFCPacientes("editar", handle_paciente)  ;
                        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFramePaciente);
                        janelaPrincipal.internalFramePaciente.setVisible(true);
                        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
                        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
                        int lIFrame = janelaPrincipal.internalFramePaciente.getWidth();     
                        int aIFrame = janelaPrincipal.internalFramePaciente.getHeight();     

                        janelaPrincipal.internalFramePaciente.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );

                        
        this.dispose();
        janelaPrincipal.internalFramePacienteVisualizar = null;        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTFPesquisaNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFPesquisaNomeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
            atualizarTabela();  
        }
    }//GEN-LAST:event_jTFPesquisaNomeKeyPressed

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
