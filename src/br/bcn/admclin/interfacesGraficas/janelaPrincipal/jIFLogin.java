/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.janelaPrincipal;

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.DADOS_EMPRESA;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Usuario;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author CeSaR
 */
public class jIFLogin extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    public static String usuario;
    public static String fgEstatus = null;
    private Connection con = null;
    
    /**
     * Creates new form jIFLogin
     */
    public jIFLogin() {
        initComponents();
        jTFUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFSenha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jLabel4.setVisible(false);
        tirandoBarraDeTitulo();
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    @SuppressWarnings("deprecation")
    public void botaoEntrar(){
        
        jTFUsuario.setText(jTFUsuario.getText().toUpperCase());
        con = Conexao.fazConexao();
        //antes de fazer o login vamos pegar o modelo de impressao
        try {
            if (!con.isClosed()) {
              getModeloImpressao();  
            }
        } catch (Exception e) {
            System.exit(0);
        }
        
        
        //aqui vamos fazer o login
        Usuario usuarioModel = new Usuario();
        usuarioModel.setUsuario(jTFUsuario.getText());
        usuarioModel.setSenha(jTFSenha.getText());
        boolean existe = false;
        try {
            con = Conexao.fazConexao();
            existe = USUARIOS.getLogin(con, usuarioModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao fazer login. Procure o administrador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
            if(existe){
                
                //aqui vai o if paraliberar o menu deacordocomo usuario
                this.dispose();
                janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();



                //administrador tem acesso a tudo, se for financeiro bloqueia cadastros, e qualquer outro bloqueia cadastros e financeiro
                if(!"A".equals(USUARIOS.statusUsuario) && !"F".equals(USUARIOS.statusUsuario)){
                    //diferente de administrador e financeiro, bloqueia cadastros e financeiro
                    janelaPrincipal.jMCadastros.setVisible(false);
                    janelaPrincipal.jMFinanceiro.setVisible(false);
                }else if("F".equals(USUARIOS.statusUsuario)){
                    //financeiro bloqueia cadastros
                    janelaPrincipal.jMCadastros.setVisible(false);
                }
                
                
            }else{
                jLabel4.setVisible(true);
                jTFSenha.setText("");
            }
    }
    
    /**
     * metodo que busca o tipo de modelo de impressao da empresa
     */
    public void getModeloImpressao(){
        ResultSet resultSet = null;
        try {
            con = br.bcn.admclin.dao.dbris.Conexao.fazConexao();
            resultSet = DADOS_EMPRESA.getConsultar(con, 1);
            while(resultSet.next()){
                janelaPrincipal.modeloDeImpressao = resultSet.getInt("modelo_impressao");
            }
            //se nao for um modelo existente, o programa nao abre!
            if (janelaPrincipal.modeloDeImpressao != 1 && janelaPrincipal.modeloDeImpressao != 2 && janelaPrincipal.modeloDeImpressao != 3 && janelaPrincipal.modeloDeImpressao != 4 && janelaPrincipal.modeloDeImpressao != 5) {
                JOptionPane.showMessageDialog(null, "Modelo de Impressão incorreto. Procure o Administrador.", "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Erro ao verificar modelo de Impressão. Procure o Administrador.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }finally{
            Conexao.fechaConexao(con);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFUsuario = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64),null,0);
        jLabel2 = new javax.swing.JLabel();
        jTFSenha = new javax.swing.JPasswordField(new DocumentoSemAspasEPorcento(64),null,0);
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Usuário:");

        jLabel2.setText("Senha:");

        jTFSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFSenhaFocusLost(evt);
            }
        });
        jTFSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFSenhaKeyPressed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemLogin.png"))); // NOI18N

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Usuário ou Senha inválido");

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setForeground(new java.awt.Color(250, 250, 250));
        jLabel3.setText("7.1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(5, 5, 5)
                                .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel2)
                                .addGap(8, 8, 8)
                                .addComponent(jTFSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1))
                    .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2))
                    .addComponent(jTFSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFSenhaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSenhaFocusLost
        jLabel4.setVisible(false);
    }//GEN-LAST:event_jTFSenhaFocusLost

    private void jTFSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSenhaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
            SwingWorker<?, ?> worker = new SwingWorker<Object, Object>(){
                @Override
                protected Object doInBackground() throws Exception {
                    botaoEntrar();
                    return null;
                }
                @Override
                protected void done() {
                    janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
                }
            };
            worker.execute();
            
        }
    }//GEN-LAST:event_jTFSenhaKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jTFSenha;
    private javax.swing.JTextField jTFUsuario;
    // End of variables declaration//GEN-END:variables
}
