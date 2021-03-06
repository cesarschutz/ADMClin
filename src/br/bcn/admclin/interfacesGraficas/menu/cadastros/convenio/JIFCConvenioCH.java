/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCConvenioCH.java
 *
 * Created on 25/07/2012, 13:15:30
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.JTextFieldDinheiroReais;
import br.bcn.admclin.dao.dbris.CONVENIOCH;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.ConvenioCh;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCConvenioCH extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    int handle_convenio;
    private Connection con = null;
    java.sql.Date dataDeHojeEmVariavelDate = null;

    public void pegandoDataDoSistema() {
        // pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dataDeHoje = format.format(hoje.getTime());
        try {
            dataDeHojeEmVariavelDate = new java.sql.Date(format.parse(dataDeHoje).getTime());
        } catch (ParseException ex) {

        }
    }

    /** Creates new form JIFCConvenioCH */
    public JIFCConvenioCH(int handle_convenio, String nomeConvenio) {
        initComponents();
        this.handle_convenio = handle_convenio;
        iniciarClasse();
        atualizarTabela();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valores CH: " + nomeConvenio,
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    public void atualizarTabela() {
        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = CONVENIOCH.getConsultar(con, handle_convenio);
        try {
            while (resultSet.next()) {
                modelo.addRow(new Object[] { resultSet.getInt("convenioChId"),
                    resultSet.getString("valor").replace(".", ","),
                    MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("dataavaler")) });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o Administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    public void iniciarClasse() {
        // deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");

        jTFValorCh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTable1.setRowHeight(20);
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void botaoAdicionar() {
        boolean dataMaiorQueUltimaCadastrada = false;
        // verificando se a data digitada eh maior que a ultima cadastrada
        con = Conexao.fazConexao();

        if (jTable1.getRowCount() == 0) {
            dataMaiorQueUltimaCadastrada = true;
        } else {
            Date dataSelecionada = jXDatePicker1.getDate();
            // criando um formato de data
            SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
            // colocando data selecionado no formato criado acima
            String data = dataFormatada.format(dataSelecionada);
            dataMaiorQueUltimaCadastrada =
                CONVENIOCH.getConsultarSeDataEhMenorQueAultimaCadastrada(con, handle_convenio, data);
        }

        if (dataMaiorQueUltimaCadastrada) {

            // fazer a inserção no banco
            con = Conexao.fazConexao();
            ConvenioCh conveniosChModel = new ConvenioCh();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date data = null;
            Date dataSelecionada = jXDatePicker1.getDate();
            // criando um formato de data
            SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
            // colocando data selecionado no formato criado acima
            String data2 = dataFormatada.format(dataSelecionada);

            try {
                data = new java.sql.Date(format.parse(data2).getTime());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Preencha a data corretamente");
            }

            conveniosChModel.setUsuarioId(USUARIOS.usrId);
            conveniosChModel.setHandle_convenio(handle_convenio);
            conveniosChModel.setDataAValer(data);
            conveniosChModel.setValor(Double.valueOf(jTFValorCh.getText().replaceAll(",", ".")));
            conveniosChModel.setDat(dataDeHojeEmVariavelDate);
            boolean cadastro = CONVENIOCH.setCadastrar(con, conveniosChModel);
            Conexao.fechaConexao(con);
            // atualiza tabela
            if (cadastro) {
                atualizarTabela();
                jTFValorCh.setText("");
            }
        }
    }

    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameConveioCH = null;

        janelaPrincipal.internalFrameConvenios.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameConvenios.getWidth();
        int aIFrame = janelaPrincipal.internalFrameConvenios.getHeight();

        janelaPrincipal.internalFrameConvenios.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jTFValorCh = new JTextFieldDinheiroReais(new DecimalFormat("0.00000")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(8);
            }
        };
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jBAdicionar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        Remover = new javax.swing.JButton();

        setTitle("Valores CH");

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
        jBCancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
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

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18));
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "AHAM",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Valor do CH");

        jLabel2.setText("Data a Valer o Valor");

        jBAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaPraDireita.png"))); // NOI18N
        jBAdicionar.setText("Adicionar");
        jBAdicionar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jBAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAdicionarActionPerformed(evt);
            }
        });
        jBAdicionar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAdicionarFocusLost(evt);
            }
        });
        jBAdicionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAdicionarKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "valorChId", "Valor", "Data a Valer" }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        Remover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        Remover.setText("Remover");
        Remover.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Remover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBAdicionar, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFValorCh, javax.swing.GroupLayout.Alignment.LEADING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jXDatePicker1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Remover, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFValorCh, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel2)
                                    .addGap(11, 11, 11)
                                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jBAdicionar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(Remover)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addComponent(jBCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addComponent(jBCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 493) / 2, (screenSize.height - 413) / 2, 493, 413);
    }// </editor-fold>//GEN-END:initComponents

    private void jBAdicionarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAdicionarActionPerformed
        botaoAdicionar(); // TODO add your handling code here:
    }// GEN-LAST:event_jBAdicionarActionPerformed

    private void jBAdicionarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAdicionarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFValorCh.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBAdicionarFocusLost

    private void jBAdicionarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAdicionarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAdicionar();
        }
    }// GEN-LAST:event_jBAdicionarKeyReleased

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void RemoverActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RemoverActionPerformed
        if (jTable1.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Valor?", "ATENÇÃO", 0);
            if (resposta == JOptionPane.YES_OPTION) {
                // fazer o delete de acordo com o codigo
                con = Conexao.fazConexao();
                int convenioChId = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
                boolean deleto = CONVENIOCH.setDeletarUmValor(con, convenioChId);
                Conexao.fechaConexao(con);
                if (deleto) {
                    atualizarTabela();
                }
            }
        }
    }// GEN-LAST:event_RemoverActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Remover;
    private javax.swing.JButton jBAdicionar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFValorCh;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables
}
