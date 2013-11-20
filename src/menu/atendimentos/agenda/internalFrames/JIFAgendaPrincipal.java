package menu.atendimentos.agenda.internalFrames;


import janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import ClasseAuxiliares.ImagemNoJDesktopPane;
import br.bcn.admclin.dao.AGENDAS;
import br.bcn.admclin.dao.Conexao;




/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CeSaR
 */
public class JIFAgendaPrincipal extends javax.swing.JInternalFrame {
    
    String dataDeVisualizacaoDaAgenda;
    public static  List<Integer> listaHandleAgendas = new ArrayList<Integer>();
    private Connection con = null;
    
    //public static JIFUmaAgenda internalFrameUmaTabela = null;
    private JIFDuasAgendas internalFrameDuasTabela = null;
    private JIFTresAgendas internalFrameTresTabela = null;
    private JIFQuatroAgendas internalFrameQuatroTabela = null;
    
    
    /**
     * Creates new form Agenda
     */
    public JIFAgendaPrincipal() {
        initComponents();
        preenchendoAgendasNosComboBox();
        jXDatePicker1.setFormats(new String [] { "E dd/MM/yyyy" }); 
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");
        reescreverMetodoActionPerformanceDoDatePicker();
        tirandoBarraDeTitulo();
        
        
        //temporario, isso é para que ele nao posa selecionar mais de uma agenda
        jComboBox2.setVisible(false);
        jComboBox3.setVisible(false);
        jComboBox4.setVisible(false);
    }
    
    public void tirandoBarraDeTitulo(){
        ((BasicInternalFrameUI)this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );
        this.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
    }
    
    //metodo que pega a data que esta no datePicker
    public void pegandoDataSelecionada(){
        //pegando data que foi clicada
                  Date dataSelecionada = jXDatePicker1.getDate();
                  //criando um formato de data
                  SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy"); 
                  //colocando data selecionado no formato criado acima
                  dataDeVisualizacaoDaAgenda = dataFormatada.format(dataSelecionada);            
    }
    
    //metodo que soh serve para reescrever o evento ActionPerformance do DatePicker
    public void reescreverMetodoActionPerformanceDoDatePicker(){
        jXDatePicker1.addActionListener(new ActionListener() {  
              @Override
              public void actionPerformed(ActionEvent evt) { 
                   selecionarUmComboBoxOuData();                  
                  }  
        });
    }
    //isso para enquanto tive preenchendo nao pegar o evento do combo box
    // soh sera disparado quando as agendas estiverem preenchidas!!
    boolean preencheuAgendas = false;
    public void preenchendoAgendasNosComboBox(){
        preencheuAgendas = false;
        //preenchendo as Classes de Exames
        con = Conexao.fazConexao();
        ResultSet resultSet = AGENDAS.getConsultar(con);
        listaHandleAgendas.removeAll(listaHandleAgendas);
        jComboBox1.addItem("");  
        jComboBox2.addItem("");  
        jComboBox3.addItem("");  
        jComboBox4.addItem("");  
        listaHandleAgendas.add(0);
        try{
            while(resultSet.next()){
                if(resultSet.getInt("ativa") == 1){
                    jComboBox1.addItem(resultSet.getString("nome"));
                    jComboBox2.addItem(resultSet.getString("nome"));
                    jComboBox3.addItem(resultSet.getString("nome"));
                    jComboBox4.addItem(resultSet.getString("nome"));
                    listaHandleAgendas.add(resultSet.getInt("handle_agenda"));
                }
            }          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher as Agendas. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            Conexao.fechaConexao(con);
        }
        
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        preencheuAgendas = true;
    }
    
    
    //Metodo quando se clicar em algum comboBox ou no datePicker
    public void selecionarUmComboBoxOuData(){
        pegandoDataSelecionada();
        //verificando quantas agendas tem selecionadas
        int numeroDeAgendasSelecionadas = 0;
        
        if (jComboBox1.getSelectedIndex() != 0){
            numeroDeAgendasSelecionadas++;
        }
        
        
        
        if (jComboBox2.getSelectedIndex() != 0){
            numeroDeAgendasSelecionadas++;
        }
        
        if (jComboBox3.getSelectedIndex() != 0){
            numeroDeAgendasSelecionadas++;
        }
        
        if (jComboBox4.getSelectedIndex() != 0){
            numeroDeAgendasSelecionadas++;
        }
        
        
        
        //chamando metodo correto dependendo quantas agendas tiverem sido selecionadas
        
        
        if(numeroDeAgendasSelecionadas == 1){
            jXDatePicker1.setEnabled(true);
            selecionarUmaAgenda();
        }else if(numeroDeAgendasSelecionadas == 2){
            jXDatePicker1.setEnabled(true);
            selecionarDuasAgendas();
        }else if (numeroDeAgendasSelecionadas == 3){
            jXDatePicker1.setEnabled(true);
            selecionarTresAgendas();
        }else if(numeroDeAgendasSelecionadas == 4){
            jXDatePicker1.setEnabled(true);
            selecionarQuatroAgendas();
            //dispose em todos os jinternalframes
        }else if(numeroDeAgendasSelecionadas == 0){
            jXDatePicker1.setEnabled(false);
            selecionarNenhumaAgenda();
            //dispose em todos os jinternalframes
        }
    }
    //metodo caso nenhuma agenda seja selecionada
    public void selecionarNenhumaAgenda(){      
        if(janelaPrincipal.internalFrameUmaTabela != null){
            janelaPrincipal.internalFrameUmaTabela.dispose();
            janelaPrincipal.internalFrameUmaTabela = null;
        }
    }
    //metodo caso seja selecionado uma unica agenda
    public void selecionarUmaAgenda  (){
        if(internalFrameDuasTabela != null){
            internalFrameDuasTabela.dispose();
            internalFrameDuasTabela = null;
        }
        
        int handle_agenda = 0;
        
        if(JIFAgendaPrincipal.jComboBox1.getSelectedIndex()!=0){
            try {
                JIFUmaAgenda.jTextField1.setText((String)jComboBox1.getSelectedItem());
                handle_agenda = listaHandleAgendas.get(jComboBox1.getSelectedIndex());
            } catch (NullPointerException e) {
            }
            
        }
        if(JIFAgendaPrincipal.jComboBox2.getSelectedIndex()!=0){
            JIFUmaAgenda.jTextField1.setText((String)jComboBox2.getSelectedItem());
            handle_agenda = listaHandleAgendas.get(jComboBox2.getSelectedIndex());
        }
        if(JIFAgendaPrincipal.jComboBox3.getSelectedIndex()!=0){
            JIFUmaAgenda.jTextField1.setText((String)jComboBox3.getSelectedItem());
            handle_agenda = listaHandleAgendas.get(jComboBox3.getSelectedIndex());
        }

        if(JIFAgendaPrincipal.jComboBox4.getSelectedIndex()!=0){
            String agenda = (String)jComboBox4.getSelectedItem();
            if(agenda == null){
                
            }else{
                JIFUmaAgenda.jTextField1.setText(agenda);
                handle_agenda = listaHandleAgendas.get(jComboBox4.getSelectedIndex());
            }
            
        }
        
        //abrindo o internalframe
        if (janelaPrincipal.internalFrameUmaTabela == null) {

                    janelaPrincipal.internalFrameUmaTabela = new JIFUmaAgenda(handle_agenda);
                    jDesktopPane1.add(janelaPrincipal.internalFrameUmaTabela);
                    janelaPrincipal.internalFrameUmaTabela.setVisible(true);
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = janelaPrincipal.internalFrameUmaTabela.getWidth();
                    int aIFrame = janelaPrincipal.internalFrameUmaTabela.getHeight();

                    janelaPrincipal.internalFrameUmaTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

                } else {
                    janelaPrincipal.internalFrameUmaTabela.dispose();
                    janelaPrincipal.internalFrameUmaTabela = new JIFUmaAgenda(handle_agenda);
                    jDesktopPane1.add(janelaPrincipal.internalFrameUmaTabela);
                    janelaPrincipal.internalFrameUmaTabela.setVisible(true);
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = janelaPrincipal.internalFrameUmaTabela.getWidth();
                    int aIFrame = janelaPrincipal.internalFrameUmaTabela.getHeight();

                    janelaPrincipal.internalFrameUmaTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
                }
        
        
    }
    //metodo caso seja selecionado duas agendas
    public void selecionarDuasAgendas  (){
        if(janelaPrincipal.internalFrameUmaTabela != null){
            janelaPrincipal.internalFrameUmaTabela.dispose();
            janelaPrincipal.internalFrameUmaTabela = null;
        }
        
        if(internalFrameTresTabela != null){
            internalFrameTresTabela.dispose();
            internalFrameTresTabela = null;
        }
        
//        /abrindo jinternalframe
        if (internalFrameDuasTabela == null) {

                    internalFrameDuasTabela = new JIFDuasAgendas();
                    jDesktopPane1.add(internalFrameDuasTabela);
                    internalFrameDuasTabela.setVisible(true);
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = internalFrameDuasTabela.getWidth();
                    int aIFrame = internalFrameDuasTabela.getHeight();

                    internalFrameDuasTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

                } else {
                    //centralizando jinternal frame no jdesktoppane
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = internalFrameDuasTabela.getWidth();
                    int aIFrame = internalFrameDuasTabela.getHeight();

                    internalFrameDuasTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
                    internalFrameDuasTabela.toFront();
                }
        
        //colocando titulo da agenda
        if(jComboBox1.getSelectedIndex() == 0){
            
            if(jComboBox2.getSelectedIndex() == 0){
                String titulo = (String) jComboBox3.getSelectedItem();
                JIFDuasAgendas.jTextField1.setText(titulo);
            
                String tituloo = (String) jComboBox4.getSelectedItem();
                JIFDuasAgendas.jTextField2.setText(tituloo);
            }
            
            if(jComboBox3.getSelectedIndex() == 0){
                String titulo = (String) jComboBox2.getSelectedItem();
                JIFDuasAgendas.jTextField1.setText(titulo);
            
                String tituloo = (String) jComboBox4.getSelectedItem();
                JIFDuasAgendas.jTextField2.setText(tituloo);
            }
            
            if(jComboBox4.getSelectedIndex() == 0){
                String titulo = (String) jComboBox2.getSelectedItem();
                JIFDuasAgendas.jTextField1.setText(titulo);
            
                String tituloo = (String) jComboBox3.getSelectedItem();
                JIFDuasAgendas.jTextField2.setText(tituloo);
            }
        }else{
            if(jComboBox2.getSelectedIndex() == 0 && jComboBox3.getSelectedIndex() == 0){
            String titulo = (String) jComboBox1.getSelectedItem();
            JIFDuasAgendas.jTextField1.setText(titulo);
            
            String tituloo = (String) jComboBox4.getSelectedItem();
            JIFDuasAgendas.jTextField2.setText(tituloo);
            }
            
            if(jComboBox2.getSelectedIndex() == 0 && jComboBox4.getSelectedIndex() == 0){
            String titulo = (String) jComboBox1.getSelectedItem();
            JIFDuasAgendas.jTextField1.setText(titulo);
            
            String tituloo = (String) jComboBox3.getSelectedItem();
            JIFDuasAgendas.jTextField2.setText(tituloo);
            }
            
            if(jComboBox4.getSelectedIndex() == 0 && jComboBox3.getSelectedIndex() == 0){
            String titulo = (String) jComboBox1.getSelectedItem();
            JIFDuasAgendas.jTextField1.setText(titulo);
            
            String tituloo = (String) jComboBox2.getSelectedItem();
            JIFDuasAgendas.jTextField2.setText(tituloo);
            }
        }
        
        
    }
    //metodo utilizado caso seja selecionado tres agendas
    public void selecionarTresAgendas(){
        if(internalFrameQuatroTabela != null){
            internalFrameQuatroTabela.dispose();
            internalFrameQuatroTabela = null;
        }
        
        if(internalFrameDuasTabela != null){
            internalFrameDuasTabela.dispose();
            internalFrameDuasTabela = null;
        }
        
//          /abrindo jinternalframe
        if (internalFrameTresTabela == null) {

                    internalFrameTresTabela = new JIFTresAgendas();
                    jDesktopPane1.add(internalFrameTresTabela);
                    internalFrameTresTabela.setVisible(true);
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = internalFrameTresTabela.getWidth();
                    int aIFrame = internalFrameTresTabela.getHeight();

                    internalFrameTresTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

                } else {
                    //centralizando jinternal frame no jdesktoppane
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = internalFrameTresTabela.getWidth();
                    int aIFrame = internalFrameTresTabela.getHeight();

                    internalFrameTresTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
                    internalFrameTresTabela.toFront();
                }
        
        //colocando titulo nas agendas
        if(jComboBox1.getSelectedIndex() == 0){
            String titulo = (String) jComboBox2.getSelectedItem();
            JIFTresAgendas.jTextField1.setText(titulo);
            String tituloo = (String) jComboBox3.getSelectedItem();
            JIFTresAgendas.jTextField2.setText(tituloo);
            String titulooo = (String) jComboBox4.getSelectedItem();
            JIFTresAgendas.jTextField3.setText(titulooo);
        }
        
        if(jComboBox2.getSelectedIndex() == 0){
            String titulo = (String) jComboBox1.getSelectedItem();
            JIFTresAgendas.jTextField1.setText(titulo);
            String tituloo = (String) jComboBox3.getSelectedItem();
            JIFTresAgendas.jTextField2.setText(tituloo);
            String titulooo = (String) jComboBox4.getSelectedItem();
            JIFTresAgendas.jTextField3.setText(titulooo);
        }
        
        if(jComboBox3.getSelectedIndex() == 0){
            String titulo = (String) jComboBox1.getSelectedItem();
            JIFTresAgendas.jTextField1.setText(titulo);
            String tituloo = (String) jComboBox2.getSelectedItem();
            JIFTresAgendas.jTextField2.setText(tituloo);
            String titulooo = (String) jComboBox4.getSelectedItem();
            JIFTresAgendas.jTextField3.setText(titulooo);
        }
        
        if(jComboBox4.getSelectedIndex() == 0){
            String titulo = (String) jComboBox1.getSelectedItem();
            JIFTresAgendas.jTextField1.setText(titulo);
            String tituloo = (String) jComboBox2.getSelectedItem();
            JIFTresAgendas.jTextField2.setText(tituloo);
            String titulooo = (String) jComboBox3.getSelectedItem();
            JIFTresAgendas.jTextField3.setText(titulooo);
        }
        
        
        
    }
    
    //metodo utilizado caso seja selecionado 4 agendas
    public void selecionarQuatroAgendas(){
        if(internalFrameTresTabela != null){
            internalFrameTresTabela.dispose();
            internalFrameTresTabela = null;
        }
        
           //abrindo jinternalframe
        if (internalFrameQuatroTabela == null) {

                    internalFrameQuatroTabela = new JIFQuatroAgendas();
                    jDesktopPane1.add(internalFrameQuatroTabela);
                    internalFrameQuatroTabela.setVisible(true);
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = internalFrameQuatroTabela.getWidth();
                    int aIFrame = internalFrameQuatroTabela.getHeight();

                    internalFrameQuatroTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

                } else {
                    //centralizando jinternal frame no jdesktoppane
                    int lDesk = jDesktopPane1.getWidth();
                    int aDesk = jDesktopPane1.getHeight();
                    int lIFrame = internalFrameQuatroTabela.getWidth();
                    int aIFrame = internalFrameQuatroTabela.getHeight();

                    internalFrameQuatroTabela.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
                    internalFrameQuatroTabela.toFront();
                }
        //colocando titulos das agendas
        String titulo1 = (String) jComboBox1.getSelectedItem();
        JIFQuatroAgendas.jTextField1.setText(titulo1);
        String titulo2 = (String) jComboBox2.getSelectedItem();
        JIFQuatroAgendas.jTextField2.setText(titulo2);
        String titulo3 = (String) jComboBox3.getSelectedItem();
        JIFQuatroAgendas.jTextField3.setText(titulo3);
        String titulo4 = (String) jComboBox4.getSelectedItem();
        JIFQuatroAgendas.jTextField4.setText(titulo4);
        
    }
    
    
    /*
     * Metodo para deixar a legenda invisivel ou visivel
     */
    public static void sumirLegenda(boolean visivel){
        jBLegAgendamento.setVisible(visivel);
        jLLegAgendamento.setVisible(visivel);
        
        jBLegAgendamentoExt.setVisible(visivel);
        jLLegAgendamentoExt.setVisible(visivel);
        
        jBLegAtendimento.setVisible(visivel);
        jLLegAtendimento.setVisible(visivel);
        
        jBLegAtendimentoExt.setVisible(visivel);
        jLLegAtendimentoExt.setVisible(visivel);
        
        if (visivel) {
            jDesktopPane1.setSize(978, 513);
        } else {
            jDesktopPane1.setSize(978, 548);
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

        jPanel1 = new javax.swing.JPanel();
        jDesktopPane1 = new ImagemNoJDesktopPane("/Imagens/fundoJDesktopAgenda.jpg");
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox1 = new javax.swing.JComboBox();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLLegAtendimento = new javax.swing.JLabel();
        jLLegAtendimentoExt = new javax.swing.JLabel();
        jLLegAgendamento = new javax.swing.JLabel();
        jLLegAgendamentoExt = new javax.swing.JLabel();
        jBLegAgendamentoExt = new javax.swing.JButton();
        jBLegAtendimento = new javax.swing.JButton();
        jBLegAtendimentoExt = new javax.swing.JButton();
        jBLegAgendamento = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agenda", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jComboBox4.setOpaque(false);
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jXDatePicker1.setEnabled(false);

        jComboBox3.setOpaque(false);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jComboBox2.setOpaque(false);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLLegAtendimento.setText("Atendimento");

        jLLegAtendimentoExt.setText("Atendimento Estendido");

        jLLegAgendamento.setText("Agendamento");

        jLLegAgendamentoExt.setText("Agendamento estendido");

        jBLegAgendamentoExt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu/atendimentos/agenda/imagens/menuAgendarExtendido.png"))); // NOI18N

        jBLegAtendimento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu/atendimentos/agenda/imagens/menuAtendimento.png"))); // NOI18N

        jBLegAtendimentoExt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu/atendimentos/agenda/imagens/menuAtendimentoExtendido.png"))); // NOI18N

        jBLegAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/menu/atendimentos/agenda/imagens/menuAgendar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, 0, 235, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 405, Short.MAX_VALUE)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(398, 398, 398))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(jBLegAgendamento, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLLegAgendamento)
                        .addGap(18, 18, 18)
                        .addComponent(jBLegAgendamentoExt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLLegAgendamentoExt)
                        .addGap(18, 18, 18)
                        .addComponent(jBLegAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLLegAtendimento)
                        .addGap(18, 18, 18)
                        .addComponent(jBLegAtendimentoExt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLLegAtendimentoExt)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBLegAgendamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBLegAgendamentoExt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBLegAtendimento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBLegAtendimentoExt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLLegAgendamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLLegAgendamentoExt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLLegAtendimento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLLegAtendimentoExt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1006, 654));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        if (preencheuAgendas) {
            janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
                    SwingWorker worker = new SwingWorker(){
                                @Override  
                                protected Object doInBackground() throws Exception {
                                    
                                    selecionarUmComboBoxOuData();
                                    
                                    return null;  
                                }  
                                @Override  
                                protected void done() {
                                    janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
                                }  
                           };

                     worker.execute();
        }
        
        
                // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        selecionarUmComboBoxOuData();  
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        selecionarUmComboBoxOuData();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        selecionarUmComboBoxOuData();   // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jBLegAgendamento;
    private static javax.swing.JButton jBLegAgendamentoExt;
    private static javax.swing.JButton jBLegAtendimento;
    private static javax.swing.JButton jBLegAtendimentoExt;
    public static javax.swing.JComboBox jComboBox1;
    public static javax.swing.JComboBox jComboBox2;
    public static javax.swing.JComboBox jComboBox3;
    public static javax.swing.JComboBox jComboBox4;
    public static javax.swing.JDesktopPane jDesktopPane1;
    private static javax.swing.JLabel jLLegAgendamento;
    private static javax.swing.JLabel jLLegAgendamentoExt;
    private static javax.swing.JLabel jLLegAtendimento;
    private static javax.swing.JLabel jLLegAtendimentoExt;
    public static javax.swing.JPanel jPanel1;
    public static org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables

    //criei para para o erro
    private void setLocationRelativeTo(Object object) {
        
    }
}
