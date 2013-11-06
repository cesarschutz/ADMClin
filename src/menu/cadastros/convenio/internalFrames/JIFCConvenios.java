/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCConveniosEditarNovo.java
 *
 * Created on 23/07/2012, 15:23:29
 */
package menu.cadastros.convenio.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.documentoSomenteNumerosELetras;
import conexao.Conexao;
import janelaPrincipal.janelaPrincipal;
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
import menu.cadastros.convenio.dao.conveniosChDAO;
import menu.cadastros.convenio.dao.conveniosDAO;
import menu.cadastros.convenio.dao.conveniosFilmeDAO;
import menu.cadastros.convenio.model.conveniosMODEL;
import menu.cadastros.pessoal.dao.usuariosDAO;

/**
 *
 * @author BCN
 */
public class JIFCConvenios extends javax.swing.JInternalFrame {

    private Connection con = null;
    public List<Integer> lista_handle_grupo_convenio = new ArrayList<Integer>();
    int handle_convenio;
    String novoOuEditar = null;
    String valorCh, valorFilme, dataAValerCh, dataAValerFilme;
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
    /** Creates new form JIFCConveniosEditarNovo */
    public JIFCConvenios(String novoOuEditar, int handle_convenio) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        preencherGrupoDeConvenios();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
        if("novo".equals(novoOuEditar)){
            jBEditar.setVisible(false);
            jBDeletar.setVisible(false);
            this.handle_convenio = handle_convenio;
            jBFilme.setEnabled(false);
            jBCh.setEnabled(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Convênio", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
        if("editar".equals(novoOuEditar)){
            jBSalvar.setVisible(false);
            this.handle_convenio = handle_convenio;
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Convênio", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            preencherConvenio();
        }
    }

    public void preencherGrupoDeConvenios(){
        con = Conexao.fazConexao();
        try{
            lista_handle_grupo_convenio.add(0);
            ResultSet resultSet = conveniosDAO.getConsultarGruposDeConvenios(con);
            while(resultSet.next()){
                lista_handle_grupo_convenio.add(resultSet.getInt("grupo_id"));
                jCBGrupo.addItem(resultSet.getString("nome"));
                Conexao.fechaConexao(con);
            }
        }catch(Exception e){
            this.dispose();
            janelaPrincipal.internalFrameConvenios = null;
        }
    }
    
    public void preencherConvenio(){
        con = Conexao.fazConexao();
        try{
            ResultSet resultSet = conveniosDAO.getConsultarDadosDeUmConvenio(con, handle_convenio);
            while(resultSet.next()){
                jTFNome.setText(resultSet.getString("nome"));
                jTFEndereco.setText(resultSet.getString("endereco"));
                jTFCidade.setText(resultSet.getString("cidade"));
                jTFCep.setText(resultSet.getString("cep"));
                jCBUf.setSelectedItem(resultSet.getString("uf"));
                jTFTelefone.setText(resultSet.getString("telefone"));
                jTFContato.setText(resultSet.getString("contato"));
                jTFEmail.setText(resultSet.getString("email"));
                jCBVerificacao_matricula.setSelectedIndex(resultSet.getInt("validacao_matricula"));
                jCBArquivoTextoFatura.setSelectedIndex(resultSet.getInt("IMPRIMI_ARQUIVO_TXT_COM_FATURA"));
                jTFPorcentConvenio.setText(resultSet.getString("porcentconvenio"));
                jTFPorcentPaciente.setText(resultSet.getString("porcentpaciente"));
                jTFRedutor.setText(resultSet.getString("redutor"));
                for(int x=0; x < lista_handle_grupo_convenio.size(); x++){
                    if(lista_handle_grupo_convenio.get(x) == resultSet.getInt("grupoid")){
                        jCBGrupo.setSelectedIndex(x);
                    }
                }
                Conexao.fechaConexao(con);
            }
        }catch(Exception e){
            this.dispose();
            janelaPrincipal.internalFrameConvenios = null;
        }
            
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void botaoCh(){
        janelaPrincipal.internalFrameConvenios.setVisible(false);
         
            janelaPrincipal.internalFrameConveioCH = new JIFCConvenioCH(handle_convenio, jTFNome.getText());
            janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameConveioCH);
            janelaPrincipal.internalFrameConveioCH.setVisible(true);
            
            int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
            int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
            int lIFrame = janelaPrincipal.internalFrameConveioCH.getWidth();
            int aIFrame = janelaPrincipal.internalFrameConveioCH.getHeight();
            janelaPrincipal.internalFrameConveioCH.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    
    public void botaoFilme(){
         
         janelaPrincipal.internalFrameConvenios.setVisible(false);
         
            janelaPrincipal.internalFrameConvenioFilme = new JIFCConvenioFILME(handle_convenio, jTFNome.getText());
            janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameConvenioFilme);
            janelaPrincipal.internalFrameConvenioFilme.setVisible(true);
            
            int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
            int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
            int lIFrame = janelaPrincipal.internalFrameConvenioFilme.getWidth();
            int aIFrame = janelaPrincipal.internalFrameConvenioFilme.getHeight();
            janelaPrincipal.internalFrameConvenioFilme.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    
    public boolean verificarSeTudoFoiPreenchido(){
    
        boolean porcentagemPaciente = false;
        boolean porcentagemConvenio = false;
        boolean somaOk = false;
        boolean redutorOk = false;
        
        
        String porcentagemConvenioSemEspaço = jTFPorcentConvenio.getText().replace(" ", "");
        if (porcentagemConvenioSemEspaço.length() == 6) {
            porcentagemConvenio = true;
        }
        
        
        String porcentagemPacienteSemEspaço = jTFPorcentPaciente.getText().replace(" ", "");
        if (porcentagemPacienteSemEspaço.length() == 6) {
            porcentagemPaciente = true;
        }
        
        
        if (Double.valueOf(porcentagemConvenioSemEspaço) + Double.valueOf(porcentagemPacienteSemEspaço) == 100) {
            somaOk = true;
        }
        
        //verificando se o redutor esta correto
        try {
            double valorRedutor = Double.valueOf(jTFRedutor.getText());
            if (valorRedutor <= 100) {
                redutorOk = true;
            }else{
                redutorOk = false;
            }
        } catch (Exception e) {
            redutorOk = false;
        }
        
        
        if(porcentagemConvenio && porcentagemPaciente && somaOk && redutorOk){
            return true;
        }else{
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Porcentagem Inválida"); 
            jTFPorcentConvenio.setText(""); 
            jTFPorcentPaciente.setText(""); 
            return false;
        }
        
        
    }
    
    public void botaoSalvarEAtualizar(){
        if(verificarSeTudoFoiPreenchido()){
            con = Conexao.fazConexao();
            conveniosMODEL convenioMODEL = new conveniosMODEL();
            convenioMODEL.setNome(jTFNome.getText());
            convenioMODEL.setHandle_convenio(handle_convenio);
            boolean existe = conveniosDAO.getConsultarParaSalvarAtualizarRegistro(con, convenioMODEL);
            Conexao.fechaConexao(con);
            if(conveniosDAO.conseguiuConsulta){
                if(existe){
                    JOptionPane.showMessageDialog(null, "Nome de Convênio já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }else{
                    convenioMODEL.setNome(jTFNome.getText());
                    convenioMODEL.setEndereco(jTFEndereco.getText());
                    convenioMODEL.setCidade(jTFCidade.getText());
                    try { convenioMODEL.setCep(jTFCep.getText());} catch (Exception e){}
                    try{ convenioMODEL.setUf(String.valueOf(jCBUf.getSelectedItem()));} catch (Exception e){}
                    convenioMODEL.setTelefone(jTFTelefone.getText());
                    convenioMODEL.setContato(jTFContato.getText());
                    convenioMODEL.setEmail(jTFEmail.getText());
                    convenioMODEL.setPorcentConvenio(jTFPorcentConvenio.getText());
                    convenioMODEL.setPorcentPaciente(jTFPorcentPaciente.getText());
                    convenioMODEL.setRedutor(jTFRedutor.getText());
                    convenioMODEL.setUsuarioId(usuariosDAO.usrId);
                    convenioMODEL.setDat(dataDeHojeEmVariavelDate);
                    convenioMODEL.setHandle_convenio(handle_convenio);
                    convenioMODEL.setVALIDACAO_MATRICULA(jCBVerificacao_matricula.getSelectedIndex());
                    convenioMODEL.setIMPRIMI_ARQUIVO_TXT_COM_FATURA(jCBArquivoTextoFatura.getSelectedIndex());
                    convenioMODEL.setGrupoid(lista_handle_grupo_convenio.get(jCBGrupo.getSelectedIndex()));

                    con = Conexao.fazConexao();
                    boolean atualizo = conveniosDAO.setUpdate(con, convenioMODEL);
                    Conexao.fechaConexao(con);
                    if(atualizo){
                        botaoCancelar("n");
                        
                    }
                }
            }  
        } 
    }
    
    
    public void botaoCancelar(String apagar){
        
        boolean nomePreenchido = false;
        String semEspaco = jTFNome.getText().replace(" ", "");
        if(semEspaco.length()>=3){
            nomePreenchido = true;
        }
        
        if("novo".equals(novoOuEditar) && nomePreenchido && "s".equals(apagar)){
            con = Conexao.fazConexao();
            conveniosDAO.setDeletar(con, handle_convenio);
            conveniosChDAO.setDeletar(con, handle_convenio);
            conveniosFilmeDAO.setDeletar(con, handle_convenio);
            Conexao.fechaConexao(con);
        }
        
        this.dispose();
        janelaPrincipal.internalFrameConvenios = null;
        
        janelaPrincipal.internalFrameConvenioVisualizar = new JIFCConveniosVisualizar()  ;
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameConvenioVisualizar);
        janelaPrincipal.internalFrameConvenioVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = janelaPrincipal.internalFrameConvenioVisualizar.getWidth();     
        int aIFrame = janelaPrincipal.internalFrameConvenioVisualizar.getHeight();     

        janelaPrincipal.internalFrameConvenioVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
        janelaPrincipal.internalFrameJanelaPrincipal.ativandoOMenu();
    }
    
    public void botaoDeletar(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Convênio?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            con = Conexao.fazConexao();
            conveniosDAO.setDeletar(con, handle_convenio);
            conveniosChDAO.setDeletar(con, handle_convenio);
            conveniosFilmeDAO.setDeletar(con, handle_convenio);
            Conexao.fechaConexao(con);
            
            botaoCancelar("n");
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

        jBSalvar = new javax.swing.JButton();
        jBEditar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jBDeletar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTFContato = new javax.swing.JTextField(new documentoSomenteNumerosELetras(32),null,0);
        jLabel8 = new javax.swing.JLabel();
        jTFCep = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##.###-###"));
        jLabel6 = new javax.swing.JLabel();
        jCBUf = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTFEndereco = new javax.swing.JTextField(new documentoSemAspasEPorcento(64),null,0);
        jTFNome = new javax.swing.JTextField(new documentoSemAspasEPorcento(64),null,0);
        jTFCidade = new javax.swing.JTextField(new documentoSomenteNumerosELetras(32),null,0);
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTFEmail = new javax.swing.JTextField(new documentoSemAspasEPorcento(64),null,0);
        jLabel10 = new javax.swing.JLabel();
        jTFTelefone = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("(##) ####-####"))
        ;
        jLabel4 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jBFilme = new javax.swing.JButton();
        jBCh = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTFPorcentConvenio = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("###.##"));
        jTFRedutor = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("###.##"));
        jLabel19 = new javax.swing.JLabel();
        jTFPorcentPaciente = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("###.##"));
        jCBVerificacao_matricula = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jCBArquivoTextoFatura = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jCBGrupo = new javax.swing.JComboBox();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/salvar.png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setEnabled(false);
        jBSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });
        jBSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarKeyReleased(evt);
            }
        });

        jBEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/atualizar.png"))); // NOI18N
        jBEditar.setText("Editar");
        jBEditar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarActionPerformed(evt);
            }
        });
        jBEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBEditarKeyReleased(evt);
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

        jBDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/deletar.png"))); // NOI18N
        jBDeletar.setText("Apagar");
        jBDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDeletarActionPerformed(evt);
            }
        });
        jBDeletar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBDeletarKeyReleased(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "AHAAAAM", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel8.setText("UF");

        jLabel6.setText("Cidade");

        jCBUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));

        jLabel7.setText("CEP");

        jTFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNomeFocusLost(evt);
            }
        });

        jLabel5.setText("Endereço");

        jLabel9.setText("Contato");

        jLabel11.setText("E-Mail");

        jLabel10.setText("Telefone");

        jLabel4.setText("Nome");

        jLabel17.setText("% Paciente");

        jBFilme.setText("Filme");
        jBFilme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBFilmeActionPerformed(evt);
            }
        });
        jBFilme.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBFilmeKeyReleased(evt);
            }
        });

        jBCh.setText("CH");
        jBCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBChActionPerformed(evt);
            }
        });
        jBCh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBChKeyReleased(evt);
            }
        });

        jLabel18.setText("% Convênio");

        jTFPorcentConvenio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPorcentConvenioFocusLost(evt);
            }
        });

        jLabel19.setText("Redutor");

        jTFPorcentPaciente.setEnabled(false);

        jCBVerificacao_matricula.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nenhuma", "Ipê" }));

        jLabel12.setText("Verificação da Matrícula");

        jCBArquivoTextoFatura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não", "Ipê" }));

        jLabel13.setText("Imprimi Arquivo TXT ao gerar Fatura?");

        jLabel14.setText("Grupo");

        jCBGrupo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nenhum" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jTFTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTFContato)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBVerificacao_matricula, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTFPorcentConvenio, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel17)
                                        .addComponent(jTFPorcentPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel19)
                                        .addComponent(jTFRedutor, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(33, 33, 33)
                                    .addComponent(jBCh, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBFilme, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(332, 332, 332)
                                    .addComponent(jLabel12))
                                .addComponent(jTFEndereco)
                                .addComponent(jTFNome)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jTFCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTFCep, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCBGrupo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCBArquivoTextoFatura, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBVerificacao_matricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jCBArquivoTextoFatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jCBGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBFilme, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFRedutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTFPorcentConvenio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTFPorcentPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel17)
                    .addComponent(jLabel19)
                    .addComponent(jBCh, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTFMensagemParaUsuario)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jBCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBDeletar)
                .addGap(0, 0, Short.MAX_VALUE))
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
                        .addComponent(jBSalvar)
                        .addComponent(jBEditar)
                        .addComponent(jBDeletar))
                    .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
botaoCancelar("s");        
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoCancelar("s");
}
    }//GEN-LAST:event_jBCancelarKeyReleased

    private void jBFilmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBFilmeActionPerformed
botaoFilme();
    }//GEN-LAST:event_jBFilmeActionPerformed

    private void jBChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBChActionPerformed
botaoCh();
    }//GEN-LAST:event_jBChActionPerformed

    private void jBChKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBChKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoCh();    
}        // TODO add your handling code here:
    }//GEN-LAST:event_jBChKeyReleased

    private void jBFilmeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBFilmeKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoFilme();    
}         // TODO add your handling code here:
    }//GEN-LAST:event_jBFilmeKeyReleased

    private void jTFNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNomeFocusLost
        
        boolean nomePreenchido = false;
        String semEspaco = jTFNome.getText().replace(" ", "");
        if(semEspaco.length()>=3){
            nomePreenchido = true;
        }else{
            
        }
        
        if("novo".equals(novoOuEditar) && nomePreenchido && handle_convenio==0){
            con = Conexao.fazConexao();
            conveniosMODEL convenioMODEL = new conveniosMODEL();
            convenioMODEL.setNome(jTFNome.getText());
            boolean existe = conveniosDAO.getConsultarParaSalvarNovoRegistro(con, convenioMODEL);
            if(conveniosDAO.conseguiuConsulta){
                if(existe){
                    JOptionPane.showMessageDialog(null, "Convênio já existe.","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    jTFNome.requestFocusInWindow();
                }else{
                    boolean cadastro = conveniosDAO.setCadastrarSomenteNome(con, convenioMODEL);
                    Conexao.fechaConexao(con);
                    if(!cadastro){
                        jTFNome.requestFocusInWindow();
                    }else{
                        con = Conexao.fazConexao();
                        ResultSet resultSet = conveniosDAO.getConsultarIdDeUmNomeCadastrado(con, convenioMODEL);
                        
                        try {
                            while(resultSet.next()){
                                handle_convenio = resultSet.getInt("handle_convenio");
                            }
                            jBCh.setEnabled(true);
                            jBFilme.setEnabled(true);
                            jBSalvar.setEnabled(true);
                            janelaPrincipal.internalFrameJanelaPrincipal.desativandoOMenu();
                            Conexao.fechaConexao(con);
                        } catch (SQLException ex) {
                            jTFNome.requestFocusInWindow();
                            JOptionPane.showMessageDialog(null, "Erro ao verificar handle_convenio. Procure o Administrador","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        }  
    }//GEN-LAST:event_jTFNomeFocusLost

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarActionPerformed
botaoSalvarEAtualizar();
    }//GEN-LAST:event_jBSalvarActionPerformed

    private void jBSalvarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoSalvarEAtualizar();    
}  
    }//GEN-LAST:event_jBSalvarKeyReleased

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEditarActionPerformed
botaoSalvarEAtualizar();
    }//GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBEditarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoSalvarEAtualizar();    
}
    }//GEN-LAST:event_jBEditarKeyReleased

    private void jBDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDeletarActionPerformed
botaoDeletar();        // TODO add your handling code here:
    }//GEN-LAST:event_jBDeletarActionPerformed

    private void jBDeletarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBDeletarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
    botaoDeletar();    
}
    }//GEN-LAST:event_jBDeletarKeyReleased

    private void jTFPorcentConvenioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFPorcentConvenioFocusLost

        String porcentagemConvenioSemEspaço = jTFPorcentConvenio.getText().replace(" ", "");
        if (porcentagemConvenioSemEspaço.length() == 6) {
            if(Double.valueOf(jTFPorcentConvenio.getText()) <= 100.00){
                try {
                double porcentagemConvenio = Double.valueOf(jTFPorcentConvenio.getText());
                String porcentagemPaciente = String.valueOf(100.00 - porcentagemConvenio);
                String[] valorDividido= porcentagemPaciente.split("\\.");

                    if (valorDividido[0].length() == 1) {
                        valorDividido[0] = "00" + valorDividido[0];
                    }else if(valorDividido[0].length() == 2){
                        valorDividido[0] = "0" + valorDividido[0];
                    }

                    if (valorDividido[1].length() == 1) {
                        valorDividido[1] = "00" + valorDividido[1];
                    }else if(valorDividido[1].length() == 2){
                        valorDividido[1] = "0" + valorDividido[1];
                    } 

                jTFPorcentPaciente.setText(String.valueOf(valorDividido[0] + valorDividido[1]));
                } catch (Exception e) {

                }
                jTFMensagemParaUsuario.setText("");
            }else{
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Porcentagem Inválida");
                
                jTFPorcentConvenio.setText("");
                jTFPorcentPaciente.setText("");
            }
            
        }else{
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Porcentagem Inválida"); 
            jTFPorcentConvenio.setText(""); 
            jTFPorcentPaciente.setText(""); 
        }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFPorcentConvenioFocusLost

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBCh;
    private javax.swing.JButton jBDeletar;
    private javax.swing.JButton jBEditar;
    private javax.swing.JButton jBFilme;
    private javax.swing.JButton jBSalvar;
    public static javax.swing.JComboBox jCBArquivoTextoFatura;
    public static javax.swing.JComboBox jCBGrupo;
    public static javax.swing.JComboBox jCBUf;
    public static javax.swing.JComboBox jCBVerificacao_matricula;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JTextField jTFCep;
    public static javax.swing.JTextField jTFCidade;
    public static javax.swing.JTextField jTFContato;
    public static javax.swing.JTextField jTFEmail;
    public static javax.swing.JTextField jTFEndereco;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    public static javax.swing.JTextField jTFPorcentConvenio;
    public static javax.swing.JTextField jTFPorcentPaciente;
    public static javax.swing.JTextField jTFRedutor;
    public static javax.swing.JTextField jTFTelefone;
    // End of variables declaration//GEN-END:variables
}
