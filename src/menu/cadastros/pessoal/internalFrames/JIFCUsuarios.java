
package menu.cadastros.pessoal.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.documentoSemAspasEPorcentoMinusculas;
import conexao.Conexao;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import menu.cadastros.pessoal.dao.usuariosDAO;
import menu.cadastros.pessoal.model.usuariosMODEL;

/**
 *
 * @author BCN
 */
public class JIFCUsuarios extends javax.swing.JInternalFrame {
    int usuarioId;
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
    /** Creates new form JIFCUsuarios */
    public JIFCUsuarios(String novoOuEditar, int usuarioId) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.usuarioId = usuarioId;
        iniciarClasse();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    /*verifica se envia email foi selecionado**/
    public boolean verificarSeEnviaEmailFoiSelecionado(){
        boolean selecionado = false;
            if(jRadioButton1.isSelected() || jRadioButton2.isSelected()){
                selecionado = true;
            }else{
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Informe se usuário envia E-mail automaticamente");
            }
        return selecionado;
    }
    /**Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela.*/
    public void iniciarClasse(){
        
        //colocando maximo de caracteres nos jTextField
        jTFDescricao.setDocument(new documentoSemAspasEPorcento(64));
        jTFSenha.setDocument(new documentoSemAspasEPorcento(16));
        jTFUsuario.setDocument(new documentoSemAspasEPorcento(32));
        jTFEmail.setDocument(new documentoSemAspasEPorcento(64));
        
        jTFImpressoraEtiquetaEnvelope.setDocument(new documentoSemAspasEPorcentoMinusculas(240));
        jTFImpressoraFicha.setDocument(new documentoSemAspasEPorcentoMinusculas(240));
        jTFImpressoraNotaFiscal.setDocument(new documentoSemAspasEPorcentoMinusculas(240));
        jTFPastaRaiz.setDocument(new documentoSemAspasEPorcentoMinusculas(240));
        jTFImpressora_cod_de_barras.setDocument(new documentoSemAspasEPorcentoMinusculas(240));
        
        if("novo".equals(novoOuEditar)){
            jBApagarRegistro.setVisible(false);
            jBAtualizarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de novo Usuário", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        }else{
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Usuário", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            jBSalvarRegistro.setVisible(false);
            int cont = 0;
            while(cont<JIFCUsuariosVisualizar.listaUsuarios.size()){
                int codObjetos = JIFCUsuariosVisualizar.listaUsuarios.get(cont).getUsrid();
                if(usuarioId==codObjetos){
                     jTFDescricao.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getDescricao());
                     jTFUsuario.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getUsuario());
                     jTFSenha.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getSenha());
                     jTFEmail.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEmail());
                     if("A".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(1);
                     if("C".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(2);
                     if("D".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(3);
                     if("G".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(4);
                     if("M".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(5);
                     if("R".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(6);
                     if("O".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(7);
                     if("T".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(8);
                     if("R".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(9);
                     if("X".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(10);
                     if("F".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEstatus())) jCBEstatus.setSelectedIndex(11);
                     if ("S".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEnvia_email())) jRadioButton1.setSelected(true);
                     if ("N".equals(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getEnvia_email())) jRadioButton2.setSelected(true);
                     
                     jTFImpressoraFicha.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getImpressora_ficha());
                     jTFImpressoraEtiquetaEnvelope.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getImpressora_etiqueta_envelope());
                     jTFImpressoraNotaFiscal.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getImpressora_nota_fiscal());
                     jTFPastaRaiz.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getPasta_raiz());
                     jTFImpressora_cod_de_barras.setText(JIFCUsuariosVisualizar.listaUsuarios.get(cont).getImpressora_codigo_de_barras());
                }
                cont++;
            }
        }
        
    }
    /**Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo.*/
    public void botaoCancelar(){
        this.dispose();
        janelaPrincipal.janelaPrincipal.internalFrameUsuarios = null;
        
        janelaPrincipal.janelaPrincipal.internalFrameUsuariosVisualizar = new JIFCUsuariosVisualizar();
        janelaPrincipal.janelaPrincipal.jDesktopPane1.add(janelaPrincipal.janelaPrincipal.internalFrameUsuariosVisualizar);
        janelaPrincipal.janelaPrincipal.internalFrameUsuariosVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = janelaPrincipal.janelaPrincipal.internalFrameUsuariosVisualizar.getWidth();     
        int aIFrame = janelaPrincipal.janelaPrincipal.internalFrameUsuariosVisualizar.getHeight();     

        janelaPrincipal.janelaPrincipal.internalFrameUsuariosVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    /**Salva uma nova classe de exame na banco de dados.*/
    public void botaoSalvarRegistro(){
        
        boolean emailOk = true;
        boolean enviaEmail = verificarSeEnviaEmailFoiSelecionado();
        if(jRadioButton1.isSelected()){
            emailOk = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);
        }
        boolean usuarioOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFUsuario, 3, jTFMensagemParaUsuario);
        boolean senhaOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFSenha, 4, jTFMensagemParaUsuario);
        boolean descricaoOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFDescricao, 2, jTFMensagemParaUsuario);
        boolean estatusOk = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBEstatus, jTFMensagemParaUsuario);
                       
        if(senhaOk && usuarioOk && descricaoOk && emailOk && enviaEmail){
            if(estatusOk){
                //variavel do jcombox estatus
                String estatus = null;
                String enviaEmaill = null;
                //fazendo um if para verificar se descricao ou referencia ja existem
                usuariosMODEL usuarioModel = new usuariosMODEL();
                //setando os atributos da classe objeto de acordo com os campos
                con = Conexao.fazConexao();
                usuarioModel.setUsuario(jTFUsuario.getText().toUpperCase());
                usuarioModel.setSenha(jTFSenha.getText().toUpperCase());
                boolean existe = usuariosDAO.getConsultarParaSalvarNovoRegistro(con, usuarioModel);
                Conexao.fechaConexao(con);
                if(usuariosDAO.conseguiuConsulta){
                    if(existe){
                    JOptionPane.showMessageDialog(null, "Usuário já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazendo inserção no banco
                        usuarioModel.setDescricao(jTFDescricao.getText());
                        //pegando dados do jcombobox Estatus
                        switch (jCBEstatus.getSelectedIndex()){
                            case 1:
                                estatus = "A";
                                break;
                            case 2:
                                estatus = "C";
                                break;
                            case 3:
                                estatus = "D";
                                break;
                            case 4:
                                estatus = "G";
                                break;
                            case 5:
                                estatus = "M";
                                break;
                            case 6:
                                estatus = "R";
                                break;
                            case 7:
                                estatus = "O";
                                break;
                            case 8:
                                estatus = "T";
                                break;
                            case 9:
                                estatus = "R";
                                break;
                            case 10:
                                estatus = "X";
                                break;
                            case 11:
                                estatus = "F";
                        }
                        usuarioModel.setEstatus(estatus);
                        usuarioModel.setEmail(jTFEmail.getText().toUpperCase());
                        //verificando se manda e-mail ou não
                        if(jRadioButton1.isSelected()){
                            enviaEmaill = "S";
                        }else{
                            enviaEmaill = "N";
                        }
                        usuarioModel.setEnvia_email(enviaEmaill);
                        usuarioModel.setUsuarioId(usuariosDAO.usrId);
                        usuarioModel.setDat(dataDeHojeEmVariavelDate);
                        usuarioModel.setImpressora_ficha(jTFImpressoraFicha.getText());
                        usuarioModel.setImpressora_etiqueta_envelope(jTFImpressoraEtiquetaEnvelope.getText());
                        usuarioModel.setImpressora_nota_fiscal(jTFImpressoraNotaFiscal.getText());
                        usuarioModel.setPasta_raiz(jTFPastaRaiz.getText());
                        usuarioModel.setImpressora_codigo_de_barras(jTFImpressora_cod_de_barras.getText());
                        
                        
                        con = Conexao.fazConexao();
                        boolean cadastro = usuariosDAO.setCadastrar(con, usuarioModel);
                        Conexao.fechaConexao(con);
                        //atualiza tabela
                        if(cadastro){
                            botaoCancelar();
                        }
                    }
                }
            }else{
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Selecione um ESTATUS para o usuário");
            }
        }
    }
    /**Atualiza uma classe de exame no banco de dados.*/
    public void botaoAtualizarRegistro(){
        boolean emailOk = true;
        boolean enviaEmail = verificarSeEnviaEmailFoiSelecionado();
        if(jRadioButton1.isSelected()){
            emailOk = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);
        }
        boolean usuarioOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFUsuario, 3, jTFMensagemParaUsuario);
        boolean senhaOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFSenha, 4, jTFMensagemParaUsuario);
        boolean descricaoOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFDescricao, 2, jTFMensagemParaUsuario);
        boolean estatusOk = MetodosUteis.VerificarSeComboBoxFoiSelecionado(jCBEstatus, jTFMensagemParaUsuario);
                       
        if(senhaOk && usuarioOk && descricaoOk && emailOk && enviaEmail){
            if(estatusOk){
                //fazendo um if para verificar se descricao ou referencia ja existem
                usuariosMODEL usuarioModel = new usuariosMODEL();
                usuarioModel.setUsuario(jTFUsuario.getText());
                usuarioModel.setSenha(jTFSenha.getText());
                usuarioModel.setUsrid(usuarioId);
                con = Conexao.fazConexao();
                boolean existe = usuariosDAO.getConsultarParaAtualizarRegistro(con, usuarioModel);
                Conexao.fechaConexao(con);
                if(usuariosDAO.conseguiuConsulta){
                    if(existe){
                    JOptionPane.showMessageDialog(null,"Usuário já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        String estatus = null;
                        String enviaEmaill = null;
                        usuarioModel.setDescricao(jTFDescricao.getText());
                        //pegando dados do jcombobox Estatus
                        switch (jCBEstatus.getSelectedIndex()){
                            case 1:
                                estatus = "A";
                                break;
                            case 2:
                                 estatus = "C";
                                 break;
                            case 3:
                                 estatus = "D";
                                 break;
                            case 4:
                                 estatus = "G";
                                break;
                            case 5:
                                estatus = "M";
                                break;
                            case 6:
                                estatus = "R";
                                break;
                            case 7:
                               estatus = "O";
                               break;
                            case 8:
                               estatus = "T";
                                break;
                            case 9:
                                estatus = "R";
                                break;
                            case 10:
                                estatus = "X";
                                break;
                            case 11:
                                estatus = "F";    
                        }
                        usuarioModel.setEstatus(estatus);
                        usuarioModel.setEmail(jTFEmail.getText().toUpperCase());
                        //verificando se manda e-mail ou não
                        if(jRadioButton1.isSelected()){
                            enviaEmaill = "S";
                        }else{
                            enviaEmaill = "N";
                        }
                        usuarioModel.setEnvia_email(enviaEmaill);
                        usuarioModel.setUsuarioId(usuariosDAO.usrId);
                        usuarioModel.setDat(dataDeHojeEmVariavelDate);
                        usuarioModel.setImpressora_ficha(jTFImpressoraFicha.getText());
                        usuarioModel.setImpressora_etiqueta_envelope(jTFImpressoraEtiquetaEnvelope.getText());
                        usuarioModel.setImpressora_nota_fiscal(jTFImpressoraNotaFiscal.getText());
                        usuarioModel.setPasta_raiz(jTFPastaRaiz.getText());
                        usuarioModel.setImpressora_codigo_de_barras(jTFImpressora_cod_de_barras.getText());
                        con = Conexao.fazConexao();
                        boolean atualizo = usuariosDAO.setUpdate(con, usuarioModel);
                        Conexao.fechaConexao(con);
                        if(atualizo){
                            JOptionPane.showMessageDialog(null, "É necessário fazer Logoff para efetivar essa alteração.");
                            botaoCancelar();
                       }
                   }
                }
            }else{
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Selecione um ESTATUS para o usuário");
            }
        }
    }
    /**Apaga a classe de exame selecionada do banco de dados.*/
    public void botaoApagarRegistro(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Usuário?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            //fazer o delete de acordo com o codigo
            usuariosMODEL usuarioModel = new usuariosMODEL();
            usuarioModel.setUsrid(usuarioId);
            con = Conexao.fazConexao();
            boolean deleto = usuariosDAO.setDeletar(con, usuarioModel);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFDescricao = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jCBEstatus = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jTFSenha = new javax.swing.JTextField();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTFImpressoraFicha = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTFImpressoraNotaFiscal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTFImpressoraEtiquetaEnvelope = new javax.swing.JTextField();
        jTFPastaRaiz = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTFImpressora_cod_de_barras = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setTitle("Cadastro de Usuários");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Usuário", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Usuário");

        jTFUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFUsuarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFUsuarioFocusLost(evt);
            }
        });
        jTFUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFUsuarioKeyReleased(evt);
            }
        });

        jLabel2.setText("Unidade");

        jTFDescricao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFDescricaoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFDescricaoFocusLost(evt);
            }
        });
        jTFDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFDescricaoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFDescricaoKeyReleased(evt);
            }
        });

        jLabel3.setText("Senha");

        jCBEstatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione o Tipo", "Administrador", "Coordenador", "Digitador", "Grupo", "Médico", "Médico Radiologista", "Operador", "Téc. Radiologia", "Recepcionista", "Almoxerifado", "Financeiro" }));

        jLabel4.setText("Tipo");

        jLabel5.setText("E-Mail");

        jTFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEmailFocusLost(evt);
            }
        });
        jTFEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFEmailKeyReleased(evt);
            }
        });

        jLabel6.setText("Envia E-Mail's Automaticamente?");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Não");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Sim");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jRadioButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jRadioButton1FocusLost(evt);
            }
        });

        jTFSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFSenhaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFSenhaFocusLost(evt);
            }
        });
        jTFSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFSenhaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTFDescricao)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTFUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(243, 243, 243))
                            .addComponent(jTFEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton2)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTFSenha, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCBEstatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(135, 135, 135))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
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
        jBCancelar.setAlignmentY(0.0F);
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configurações", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel7.setText("Impressora para Ficha de Sala (matricial)");

        jTFImpressoraFicha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFImpressoraFichaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFImpressoraFichaFocusLost(evt);
            }
        });
        jTFImpressoraFicha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFImpressoraFichaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFImpressoraFichaKeyReleased(evt);
            }
        });

        jLabel8.setText("Impressora para Nota Fiscal (matricial)");

        jTFImpressoraNotaFiscal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFImpressoraNotaFiscalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFImpressoraNotaFiscalFocusLost(evt);
            }
        });
        jTFImpressoraNotaFiscal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFImpressoraNotaFiscalKeyReleased(evt);
            }
        });

        jLabel9.setText("Impressora para Etiqueta do Envelope (matricial)");

        jTFImpressoraEtiquetaEnvelope.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFImpressoraEtiquetaEnvelopeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFImpressoraEtiquetaEnvelopeFocusLost(evt);
            }
        });
        jTFImpressoraEtiquetaEnvelope.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFImpressoraEtiquetaEnvelopeKeyReleased(evt);
            }
        });

        jTFPastaRaiz.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFPastaRaizFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPastaRaizFocusLost(evt);
            }
        });
        jTFPastaRaiz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFPastaRaizKeyReleased(evt);
            }
        });

        jLabel10.setText("Pasta Raiz para armazenamento de Arquivos");

        jTFImpressora_cod_de_barras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFImpressora_cod_de_barrasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFImpressora_cod_de_barrasFocusLost(evt);
            }
        });
        jTFImpressora_cod_de_barras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFImpressora_cod_de_barrasKeyReleased(evt);
            }
        });

        jLabel11.setText("Impressora para Código de Barras");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTFImpressoraFicha)
            .addComponent(jTFImpressoraNotaFiscal)
            .addComponent(jTFImpressoraEtiquetaEnvelope)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFPastaRaiz)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addGap(0, 241, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jTFImpressora_cod_de_barras)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addGap(0, 321, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFImpressoraFicha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFImpressoraNotaFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFImpressoraEtiquetaEnvelope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFImpressora_cod_de_barras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFPastaRaiz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                .addComponent(jBCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTFMensagemParaUsuario)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBAtualizarRegistro)
                    .addComponent(jBApagarRegistro)
                    .addComponent(jBSalvarRegistro)
                    .addComponent(jBCancelar))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        setBounds(0, 0, 948, 466);
    }// </editor-fold>//GEN-END:initComponents

private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
    botaoCancelar();
}//GEN-LAST:event_jBCancelarActionPerformed

private void jBCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyPressed
if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoCancelar(); 
        }
}//GEN-LAST:event_jBCancelarKeyPressed

private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarRegistroActionPerformed
    botaoSalvarRegistro();
}//GEN-LAST:event_jBSalvarRegistroActionPerformed

private void jBSalvarRegistroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarRegistroKeyPressed
if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoSalvarRegistro(); 
        }
}//GEN-LAST:event_jBSalvarRegistroKeyPressed

private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroActionPerformed
    botaoAtualizarRegistro();
}//GEN-LAST:event_jBAtualizarRegistroActionPerformed

private void jBAtualizarRegistroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoAtualizarRegistro(); 
        } 
}//GEN-LAST:event_jBAtualizarRegistroKeyPressed

private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBApagarRegistroActionPerformed
    botaoApagarRegistro();
}//GEN-LAST:event_jBApagarRegistroActionPerformed

private void jBApagarRegistroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBApagarRegistroKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
            botaoApagarRegistro();
        }
}//GEN-LAST:event_jBApagarRegistroKeyPressed

    private void jTFDescricaoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFDescricaoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 4 caracteres");
    }//GEN-LAST:event_jTFDescricaoFocusGained

    private void jTFDescricaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFDescricaoFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFDescricao, 4, jTFMensagemParaUsuario);
        if(ok){
            jTFDescricao.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFDescricaoFocusLost

    private void jTFUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFUsuarioFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 4 caracteres");
    }//GEN-LAST:event_jTFUsuarioFocusGained

    private void jTFUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFUsuarioFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFUsuario, 4, jTFMensagemParaUsuario);
        if(ok){
            jTFUsuario.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFUsuarioFocusLost

    private void jTFEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFEmailFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("xxx@xxx.xxx");
    }//GEN-LAST:event_jTFEmailFocusGained

    private void jTFEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFEmailFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);
        if(ok){
            jTFEmail.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFEmailFocusLost

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
jTFEmail.setEnabled(true);
        jTFEmail.requestFocus();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jRadioButton1FocusLost
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jRadioButton1FocusLost

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFDescricao.setBackground(new java.awt.Color(255,255,255));
        jTFUsuario.setBackground(new java.awt.Color(255,255,255));
        jTFSenha.setBackground(new java.awt.Color(255,255,255));
        jTFEmail.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFDescricao.setBackground(new java.awt.Color(255,255,255));
        jTFUsuario.setBackground(new java.awt.Color(255,255,255));
        jTFSenha.setBackground(new java.awt.Color(255,255,255));
        jTFEmail.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jTFDescricaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFDescricaoKeyPressed
        
    }//GEN-LAST:event_jTFDescricaoKeyPressed

    private void jTFDescricaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFDescricaoKeyReleased
        jTFDescricao.setText(jTFDescricao.getText().toUpperCase());
    }//GEN-LAST:event_jTFDescricaoKeyReleased

    private void jTFUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFUsuarioKeyReleased

    }//GEN-LAST:event_jTFUsuarioKeyReleased

    private void jTFEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFEmailKeyReleased
jTFEmail.setText(jTFEmail.getText().toUpperCase());
    }//GEN-LAST:event_jTFEmailKeyReleased

    private void jTFSenhaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSenhaFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 4 caracteres");
    }//GEN-LAST:event_jTFSenhaFocusGained

    private void jTFSenhaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFSenhaFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFSenha, 4, jTFMensagemParaUsuario);
        if(ok){
            jTFSenha.setBackground(new java.awt.Color(255,255,255));
        }
    }//GEN-LAST:event_jTFSenhaFocusLost

    private void jTFSenhaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSenhaKeyReleased

    }//GEN-LAST:event_jTFSenhaKeyReleased

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jTFEmail.setEnabled(false);
        jTFEmail.setText("");
        jTFEmail.setBackground(new java.awt.Color(255,255,255));
        jTFMensagemParaUsuario.setText("");
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jTFImpressoraFichaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressoraFichaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraFichaFocusGained

    private void jTFImpressoraFichaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressoraFichaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraFichaFocusLost

    private void jTFImpressoraFichaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFImpressoraFichaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraFichaKeyPressed

    private void jTFImpressoraFichaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFImpressoraFichaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraFichaKeyReleased

    private void jTFImpressoraNotaFiscalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressoraNotaFiscalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraNotaFiscalFocusGained

    private void jTFImpressoraNotaFiscalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressoraNotaFiscalFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraNotaFiscalFocusLost

    private void jTFImpressoraNotaFiscalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFImpressoraNotaFiscalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraNotaFiscalKeyReleased

    private void jTFImpressoraEtiquetaEnvelopeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressoraEtiquetaEnvelopeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraEtiquetaEnvelopeFocusGained

    private void jTFImpressoraEtiquetaEnvelopeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressoraEtiquetaEnvelopeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraEtiquetaEnvelopeFocusLost

    private void jTFImpressoraEtiquetaEnvelopeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFImpressoraEtiquetaEnvelopeKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressoraEtiquetaEnvelopeKeyReleased

    private void jTFPastaRaizFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFPastaRaizFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPastaRaizFocusGained

    private void jTFPastaRaizFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFPastaRaizFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPastaRaizFocusLost

    private void jTFPastaRaizKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFPastaRaizKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPastaRaizKeyReleased

    private void jTFImpressora_cod_de_barrasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressora_cod_de_barrasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressora_cod_de_barrasFocusGained

    private void jTFImpressora_cod_de_barrasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFImpressora_cod_de_barrasFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressora_cod_de_barrasFocusLost

    private void jTFImpressora_cod_de_barrasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFImpressora_cod_de_barrasKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFImpressora_cod_de_barrasKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    public static javax.swing.JComboBox jCBEstatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public static javax.swing.JRadioButton jRadioButton1;
    public static javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTFDescricao;
    public static javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFImpressoraEtiquetaEnvelope;
    private javax.swing.JTextField jTFImpressoraFicha;
    private javax.swing.JTextField jTFImpressoraNotaFiscal;
    private javax.swing.JTextField jTFImpressora_cod_de_barras;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFPastaRaiz;
    private javax.swing.JTextField jTFSenha;
    private javax.swing.JTextField jTFUsuario;
    // End of variables declaration//GEN-END:variables

    private void setLocationRelativeTo(Object object) {

    }
}
