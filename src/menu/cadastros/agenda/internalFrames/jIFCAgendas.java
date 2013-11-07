/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package menu.cadastros.agenda.internalFrames;

import ClasseAuxiliares.documentoSemAspasEPorcento;
import ClasseAuxiliares.MetodosUteis;
import ClasseAuxiliares.documentoSomenteNumerosELetras;
import br.bcn.admclin.dao.Conexao;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import menu.cadastros.agenda.dao.agendasDAO;
import menu.cadastros.agenda.model.agendasMODEL;
import menu.cadastros.pessoal.dao.usuariosDAO;

/**
 *
 * @author BCN
 */
public class jIFCAgendas extends javax.swing.JInternalFrame {

    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;
    public static int handle_agenda = 0;
    
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
     * Creates new form jIFCAgendas
     */
    public jIFCAgendas() {
        initComponents();
        tirandoBarraDeTitulo();
        PreenchendoTabelaDasAgendas();
        iniciarClasse();
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    public void PreenchendoTabelaDasAgendas(){
        ((DefaultTableModel) jTable2.getModel()).setNumRows(0);
        jTable2.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = agendasDAO.getConsultar(con);
        try{
            while(resultSet.next()){
                //colocando dados na tabela
                modelo.addRow(new String[] {Integer.toString(resultSet.getInt("handle_agenda")),resultSet.getString("nome")});
            } 
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }
    
    public void deixandoCamposEnable(){
        jCBAtiva.setEnabled(true);
        jTFNome.setEnabled(true);
        jTADescricao.setEnabled(true);
        jScrollPane3.setEnabled(true);
        
        jCBSeg.setEnabled(true);
        jCBTer.setEnabled(true);
        jCBQua.setEnabled(true);
        jCBQui.setEnabled(true);
        jCBSex.setEnabled(true);
        jCBSab.setEnabled(true);
        jCBDom.setEnabled(true);
        
        jCBcr.setEnabled(true);
        jCBct.setEnabled(true);
        jCBdr.setEnabled(true);
        jCBdx.setEnabled(true);
        jCBmg.setEnabled(true);
        jCBmr.setEnabled(true);
        jCBnm.setEnabled(true);
        jCBot.setEnabled(true);
        jCBrf.setEnabled(true);
        jCBdo.setEnabled(true);
        jCBus.setEnabled(true);
        jCBod.setEnabled(true);
        jCBtr.setEnabled(true);
        jCBTodas.setEnabled(true);   
        
        
    }
    
    public void deixandoCamposDisenable(){
        jCBAtiva.setEnabled(false);
        jCBAtiva.setSelectedIndex(0);
        jTFNome.setEnabled(false);
        jTADescricao.setEnabled(false);
        jScrollPane3.setEnabled(false);
        
        jCBSeg.setEnabled(false);
        jCBTer.setEnabled(false);
        jCBQua.setEnabled(false);
        jCBQui.setEnabled(false);
        jCBSex.setEnabled(false);
        jCBSab.setEnabled(false);
        jCBDom.setEnabled(false);
        
        jCBcr.setEnabled(false);
        jCBct.setEnabled(false);
        jCBdr.setEnabled(false);
        jCBdx.setEnabled(false);
        jCBmg.setEnabled(false);
        jCBmr.setEnabled(false);
        jCBnm.setEnabled(false);
        jCBot.setEnabled(false);
        jCBrf.setEnabled(false);
        jCBdo.setEnabled(false);
        jCBus.setEnabled(false);
        jCBod.setEnabled(false);
        jCBtr.setEnabled(false);
        jCBTodas.setEnabled(false);
        
        jTable1.setEnabled(false);
        
    }

    public void iniciarClasse(){
        jTable2.setRowHeight(20);
        jTable2.setAutoCreateRowSorter(true);
        
        //selecionar somente uma linha na tabela
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable2.getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable2.getColumnModel().getColumn( 0 ).setMinWidth( 0 );  
        jTable2.getTableHeader().getColumnModel().getColumn( 0 ).setMaxWidth( 0 );  
        jTable2.getTableHeader().getColumnModel().getColumn( 0 ).setMinWidth( 0 );
        
        jBCancelar.setVisible(false);
        jBSalvar.setVisible(false);
        jBEditar.setVisible(false);
        jBDeletar.setVisible(false);
        
        pegandoDataDoSistema();
        
        setandoPropriedadesDaTabelaDosTurnos();
    }
    
    public void setandoPropriedadesDaTabelaDosTurnos(){
        //tamanho da linha
        jTable1.setRowHeight(30);
        
        //mascara nas celulas
        MaskFormatter msk = null;  
        try {  
            msk = new MaskFormatter("##:##");  
        } catch (Exception ex){  
            JOptionPane.showMessageDialog(this, "Erro na criação de máscara no JTable");  
        }  
        JTextField jftf = new JFormattedTextField(msk);  
        jTable1.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jftf));  
        jTable1.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(jftf));  
        jTable1.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(jftf));  
        jTable1.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(jftf));  
        
        //centralizar cabeçalhos colunas
        ((DefaultTableCellRenderer)jTable1.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER); 
        
        //centralizar texto das linhas
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER); 
        jTable1.getColumnModel().getColumn(0).setCellRenderer(centralizado);  
        jTable1.getColumnModel().getColumn(1).setCellRenderer(centralizado);  
        jTable1.getColumnModel().getColumn(2).setCellRenderer(centralizado);  
        jTable1.getColumnModel().getColumn(3).setCellRenderer(centralizado);  
        jTable1.getColumnModel().getColumn(4).setCellRenderer(centralizado); 
    }
    
    public boolean verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(int coluna){       
        //pegando os dados do turno
        String horarioInicial = (String) jTable1.getValueAt(0, coluna);
        String horarioFinal = (String) jTable1.getValueAt(1, coluna);
        String duracao = (String) jTable1.getValueAt(2, coluna);
        try{
            //se todos estiverem sem nenhum horario volta true
            if((horarioInicial == null || "  :  ".equals(horarioInicial) || "".equals(horarioInicial)) && (horarioFinal == null || "  :  ".equals(horarioFinal) || "".equals(horarioFinal)) && (duracao == null || "  :  ".equals(duracao) || "".equals(duracao))){
                return true;
            }else{
                //if se algum deles estiver sem horario (nao preenchido) volta false
                if((horarioInicial == null || "  :  ".equals(horarioInicial)) || (horarioFinal == null || "  :  ".equals(horarioFinal)) || (duracao == null || "  :  ".equals(duracao))){
                    jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                    jTFMensagemParaUsuario.setText("Turno " + coluna + " incorreto");
                    return false;
                }else{
                    //se alguma hora estiver incorreta volta false
                    if(MetodosUteis.verificarSeHoraEstaCorreta(horarioInicial) && MetodosUteis.verificarSeHoraEstaCorreta(horarioFinal) && MetodosUteis.verificarSeHoraEstaCorreta(duracao)){
                        //se horario inicial eh maior ou gual ao horario final
                        if(MetodosUteis.transformarHorarioEmMinutos(horarioInicial) >= MetodosUteis.transformarHorarioEmMinutos(horarioFinal)){
                            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                            jTFMensagemParaUsuario.setText("Turno " + coluna + " incorreto");
                            return false;
                            //se duracao menor que 1 ou (horariofinal - horarioinicial) menor que duração volta falso
                        } else if((MetodosUteis.transformarHorarioEmMinutos(duracao) < 1) || (((MetodosUteis.transformarHorarioEmMinutos(horarioFinal) - MetodosUteis.transformarHorarioEmMinutos(horarioInicial)) < MetodosUteis.transformarHorarioEmMinutos(duracao)))){
                            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                            jTFMensagemParaUsuario.setText("Turno " + coluna + " incorreto");
                            return false;
                            //se resto de horariofinal - horario inicial dividio por duracao sobrar algo q nao seja 0 volta false
                        } else if ( (MetodosUteis.transformarHorarioEmMinutos(horarioFinal) - MetodosUteis.transformarHorarioEmMinutos(horarioInicial)) % MetodosUteis.transformarHorarioEmMinutos(duracao) != 0){
                            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                            jTFMensagemParaUsuario.setText("Turno " + coluna + " incorreto");
                            return false;
                            //se coluna nao for a primeira e se horario inicial for menor que horario final do ultimo turno volta false
                        }else if(coluna != 1 && MetodosUteis.transformarHorarioEmMinutos(horarioInicial) < MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, coluna-1))){
                                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                                jTFMensagemParaUsuario.setText("Turno " + coluna+1 + " incorreto");
                                return false; 
                        }else{
                            return true;
                        }
                    }else{
                        jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                        jTFMensagemParaUsuario.setText("Turno " + coluna + " incorreto");
                        return false;
                    }
                }
            }
            //exceção caso o turno anterior nao tenha sido preenchido
        }catch(Exception e){
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
            jTFMensagemParaUsuario.setText("Preencha o turno " + coluna);
            return false;
        }
    }
    
    public boolean verificandoSeTudoFoiPreenchido(){
        boolean diasSelecionados = false;
        boolean modalidadeSelecionada = false;
        boolean nomeOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFNome, 2, jTFMensagemParaUsuario);
        
            //verificando se dia foi selecionado
            if(!jCBSeg.isSelected() && !jCBTer.isSelected() && !jCBQua.isSelected() && !jCBQui.isSelected() && !jCBSex.isSelected() && !jCBSab.isSelected() && !jCBDom.isSelected()){
                diasSelecionados = false;
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Selecione pelo menos um dia da semana");
            } else if(jCBSeg.isSelected() || jCBTer.isSelected() || jCBQua.isSelected() || jCBQui.isSelected() || jCBSex.isSelected() || jCBSab.isSelected() || jCBDom.isSelected()){
                diasSelecionados = true;
            }  
            
            //verificando se modalidade foi selecionada
            if(!jCBcr.isSelected() && !jCBct.isSelected() && !jCBdr.isSelected() && !jCBdx.isSelected() && !jCBmg.isSelected() && !jCBmr.isSelected() 
                    && !jCBnm.isSelected() && !jCBot.isSelected() && !jCBrf.isSelected() && !jCBdo.isSelected() && !jCBus.isSelected() && !jCBod.isSelected() && !jCBtr.isSelected()){
                modalidadeSelecionada = false;
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Selecione pelo menos uma Modalidade");
            } else if(jCBcr.isSelected() || jCBct.isSelected() || jCBdr.isSelected() || jCBdx.isSelected() || jCBmg.isSelected() || jCBmr.isSelected() 
                    || jCBnm.isSelected() || jCBot.isSelected() || jCBrf.isSelected() || jCBdo.isSelected() || jCBus.isSelected() || jCBod.isSelected() || jCBtr.isSelected()){
                modalidadeSelecionada = true;
            }  

        if(diasSelecionados && nomeOk && modalidadeSelecionada){
            return true;
        }else{
            return false;
        }
    }
    
    public void botaoNovo(){
        limpandoOsCampos();
        deixandoCamposEnable();
        jTable1.setEnabled(true);
        
        jTable2.setEnabled(false);
        
        jBNovoRegistro.setVisible(false);
        jBCancelar.setVisible(true);
        jBSalvar.setVisible(true);
        
        jTFNome.requestFocusInWindow();
    }
    
    public void botaoSalvar (){
            if(verificandoSeTudoFoiPreenchido() && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(1) && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(2) && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(3) && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(4)){
                con = Conexao.fazConexao();
                agendasMODEL agendaModelo = new agendasMODEL();
                agendaModelo.setNome(jTFNome.getText().toUpperCase());
                boolean existe = agendasDAO.getConsultarParaSalvarNovoRegistro(con, agendaModelo);
                Conexao.fechaConexao(con);
                if(agendasDAO.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Agenda já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        agendaModelo.setData(dataDeHojeEmVariavelDate);
                        agendaModelo.setUsuarioId(usuariosDAO.usrId);
                        
                        //setando se agenda é ativa
                        if(jCBAtiva.getSelectedIndex() == 0){
                            agendaModelo.setAtiva(1);
                        }else{
                            agendaModelo.setAtiva(0);
                        }
                        
                        
                        agendaModelo.setDescricao(jTADescricao.getText());
                        
                        
                        //setando horario inicial, horario final e duração do turno 1
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 1)) || ((String)jTable1.getValueAt(0, 1)) == null  || "".equals((String)jTable1.getValueAt(0, 1))){
                            agendaModelo.setHorarioInicialTurno1(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno1(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 1)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 1)) || ((String)jTable1.getValueAt(1, 1)) == null  || "".equals((String)jTable1.getValueAt(1, 1))){
                            agendaModelo.setHorarioFinalTurno1(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno1(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 1)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 1)) || ((String)jTable1.getValueAt(2, 1)) == null  || "".equals((String)jTable1.getValueAt(2, 1))){
                            agendaModelo.setDuracaoTurno1(0);
                        }else{
                            agendaModelo.setDuracaoTurno1(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 1)));
                        }
                        
                        
                        
                        //setando horario inicial, horario final e duração do turno 2
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 2)) || ((String)jTable1.getValueAt(0, 2)) == null  || "".equals((String)jTable1.getValueAt(0, 2))){
                            agendaModelo.setHorarioInicialTurno2(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno2(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 2)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 2)) || ((String)jTable1.getValueAt(1, 2)) == null  || "".equals((String)jTable1.getValueAt(1,2))){
                            agendaModelo.setHorarioFinalTurno2(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno2(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 2)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 2)) || ((String)jTable1.getValueAt(2, 2)) == null  || "".equals((String)jTable1.getValueAt(2,2))){
                            agendaModelo.setDuracaoTurno2(0);
                        }else{
                            agendaModelo.setDuracaoTurno2(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 2)));
                        }
                        
                        
                        
                        //setando horario inicial, horario final e duração do turno 3
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 3)) || ((String)jTable1.getValueAt(0, 3)) == null  || "".equals((String)jTable1.getValueAt(0, 3))){
                            agendaModelo.setHorarioInicialTurno3(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno3(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 3)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 3)) || ((String)jTable1.getValueAt(1, 3)) == null  || "".equals((String)jTable1.getValueAt(1,3))){
                            agendaModelo.setHorarioFinalTurno3(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno3(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 3)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 3)) || ((String)jTable1.getValueAt(2, 3)) == null  || "".equals((String)jTable1.getValueAt(2,3))){
                            agendaModelo.setDuracaoTurno3(0);
                        }else{
                            agendaModelo.setDuracaoTurno3(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 3)));
                        }
                        
                        
                        
                        //setando horario inicial, horario final e duração do turno 4
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 4)) || ((String)jTable1.getValueAt(0, 4)) == null  || "".equals((String)jTable1.getValueAt(0, 4))){
                            agendaModelo.setHorarioInicialTurno4(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno4(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 4)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 4)) || ((String)jTable1.getValueAt(1, 4)) == null  || "".equals((String)jTable1.getValueAt(1,4))){
                            agendaModelo.setHorarioFinalTurno4(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno4(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 4)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 4)) || ((String)jTable1.getValueAt(2, 4)) == null  || "".equals((String)jTable1.getValueAt(2,4))){
                            agendaModelo.setDuracaoTurno4(0);
                        }else{
                            agendaModelo.setDuracaoTurno4(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 4)));
                        }
                        
                        
                        
                        
                        //setando os dias de funcionamento
                        if(jCBSeg.isSelected()){
                            agendaModelo.setSeg(1);
                        }else{
                            agendaModelo.setSeg(0);
                        }
                        
                        if(jCBTer.isSelected()){
                            agendaModelo.setTer(1);
                        }else{
                            agendaModelo.setTer(0);
                        }
                        
                        if(jCBQua.isSelected()){
                            agendaModelo.setQua(1);
                        }else{
                            agendaModelo.setQua(0);
                        }
                        
                        if(jCBQui.isSelected()){
                            agendaModelo.setQui(1);
                        }else{
                            agendaModelo.setQui(0);
                        }
                        
                        if(jCBSex.isSelected()){
                            agendaModelo.setSex(1);
                        }else{
                            agendaModelo.setSex(0);
                        }
                        
                        if(jCBSab.isSelected()){
                            agendaModelo.setSab(1);
                        }else{
                            agendaModelo.setSab(0);
                        }
                        
                        if(jCBDom.isSelected()){
                            agendaModelo.setDom(1);
                        }else{
                            agendaModelo.setDom(0);
                        }
                        
                        
                        
                        
                        //setando as modalidades
                        if(jCBcr.isSelected()){
                            agendaModelo.setMODALIDADE_CR(1);
                        }else{
                            agendaModelo.setMODALIDADE_CR(0);
                        }
                        
                        if(jCBct.isSelected()){
                            agendaModelo.setMODALIDADE_CT(1);
                        }else{
                            agendaModelo.setMODALIDADE_CT(0);
                        }
                        
                        if(jCBdr.isSelected()){
                            agendaModelo.setMODALIDADE_DR(1);
                        }else{
                            agendaModelo.setMODALIDADE_DR(0);
                        }
                        
                        if(jCBdx.isSelected()){
                            agendaModelo.setMODALIDADE_DX(1);
                        }else{
                            agendaModelo.setMODALIDADE_DX(0);
                        }
                        
                        if(jCBmg.isSelected()){
                            agendaModelo.setMODALIDADE_MG(1);
                        }else{
                            agendaModelo.setMODALIDADE_MG(0);
                        }
                        
                        if(jCBmr.isSelected()){
                            agendaModelo.setMODALIDADE_MR(1);
                        }else{
                            agendaModelo.setMODALIDADE_MR(0);
                        }
                        
                        if(jCBnm.isSelected()){
                            agendaModelo.setMODALIDADE_NM(1);
                        }else{
                            agendaModelo.setMODALIDADE_NM(0);
                        }
                        
                        if(jCBot.isSelected()){
                            agendaModelo.setMODALIDADE_OT(1);
                        }else{
                            agendaModelo.setMODALIDADE_OT(0);
                        }
                        
                        if(jCBrf.isSelected()){
                            agendaModelo.setMODALIDADE_RF(1);
                        }else{
                            agendaModelo.setMODALIDADE_RF(0);
                        }
                        
                        if(jCBdo.isSelected()){
                            agendaModelo.setMODALIDADE_DO(1);
                        }else{
                            agendaModelo.setMODALIDADE_DO(0);
                        }
                        
                        if(jCBus.isSelected()){
                            agendaModelo.setMODALIDADE_US(1);
                        }else{
                            agendaModelo.setMODALIDADE_US(0);
                        }
                        
                        if(jCBod.isSelected()){
                            agendaModelo.setMODALIDADE_OD(1);
                        }else{
                            agendaModelo.setMODALIDADE_OD(0);
                        }
                        
                        if(jCBtr.isSelected()){
                            agendaModelo.setMODALIDADE_TR(1);
                        }else{
                            agendaModelo.setMODALIDADE_TR(0);
                        }
                        
                        
                        
                        boolean cadastro = agendasDAO.setCadastrar(con, agendaModelo);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            PreenchendoTabelaDasAgendas();
                            botaoCancelar();
                        }
                    } 
                }
        }
        
    }
    
    public void botaoAtualizar(){
        if(verificandoSeTudoFoiPreenchido() && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(1) && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(2) && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(3) && verificarSeHorariosNaTabelaEstaoPreenchidosCorretamente(4)){
                con = Conexao.fazConexao();
                agendasMODEL agendaModelo = new agendasMODEL();
                agendaModelo.setNome(jTFNome.getText().toUpperCase());
                agendaModelo.setHandle_agenda(handle_agenda);
                boolean existe = agendasDAO.getConsultarParaSalvarAtualizarRegistro(con, agendaModelo);
                Conexao.fechaConexao(con);
                if(agendasDAO.conseguiuConsulta){
                    if(existe){
                        JOptionPane.showMessageDialog(null, "Agenda já existe","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        //fazer a inserção no banco
                        con = Conexao.fazConexao();
                        agendaModelo.setData(dataDeHojeEmVariavelDate);
                        agendaModelo.setUsuarioId(usuariosDAO.usrId);
                        
                        
                        //setando se agenda é ativa
                        if(jCBAtiva.getSelectedIndex() == 0){
                            agendaModelo.setAtiva(1);
                        }else{
                            agendaModelo.setAtiva(0);
                        }
                        
                        
                        agendaModelo.setDescricao(jTADescricao.getText());
                        
                        
                        //setando horario inicial, horario final e duração do turno 1
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 1)) || ((String)jTable1.getValueAt(0, 1)) == null  || "".equals((String)jTable1.getValueAt(0, 1))){
                            agendaModelo.setHorarioInicialTurno1(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno1(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 1)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 1)) || ((String)jTable1.getValueAt(1, 1)) == null  || "".equals((String)jTable1.getValueAt(1, 1))){
                            agendaModelo.setHorarioFinalTurno1(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno1(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 1)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 1)) || ((String)jTable1.getValueAt(2, 1)) == null  || "".equals((String)jTable1.getValueAt(2, 1))){
                            agendaModelo.setDuracaoTurno1(0);
                        }else{
                            agendaModelo.setDuracaoTurno1(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 1)));
                        }
                        
                        
                        
                        //setando horario inicial, horario final e duração do turno 2
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 2)) || ((String)jTable1.getValueAt(0, 2)) == null  || "".equals((String)jTable1.getValueAt(0, 2))){
                            agendaModelo.setHorarioInicialTurno2(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno2(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 2)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 2)) || ((String)jTable1.getValueAt(1, 2)) == null  || "".equals((String)jTable1.getValueAt(1,2))){
                            agendaModelo.setHorarioFinalTurno2(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno2(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 2)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 2)) || ((String)jTable1.getValueAt(2, 2)) == null  || "".equals((String)jTable1.getValueAt(2,2))){
                            agendaModelo.setDuracaoTurno2(0);
                        }else{
                            agendaModelo.setDuracaoTurno2(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 2)));
                        }
                        
                        
                        
                        //setando horario inicial, horario final e duração do turno 3
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 3)) || ((String)jTable1.getValueAt(0, 3)) == null  || "".equals((String)jTable1.getValueAt(0, 3))){
                            agendaModelo.setHorarioInicialTurno3(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno3(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 3)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 3)) || ((String)jTable1.getValueAt(1, 3)) == null  || "".equals((String)jTable1.getValueAt(1,3))){
                            agendaModelo.setHorarioFinalTurno3(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno3(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 3)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 3)) || ((String)jTable1.getValueAt(2, 3)) == null  || "".equals((String)jTable1.getValueAt(2,3))){
                            agendaModelo.setDuracaoTurno3(0);
                        }else{
                            agendaModelo.setDuracaoTurno3(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 3)));
                        }
                        
                        
                        
                        //setando horario inicial, horario final e duração do turno 4
                        if( "  :  ".equals((String) jTable1.getValueAt(0, 4)) || ((String)jTable1.getValueAt(0, 4)) == null  || "".equals((String)jTable1.getValueAt(0, 4))){
                            agendaModelo.setHorarioInicialTurno4(0);
                        }else{
                            agendaModelo.setHorarioInicialTurno4(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(0, 4)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(1, 4)) || ((String)jTable1.getValueAt(1, 4)) == null  || "".equals((String)jTable1.getValueAt(1,4))){
                            agendaModelo.setHorarioFinalTurno4(0);
                        }else{
                            agendaModelo.setHorarioFinalTurno4(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(1, 4)));
                        }
                        
                        if( "  :  ".equals((String) jTable1.getValueAt(2, 4)) || ((String)jTable1.getValueAt(2, 4)) == null  || "".equals((String)jTable1.getValueAt(2,4))){
                            agendaModelo.setDuracaoTurno4(0);
                        }else{
                            agendaModelo.setDuracaoTurno4(MetodosUteis.transformarHorarioEmMinutos((String) jTable1.getValueAt(2, 4)));
                        }
                        
                        
                        //setando os dias de funcionamento
                        if(jCBSeg.isSelected()){
                            agendaModelo.setSeg(1);
                        }else{
                            agendaModelo.setSeg(0);
                        }
                        
                        if(jCBTer.isSelected()){
                            agendaModelo.setTer(1);
                        }else{
                            agendaModelo.setTer(0);
                        }
                        
                        if(jCBQua.isSelected()){
                            agendaModelo.setQua(1);
                        }else{
                            agendaModelo.setQua(0);
                        }
                        
                        if(jCBQui.isSelected()){
                            agendaModelo.setQui(1);
                        }else{
                            agendaModelo.setQui(0);
                        }
                        
                        if(jCBSex.isSelected()){
                            agendaModelo.setSex(1);
                        }else{
                            agendaModelo.setSex(0);
                        }
                        
                        if(jCBSab.isSelected()){
                            agendaModelo.setSab(1);
                        }else{
                            agendaModelo.setSab(0);
                        }
                        
                        if(jCBDom.isSelected()){
                            agendaModelo.setDom(1);
                        }else{
                            agendaModelo.setDom(0);
                        }
                        
                        
                        //setando as modalidades
                        if(jCBcr.isSelected()){
                            agendaModelo.setMODALIDADE_CR(1);
                        }else{
                            agendaModelo.setMODALIDADE_CR(0);
                        }
                        
                        if(jCBct.isSelected()){
                            agendaModelo.setMODALIDADE_CT(1);
                        }else{
                            agendaModelo.setMODALIDADE_CT(0);
                        }
                        
                        if(jCBdr.isSelected()){
                            agendaModelo.setMODALIDADE_DR(1);
                        }else{
                            agendaModelo.setMODALIDADE_DR(0);
                        }
                        
                        if(jCBdx.isSelected()){
                            agendaModelo.setMODALIDADE_DX(1);
                        }else{
                            agendaModelo.setMODALIDADE_DX(0);
                        }
                        
                        if(jCBmg.isSelected()){
                            agendaModelo.setMODALIDADE_MG(1);
                        }else{
                            agendaModelo.setMODALIDADE_MG(0);
                        }
                        
                        if(jCBmr.isSelected()){
                            agendaModelo.setMODALIDADE_MR(1);
                        }else{
                            agendaModelo.setMODALIDADE_MR(0);
                        }
                        
                        if(jCBnm.isSelected()){
                            agendaModelo.setMODALIDADE_NM(1);
                        }else{
                            agendaModelo.setMODALIDADE_NM(0);
                        }
                        
                        if(jCBot.isSelected()){
                            agendaModelo.setMODALIDADE_OT(1);
                        }else{
                            agendaModelo.setMODALIDADE_OT(0);
                        }
                        
                        if(jCBrf.isSelected()){
                            agendaModelo.setMODALIDADE_RF(1);
                        }else{
                            agendaModelo.setMODALIDADE_RF(0);
                        }
                        
                        if(jCBdo.isSelected()){
                            agendaModelo.setMODALIDADE_DO(1);
                        }else{
                            agendaModelo.setMODALIDADE_DO(0);
                        }
                        
                        if(jCBus.isSelected()){
                            agendaModelo.setMODALIDADE_US(1);
                        }else{
                            agendaModelo.setMODALIDADE_US(0);
                        }
                        
                        if(jCBod.isSelected()){
                            agendaModelo.setMODALIDADE_OD(1);
                        }else{
                            agendaModelo.setMODALIDADE_OD(0);
                        }
                        
                        if(jCBtr.isSelected()){
                            agendaModelo.setMODALIDADE_TR(1);
                        }else{
                            agendaModelo.setMODALIDADE_TR(0);
                        }
                        
                        
                        boolean cadastro = agendasDAO.setAtualizar(con, agendaModelo);
                        Conexao.fechaConexao(con);
                        if(cadastro){
                            PreenchendoTabelaDasAgendas();
                            botaoCancelar();
                        }
                    } 
                }
        }
    }
    
    public void botaoDeletar(){
        int resposta = JOptionPane.showConfirmDialog(null,"Deseja realmente deletar essa Agenda?", "ATENÇÃO",0);   
        if(resposta == JOptionPane.YES_OPTION){
            con = Conexao.fazConexao();
            //deletar agenda e intervalos dela
            Conexao.fechaConexao(con);
            PreenchendoTabelaDasAgendas();
            botaoCancelar();
            
        }
        
    }
    
    public void botaoCancelar(){
        jBNovoRegistro.setVisible(true);
        jBNovoRegistro.requestFocusInWindow();
        deixandoCamposDisenable();
        
        limpandoOsCampos();
        
        jBSalvar.setVisible(false);
        jBDeletar.setVisible(false);
        jBEditar.setVisible(false);
        jBCancelar.setVisible(false);
        
        jTable2.setEnabled(true);
        //limpar seleção da tabela
        jTable2.clearSelection();
    }
    
    public void limpandoOsCampos(){
        jTFNome.setText("");
        jTADescricao.setText("");
                
        jCBSeg.setSelected(false);
        jCBTer.setSelected(false);
        jCBQua.setSelected(false);
        jCBQui.setSelected(false);
        jCBSex.setSelected(false);
        jCBSab.setSelected(false);
        jCBDom.setSelected(false);
        
        jCBcr.setSelected(false);
        jCBct.setSelected(false);
        jCBdr.setSelected(false);
        jCBdx.setSelected(false);
        jCBmg.setSelected(false);
        jCBmr.setSelected(false);
        jCBnm.setSelected(false);
        jCBot.setSelected(false);
        jCBrf.setSelected(false);
        jCBdo.setSelected(false);
        jCBus.setSelected(false);
        jCBod.setSelected(false);
        jCBtr.setSelected(false);
        jCBTodas.setSelected(false);
        
        jTable1.setEnabled(false);
        
        jTable1.setValueAt("",0,1);
        jTable1.setValueAt("",1,1);
        jTable1.setValueAt("",2,1);
        
        jTable1.setValueAt("",0,2);
        jTable1.setValueAt("",1,2);
        jTable1.setValueAt("",2,2);
        
        jTable1.setValueAt("",0,3);
        jTable1.setValueAt("",1,3);
        jTable1.setValueAt("",2,3);
        
        jTable1.setValueAt("",0,4);
        jTable1.setValueAt("",1,4);
        jTable1.setValueAt("",2,4);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTADescricao = new javax.swing.JTextArea(new documentoSemAspasEPorcento(500));
        jLabel4 = new javax.swing.JLabel();
        jCBSab = new javax.swing.JCheckBox();
        jCBDom = new javax.swing.JCheckBox();
        jTFNome = new javax.swing.JTextField(new documentoSomenteNumerosELetras(64),null,0);
        jCBSex = new javax.swing.JCheckBox();
        jCBQui = new javax.swing.JCheckBox();
        jCBQua = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jCBTer = new javax.swing.JCheckBox();
        jCBSeg = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jCBcr = new javax.swing.JCheckBox();
        jCBct = new javax.swing.JCheckBox();
        jCBdr = new javax.swing.JCheckBox();
        jCBdx = new javax.swing.JCheckBox();
        jCBmr = new javax.swing.JCheckBox();
        jCBTodas = new javax.swing.JCheckBox();
        jCBus = new javax.swing.JCheckBox();
        jCBdo = new javax.swing.JCheckBox();
        jCBrf = new javax.swing.JCheckBox();
        jCBot = new javax.swing.JCheckBox();
        jCBnm = new javax.swing.JCheckBox();
        jCBAtiva = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCBtr = new javax.swing.JCheckBox();
        jCBod = new javax.swing.JCheckBox();
        jCBmg = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jBNovoRegistro = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBCancelar = new javax.swing.JButton();
        jBSalvar = new javax.swing.JButton();
        jBEditar = new javax.swing.JButton();
        jBDeletar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Agendas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informações da Agenda", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jScrollPane3.setEnabled(false);

        jTADescricao.setColumns(20);
        jTADescricao.setRows(5);
        jTADescricao.setEnabled(false);
        jScrollPane3.setViewportView(jTADescricao);

        jLabel4.setText("Descrição");

        jCBSab.setText("Sáb");
        jCBSab.setEnabled(false);

        jCBDom.setText("Dom");
        jCBDom.setEnabled(false);

        jTFNome.setEnabled(false);
        jTFNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNomeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNomeFocusLost(evt);
            }
        });

        jCBSex.setText("Sex");
        jCBSex.setEnabled(false);

        jCBQui.setText("Qui");
        jCBQui.setEnabled(false);

        jCBQua.setText("Qua");
        jCBQua.setEnabled(false);

        jLabel5.setText("Dias de Funcionamento");

        jCBTer.setText("Ter");
        jCBTer.setEnabled(false);

        jCBSeg.setText("Seg");
        jCBSeg.setEnabled(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Horário Inicial", null, null, null, null},
                {"Horário Final", null, null, null, null},
                {"Duração", null, "", null, null}
            },
            new String [] {
                "", "Turno 1", "Turno 2", "Turno 3", "Turno 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setEnabled(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setText("Modalidades");

        jCBcr.setText("CR");
        jCBcr.setEnabled(false);
        jCBcr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBcrActionPerformed(evt);
            }
        });

        jCBct.setText("CT");
        jCBct.setEnabled(false);
        jCBct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBctActionPerformed(evt);
            }
        });

        jCBdr.setText("DR");
        jCBdr.setEnabled(false);
        jCBdr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBdrActionPerformed(evt);
            }
        });

        jCBdx.setText("DX");
        jCBdx.setEnabled(false);
        jCBdx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBdxActionPerformed(evt);
            }
        });

        jCBmr.setText("MR");
        jCBmr.setEnabled(false);
        jCBmr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBmrActionPerformed(evt);
            }
        });

        jCBTodas.setText("Todas");
        jCBTodas.setEnabled(false);
        jCBTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTodasActionPerformed(evt);
            }
        });

        jCBus.setText("US");
        jCBus.setEnabled(false);
        jCBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBusActionPerformed(evt);
            }
        });

        jCBdo.setText("DO");
        jCBdo.setEnabled(false);
        jCBdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBdoActionPerformed(evt);
            }
        });

        jCBrf.setText("RF");
        jCBrf.setEnabled(false);
        jCBrf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBrfActionPerformed(evt);
            }
        });

        jCBot.setText("OT");
        jCBot.setEnabled(false);
        jCBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBotActionPerformed(evt);
            }
        });

        jCBnm.setText("NM");
        jCBnm.setEnabled(false);
        jCBnm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBnmActionPerformed(evt);
            }
        });

        jCBAtiva.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sim", "Não" }));
        jCBAtiva.setEnabled(false);

        jLabel2.setText("Ativa");

        jLabel7.setText("Nome");

        jCBtr.setText("TR");
        jCBtr.setEnabled(false);
        jCBtr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBtrActionPerformed(evt);
            }
        });

        jCBod.setText("OD");
        jCBod.setEnabled(false);
        jCBod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBodActionPerformed(evt);
            }
        });

        jCBmg.setText("MG");
        jCBmg.setEnabled(false);
        jCBmg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBmgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFNome, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCBAtiva, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 315, Short.MAX_VALUE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCBnm)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBod)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBot)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBrf)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBtr)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCBTodas))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCBcr)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBdr)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBdx)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBdo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCBmg)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCBmr)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBAtiva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jCBSeg)
                    .addComponent(jCBTer)
                    .addComponent(jCBQua)
                    .addComponent(jCBQui)
                    .addComponent(jCBSex)
                    .addComponent(jCBSab)
                    .addComponent(jCBDom))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jCBcr)
                            .addComponent(jCBct)
                            .addComponent(jCBdr)
                            .addComponent(jCBdx))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCBnm)
                            .addComponent(jCBod)
                            .addComponent(jCBot)
                            .addComponent(jCBrf)
                            .addComponent(jCBus)
                            .addComponent(jCBtr)
                            .addComponent(jCBTodas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCBdo)
                        .addComponent(jCBmg)
                        .addComponent(jCBmr)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todas as Agendas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "handle_agenda", "Nome"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
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
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/novo.png"))); // NOI18N
        jBNovoRegistro.setText("Novo");
        jBNovoRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
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

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18));
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/cancelar.png"))); // NOI18N
        jBCancelar.setText("Cancelar");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jBNovoRegistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBDeletar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBCancelar)
                .addContainerGap(363, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBNovoRegistro)
                    .addComponent(jBSalvar)
                    .addComponent(jBEditar)
                    .addComponent(jBDeletar)
                    .addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDeletarActionPerformed
        botaoDeletar();
    }//GEN-LAST:event_jBDeletarActionPerformed

    private void jBDeletarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBDeletarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoDeletar();
        }  
    }//GEN-LAST:event_jBDeletarKeyReleased

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEditarActionPerformed
        botaoAtualizar();
    }//GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBEditarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_jBEditarFocusLost

    private void jBEditarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBEditarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizar();
        }
    }//GEN-LAST:event_jBEditarKeyReleased

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }//GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalvarActionPerformed
        botaoSalvar();
    }//GEN-LAST:event_jBSalvarActionPerformed

    private void jBSalvarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jBSalvarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_jBSalvarFocusLost

    private void jBSalvarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBSalvarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvar();
        }
    }//GEN-LAST:event_jBSalvarKeyReleased
    
    public void preenchendoOsDadosDaAgendaQuandoForEditar (){
        handle_agenda = Integer.valueOf((String) jTable2.getValueAt(jTable2.getSelectedRow(), 0)) ;
        //colocando os valores
        con = Conexao.fazConexao();
        ResultSet resultSet = agendasDAO.getConsultarDadosDeUmaAgenda(con, handle_agenda);
        try{
            while(resultSet.next()){
                //colocando dados na nos campos
                
                if(resultSet.getInt("ativa")==0){
                   jCBAtiva.setSelectedIndex(1); 
                } else{
                    jCBAtiva.setSelectedIndex(0); 
                }
                
                jTFNome.setText(resultSet.getString("nome"));
                jTADescricao.setText(resultSet.getString("descricao"));
                
                
                //colocando horarios no turno 1
                String horarioInicialTurno1 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicialturno1"));
                jTable1.setValueAt(horarioInicialTurno1, 0, 1);
                
                String horarioFinalTurno1 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinalturno1"));
                jTable1.setValueAt(horarioFinalTurno1, 1, 1);
                
                String duracaoTurno1 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("duracaoturno1"));
                jTable1.setValueAt(duracaoTurno1, 2, 1);
                
                
                //colocando horarios no turno 2
                String horarioInicialTurno2 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicialturno2"));
                jTable1.setValueAt(horarioInicialTurno2, 0, 2);
                
                String horarioFinalTurno2 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinalturno2"));
                jTable1.setValueAt(horarioFinalTurno2, 1, 2);
                
                String duracaoTurno2 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("duracaoturno2"));
                jTable1.setValueAt(duracaoTurno2, 2, 2);
                
                
                //colocando horarios no turno 3
                String horarioInicialTurno3 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicialturno3"));
                jTable1.setValueAt(horarioInicialTurno3, 0, 3);
                
                String horarioFinalTurno3 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinalturno3"));
                jTable1.setValueAt(horarioFinalTurno3, 1, 3);
                
                String duracaoTurno3 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("duracaoturno3"));
                jTable1.setValueAt(duracaoTurno3, 2, 3);
                
                
                //colocando horarios no turno 4
                String horarioInicialTurno4 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horarioinicialturno4"));
                jTable1.setValueAt(horarioInicialTurno4, 0, 4);
                
                String horarioFinalTurno4 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("horariofinalturno4"));
                jTable1.setValueAt(horarioFinalTurno4, 1, 4);
                
                String duracaoTurno4 = MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("duracaoturno4"));
                jTable1.setValueAt(duracaoTurno4, 2, 4);
                
                
                //preenchendo dias de funcionamento
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
                
                
                
                
                //preenchendo as modalidades
                 if(resultSet.getInt("modalidade_cr")==0){
                   jCBcr.setSelected(false); 
                } else{
                    jCBcr.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_ct")==0){
                   jCBct.setSelected(false); 
                } else{
                    jCBct.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_dr")==0){
                   jCBdr.setSelected(false); 
                } else{
                    jCBdr.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_dx")==0){
                   jCBdx.setSelected(false); 
                } else{
                    jCBdx.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_mg")==0){
                   jCBmg.setSelected(false); 
                } else{
                    jCBmg.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_mr")==0){
                   jCBmr.setSelected(false); 
                } else{
                    jCBmr.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_nm")==0){
                   jCBnm.setSelected(false); 
                } else{
                    jCBnm.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_ot")==0){
                   jCBot.setSelected(false); 
                } else{
                    jCBot.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_rf")==0){
                   jCBrf.setSelected(false); 
                } else{
                    jCBrf.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_do")==0){
                   jCBdo.setSelected(false); 
                } else{
                    jCBdo.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_us")==0){
                   jCBus.setSelected(false); 
                } else{
                    jCBus.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_od")==0){
                   jCBod.setSelected(false); 
                } else{
                    jCBod.setSelected(true); 
                }
                
                if(resultSet.getInt("modalidade_tr")==0){
                   jCBtr.setSelected(false); 
                } else{
                    jCBtr.setSelected(true); 
                }
                
                
            } 
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher dados da Agenda. Procure o administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
        limpandoOsHorariosVaziosDaTabelaDeHorarios();
        marcandoTodasModalidadesSeNecessario();
        
        //marcando todas as modalidade se todas estiveram marcadas
        if(jCBcr.isSelected() && jCBct.isSelected() && jCBdr.isSelected() && jCBdx.isSelected() && jCBmg.isSelected() && jCBmr.isSelected() 
             && jCBnm.isSelected() && jCBot.isSelected() && jCBrf.isSelected() && jCBdo.isSelected() && jCBus.isSelected() && jCBod.isSelected() && jCBtr.isSelected()){
            
            jCBTodas.setSelected(true);
        } else{
            jCBTodas.setSelected(false);
        }
    }
    
    public void limpandoOsHorariosVaziosDaTabelaDeHorarios(){
        //for nas colunas
        for(int i = 1 ; i<5 ; i++){
            //for nas linhas
            for (int j = 0 ; j<3 ; j++){
                String valorDaCelula = (String) jTable1.getValueAt(j, i);
                if("00:00".equals(valorDaCelula)){
                    jTable1.setValueAt("", j, i);
                }
            }
        }
    }
    
    public void marcandoTodasModalidadesSeNecessario(){
        if(jCBcr.isSelected() && jCBct.isSelected() && jCBdr.isSelected() && jCBdx.isSelected() && jCBmg.isSelected() && jCBmr.isSelected() 
                    && jCBnm.isSelected() && jCBot.isSelected() && jCBrf.isSelected() && jCBdo.isSelected() && jCBus.isSelected() && jCBod.isSelected() && jCBtr.isSelected()){
                jCBTodas.setSelected(true);
        }  
    }
    
    
    
    private void jBNovoRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNovoRegistroActionPerformed
        botaoNovo();
    }//GEN-LAST:event_jBNovoRegistroActionPerformed

    private void jBNovoRegistroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBNovoRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoNovo();
        }
    }//GEN-LAST:event_jBNovoRegistroKeyReleased

    private void jCBTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBTodasActionPerformed
        //marca todas as modalidades
        if(jCBTodas.isSelected()){
            jCBcr.setSelected(true);
            jCBct.setSelected(true);
            jCBdr.setSelected(true);
            jCBdx.setSelected(true);
            jCBmg.setSelected(true);
            jCBmr.setSelected(true);
            jCBnm.setSelected(true);
            jCBot.setSelected(true);
            jCBrf.setSelected(true);
            jCBdo.setSelected(true);
            jCBus.setSelected(true);
            jCBod.setSelected(true);
            jCBtr.setSelected(true);
        }else{
            jCBcr.setSelected(false);
            jCBct.setSelected(false);
            jCBdr.setSelected(false);
            jCBdx.setSelected(false);
            jCBmg.setSelected(false);
            jCBmr.setSelected(false);
            jCBnm.setSelected(false);
            jCBot.setSelected(false);
            jCBrf.setSelected(false);
            jCBdo.setSelected(false);
            jCBus.setSelected(false);
            jCBod.setSelected(false);
            jCBtr.setSelected(false);
        }
        
    }//GEN-LAST:event_jCBTodasActionPerformed

    private void jCBcrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBcrActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBcrActionPerformed

    private void jCBctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBctActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBctActionPerformed

    private void jCBdrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBdrActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBdrActionPerformed

    private void jCBdxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBdxActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBdxActionPerformed

    private void jCBmgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBmgActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBmgActionPerformed

    private void jCBmrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBmrActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBmrActionPerformed

    private void jCBnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBnmActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBnmActionPerformed

    private void jCBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBotActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBotActionPerformed

    private void jCBrfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBrfActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBrfActionPerformed

    private void jCBdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBdoActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBdoActionPerformed

    private void jCBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBusActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBusActionPerformed

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

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (!jTable1.isEnabled()) {
           preenchendoOsDadosDaAgendaQuandoForEditar();
            

            handle_agenda = Integer.valueOf((String) jTable2.getValueAt(jTable2.getSelectedRow(), 0)) ;
            jBEditar.setVisible(true);
            jBDeletar.setVisible(true);
            jBCancelar.setVisible(true);
            jBNovoRegistro.setVisible(false);

            jTFNome.requestFocusInWindow();

            deixandoCamposEnable();   
        }
            
              // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void jCBtrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBtrActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBtrActionPerformed

    private void jCBodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBodActionPerformed
        if(jCBTodas.isSelected()){
            jCBTodas.setSelected(false);
        }
    }//GEN-LAST:event_jCBodActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBCancelar;
    public static javax.swing.JButton jBDeletar;
    public static javax.swing.JButton jBEditar;
    private javax.swing.JButton jBNovoRegistro;
    public static javax.swing.JButton jBSalvar;
    private javax.swing.JComboBox jCBAtiva;
    public static javax.swing.JCheckBox jCBDom;
    public static javax.swing.JCheckBox jCBQua;
    public static javax.swing.JCheckBox jCBQui;
    public static javax.swing.JCheckBox jCBSab;
    public static javax.swing.JCheckBox jCBSeg;
    public static javax.swing.JCheckBox jCBSex;
    public static javax.swing.JCheckBox jCBTer;
    public static javax.swing.JCheckBox jCBTodas;
    public static javax.swing.JCheckBox jCBcr;
    public static javax.swing.JCheckBox jCBct;
    public static javax.swing.JCheckBox jCBdo;
    public static javax.swing.JCheckBox jCBdr;
    public static javax.swing.JCheckBox jCBdx;
    public static javax.swing.JCheckBox jCBmg;
    public static javax.swing.JCheckBox jCBmr;
    public static javax.swing.JCheckBox jCBnm;
    public static javax.swing.JCheckBox jCBod;
    public static javax.swing.JCheckBox jCBot;
    public static javax.swing.JCheckBox jCBrf;
    public static javax.swing.JCheckBox jCBtr;
    public static javax.swing.JCheckBox jCBus;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JTextArea jTADescricao;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    public static javax.swing.JTextField jTFNome;
    public static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
