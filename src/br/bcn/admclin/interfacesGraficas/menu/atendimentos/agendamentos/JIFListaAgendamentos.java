package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.bcn.admclin.ClasseAuxiliares.ColunaAceitandoIcone;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.AREAS_ATENDIMENTO;
import br.bcn.admclin.dao.dbris.NAGENDAMENTOS;
import br.bcn.admclin.dao.model.Areas_atendimento;
import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;

import javax.swing.JComboBox;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;


/**
 *
 * @author cesar
 */
public class JIFListaAgendamentos extends javax.swing.JInternalFrame {
	
	public ArrayList<Areas_atendimento> listaAreasDeAtendimento = new ArrayList<Areas_atendimento>();

    /**
     * Creates new form JIFCadastroAgendaDesc
     */
    public JIFListaAgendamentos() {
        initComponents();
        tirandoBarraDeTitulo();
        preenchendoAreasDeAtendimento();
        iniciarClasse();
        
    }
    
    private void preenchendoAreasDeAtendimento(){
        listaAreasDeAtendimento = AREAS_ATENDIMENTO.getConsultarComOpcaoDeTodasAsAreas();
        for (int i = 0; i < listaAreasDeAtendimento.size() ; i++) {
            jCBAreaDeAtendimento.addItem(listaAreasDeAtendimento.get(i).getNome());
        }
    }
    
    private void reescreverMetodoActionPerformanceDoDatePicker() {
        jXDatePicker1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                preencheTabela();
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        
                    }
                });
            }
        });
    }
    
    private Date pegandoDataDoDataPicker() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date data = null;
        java.util.Date dataSelecionada = jXDatePicker1.getDate();
        // criando um formato de data
        SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        // colocando data selecionado no formato criado acima
        String data2 = dataFormatada.format(dataSelecionada);

        try {
            data = new java.sql.Date(format.parse(data2).getTime());
            return data;
        } catch (ParseException ex) {
            return null;
        }
    }
    
    private void ativandoSelecaoDeLinhaComBotaoDireitoDoMouse() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    int col = jTable1.columnAtPoint(e.getPoint());
                    int row = jTable1.rowAtPoint(e.getPoint());
                    if (col != -1 && row != -1) {
                        jTable1.setColumnSelectionInterval(col, col);
                        jTable1.setRowSelectionInterval(row, row);
                    }
                }

                // colocando a seleção na celula clicada
                int linhaSelecionada = jTable1.getSelectedRow();
                int colunaSelecionada = jTable1.getSelectedColumn();

                jTable1.editCellAt(linhaSelecionada, colunaSelecionada);                
            }
        });
    }
    
    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }
    
    private void iniciarClasse(){
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
        reescreverMetodoActionPerformanceDoDatePicker();
        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");
        
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        
        jTable1.setRowHeight(30);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        preencheTabela();
        
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        
        TableColumn column1 = jTable1.getColumnModel().getColumn(1);
        column1.setCellRenderer(centralizado);
        
        TableColumn column2 = jTable1.getColumnModel().getColumn(2);
        column2.setCellRenderer(centralizado);
        
        
        TableCellRenderer tcrColuna7 = new ColunaAceitandoIcone();
        TableColumn column7 = jTable1.getColumnModel().getColumn(7);
        column7.setCellRenderer(tcrColuna7);
    }   

    
    private void preencheTabela(){
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        
        ArrayList<Nagendamentos> listaAgendamentos = NAGENDAMENTOS.getConsultar(pegandoDataDoDataPicker());
        //ordenando a lista por horario do primeiro exame
        Collections.sort(listaAgendamentos);
        
        //se tiver em todas as areas preenche tudo
        if(jCBAreaDeAtendimento.getSelectedIndex() == 0){
        	for (Nagendamentos agendamento : listaAgendamentos) {
        		
        		int minutosPrimeroExame = agendamento.getListaExames().get(0).getHORA();
    			String horaPrimeiroExameString = MetodosUteis.transformarMinutosEmHorario(minutosPrimeroExame);
    			
    			int numeroDeExames = agendamento.getListaExames().size();
    			int minutosSegundoExame = agendamento.getListaExames().get(numeroDeExames - 1).getHORA();
    			String horaSegundoExameString = MetodosUteis.transformarMinutosEmHorario(minutosSegundoExame);
    			
    			
    			modelo.addRow(new Object[] { agendamento, horaPrimeiroExameString, horaSegundoExameString, agendamento.getPACIENTE(), agendamento.getTELEFONE(), agendamento.getCELULAR(), agendamento.getNOME_CONVENIO(), agendamento.getVirou_atendimento()  });
            } 
        	//se for uma area especifica ele vai apresentar na tela somente os agendamentos que tenham aquela area selecionada
        }else{
        	int handleAreaSelecionada = listaAreasDeAtendimento.get(jCBAreaDeAtendimento.getSelectedIndex()).getId_areas_atendimento();
        	//varre os agendamentos
        	for (Nagendamentos agendamento : listaAgendamentos) {
        		boolean agendamentoTemExameDaArea = false;
        		//varre os exames do agendamento para verficar se existe exames da area selecionada
        		for (NagendamentosExames exame : agendamento.getListaExames()) {
        			if(exame.getID_AREAS_ATENDIMENTO() == handleAreaSelecionada){
        				agendamentoTemExameDaArea = true;
        			}
        		}
        		//se existe exames no agendamento da area selecionada ele apresenta na tela
        		if(agendamentoTemExameDaArea){
        			
        			int minutosPrimeroExame = agendamento.getListaExames().get(0).getHORA();
        			String horaPrimeiroExameString = MetodosUteis.transformarMinutosEmHorario(minutosPrimeroExame);
        			
        			int numeroDeExames = agendamento.getListaExames().size();
        			int minutosSegundoExame = agendamento.getListaExames().get(numeroDeExames - 1).getHORA();
        			String horaSegundoExameString = MetodosUteis.transformarMinutosEmHorario(minutosSegundoExame);
        			
        			
        			modelo.addRow(new Object[] { agendamento, horaPrimeiroExameString, horaSegundoExameString, agendamento.getPACIENTE(), agendamento.getTELEFONE(), agendamento.getCELULAR(), agendamento.getNOME_CONVENIO(), agendamento.getVirou_atendimento()  });
        		}
               
            } 
        }
        
        colocarIconesNoVirouAtendimento();
    }
    
    ImageIcon iconeVirouAtendimento = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/LaudoEExameEntregue.png"));
    private void colocarIconesNoVirouAtendimento(){
    	for (int i = 0; i < jTable1.getRowCount(); i++) {
			if(jTable1.getValueAt(i, 7).toString().equals("1")){
				jTable1.setValueAt(iconeVirouAtendimento, i, 7);
			}else if(jTable1.getValueAt(i, 7).toString().equals("0")){
				jTable1.setValueAt("", i, 7);
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

        jPanel2 = new javax.swing.JPanel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agendamentos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"nagendamento", "Hora Inicial", "Hora Final", "Paciente", "Telefone", "Celular", "Conv\u00EAnio", "Atendido"
        	}
        ) {
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false, false, false, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(75);
        jTable1.getColumnModel().getColumn(1).setMinWidth(75);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(75);
        
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(70);
        jTable1.getColumnModel().getColumn(2).setMinWidth(70);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(70);
        
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(4).setMinWidth(120);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(120);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(5).setMinWidth(120);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(120);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(7).setMinWidth(60);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(60);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        
        jCBAreaDeAtendimento = new JComboBox();
        jCBAreaDeAtendimento.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		preencheTabela();
        	}
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setHorizontalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addComponent(jXDatePicker1, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jCBAreaDeAtendimento, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(516, Short.MAX_VALUE))
        		.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
        	jPanel2Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel2Layout.createSequentialGroup()
        			.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jXDatePicker1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jCBAreaDeAtendimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
        );
        jPanel2.setLayout(jPanel2Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        int colunaClicada = jTable1.columnAtPoint(evt.getPoint());
        int linhaClicada = jTable1.rowAtPoint(evt.getPoint());
        int linhaSelecionada = jTable1.getSelectedRow();
        
        if (MouseEvent.BUTTON3 == evt.getButton() && linhaClicada == linhaSelecionada){
            abrirPopUp(evt);
        }
        
    }
    
    private void abrirPopUp(java.awt.event.MouseEvent evt){
        ImageIcon iconeMenuAtendimento = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuAtendimento.png"));
        Nagendamentos agendamentoClicado = (Nagendamentos) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
        JPopupMenu popup = new JPopupMenu();
        
        // menu registrar atendimento
        JMenuItem registrarAtendimento = new JMenuItem("Registrar Atendimento", iconeMenuAtendimento);
        registrarAtendimento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                TratamentoParaRegistrarAtendimentoApartirDeAgendamento tratamentoDeAgendamento = new TratamentoParaRegistrarAtendimentoApartirDeAgendamento((Nagendamentos)jTable1.getValueAt(jTable1.getSelectedRow(), 0));
            }
        });
        popup.add(registrarAtendimento);
        if(agendamentoClicado.getVirou_atendimento() == 1){
        	registrarAtendimento.setEnabled(false);
        }
        
        
        
        //preenchendo os exames
        ArrayList<NagendamentosExames> listaDeExamesDoAgendamento = agendamentoClicado.getListaExames();
        //precorre os exames do agendamento
        int id_area = 0;
        for (int i = 0; i < listaDeExamesDoAgendamento.size(); i++) {
            if(listaDeExamesDoAgendamento.get(i).getID_AREAS_ATENDIMENTO() == id_area){
                //coloca o nome do exame
                JMenuItem exame = new JMenuItem("    -  " + listaDeExamesDoAgendamento.get(i).getNomeExame() + "    - " + listaDeExamesDoAgendamento.get(i).getNomeAgenda() + " - " + MetodosUteis.transformarMinutosEmHorario(listaDeExamesDoAgendamento.get(i).getHORA()));
                exame.setForeground(java.awt.Color.blue);  
                popup.add(exame);
            }else{
                //coloca separador
                popup.addSeparator();
                //coloca o nome da area de atendimento
                JMenuItem areaDeAtendimento = new JMenuItem("ÁREA: " + listaDeExamesDoAgendamento.get(i).getNomeAreaAtendimento());
                areaDeAtendimento.setForeground(java.awt.Color.red); 
                popup.add(areaDeAtendimento);
                //coloca o nome do exame
                JMenuItem exame = new JMenuItem("    -  " + listaDeExamesDoAgendamento.get(i).getNomeExame() + "    - " + listaDeExamesDoAgendamento.get(i).getNomeAgenda() + " - " + MetodosUteis.transformarMinutosEmHorario(listaDeExamesDoAgendamento.get(i).getHORA()));
                exame.setForeground(java.awt.Color.blue); 
                popup.add(exame);
                
                id_area = listaDeExamesDoAgendamento.get(i).getID_AREAS_ATENDIMENTO();
            }
        }
        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }
    
    //Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private JComboBox jCBAreaDeAtendimento;
}
