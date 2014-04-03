package br.bcn.admclin.interfacesGraficas.menu.atendimentos.agenda.novoregistrodeagendamento;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.interfacesGraficas.janelaPrincipal.janelaPrincipal;

public class RegistraAtendimento extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> jCBConvenio;
    private Connection con = null;
    private List<Double> listaPorcentagemPaciente = new ArrayList<Double>();
    private List<Double> listaPorcentagemConvenio = new ArrayList<Double>();
    private List<Integer> listaHandleConvenio = new ArrayList<Integer>();

    /**
     * Create the frame.
     */
    public RegistraAtendimento() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1915, 1073);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblConvnio = new JLabel("Convênio:");
        lblConvnio.setBounds(10, 11, 55, 14);
        contentPane.add(lblConvnio);
        
        jCBConvenio = new JComboBox<String>();
        jCBConvenio.setBounds(77, 8, 261, 20);
        contentPane.add(jCBConvenio);
        
        
        iniciarClasse();
    }
    
    private void iniciarClasse(){
        preencheConvenios();
    }
    
    private void preencheConvenios(){
        con = Conexao.fazConexao();
        ResultSet resultSet = CONVENIO.getConsultar(con);
        listaHandleConvenio.removeAll(listaHandleConvenio);
        listaPorcentagemConvenio.removeAll(listaPorcentagemConvenio);
        listaPorcentagemPaciente.removeAll(listaPorcentagemPaciente);

        jCBConvenio.addItem("Selecione um Convênio");
        listaHandleConvenio.add(0);
        listaPorcentagemConvenio.add(0.0);
        listaPorcentagemPaciente.add(0.0);
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("handle_convenio") > 0) {
                    jCBConvenio.addItem(resultSet.getString("nome"));
                    int handle_convenio = resultSet.getInt("handle_convenio");
                    listaHandleConvenio.add(handle_convenio);

                    double porcentagemPaciente = Double.valueOf(resultSet.getString("porcentPaciente"));
                    double porcentagemConvenio = Double.valueOf(resultSet.getString("porcentConvenio"));

                    listaPorcentagemConvenio.add(porcentagemConvenio);
                    listaPorcentagemPaciente.add(porcentagemPaciente);
                }

            }
        } catch (SQLException e) {
            jCBConvenio.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Não foi possivel preencher os Convênios. Procure o administrador.",
                "ERRO", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            Conexao.fechaConexao(con);
        }
    }
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        
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
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RegistraAtendimento frame = new RegistraAtendimento();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
