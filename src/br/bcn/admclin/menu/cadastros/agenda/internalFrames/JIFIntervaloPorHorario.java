/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCadastrarEditarIntervaloPorHorario.java
 *
 * Created on 15/08/2012, 15:35:09
 */
package br.bcn.admclin.menu.cadastros.agenda.internalFrames;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.documentoSemAspasEPorcento;
import br.bcn.admclin.dao.Conexao;
import br.bcn.admclin.dao.USUARIOS;
import br.bcn.admclin.dao.AGENDAS;
import br.bcn.admclin.dao.A_INTERVALOSPORHORARIO;
import br.bcn.admclin.dao.A_INTERVALOSPORHORARION;
import br.bcn.admclin.dao.model.A_intervalosPorHorario;
import br.bcn.admclin.dao.model.A_intervalosPorHorarioN;

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
import javax.swing.table.DefaultTableModel;
    
/**
 *
 * @author BCN
 */
public class JIFIntervaloPorHorario extends javax.swing.JInternalFrame {
    
    public List<Integer> listaCodAgendas = new ArrayList<Integer>();
    
    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    String novoOuEditar = null;
    public static int intervaloPorHorarioId = 0;
    
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
    
    /** Creates new form JIFCadastrarEditarIntervaloPorHorario */
    public JIFIntervaloPorHorario(String novoOuEditar, int intervaloPorHorarioId) {
        initComponents();
        jTFNome.setDocument(new documentoSemAspasEPorcento(64));
        this.novoOuEditar = novoOuEditar;
        pegandoDataDoSistema();
        
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        
        if("novo".equals(novoOuEditar)){
            jBEditar.setVisible(false);
            jBDeletar.setVisible(false);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de novo Intervalo por Horário", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        }
        if("editar".equals(novoOuEditar)){
            jBSalvar.setVisible(false);
            this.intervaloPorHorarioId = intervaloPorHorarioId;
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Intervalo por Horário", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            preenchendoDadosDoIntervalo();
            preenchendoTabela();
        }
        preenchendoTodasAsAgendasNoComboBox();
        
        jTFHorarioInicial.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFHorarioFinal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tirandoBarraDeTitulo();
        jTable1.setAutoCreateRowSorter(true);
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void preenchendoDadosDoIntervalo(){
        //colocando os valores
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSPORHORARION.getConsultarDadosDeUmIntervaloPorHorario(con, intervaloPorHorarioId);
        try{
            while(resultSet.next()){
                //colocando dados na nos campos
                jTFNome.setText(resultSet.getString("nome"));
                String horarioInicial = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicial"));
                jTFHorarioInicial.setText(horarioInicial);
                String horarioFinal = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinal"));
                jTFHorarioFinal.setText(horarioFinal);
                
                if(resultSet.getInt("seg")==0){
                   jCBSeg.setSelected(false); 
                } else{
                    jCBSeg.setSelected(true); 
                }
                
                if(resultSet.getInt("ter")==0){
                   jCBTer.setSelected(false); 
                } else{
                    jCBTer.setSelected(true); 
                }
                
                if(resultSet.getInt("qua")==0){
                   jCBQua.setSelected(false); 
                } else{
                    jCBQua.setSelected(true); 
                }
                
                if(resultSet.getInt("qui")==0){
                   jCBQui.setSelected(false); 
                } else{
                    jCBQui.setSelected(true); 
                }
                
                if(resultSet.getInt("sex")==0){
                   jCBSex.setSelected(false); 
                } else{
                    jCBSex.setSelected(true); 
                }
                
                if(resultSet.getInt("sab")==0){
                   jCBSab.setSelected(false); 
                } else{
                    jCBSab.setSelected(true); 
                }
                
                if(resultSet.getInt("dom")==0){
                   jCBDom.setSelected(false); 
                } else{
                    jCBDom.setSelected(true); 
                }
            } 
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher dados do Intervalo por Horário. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }
    
    public void preenchendoTabela(){
        boolean preencherTodasAgendas = true;
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSPORHORARIO.getConsultarAgendasDeUmIntervaloPorHorario(con,intervaloPorHorarioId);
        try{
            while(resultSet.next()){
                preencherTodasAgendas = false;
                //colocando dados na tabela
                    modelo.addRow(new String[] {Integer.toString(resultSet.getInt("agendaid")),resultSet.getString("nome")});
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
        boolean diasSelecionados = false;
        boolean AgendaSelecionada = false;
        boolean nomeOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 1, jTFMensagemParaUsuario);
        boolean horarioInicialOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorarioInicial, jTFMensagemParaUsuario, "  :  ");
        boolean horarioFinalOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorarioFinal, jTFMensagemParaUsuario, "  :  ");
        
        
        if(jTable1.getRowCount() == 0){
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione pelo menos uma Agenda");
            AgendaSelecionada = false;
        }else{
            AgendaSelecionada = true;
        }
        
        //verificando se dia foi selecionado
        if(!jCBSeg.isSelected() && !jCBTer.isSelected() && !jCBQua.isSelected() && !jCBQui.isSelected() && !jCBSex.isSelected() && !jCBSab.isSelected() && !jCBDom.isSelected()){
            diasSelecionados = false;
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione pelo menos um dia da semana");
        } else if(jCBSeg.isSelected() || jCBTer.isSelected() || jCBQua.isSelected() || jCBQui.isSelected() || jCBSex.isSelected() || jCBSab.isSelected() || jCBDom.isSelected()){
            diasSelecionados = true;
        }
        
        //verificando horario de inicio e fim se esta correto
        if(horarioFinalOk && horarioInicialOk){
            horarioFinalOk = MetodosUteis.verificarSeHoraEstaCorreta(jTFHorarioFinal, jTFMensagemParaUsuario);
            horarioInicialOk = MetodosUteis.verificarSeHoraEstaCorreta(jTFHorarioInicial, jTFMensagemParaUsuario);
            
            if(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()) >= MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText())){
                horarioInicialOk = false;
                horarioFinalOk = false;
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Horário Inválido");
                jTFHorarioFinal.setBackground(new java.awt.Color(255, 170, 170));
                jTFHorarioInicial.setBackground(new java.awt.Color(255, 170, 170));
            }
        }
        if(diasSelecionados && nomeOk && horarioFinalOk && horarioInicialOk && AgendaSelecionada){
            return true;
        }else{
            return false;
        }
    }

    public void preenchendoTodasAsAgendasNoComboBox(){
        con = Conexao.fazConexao();
        ResultSet resultSet = AGENDAS.getConsultar(con);
        listaCodAgendas.removeAll(listaCodAgendas);
        jCBAgendas.addItem("Todas as Agendas");  
        listaCodAgendas.add(0);
        try{
            while(resultSet.next()){
              jCBAgendas.addItem(resultSet.getString("nome"));
              listaCodAgendas.add(resultSet.getInt("handle_agenda"));
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Agendas. Procure o administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            Conexao.fechaConexao(con);
        }
    }
    
    public void botaoCancelar(){
        this.dispose();
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorario = null;
        
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorarioVisualizar = new JIFIntervaloPorHorarioVisualizar()  ;
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.jDesktopPane1.add(br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorarioVisualizar);
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorarioVisualizar.setVisible(true);
        int lDesk = br.bcn.admclin.janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = br.bcn.admclin.janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorarioVisualizar.getWidth();     
        int aIFrame = br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorarioVisualizar.getHeight();     

        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorHorarioVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    
    public void botaoSalvar(){
        if(verificandoSeTudoFoiPreenchido()){
            if(jTable1.getRowCount() > 0){
                con = Conexao.fazConexao();
                A_intervalosPorHorarioN intervaloPorHorarioMODEL = new A_intervalosPorHorarioN();
                intervaloPorHorarioMODEL.setNome(jTFNome.getText().toUpperCase());
                boolean existe = A_INTERVALOSPORHORARION.getConsultarParaSalvarRegistro(con, intervaloPorHorarioMODEL);
                Conexao.fechaConexao(con);
                if(A_INTERVALOSPORHORARION.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Intervalo por Horário já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        intervaloPorHorarioMODEL.setDat(dataDeHojeEmVariavelDate);
                        intervaloPorHorarioMODEL.setUsuarioId(USUARIOS.usrId);
                        intervaloPorHorarioMODEL.setHorarioInicial(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()));
                        intervaloPorHorarioMODEL.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText()));
                        
                        if(jCBSeg.isSelected()){
                            intervaloPorHorarioMODEL.setSeg(1);
                        }else{
                            intervaloPorHorarioMODEL.setSeg(0);
                        }
                        
                        if(jCBTer.isSelected()){
                            intervaloPorHorarioMODEL.setTer(1);
                        }else{
                            intervaloPorHorarioMODEL.setTer(0);
                        }
                        
                        if(jCBQua.isSelected()){
                            intervaloPorHorarioMODEL.setQua(1);
                        }else{
                            intervaloPorHorarioMODEL.setQua(0);
                        }
                        
                        if(jCBQui.isSelected()){
                            intervaloPorHorarioMODEL.setQui(1);
                        }else{
                            intervaloPorHorarioMODEL.setQui(0);
                        }
                        
                        if(jCBSex.isSelected()){
                            intervaloPorHorarioMODEL.setSex(1);
                        }else{
                            intervaloPorHorarioMODEL.setSex(0);
                        }
                        
                        if(jCBSab.isSelected()){
                            intervaloPorHorarioMODEL.setSab(1);
                        }else{
                            intervaloPorHorarioMODEL.setSab(0);
                        }
                        
                        if(jCBDom.isSelected()){
                            intervaloPorHorarioMODEL.setDom(1);
                        }else{
                            intervaloPorHorarioMODEL.setDom(0);
                        }
                        
                        boolean cadastro = A_INTERVALOSPORHORARION.setCadastrar(con, intervaloPorHorarioMODEL);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            //pegando id do intervalo cadastrado
                            con = Conexao.fazConexao();
                            A_intervalosPorHorarioN intervaloPorHorarionModel = new A_intervalosPorHorarioN();
                            intervaloPorHorarionModel.setNome(jTFNome.getText().toUpperCase());
                            int idIntervalo = A_INTERVALOSPORHORARION.getConsultarIdDeUmNomeCadastrado(con, intervaloPorHorarionModel);
                            
                            //salvando as agendas
                            A_intervalosPorHorario intervaloPorHorarioModel = new A_intervalosPorHorario();
                            intervaloPorHorarioModel.setA_intervaloPorHorarioNId(idIntervalo);
                            
                            
                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while(i<numeroDeLinhasNaTabela){
                                intervaloPorHorarioModel.setAgendaId(Integer.valueOf((String)jTable1.getValueAt(i, 0)));
                                A_INTERVALOSPORHORARIO.setCadastrar(con, intervaloPorHorarioModel);
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
            modelo.addRow(new String[] {String.valueOf(listaCodAgendas.get(jCBAgendas.getSelectedIndex())), String.valueOf(jCBAgendas.getSelectedItem())});
        }else{
            
            if(jTable1.getRowCount() > 0){
                if(Integer.valueOf((String)jTable1.getValueAt(0, 0)) == 0){
                ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
                jTable1.updateUI();
                }
            }
            
            
            if(verificandoSeAgendaJaEstaCadastrada(listaCodAgendas.get(jCBAgendas.getSelectedIndex()))){
                
            }else{
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(new String[] {String.valueOf(listaCodAgendas.get(jCBAgendas.getSelectedIndex())), String.valueOf(jCBAgendas.getSelectedItem())});
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
                int agendaIdSendoCadastrada = listaCodAgendas.get(jCBAgendas.getSelectedIndex());
                
                if(agendaIdDaTabela == agendaIdSendoCadastrada){
                    AgendaJaFoiCadastrada = true;
                }

                i++;
            }
        }
        return AgendaJaFoiCadastrada;
        
    }
    
    public void botaoDeletar(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Intervalo por Horário?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            con = Conexao.fazConexao();
            A_INTERVALOSPORHORARIO.setDeletar(con, intervaloPorHorarioId);
            A_INTERVALOSPORHORARION.setDeletar(con, intervaloPorHorarioId);
            Conexao.fechaConexao(con);
            
            botaoCancelar();
        }
        
    }
    
    public void botaoAtualizar(){
        if(verificandoSeTudoFoiPreenchido()){
            
                con = Conexao.fazConexao();
                A_intervalosPorHorarioN intervaloModel = new A_intervalosPorHorarioN();
                intervaloModel.setNome(jTFNome.getText().toUpperCase());
                intervaloModel.setA_intervaloPorHorarioNId(intervaloPorHorarioId);
                boolean existe = A_INTERVALOSPORHORARION.getConsultarParaAtualizarRegistro(con, intervaloModel);
                Conexao.fechaConexao(con);
                if(A_INTERVALOSPORHORARION.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Intervalo po Horário já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        intervaloModel.setDat(dataDeHojeEmVariavelDate);
                        intervaloModel.setUsuarioId(USUARIOS.usrId);
                        intervaloModel.setHorarioInicial(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()));
                        intervaloModel.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText()));
                        intervaloModel.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText()));
                        
                        if(jCBSeg.isSelected()){
                            intervaloModel.setSeg(1);
                        }else{
                            intervaloModel.setSeg(0);
                        }
                        
                        if(jCBTer.isSelected()){
                            intervaloModel.setTer(1);
                        }else{
                            intervaloModel.setTer(0);
                        }
                        
                        if(jCBQua.isSelected()){
                            intervaloModel.setQua(1);
                        }else{
                            intervaloModel.setQua(0);
                        }
                        
                        if(jCBQui.isSelected()){
                            intervaloModel.setQui(1);
                        }else{
                            intervaloModel.setQui(0);
                        }
                        
                        if(jCBSex.isSelected()){
                            intervaloModel.setSex(1);
                        }else{
                            intervaloModel.setSex(0);
                        }
                        
                        if(jCBSab.isSelected()){
                            intervaloModel.setSab(1);
                        }else{
                            intervaloModel.setSab(0);
                        }
                        
                        if(jCBDom.isSelected()){
                            intervaloModel.setDom(1);
                        }else{
                            intervaloModel.setDom(0);
                        }
                        
                        boolean cadastro = A_INTERVALOSPORHORARION.setAtualizar(con, intervaloModel);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            //deletando as agendas
                            con = Conexao.fazConexao();
                            A_INTERVALOSPORHORARIO.setDeletar(con, intervaloPorHorarioId);
                            
                            //cadastrando novas agendas
                            A_intervalosPorHorario intervaloPorHorarioModel = new A_intervalosPorHorario();
                            intervaloPorHorarioModel.setA_intervaloPorHorarioNId(intervaloPorHorarioId);
                            
                            
                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while(i<numeroDeLinhasNaTabela){
                                intervaloPorHorarioModel.setAgendaId(Integer.valueOf((String)jTable1.getValueAt(i, 0)));
                                A_INTERVALOSPORHORARIO.setCadastrar(con, intervaloPorHorarioModel);
                                i++;
                            }
                            
                            Conexao.fechaConexao(con);
                            
                            
                            botaoCancelar();
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
        jTFNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTFHorarioInicial = new JFormattedTextField(new br.bcn.admclin.ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##:##"));
        jTFHorarioFinal = new JFormattedTextField(new br.bcn.admclin.ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##:##"));
        jLabel3 = new javax.swing.JLabel();
        jCBSab = new javax.swing.JCheckBox();
        jCBDom = new javax.swing.JCheckBox();
        jCBQui = new javax.swing.JCheckBox();
        jCBSex = new javax.swing.JCheckBox();
        jCBTer = new javax.swing.JCheckBox();
        jCBSeg = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jCBAgendas = new javax.swing.JComboBox();
        jBIncluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jCBQua = new javax.swing.JCheckBox();

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18));
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/salvar.png"))); // NOI18N
        jBSalvar.setText("Salvar");
        jBSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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
        jBEditar.setText("Editar");
        jBEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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
        jBDeletar.setText("Deletar");
        jBDeletar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBDeletar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "aaa", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFNomeKeyReleased(evt);
            }
        });

        jLabel1.setText("Horário Inicial");

        jLabel2.setText("Horário Final");

        jLabel5.setText("Dias do Intervalo");

        jLabel3.setText("Nome");

        jCBSab.setText("Sáb");

        jCBDom.setText("Dom");

        jCBQui.setText("Qui");

        jCBSex.setText("Sex");

        jCBTer.setText("Ter");

        jCBSeg.setText("Seg");

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
                "AgendaId", "Agendas Cadastradas"
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );

        jCBQua.setText("Qua");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(42, 42, 42)
                        .addComponent(jTFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
                    .addComponent(jLabel5)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jCBSeg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCBTer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBQua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCBQui)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBSex)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBSab)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBDom))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTFHorarioInicial))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTFHorarioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTFHorarioInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTFHorarioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBSeg)
                            .addComponent(jCBTer)
                            .addComponent(jCBQua)
                            .addComponent(jCBQui)
                            .addComponent(jCBSex)
                            .addComponent(jCBSab)
                            .addComponent(jCBDom)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBDeletar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar))
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBSalvar)
                    .addComponent(jBEditar)
                    .addComponent(jBDeletar)
                    .addComponent(jBCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNomeKeyReleased

    }//GEN-LAST:event_jTFNomeKeyReleased

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
        jTFHorarioFinal.setBackground(new java.awt.Color(255,255,255));
        jTFHorarioInicial.setBackground(new java.awt.Color(255,255,255));     // TODO add your handling code here:
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
        jTFHorarioFinal.setBackground(new java.awt.Color(255,255,255));
        jTFHorarioInicial.setBackground(new java.awt.Color(255,255,255));
    }//GEN-LAST:event_jBEditarFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBDeletar;
    public static javax.swing.JButton jBEditar;
    private javax.swing.JButton jBIncluir;
    public static javax.swing.JButton jBSalvar;
    public static javax.swing.JComboBox jCBAgendas;
    public static javax.swing.JCheckBox jCBDom;
    public static javax.swing.JCheckBox jCBQua;
    public static javax.swing.JCheckBox jCBQui;
    public static javax.swing.JCheckBox jCBSab;
    public static javax.swing.JCheckBox jCBSeg;
    public static javax.swing.JCheckBox jCBSex;
    public static javax.swing.JCheckBox jCBTer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextField jTFHorarioFinal;
    public static javax.swing.JTextField jTFHorarioInicial;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    public static javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
