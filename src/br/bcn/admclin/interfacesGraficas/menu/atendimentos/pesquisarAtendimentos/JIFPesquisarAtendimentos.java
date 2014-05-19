/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.menu.atendimentos.pesquisarAtendimentos;

import br.bcn.admclin.ClasseAuxiliares.ColunaAceitandoIcone;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteNumerosELetras;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos.JIFListaAtendimentos;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFCadastroDeAtendimento;
import static br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal.jDesktopPane1;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFPesquisarAtendimentos extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    private List<EditarAtendimentosMODEL> listaAtendimentos = new ArrayList<EditarAtendimentosMODEL>();
    private Connection con = null;

    /**
     * Creates new form EditarAtendimentos
     */
    public JIFPesquisarAtendimentos() {
        initComponents();
        tirandoBarraDeTitulo();
        iniciarClasse();
        ativandoSelecaoDeLinhaComBotaoDireitoDoMouse();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jTFPesquisaPaciente.requestFocusInWindow();
            }
        });
    }

    /*
     * metodo apra selecionar a linha se clicar com o botao direito
     */
    public void ativandoSelecaoDeLinhaComBotaoDireitoDoMouse() {
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

    private void iniciarClasse() {
        // arrumando tamanho das colunas da tabela
        jTable1.getColumnModel().getColumn(0).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(45);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(80);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(38);
        jTable1.getColumnModel().getColumn(6).setMaxWidth(105);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(105);
        jTable1.getColumnModel().getColumn(7).setMaxWidth(60);

        // COLUNA ACEITANDO ICONES
        TableCellRenderer tcrColuna7 = new ColunaAceitandoIcone();
        TableColumn column7 = jTable1.getColumnModel().getColumn(7);
        column7.setCellRenderer(tcrColuna7);

        // selecionar somente uma linha na tabela
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // aumentando tamanho da linha
        jTable1.setRowHeight(30);
    }

    private void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    private void botaoPesquisarAtendimentos(String digitado) {

        ((DefaultTableModel) jTable1.getModel()).setNumRows(0);
        jTable1.updateUI();
        listaAtendimentos.removeAll(listaAtendimentos);

        boolean isNumber;
        try {
            @SuppressWarnings("unused")
            int x = Integer.valueOf(jTFPesquisaPaciente.getText());
            isNumber = true;
        } catch (Exception e) {
            isNumber = false;
        }

        // se for numero pesquisamos por handle_at
        // se nao for numero procuramos por
        if (isNumber) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
            jTFMensagemParaUsuario.setText("Pesquisa por Código");
            // fazer o select pelo handle_at
            con = Conexao.fazConexao();
            ResultSet resultSet =
                EditarAtendimentosDAO.getConsultarAtendimentosPorHandleAt(con, Integer.valueOf(digitado));
            try {
                while (resultSet.next()) {
                    EditarAtendimentosMODEL modelo = new EditarAtendimentosMODEL();
                    modelo.setData_atendimento(resultSet.getString("data_atendimento"));
                    modelo.setHora(MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento")));
                    modelo.setHandle_at(resultSet.getInt("handle_at"));
                    modelo.setMod(resultSet.getString("modalidade"));
                    modelo.setPaciente(resultSet.getString("nomePaciente"));
                    modelo.setMedico_solicitante(resultSet.getString("nomeMedico"));
                    modelo.setCrm(resultSet.getString("crmMedico"));
                    modelo.setStatusA(resultSet.getString("status1"));
                    modelo.setFlagFaturado(resultSet.getInt("flag_faturado"));
                    
                    listaAtendimentos.add(modelo);
                }

                // agora vamos preencher a tabela

                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                for (int i = 0; i < listaAtendimentos.size(); i++) {
                    modelo.addRow(new Object[] {
                        MetodosUteis.converterDataParaMostrarAoUsuario(listaAtendimentos.get(i).getData_atendimento()),
                        listaAtendimentos.get(i).getHora(), listaAtendimentos.get(i).getHandle_at(),
                        listaAtendimentos.get(i).getMod(), listaAtendimentos.get(i).getPaciente(),
                        listaAtendimentos.get(i).getMedico_solicitante(), listaAtendimentos.get(i).getCrm(),
                        listaAtendimentos.get(i).getStatusA() });
                }

                colocarIconesNoStatusA();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao consultar Atendimentos. Procure o Administrador" + e);
            } finally {
                Conexao.fechaConexao(con);
            }
        } else {

            // aqui se for nome, montamos o nome pra enviar ao SQL e fizemos a consulta e montamos a tabela
            String[] nomesDigitados = digitado.split(" ");
            String nomeParaEnviarAoSQL = "";
            for (int i = 0; i < nomesDigitados.length; i++) {
                nomeParaEnviarAoSQL = nomeParaEnviarAoSQL + nomesDigitados[i] + "%";
            }

            if (nomesDigitados.length > 1) {
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                jTFMensagemParaUsuario.setText("Pesquisa por Nome");
                // aqui vamos buscar no banco e coloca na lista
                con = Conexao.fazConexao();
                ResultSet resultSet = EditarAtendimentosDAO.getConsultarAtendimentosPorNome(con, nomeParaEnviarAoSQL);
                try {
                    while (resultSet.next()) {

                        EditarAtendimentosMODEL modelo = new EditarAtendimentosMODEL();
                        modelo.setData_atendimento(resultSet.getString("data_atendimento"));
                        modelo.setHora(MetodosUteis.transformarMinutosEmHorario(resultSet.getInt("hora_atendimento")));
                        modelo.setHandle_at(resultSet.getInt("handle_at"));
                        modelo.setMod(resultSet.getString("modalidade"));
                        modelo.setPaciente(resultSet.getString("nomePaciente"));
                        modelo.setMedico_solicitante(resultSet.getString("nomeMedico"));
                        modelo.setCrm(resultSet.getString("crmMedico"));
                        modelo.setStatusA(resultSet.getString("status1"));
                        modelo.setFlagFaturado(resultSet.getInt("flag_faturado"));
                        
                        listaAtendimentos.add(modelo);
                    }

                    // agora vamos preencher a tabela

                    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

                    for (int i = 0; i < listaAtendimentos.size(); i++) {
                        modelo.addRow(new Object[] {
                            MetodosUteis.converterDataParaMostrarAoUsuario(listaAtendimentos.get(i)
                                .getData_atendimento()), listaAtendimentos.get(i).getHora(),
                            listaAtendimentos.get(i).getHandle_at(), listaAtendimentos.get(i).getMod(),
                            listaAtendimentos.get(i).getPaciente(), listaAtendimentos.get(i).getMedico_solicitante(),
                            listaAtendimentos.get(i).getCrm(), listaAtendimentos.get(i).getStatusA() });
                    }

                    colocarIconesNoStatusA();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Erro ao consultar Atendimentos. Procure o Administrador");
                } finally {
                    Conexao.fechaConexao(con);
                }
            } else {
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(255, 0, 0));
                jTFMensagemParaUsuario.setText("Digite no mínimo Nome e Sobrenome");
            }

        }
    }

    /*
     * coloca os icones dos atendimentos de acordo com o status
     */
    Icon iconeAtendimento = new ImageIcon(getToolkit().createImage(
        getClass().getResource("/br/bcn/admclin/imagens/menuAtendimento.png")));
    Icon iconeImprimuFicha = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imprimirFicha.png"));
    Icon iconeLaudoDigitado = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/laudoDigitado.png"));
    Icon iconeLaudoAssinado = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/laudoAssinado.png"));
    Icon iconeJaFezOExame = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/jaFezOExame.png"));

    private void colocarIconesNoStatusA() throws Exception {
        // icone atendimento
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            try {
                int status1 = Integer.valueOf(String.valueOf(jTable1.getValueAt(i, 7)));
                if (status1 == 0) {
                    jTable1.setValueAt(iconeAtendimento, i, 7);
                }
                // ja imprimiu ficha
                if (status1 == 1) {
                    jTable1.setValueAt(iconeImprimuFicha, i, 7);
                }
                // ja fez o exame
                if (status1 == 2) {
                    jTable1.setValueAt(iconeJaFezOExame, i, 7);
                }
                // laudo digitado
                if (status1 == 4) {
                    jTable1.setValueAt(iconeLaudoDigitado, i, 7);
                }
                // laudo assinado
                if (status1 == 5) {
                    jTable1.setValueAt(iconeLaudoAssinado, i, 7);
                }
            } catch (Exception e) {
                // cai aqui se nao tive nada na coluna do bando status1 (como nao tem nada ainda nao foi feito nada
                // naquele atendimento)
                jTable1.setValueAt(iconeAtendimento, i, 7);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTFPesquisaPaciente = new javax.swing.JTextField(new DocumentoSomenteNumerosELetras(100), null, 0);
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jBPesquisaPaciente2 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pesquisar Atendimentos",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel2.setText("Código / Paciente");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemPesquisar.png"))); // NOI18N

        jTFPesquisaPaciente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFPesquisaPacienteFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFPesquisaPacienteFocusLost(evt);
            }
        });
        jTFPesquisaPaciente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFPesquisaPacienteKeyReleased(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "Data", "Hora", "Código", "Mod.", "Paciente", "Médico Solicitante", "CRM", "Status A." }) {
            private static final long serialVersionUID = 1L;
            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false, false };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setToolTipText("");
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jBPesquisaPaciente2.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/Lupa.png"))); // NOI18N
        jBPesquisaPaciente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPesquisaPaciente2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
            .addComponent(jTFMensagemParaUsuario)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                jPanel1Layout.createSequentialGroup().addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel3).addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTFPesquisaPaciente))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jBPesquisaPaciente2, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel1Layout
                    .createSequentialGroup()
                    .addGroup(
                        jPanel1Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3).addComponent(jLabel2))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFPesquisaPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBPesquisaPaciente2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTFPesquisaPacienteFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaPacienteFocusGained
        if ("".equals(jTFPesquisaPaciente.getText())) {
            jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
            jTFMensagemParaUsuario.setText("Digite um Código ou um Paciente para Pesquisar");
        }

    }// GEN-LAST:event_jTFPesquisaPacienteFocusGained

    private void jTFPesquisaPacienteFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFPesquisaPacienteFocusLost

        jTFMensagemParaUsuario.setText("");

    }// GEN-LAST:event_jTFPesquisaPacienteFocusLost

    private void jTFPesquisaPacienteKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTFPesquisaPacienteKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoPesquisarAtendimentos(jTFPesquisaPaciente.getText());
        } else {
            if ("".equals(jTFPesquisaPaciente.getText())) {
                jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
                jTFMensagemParaUsuario.setText("Digite um Código ou um Paciente para Pesquisar");
            }
        }
    }// GEN-LAST:event_jTFPesquisaPacienteKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        int linhaClicada = jTable1.rowAtPoint(evt.getPoint());
        int linhaSelecionada = jTable1.getSelectedRow();

        if (MouseEvent.BUTTON3 == evt.getButton() && linhaClicada == linhaSelecionada) {
            abrirPopUp(evt);
        }

        jTFMensagemParaUsuario.requestFocusInWindow(); //
    }// GEN-LAST:event_jTable1MouseClicked

    private void jBPesquisaPaciente2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBPesquisaPaciente2ActionPerformed
        botaoPesquisarAtendimentos(jTFPesquisaPaciente.getText());
    }// GEN-LAST:event_jBPesquisaPaciente2ActionPerformed

    ImageIcon iconeVisualizarAtendimento = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/imagemPesquisarInvertida.png"));
    ImageIcon iconeEditarAtendimento = new javax.swing.ImageIcon(getClass().getResource(
        "/br/bcn/admclin/imagens/menuAtendimentoAtendimento.png"));

    private void abrirPopUp(MouseEvent evt) {

        // Paciente recebeu o exame
        JMenuItem editarAtendimento = new JMenuItem("Editar Atendimento", iconeEditarAtendimento);
        editarAtendimento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarAtendimento();
            }
        });

        // Paciente recebeu o laudo
        JMenuItem visualizarAtendimento = new JMenuItem("Visualizar Atendimento", iconeVisualizarAtendimento);
        visualizarAtendimento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizarAtendimento();
            }
        });

        // cria o menu popup e adiciona os itens
        JPopupMenu popup = new JPopupMenu();
        // se for financeiro ou administrador  E  flag_faturado for igual a 0 libera o menu editar atendimento
        int flagFaturado = listaAtendimentos.get(jTable1.getSelectedRow()).getFlagFaturado();
        if ("A".equals(USUARIOS.statusUsuario) || "F".equals(USUARIOS.statusUsuario)) {
            popup.add(editarAtendimento);
            if(flagFaturado == 1){
                editarAtendimento.setEnabled(false);
                editarAtendimento.setText("Editar Atendimento (já faturado)");
            }
        }
        popup.add(visualizarAtendimento);

        // mostra na tela
        int x = evt.getX();
        int y = evt.getY();
        popup.show(jTable1, x, y);
    }

    private void visualizarAtendimento() {
        String data = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        int handle_at = Integer.parseInt(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 2)));
        data = data.replace("/", "-");

        // fechando os outros jinternalframes abertos antes de abrir este
        JInternalFrame[] iframes = jDesktopPane1.getAllFrames();
        // percorrendo todos os jinternalframes pegos
        for (JInternalFrame iframe : iframes) {
            // se tive aberto vo fecha
            if (iframe.isVisible()) {
                // fecha oi internal frame
                iframe.dispose();
                iframe = null;
            }
        }

        janelaPrincipal.internalFrameListaAtendimentos = new JIFListaAtendimentos(handle_at, data);
        jDesktopPane1.add(janelaPrincipal.internalFrameListaAtendimentos);
        janelaPrincipal.internalFrameListaAtendimentos.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameListaAtendimentos.getWidth();
        int aIFrame = janelaPrincipal.internalFrameListaAtendimentos.getHeight();

        janelaPrincipal.internalFrameListaAtendimentos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    @SuppressWarnings("rawtypes")
    public void editarAtendimento() {
        // deixando esse invisivel

        janelaPrincipal.internalFrameJanelaPrincipal.ativarCarregamento();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                janelaPrincipal.internalFramePesquisarAtendimentos.setVisible(false);

                int handle_at = Integer.valueOf(String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 2)));
                String data = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                String hora = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1));

                // abrindo o editar atendimento
                // abrindo a agenda
                janelaPrincipal.internalFrameAtendimento = new JIFCadastroDeAtendimento(handle_at, data, hora);
                janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameAtendimento);
                int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
                int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
                int lIFrame = janelaPrincipal.internalFrameAtendimento.getWidth();
                int aIFrame = janelaPrincipal.internalFrameAtendimento.getHeight();

                janelaPrincipal.internalFrameAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame
                    / 2);

                janelaPrincipal.internalFrameAtendimento.setVisible(true);
                return null;
            }

            @Override
            protected void done() {
                janelaPrincipal.internalFrameJanelaPrincipal.desativarCarregamento();
            }
        };
        worker.execute();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBPesquisaPaciente2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFPesquisaPaciente;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
