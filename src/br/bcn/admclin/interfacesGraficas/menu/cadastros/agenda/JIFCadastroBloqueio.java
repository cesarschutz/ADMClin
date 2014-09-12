package br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.bcn.admclin.ClasseAuxiliares.ColunaAceitandoIcone;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.NAGENDASDESC;
import br.bcn.admclin.dao.dbris.NAgendaDayBlock;
import br.bcn.admclin.dao.model.Nagenda;
import br.bcn.admclin.dao.model.Nagendadayblock;
import br.bcn.admclin.dao.model.Nagendamentos;
import br.bcn.admclin.dao.model.NagendamentosExames;
import br.bcn.admclin.dao.model.Nagendasdesc;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos.TratamentoParaRegistrarAtendimentoApartirDeAgendamento;

import com.lowagie.text.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JToggleButton;


/**
 *
 * @author cesar
 */
public class JIFCadastroBloqueio extends javax.swing.JInternalFrame {
	
	ArrayList<Integer> listaHanldeAgendas = new ArrayList();
	int weekDay;
	int handle_agenda;
	
    /**
     * Creates new form JIFCadastroBloqueio
     */
    public JIFCadastroBloqueio() {
        initComponents();
        tirandoBarraDeTitulo();
        preenchherAgendas();
        iniciarClasse();
    }
    
    private void iniciarClasse(){
    	jTable1.setRowHeight(25); 
    	ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
    	
    	DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(centralizado);
        
        TableCellRenderer tcrColuna5 = new ColunaAceitandoIcone();
        TableColumn column5 = jTable1.getColumnModel().getColumn(1);
        column5.setCellRenderer(tcrColuna5);
    	
    }
    
    private void ativandoSelecaoDeLinhaComBotaoDireitoDoMouse() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    int row = jTable1.rowAtPoint(e.getPoint());
                    if(jTable1.getSelectedRowCount() > 0){
                    	int[] listaLinhasSelecionadas = jTable1.getSelectedRows();  
                    	for (int linha : listaLinhasSelecionadas) {
							if(linha == row){
								abrirPopUp(e);
							}
						}
                    	
                    }
                }            
            }
        });
    }
    
    private void abrirPopUp(java.awt.event.MouseEvent evt){
        JPopupMenu popup = new JPopupMenu();
        
        // menu bloquear
        JMenuItem bloquear = new JMenuItem("Bloquear");
        bloquear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int[] listaLinhasSelecionadas = jTable1.getSelectedRows();  
            	for (int linha : listaLinhasSelecionadas) {
					if(jTable1.getValueAt(linha, 1) == null){
						int horario = converterHoraEmMinutos(jTable1.getValueAt(linha, 0).toString());
						NAgendaDayBlock.setCadastrar(handle_agenda, weekDay, horario);
					}
				}
            	preencheTabela();
            }
        });
        popup.add(bloquear);

        
        // menu desbloquear
        JMenuItem desbloquear = new JMenuItem("Desbloquear");
        desbloquear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int[] listaLinhasSelecionadas = jTable1.getSelectedRows();  
            	for (int linha : listaLinhasSelecionadas) {
					if(jTable1.getValueAt(linha, 1) != null){
						int horario = converterHoraEmMinutos(jTable1.getValueAt(linha, 0).toString());
						NAgendaDayBlock.setDeletar(handle_agenda, weekDay, horario);
					}
				}
            	preencheTabela();
            }
        });
        popup.add(desbloquear);
        
        
        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }
    
    private void preenchherAgendas(){
    	ArrayList<Nagendasdesc> listaAgendas = NAGENDASDESC.getConsultar();
        listaAgendas = NAGENDASDESC.getConsultar();
        for (int i = 0; i < listaAgendas.size() ; i++) {
            jCBAgendas.addItem(listaAgendas.get(i).getName());
            listaHanldeAgendas.add(listaAgendas.get(i).getNagdid());
        }
    }
    
    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }
    
    private void preencheTabela(){
    	((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        
    	//jogamos as informações na variavel local para usarmos para salvar ou deletar bloqueios
    	handle_agenda = listaHanldeAgendas.get(jCBAgendas.getSelectedIndex());
    	ArrayList<Nagenda> listaTurno = NAGENDASDESC.getTurnosDaAgenda(handle_agenda);
    	for (Nagenda nagenda : listaTurno) {
    		if(nagenda.getWeekday() == weekDay){
    			int periodoDaAgenda = nagenda.getDuracao();
    			int start1 = nagenda.getStart1();
    			int end1 = nagenda.getEnd1();
    			int start2 = nagenda.getStart2();
    			int end2 = nagenda.getEnd2();
    			int start3 = nagenda.getStart3();
    			int end3 = nagenda.getEnd3();
    			int start4 = nagenda.getStart4();
    			int end4 = nagenda.getEnd4();
    			
    			if(start1 > 0) jogaTurnoNaTabela(start1, end1, periodoDaAgenda);
    			if(start2 > 0) jogaTurnoNaTabela(start2, end2, periodoDaAgenda);
    			if(start3 > 0) jogaTurnoNaTabela(start3, end3, periodoDaAgenda);
    			if(start4 > 0) jogaTurnoNaTabela(start4, end4, periodoDaAgenda);
    		}
    	}
    	
    	preencheBloqueiosNaTabela();
    }
    
    private void jogaTurnoNaTabela(int start, int end, int duracaoAgenda){
    	DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
    	for (int j = start; j < end; j=j+duracaoAgenda) {
    		modelo.addRow(new String[] { converterMinutosParaHora(j) });
		}
    }
    
    public String converterMinutosParaHora(int minutosPraConverter) {
        String horas = String.valueOf(minutosPraConverter / 60);
        String minutos = String.valueOf(minutosPraConverter % 60);

        if (horas.length() < 2) {
            horas = "0" + horas;
        }

        if (minutos.length() < 2) {
            minutos = "0" + minutos;
        }

        return horas + ":" + minutos;
    }

    public int converterHoraEmMinutos(String hora) {
        String[] horaEMinutos = hora.split(":");

        int horas = Integer.valueOf(horaEMinutos[0]) * 60;
        int minutos = Integer.valueOf(horaEMinutos[1]);

        return horas + minutos;
    }

    ImageIcon iconeBloqueado = new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/fecharPrograma.png"));
    private void preencheBloqueiosNaTabela(){
    	ArrayList<Nagendadayblock> listaBloqueios = NAgendaDayBlock.getConsultar(weekDay, handle_agenda);
    	for (int i = 0; i < listaBloqueios.size(); i++) {
			for (int j = 0; j < jTable1.getRowCount(); j++) {
				String horarioTabela   = jTable1.getValueAt(j, 0).toString();
				String horarioBloqueio = converterMinutosParaHora(listaBloqueios.get(i).getHORARIO());
				if(horarioBloqueio.equals(horarioTabela)){
					jTable1.setValueAt(iconeBloqueado, j, 1);
					break;
				}
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCBAgendas = new javax.swing.JComboBox();
        jCBAgendas.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		((DefaultTableModel) jTable1.getModel()).setNumRows(0);
                jTable1.updateUI();
                jBDom.setSelected(false);
                jBSeg.setSelected(false);
                jBTer.setSelected(false);
                jBQua.setSelected(false);
                jBQui.setSelected(false);
                jBSex.setSelected(false);
                jBSab.setSelected(false);
        	}
        });
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastro de Bloqueios", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Agenda:");

        jTable1.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Horario", "Bloqueio"
        	}
        ){
        	public boolean isCellEditable(int row, int column) {   
                return false;  
            } 
        });
        jScrollPane1.setViewportView(jTable1);
        
        jBDom = new JToggleButton("Dom");
        jBDom.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		weekDay = 1;
        		preencheTabela();
        		jBDom.setSelected(true);
                jBSeg.setSelected(false);
                jBTer.setSelected(false);
                jBQua.setSelected(false);
                jBQui.setSelected(false);
                jBSex.setSelected(false);
                jBSab.setSelected(false);
        	}
        });
        
        jBSeg = new JToggleButton("Seg");
        jBSeg.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		weekDay = 2;
        		preencheTabela();
        		jBDom.setSelected(false);
                jBSeg.setSelected(true);
                jBTer.setSelected(false);
                jBQua.setSelected(false);
                jBQui.setSelected(false);
                jBSex.setSelected(false);
                jBSab.setSelected(false);
        	}
        });
        
        jBTer = new JToggleButton("Ter");
        jBTer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		weekDay = 3;
        		preencheTabela();
        		jBDom.setSelected(false);
                jBSeg.setSelected(false);
                jBTer.setSelected(true);
                jBQua.setSelected(false);
                jBQui.setSelected(false);
                jBSex.setSelected(false);
                jBSab.setSelected(false);
        	}
        });
        
        jBQua = new JToggleButton("Qua");
        jBQua.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		weekDay = 4;
        		preencheTabela();
        		jBDom.setSelected(false);
                jBSeg.setSelected(false);
                jBTer.setSelected(false);
                jBQua.setSelected(true);
                jBQui.setSelected(false);
                jBSex.setSelected(false);
                jBSab.setSelected(false);
        	}
        });
        
        jBQui = new JToggleButton("Qui");
        jBQui.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		weekDay = 5;
        		preencheTabela();
        		jBDom.setSelected(false);
                jBSeg.setSelected(false);
                jBTer.setSelected(false);
                jBQua.setSelected(false);
                jBQui.setSelected(true);
                jBSex.setSelected(false);
                jBSab.setSelected(false);
        	}
        });
        
        jBSex = new JToggleButton("Sex");
        jBSex.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		weekDay = 6;
        		preencheTabela();
        		jBDom.setSelected(false);
                jBSeg.setSelected(false);
                jBTer.setSelected(false);
                jBQua.setSelected(false);
                jBQui.setSelected(false);
                jBSex.setSelected(true);
                jBSab.setSelected(false);
        	}
        });
        
        jBSab = new JToggleButton("Sab");
        jBSab.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		weekDay = 7;
        		preencheTabela();
        		jBDom.setSelected(false);
                jBSeg.setSelected(false);
                jBTer.setSelected(false);
                jBQua.setSelected(false);
                jBQui.setSelected(false);
                jBSex.setSelected(false);
                jBSab.setSelected(true);
        	}
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(jLabel1)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jCBAgendas, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(jBDom)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jBSeg, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(jScrollPane1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
        						.addGroup(Alignment.LEADING, jPanel1Layout.createSequentialGroup()
        							.addComponent(jBTer, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jBQua)
        							.addGap(4)
        							.addComponent(jBQui, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jBSex, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jBSab, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)))
        			.addGap(39))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel1)
        				.addComponent(jCBAgendas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jBDom)
        				.addComponent(jBSeg)
        				.addComponent(jBTer)
        				.addComponent(jBQua)
        				.addComponent(jBQui)
        				.addComponent(jBSex)
        				.addComponent(jBSab))
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 518, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 422, Short.MAX_VALUE)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private javax.swing.JComboBox jCBAgendas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private JToggleButton jBDom;
    private JToggleButton jBSeg;
    private JToggleButton jBTer;
    private JToggleButton jBQua;
    private JToggleButton jBQui;
    private JToggleButton jBSex;
    private JToggleButton jBSab;
}
