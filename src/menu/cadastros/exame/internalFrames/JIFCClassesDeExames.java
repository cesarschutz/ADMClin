package menu.cadastros.exame.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.TB_CLASSESDEEXAMES;
import br.bcn.admclin.model.Tb_ClassesDeExames;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author BCN
 */
public class JIFCClassesDeExames extends javax.swing.JInternalFrame {

    String novoOuEditar;
    int classeDeExameId;
    
    public List<Integer> listaCodModalidade = new ArrayList<Integer>();

    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    
    
    public void pegandoDataDoSistema(){
    //pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format( hoje.getTime() );
        try {
            dataDeHojeEmVariavelDate = new java.sql.Date(format.parse(dataDeHoje).getTime());
        } catch (ParseException ex) {
            
        }
    }
    /**
     * Creates new form JIModelo
     */
    public JIFCClassesDeExames(String novoOuEditar, int classeDeExameId) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.classeDeExameId = classeDeExameId;
        iniciarClasse();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    /**Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela.*/
    public void iniciarClasse(){
        //preenchendo as modalidades
        con = Conexao.fazConexao();
        ResultSet resultSet = TB_CLASSESDEEXAMES.getConsultarModalidades(con);
        listaCodModalidade.removeAll(listaCodModalidade);
        listaCodModalidade.add(0);
        try{
            while(resultSet.next()){
              jCBModalidade.addItem(resultSet.getString("modalidade"));  
              listaCodModalidade.add(resultSet.getInt("modidx"));
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Modalidades. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            Conexao.fechaConexao(con);
        }
        
        //colocando maximo de caracteres nos jtextfield
        jTDescricao.setDocument(new documentoSemAspasEPorcento(64));

        if("novo".equals(novoOuEditar)){
            jBAtualizarRegistro.setVisible(false);
            jBApagarRegistro.setVisible(false);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de nova Classe de Exame", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        }else{
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Classe de Exame", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jBSalvarRegistro.setVisible(false);
            //procura o objeto com o cod igual ao cod da tabela e atualiza os jtextfield
            int cont = 0;
                while(cont<JIFCClassesDeExamesVisualizar.listaClassesDeExames.size()){
                    int codObjetos = JIFCClassesDeExamesVisualizar.listaClassesDeExames.get(cont).getCod();
                    if(classeDeExameId == codObjetos){
                         jTDescricao.setText(JIFCClassesDeExamesVisualizar.listaClassesDeExames.get(cont).getDescricao());
                         jCBModalidade.setSelectedIndex(JIFCClassesDeExamesVisualizar.listaClassesDeExames.get(cont).getModIdx());
                    }
                    cont++;
                }
        }
        
        
        
         
   } //ok

    /**Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com os objetos.*/
    public void botaoCancelar(){
        this.dispose();
        janelaPrincipal.janelaPrincipal.internalFrameClasseDeExames = null;
        
        janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar = new JIFCClassesDeExamesVisualizar()  ;
        janelaPrincipal.janelaPrincipal.jDesktopPane1.add(janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar);
        janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar.getWidth();     
        int aIFrame = janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar.getHeight();     

        janelaPrincipal.janelaPrincipal.internalFrameClasseDeExamesVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }

    /**Salva uma nova classe de exame na banco de dados.*/
    public void botaoSalvarRegistro(){
        
        boolean modalidadePrenchida = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBModalidade, jTFMensagemParaUsuario);
        boolean descricaoPreenchida = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
        
        if(descricaoPreenchida && modalidadePrenchida){
          //fazendo um if para verificar se descricao ou referencia ja existem
            con = Conexao.fazConexao();
            Tb_ClassesDeExames classeDeExameModelo = new Tb_ClassesDeExames();
            classeDeExameModelo.setDescricao(jTDescricao.getText().toUpperCase());
            boolean existe = TB_CLASSESDEEXAMES.getConsultarParaSalvarNovoRegistro(con, classeDeExameModelo);
            Conexao.fechaConexao(con);
            if(TB_CLASSESDEEXAMES.conseguiuConsulta){
                if(existe){
                    JOptionPane.showMessageDialog(null, "Classe de Exame já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }else{
                    //fazer a inserção no banco
                    con = Conexao.fazConexao();
                    classeDeExameModelo.setDescricao(jTDescricao.getText().toUpperCase());
                    classeDeExameModelo.setUsuarioid(USUARIOS.usrId);
                    classeDeExameModelo.setData(dataDeHojeEmVariavelDate);
                    classeDeExameModelo.setModIdx(listaCodModalidade.get(jCBModalidade.getSelectedIndex()));
                    boolean cadastro = TB_CLASSESDEEXAMES.setCadastrar(con, classeDeExameModelo);
                    Conexao.fechaConexao(con);
                    //atualiza tabela
                    if(cadastro){
                        botaoCancelar();
                    }
                } 
            }
        }
    } //ok
    /**Atualiza uma classe de exame no banco de dados.*/
    public void botaoAtualizarRegistro(){
        
        boolean modalidadePrenchida = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBModalidade, jTFMensagemParaUsuario);
        boolean descricaoPreenchida = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
        
        if(descricaoPreenchida && modalidadePrenchida){
            //fazendo um if para verificar se descricao ou referencia ja existem
            con = Conexao.fazConexao();
            Tb_ClassesDeExames classeDeExameModel = new Tb_ClassesDeExames();
            classeDeExameModel.setDescricao(jTDescricao.getText().toUpperCase());
            classeDeExameModel.setCod(classeDeExameId);
            boolean existe = TB_CLASSESDEEXAMES.getConsultarParaAtualizarRegistro(con, classeDeExameModel);
            Conexao.fechaConexao(con);
            if(TB_CLASSESDEEXAMES.conseguiuConsulta){
                if(existe){
                    JOptionPane.showMessageDialog(null, "Classe de Exame já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }else{
                    con = Conexao.fazConexao();
                    classeDeExameModel.setUsuarioid(USUARIOS.usrId);
                    classeDeExameModel.setData(dataDeHojeEmVariavelDate);
                    classeDeExameModel.setModIdx(listaCodModalidade.get(jCBModalidade.getSelectedIndex()));
                    boolean atualizo = TB_CLASSESDEEXAMES.setUpdate(con, classeDeExameModel);
                    Conexao.fechaConexao(con);
                    if(atualizo){
                        //dexando janela como no inicio
                        botaoCancelar();
                    }
                }
            }
        }
    }
    /**Apaga a classe de exame selecionada do banco de dados.*/
    public void botaoApagarRegistro(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar essa Classe De Exame?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            con = Conexao.fazConexao();
            boolean utilizada = TB_CLASSESDEEXAMES.getConsultarSeClasseEstaSendoUtilizada(con, classeDeExameId);
            Conexao.fechaConexao(con);
            if(TB_CLASSESDEEXAMES.conseguiuConsulta){
                if(utilizada){
                    JOptionPane.showMessageDialog(null, "Esta Classe de Exame  não pode ser deletada pois está sendo utilizada por algum Exame","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }else{
                    //fazer o delete de acordo com o codigo
                    Tb_ClassesDeExames classeDeExameModel = new Tb_ClassesDeExames();
                    classeDeExameModel.setCod(classeDeExameId);
                    con = Conexao.fazConexao();
                    boolean deleto = TB_CLASSESDEEXAMES.setDeletar(con, classeDeExameModel);
                    Conexao.fechaConexao(con);
                    //atualizar tabela
                    if(deleto){
                    botaoCancelar();
                    }
                }
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jTDescricao = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCBModalidade = new javax.swing.JComboBox();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        setTitle("Cadastro de Classes de Exames");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados da Classe de Exame", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTDescricao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTDescricaoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTDescricaoFocusLost(evt);
            }
        });
        jTDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTDescricaoKeyReleased(evt);
            }
        });

        jLabel5.setText("Nome");

        jLabel6.setText("Modalidade");

        jCBModalidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione uma Modalidade" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(jCBModalidade, 0, 429, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBModalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBAtualizarRegistro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBAtualizarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarRegistroActionPerformed(evt);
            }
        });
        jBAtualizarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAtualizarRegistroFocusLost(evt);
            }
        });
        jBAtualizarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyPressed(evt);
            }
        });

        jBApagarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/deletar.png"))); // NOI18N
        jBApagarRegistro.setText("Apagar");
        jBApagarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBApagarRegistro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBApagarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBApagarRegistroActionPerformed(evt);
            }
        });
        jBApagarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBApagarRegistroFocusLost(evt);
            }
        });
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyPressed(evt);
            }
        });

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBSalvarRegistro.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBSalvarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarRegistroActionPerformed(evt);
            }
        });
        jBSalvarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarRegistroFocusLost(evt);
            }
        });
        jBSalvarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyPressed(evt);
            }
        });

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/cancelar.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBCancelarKeyPressed(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBAtualizarRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBApagarRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSalvarRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBAtualizarRegistro)
                    .addComponent(jBApagarRegistro)
                    .addComponent(jBSalvarRegistro)
                    .addComponent(jBCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-477)/2, (screenSize.height-284)/2, 477, 284);
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoCancelar(); 
        } 
    }//GEN-LAST:event_jBCancelarKeyPressed

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }//GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoAtualizarRegistro(); 
        } 
    }//GEN-LAST:event_jBAtualizarRegistroKeyPressed

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }//GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoSalvarRegistro(); 
        } 
    }//GEN-LAST:event_jBSalvarRegistroKeyPressed

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }//GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBApagarRegistroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoApagarRegistro();
        } 
    }//GEN-LAST:event_jBApagarRegistroKeyPressed

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTDescricao.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jBApagarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBApagarRegistroFocusLost
        
    }//GEN-LAST:event_jBApagarRegistroFocusLost

    private void jTDescricaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTDescricaoKeyReleased
        jTDescricao.setText(jTDescricao.getText().toUpperCase());
    }//GEN-LAST:event_jTDescricaoKeyReleased

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTDescricao.setBackground(new java.awt.Color(255,255,255));     // TODO add your handling code here:
    }//GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jTDescricaoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTDescricaoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
    }//GEN-LAST:event_jTDescricaoFocusGained

    private void jTDescricaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTDescricaoFocusLost
 jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
  if(ok){
     jTDescricao.setBackground(new java.awt.Color(255,255,255));
 }
    }//GEN-LAST:event_jTDescricaoFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    private javax.swing.JComboBox jCBModalidade;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTDescricao;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    // End of variables declaration//GEN-END:variables
}
