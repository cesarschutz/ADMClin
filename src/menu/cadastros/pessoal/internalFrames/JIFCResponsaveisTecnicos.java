package menu.cadastros.pessoal.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.Conexao;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import menu.cadastros.pessoal.dao.responsaveis_tecnicosDAO;
import menu.cadastros.pessoal.dao.usuariosDAO;
import menu.cadastros.pessoal.model.responsaveis_tecnicosMODEL;

/**
 *
 * @author BCN
 */
public class JIFCResponsaveisTecnicos extends javax.swing.JInternalFrame {
    int rtId;
    String novoOuEditar;
    Connection con = null;
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
    /** Creates new form JIFCResponsaveisTecnicos */
    public JIFCResponsaveisTecnicos(String novoOuEditar, int rtId) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.rtId = rtId;
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
        //colocando maximo de caracteres nos jtextfield
        jTFNome.setDocument(new documentoSemAspasEPorcento(64));
        jTFConselho.setDocument(new documentoSemAspasEPorcento(8));
        if("novo".equals(novoOuEditar)){
            jBApagarRegistro.setVisible(false);
            jBAtualizarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de novo Responsável Técnico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }else{
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Responsável Técnico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jBSalvarRegistro.setVisible(false);
            int cont = 0;
            while(cont<JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.size()){
                int codObjetos = JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getRtId();
                if(rtId==codObjetos){
                     jTFNome.setText(JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getNome());
                     jTFConselho.setText(JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getConselho());
                     jTFRegistroConselho.setText(JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getRegistroConselho());
                     jTFCpf.setText(JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getCpf());
                     jCBUf.setSelectedItem(JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getUf());
                     jTFRegistroAns.setText(JIFCResponsaveisTecnicosVisualizar.listaResponsaveisTecnicos.get(cont).getRegistroAns());
                }
                cont++;
            }
        }
    }
    /**Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com os objetos.*/
    public void botaoCancelar(){
        this.dispose();
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicos = null;
        
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar = new JIFCResponsaveisTecnicosVisualizar()  ;
        janelaPrincipal.janelaPrincipal.jDesktopPane1.add(janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar);
        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar.getWidth();     
        int aIFrame = janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar.getHeight();     

        janelaPrincipal.janelaPrincipal.internalFrameResponsaveisTecnicosVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    /**Salva um novo Responsável Técnico na banco de dados.*/
    public void botaoSalvarRegistro(){
        boolean nomePreenchido = false;
        boolean conselhoPreenchido = false;
        boolean regConselhoPreenchido = false;
        boolean cpfPreenchido = false;
        boolean ufPreenchido = false;
        boolean regAnsPreenchido = false;
        
        //verificando os textField
        nomePreenchido = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);
        conselhoPreenchido = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFConselho, 2, jTFMensagemParaUsuario);
        regConselhoPreenchido = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFRegistroConselho, jTFMensagemParaUsuario, "      ");
        cpfPreenchido = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFCpf, jTFMensagemParaUsuario, "   .   .   -  ");
        ufPreenchido = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBUf, jTFMensagemParaUsuario);
        regAnsPreenchido = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFRegistroAns, jTFMensagemParaUsuario, "      ");
        
        if(nomePreenchido && conselhoPreenchido && regConselhoPreenchido && cpfPreenchido && ufPreenchido && regAnsPreenchido){
            responsaveis_tecnicosMODEL responsavelTecnicoModel = new responsaveis_tecnicosMODEL();
            responsavelTecnicoModel.setCpf(jTFCpf.getText().toUpperCase());
            con = Conexao.fazConexao();
            boolean existe = responsaveis_tecnicosDAO.getConsultarParaSalvarNovoRegistro(con, responsavelTecnicoModel);
            if(responsaveis_tecnicosDAO.conseguiuConsulta){
                if(existe){
                    JOptionPane.showMessageDialog(null, "CPF já cadastrado","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }else{
                    //fazer a inserção no banco
                    responsavelTecnicoModel.setUsuarioId(usuariosDAO.usrId);
                    responsavelTecnicoModel.setData(dataDeHojeEmVariavelDate);
                    responsavelTecnicoModel.setNome(jTFNome.getText().toUpperCase());
                    responsavelTecnicoModel.setConselho(jTFConselho.getText().toUpperCase());
                    responsavelTecnicoModel.setRegistroConselho(jTFRegistroConselho.getText().toUpperCase());
                    responsavelTecnicoModel.setUf((String)jCBUf.getSelectedItem());
                    responsavelTecnicoModel.setRegistroAns(jTFRegistroAns.getText().toUpperCase());
                    boolean cadastro = responsaveis_tecnicosDAO.setCadastrar(con, responsavelTecnicoModel);
                    Conexao.fechaConexao(con);
                    if(cadastro){
                        botaoCancelar();
                    }
                }
            }
        }
    }
    /**
     * Atualiza um Responsavel Tecnico no Banco De Dados.
     */
    public void botaoAtualizarRegistro(){
        boolean nomePreenchido = false;
        boolean conselhoPreenchido = false;
        boolean regConselhoPreenchido = false;
        boolean cpfPreenchido = false;
        boolean ufPreenchido = false;
        boolean regAnsPreenchido = false;
        
        //verificando os textField
        nomePreenchido = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);
        conselhoPreenchido = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFConselho, 2, jTFMensagemParaUsuario);
        regConselhoPreenchido = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFRegistroConselho, jTFMensagemParaUsuario, "      ");
        cpfPreenchido = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFCpf, jTFMensagemParaUsuario, "   .   .   -  ");
        ufPreenchido = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBUf, jTFMensagemParaUsuario);
        regAnsPreenchido = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFRegistroAns, jTFMensagemParaUsuario, "      ");
        
        if(nomePreenchido && conselhoPreenchido && regConselhoPreenchido && cpfPreenchido && ufPreenchido && regAnsPreenchido){
            responsaveis_tecnicosMODEL responsavelTecnicoModel = new responsaveis_tecnicosMODEL();
            responsavelTecnicoModel.setCpf(jTFCpf.getText().toUpperCase());
            responsavelTecnicoModel.setRtId(rtId);
            con = Conexao.fazConexao();
            boolean existe = responsaveis_tecnicosDAO.getConsultarParaAtualizarRegistro(con, responsavelTecnicoModel);
            Conexao.fechaConexao(con);
            if(responsaveis_tecnicosDAO.conseguiuConsulta){
                if(existe){
                    JOptionPane.showMessageDialog(null, "CPF já cadastrado","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }else{
                    responsavelTecnicoModel.setUsuarioId(usuariosDAO.usrId);
                    responsavelTecnicoModel.setData(dataDeHojeEmVariavelDate);
                    responsavelTecnicoModel.setNome(jTFNome.getText().toUpperCase());
                    responsavelTecnicoModel.setConselho(jTFConselho.getText().toUpperCase());
                    responsavelTecnicoModel.setRegistroConselho(jTFRegistroConselho.getText().toUpperCase());
                    responsavelTecnicoModel.setUf((String)jCBUf.getSelectedItem());
                    responsavelTecnicoModel.setRegistroAns(jTFRegistroAns.getText().toUpperCase());
                    con = Conexao.fazConexao();
                    boolean atualizo = responsaveis_tecnicosDAO.setUpdate(con, responsavelTecnicoModel);
                    Conexao.fechaConexao(con);
                    if(atualizo){
                        //dexando janela como no inicio
                        botaoCancelar();
                    }
                }
            }
        }
    }
    /*
     * Apaga Responsável Técnico do Banco De Dados.
     */
    public void botaoApagarRegistro(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Responsável Técnico?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            responsaveis_tecnicosMODEL responsavelTecnico = new responsaveis_tecnicosMODEL();
            responsavelTecnico.setRtId(rtId);
            con = Conexao.fazConexao();
            boolean deleto = responsaveis_tecnicosDAO.setDeletar(con, responsavelTecnico);
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
        jLabel1 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField();
        jTFConselho = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFRegistroConselho = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("######"));
        jLabel6 = new javax.swing.JLabel();
        jTFCpf = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("###.###.###-##"));
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTFRegistroAns = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("######"));
        jCBUf = new javax.swing.JComboBox();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        setTitle("Cadastro de Responsáveis Técnicos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Responsável Técnico", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Nome");

        jTFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNomeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNomeFocusLost(evt);
            }
        });
        jTFNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFNomeKeyReleased(evt);
            }
        });

        jTFConselho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFConselhoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFConselhoFocusLost(evt);
            }
        });
        jTFConselho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFConselhoKeyReleased(evt);
            }
        });

        jLabel4.setText("Conselho");

        jLabel5.setText("Registro no Conselho");

        jTFRegistroConselho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFRegistroConselhoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFRegistroConselhoFocusLost(evt);
            }
        });
        jTFRegistroConselho.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFRegistroConselhoKeyReleased(evt);
            }
        });

        jLabel6.setText("CPF");

        jTFCpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCpfFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCpfFocusLost(evt);
            }
        });
        jTFCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFCpfKeyReleased(evt);
            }
        });

        jLabel7.setText("UF");

        jLabel8.setText("Registro na ANS");

        jTFRegistroAns.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFRegistroAnsFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFRegistroAnsFocusLost(evt);
            }
        });
        jTFRegistroAns.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFRegistroAnsKeyReleased(evt);
            }
        });

        jCBUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFRegistroAns, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jTFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTFConselho, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jTFRegistroConselho, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jTFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jCBUf, 0, 152, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFRegistroConselho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFConselho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFRegistroAns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyReleased(evt);
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
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyReleased(evt);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyReleased(evt);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBCancelarKeyReleased(evt);
            }
        });

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18));
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBAtualizarRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBApagarRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSalvarRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBAtualizarRegistro)
                        .addComponent(jBApagarRegistro)
                        .addComponent(jBSalvarRegistro))
                    .addComponent(jBCancelar)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoCancelar(); 
        } 
    }//GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }//GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoSalvarRegistro(); 
        } 
    }//GEN-LAST:event_jBSalvarRegistroKeyReleased

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }//GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoAtualizarRegistro(); 
        }
    }//GEN-LAST:event_jBAtualizarRegistroKeyReleased

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }//GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBApagarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoApagarRegistro();
        } 
    }//GEN-LAST:event_jBApagarRegistroKeyReleased

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255,255,255));
        jTFCpf.setBackground(new java.awt.Color(255,255,255));
        jTFConselho.setBackground(new java.awt.Color(255,255,255));
        jTFRegistroConselho.setBackground(new java.awt.Color(255,255,255));
        jTFRegistroAns.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jTFNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNomeKeyReleased

    }//GEN-LAST:event_jTFNomeKeyReleased

    private void jTFConselhoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFConselhoKeyReleased


    }//GEN-LAST:event_jTFConselhoKeyReleased

    private void jTFRegistroConselhoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFRegistroConselhoKeyReleased

    }//GEN-LAST:event_jTFRegistroConselhoKeyReleased

    private void jTFCpfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFCpfKeyReleased

    }//GEN-LAST:event_jTFCpfKeyReleased

    private void jTFRegistroAnsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFRegistroAnsKeyReleased

    }//GEN-LAST:event_jTFRegistroAnsKeyReleased

    private void jTFNomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNomeFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Nome e Sobrenome");
    }//GEN-LAST:event_jTFNomeFocusGained

    private void jTFNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNomeFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);
        if(ok){
            jTFNome.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFNomeFocusLost

    private void jTFConselhoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFConselhoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
    }//GEN-LAST:event_jTFConselhoFocusGained

    private void jTFConselhoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFConselhoFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFConselho, 2, jTFMensagemParaUsuario);
        if(ok){
            jTFConselho.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFConselhoFocusLost

    private void jTFRegistroConselhoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFRegistroConselhoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("######");
    }//GEN-LAST:event_jTFRegistroConselhoFocusGained

    private void jTFRegistroConselhoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFRegistroConselhoFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFRegistroConselho, jTFMensagemParaUsuario, "      ");
        String correta = jTFRegistroConselho.getText().replace(" ", "");
        if(correta.length()<6){
            jTFRegistroConselho.setBackground(new java.awt.Color(255, 170, 170));
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Preencha corretamente os campos demarcados");
        }
        if(ok && correta.length()==6){
            jTFRegistroConselho.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFRegistroConselhoFocusLost

    private void jTFCpfFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCpfFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("###.###.###-##");
    }//GEN-LAST:event_jTFCpfFocusGained

    private void jTFCpfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFCpfFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFCpf, jTFMensagemParaUsuario, "   .   .   -  ");
        String correta = jTFCpf.getText().replace(" ", "");
        if(correta.length()<14){
            jTFCpf.setBackground(new java.awt.Color(255, 170, 170));
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Preencha corretamente os campos demarcados");
        }
        if(ok && correta.length()==14){
            jTFCpf.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFCpfFocusLost

    private void jTFRegistroAnsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFRegistroAnsFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("######");
    }//GEN-LAST:event_jTFRegistroAnsFocusGained

    private void jTFRegistroAnsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFRegistroAnsFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFRegistroAns, jTFMensagemParaUsuario, "      ");
        String correta = jTFRegistroAns.getText().replace(" ", "");
        if(correta.length()<6){
            jTFRegistroAns.setBackground(new java.awt.Color(255, 170, 170));
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Preencha corretamente os campos demarcados");
        }
        if(ok && correta.length()==6){
            jTFRegistroAns.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFRegistroAnsFocusLost

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroFocusLost
                jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255,255,255));
        jTFCpf.setBackground(new java.awt.Color(255,255,255));
        jTFConselho.setBackground(new java.awt.Color(255,255,255));
        jTFRegistroConselho.setBackground(new java.awt.Color(255,255,255));
        jTFRegistroAns.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_jBAtualizarRegistroFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    private javax.swing.JComboBox jCBUf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFConselho;
    private javax.swing.JTextField jTFCpf;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFRegistroAns;
    private javax.swing.JTextField jTFRegistroConselho;
    // End of variables declaration//GEN-END:variables

}
