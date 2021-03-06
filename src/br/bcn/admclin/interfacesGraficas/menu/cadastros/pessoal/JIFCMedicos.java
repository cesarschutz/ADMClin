/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIFCMedicos.java
 *
 * Created on 07/05/2012, 13:52:58
 */
package br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import br.bcn.admclin.ClasseAuxiliares.DocumentoSemAspasEPorcento;
import br.bcn.admclin.ClasseAuxiliares.DocumentoSomenteLetras;
import br.bcn.admclin.ClasseAuxiliares.MetodosUteis;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.ESPECIALIDADES_MEDICAS;
import br.bcn.admclin.dao.dbris.MEDICOS;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.dao.model.Especialidades_Medicas;
import br.bcn.admclin.dao.model.Medicos;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

/**
 * 
 * @author Cesar Schutz
 */
public class JIFCMedicos extends javax.swing.JInternalFrame {
    private static final long serialVersionUID = 1L;
    String novoOuEditar;
    int medicoId = 0;
    // criando lista para objetos do banco
    public List<Integer> listaCodEspecialidadesMedicas = new ArrayList<Integer>();
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

    /** Creates new form JIFCMedicos */
    public JIFCMedicos(String novoOuEditar, int medicoId) {
        initComponents();
        
        
        
        this.novoOuEditar = novoOuEditar;
        this.medicoId = medicoId;
        iniciarClasse();
        pegandoDataDoSistema();
        if (novoOuEditar == "novo") {
            jBAtualizarRegistro.setVisible(false);
            jBApagarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar novo Médico",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            
            jTFCidade.setText(janelaPrincipal.cidadePadrao);
            jCBUf.setSelectedItem(janelaPrincipal.estadoPadrao);
        } else {
            jBSalvarRegistro.setVisible(false);
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Editar Médico",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
            // preenchendo os campos

            // procura o objeto com o cod igual ao cod da tabela e atualiza os jtextfield
            int indexDoArray = 0;
            int cont = 0;
            while (cont < JIFCMedicosVisualizar.listaMedicos.size()) {
                int codObjetos = JIFCMedicosVisualizar.listaMedicos.get(cont).getMedicoId();
                if (medicoId == codObjetos) {
                    jTFNome.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getNome());
                    jTFCRM.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCrm());
                    jCBUfCRM.setSelectedItem(JIFCMedicosVisualizar.listaMedicos.get(cont).getUfcrm());
                    jTFNascimento.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getNascimento());
                    jTFTelefone.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getTelefone());
                    jTFTelefoneDois.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getTelefoneDois());
                    jTFCpfCnpj.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCpfCnpj());
                    jTFNomeSecretaria.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getNomeSecretaria());
                    jTFCelular.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCelular());
                    jTFEndereco.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getEndereco());
                    jTFBairro.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getBairro());
                    jTFCep.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCep());
                    jTFCidade.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getCidade());
                    jTFEmail.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getEmail());
                    jCBUf.setSelectedItem(JIFCMedicosVisualizar.listaMedicos.get(cont).getUf());
                    for (int i = 0; i < listaCodEspecialidadesMedicas.size(); i++) {
                        if (listaCodEspecialidadesMedicas.get(i) == JIFCMedicosVisualizar.listaMedicos.get(cont)
                            .getEmId()) {
                            indexDoArray = i;
                        }
                    }
                    jTAObs.setText(JIFCMedicosVisualizar.listaMedicos.get(cont).getObs());
                    jCBEspecialidadeMedica.setSelectedIndex(indexDoArray);
                }
                cont++;
            }
            
            // colocando foco na referencia
            jTFNome.requestFocusInWindow();
        }
        tirandoBarraDeTitulo();
        
    }

    public void tirandoBarraDeTitulo() {
        ((BasicInternalFrameUI) this.getUI()).getNorthPane().setPreferredSize(new Dimension(0, 0));
        this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
    }

    /** Metodo que vai no construtor da classe. Eexecuta operações antes de abrir a janela. */
    @SuppressWarnings("unchecked")
    public void iniciarClasse() {
        jTFEmail.setDocument(new DocumentoSemAspasEPorcento(64));
        jTFEndereco.setDocument(new DocumentoSemAspasEPorcento(80));
        jTFBairro.setDocument(new DocumentoSemAspasEPorcento(32));
        jTFCidade.setDocument(new DocumentoSemAspasEPorcento(32));
        
        preencherEspecialidadesMedicas();
        jCBEspecialidadeMedica.setSelectedItem("SEM ESPECIALIDADE");
        
    }
    
    private void preencherEspecialidadesMedicas(){
    	// preenchendo as Especialidades Médicas
        con = Conexao.fazConexao();
        ResultSet resultSet = ESPECIALIDADES_MEDICAS.getConsultar(con);
        listaCodEspecialidadesMedicas.removeAll(listaCodEspecialidadesMedicas);
        jCBEspecialidadeMedica.removeAllItems();
        //jCBEspecialidadeMedica.addItem("");
        //listaCodEspecialidadesMedicas.add(0);
        try {
            while (resultSet.next()) {
                jCBEspecialidadeMedica.addItem(resultSet.getString("descricao"));
                listaCodEspecialidadesMedicas.add(resultSet.getInt("emid"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Não foi possivel preencher as Especialidades Médicas. Procure o administrador.", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }

    /**
     * Volta a janela ao seu estado inicial, dexando inativo os jTextField por exemplo. Atualiza a tabela de acordo com
     * os objetos.
     */
    public void botaoCancelar() {
        this.dispose();
        janelaPrincipal.internalFrameMedicos = null;

        janelaPrincipal.internalFrameMedicosVisualizar = new JIFCMedicosVisualizar();
        janelaPrincipal.jDesktopPane1.add(janelaPrincipal.internalFrameMedicosVisualizar);
        janelaPrincipal.internalFrameMedicosVisualizar.setVisible(true);
        int lDesk = janelaPrincipal.jDesktopPane1.getWidth();
        int aDesk = janelaPrincipal.jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameMedicosVisualizar.getWidth();
        int aIFrame = janelaPrincipal.internalFrameMedicosVisualizar.getHeight();

        janelaPrincipal.internalFrameMedicosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

    }

    public boolean verificarSetudoFoiPreenchidoCorretamente() {
        boolean nomeOk = MetodosUteis.veriricarSeNomeDePessaoEhValido(jTFNome, jTFMensagemParaUsuario);

        boolean nascimentoOk = true;
        if (!"  /  /    ".equals(jTFNascimento.getText())) {
            nascimentoOk = MetodosUteis.verificarSeDataDeNascimentoEValida(jTFNascimento, jTFMensagemParaUsuario);
        }

        boolean emailOk = true;
        if (!"".equals(jTFEmail.getText())) {
        	emailOk = MetodosUteis.verificarSeEmailEValido(jTFEmail, jTFMensagemParaUsuario);
        }
        
        boolean crmMedicoOk = MetodosUteis.VerificarSeTextFieldContemMinimoDeCarcteres(jTFCRM, 2, jTFMensagemParaUsuario);

        if (emailOk && nascimentoOk && nomeOk && crmMedicoOk) {
            return true;
        } else {
            return false;
        }
    }

    /** Salva um novo Médico na banco de dados. */
    public void botaoSalvarRegistro() {

        if (verificarSetudoFoiPreenchidoCorretamente()) {
            // salva no banco
            Medicos medicosMODEL = new Medicos();
            // fazer a inserção no banco
            con = Conexao.fazConexao();
            medicosMODEL.setUsuarioId(USUARIOS.usrId);
            medicosMODEL.setDat(dataDeHojeEmVariavelDate);
            medicosMODEL.setNome(jTFNome.getText());
            medicosMODEL.setCrm(jTFCRM.getText());
            medicosMODEL.setUfcrm((String) jCBUfCRM.getSelectedItem());
            medicosMODEL.setDat(dataDeHojeEmVariavelDate);
            medicosMODEL.setNascimento(jTFNascimento.getText());
            medicosMODEL.setEmail(jTFEmail.getText());
            medicosMODEL.setTelefone(jTFTelefone.getText());
            medicosMODEL.setTelefoneDois(jTFTelefoneDois.getText());
            medicosMODEL.setCelular(jTFCelular.getText());
            medicosMODEL.setCpfCnpj(jTFCpfCnpj.getText());
            medicosMODEL.setNomeSecretaria(jTFNomeSecretaria.getText());
            medicosMODEL.setEndereco(jTFEndereco.getText());
            medicosMODEL.setBairro(jTFBairro.getText());
            medicosMODEL.setCep(jTFCep.getText());
            medicosMODEL.setCidade(jTFCidade.getText());
            medicosMODEL.setUf((String) jCBUf.getSelectedItem());
            medicosMODEL.setEmId(listaCodEspecialidadesMedicas.get(jCBEspecialidadeMedica.getSelectedIndex()));
            medicosMODEL.setObs(jTAObs.getText());
            
            janelaPrincipal.cidadePadrao = medicosMODEL.getCidade();
            janelaPrincipal.estadoPadrao = medicosMODEL.getUf();
            
            boolean cadastro = MEDICOS.setCadastrar(con, medicosMODEL);
            Conexao.fechaConexao(con);
            // atualiza tabela
            if (cadastro) {
                botaoCancelar();
            }
        }
    }

    /** Atualiza umMédico no banco de dados. */
    public void botaoAtualizarRegistro() {

        if (verificarSetudoFoiPreenchidoCorretamente()) {
            // salva no banco
            Medicos medicosMODEL = new Medicos();
            // fazer a inserção no banco
            con = Conexao.fazConexao();
            medicosMODEL.setUsuarioId(USUARIOS.usrId);
            medicosMODEL.setDat(dataDeHojeEmVariavelDate);
            medicosMODEL.setNome(jTFNome.getText());
            medicosMODEL.setMedicoId(medicoId);
            medicosMODEL.setCrm(jTFCRM.getText());
            medicosMODEL.setUfcrm((String) jCBUfCRM.getSelectedItem());
            medicosMODEL.setNascimento(jTFNascimento.getText());
            medicosMODEL.setEmail(jTFEmail.getText());
            medicosMODEL.setTelefone(jTFTelefone.getText());
            medicosMODEL.setTelefoneDois(jTFTelefoneDois.getText());
            medicosMODEL.setCelular(jTFCelular.getText());
            medicosMODEL.setCpfCnpj(jTFCpfCnpj.getText());
            medicosMODEL.setNomeSecretaria(jTFNomeSecretaria.getText());
            medicosMODEL.setEndereco(jTFEndereco.getText());
            medicosMODEL.setBairro(jTFBairro.getText());
            medicosMODEL.setCep(jTFCep.getText());
            medicosMODEL.setCidade(jTFCidade.getText());
            medicosMODEL.setUf((String) jCBUf.getSelectedItem());
            medicosMODEL.setEmId(listaCodEspecialidadesMedicas.get(jCBEspecialidadeMedica.getSelectedIndex()));
            medicosMODEL.setObs(jTAObs.getText());
            
            janelaPrincipal.cidadePadrao = medicosMODEL.getCidade();
            janelaPrincipal.estadoPadrao = medicosMODEL.getUf();
            
            boolean cadastro = MEDICOS.setUpdate(con, medicosMODEL);
            Conexao.fechaConexao(con);
            // atualiza tabela
            if (cadastro) {
                botaoCancelar();
            }
        }
    }

    /** Deleta o Médico selecionado do banco de dados. */
    public void botaoApagarRegistro() {
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente deletar esse Médico?", "ATENÇÃO", 0);
        if (resposta == JOptionPane.YES_OPTION) {
            // fazer o delete de acordo com o codigo
            Medicos medicoMODEL = new Medicos();
            medicoMODEL.setMedicoId(medicoId);
            con = Conexao.fazConexao();
            boolean deleto = MEDICOS.setDeletar(con, medicoMODEL);
            Conexao.fechaConexao(con);
            // atualizar tabela
            if (deleto) {
                botaoCancelar();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTFEmail = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jCBUf = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        new br.bcn.admclin.ClasseAuxiliares.MetodosUteis();
        jTFNascimento = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##/##/####"));
        jLabel3 = new javax.swing.JLabel();
        jTFNome = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(64), null, 0);
        jTFEndereco = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(80), null, 0);
        jTFCidade = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(32), null, 0);
        jLabel15 = new javax.swing.JLabel();
        jTFBairro = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(32), null, 0);
        jLabel13 = new javax.swing.JLabel();
        jTFCep = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("##.###-###"));
        jLabel14 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jTFTelefone = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jTFTelefoneDois = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTFCelular = new JFormattedTextField(MetodosUteis.mascaraParaJFormattedTextField("(##) ####-####"));
        jCBEspecialidadeMedica = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFCRM = new javax.swing.JTextField(new DocumentoSemAspasEPorcento(20), null, 0);
        jLabel7 = new javax.swing.JLabel();
        jCBUfCRM = new javax.swing.JComboBox();
        jBAtualizarRegistro = new javax.swing.JButton();
        jBApagarRegistro = new javax.swing.JButton();
        jBSalvarRegistro = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jTFMensagemParaUsuario = new javax.swing.JTextField();

        setTitle("Cadastro de Médicos");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Médico",
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jTFEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFEmailFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFEmailFocusLost(evt);
            }
        });

        jLabel20.setText("E-Mail");

        jLabel16.setText("UF");

        jCBUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE",
            "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
            "SP", "SE", "TO" }));

        jLabel17.setText("Cidade");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText("Nascimento");

        jTFNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFNascimentoFocusLost(evt);
            }
        });

        jLabel3.setText("Nome");

        jLabel15.setText("CEP");

        jLabel13.setText("Endereço");

        jTFCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCepFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCepFocusLost(evt);
            }
        });

        jLabel14.setText("Bairro");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTFTelefone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFTelefoneFocusLost(evt);
            }
        });

        jLabel11.setText("Telefone");

        jLabel12.setText("Celular");

        jTFCelular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTFCelularFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTFCelularFocusLost(evt);
            }
        });

        jLabel18.setText("Especialidade Medica");

        jLabel6.setText("CRM");

        jLabel7.setText("UF CRM");

        jCBUfCRM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE",
            "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
            "SP", "SE", "TO" }));
        
        btnCadastrarEspecialidadeMdica = new JButton("Cadastrar Especialidade Médica");
        btnCadastrarEspecialidadeMdica.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		botaoCadastrarEspecialidade();
        	}
        });
        btnCadastrarEspecialidadeMdica.setIcon(new ImageIcon(JIFCMedicos.class.getResource("/br/bcn/admclin/imagens/menuPessoalEspecialidadesMedicas.png")));
        
        
        
        JLabel label = new JLabel();
        label.setText("Telefone");
        
        JLabel lblCpfCnpj = new JLabel();
        lblCpfCnpj.setText("CPF");
        
        jTFCpfCnpj = new JFormattedTextField(
				MetodosUteis.mascaraParaJFormattedTextField("###.###.###-##"));
        
        JLabel lblNomeSecretaria = new JLabel();
        lblNomeSecretaria.setText("Nome Secretaria");
        
        jTFNomeSecretaria = new javax.swing.JTextField(new DocumentoSomenteLetras(64), null, 0);
        
        JLabel lblObs = new JLabel();
        lblObs.setText("Obs.");
        
        JScrollPane scrollPane = new JScrollPane();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel3)
        				.addComponent(jLabel13)
        				.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        					.addComponent(jTFNome)
        					.addComponent(jTFEndereco)
        					.addGroup(jPanel1Layout.createSequentialGroup()
        						.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        							.addComponent(jTFBairro, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
        							.addComponent(jLabel14))
        						.addPreferredGap(ComponentPlacement.RELATED)
        						.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        							.addComponent(jLabel15)
        							.addComponent(jTFCep, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE))))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jTFCRM, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel6))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel7)
        						.addComponent(jCBUfCRM, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jLabel5)
        				.addComponent(jTFNascimento, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblObs, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
        				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(jLabel18)
        						.addComponent(btnCadastrarEspecialidadeMdica)
        						.addComponent(jTFNomeSecretaria, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        						.addComponent(jCBEspecialidadeMedica, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        					.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
        					.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING, false)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel17)
        								.addComponent(jTFCidade, GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel16)
        								.addComponent(jCBUf, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))
        						.addComponent(jLabel20)
        						.addComponent(jTFEmail)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jTFTelefone, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
        								.addComponent(jLabel11))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jTFCelular, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        								.addComponent(jLabel12))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(label, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
        								.addComponent(jTFTelefoneDois, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)))
        						.addComponent(jTFCpfCnpj, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        						.addComponent(lblCpfCnpj, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
        						.addComponent(lblNomeSecretaria, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))
        					.addContainerGap(21, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 386, Short.MAX_VALUE)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(jLabel3)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel6)
        						.addComponent(jLabel7))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jTFCRM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jCBUfCRM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel5)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFNascimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel13)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jLabel14)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jLabel15)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblObs)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGap(60)
        							.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
        					.addGap(35))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jLabel17)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel20))
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jLabel16)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jCBUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING, false)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addComponent(jLabel11)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jTFTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        						.addComponent(jTFCelular, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addGroup(jPanel1Layout.createSequentialGroup()
        							.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(label)
        								.addComponent(jLabel12))
        							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        							.addComponent(jTFTelefoneDois, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblCpfCnpj)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFCpfCnpj, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(lblNomeSecretaria)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jTFNomeSecretaria, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel18)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jCBEspecialidadeMedica, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnCadastrarEspecialidadeMdica)
        					.addContainerGap())))
        );
        
        jTAObs = new JTextArea(new DocumentoSemAspasEPorcento(1024));
        scrollPane.setViewportView(jTAObs);
        jPanel1.setLayout(jPanel1Layout);

        jBAtualizarRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/atualizar.png"))); // NOI18N
        jBAtualizarRegistro.setText("Atualizar");
        jBAtualizarRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jBAtualizarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAtualizarRegistroActionPerformed(evt);
            }
        });
        jBAtualizarRegistro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jBAtualizarRegistroFocusLost(evt);
            }
        });
        jBAtualizarRegistro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jBAtualizarRegistroKeyReleased(evt);
            }
        });

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

        jTFMensagemParaUsuario.setBackground(new java.awt.Color(220, 220, 220));
        jTFMensagemParaUsuario.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTFMensagemParaUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTFMensagemParaUsuario.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 613, GroupLayout.PREFERRED_SIZE)
        					.addContainerGap())
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jTFMensagemParaUsuario)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jBCancelar)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jBAtualizarRegistro)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jBApagarRegistro)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jBSalvarRegistro)
        							.addPreferredGap(ComponentPlacement.RELATED, 205, GroupLayout.PREFERRED_SIZE)))
        					.addGap(76))))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 451, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jTFMensagemParaUsuario, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(jBAtualizarRegistro, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBApagarRegistro, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jBSalvarRegistro))
        				.addComponent(jBCancelar, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoCadastrarEspecialidade(){
    	Object[] message = {"Descrição"}; 
    	String especialidade = JOptionPane.showInputDialog(janelaPrincipal.internalFrameJanelaPrincipal, message, "Adicionar Especialidade Médica", JOptionPane.INFORMATION_MESSAGE); 
    	if(especialidade != null){
    		cadastrarEspecialidadeMedica(especialidade.toUpperCase());
    	}
    }
    
    private void cadastrarEspecialidadeMedica(String especialidade){
    	if(especialidade.replaceAll(" ", "").length() < 3){
    		JOptionPane.showMessageDialog(janelaPrincipal.internalFrameJanelaPrincipal, "A descrição deve conter no mínimo 3 caracteres");
    		botaoCadastrarEspecialidade();
    	}else{
    		con = Conexao.fazConexao();
            Especialidades_Medicas especialidadeMedicaMODELO = new Especialidades_Medicas();
            especialidadeMedicaMODELO.setDescricao(especialidade);
            boolean existe = ESPECIALIDADES_MEDICAS.getConsultarParaSalvarNovoRegistro(con, especialidadeMedicaMODELO);
            Conexao.fechaConexao(con);
            if (ESPECIALIDADES_MEDICAS.conseguiuConsulta) {
                if (existe) {
                    JOptionPane.showMessageDialog(null, "Especialidade Médica já existe", "ATENÇÃO",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // fazer a inserção no banco
                    con = Conexao.fazConexao();
                    especialidadeMedicaMODELO.setUsuarioId(USUARIOS.usrId);
                    especialidadeMedicaMODELO.setDat(dataDeHojeEmVariavelDate);
                    boolean cadastro = ESPECIALIDADES_MEDICAS.setCadastrar(con, especialidadeMedicaMODELO);
                    Conexao.fechaConexao(con);
                    // atualiza tabela
                    if (cadastro) {
                        //se cadastro atualizamos o combo box
                    	preencherEspecialidadesMedicas();
                    	jCBEspecialidadeMedica.setSelectedItem(especialidade);
                    	
                    }
                }
            }
    	}
    }
    
    private void jTFNascimentoFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNascimentoFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("dd/mm/aaaa");
    }// GEN-LAST:event_jTFNascimentoFocusGained

    private void jTFTelefoneFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFTelefoneFocusGained

    private void jTFCelularFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCelularFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("(##) ####-####");
    }// GEN-LAST:event_jTFCelularFocusGained

    private void jTFCepFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCepFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("##.###-###");
    }// GEN-LAST:event_jTFCepFocusGained

    private void jTFEmailFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFEmailFocusGained

        jTFMensagemParaUsuario.setForeground(new java.awt.Color(0, 0, 255));
        jTFMensagemParaUsuario.setText("xxx@xxx.xxx");
    }// GEN-LAST:event_jTFEmailFocusGained

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBCancelarActionPerformed
        botaoCancelar();
    }// GEN-LAST:event_jBCancelarActionPerformed

    private void jBCancelarKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBCancelarKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoCancelar();
        }
    }// GEN-LAST:event_jBCancelarKeyReleased

    private void jBSalvarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBSalvarRegistroActionPerformed
        botaoSalvarRegistro();
    }// GEN-LAST:event_jBSalvarRegistroActionPerformed

    private void jBSalvarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBSalvarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoSalvarRegistro();
        }
    }// GEN-LAST:event_jBSalvarRegistroKeyReleased

    private void jBApagarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBApagarRegistroActionPerformed
        botaoApagarRegistro();
    }// GEN-LAST:event_jBApagarRegistroActionPerformed

    private void jBApagarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBApagarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoApagarRegistro();
        }
    }// GEN-LAST:event_jBApagarRegistroKeyReleased

    private void jBAtualizarRegistroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroActionPerformed
        botaoAtualizarRegistro();
    }// GEN-LAST:event_jBAtualizarRegistroActionPerformed

    private void jBAtualizarRegistroKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            botaoAtualizarRegistro();
        }
    }// GEN-LAST:event_jBAtualizarRegistroKeyReleased

    private void jBAtualizarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBAtualizarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));
        jTFCelular.setBackground(new java.awt.Color(255, 255, 255));
        jTFEndereco.setBackground(new java.awt.Color(255, 255, 255));
        jTFBairro.setBackground(new java.awt.Color(255, 255, 255));
        jTFCep.setBackground(new java.awt.Color(255, 255, 255));
        jTFCidade.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBAtualizarRegistroFocusLost

    private void jBSalvarRegistroFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jBSalvarRegistroFocusLost
        jTFMensagemParaUsuario.setText("");
        jTFNome.setBackground(new java.awt.Color(255, 255, 255));
        jTFNascimento.setBackground(new java.awt.Color(255, 255, 255));
        jTFEmail.setBackground(new java.awt.Color(255, 255, 255));
        jTFTelefone.setBackground(new java.awt.Color(255, 255, 255));
        jTFCelular.setBackground(new java.awt.Color(255, 255, 255));
        jTFEndereco.setBackground(new java.awt.Color(255, 255, 255));
        jTFBairro.setBackground(new java.awt.Color(255, 255, 255));
        jTFCep.setBackground(new java.awt.Color(255, 255, 255));
        jTFCidade.setBackground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_jBSalvarRegistroFocusLost

    private void jTFNascimentoFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFNascimentoFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFNascimentoFocusLost

    private void jTFTelefoneFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFTelefoneFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFTelefoneFocusLost

    private void jTFCelularFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCelularFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFCelularFocusLost

    private void jTFEmailFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFEmailFocusLost
        jTFMensagemParaUsuario.setText("");
    }// GEN-LAST:event_jTFEmailFocusLost

    private void jTFCepFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTFCepFocusLost
        jTFMensagemParaUsuario.setText("");
        
        try {
			String CEP = jTFCep.getText().replaceAll("\\.", "").replaceAll("\\-", "");
			String endereco = MetodosUteis.getEndereco(CEP);
			String bairro= MetodosUteis.getBairro(CEP);
			String cidade = MetodosUteis.getCidade(CEP);
			String uf = MetodosUteis.getUF(CEP);
			
			jTFEndereco.setText(endereco);
			jTFBairro.setText(bairro);
			jTFCidade.setText(cidade);
			jCBUf.setSelectedItem(uf);
			
		} catch (Exception e) {
		}
    }// GEN-LAST:event_jTFCepFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBApagarRegistro;
    private javax.swing.JButton jBAtualizarRegistro;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBSalvarRegistro;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBEspecialidadeMedica;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBUf;
    @SuppressWarnings("rawtypes")
    private javax.swing.JComboBox jCBUfCRM;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTFBairro;
    private javax.swing.JTextField jTFCRM;
    private javax.swing.JTextField jTFCelular;
    private javax.swing.JTextField jTFCep;
    private javax.swing.JTextField jTFCidade;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEndereco;
    private javax.swing.JTextField jTFMensagemParaUsuario;
    private javax.swing.JTextField jTFNascimento;
    private javax.swing.JTextField jTFNome;
    private javax.swing.JTextField jTFTelefone;
    private JButton btnCadastrarEspecialidadeMdica;
    private JTextField jTFTelefoneDois;
    private JTextField jTFCpfCnpj;
    private JTextField jTFNomeSecretaria;
    private JTextArea jTAObs;
}
