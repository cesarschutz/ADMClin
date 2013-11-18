package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.model.Materiais;
import br.bcn.admclin.model.ValoresMateriais;

/**
 * Classe DAO da tabela MATERIAIS.
 * @author BCN
 */
public class MATERIAIS {
    public static boolean conseguiuConsulta;
    /**
     * Verifica se Material já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param materialMODEL
     * @return boolean
     */
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Materiais model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from materiais where nome=? or codigo=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setString(2, model.getCodigo());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Material já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Verifica se Material já existe antes de atualizar o banco de dados
     * @param Connection
     * @param materialMODEL
     * @return boolean
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Materiais model){
       boolean existe = true;
        try{
          PreparedStatement stmtQuery = con.prepareStatement("select * from materiais where (nome=? or codigo=?) and handle_material!=?");
          stmtQuery.setString(1, model.getNome());
          stmtQuery.setString(2, model.getCodigo());
          stmtQuery.setInt(3, model.getHandle_material());
          ResultSet resultSet = stmtQuery.executeQuery();
          if(!resultSet.next()){
                existe = false;
            }
          conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            existe = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Material já existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Consulta Todos os Materiais existentes no Banco de Dados.
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con){
        ResultSet resultSet = null;
        try{
        PreparedStatement stmtQuery = con.prepareStatement("select * from materiais order by nome");
        resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Materiais. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        }
    }
    /**
     * Cadastra um novo Material no Banco de Dados e em seguida cadastro um valor para aquele material (primeiro valor no caso).
     * @param Connection 
     * @param Materiais
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, Materiais model, ValoresMateriais valorModel){
        boolean cadastro = false;
        int handle_material = -1;
        String sqlInsertMaterial = "insert into materiais (nome,codigo,usuarioid,dat) values(?,?,?,?)";
        String sqlConsultaId = "select * from materiais where nome=?";
        String sqlInsrtValor = "insert into valoresmateriais (handle_material,valor,dataavaler,dat,usuarioid) values(?,?,?,?,?)";
        try{
            //inserindo material
            PreparedStatement stmtInsertMaterial = con.prepareStatement(sqlInsertMaterial);
            stmtInsertMaterial.setString(1, model.getNome());
            stmtInsertMaterial.setString(2, model.getCodigo());
            stmtInsertMaterial.setInt(3, USUARIOS.usrId);
            stmtInsertMaterial.setDate(4, model.getData());
            stmtInsertMaterial.executeUpdate();
            stmtInsertMaterial.close();
            //verificando o id do material cadastrado
            PreparedStatement stmtConsulta = con.prepareStatement(sqlConsultaId);
            stmtConsulta.setString(1, model.getNome());
            ResultSet resultSet = stmtConsulta.executeQuery();
            while(resultSet.next()){
                handle_material = resultSet.getInt("handle_material");
            }
            stmtConsulta.close();
            //inserindo valor no material
            PreparedStatement stmtInsertValor = con.prepareStatement(sqlInsrtValor);
            stmtInsertValor.setInt(1, handle_material);
            stmtInsertValor.setDouble(2, Double.valueOf(valorModel.getValor()));
            stmtInsertValor.setDate(3, valorModel.getDataAValer());
            stmtInsertValor.setDate(4, model.getData());
            stmtInsertValor.setInt(5, USUARIOS.usrId);
            
            
            stmtInsertValor.executeUpdate();
            stmtInsertValor.close();           
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Material. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    /**
     * Deleta uma Materiais no Banco de Dados.
     * @param Connection
     * @param Materiais 
     * @return Boolean
     */
    public static boolean setDeletar(Connection con, Materiais model){
        boolean deleto = false;
        String sql="delete from materiais where handle_material=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, model.getHandle_material());
            stmt.executeUpdate();
            stmt.close();    
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Material. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
    
    /**
     * Atualiza um Exame no Banco De Dados.
     * @param Connection
     * @param Materiais 
     * @return Boolean
     */
    public static boolean setUpdate(Connection con, Materiais material){
        boolean cadastro = false;
        String sql = "update materiais set usuarioid=?, dat=?, nome=?, codigo=? where handle_material=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, USUARIOS.usrId);
            stmt.setDate(2, material.getData());
            stmt.setString(3, material.getNome());
            stmt.setString(4, material.getCodigo());
            stmt.setInt(5, material.getHandle_material());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Material. Procure o administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    
}
