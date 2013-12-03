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
import br.bcn.admclin.dao.A_INTERVALOSPORPERIODO;
import br.bcn.admclin.dao.A_INTERVALOSPORPERIODON;
import br.bcn.admclin.dao.model.A_intervalosPorPeriodo;
import br.bcn.admclin.dao.model.A_intervalosPorPeriodoN;

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
import java.util.Date;
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
public class JIFIntervaloPorPeriodo extends javax.swing.JInternalFrame {
    
    public List<Integer> listaCodAgendas = new ArrayList<Integer>();
    
    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    String novoOuEditar = null;
    public static int intervaloPorPeriodoId = 0;
    
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
    public JIFIntervaloPorPeriodo(String novoOuEditar, int intervaloPorPeriodoId) {
        initComponents();
        jTFNome.setDocument(new documentoSemAspasEPorcento(64));
        jTADescricao.setDocument(new documentoSemAspasEPorcento(500));
        jXDatePicker1.setFormats(new String [] { "E dd/MM/yyyy" }); 
        jXDatePicker2.setFormats(new String [] { "E dd/MM/yyyy" }); 
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Ir para data atual");
        jXDatePicker2.setLinkDate(System.currentTimeMillis(), "Ir para data atual");
        
        jTable1.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable1.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        
        this.novoOuEditar = novoOuEditar;
        pegandoDataDoSistema();
        
        if("novo".equals(novoOuEditar)){
            jBEditar.setVisible(false);
            jBDeletar.setVisible(false);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de novo Intervalo por Período", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }
        if("editar".equals(novoOuEditar)){
            jBSalvar.setVisible(false);
            this.intervaloPorPeriodoId = intervaloPorPeriodoId;
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Intervalo por Período", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
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
        ResultSet resultSet = A_INTERVALOSPORPERIODON.getConsultarDadosDeUmIntervaloPorHorario(con, intervaloPorPeriodoId);
        try{
            while(resultSet.next()){
                //colocando dados na nos campos
                jTFNome.setText(resultSet.getString("nome"));
                jTADescricao.setText(resultSet.getString("descricao"));
                jXDatePicker1.setDate(resultSet.getDate("diainicial"));
                jXDatePicker2.setDate(resultSet.getDate("diafinal"));
                String horarioInicial = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicial"));
                jTFHorarioInicial.setText(horarioInicial);
                String horarioFinal = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinal"));
                jTFHorarioFinal.setText(horarioFinal);
            } 
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher dados do Intervalo por Período. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }
    
    public void preenchendoTabela(){
        boolean preencherTodasAgendas = true;
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = A_INTERVALOSPORPERIODO.getConsultarAgendasDeUmIntervaloDiario(con,intervaloPorPeriodoId);
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
        boolean AgendaSelecionada;
        boolean nomeOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 1, jTFMensagemParaUsuario);
        boolean horarioInicialOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorarioInicial, jTFMensagemParaUsuario, "  :  ");
        boolean horarioFinalOk = MetodosUteis.verificarSeCampoComMascaraFoiPrenchido(jTFHorarioFinal, jTFMensagemParaUsuario, "  :  ");
        boolean dataOk; 
        
        
        if(jTable1.getRowCount() == 0){
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Selecione pelo menos uma Agenda");
            AgendaSelecionada = false;
        }else{
            AgendaSelecionada = true;
        }
        
               
        //verificando horario de inicio e fim se esta correto
        if(horarioFinalOk && horarioInicialOk){
            horarioFinalOk = MetodosUteis.verificarSeHoraEstaCorreta(jTFHorarioFinal, jTFMensagemParaUsuario);
            horarioInicialOk = MetodosUteis.verificarSeHoraEstaCorreta(jTFHorarioInicial, jTFMensagemParaUsuario);
        }
        
        //verificando se data final é maior que data inicial
        int dataInicial, dataFinal;
        //criando um formato de data
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        //pegando data inicial

                        Date dataSelecionada = jXDatePicker1.getDate();
                        String diaInicial = format.format(dataSelecionada);  
                        dataInicial = Integer.valueOf(diaInicial);
                        
                        //pegando data final
                        Date dataSelecionada2 = jXDatePicker2.getDate();
                        String diaFinal = format.format(dataSelecionada2);
                        dataFinal = Integer.valueOf(diaFinal);
                        
        
        if(dataInicial > dataFinal){
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Data final maior que a data inicial");
            dataOk = false;
        }else if(dataInicial < dataFinal){
            dataOk = true;
        } else{
            if(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()) >= MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText())){
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Horário Inválido");
                dataOk = false;
                jTFHorarioFinal.setBackground(new java.awt.Color(255, 170, 170));
                jTFHorarioInicial.setBackground(new java.awt.Color(255, 170, 170));
            }else{
                dataOk = true;
            }
        }
                        
        
        
        if(nomeOk && horarioFinalOk && horarioInicialOk && AgendaSelecionada && dataOk){
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
            JOptionPane.showMessageDialog(null, "Não foi possível preencher as Agendas. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            Conexao.fechaConexao(con);
        }
    }
    
    public void botaoCancelar(){
        this.dispose();
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodo = null;
        
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodoVisualizar = new JIFIntervaloPorPeriodoVisualizar()  ;
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.jDesktopPane1.add(br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodoVisualizar);
        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodoVisualizar.setVisible(true);
        int lDesk = br.bcn.admclin.janelaPrincipal.janelaPrincipal.jDesktopPane1.getWidth();     
        int aDesk = br.bcn.admclin.janelaPrincipal.janelaPrincipal.jDesktopPane1.getHeight();     
        int lIFrame = br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodoVisualizar.getWidth();     
        int aIFrame = br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodoVisualizar.getHeight();     

        br.bcn.admclin.janelaPrincipal.janelaPrincipal.internalFrameIntervaloPorPeriodoVisualizar.setLocation( lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2 );
    }
    
    public void botaoSalvar(){
        if(verificandoSeTudoFoiPreenchido()){
            if(jTable1.getRowCount() > 0){
                con = Conexao.fazConexao();
                A_intervalosPorPeriodoN intervaloPorPeriodoNModel = new A_intervalosPorPeriodoN();
                intervaloPorPeriodoNModel.setNome(jTFNome.getText().toUpperCase());
                boolean existe = A_INTERVALOSPORPERIODON.getConsultarParaSalvarRegistro(con, intervaloPorPeriodoNModel);
                Conexao.fechaConexao(con);
                if(A_INTERVALOSPORPERIODON.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Intervalo por Período já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        intervaloPorPeriodoNModel.setDat(dataDeHojeEmVariavelDate);
                        intervaloPorPeriodoNModel.setUsuarioId(USUARIOS.usrId);
                        intervaloPorPeriodoNModel.setDescricao(jTADescricao.getText());
                        intervaloPorPeriodoNModel.setHorarioInicial(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()));
                        intervaloPorPeriodoNModel.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText()));
                        
                        //pegando data inicial
                        Date dataSelecionada = jXDatePicker1.getDate();
                        //criando um formato de data
                        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy"); 
                        //colocando data selecionado no formato criado acima
                        String diaDoIntervalo = dataFormatada.format(dataSelecionada);
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
                        try{
                            java.sql.Date diaDoIntervaloCorrreto = new java.sql.Date(format.parse(diaDoIntervalo).getTime());
                            intervaloPorPeriodoNModel.setDiaInicial(diaDoIntervaloCorrreto);
                        }catch(ParseException e){
                            JOptionPane.showMessageDialog(null, "Erro com a data inicial. Procure o Administrador.");
                        }
                        
                        //pegando data final
                        Date dataSelecionada2 = jXDatePicker2.getDate();
                        //criando um formato de data
                        SimpleDateFormat dataFormatada2 = new SimpleDateFormat("dd/MM/yyyy"); 
                        //colocando data selecionado no formato criado acima
                        String diaDoIntervalo2 = dataFormatada2.format(dataSelecionada2);
                        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");  
                        try{
                            java.sql.Date diaDoIntervaloCorrreto2 = new java.sql.Date(format2.parse(diaDoIntervalo2).getTime());
                            intervaloPorPeriodoNModel.setDiaFinal(diaDoIntervaloCorrreto2);
                        }catch(ParseException e){
                            JOptionPane.showMessageDialog(null, "Erro com a data final. Procure o Administrador.");
                        }
                        
                        
                        
                        boolean cadastro = A_INTERVALOSPORPERIODON.setCadastrar(con, intervaloPorPeriodoNModel);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            //pegando id do intervalo cadastrado
                            con = Conexao.fazConexao();
                            A_intervalosPorPeriodoN intervaloPorPeriodoNMODEL = new A_intervalosPorPeriodoN();
                            intervaloPorPeriodoNMODEL.setNome(jTFNome.getText().toUpperCase());
                            int idIntervalo = A_INTERVALOSPORPERIODON.getConsultarIdDeUmNomeCadastrado(con, intervaloPorPeriodoNMODEL);
                            
                            //salvando as agendas
                            A_intervalosPorPeriodo intervaloPorPeriodoModel = new A_intervalosPorPeriodo();
                            intervaloPorPeriodoModel.setA_intervaloPorPeriodoNId(idIntervalo);
                            
                            
                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while(i<numeroDeLinhasNaTabela){
                                intervaloPorPeriodoModel.setAgendaId(Integer.valueOf((String)jTable1.getValueAt(i, 0)));
                                A_INTERVALOSPORPERIODO.setCadastrar(con, intervaloPorPeriodoModel);
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
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar esse Intervalo Diário?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            con = Conexao.fazConexao();
            A_INTERVALOSPORPERIODO.setDeletar(con, intervaloPorPeriodoId);
            A_INTERVALOSPORPERIODON.setDeletar(con, intervaloPorPeriodoId);
            Conexao.fechaConexao(con);
            
            botaoCancelar();
        }
        
    }
    
    public void botaoAtualizar(){
        if(verificandoSeTudoFoiPreenchido()){
            if(jTable1.getRowCount() > 0){
                con = Conexao.fazConexao();
                A_intervalosPorPeriodoN intervaloPorPeriodoNModel = new A_intervalosPorPeriodoN();
                intervaloPorPeriodoNModel.setNome(jTFNome.getText().toUpperCase());
                intervaloPorPeriodoNModel.setA_intervaloPorPeriodoNId(intervaloPorPeriodoId);
                boolean existe = A_INTERVALOSPORPERIODON.getConsultarParaAtualizarRegistro(con, intervaloPorPeriodoNModel);
                Conexao.fechaConexao(con);
                if(A_INTERVALOSPORPERIODON.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Intervalo por Período já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        intervaloPorPeriodoNModel.setDat(dataDeHojeEmVariavelDate);
                        intervaloPorPeriodoNModel.setUsuarioId(USUARIOS.usrId);
                        intervaloPorPeriodoNModel.setDescricao(jTADescricao.getText());
                        intervaloPorPeriodoNModel.setHorarioInicial(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioInicial.getText()));
                        intervaloPorPeriodoNModel.setHorarioFinal(MetodosUteis.transformarHorarioEmMinutos(jTFHorarioFinal.getText()));
                        
                        //pegando data inicial
                        Date dataSelecionada = jXDatePicker1.getDate();
                        //criando um formato de data
                        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy"); 
                        //colocando data selecionado no formato criado acima
                        String diaDoIntervalo = dataFormatada.format(dataSelecionada);
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
                        try{
                            java.sql.Date diaDoIntervaloCorrreto = new java.sql.Date(format.parse(diaDoIntervalo).getTime());
                            intervaloPorPeriodoNModel.setDiaInicial(diaDoIntervaloCorrreto);
                        }catch(ParseException e){
                            JOptionPane.showMessageDialog(null, "Erro com a data inicial. Procure o Administrador.");
                        }
                        
                        //pegando data final
                        Date dataSelecionada2 = jXDatePicker2.getDate();
                        //criando um formato de data
                        SimpleDateFormat dataFormatada2 = new SimpleDateFormat("dd/MM/yyyy"); 
                        //colocando data selecionado no formato criado acima
                        String diaDoIntervalo2 = dataFormatada2.format(dataSelecionada2);
                        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");  
                        try{
                            java.sql.Date diaDoIntervaloCorrreto2 = new java.sql.Date(format2.parse(diaDoIntervalo2).getTime());
                            intervaloPorPeriodoNModel.setDiaFinal(diaDoIntervaloCorrreto2);
                        }catch(ParseException e){
                            JOptionPane.showMessageDialog(null, "Erro com a data final. Procure o Administrador.");
                        }
                        
                        
                        
                        
                        boolean cadastro = A_INTERVALOSPORPERIODON.setAtualizar(con, intervaloPorPeriodoNModel);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            //deletando as agendas
                            con = Conexao.fazConexao();
                            A_INTERVALOSPORPERIODO.setDeletar(con, intervaloPorPeriodoId);
                            
                            //cadastrando novas agendas
                            A_intervalosPorPeriodo intervaloPorPeriodoModel = new A_intervalosPorPeriodo();
                            intervaloPorPeriodoModel.setA_intervaloPorPeriodoNId(intervaloPorPeriodoId);
                            
                            
                            int i = 0;
                            int numeroDeLinhasNaTabela = jTable1.getRowCount();

                            while(i<numeroDeLinhasNaTabela){
                                intervaloPorPeriodoModel.setAgendaId(Integer.valueOf((String)jTable1.getValueAt(i, 0)));
                                A_INTERVALOSPORPERIODO.setCadastrar(con, intervaloPorPeriodoModel);
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
        jTFNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFHorarioInicial = new JFormattedTextField(new br.bcn.admclin.ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##:##"));
        jTFHorarioFinal = new JFormattedTextField(new br.bcn.admclin.ClasseAuxiliares.MetodosUteis().mascaraParaJFormattedTextField("##:##"));
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jCBAgendas = new javax.swing.JComboBox();
        jBIncluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTADescricao = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jLabel6 = new javax.swing.JLabel();

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
                    .addComponent(jCBAgendas, 0, 339, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                    .addComponent(jBIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
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

        jLabel4.setText("Dia Inicial");

        jTADescricao.setColumns(20);
        jTADescricao.setRows(5);
        jTADescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTADescricaoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTADescricao);

        jLabel5.setText("Descrição");

        jLabel6.setText("Dia Final");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(43, 43, 43)
                        .addComponent(jTFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(24, 24, 24)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(5, 5, 5)
                                .addComponent(jTFHorarioInicial))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTFHorarioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)))
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(19, 19, 19)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jTFHorarioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTFHorarioInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void jTADescricaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTADescricaoKeyReleased
jTADescricao.setText(jTADescricao.getText().toUpperCase());        // TODO add your handling code here:
    }//GEN-LAST:event_jTADescricaoKeyReleased

    private void jBEditarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBEditarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255,255,255));
        jTFHorarioFinal.setBackground(new java.awt.Color(255,255,255));
        jTFHorarioInicial.setBackground(new java.awt.Color(255,255,255));     // TODO add your handling code here:
    }//GEN-LAST:event_jBEditarFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBDeletar;
    public static javax.swing.JButton jBEditar;
    private javax.swing.JButton jBIncluir;
    public static javax.swing.JButton jBSalvar;
    public static javax.swing.JComboBox jCBAgendas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTADescricao;
    public static javax.swing.JTextField jTFHorarioFinal;
    public static javax.swing.JTextField jTFHorarioInicial;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    public static javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    // End of variables declaration//GEN-END:variables
}
