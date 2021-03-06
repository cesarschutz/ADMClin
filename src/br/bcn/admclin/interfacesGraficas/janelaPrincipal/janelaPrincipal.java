/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.interfacesGraficas.janelaPrincipal;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import br.bcn.admclin.ClasseAuxiliares.ImagemNoJDesktopPane;
import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.USUARIOS;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.agendamentos.JIFListaAgendamentos;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.consultaValorExames.ConsultaValorExames;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.consultaValorExames.ListaConvenios;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.fichasDeAtendimentos.JIFListaAtendimentos;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.pesquisarAtendimentos.JIFPesquisarAtendimentos;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFAtendimentoSelecionarUmMedicoSolicitante;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFAtendimentoSelecionarUmPaciente;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFCadastroDeAtendimento;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFCMedicosAtendimentos;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.JIFCPacientesAtendimentos;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.registrarAtendimento.jIFAlterarValorDeExame;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.relatoriodecaixa.JIFRelatorioDeCaixa;
import br.bcn.admclin.interfacesGraficas.menu.atendimentos.relatoriodecaixa.JIFRelatorioDeMovimentos;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda.JIFCadastroAgendaDesc;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda.JIFCadastroAgendaVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda.JIFCadastroBloqueio;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda.JIFFeriado;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.agenda.JIFFeriadoVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCConvenioCH;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCConvenioFILME;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCConvenios;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCConveniosVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCTabelas;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCTabelasAdicionarUMExame;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCTabelasAdicionarUmMaterialAUmExame;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCTabelasEditarCoeficientesDeUmExame;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFCTabelasVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFGruposConvenios;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio.JIFGruposConveniosVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.exame.JIFCClassesDeExames;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.exame.JIFCClassesDeExamesVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.exame.JIFCMaterial;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.exame.JIFCMaterialVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.exame.jIFCExames;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCEspecialidadesMedicas;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCEspecialidadesMedicasVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCMedicos;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCMedicosVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCPacientes;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCPacientesVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCResponsaveisTecnicos;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCResponsaveisTecnicosVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCUsuarios;
import br.bcn.admclin.interfacesGraficas.menu.cadastros.pessoal.JIFCUsuariosVisualizar;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresnotaipe.jIFAlterarValoresNotaIpe;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresrecebidosconvenio.jIFAlterarValoresDeConvenio;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.alterarvaloresrecebidosconvenio.jIFListaAlterarValoresDeConvenio;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.recebimentoConvenio.jIFRecebimentoDeConvenios;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.atendimentos.jIFFinanceiroAtendimentos;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.demed.jIFDemed;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio.jIFFaturarConvenios;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.faturarConvenio.jIFListaAtendimentosParaFaturar;
import br.bcn.admclin.interfacesGraficas.menu.financeiro.relatorios.valoresrecebidos.JIFrelatorioDeValoresRecebidosConvenio;

/**
 * 
 * @author CeSaR
 */
public class janelaPrincipal extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    
    //numero sequencial para os pdf do sistema
    private static Integer numeroSequencialDoSistemaParaPDF = 1;
    
    public static Integer getNumeroSequencialDoSistemaParaPDF() {
    	numeroSequencialDoSistemaParaPDF = numeroSequencialDoSistemaParaPDF + 1;
		return numeroSequencialDoSistemaParaPDF;
	}

    public static String cidadePadrao = "";
    public static String estadoPadrao = "";
    
	public static JjIFAguarde internalFrameAguarde;
    public static janelaPrincipal internalFrameJanelaPrincipal;
    public static JIFFeriadoVisualizar internalFrameFeriadoVisualizar;
    public static JIFFeriado internalFrameFeriado;
    public static jIFCExames internalFrameCadastroExames;
    // public static JIFCriarRelatorioDeAgendamentos internalFrameCriarRelatorioDeAgendamento;
    public static JIFCConveniosVisualizar internalFrameConvenioVisualizar;
    public static JIFCConvenios internalFrameConvenios;
    public static JIFCConvenioCH internalFrameConveioCH;
    public static JIFCConvenioFILME internalFrameConvenioFilme;
    public static JIFCTabelas internalFrameTabelas;
    public static JIFCTabelasVisualizar internalFrameTabelasVisualizar;
    public static JIFCTabelasAdicionarUmMaterialAUmExame internalFrameTabelasAdicionarUmMaterial;
    public static JIFCTabelasAdicionarUMExame internalFrameTabelasAdicionarUmExame;
    public static JIFCTabelasEditarCoeficientesDeUmExame internalFrameTabelasEditarCoefExame;
    public static JIFCTabelasAdicionarUmMaterialAUmExame internalFrameTabelasAdicionarMaterialAoExame;
    public static JIFCMaterialVisualizar internalFrameMateriaisVisualizar;
    public static JIFCMaterial internalFrameMateriais;
    public static JIFCPacientesVisualizar internalFramePacienteVisualizar;
    public static JIFCPacientes internalFramePaciente;
    public static JIFCMedicosVisualizar internalFrameMedicosVisualizar;
    public static JIFCMedicos internalFrameMedicos;
    public static JIFCPacientesAtendimentos internalFrameCadastroPacienteAtendimento;
    public static JIFListaAtendimentos internalFrameListaAtendimentos;
    public static JIFCClassesDeExames internalFrameClasseDeExames;
    public static JIFCClassesDeExamesVisualizar internalFrameClasseDeExamesVisualizar;
    public static JIFCUsuariosVisualizar internalFrameUsuariosVisualizar;
    public static JIFCUsuarios internalFrameUsuarios;
    public static JIFCEspecialidadesMedicasVisualizar internalFrameEspecialidadeMedicasVisualizar;
    public static JIFCEspecialidadesMedicas internalFrameEspecialidadeMedicas;
    public static JIFCResponsaveisTecnicos internalFrameResponsaveisTecnicos;
    public static JIFCResponsaveisTecnicosVisualizar internalFrameResponsaveisTecnicosVisualizar;
    public static ListaConvenios internalFrameListaConveniosConsultaValorDeExames;
    public static ConsultaValorExames internalFrameConsultaValorDeExames;
    public static jIFFaturarConvenios internalFrameFinanceiroRelatorioFaturarConvenios;
    public static jIFFinanceiroAtendimentos internalFrameFinanceiroRelatorioAtendimentos;
    public static jIFAlterarValorDeExame internalFrameAlterarValorDeExamesNoAtendimento;
    public static jIFDemed internalFrameDmed;
    public static JIFPesquisarAtendimentos internalFramePesquisarAtendimentos;
    public static JIFGruposConveniosVisualizar internalFrameGruposConveniosVisualizar;
    public static JIFGruposConvenios internalFrameGruposDeConvenios;
    public static JIFAtendimentoSelecionarUmPaciente internalFrameAtendimentoSelecionarUmPaciente;
    public static JIFAtendimentoSelecionarUmMedicoSolicitante internalFrameAtendimentoSelecionarUmMedicoSolicitante;
    public static JIFCMedicosAtendimentos internalFrameAtendimentoCadastroMedicos;
    public static jIFListaAtendimentosParaFaturar internalFrameAtendimentosParaFaturar;
    public static jIFRecebimentoDeConvenios internalFrameRecebimentoDeConvenios;
    public static JIFrelatorioDeValoresRecebidosConvenio internalFrameValoresRecebidos;
    public static JIFCadastroDeAtendimento internalFrameAtendimento;
    public static JIFCadastroAgendaVisualizar internalFrameCadastroAgendasVisualizar;
    public static JIFCadastroAgendaDesc internalFrameCadastroAgendasDesc;
    public static JIFListaAgendamentos internalFrameListaDeAgendamentos;
    public static JIFCadastroBloqueio internalFrameCadastroDeBloqueiosAgenda;
    public static JIFRelatorioDeCaixa internalFrameRelatorioDeCaixa;
    public static JIFRelatorioDeMovimentos internalFrameRelatorioDeMovimento;
    public static jIFAlterarValoresNotaIpe internalFrameAlterarValorDeExamesNotaIpe;
    public static jIFAlterarValoresDeConvenio internalFramaAlterarValoresDeConvenio;
    public static jIFListaAlterarValoresDeConvenio internalFrameListaAlterarValoresDeConvenio;

    // variavel que guarda o tipo de impressao da empresa
    public static int modeloDeImpressao = 0;
    Connection con = null;

    /**
     * Creates new form janelaPrincipal
     */
    public static boolean apagarPasta(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = apagarPasta(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }
    
    //metodo para pegar a fonte que sera utilizada no linux
    //se nao definirmos o linux nao tem a fonte tahoma e ae fica distorcida as janelas!
    public static Font getFonte( int tamanho ){  
        Font font = null;  
        try{  
            File file = new File("tahoma.ttf");  
            FileInputStream fis = new FileInputStream(file);  
            font = Font.createFont( Font.TRUETYPE_FONT , fis );  
        }catch( Exception e ){  
            System.out.println( e.getMessage() );  
        }
        font = font.deriveFont( Font.PLAIN , tamanho );  
        return font;  
    }
    
    public String codigoParaImpressoesLinux = "LINUX" + pegandoDataDoSistema();
    private void apagarArquivosDeImpressaoLinux(){
        String caminho;
        try {
            caminho = System.getProperty("user.dir");
            caminho = caminho + "/";
            File path = new File(caminho);
              
            File[] files = path.listFiles();  
            for (File file : files) {
                if(file.getName().toLowerCase().contains(codigoParaImpressoesLinux.toLowerCase())){
                    file.delete();
                }
            } 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao deletar Arquivo. Procure o Administrador!");
        }  
    }
    
    public String pegandoDataDoSistema() {
        // pegando data do sistema
        Calendar hoje = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String dataDeHoje = format.format(hoje.getTime());
        return dataDeHoje;
    }

    /*
     * TESTANDO PARA MOSTRAR AO THEO ESSE METODO FAZ ISSO ISSO E ISSO
     */
    public janelaPrincipal() {

        initComponents();
        

        // sobreescrevendo o metodo de fechar
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                // se for pra fechar ele apaga as pastas, se tiver pra nao fazer nada ele nao apaga
                File dir = new File(USUARIOS.pasta_raiz);
                apagarPasta(dir);
                if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
                    apagarArquivosDeImpressaoLinux();
                }
            }
        });

        setIconImage(getToolkit().createImage(getClass().getResource("/br/bcn/admclin/imagens/imagemIconePrograma.png")));

        this.setTitle("ADMClin - BCN Medical System");

        jDesktopPane1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // aqui vai o que quero que aconteça quando mecher no tamanho do jdesktoppane
                // pega todos os jinternalframes do jdesktoppane
                JInternalFrame[] iframes = jDesktopPane1.getAllFrames();
                // percorrendo todos os jinternalframes pegos
                for (JInternalFrame iframe : iframes) {
                    // se jinternalframe estiver aberto, vai ser centralizado
                    if (iframe.isVisible()) {
                        // centralizando jinternalframe

                        int lDesk = jDesktopPane1.getWidth();
                        int aDesk = jDesktopPane1.getHeight();
                        int lIFrame = iframe.getWidth();
                        int aIFrame = iframe.getHeight();

                        iframe.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
                    }
                }
            }
        });

        // abrindo login
        jIFLogin login = new jIFLogin();
        jDesktopPane1.add(login);
        login.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = login.getWidth();
        int aIFrame = login.getHeight();

        login.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

        setResizable(false);
        
      //definindo fonte correta caso seja linux
        if(!OSvalidator.isWindows() && !OSvalidator.isMac()){ 
            UIManager.put("Table.font", janelaPrincipal.getFonte(12));  
        }
    }

    public void desativandoOMenu() {
        jMCadastros.setEnabled(false);
        jMEntradaESaida.setEnabled(false);
        jMSair.setEnabled(false);
        jMFinanceiro.setEnabled(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void ativandoOMenu() {
        jMCadastros.setEnabled(true);
        jMEntradaESaida.setEnabled(true);
        jMSair.setEnabled(true);
        jMFinanceiro.setEnabled(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    /*
     * Ativando o ato de aguardar o carregamento
     */
    public void ativarCarregamento() {
        janelaPrincipal.internalFrameAguarde = new JjIFAguarde();
        jDesktopPane1.add(janelaPrincipal.internalFrameAguarde);
        janelaPrincipal.internalFrameAguarde.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = janelaPrincipal.internalFrameAguarde.getWidth();
        int aIFrame = janelaPrincipal.internalFrameAguarde.getHeight();

        janelaPrincipal.internalFrameAguarde.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

        janelaPrincipal.internalFrameJanelaPrincipal.setEnabled(false);
    }

    /*
     * Desativando o ato de aguardar o carregamento
     */
    public void desativarCarregamento() {
        janelaPrincipal.internalFrameAguarde.dispose();
        janelaPrincipal.internalFrameAguarde = null;
        janelaPrincipal.internalFrameJanelaPrincipal.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new ImagemNoJDesktopPane("/br/bcn/admclin/imagens/fundoJDesktop.jpg");
        jMenuBar1 = new javax.swing.JMenuBar();
        jMSair = new javax.swing.JMenu();
        jMCadastros = new javax.swing.JMenu();
        jMCAgenda = new javax.swing.JMenu();
        jMCAAgendas = new javax.swing.JMenuItem();
        jMCAFeriados = new javax.swing.JMenuItem();
        jMCConvenios = new javax.swing.JMenu();
        jMCCConvenios = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMCCTabelas = new javax.swing.JMenuItem();
        jMCExames = new javax.swing.JMenu();
        jMCEClassesDeExames = new javax.swing.JMenuItem();
        jMCEEXAMES = new javax.swing.JMenuItem();
        jMCEMateriais = new javax.swing.JMenuItem();
        jMCPessoal = new javax.swing.JMenu();
        jMICPEspecialidadesMedicas = new javax.swing.JMenuItem();
        jMICPMedicos = new javax.swing.JMenuItem();
        jMICPPacientes = new javax.swing.JMenuItem();
        jMICPResponsaveisTecnicos = new javax.swing.JMenuItem();
        jMICPUsuarios = new javax.swing.JMenuItem();
        jMEntradaESaida = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMFinanceiro = new javax.swing.JMenu();
        jMRelatorios = new javax.swing.JMenu();
        jMIAtendimentos = new javax.swing.JMenuItem();
        jMIDemed = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jDesktopPane1.setBackground(new java.awt.Color(240, 240, 240));

        jMSair.setIcon(new javax.swing.ImageIcon(getClass()
            .getResource("/br/bcn/admclin/imagens/imagemBotaoLogoff.png"))); // NOI18N
        jMSair.setEnabled(false);
        jMSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMSairMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMSair);

        jMCadastros.setText("Cadastros");
        jMCadastros.setEnabled(false);

        jMCAgenda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuAgenda.png"))); // NOI18N
        jMCAgenda.setText("Agendas");

        jMCAAgendas.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuAgendaAgendas.png"))); // NOI18N
        jMCAAgendas.setText("Agendas");
        jMCAAgendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCAAgendasActionPerformed(evt);
            }
        });
        jMCAgenda.add(jMCAAgendas);

        jMCAFeriados.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuAgendaFeriados.png"))); // NOI18N
        jMCAFeriados.setText("Feriados");
        jMCAFeriados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCAFeriadosActionPerformed(evt);
            }
        });
        
        
        JMenuItem jMCBloqueios = new JMenuItem("Bloqueios");
        jMCBloqueios.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		jMCadastroDeBloqueio();
        	}
        });
        jMCAgenda.add(jMCBloqueios);
        jMCAgenda.add(jMCAFeriados);

        jMCadastros.add(jMCAgenda);

        jMCConvenios.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuConvenios.png"))); // NOI18N
        jMCConvenios.setText("Convênios");

        jMCCConvenios.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuConveniosConvenios.png"))); // NOI18N
        jMCCConvenios.setText("Convênios");
        jMCCConvenios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCCConveniosActionPerformed(evt);
            }
        });
        jMCConvenios.add(jMCCConvenios);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/GrupoDeConvenios.png"))); // NOI18N
        jMenuItem3.setText("Grupos de Convênios");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMCConvenios.add(jMenuItem3);

        jMCCTabelas.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuConveniosTabelas.png"))); // NOI18N
        jMCCTabelas.setText("Tabelas");
        jMCCTabelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCCTabelasActionPerformed(evt);
            }
        });
        jMCConvenios.add(jMCCTabelas);

        jMCadastros.add(jMCConvenios);

        jMCExames.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuExames.png"))); // NOI18N
        jMCExames.setText("Exames");

        jMCEClassesDeExames.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuExamesClassesDeExames.png"))); // NOI18N
        jMCEClassesDeExames.setText("Classes de Exames");
        jMCEClassesDeExames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCEClassesDeExamesActionPerformed(evt);
            }
        });
        jMCExames.add(jMCEClassesDeExames);

        jMCEEXAMES.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuExamesExames.png"))); // NOI18N
        jMCEEXAMES.setText("Exames");
        jMCEEXAMES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCEEXAMESActionPerformed(evt);
            }
        });
        jMCExames.add(jMCEEXAMES);

        jMCEMateriais.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuExamesMateriais.png"))); // NOI18N
        jMCEMateriais.setText("Materiais");
        jMCEMateriais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMCEMateriaisActionPerformed(evt);
            }
        });
        jMCExames.add(jMCEMateriais);

        jMCadastros.add(jMCExames);

        jMCPessoal
            .setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/bcn/admclin/imagens/menuPessoal.png"))); // NOI18N
        jMCPessoal.setText("Pessoal");

        jMICPEspecialidadesMedicas.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuPessoalEspecialidadesMedicas.png"))); // NOI18N
        jMICPEspecialidadesMedicas.setText("Especialidades Médicas");
        jMICPEspecialidadesMedicas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICPEspecialidadesMedicasActionPerformed(evt);
            }
        });
        jMCPessoal.add(jMICPEspecialidadesMedicas);

        jMICPMedicos.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuPessoalMedicos.png"))); // NOI18N
        jMICPMedicos.setText("Médicos");
        jMICPMedicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICPMedicosActionPerformed(evt);
            }
        });
        jMCPessoal.add(jMICPMedicos);

        jMICPPacientes.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuPessoalPacientes.png"))); // NOI18N
        jMICPPacientes.setText("Pacientes");
        jMICPPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICPPacientesActionPerformed(evt);
            }
        });
        jMCPessoal.add(jMICPPacientes);

        jMICPResponsaveisTecnicos.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuPessoalResponsaveisTecnicos.png"))); // NOI18N
        jMICPResponsaveisTecnicos.setText("Responsáveis Técnicos");
        jMICPResponsaveisTecnicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICPResponsaveisTecnicosActionPerformed(evt);
            }
        });
        jMCPessoal.add(jMICPResponsaveisTecnicos);

        jMICPUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuPessoalUsuarios.png"))); // NOI18N
        jMICPUsuarios.setText("Usuários");
        jMICPUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMICPUsuariosActionPerformed(evt);
            }
        });
        jMCPessoal.add(jMICPUsuarios);

        jMCadastros.add(jMCPessoal);

        jMenuBar1.add(jMCadastros);

        jMEntradaESaida.setText("Atendimentos");
        jMEntradaESaida.setEnabled(false);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuAtendimentoAtendimento.png"))); // NOI18N
        jMenu1.setText("Atendimentos");
        
        JMenuItem jMEAgendamentos = new JMenuItem("Agendamentos");
        jMEAgendamentos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                jMCAAgendamentosctionPerformed();
            }
        });
        jMEAgendamentos.setIcon(new ImageIcon(janelaPrincipal.class.getResource("/br/bcn/admclin/imagens/laudoDigitado.png")));
        jMenu1.add(jMEAgendamentos);
        jMEFichasDeAtendimento = new javax.swing.JMenuItem();
        
                jMEFichasDeAtendimento.setIcon(new ImageIcon(janelaPrincipal.class.getResource("/br/bcn/admclin/imagens/imprimirFicha.png"))); // NOI18N
                jMEFichasDeAtendimento.setText("Fichas de Atendimento");
                jMEFichasDeAtendimento.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMEFichasDeAtendimentoActionPerformed(evt);
                    }
                });
                jMenu1.add(jMEFichasDeAtendimento);
        jMIEditarAtendimentos = new javax.swing.JMenuItem();
        
                jMIEditarAtendimentos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
                jMIEditarAtendimentos.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                    "/br/bcn/admclin/imagens/imagemPesquisarInvertida.png"))); // NOI18N
                jMIEditarAtendimentos.setText("Pesquisar Atendimentos");
                jMIEditarAtendimentos.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMIEditarAtendimentosActionPerformed(evt);
                    }
                });
                jMenu1.add(jMIEditarAtendimentos);
        
        JMenuItem jMERegistrarAtendimento = new JMenuItem("Registrar Atendimento");
        jMenu1.add(jMERegistrarAtendimento);
        jMERegistrarAtendimento.setIcon(new ImageIcon(janelaPrincipal.class.getResource("/br/bcn/admclin/imagens/menuAtendimentoAtendimento.png")));
        jMERegistrarAtendimento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                jMEAtendimentoSemAgendaActionPerformed(arg0);
            }
        });
        jMERegistrarAtendimento.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));

        jMEntradaESaida.add(jMenu1);
        
        JMenuItem mntmRelatrioDeCaixa = new JMenuItem("Relatório de Caixa");
        mntmRelatrioDeCaixa.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		fechandoTodosOsInternalFrames();

        		internalFrameRelatorioDeCaixa = new JIFRelatorioDeCaixa();
                jDesktopPane1.add(internalFrameRelatorioDeCaixa);
                internalFrameRelatorioDeCaixa.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = internalFrameRelatorioDeCaixa.getWidth();
                int aIFrame = internalFrameRelatorioDeCaixa.getHeight();

                internalFrameRelatorioDeCaixa.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
        	}
        });
        mntmRelatrioDeCaixa.setIcon(new ImageIcon(janelaPrincipal.class.getResource("/br/bcn/admclin/imagens/financeiroAtendimentos.png")));
        jMenu1.add(mntmRelatrioDeCaixa);
        
        mntmRelatrioDeMovimento = new JMenuItem("Relatório de Movimento");
        mntmRelatrioDeMovimento.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		fechandoTodosOsInternalFrames();

        		internalFrameRelatorioDeMovimento = new JIFRelatorioDeMovimentos();
                jDesktopPane1.add(internalFrameRelatorioDeMovimento);
                internalFrameRelatorioDeMovimento.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = internalFrameRelatorioDeMovimento.getWidth();
                int aIFrame = internalFrameRelatorioDeMovimento.getHeight();

                internalFrameRelatorioDeMovimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
        	}
        });
        jMenu1.add(mntmRelatrioDeMovimento);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuAtendimentosConsultaValoresDeExames.png"))); // NOI18N
        jMenuItem1.setText("Consultar Valores dos Exames");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMEntradaESaida.add(jMenuItem1);

        jMenuBar1.add(jMEntradaESaida);

        jMFinanceiro.setText("Financeiro");
        jMFinanceiro.setEnabled(false);

        jMRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/financeiroAtendimentos.png"))); // NOI18N
        jMRelatorios.setText("Relatórios");

        jMIAtendimentos.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/menuFinanceiroAtendimento.png"))); // NOI18N
        jMIAtendimentos.setText("Atendimentos");
        jMIAtendimentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIAtendimentosActionPerformed(evt);
            }
        });
        jMRelatorios.add(jMIAtendimentos);

        jMIDemed.setIcon(new javax.swing.ImageIcon(getClass()
            .getResource("/br/bcn/admclin/imagens/receita-federal.png"))); // NOI18N
        jMIDemed.setText("Dmed");
        jMIDemed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIDemedActionPerformed(evt);
            }
        });
        jMRelatorios.add(jMIDemed);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource(
            "/br/bcn/admclin/imagens/faturamentoDeConvenios.png"))); // NOI18N
        jMenuItem2.setText("Faturamento de Convênios");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMRelatorios.add(jMenuItem2);

        jMFinanceiro.add(jMRelatorios);
        
        JMenuItem mntmValoresRecebidos = new JMenuItem("Valores Recebidos");
        mntmValoresRecebidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fechandoTodosOsInternalFrames();

                internalFrameValoresRecebidos = new JIFrelatorioDeValoresRecebidosConvenio();
                jDesktopPane1.add(internalFrameValoresRecebidos);
                internalFrameValoresRecebidos.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = internalFrameValoresRecebidos.getWidth();
                int aIFrame = internalFrameValoresRecebidos.getHeight();

                internalFrameValoresRecebidos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
            }
        });
        
        JMenuItem jMIAlterarValoresNotaIPE = new JMenuItem("Alterar Valores Nota IPÊ");
        jMIAlterarValoresNotaIPE.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		fechandoTodosOsInternalFrames();

        		internalFrameAlterarValorDeExamesNotaIpe = new jIFAlterarValoresNotaIpe();
                jDesktopPane1.add(internalFrameAlterarValorDeExamesNotaIpe);
                internalFrameAlterarValorDeExamesNotaIpe.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = internalFrameAlterarValorDeExamesNotaIpe.getWidth();
                int aIFrame = internalFrameAlterarValorDeExamesNotaIpe.getHeight();

                internalFrameAlterarValorDeExamesNotaIpe.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
        	}
        });
        jMRelatorios.add(jMIAlterarValoresNotaIPE);
        
        jMIAlterarValoresDeConvenio = new JMenuItem("Alterar Valores de Convênio");
        jMIAlterarValoresDeConvenio.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		fechandoTodosOsInternalFrames();

        		internalFramaAlterarValoresDeConvenio = new jIFAlterarValoresDeConvenio();
                jDesktopPane1.add(internalFramaAlterarValoresDeConvenio);
                internalFramaAlterarValoresDeConvenio.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = internalFramaAlterarValoresDeConvenio.getWidth();
                int aIFrame = internalFramaAlterarValoresDeConvenio.getHeight();

                internalFramaAlterarValoresDeConvenio.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
        	}
        });
        jMRelatorios.add(jMIAlterarValoresDeConvenio);
        mntmValoresRecebidos.setIcon(new ImageIcon(janelaPrincipal.class.getResource("/br/bcn/admclin/imagens/menuValoresRecebidos.png")));
        jMRelatorios.add(mntmValoresRecebidos);

        jMenuBar1.add(jMFinanceiro);
        
        JMenuItem jMIRecebimentoDeConvenios = new JMenuItem("Recebimento de Convênios");
        jMIRecebimentoDeConvenios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                fechandoTodosOsInternalFrames();

                internalFrameRecebimentoDeConvenios = new jIFRecebimentoDeConvenios();
                jDesktopPane1.add(internalFrameRecebimentoDeConvenios);
                internalFrameRecebimentoDeConvenios.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = internalFrameRecebimentoDeConvenios.getWidth();
                int aIFrame = internalFrameRecebimentoDeConvenios.getHeight();

                internalFrameRecebimentoDeConvenios.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
            }
        });
        jMIRecebimentoDeConvenios.setIcon(new ImageIcon(janelaPrincipal.class.getResource("/br/bcn/admclin/imagens/menuRecebimentoDeConvenios.png")));
        jMFinanceiro.add(jMIRecebimentoDeConvenios);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
            jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE));

        setSize(new java.awt.Dimension(1024, 725));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void fechandoTodosOsInternalFrames() {
        // fechando os outros jinternalframes abertos antes de abrir este
        JInternalFrame[] iframes = jDesktopPane1.getAllFrames();
        // percorrendo todos os jinternalframes pegos

        for (JInternalFrame iframe : iframes) {
            // se tive aberto vo fecha
            if (iframe != null) {
                // fecha oi internal frame
                iframe.dispose();
                iframe = null;
            }
        }
    }
    
    private void  jMEAtendimentoSemAgendaActionPerformed(java.awt.event.ActionEvent evt){
        fechandoTodosOsInternalFrames();

        internalFrameAtendimento = new JIFCadastroDeAtendimento("livre", 0, false);
        jDesktopPane1.add(internalFrameAtendimento);
        internalFrameAtendimento.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameAtendimento.getWidth();
        int aIFrame = internalFrameAtendimento.getHeight();

        internalFrameAtendimento.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }

    private void jMCAAgendasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCAAgendasActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameCadastroAgendasVisualizar = new JIFCadastroAgendaVisualizar();
        jDesktopPane1.add(internalFrameCadastroAgendasVisualizar);
        internalFrameCadastroAgendasVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameCadastroAgendasVisualizar.getWidth();
        int aIFrame = internalFrameCadastroAgendasVisualizar.getHeight();

        internalFrameCadastroAgendasVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCAAgendasActionPerformed

    private void jMSairMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jMSairMouseClicked
        if (jMSair.isEnabled()) {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente Sair?", "ATENÇÃO", 0);
            if (resposta == JOptionPane.YES_OPTION) {

                jMCadastros.setVisible(true);
                jMFinanceiro.setVisible(true);
                jMEntradaESaida.setVisible(true);

                fechandoTodosOsInternalFrames();
                // sumindo o menu
                desativandoOMenu();
                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                // abrindo login
                jIFLogin login = new jIFLogin();
                jDesktopPane1.add(login);
                login.setVisible(true);
                int lDesk = jDesktopPane1.getWidth();
                int aDesk = jDesktopPane1.getHeight();
                int lIFrame = login.getWidth();
                int aIFrame = login.getHeight();

                login.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
            }

        }

    }// GEN-LAST:event_jMSairMouseClicked

    private void jMCAFeriadosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCAFeriadosActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameFeriadoVisualizar = new JIFFeriadoVisualizar();
        jDesktopPane1.add(internalFrameFeriadoVisualizar);
        internalFrameFeriadoVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameFeriadoVisualizar.getWidth();
        int aIFrame = internalFrameFeriadoVisualizar.getHeight();

        internalFrameFeriadoVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCAFeriadosActionPerformed

    private void jMCEEXAMESActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCEEXAMESActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameCadastroExames = new jIFCExames();
        jDesktopPane1.add(internalFrameCadastroExames);
        internalFrameCadastroExames.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameCadastroExames.getWidth();
        int aIFrame = internalFrameCadastroExames.getHeight();

        internalFrameCadastroExames.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCEEXAMESActionPerformed

    private void jMCCConveniosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCCConveniosActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameConvenioVisualizar = new JIFCConveniosVisualizar();
        jDesktopPane1.add(internalFrameConvenioVisualizar);
        internalFrameConvenioVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameConvenioVisualizar.getWidth();
        int aIFrame = internalFrameConvenioVisualizar.getHeight();

        internalFrameConvenioVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCCConveniosActionPerformed

    private void jMCCTabelasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCCTabelasActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameTabelasVisualizar = new JIFCTabelasVisualizar();
        jDesktopPane1.add(internalFrameTabelasVisualizar);
        internalFrameTabelasVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameTabelasVisualizar.getWidth();
        int aIFrame = internalFrameTabelasVisualizar.getHeight();

        internalFrameTabelasVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCCTabelasActionPerformed

    private void jMCEMateriaisActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCEMateriaisActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameMateriaisVisualizar = new JIFCMaterialVisualizar();
        jDesktopPane1.add(internalFrameMateriaisVisualizar);
        internalFrameMateriaisVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameMateriaisVisualizar.getWidth();
        int aIFrame = internalFrameMateriaisVisualizar.getHeight();

        internalFrameMateriaisVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCEMateriaisActionPerformed

    private void jMICPPacientesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMICPPacientesActionPerformed
        fechandoTodosOsInternalFrames();

        internalFramePacienteVisualizar = new JIFCPacientesVisualizar();
        jDesktopPane1.add(internalFramePacienteVisualizar);
        internalFramePacienteVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFramePacienteVisualizar.getWidth();
        int aIFrame = internalFramePacienteVisualizar.getHeight();

        internalFramePacienteVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMICPPacientesActionPerformed

    private void jMICPMedicosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMICPMedicosActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameMedicosVisualizar = new JIFCMedicosVisualizar();
        jDesktopPane1.add(internalFrameMedicosVisualizar);
        internalFrameMedicosVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameMedicosVisualizar.getWidth();
        int aIFrame = internalFrameMedicosVisualizar.getHeight();

        internalFrameMedicosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMICPMedicosActionPerformed

    private void jMEFichasDeAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMEFichasDeAtendimentoActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameListaAtendimentos = new JIFListaAtendimentos();
        jDesktopPane1.add(internalFrameListaAtendimentos);
        internalFrameListaAtendimentos.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameListaAtendimentos.getWidth();
        int aIFrame = internalFrameListaAtendimentos.getHeight();

        internalFrameListaAtendimentos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMEFichasDeAtendimentoActionPerformed

    private void jMCEClassesDeExamesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMCEClassesDeExamesActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameClasseDeExamesVisualizar = new JIFCClassesDeExamesVisualizar();
        jDesktopPane1.add(internalFrameClasseDeExamesVisualizar);
        internalFrameClasseDeExamesVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameClasseDeExamesVisualizar.getWidth();
        int aIFrame = internalFrameClasseDeExamesVisualizar.getHeight();

        internalFrameClasseDeExamesVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMCEClassesDeExamesActionPerformed

    private void jMICPUsuariosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMICPUsuariosActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameUsuariosVisualizar = new JIFCUsuariosVisualizar();
        jDesktopPane1.add(internalFrameUsuariosVisualizar);
        internalFrameUsuariosVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameUsuariosVisualizar.getWidth();
        int aIFrame = internalFrameUsuariosVisualizar.getHeight();

        internalFrameUsuariosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMICPUsuariosActionPerformed

    private void jMICPEspecialidadesMedicasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMICPEspecialidadesMedicasActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameEspecialidadeMedicasVisualizar = new JIFCEspecialidadesMedicasVisualizar();
        jDesktopPane1.add(internalFrameEspecialidadeMedicasVisualizar);
        internalFrameEspecialidadeMedicasVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameEspecialidadeMedicasVisualizar.getWidth();
        int aIFrame = internalFrameEspecialidadeMedicasVisualizar.getHeight();

        internalFrameEspecialidadeMedicasVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMICPEspecialidadesMedicasActionPerformed

    private void jMICPResponsaveisTecnicosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMICPResponsaveisTecnicosActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameResponsaveisTecnicosVisualizar = new JIFCResponsaveisTecnicosVisualizar();
        jDesktopPane1.add(internalFrameResponsaveisTecnicosVisualizar);
        internalFrameResponsaveisTecnicosVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameResponsaveisTecnicosVisualizar.getWidth();
        int aIFrame = internalFrameResponsaveisTecnicosVisualizar.getHeight();

        internalFrameResponsaveisTecnicosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMICPResponsaveisTecnicosActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameListaConveniosConsultaValorDeExames = new ListaConvenios();
        jDesktopPane1.add(internalFrameListaConveniosConsultaValorDeExames);
        internalFrameListaConveniosConsultaValorDeExames.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameListaConveniosConsultaValorDeExames.getWidth();
        int aIFrame = internalFrameListaConveniosConsultaValorDeExames.getHeight();

        internalFrameListaConveniosConsultaValorDeExames.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem2ActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameFinanceiroRelatorioFaturarConvenios = new jIFFaturarConvenios();
        jDesktopPane1.add(internalFrameFinanceiroRelatorioFaturarConvenios);
        internalFrameFinanceiroRelatorioFaturarConvenios.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameFinanceiroRelatorioFaturarConvenios.getWidth();
        int aIFrame = internalFrameFinanceiroRelatorioFaturarConvenios.getHeight();

        internalFrameFinanceiroRelatorioFaturarConvenios.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMIAtendimentosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMIAtendimentosActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameFinanceiroRelatorioAtendimentos = new jIFFinanceiroAtendimentos();
        jDesktopPane1.add(internalFrameFinanceiroRelatorioAtendimentos);
        internalFrameFinanceiroRelatorioAtendimentos.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameFinanceiroRelatorioAtendimentos.getWidth();
        int aIFrame = internalFrameFinanceiroRelatorioAtendimentos.getHeight();

        internalFrameFinanceiroRelatorioAtendimentos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMIAtendimentosActionPerformed

    private void jMIDemedActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMIDemedActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameDmed = new jIFDemed();
        jDesktopPane1.add(internalFrameDmed);
        internalFrameDmed.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameDmed.getWidth();
        int aIFrame = internalFrameDmed.getHeight();

        internalFrameDmed.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMIDemedActionPerformed

    private void jMIEditarAtendimentosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMIEditarAtendimentosActionPerformed

        fechandoTodosOsInternalFrames();

        internalFramePesquisarAtendimentos = new JIFPesquisarAtendimentos();

        jDesktopPane1.add(internalFramePesquisarAtendimentos);
        internalFramePesquisarAtendimentos.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFramePesquisarAtendimentos.getWidth();
        int aIFrame = internalFramePesquisarAtendimentos.getHeight();

        internalFramePesquisarAtendimentos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);

    }// GEN-LAST:event_jMIEditarAtendimentosActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed
        fechandoTodosOsInternalFrames();

        internalFrameGruposConveniosVisualizar = new JIFGruposConveniosVisualizar();

        jDesktopPane1.add(internalFrameGruposConveniosVisualizar);
        internalFrameGruposConveniosVisualizar.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameGruposConveniosVisualizar.getWidth();
        int aIFrame = internalFrameGruposConveniosVisualizar.getHeight();

        internalFrameGruposConveniosVisualizar.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }// GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMCAAgendamentosctionPerformed(){
        fechandoTodosOsInternalFrames();

        internalFrameListaDeAgendamentos = new JIFListaAgendamentos();

        jDesktopPane1.add(internalFrameListaDeAgendamentos);
        internalFrameListaDeAgendamentos.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameListaDeAgendamentos.getWidth();
        int aIFrame = internalFrameListaDeAgendamentos.getHeight();

        internalFrameListaDeAgendamentos.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }
    
    private void jMCadastroDeBloqueio(){
        fechandoTodosOsInternalFrames();

        internalFrameCadastroDeBloqueiosAgenda = new JIFCadastroBloqueio();

        jDesktopPane1.add(internalFrameCadastroDeBloqueiosAgenda);
        internalFrameCadastroDeBloqueiosAgenda.setVisible(true);
        int lDesk = jDesktopPane1.getWidth();
        int aDesk = jDesktopPane1.getHeight();
        int lIFrame = internalFrameCadastroDeBloqueiosAgenda.getWidth();
        int aIFrame = internalFrameCadastroDeBloqueiosAgenda.getHeight();

        internalFrameCadastroDeBloqueiosAgenda.setLocation(lDesk / 2 - lIFrame / 2, aDesk / 2 - aIFrame / 2);
    }
    /**
     * @param args
     *            the command line arguments
     */

    // variavel definida no OS para o IP do servidor...
    public static String RISIP = "";
    // variavel definida no OS para o caminho do banco de dados no servidor
    public static String RISDB = "";
    public static String PACDB = "";
    public static String RISDBMEDIPE = ""; 
    public static String NOMEPORTAL = "";
    
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(janelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(janelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(janelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(janelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE,
                null, ex);
        }
        // </editor-fold>

        /*
         * Create and display the form
         */

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                //definindo fonte correta caso seja linux
                if(!OSvalidator.isWindows() && !OSvalidator.isMac()){
                    UIManager.put("Label.font", janelaPrincipal.getFonte(12)); 
                    UIManager.put("TextField.font", janelaPrincipal.getFonte(12));  
                    UIManager.put("TextArea.font", janelaPrincipal.getFonte(12));  
                    UIManager.put("Button.font", janelaPrincipal.getFonte(12));  
                    UIManager.put("ComboBox.font", janelaPrincipal.getFonte(12));  
                }
                
                
                
                RISIP = System.getenv("RISIP");
                RISDB = System.getenv("RISDB");
                PACDB = System.getenv("PACDB");
                NOMEPORTAL = System.getenv("NOMEPORTAL");

                internalFrameJanelaPrincipal = new janelaPrincipal();

                internalFrameJanelaPrincipal.setVisible(true);
                // new janelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenuItem jMCAAgendas;
    private javax.swing.JMenuItem jMCAFeriados;
    private javax.swing.JMenu jMCAgenda;
    private javax.swing.JMenuItem jMCCConvenios;
    private javax.swing.JMenuItem jMCCTabelas;
    private javax.swing.JMenu jMCConvenios;
    private javax.swing.JMenuItem jMCEClassesDeExames;
    private javax.swing.JMenuItem jMCEEXAMES;
    private javax.swing.JMenuItem jMCEMateriais;
    private javax.swing.JMenu jMCExames;
    private javax.swing.JMenu jMCPessoal;
    public static javax.swing.JMenu jMCadastros;
    private javax.swing.JMenuItem jMEFichasDeAtendimento;
    public static javax.swing.JMenu jMEntradaESaida;
    public static javax.swing.JMenu jMFinanceiro;
    private javax.swing.JMenuItem jMIAtendimentos;
    private javax.swing.JMenuItem jMICPEspecialidadesMedicas;
    private javax.swing.JMenuItem jMICPMedicos;
    private javax.swing.JMenuItem jMICPPacientes;
    private javax.swing.JMenuItem jMICPResponsaveisTecnicos;
    private javax.swing.JMenuItem jMICPUsuarios;
    private javax.swing.JMenuItem jMIDemed;
    private javax.swing.JMenuItem jMIEditarAtendimentos;
    private javax.swing.JMenu jMRelatorios;
    public static javax.swing.JMenu jMSair;
    private javax.swing.JMenu jMenu1;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private JMenuItem mntmRelatrioDeMovimento;
    private JMenuItem jMIAlterarValoresDeConvenio;
}
