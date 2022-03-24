package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

@Stateless
public class UsuarioRepository extends AbstractBaseRepository{
	
	public void inserir(Usuario usuario) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		try (Connection c = this.abrirConexao()){
			
			int id = this.recuperarProximoValorDaSequence("seq_usuario");
			usuario.setId(id);
			
			PreparedStatement ps = c.prepareStatement("insert into usuario (id, nome) values (?, ?)");
			ps.setInt(1, usuario.getId());
			ps.setString(2, usuario.getNome());
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			
			throw new ErroAoConectarNaBaseException("Ocorreu um erro ao inserir usuario", e);
		};
	}
	
	public void atualizar(Usuario usuario) throws ErroAoConectarNaBaseException {
		
		try (Connection c = this.abrirConexao()){
			
			PreparedStatement ps = c.prepareStatement("update usuario set nome = ? where id = ?");
			ps.setString(1, usuario.getNome());
			ps.setInt(2, usuario.getId());
			ps.execute();
			ps.close();
			
		} catch (SQLException e){
			
			throw new ErroAoConectarNaBaseException("Ocorreu um erro ao atualizar usuario", e);
		}
	}
	
	public void remover(Usuario usuario) throws ErroAoConectarNaBaseException {
		
		try (Connection c = this.abrirConexao()){
			
			PreparedStatement ps = c.prepareStatement("delete from usuario where id = ?");
			ps.setInt(1, usuario.getId());
			ps.execute();
			ps.close();
			
		}catch(SQLException e) {
			
			throw new ErroAoConectarNaBaseException("Erro ao Remover usuario",  e);
		}
	}
	
	public Usuario consultar(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		try (Connection c = this.abrirConexao()){
		
			Usuario user = null;
			
			PreparedStatement ps = c.prepareStatement("select id, nome from usuario where id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				user = new Usuario();
				user.setId(rs.getInt("id"));
				user.setNome(rs.getString("nome"));
			}
			rs.close();
			ps.close();

			return user;
			
		} catch (SQLException e) {
			
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar usuario", e);
		}	
	}
	
	public List<Usuario> listarTodos() throws ErroAoConsultarBaseException, ErroAoConectarNaBaseException{
		
		try (Connection c = this.abrirConexao()){
		
			List<Usuario> users = new ArrayList<>();
			
			PreparedStatement ps = c.prepareStatement("select * from usuario");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Usuario user = new Usuario();
				user.setId(rs.getInt("id"));
				user.setNome(rs.getString("nome"));
				
				users.add(user);
			}
			rs.close();
			ps.close();
			
			return users;
			
		}catch (SQLException e) {
			
			throw new ErroAoConsultarBaseException("Erro ao listar todos os usuario", e);
		}
	}
}
