package br.bcn.admclin.interfacesGraficas.menu.financeiro.recebimentoConvenio;

import java.awt.Dimension;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.model.Atendimento_Exames;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio.atendimentoDAO;

import com.lowagie.text.List;

import javax.swing.JTable;

public class jIFListaDeExamesParaRecebimento extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    JPanel panel;
    private String tipo;
    private Date data1;
    private Date data2;
    private int handle_convenio;
    private int handle_grupo;
    private ArrayList<Atendimento_Exames> listaExames = new ArrayList<Atendimento_Exames>();

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
                modelo.addRow(new Object[] { exame.getHANDLE_AT(),
                    MetodosUteis.converterDataParaMostrarAoUsuario(exame.getData().toString()), exame.getPaciente(),
                    exame.getNomeExame(), exame.getVALOR_CORRETO_CONVENIO() });
            }

        }
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "xxx",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Código", "Data", "Paciente", "Exame", "Valor", "Valor Recebido", "Data Recebido" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

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

    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}
