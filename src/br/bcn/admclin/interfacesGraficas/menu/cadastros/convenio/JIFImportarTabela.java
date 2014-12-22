package br.bcn.admclin.interfacesGraficas.menu.cadastros.convenio;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import br.bcn.admclin.ClasseAuxiliares.OSvalidator;
import br.bcn.admclin.dao.dbris.CONVENIO;
import br.bcn.admclin.dao.dbris.Conexao;
import br.bcn.admclin.dao.dbris.TABELAS;
import br.bcn.admclin.dao.dbris.USUARIOS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JIFImportarTabela extends JFrame {

	private JPanel contentPane;
	private int handle_convenio;
	private ArrayList<Integer> listaHandleConvenio = new ArrayList<>();
	JComboBox<String> jCBConvenio;
	
	/**
	 * Create the frame.
	 */
	public JIFImportarTabela(int handle_convenio) {
		this.handle_convenio = handle_convenio;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 351, 141);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSelecioneOConvnio = new JLabel("Selecione o Convênio:");
		lblSelecioneOConvnio.setBounds(10, 11, 152, 14);
		contentPane.add(lblSelecioneOConvnio);
		
		jCBConvenio = new JComboBox();
		jCBConvenio.setBounds(10, 36, 311, 20);
		contentPane.add(jCBConvenio);
		
		JButton btnImportar = new JButton("Importar");
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				importarTabela();
			}
		});
		btnImportar.setBounds(10, 67, 311, 23);
		contentPane.add(btnImportar);
		preencherConvenios();

	}
	
	private void preencherConvenios(){
        Connection con = Conexao.fazConexao();
        ResultSet resultSet = CONVENIO.getConsultar(con);
        try {
            while (resultSet.next()) {
                // colocando dados na tabela
                if (resultSet.getInt("handle_convenio") > 0) {
                	listaHandleConvenio.add(Integer.valueOf(resultSet.getInt("handle_convenio")));
                	jCBConvenio.addItem(resultSet.getString("nome"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel atualizar a tabela. Procure o administrador", "ERRO",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        Conexao.fechaConexao(con);
	}
	
	private void importarTabela(){
		TABELAS.duplicarTabela(listaHandleConvenio.get(jCBConvenio.getSelectedIndex()), handle_convenio);
		dispose();
	}
}
