package br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe;

import java.awt.Dimension;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import br.bcn.admclin.ClasseAuxiliares.ColorirHorariosIndisponiveisNaAgendaELiberarIconesNaTabela;
import br.bcn.admclin.ClasseAuxiliares.ColunaAceitandoIcone;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.model.Atendimento_Exames;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio.atendimentoDAO;

import com.lowagie.text.List;

import javax.swing.JTable;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class jIFAlterarValoresNotaIpe extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private ArrayList<ExameNotaIpeMODEL> listaExames = new ArrayList<>();

    /**
     * Create the frame.
     */
    public jIFAlterarValoresNotaIpe() {
        initComponents();
        iniciarClasse();

    }

    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    private void iniciarClasse() {
        tirandoBarraDeTitulo();
        
        jTable1.setRowHeight(30);
        
        // alinhando conteudo da coluna de uma tabela
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(40);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(1).setMinWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(110);
        jTable1.getTableHeader().getColumnModel().getColumn(3).setMinWidth(110);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(30);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMinWidth(30);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(90);
        jTable1.getTableHeader().getColumnModel().getColumn(6).setMinWidth(90);
        
        jTable1.getColumnModel().getColumn(0).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(6).setCellRenderer(direita);

        
        
        //colocando focu na tabela
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTable1.requestFocus();
            }
        });
    }

    private void buscarExames(int numeroNota) {
        listaExames = ExameNotaIpeDAO.getExamesPorNota(numeroNota);
        preencheTabela();
    }

    private void preencheTabela() {
        if (listaExames != null) {
            
            // limpa a tabela
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

            //"Ref.", "Ficha", "Paciente", "Matricula", "Exame", "Dia", "Valor"
            // preenche a tabela com os atendimentos
            for (ExameNotaIpeMODEL exame : listaExames) {
                modelo.addRow(new Object[] { jTable1.getRowCount() + 1, exame.getFicha(), exame.getPaciente(), exame.getMatricula(), exame.getExame(), exame.getDia(), exame.getValor() });
            }

        }
    }
    
    private void clicarNaTabela() {
		if(jTable1.getSelectedRow() >= 0){
			int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
			String novo_valor = JOptionPane.showInputDialog("Digite o valor.");
			novo_valor = novo_valor.replaceAll(",", ".");
			Double valorDOuble;
			try {
				valorDOuble = Double.valueOf(novo_valor);
				boolean retorno = atualizaValorConvenio(handle_at, valorDOuble);
				if(retorno){
					jTable1.setValueAt(valorDOuble, jTable1.getSelectedRow(), 6);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Valor Inválido");
			}	
		}
	}
        
    private boolean atualizaValorConvenio(int handle_at, Double valorDOuble) {
    	return ExameNotaIpeDAO.atualizaValorConvenio(handle_at, valorDOuble);
	}

	private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTable1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    clicarNaTabela();
                }
                
            }


        });

        jPanel1.setBorder(new TitledBorder(null, "Alterar Valores de Exames em Nota IP\u00CA", TitledBorder.CENTER, TitledBorder.TOP, null, null));

        jTable1.setModel(new DefaultTableModel(
        	new Object[][] {
        		{null, null, null, null, null, null, null},
        	},
        	new String[] {
        		"Ref.", "Ficha", "Paciente", "Matricula", "Exame", "Dia", "Valor"
        	}
        ) {
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false, false, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        jTable1.getColumnModel().getColumn(0).setResizable(false);
        jTable1.getColumnModel().getColumn(1).setResizable(false);
        jTable1.getColumnModel().getColumn(2).setResizable(false);
        jTable1.getColumnModel().getColumn(3).setResizable(false);
        jTable1.getColumnModel().getColumn(4).setResizable(false);
        jTable1.getColumnModel().getColumn(5).setResizable(false);
        jTable1.getColumnModel().getColumn(6).setResizable(false);
        jScrollPane1.setViewportView(jTable1);
        
        lblNmeroNota = new JLabel("Número Nota:");
        
        jTFNumeroNota = new JTextField();
        jTFNumeroNota.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent arg0) {
        		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
        			int numeroNota;
        			try {
						numeroNota = Integer.valueOf(jTFNumeroNota.getText());
						buscarExames(numeroNota);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "Número de Nota Inválido. Tente Novamente");
						
						//limpa a tabela pois deu erro ao consultar exames
						((DefaultTableModel) jTable1.getModel()).setNumRows(0);
			            jTable1.updateUI();
						
					}
                    
                }
        	}
        });
        jTFNumeroNota.setColumns(10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addComponent(lblNmeroNota)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jTFNumeroNota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(361, Short.MAX_VALUE))
        		.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblNmeroNota)
        				.addComponent(jTFNumeroNota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 936, Short.MAX_VALUE)
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
        );
        getContentPane().setLayout(layout);

        pack();
    }
    
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTable1;
    private JLabel lblNmeroNota;
    private JTextField jTFNumeroNota;
}
