/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCadastrarEditarIntervaloPorHorario.java
 *
 * Created on 15/08/2012, 15:35:09
 */
package menu.cadastros.agenda.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.documentoSomenteNumerosELetras;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.AGENDAS;
import br.bcn.admclin.dao.A_FERIADOS;
import br.bcn.admclin.dao.A_FERIADOSN;
import br.bcn.admclin.model.A_feriados;
import br.bcn.admclin.model.A_feriadosN;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
    
/**
 *
 * @author BCN
 */
public class JIFFeriado extends javax.swing.JInternalFrame {
    
    public List<Integer> listaHandleAgendas = new ArrayList<Integer>();
    
    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    String novoOuEditar = null;
    public static int handleFeriadoN = 0;
    
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
    
    /** Creates new form JIFCadastrarEditarferiado */
    public JIFFeriado(String novoOuEditar, int handleFeriadoN) {
        initComponents();
        
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        
        this.novoOuEditar = novoOuEditar;
        pegandoDataDoSistema();
        
        if("novo".equals(novoOuEditar)){
            jBEditar.setVisible(false);
            jBDeletar.setVisible(false);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Feriado", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
        if("editar".equals(novoOuEditar)){
            jBSalvar.setVisible(false);
            this.handleFeriadoN = handleFeriadoN;
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Feriado", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            preenchendoDadosDoIntervalo();
            preenchendoTabela();
        }
        preenchendoTodasAsAgendasNoComboBox();
        
        jTFDiaDoIntervalo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setRowHeight(20);
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void preenchendoDadosDoIntervalo(){
        //colocando os valores
        con = Conexao.fazConexao();
        ResultSet resultSet = A_FERIADOSN.getConsultarDadosDeUmFeriado(con, handleFeriadoN);
        try{
            while(resultSet.next()){
                //colocando dados na nos campos
                jTFNome.setText(resultSet.getString("nome"));
                jTADescricao.setText(resultSet.getString("descricao"));
                jTFDiaDoIntervalo.setText(resultSet.getString("diaDoferiado"));
            } 
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher dados do Feriado. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }
    
    public void preenchendoTabela(){
        boolean preencherTodasAgendas = true;
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = A_FERIADOS.getConsultarAgendasDeUmFeriado(con,handleFeriadoN);
        try{
            while(resultSet.next()){
                preencherTodasAgendas = false;
                //colocando dados na tabela
                    modelo.addRow(new String[] {Integer.toString(resultSet.getInt("handle_agenda")),resultSet.getString("nome")});
            } 

            if(preencherTodasAgendas){
                    modelo.addRow(new String[] {"0","Todas as Agendas"});
            }

        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Agendas. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    
    public boolean verificandoSeTudoFoiPreenchido(){
        boolean AgendaSelecionada = false;
        boolean nomeOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        boolean diaDoIntervaloOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFDiaDoIntervalo, jTFMensagemParaUsuario, "  /  ");
        
        
        if(jTable1.getRowCount() == 0){
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione pelo menos uma Agenda");
            AgendaSelecionada = false;
        }else{
            AgendaSelecionada = true;
        }
        
        //verificar se dia do feriado foi preenchido corretamente
        if(diaDoIntervaloOk){
            boolean mesOk = false;
            String[] diaEMes = jTFDiaDoIntervalo.getText().split("/");
            
            int dia = Integer.valueOf(diaEMes[0]);
            int mes = Integer.valueOf(diaEMes[1]);
            
            //verificando se mes esta ok
            if(mes < 1 || mes > 12){
                jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Data Inválida");
            }else{
                mesOk = true;
            }
            
            //se mes esta ok agora vamos verificar o dia
            if(mesOk){
                if(mes==1 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                //mes fevereiro
                if(mes==2 && (dia < 1 || dia > 29)){
                            diaDoIntervaloOk = false;
                            jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                            jTFMensagemParaUsuario.setText("Data Inválida");
                        }
                
                
                //mes março
                if(mes==3 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==4 && (dia < 1 || dia > 30)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==5 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==6 && (dia < 1 || dia > 30)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==7 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==8 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==9 && (dia < 1 || dia > 30)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==10 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==11 && (dia < 1 || dia > 30)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
                
                if(mes==12 && (dia < 1 || dia > 31)){
                    diaDoIntervaloOk = false;
                    jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 170, 170));
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Data Inválida");
                }
            }else{
                diaDoIntervaloOk = false;
            }
            
            
        }
                
        
        if(nomeOk && diaDoIntervaloOk && AgendaSelecionada){
            return true;
        }else{
            return false;
        }
    }

    public void preenchendoTodasAsAgendasNoComboBox(){
        con = Conexao.fazConexao();
        ResultSet resultSet = AGENDAS.getConsultar(con);
        listaHandleAgendas.removeAll(listaHandleAgendas);
        jCBAgendas.addItem("Todas as Agendas");  
        listaHandleAgendas.add(0);
        try{
            while(resultSet.next()){
              jCBAgendas.addItem(resultSet.getString("nome"));
              listaHandleAgendas.add(resultSet.getInt("HANDLE_AGENDA"));
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possível preencher as Agendas. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            Conexao.fechaConexao(con);
        }
    }
    
    public void botaoCancelar(){
        this.dispose();
        janelaPrincipal.internalFrameFeriado = null;
        
        janelaPrincipal.internalFrameFeriadoVisualizar = new JIFFeriadoVisualizar()  ;
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameFeriadoVisualizar);
        janelaPrincipal.internalFrameFeriadoVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = janelaPrincipal.internalFrameFeriadoVisualizar.getWidth();     
        int aIFrame = janelaPrincipal.internalFrameFeriadoVisualizar.getHeight();     

        janelaPrincipal.internalFrameFeriadoVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    
    public void botaoSalvar(){
        if(verificandoSeTudoFoiPreenchido()){
            if(jTable1.getRowCount() > 0){
                con = Conexao.fazConexao();
                A_feriadosN feriadoNModel = new A_feriadosN();
                feriadoNModel.setNome(jTFNome.getText().toUpperCase());
                boolean existe = A_FERIADOSN.getConsultarParaSalvarRegistro(con, feriadoNModel);
                Conexao.fechaConexao(con);
                if(A_FERIADOSN.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Feriado já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        feriadoNModel.setDat(dataDeHojeEmVariavelDate);
                        feriadoNModel.setUsuarioId(USUARIOS.usrId);
                        feriadoNModel.setDescricao(jTADescricao.getText());
                        feriadoNModel.setDiaDoFeriado(jTFDiaDoIntervalo.getText());
                        
                        boolean cadastro = A_FERIADOSN.setCadastrar(con, feriadoNModel);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            //pegando id do intervalo cadastrado
                            con = Conexao.fazConexao();
                            A_feriadosN feriadoNMODEL = new A_feriadosN();
                            feriadoNMODEL.setNome(jTFNome.getText().toUpperCase());
                            handleFeriadoN = A_FERIADOSN.getConsultarIdDeUmNomeCadastrado(con, feriadoNMODEL);
                            
                            //salvando as agendas
                            A_feriados feriadoModel = new A_feriados();
                            feriadoModel.setHandleFeriadoN(handleFeriadoN);
                            
                            
                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while(i<numeroDeLinhasNaTabela){
                                feriadoModel.setHandleAgenda(Integer.valueOf((String)jTable1.getValueAt(i, 0)));
                                A_FERIADOS.setCadastrar(con, feriadoModel);
                                i++;
                            }
                            
                            Conexao.fechaConexao(con);
                            
                            botaoCancelar();
                        }
                    }
                }
            }
        }
    }
    
    public void botaoIncluir(){
        if(jCBAgendas.getSelectedIndex() == 0){
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.addRow(new String[] {String.valueOf(listaHandleAgendas.get(jCBAgendas.getSelectedIndex())), String.valueOf(jCBAgendas.getSelectedItem())});
        }else{
            
            if(jTable1.getRowCount() > 0){
                if(Integer.valueOf((String)jTable1.getValueAt(0, 0)) == 0){
                ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
                jTable1.updateUI();
                }
            }
            
            
            if(verificandoSeAgendaJaEstaCadastrada(listaHandleAgendas.get(jCBAgendas.getSelectedIndex()))){
                
            }else{
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] {String.valueOf(listaHandleAgendas.get(jCBAgendas.getSelectedIndex())), String.valueOf(jCBAgendas.getSelectedItem())});
            }
            
        }
        
    }
    
    public boolean verificandoSeAgendaJaEstaCadastrada(int agendaId){
        boolean AgendaJaFoiCadastrada = false;
       
        if(jTable1.getRowCount() > 0){
            int i = 0;
            int numeroDeLinhasNaTabela = jTable1.getRowCount();
            
            while(i<numeroDeLinhasNaTabela){
                
                int agendaIdDaTabela = Integer.valueOf((String)jTable1.getValueAt(i, 0));
                int agendaIdSendoCadastrada = listaHandleAgendas.get(jCBAgendas.getSelectedIndex());
                
                if(agendaIdDaTabela == agendaIdSendoCadastrada){
                    AgendaJaFoiCadastrada = true;
                }

                i++;
            }
        }
        return AgendaJaFoiCadastrada;
        
    }
    
    public void botaoDeletar(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Feriado?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            con = Conexao.fazConexao();
            A_FERIADOS.setDeletar(con, handleFeriadoN);
            A_FERIADOSN.setDeletar(con, handleFeriadoN);
            Conexao.fechaConexao(con);
            
            botaoCancelar();
        }
        
    }
    
    public void botaoAtualizar(){
        if(verificandoSeTudoFoiPreenchido()){
            if(jTable1.getRowCount() > 0){
                con = Conexao.fazConexao();
                A_feriadosN feriadoNModel = new A_feriadosN();
                feriadoNModel.setNome(jTFNome.getText().toUpperCase());
                feriadoNModel.setHandleFeriadoN(handleFeriadoN);
                boolean existe = A_FERIADOSN.getConsultarParaAtualizarRegistro(con, feriadoNModel);
                Conexao.fechaConexao(con);
                if(A_FERIADOSN.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Feriado já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        feriadoNModel.setDat(dataDeHojeEmVariavelDate);
                        feriadoNModel.setUsuarioId(USUARIOS.usrId);
                        feriadoNModel.setDescricao(jTADescricao.getText());
                        feriadoNModel.setDiaDoFeriado(jTFDiaDoIntervalo.getText());
                       
                        
                        
                        
                        boolean cadastro = A_FERIADOSN.setAtualizar(con, feriadoNModel);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            //deletando as agendas
                            con = Conexao.fazConexao();
                            A_FERIADOS.setDeletar(con, handleFeriadoN);
                            
                            //cadastrando novas agendas
                            A_feriados feriadosModel = new A_feriados();
                            feriadosModel.setHandleFeriadoN(handleFeriadoN);
                            
                            
                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while(i<numeroDeLinhasNaTabela){
                                feriadosModel.setHandleAgenda(Integer.valueOf((String)jTable1.getValueAt(i, 0)));
                                A_FERIADOS.setCadastrar(con, feriadosModel);
                                i++;
                            }
                            
                            Conexao.fechaConexao(con);
                            
                            
                            botaoCancelar();
                        }
                    }
                }
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

        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBSalvar = new javax.swing.JButton();
        jBEditar = new javax.swing.JButton();
        jBDeletar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTFNome = new javax.swing.JTextField(new documentoSemAspasEPorcento(64),null,0);
        jLabel1 = new javax.swing.JLabel();
        jTFDiaDoIntervalo = new JFormattedTextField(new ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##/##"));
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jCBAgendas = new javax.swing.JComboBox();
        jBIncluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTADescricao = new javax.swing.JTextArea(new documentoSemAspasEPorcento(500));
        jLabel5 = new javax.swing.JLabel();

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/salvar.png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarActionPerformed(evt);
            }
        });
        jBSalvar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarFocusLost(evt);
            }
        });
        jBSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarKeyReleased(evt);
            }
        });

        jBEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/atualizar.png"))); // NOI18N
        jBEditar.setText("Atualizar");
        jBEditar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEditarActionPerformed(evt);
            }
        });
        jBEditar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBEditarFocusLost(evt);
            }
        });
        jBEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBEditarKeyReleased(evt);
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "aaa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNomeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNomeFocusLost(evt);
            }
        });

        jLabel1.setText("Dia do Feriado");

        jTFDiaDoIntervalo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFDiaDoIntervaloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFDiaDoIntervaloFocusLost(evt);
            }
        });

        jLabel3.setText("Nome");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agendas que utilizam o Intervalo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jBIncluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/imagemSetaParaBaixo.png"))); // NOI18N
        jBIncluir.setText("Incluir");
        jBIncluir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jBIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBIncluirActionPerformed(evt);
            }
        });
        jBIncluir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBIncluirKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "handle Agenda", "Agendas Cadastradas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBAgendas, 0, 349, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(jBIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBAgendas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTADescricao.setColumns(20);
        jTADescricao.setRows(5);
        jScrollPane2.setViewportView(jTADescricao);

        jLabel5.setText("Descrição");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTFDiaDoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 198, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(jTFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTFDiaDoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(jBSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBIncluirActionPerformed
        botaoIncluir();
    }//GEN-LAST:event_jBIncluirActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
           botaoCancelar();
        }    
    }//GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarActionPerformed
        botaoSalvar();
    }//GEN-LAST:event_jBSalvarActionPerformed

    private void jBSalvarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
           botaoSalvar();
        }    
    }//GEN-LAST:event_jBSalvarKeyReleased

    private void jBSalvarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBSalvarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255,255,255));
        jTFDiaDoIntervalo.setBackground(new java.awt.Color(255,255,255));     // TODO add your handling code here:
    }//GEN-LAST:event_jBSalvarFocusLost

    private void jBIncluirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBIncluirKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
           botaoIncluir();
        }    
    }//GEN-LAST:event_jBIncluirKeyReleased

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
        int linha = jTable1.getSelectedRow();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.removeRow(linha);
        
        jBIncluir.requestFocusInWindow();
    }//GEN-LAST:event_jTable1FocusGained

    private void jBDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDeletarActionPerformed
        botaoDeletar();
    }//GEN-LAST:event_jBDeletarActionPerformed

    private void jBDeletarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBDeletarKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
           botaoDeletar();
        }
    }//GEN-LAST:event_jBDeletarKeyReleased

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEditarActionPerformed
        botaoAtualizar();
    }//GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBEditarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){  
           botaoAtualizar();
        }
    }//GEN-LAST:event_jBEditarKeyReleased

    private void jBEditarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBEditarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255,255,255));
        jTFDiaDoIntervalo.setBackground(new java.awt.Color(255,255,255));   
    }//GEN-LAST:event_jBEditarFocusLost

    private void jTFNomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNomeFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_jTFNomeFocusGained

    private void jTFNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFNomeFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        jTFMensagemParaUsuario.setText("");
        if (ok) {
            jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        }
    }//GEN-LAST:event_jTFNomeFocusLost

    private void jTFDiaDoIntervaloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFDiaDoIntervaloFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("dd/mm");
        jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_jTFDiaDoIntervaloFocusGained

    private void jTFDiaDoIntervaloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFDiaDoIntervaloFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFDiaDoIntervalo, jTFMensagemParaUsuario, "  /  ");
        jTFMensagemParaUsuario.setText("");
        if (ok) {
            jTFDiaDoIntervalo.setBackground(new java.awt.Color(255, 255, 255));
        }
    }//GEN-LAST:event_jTFDiaDoIntervaloFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBDeletar;
    public static javax.swing.JButton jBEditar;
    private javax.swing.JButton jBIncluir;
    public static javax.swing.JButton jBSalvar;
    public static javax.swing.JComboBox jCBAgendas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTADescricao;
    public static javax.swing.JTextField jTFDiaDoIntervalo;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    public static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
