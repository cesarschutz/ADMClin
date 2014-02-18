package br.bcn.admclin.interfacesGraficas.menu.financeiro.recebimentoConvenio;

import java.awt.Dimension;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class jIFListaDeExamesParaRecebimento extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    JPanel panel;
    private String tipo;
    private Date data1;
    private Date data2;
    private int handle_convenio;
    private int handle_grupo;
    public static String ultimaDataDigitada = "";
    private ArrayList<Atendimento_Exames> listaExames = new ArrayList<Atendimento_Exames>();
    ImageIcon iconeSim = new javax.swing.ImageIcon(getClass().getResource(
                    "/br/bcn/admclin/imagens/sim.png"));

    /**
     * Create the frame.
     */
    public jIFListaDeExamesParaRecebimento(String tipo, Date data1, Date data2, int hanle_convenio_grupo,
        String nomeGrupoConvenio) {

        initComponents();

        this.tipo = tipo;
        this.data1 = data1;
        this.data2 = data2;

        if (tipo.equals("convenio")) {
            this.handle_convenio = hanle_convenio_grupo;
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recebimento de Convênio: "
                + nomeGrupoConvenio, javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.DEFAULT_POSITION));
        } else {
            this.handle_grupo = hanle_convenio_grupo;
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recebimento de Grupo: "
                + nomeGrupoConvenio, javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.DEFAULT_POSITION));
        }

        iniciarClasse();

    }

    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    private void iniciarClasse() {
        tirandoBarraDeTitulo();
        buscarExames();
        preencheTabela();
        
        jTable1.setRowHeight(30);
        
        // definindo o tamanho das colunas
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(4).setMinWidth(100);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(5).setMinWidth(100);
        jTable1.getColumnModel().getColumn(5).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(6).setMinWidth(100);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(65);
        
        // alinhando conteudo da coluna de uma tabela
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(6).setCellRenderer(direita);
        jTable1.getColumnModel().getColumn(7).setCellRenderer(centralizado);
        
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        
        // sumindo coluna
        jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(8).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMinWidth(0);
        
        //coluna aceitando icone
        TableCellRenderer tcrColuna7 = new ColunaAceitandoIcone();
        TableColumn column7 = jTable1.getColumnModel().getColumn(7);
        column7.setCellRenderer(tcrColuna7);
        
        //definindo seleção da tabela
        jTable1.addRowSelectionInterval(0,0); 
        
        //colocando focu na tabela
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTable1.requestFocus();
            }
        });
    }

    private void buscarExames() {
        try {
            if (tipo.equals("convenio")) {
                listaExames =
                    atendimentoDAO.buscaExamesParaConciliarPagamentoConvenio("convenio", data1, data2, handle_convenio);
            } else {
                listaExames =
                    atendimentoDAO.buscaExamesParaConciliarPagamentoConvenio("grupo", data1, data2, handle_grupo);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar os Exames. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            listaExames = null;
        }
    }

    private void preencheTabela() {
        if (listaExames != null) {
            
            // limpa a tabela
            ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
            jTable1.updateUI();
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

            // preenche a tabela com os atendimentos
            for (Atendimento_Exames exame : listaExames) {
                String valor_recebido_convenio = MetodosUteis.colocarZeroEmCampoReais(exame.getVALOR_RECEBIDO_CONVENIO()).replace(".", ",");
                String data_recebido_convenio = "";
                try {
                    data_recebido_convenio = MetodosUteis.converterDataParaMostrarAoUsuario(exame.getDATA_RECEBIDO_CONVENIO().toString());
                } catch (Exception e) {
                    data_recebido_convenio = "";
                }
                int flag_conciliado = exame.getFLAG_CONCILIADO();
                modelo.addRow(new Object[] { exame.getHANDLE_AT(),
                    MetodosUteis.converterDataParaMostrarAoUsuario(exame.getData().toString()), exame.getPaciente(),
                    exame.getNomeExame(), MetodosUteis.colocarZeroEmCampoReais(exame.getVALOR_CORRETO_CONVENIO()).replace(".", ","), valor_recebido_convenio, data_recebido_convenio, flag_conciliado, exame.getATENDIMENTO_EXAME_ID() });
            }

            colocarIconeDeConciliado();
        }
    }
    
    private void clicarNaTabela(){
        if(jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString().equals("")){
            janelaPrincipal.internalFrameJanelaPrincipal.setEnabled(false);
            Object dataExame = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            Object nomePaciente = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            Object nomeExame = jTable1.getValueAt(jTable1.getSelectedRow(), 3);
            Object valorAReceber= jTable1.getValueAt(jTable1.getSelectedRow(), 4);
            Object atendimento_exame_id = jTable1.getValueAt(jTable1.getSelectedRow(), 8);
            jFDefinirValorRecebido jFDefinirValorRecebido = new jFDefinirValorRecebido(dataExame.toString(), nomePaciente.toString(), nomeExame.toString(), valorAReceber.toString(), ultimaDataDigitada, (int) atendimento_exame_id);
            jFDefinirValorRecebido.setVisible(true);
        } else{
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente alterar este valor?", "ATENÇÃO", 0);
            if (resposta == JOptionPane.YES_OPTION) {
                janelaPrincipal.internalFrameJanelaPrincipal.setEnabled(false);
                Object dataExame = jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                Object nomePaciente = jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                Object nomeExame = jTable1.getValueAt(jTable1.getSelectedRow(), 3);
                Object valorAReceber= jTable1.getValueAt(jTable1.getSelectedRow(), 4);
                Object atendimento_exame_id = jTable1.getValueAt(jTable1.getSelectedRow(), 8);
                jFDefinirValorRecebido jFDefinirValorRecebido = new jFDefinirValorRecebido(dataExame.toString(), nomePaciente.toString(), nomeExame.toString(), valorAReceber.toString(), ultimaDataDigitada, (int) atendimento_exame_id);
                jFDefinirValorRecebido.setVisible(true);
            }
        }
        
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "xxx",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Código", "Data", "Paciente", "Exame", "Valor", "Valor Recebido", "Data Recebido", "Recebido", "Atendimento_Exame_Id" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }

    private void colocarIconeDeConciliado(){
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            if (jTable1.getValueAt(i, 7).toString().equals("1")) {
                jTable1.setValueAt(iconeSim, i, 7);
            } else {
                jTable1.setValueAt("", i, 7);
            }
        }
    }
    
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable jTable1;
}
