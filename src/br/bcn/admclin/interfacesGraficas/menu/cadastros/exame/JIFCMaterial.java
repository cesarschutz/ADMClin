/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCMaterial.java
 *
 * Created on 21/05/2012, 12:47:02
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.exame;

import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSemeComercialESemJogoDaVelhaParaOsMateriais;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteNumerosELetras;
import br.bcn.admclin.ClasseAuxiliares.JTextFieldDinheiroReais;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.MATERIAIS;
import br.bcn.admclin.dao.dbris.TABELAS;
import br.bcn.admclin.dao.dbris.VALORESMATERIAIS;
import br.bcn.admclin.dao.model.Materiais;
import br.bcn.admclin.dao.model.ValoresMateriais;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCMaterial extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    String novoOuEditar;
    int handle_material;
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

    /** Creates new form JIFCMaterial */
    public JIFCMaterial(String novoOuEditar, int handle_material) {
        initComponents();
        this.novoOuEditar = novoOuEditar;
        this.handle_material = handle_material;
        iniciarClasse();
        pegandoDataDoSistema();
        tirandoBarraDeTitulo();
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /**
     * Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela.
     */
    public void iniciarClasse() {
        jXDatePicker1.setFormats(new String[] { "E dd/MM/yyyy" });
        jXDatePicker1.setLinkDate(System.currentTimeMillis(), "Hoje");

        jTable2.setAutoCreateRowSorter(true);
        // selecionar somente uma linha na tabela
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // deixando invisivel a coluna 0 da tabela (onde irá o codigo)
        jTable2.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getColumnModel().getColumn(0).setMinWidth(0);
        jTable2.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        jTable2.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        // colocando maximo de caracteres nos jtextfield
        jTDescricao.setDocument(new DocumentoSemeComercialESemJogoDaVelhaParaOsMateriais(64));
        jTable2.setRowHeight(20);

        if ("novo".equals(novoOuEditar)) {
            jBAdicionarValor.setVisible(false);
            jBRemover.setVisible(false);
            jBEditar.setVisible(false);
            jBApagarRegistro.setVisible(false);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Material",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        } else {
            jBSalvarRegistro.setVisible(false);
            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Material",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            atualizarTabelaDeValores(handle_material);

            int cont = 0;
            while (cont < JIFCMaterialVisualizar.listaMateriais.size()) {
                int codObjetos = JIFCMaterialVisualizar.listaMateriais.get(cont).getHandle_material();
                if (handle_material == codObjetos) {
                    jTDescricao.setText(JIFCMaterialVisualizar.listaMateriais.get(cont).getNome());
                    jTFCodigo.setText(JIFCMaterialVisualizar.listaMateriais.get(cont).getCodigo());
                }
                cont++;
            }
        }

    }

    /**
     * Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com
     * os objetos.
     */
    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameMateriais = null;

        janelaPrincipal.internalFrameMateriaisVisualizar = new JIFCMaterialVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameMateriaisVisualizar);
        janelaPrincipal.internalFrameMateriaisVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameMateriaisVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameMateriaisVisualizar.getHeight();

        janelaPrincipal.internalFrameMateriaisVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    /**
     * Salva um novo material na banco de dados.
     */
    public void botaoSalvarRegistro() {

        boolean descricaoPreenchida =
            MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
        boolean codigoPreenchido =
            MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFCodigo, 1, jTFMensagemParaUsuario);

        if (descricaoPreenchida && codigoPreenchido) {
            // fazendo um if para verificar se descricao ou referencia ja existem
            con = Conexao.fazConexao();
            Materiais materialModelo = new Materiais();
            materialModelo.setNome(jTDescricao.getText().toUpperCase());
            materialModelo.setCodigo(jTFCodigo.getText().toUpperCase());
            materialModelo.setData(dataDeHojeEmVariavelDate);
            boolean existe = MATERIAIS.getConsultarParaSalvarNovoRegistro(con, materialModelo);
            Conexao.fechaConexao(con);
            if (MATERIAIS.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Descrição ou Código já existe.", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // fazer a inserção no banco
                    con = Conexao.fazConexao();
                    ValoresMateriais valorMaterialModel = new ValoresMateriais();
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
                    valorMaterialModel.setDataAValer(data);
                    valorMaterialModel.setValor(Double.valueOf(jTFValor.getText().replace(",", ".")));
                    valorMaterialModel.setData(dataDeHojeEmVariavelDate);
                    boolean cadastro = MATERIAIS.setCadastrar(con, materialModelo, valorMaterialModel);
                    Conexao.fechaConexao(con);
                    // atualiza tabela
                    if (cadastro) {
                        botaoCancelar();
                    }
                }
            }
        }
    }

    public void botaoSalvarValorDeMaterial(int handle_material) {

        boolean dataMaiorQueUltimaCadastrada;
        // verificando se a data digitada eh maior que a ultima cadastrada
        con = Conexao.fazConexao();
        if (jTable2.getRowCount() == 0) {
            dataMaiorQueUltimaCadastrada = true;
        } else {
            Date dataSelecionada = jXDatePicker1.getDate();
            // criando um formato de data
            SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
            // colocando data selecionado no formato criado acima
            String dataString = dataFormatada.format(dataSelecionada);
            dataMaiorQueUltimaCadastrada =
                VALORESMATERIAIS.getConsultarSeDataEhMenorQueAultimaCadastrada(con, handle_material, dataString,
                    jTFMensagemParaUsuario);
        }

        if (dataMaiorQueUltimaCadastrada) {
            // fazer a inserção no banco
            con = Conexao.fazConexao();
            ValoresMateriais valorMaterialModel = new ValoresMateriais();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date data = null;
            Date dataSelecionada2 = jXDatePicker1.getDate();
            // criando um formato de data
            SimpleDateFormat dataFormatada2 = new SimpleDateFormat("dd/MM/yyyy");
            // colocando data selecionado no formato criado acima
            String data2 = dataFormatada2.format(dataSelecionada2);

            try {
                data = new java.sql.Date(format.parse(data2).getTime());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Preencha a data corretamente");
            }

            valorMaterialModel.setDataAValer(data);
            valorMaterialModel.setValor(Double.valueOf(jTFValor.getText().replace(",", ".")));
            valorMaterialModel.setData(dataDeHojeEmVariavelDate);
            boolean cadastro = VALORESMATERIAIS.setCadastrar(con, valorMaterialModel, handle_material);
            Conexao.fechaConexao(con);
            // atualiza tabela
            if (cadastro) {
                atualizarTabelaDeValores(handle_material);
            }
        }
    }

    /**
     * Apaga o material selecionado do banco de dados.
     */
    public void botaoApagarRegistro() {

        con = Conexao.fazConexao();
        boolean utilizada = TABELAS.verificarSeMaterialEstaSendoUtilizado(con, handle_material);
        if (utilizada) {
            JOptionPane.showMessageDialog(null,
                "Este Material não pode ser deletado pois é utilizado em alguma Tabela.", "ATENÇÃO",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Material?", "ATENÇÃO", 0);
            if (resposta == JOptionPane.YES_OPTION) {
                con = Conexao.fazConexao();
                // boolean utilizada = tb_classesdeexamesDAO.getConsultarSeClasseEstaSendoUtilizada(con, codTabela);
                Conexao.fechaConexao(con);
                // if(utilizada){
                // JOptionPane.showMessageDialog(null,
                // "Esta Classe de Exame  não pode ser deletada pois está sendo utilizada por algum Exame","ATENÇÃO",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                // }else{
                // fazer o delete de acordo com o codigo
                Materiais materialModel = new Materiais();
                materialModel.setHandle_material(handle_material);
                con = Conexao.fazConexao();
                boolean deleto = MATERIAIS.setDeletar(con, materialModel);
                Conexao.fechaConexao(con);
                // atualizar tabela
                if (deleto) {
                    con = Conexao.fazConexao();
                    VALORESMATERIAIS.setDeletar(con, handle_material);
                    Conexao.fechaConexao(con);
                    botaoCancelar();
                }
            }
        }

    }

    public void atualizarMaterial() {
        boolean descricaoPreenchida =
            MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
        boolean codigoPreenchido =
            MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFCodigo, 1, jTFMensagemParaUsuario);

        if (codigoPreenchido && descricaoPreenchida) {
            con = Conexao.fazConexao();
            Materiais materialModelo = new Materiais();
            materialModelo.setNome(jTDescricao.getText().toUpperCase());
            materialModelo.setCodigo(jTFCodigo.getText().toUpperCase());
            materialModelo.setData(dataDeHojeEmVariavelDate);
            materialModelo.setHandle_material(handle_material);
            boolean existe = MATERIAIS.getConsultarParaAtualizarRegistro(con, materialModelo);
            if (!existe) {
                boolean cadastro = MATERIAIS.setUpdate(con, materialModelo);
                Conexao.fechaConexao(con);
                if (cadastro) {
                    botaoCancelar();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Descrição ou Código já existe.", "ATENÇÃO",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    public void atualizarTabelaDeValores(int handle_material) {
        ((DefaultTableModel) jTable2.getModel()).setNumRows(0);
        jTable2.updateUI();
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        con = Conexao.fazConexao();
        ResultSet resultSet = VALORESMATERIAIS.getConsultar(con, handle_material);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                modelo.addRow(new Object[] { resultSet.getInt("valorMaterialId"),
                    resultSet.getString("valor").replace(".", ","),
                    MetodosUteis.converterDataParaMostrarAoUsuario(resultSet.getString("dataavaler")) });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel atualizar a tabela de Valores do Material. Procure o Administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTFMensagemParaUsuario = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTDescricao = new javax.swing.JTextField(new DocumentoSomenteNumerosELetras(64), null, 0);
        jTFValor = new JTextFieldDinheiroReais(new DecimalFormat("0.00")) {
            private static final long serialVersionUID = 1L;

            {// limita a 8
             // caracteres
                setLimit(8);
            }
        };
        jLValor = new javax.swing.JLabel();
        jLDataValor = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jBAdicionarValor = new javax.swing.JButton();
        jBRemover = new javax.swing.JButton();
        jLValor1 = new javax.swing.JLabel();
        jTFCodigo = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(16), null, 0);
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jBEditar = new javax.swing.JButton();

        setTitle("Cadastro de Materiais");

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Material",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel5.setText("Nome");

        jTDescricao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTDescricaoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTDescricaoFocusLost(evt);
            }
        });

        jLValor.setText("Valor");

        jLDataValor.setText("Data a Valer Valor");

        jBAdicionarValor
            .setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaPraDireita.png"))); // NOI18N
        jBAdicionarValor.setText("Adicionar");
        jBAdicionarValor.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jBAdicionarValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAdicionarValorActionPerformed(evt);
            }
        });
        jBAdicionarValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAdicionarValorKeyReleased(evt);
            }
        });

        jBRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBRemover.setText("Remover");
        jBRemover.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jBRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBRemoverActionPerformed(evt);
            }
        });

        jLValor1.setText("Código");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout
            .setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel3Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel3Layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel3Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel3Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addComponent(jTDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 456,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(
                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                    jPanel3Layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            jPanel3Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    jPanel3Layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            jPanel3Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLDataValor)
                                                                .addComponent(jXDatePicker1,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 154,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(
                                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(
                                                            jPanel3Layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLValor)
                                                                .addComponent(jTFValor,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE, 107,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addComponent(jLValor1)
                                                .addComponent(jTFCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 107,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(
                                            jPanel3Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jBAdicionarValor, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jBRemover, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel3Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTDescricao, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBAdicionarValor, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addComponent(jLValor1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTFCodigo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        jPanel3Layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jBRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(
                                jPanel3Layout.createSequentialGroup().addComponent(jLDataValor).addGap(26, 26, 26))
                            .addGroup(
                                jPanel3Layout
                                    .createSequentialGroup()
                                    .addComponent(jLValor)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        jPanel3Layout
                                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTFValor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(50, Short.MAX_VALUE)));

        jBApagarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/deletar.png"))); // NOI18N
        jBApagarRegistro.setText("Apagar");
        jBApagarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBApagarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBApagarRegistroActionPerformed(evt);
            }
        });
        jBApagarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBApagarRegistroKeyReleased(evt);
            }
        });

        jBSalvarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/salvar.png"))); // NOI18N
        jBSalvarRegistro.setText("Salvar");
        jBSalvarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBSalvarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalvarRegistroActionPerformed(evt);
            }
        });
        jBSalvarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBSalvarRegistroFocusLost(evt);
            }
        });
        jBSalvarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBSalvarRegistroKeyReleased(evt);
            }
        });

        jBCancelar.setBackground(new java.awt.Color(113, 144, 224));
        jBCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/imagemSetaParaEsquerda.png"))); // NOI18N
        jBCancelar.setText("Voltar");
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

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Valores do Material",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "valor Material Id", "Valor", "Data a Valer" }) {
            private static final long serialVersionUID = 1L;
            @SuppressWarnings("rawtypes")
            Class[] types = new Class[] { java.lang.Integer.class, java.lang.String.class, java.lang.String.class };
            boolean[] canEdit = new boolean[] { false, false, false };

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel4Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252,
                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                jPanel4Layout.createSequentialGroup().addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addContainerGap()));

        jBEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBEditar.setText("Editar");
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBEditarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTFMensagemParaUsuario)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(
                                layout.createSequentialGroup().addComponent(jBCancelar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBSalvarRegistro)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBEditar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBApagarRegistro))).addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout
                .createSequentialGroup()
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTFMensagemParaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 44,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(
                    layout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                            layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jBSalvarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBEditar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBApagarRegistro))
                        .addComponent(jBCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(37, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar(); // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro(); // TODO add your handling code here:
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBSalvarRegistroKeyReleased

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro(); // TODO add your handling code here:
    }// GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBApagarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();
        } // TODO add your handling code here:
    }// GEN-LAST:event_jBApagarRegistroKeyReleased

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTDescricao.setBackground(new java.awt.Color(255, 255, 255));
        jTFCodigo.setBackground(new java.awt.Color(255, 255, 255));
        jTFValor.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jTDescricaoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTDescricaoFocusGained
        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("Mínimo 2 caracteres");
    }// GEN-LAST:event_jTDescricaoFocusGained

    private void jTDescricaoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTDescricaoFocusLost
        jTFMensagemParaUsuario.setText("");
        boolean ok = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTDescricao, 2, jTFMensagemParaUsuario);
        if (ok) {
            jTDescricao.setBackground(new java.awt.Color(255, 255, 255));
        }
    }// GEN-LAST:event_jTDescricaoFocusLost

    private void jBAdicionarValorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAdicionarValorActionPerformed
        botaoSalvarValorDeMaterial(handle_material);
    }// GEN-LAST:event_jBAdicionarValorActionPerformed

    private void jBAdicionarValorKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAdicionarValorKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarValorDeMaterial(handle_material);
        }
    }// GEN-LAST:event_jBAdicionarValorKeyReleased

    private void jBRemoverActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBRemoverActionPerformed
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Valor?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            // fazer o delete de acordo com o codigo
            con = Conexao.fazConexao();
            int valorMaterialId = Integer.valueOf(String.valueOf(jTable2.getValueAt(jTable2.getSelectedRow(), 0)));
            boolean deleto = VALORESMATERIAIS.setDeletarUmValor(con, valorMaterialId);
            Conexao.fechaConexao(con);
            if (deleto) {
                atualizarTabelaDeValores(handle_material);
            }
        }
    }// GEN-LAST:event_jBRemoverActionPerformed

    private void jBEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBEditarActionPerformed
        atualizarMaterial();
    }// GEN-LAST:event_jBEditarActionPerformed

    private void jBEditarFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBEditarFocusLost
        jTFMensagemParaUsuario.setText("");
        jTDescricao.setBackground(new java.awt.Color(255, 255, 255));
        jTFCodigo.setBackground(new java.awt.Color(255, 255, 255));
        jTFValor.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBEditarFocusLost

    private void jBEditarKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBEditarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            atualizarMaterial();
        }
    }// GEN-LAST:event_jBEditarKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAdicionarValor;
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBEditar;
    private javax.swing.JButton jBRemover;
    private javax.swing.JButton jBSalvarRegistro;
    private javax.swing.JLabel jLDataValor;
    private javax.swing.JLabel jLValor;
    private javax.swing.JLabel jLValor1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTDescricao;
    private javax.swing.JTextField jTFCodigo;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFValor;
    private javax.swing.JTable jTable2;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables
}
