package br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresrecebidosconvenio;



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
import br.bcn.admclin.calculoValorDeUmExame.CalculoValorDeExame;
import br.bcn.admclin.dao.model.Atendimento_Exames;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe.ExameNotaIpeDAO;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe.ExameNotaIpeMODEL;
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

public class jIFListaAlterarValoresDeConvenio extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private ArrayList<ExameNotaIpeMODEL> listaExames = new ArrayList<>();

    /**
     * Create the frame.
     * @param integer 
     * @param string2 
     * @param diaFinalSql 
     * @param diaInicialSql 
     * @param string 
     */
    public jIFListaAlterarValoresDeConvenio(String convenioOUgrupo, java.sql.Date diaInicialSql, java.sql.Date diaFinalSql, Integer handleGrupoOUconvenio) {
        initComponents();
        iniciarClasse();
        buscarExames(convenioOUgrupo, diaInicialSql, diaFinalSql, handleGrupoOUconvenio);
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
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(70);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(110);
        jTable1.getTableHeader().getColumnModel().getColumn(2).setMinWidth(110);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(100);
        jTable1.getTableHeader().getColumnModel().getColumn(4).setMinWidth(100);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(90);
        jTable1.getTableHeader().getColumnModel().getColumn(5).setMinWidth(90);

        
        
        //colocando focu na tabela
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTable1.requestFocus();
            }
        });
    }

    private void buscarExames(String convenioOUgrupo, java.sql.Date diaInicialSql, java.sql.Date diaFinalSql, Integer handleGrupoOUconvenio) {
        listaExames = ExameNotaIpeDAO.getExamesPorConvenioOuGrupo(convenioOUgrupo, diaInicialSql, diaFinalSql, handleGrupoOUconvenio);
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
                modelo.addRow(new Object[] { exame.getFicha(), exame.getPaciente(), exame.getMatricula(), exame.getExame(), exame.getDia(), exame.getValor() });
            }

        }
    }
    
    private void clicarNaTabela() {
		if(jTable1.getSelectedRow() >= 0){
			int atendimento_exame_id = listaExames.get(jTable1.getSelectedRow()).getAtendimtno_exame_id();
			
			int handleConvenio = listaExames.get(jTable1.getSelectedRow()).getHandleConvenio();
			int handleExame = listaExames.get(jTable1.getSelectedRow()).getHandleExame();
			java.sql.Date dataExame = listaExames.get(jTable1.getSelectedRow()).getDataExame();
			
			CalculoValorDeExame calculoExame = new CalculoValorDeExame(handleConvenio, handleExame, dataExame, true);
			double valorConvenioAtual = calculoExame.valor_correto_convenio;
			
			String novo_valor = JOptionPane.showInputDialog("Digite o valor.", valorConvenioAtual);
			
			Double valorDOuble;
			try {
				novo_valor = novo_valor.replaceAll(",", ".");
				valorDOuble = Double.valueOf(novo_valor);
				boolean retorno = atualizaValorConvenio(atendimento_exame_id, valorDOuble);
				if(retorno){
					jTable1.setValueAt(valorDOuble, jTable1.getSelectedRow(), 5);
				}
			} catch (Exception e) {
				//JOptionPane.showMessageDialog(null, "Valor Inválido");
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

        jPanel1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Alterar Valores de Conv\u00EAnios", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));

        jTable1.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Ficha", "Paciente", "Matricula", "Exame", "Dia", "Valor"
        	}
        ) {
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false, false, false
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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 1108, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jScrollPane1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
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
}
