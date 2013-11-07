/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCMedicos.java
 *
 * Created on 07/05/2012, 13:52:58
 */
package menu.cadastros.pessoal.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.documentoSomenteLetras;
import br.bcn.admclin.dao.Conexao;

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

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import janelaPrincipal.janelaPrincipal;
import menu.cadastros.pessoal.dao.especialidades_medicasDAO;
import menu.cadastros.pessoal.dao.medicosDAO;
import menu.cadastros.pessoal.dao.usuariosDAO;
import menu.cadastros.pessoal.model.medicosMODEL;

/**
 *
 * @author BCN
 */
public class JIFCMedicos extends javax.swing.JInternalFrame {
    String novoOuEditar;
    int medicoId = 0;
    //criando lista para objetos do banco
    public List<Integer> listaCodEspecialidadesMedicas = new ArrayList<Integer>();
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
    /** Creates new form JIFCMedicos */
    public JIFCMedicos(String novoOuEditar, int medicoId) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.medicoId = medicoId;
        iniciarClasse();
        pegandoDataDoSistema();
        if(novoOuEditar == "novo"){
            jBAtualizarRegistro.setVisible(false);
            jBApagarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Médico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }else{
            jBSalvarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Médico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            //preenchendo os campos

                //procura o objeto com o cod igual ao cod da tabela e atualiza os jtextfield
                int indexDoArray=0;
                int cont = 0;
                while(cont<JIFCMedicosVisualizar.listaMedicos.size()){
                    int codObjetos = JIFCMedicosVisualizar.listaMedicos.get(cont).getMedicoId();
                    if(medicoId == codObjetos){
                        jTFNome.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getNome());
                        jTFCRM.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCrm());
                        jCBUfCRM.setSelectedItem(JIFCMedicosVisualizar.listaMedicos.get(cont).getUfcrm());
                        jTFNascimento.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getNascimento());
                        jTFTelefone.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getTelefone());
                        jTFCelular.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCelular());
                        jTFEndereco.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getEndereco());
                        jTFBairro.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getBairro());
                        jTFCep.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCep());
                        jTFCidade.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCidade());
                        jTFEmail.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getEmail());
                        jCBUf.setSelectedItem(JIFCMedicosVisualizar.listaMedicos.get(cont).getUf());
                        for(int i=0;i<listaCodEspecialidadesMedicas.size();i++){
                            if(listaCodEspecialidadesMedicas.get(i)==JIFCMedicosVisualizar.listaMedicos.get(cont).getEmId()){
                                indexDoArray = i;
                            }
                        }
                        jCBEspecialidadeMedica.setSelectedIndex(indexDoArray);
                    }
                    cont++;
                }
                        //colocando foco na referencia
                jTFNome.requestFocusInWindow();
        }
        tirandoBarraDeTitulo();
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }

    /**Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela.*/
    public void iniciarClasse(){
        jTFEmail.setDocument(new documentoSemAspasEPorcento(64));
        jTFEndereco.setDocument(new documentoSemAspasEPorcento(80));
        jTFBairro.setDocument(new documentoSemAspasEPorcento(32));
        jTFCidade.setDocument(new documentoSemAspasEPorcento(32));
        //preenchendo as Especialidades Médicas
        con = Conexao.fazConexao();
        ResultSet resultSet = especialidades_medicasDAO.getConsultar(con);
        listaCodEspecialidadesMedicas.removeAll(listaCodEspecialidadesMedicas);
        jCBEspecialidadeMedica.addItem("");  
        listaCodEspecialidadesMedicas.add(0);
        try{
            while(resultSet.next()){
              jCBEspecialidadeMedica.addItem(resultSet.getString("descricao"));
              listaCodEspecialidadesMedicas.add(resultSet.getInt("emid"));
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Especialidades Médicas. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            Conexao.fechaConexao(con);
        }
    }
    /**Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com os objetos.*/
    public void botaoCancelar(){
        this.dispose();
        janelaPrincipal.internalFrameMedicos = null;
        
        janelaPrincipal.internalFrameMedicosVisualizar = new JIFCMedicosVisualizar()  ;
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameMedicosVisualizar);
        janelaPrincipal.internalFrameMedicosVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = janelaPrincipal.internalFrameMedicosVisualizar.getWidth();     
        int aIFrame = janelaPrincipal.internalFrameMedicosVisualizar.getHeight();     

        janelaPrincipal.internalFrameMedicosVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
        
    }
    
    
    public boolean verificarSetudoFoiPreenchidoCorretamente(){
        boolean nomeOk = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);
        
        boolean nascimentoOk = true;
        if(!"  /  /    ".equals(jTFNascimento.getText())){
            nascimentoOk = MetodosUteis.verificarSeDataDeNascimentoEValida(jTFNascimento, jTFMensagemParaUsuario);
        }
        
        boolean emailOk = true;
        if(!"".equals(jTFEmail.getText())){
            nascimentoOk = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);
        }
        
        if (emailOk && nascimentoOk && nomeOk) {
            return true;
        }else{
            return false;
        }
    }
    /**Salva um novo Médico na banco de dados.*/
    public void botaoSalvarRegistro(){
        
        
        if(verificarSetudoFoiPreenchidoCorretamente()){
            //salva no banco
            medicosMODEL medicosMODEL = new medicosMODEL();
                   //fazer a inserção no banco
                    con = Conexao.fazConexao();
                    medicosMODEL.setUsuarioId(usuariosDAO.usrId);
                    medicosMODEL.setDat(dataDeHojeEmVariavelDate);
                    medicosMODEL.setNome(jTFNome.getText());
                    medicosMODEL.setCrm(jTFCRM.getText());
                    medicosMODEL.setUfcrm((String)jCBUfCRM.getSelectedItem());
                    medicosMODEL.setDat(dataDeHojeEmVariavelDate);
                    medicosMODEL.setNascimento(jTFNascimento.getText());
                    medicosMODEL.setEmail(jTFEmail.getText());
                    medicosMODEL.setTelefone(jTFTelefone.getText());
                    medicosMODEL.setCelular(jTFCelular.getText());
                    medicosMODEL.setEndereco(jTFEndereco.getText());
                    medicosMODEL.setBairro(jTFBairro.getText());
                    medicosMODEL.setCep(jTFCep.getText());
                    medicosMODEL.setCidade(jTFCidade.getText());
                    medicosMODEL.setUf((String)jCBUf.getSelectedItem());
                    medicosMODEL.setEmId(listaCodEspecialidadesMedicas.get(jCBEspecialidadeMedica.getSelectedIndex()));
                    boolean cadastro = medicosDAO.setCadastrar(con, medicosMODEL);
                    Conexao.fechaConexao(con);
                    //atualiza tabela
                    if(cadastro){
                        botaoCancelar();
                    }
        }
    }
    /**Atualiza umMédico no banco de dados.*/
    public void botaoAtualizarRegistro(){
        
        
        if(verificarSetudoFoiPreenchidoCorretamente()){
            //salva no banco
            medicosMODEL medicosMODEL = new medicosMODEL();
                   //fazer a inserção no banco
                    con = Conexao.fazConexao();
                    medicosMODEL.setUsuarioId(usuariosDAO.usrId);
                    medicosMODEL.setDat(dataDeHojeEmVariavelDate);
                    medicosMODEL.setNome(jTFNome.getText());
                    medicosMODEL.setMedicoId(medicoId);
                    medicosMODEL.setCrm(jTFCRM.getText());
                    medicosMODEL.setUfcrm((String)jCBUfCRM.getSelectedItem());
                    medicosMODEL.setNascimento(jTFNascimento.getText());
                    medicosMODEL.setEmail(jTFEmail.getText());
                    medicosMODEL.setTelefone(jTFTelefone.getText());
                    medicosMODEL.setCelular(jTFCelular.getText());
                    medicosMODEL.setEndereco(jTFEndereco.getText());
                    medicosMODEL.setBairro(jTFBairro.getText());
                    medicosMODEL.setCep(jTFCep.getText());
                    medicosMODEL.setCidade(jTFCidade.getText());
                    medicosMODEL.setUf((String)jCBUf.getSelectedItem());
                    medicosMODEL.setEmId(listaCodEspecialidadesMedicas.get(jCBEspecialidadeMedica.getSelectedIndex()));
                    boolean cadastro = medicosDAO.setUpdate(con, medicosMODEL);
                    Conexao.fechaConexao(con);
                    //atualiza tabela
                    if(cadastro){
                        botaoCancelar();
                    }
        }
    }
    /**Deleta o Médico selecionado do banco de dados.*/
    public void botaoApagarRegistro(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Médico?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            //fazer o delete de acordo com o codigo
            medicosMODEL medicoMODEL = new medicosMODEL();
            medicoMODEL.setMedicoId(medicoId);
            con = Conexao.fazConexao();
            boolean deleto = medicosDAO.setDeletar(con, medicoMODEL);
            Conexao.fechaConexao(con);
            //atualizar tabela
            if(deleto){
                botaoCancelar();
            }
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTFEmail = new javax.swing.JTextField(new documentoSemAspasEPorcento(64),null,0);
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jCBUf = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jTFNascimento = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##/##/####"));
        jLabel3 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField(new documentoSomenteLetras(64), null, 0);
        jTFEndereco = new javax.swing.JTextField(new documentoSemAspasEPorcento(80),null,0);
        jTFCidade = new javax.swing.JTextField(new documentoSemAspasEPorcento(32),null,0);
        jLabel15 = new javax.swing.JLabel();
        jTFBairro = new javax.swing.JTextField(new documentoSemAspasEPorcento(32),null,0);
        jLabel13 = new javax.swing.JLabel();
        jTFCep = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##.###-###"))
        ;
        jLabel14 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jTFTelefone = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("(##) ####-####"));
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTFCelular = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("(##) ####-####"));
        jCBEspecialidadeMedica = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFCRM = new javax.swing.JTextField(new documentoSemAspasEPorcento(20),null,0);
        jLabel7 = new javax.swing.JLabel();
        jCBUfCRM = new javax.swing.JComboBox();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        setTitle("Cadastro de Médicos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Médico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEmailFocusLost(evt);
            }
        });

        jLabel20.setText("E-Mail");

        jLabel16.setText("UF");

        jCBUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        jLabel17.setText("Cidade");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText("Nascimento");

        jTFNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusLost(evt);
            }
        });

        jLabel3.setText("Nome");

        jLabel15.setText("CEP");

        jLabel13.setText("Endereço");

        jTFCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCepFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCepFocusLost(evt);
            }
        });

        jLabel14.setText("Bairro");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTFTelefone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusLost(evt);
            }
        });

        jLabel11.setText("Telefone");

        jLabel12.setText("Celular");

        jTFCelular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCelularFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCelularFocusLost(evt);
            }
        });

        jLabel18.setText("Especialidade Medica");

        jLabel6.setText("CRM");

        jLabel7.setText("UF CRM");

        jCBUfCRM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(551, 551, 551)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel13)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTFNome, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTFEndereco, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTFBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel15)
                                .addComponent(jTFCep, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFCRM, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jCBUfCRM, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel5)
                    .addComponent(jTFNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBEspecialidadeMedica, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTFEmail)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jTFCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel18)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTFTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jTFCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel20))
                        .addGap(59, 59, 59)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(26, 26, 26))
                            .addComponent(jTFCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBEspecialidadeMedica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCRM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBUfCRM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(80, 80, 80)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyReleased(evt);
            }
        });

        jBApagarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/deletar.png"))); // NOI18N
        jBApagarRegistro.setText("Apagar");
        jBApagarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBApagarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBApagarRegistroActionPerformed(evt);
            }
        });
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyReleased(evt);
            }
        });

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyReleased(evt);
            }
        });

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });
        jBCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBCancelarKeyReleased(evt);
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBAtualizarRegistro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBApagarRegistro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBSalvarRegistro)
                        .addGap(0, 169, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBAtualizarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBApagarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBSalvarRegistro))
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNascimentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNascimentoFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));         jTFMensagemParaUsuario.setText("dd/mm/aaaa");     }//GEN-LAST:event_jTFNascimentoFocusGained

    private void jTFTelefoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFTelefoneFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));         jTFMensagemParaUsuario.setText("(##) ####-####");     }//GEN-LAST:event_jTFTelefoneFocusGained

    private void jTFCelularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCelularFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));         jTFMensagemParaUsuario.setText("(##) ####-####");     }//GEN-LAST:event_jTFCelularFocusGained

    private void jTFCepFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCepFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));         jTFMensagemParaUsuario.setText("##.###-###");     }//GEN-LAST:event_jTFCepFocusGained

    private void jTFEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFEmailFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));         jTFMensagemParaUsuario.setText("xxx@xxx.xxx");     }//GEN-LAST:event_jTFEmailFocusGained

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();         
        }
    }//GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }//GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();         
        }
    }//GEN-LAST:event_jBSalvarRegistroKeyReleased

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }//GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBApagarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();         
        }
    }//GEN-LAST:event_jBApagarRegistroKeyReleased

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }//GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();         
        }
    }//GEN-LAST:event_jBAtualizarRegistroKeyReleased

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));  
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));  
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));  
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));  
        jTFCelular.setBackground(new java.awt.Color(255, 255, 255));  
        jTFEndereco.setBackground(new java.awt.Color(255, 255, 255));  
        jTFBairro.setBackground(new java.awt.Color(255, 255, 255));  
        jTFCep.setBackground(new java.awt.Color(255, 255, 255));  
        jTFCidade.setBackground(new java.awt.Color(255, 255, 255));  
    }//GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));  
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));  
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));  
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));  
        jTFCelular.setBackground(new java.awt.Color(255, 255, 255));  
        jTFEndereco.setBackground(new java.awt.Color(255, 255, 255));  
        jTFBairro.setBackground(new java.awt.Color(255, 255, 255));  
        jTFCep.setBackground(new java.awt.Color(255, 255, 255));  
        jTFCidade.setBackground(new java.awt.Color(255, 255, 255));  
    }//GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jTFNascimentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNascimentoFocusLost
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jTFNascimentoFocusLost

    private void jTFTelefoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFTelefoneFocusLost
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jTFTelefoneFocusLost

    private void jTFCelularFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCelularFocusLost
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jTFCelularFocusLost

    private void jTFEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFEmailFocusLost
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jTFEmailFocusLost

    private void jTFCepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCepFocusLost
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jTFCepFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    private javax.swing.JComboBox jCBEspecialidadeMedica;
    private javax.swing.JComboBox jCBUf;
    private javax.swing.JComboBox jCBUfCRM;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTFBairro;
    private javax.swing.JTextField jTFCRM;
    private javax.swing.JTextField jTFCelular;
    private javax.swing.JTextField jTFCep;
    private javax.swing.JTextField jTFCidade;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEndereco;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNascimento;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFTelefone;
    // End of variables declaration//GEN-END:variables
}
