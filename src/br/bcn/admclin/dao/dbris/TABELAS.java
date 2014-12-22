/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.bcn.admclin.dao.dbris;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Tabelas;

/**
 *
 * @author Cesar Schutz
 */
public class TABELAS {
    
    public static boolean conseguiuConsulta;
    /**
     * Consulta Todos os Exames vinculados a uma determinada tabela
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarExamesDaTabela(Connection con, int handle_convenio, String modalidade){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select distinct e.nome, e.handle_exame, t.cofch1, t.cofch2, t.coeffilme, t.sinonimo, t.cod_exame from exames e " +
            "inner join tabelas t on e.handle_exame = t.handle_exame " +
            "inner join tb_classesexames c on e.handle_classedeexame = c.cod " +
            "inner join modalidades m on c.modidx = m.modidx " +
            "where t.handle_convenio = ? and m.modalidade = ? and e.flag_desativado != 1 and t.flag_desativado != 1");
        stmtQuery.setInt(1, handle_convenio);
        stmtQuery.setString(2, modalidade);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Exames da Tabela. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Consulta todos os dados de um exame de uma tabela, coeficientes e materiais
     * @param con
     * @param handle_convenio
     * @param handle_exame
     * @return 
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarDadosDeUmExame(Connection con, int handle_convenio, String handle_exame){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select a.handle_exame, a.coeffilme, a.cofch1, a.cofch2, a.handle_material, b.nome, a.qtdmaterial, a.tabelaId from tabelas as a inner join materiais as b on a.handle_material = b.handle_material where handle_convenio =" + handle_convenio + " and handle_exame ="+handle_exame);
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Dados do Exame. Procure o Administrador."+e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Cadastra um exame em uma tabela!
     * @param con
     * @param model
     * @return 
     */
    @SuppressWarnings("finally")
    public static boolean cadastrarExameAUmaTabela(Connection con, Tabelas model){
        boolean cadastro = false;
        String sqlInserirExameAUmaTabela = "insert into tabelas (usuarioid,dat, handle_convenio, handle_exame, coeffilme, cofch1, cofch2, handle_material, qtdmaterial, cod_exame, sinonimo, VAI_MATERIAIS_POR_PADRAO) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sqlInserirExameAUmaTabela);
            stmt.setInt(1, model.getUsuarioId());
            stmt.setDate(2, model.getDat());
            stmt.setInt(3, model.gethandle_convenio());
            stmt.setInt(4, model.gethandle_exame());
            stmt.setDouble(5, model.getCofFilme());
            stmt.setDouble(6, model.getCofCh1());
            stmt.setDouble(7, model.getCofCh2());
            stmt.setInt(8, model.gethandle_material());
            stmt.setInt(9, model.getQtdMaterial());
            stmt.setString(10, model.getCod_exame());
            stmt.setString(11, model.getSinonimo());
            stmt.setInt(12, model.getVAI_MATERIAIS_POR_PADRAO());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
            setapagarExamesComFlagDesativadoZero(con, String.valueOf(model.gethandle_exame()), String.valueOf(model.gethandle_convenio()));
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Exame na Tabela. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /*
     * metodo chamdo quando salvamos um exama na tabela
     * caso ele ja tenha existido e deletado ele vai estar com flag desativado 1
     * temos que apagar esse com flag 1 para que futuramente nao de conflito nos valores
     * ficaria 2 registros do mesmo exame sendo que o com flag desativo 1 eh antigo 
     * deve ser apagado
     */
    public static boolean setapagarExamesComFlagDesativadoZero(Connection con, String handle_exame, String handle_convenio){
        boolean deleto = false;
        String sql="delete from tabelas where flag_desativado = 1 and handle_exame=? and handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, handle_exame);
            stmt.setString(2, handle_convenio);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Exame da Tabela. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    /**Verifica se um material esta sendo utilizado em alguma tabela antes de apaga-lo
     * 
     * @param handle_material 
     */
    @SuppressWarnings("finally")
    public static boolean verificarSeMaterialEstaSendoUtilizado(Connection con, int handle_material){
        boolean utilizada = true;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from tabelas where handle_material=" + handle_material);
        ResultSet resultSet = stmtQuery.executeQuery();
        if(!resultSet.next()){
                utilizada = false;
        }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar se material está sendo utilizado. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return utilizada;
        }
    }
    

    /**
     * Deleta um Exame de uma tabela.
     * @param Connection
     * @param UsuarioModel 
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarUmExame(Connection con, String handle_exame, String handle_convenio){
        boolean deleto = false;
        String sql="update tabelas set flag_desativado = 1 where handle_exame=? and handle_convenio=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, handle_exame);
            stmt.setString(2, handle_convenio);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Exame da Tabela. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    /**
     * Deleta os materias de um exame que foi excluido das tabelas
     * @param Connection
     * @param UsuarioModel 
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarMateriasDeUmExame(Connection con, String handle_exame, String handle_convenio){
        boolean deleto = false;
        String sql="delete from tabelas where handle_exame=? and handle_convenio=? and handle_material != 0";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, handle_exame);
            stmt.setString(2, handle_convenio);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Materiais deste Exame","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    /**
     * Deleta um material de um Exame de uma tabela.
     * @param Connection
     * @param UsuarioModel 
     */
    @SuppressWarnings("finally")
    public static boolean setDeletarUmMaterialDeUmExame(Connection con, String handle_material, String tabeleId){
        boolean deleto = false;
        String sql="delete from tabelas where handle_material=? and tabelaId=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, handle_material);
            stmt.setString(2, tabeleId);
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Material do Exame. Procure o Administrador","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    /**
     * Atualiza os coeficientes de um exame!
     * @param Connection
     * @param ExameModel 
     * @return Boolean
     */
    @SuppressWarnings("finally")
    public static boolean setUpdateCoeficientesDeUmExame(Connection con, Tabelas exame){
        boolean cadastro = false;
        String sql = "update tabelas set usuarioid=?, dat=?, cofch1=?, cofch2=?, coeffilme=?, cod_exame=?, sinonimo=? where handle_convenio=? and handle_exame=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, exame.getUsuarioId());
            stmt.setDate(2, exame.getDat());
            stmt.setDouble(3, exame.getCofCh1());
            stmt.setDouble(4, exame.getCofCh2());
            stmt.setDouble(5, exame.getCofFilme());
            stmt.setString(6, exame.getCod_exame());
            stmt.setString(7, exame.getSinonimo());
            stmt.setInt(8, exame.gethandle_convenio());
            stmt.setInt(9, exame.gethandle_exame());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar coeficientes do Exame. Procure o administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Consulta as modalidade
     * @param Connection
     * @return ResultSet
     */
    @SuppressWarnings("finally")
    public static ResultSet getConsultarModalidades(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select modalidade from modalidades order by modalidade");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Modalidades. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            return resultSet;
        }
    }
    
    /*
     * Metodo que duplica uma tabela de convenio
     * recebe o id do convenio que será copiado (idConvenioAntigo) e o id do convenio que recebera essa tabela (idConvenioNovo)
     * Para isso o metodo deleta todos os exames do convenio que recebera a importação (isso para que nao fica exames duplciados)
     * apos isso ele cadastra todos os exames do convenio 'antigo' para o convenio 'novo'
     * 
     */
    public static boolean duplicarTabela(int idConvenioAntigo, int idConvenioNovo){
    	Connection con = Conexao.fazConexao();
    	try {    	
    		//retirando o autoCommit
			con.setAutoCommit(false);
			
			//deletando todos os exames do convenioNovo
			//PreparedStatement stmtDel = con.prepareStatement("delete from tabelas where handle_convenio = ?");
			//stmtDel.setInt(1, idConvenioNovo);
			//stmtDel.executeUpdate();
			//stmtDel.close();
			
			//consultando todos os exames da tabela do antigo convenio e colocando na lista
			ArrayList<Tabelas> listaDeExamesDaTabela = new ArrayList<>();
			PreparedStatement stmtSelect = con.prepareStatement("select * from tabelas where handle_convenio = ? and flag_desativado = 0");
			stmtSelect.setInt(1, idConvenioAntigo);
	        ResultSet resultSet = stmtSelect.executeQuery();
	        while(resultSet.next()){
	        	Tabelas tabela = new Tabelas();
	        	tabela.sethandle_convenio(idConvenioNovo);
	        	tabela.sethandle_exame(resultSet.getInt("handle_exame"));
	        	tabela.setCofFilme(resultSet.getDouble("coefFilme"));
	        	tabela.setCofCh1(resultSet.getDouble("cofCH1"));
	        	tabela.setCofCh2(resultSet.getDouble("cofCH2"));
	        	tabela.sethandle_material(resultSet.getInt("handle_material"));
	        	tabela.setQtdMaterial(resultSet.getInt("qtdMaterial"));
	        	tabela.setUsuarioId(USUARIOS.usrId);
	        	tabela.setDat(resultSet.getDate("dat"));
	        	tabela.setCod_exame(resultSet.getString("cod_exame"));
	        	tabela.setSinonimo(resultSet.getString("sinonimo"));
	        	tabela.setVAI_MATERIAIS_POR_PADRAO(resultSet.getInt("VAI_MATERIAIS_POR_PADRAO"));
	        	listaDeExamesDaTabela.add(tabela);
	        }
	        resultSet.close();
	        
	        for (Tabelas tabela : listaDeExamesDaTabela) {
	        	//salvando lista no convenio 'novo'
		        PreparedStatement smtmInsert = con.prepareStatement("insert into tabelas (usuarioid,dat, handle_convenio, handle_exame, coeffilme, cofch1, cofch2, handle_material, qtdmaterial, cod_exame, sinonimo, VAI_MATERIAIS_POR_PADRAO, flag_desativado) values(?,?,?,?,?,?,?,?,?,?,?,?,0)");
		        smtmInsert.setInt(1, tabela.getUsuarioId());
		        smtmInsert.setDate(2, tabela.getDat());
		        smtmInsert.setInt(3, tabela.gethandle_convenio());
		        smtmInsert.setInt(4, tabela.gethandle_exame());
		        smtmInsert.setDouble(5, tabela.getCofFilme());
		        smtmInsert.setDouble(6, tabela.getCofCh1());
		        smtmInsert.setDouble(7, tabela.getCofCh2());
		        smtmInsert.setInt(8, tabela.gethandle_material());
		        smtmInsert.setInt(9, tabela.getQtdMaterial());
		        smtmInsert.setString(10, tabela.getCod_exame());
		        smtmInsert.setString(11, tabela.getSinonimo());
		        smtmInsert.setInt(12, tabela.getVAI_MATERIAIS_POR_PADRAO());
		        smtmInsert.executeUpdate();
		        smtmInsert.close();
			}
	        
	        //comitando a transaçao
	        con.commit();
			Conexao.fechaConexao(con);
			return true;
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
			JOptionPane.showMessageDialog(null, "Erro ao duplicar Tabela de Convênio." + e);
			return false;
		}
    }

}
