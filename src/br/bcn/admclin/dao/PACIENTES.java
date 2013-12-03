package br.bcn.admclin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import br.bcn.admclin.dao.model.Pacientes;

/**
 *
 * @author BCN
 */
public class PACIENTES {
    public static boolean conseguiuConsulta;
    /**
     * Consulta todos os pacientes exitentes no Banco De Dados de acordo com o nome pesquisado
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultar(Connection con, String sql){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from pacientes where nome like ? order by nome");
            stmtQuery.setString(1, sql);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar Pacientes. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        } 
    }
    /**
     * Consulta os dados de um paciente exitentes no Banco De Dados.
     * @param Connection
     * @return ResultSet
     */
    public static ResultSet getConsultarDadosDeUmPaciente(Connection con, int handle_paciente){
        ResultSet resultSet = null;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from pacientes where handle_paciente = ?");
            stmtQuery.setInt(1, handle_paciente);
            resultSet = stmtQuery.executeQuery();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao consultar dados do Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return resultSet;
        } 
    }
    /**
     * Verifica se Paciente já existe antes de cadastra-lo no Banco de Dados.
     * @param Connection
     * @param PacienteMODEL
     * @return boolean
     */
    public static boolean getConsultarParaSalvarNovoRegistro(Connection con, Pacientes paciente){
        boolean existe = true;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from PACIENTES where nome=?");
            stmtQuery.setString(1, paciente.getCpf());
            ResultSet resultSet = stmtQuery.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }
            conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Paciente existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
           return existe; 
        }  
    }
    /**
     * Verifica se Paciente já existe antes de fazer um update no Banco de Dados.
     * @param Connection
     * @param PacienteMODEL
     * @return boolean
     */
    public static boolean getConsultarParaAtualizarRegistro(Connection con, Pacientes paciente){
        boolean existe = true;
        try{
            PreparedStatement pstmt = con.prepareStatement("select * from PACIENTES where (nome=?) and (handle_paciente!=?)");
            pstmt.setString(1, paciente.getCpf());
            pstmt.setInt(2, paciente.getHandle_paciente());
            ResultSet resultSet = pstmt.executeQuery();
            if(!resultSet.next()){
                existe = false;
            }        
            conseguiuConsulta = true;
        }catch(SQLException e){
            conseguiuConsulta = false;
            JOptionPane.showMessageDialog(null, "Erro ao consultar se Paciente existe. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return existe;
        }
    }
    /**
     * Salva um novo Paciente no Banco De Dados.
     * @param Connection
     * @param PacienteMODEL
     * @return Boolean
     */
    public static boolean setCadastrar(Connection con, Pacientes paciente){
        boolean cadastro = false;
        String sql = "insert into PACIENTES (usuarioid, dat, nome, cpf, nascimento, responsavel, cpfResponsavel, sexo, peso, altura, telefone, "
                + "celular, endereco, bairro, cep, cidade, uf, rg, profissao, email, cor, estadoCivil, obs, telefone_responsavel) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, paciente.getUsuarioId());
            stmt.setDate(2, paciente.getData());
            stmt.setString(3, paciente.getNome());
            stmt.setString(4, paciente.getCpf());
            stmt.setString(5, paciente.getNascimento());
            stmt.setString(6, paciente.getResponsavel());
            stmt.setString(7, paciente.getCpfResponsavel());
            stmt.setString(8, paciente.getSexo());
            stmt.setString(9, paciente.getPeso());
            stmt.setString(10, paciente.getAltura());
            stmt.setString(11, paciente.getTelefone());
            stmt.setString(12, paciente.getCelular());
            stmt.setString(13, paciente.getEndereco());
            stmt.setString(14, paciente.getBairro());
            stmt.setString(15, paciente.getCep());
            stmt.setString(16, paciente.getCidade());
            stmt.setString(17, paciente.getUf());
            stmt.setString(18, paciente.getRg());
            stmt.setString(19, paciente.getProfissao());
            stmt.setString(20, paciente.getEmail());
            stmt.setString(21, paciente.getCor());
            stmt.setString(22, paciente.getEstadoCivil());
            stmt.setString(23, paciente.getObs());
            stmt.setString(24, paciente.getTelefone_responsavel());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    /**
     * verificar handle_paciente de um paicente cadastrado no banco de dados
     * @param Connection
     * @param PacienteMODEL
     * @return boolean
     */
    public static int getConsultarHandlePaciente(Connection con, String nome){
        int handle_paciente = 0;
        try{
            PreparedStatement stmtQuery = con.prepareStatement("select * from PACIENTES where nome=?");
            stmtQuery.setString(1, nome);
            ResultSet resultSet = stmtQuery.executeQuery();
            if(resultSet.next()){
                handle_paciente = resultSet.getInt("handle_paciente");
            }
        }catch(SQLException e){
            handle_paciente = 0;
            JOptionPane.showMessageDialog(null, "Erro ao consultar handle_paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
           return handle_paciente; 
        }  
    }
    /**
     * Atualiza um Paciente no Banco De Dados.
     * @param Connection 
     * @param PacienteMODEL
     * @return Boolean
     */
    public static boolean setUpdate(Connection con, Pacientes paciente){
        boolean cadastro = false;
        String sql = "update PACIENTES set usuarioid=?, dat=?, nome=?, cpf=?, nascimento=?, responsavel=?, cpfResponsavel=?, sexo=?, peso=?, altura=?, telefone=?, "
                + "celular=?, endereco=?, bairro=?, cep=?, cidade=?, uf=?, rg=?, profissao=?, email=?, cor=?, estadoCivil=?, obs=?, telefone_responsavel=? where handle_paciente=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, paciente.getUsuarioId());
            stmt.setDate(2, paciente.getData());
            stmt.setString(3, paciente.getNome());
            stmt.setString(4, paciente.getCpf());
            stmt.setString(5, paciente.getNascimento());
            stmt.setString(6, paciente.getResponsavel());
            stmt.setString(7, paciente.getCpfResponsavel());
            stmt.setString(8, paciente.getSexo());
            stmt.setString(9, paciente.getPeso());
            stmt.setString(10, paciente.getAltura());
            stmt.setString(11, paciente.getTelefone());
            stmt.setString(12, paciente.getCelular());
            stmt.setString(13, paciente.getEndereco());
            stmt.setString(14, paciente.getBairro());
            stmt.setString(15, paciente.getCep());
            stmt.setString(16, paciente.getCidade());
            stmt.setString(17, paciente.getUf());
            stmt.setString(18, paciente.getRg());
            stmt.setString(19, paciente.getProfissao());
            stmt.setString(20, paciente.getEmail());
            stmt.setString(21, paciente.getCor());
            stmt.setString(22, paciente.getEstadoCivil());
            stmt.setString(23, paciente.getObs());
            stmt.setString(24, paciente.getTelefone_responsavel());
            stmt.setInt(25, paciente.getHandle_paciente());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Paciente. Procure o Administrador." + e,"ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
  //atualizar telefones do paciente
    public static boolean setUpdateTelefone(Connection con, Pacientes paciente){
        boolean cadastro = false;
        String sql = "update PACIENTES set usuarioid=?, dat=?, telefone=?, celular=? where handle_paciente=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, paciente.getUsuarioId());
            stmt.setDate(2, paciente.getData());
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getCelular());
            stmt.setInt(5, paciente.getHandle_paciente());
            stmt.executeUpdate();
            stmt.close();
            cadastro = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao atualizar telefones do Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return cadastro;
        }
    }
    
    /**
     * Deleta um Paciente no Banco de Dados.
     * @param Connection
     * @param PacienteMODEL 
     * @return Boolean
     */
    public static boolean setDeletar(Connection con, Pacientes paciente){
        boolean deleto = false;
        String sql = "delete from PACIENTES where handle_paciente=?";
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, paciente.getHandle_paciente());
            stmt.executeUpdate();
            stmt.close();
            deleto = true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Erro ao deletar Paciente. Procure o Administrador.","ERRO",javax.swing.JOptionPane.ERROR_MESSAGE);
        }finally{
            return deleto;
        }
    }
}
