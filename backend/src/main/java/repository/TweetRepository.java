package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;

@Stateless
public class TweetRepository extends AbstractBaseRepository{
	
	public void inserir(Tweet tweet) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException{
		
		try (Connection c = this.abrirConexao()){
			
			int id = this.recuperarProximoValorDaSequence("seq_sequence");
			tweet.setId(id);
			
			Calendar dataHoje = Calendar.getInstance();
			
			PreparedStatement ps = c.prepareStatement("insert into tweet (id, conteudo, id_usuario, data_postagem) values (?,?,?,?)");
			ps.setInt(1, tweet.getId());
			ps.setString(2, tweet.getConteudo());
			ps.setInt(3, tweet.getUsuario().getId());
			ps.setTimestamp(4, new Timestamp(dataHoje.getTimeInMillis()));
			ps.execute();
			ps.close();
			
		}catch (SQLException e) {
			throw new ErroAoConectarNaBaseException("Erro ao inserir um tweet", e);
		}
		
	}
	
	public void atualizar() {
		
	}
	
	public void remover() {
		
	}
	
	public Tweet consultar(int id) throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		try (Connection c = this.abrirConexao()) {
			
			Tweet tweet = null;
			
			StringBuilder sql = new StringBuilder();
			sql.append("select t.id, t.conteudo, t.id_usuario, t.data_postagem, u.nome from tweet t");
			sql.append("join usuario u on t.id_usuario = u.id");
			sql.append("where t.id = ?");
			
			PreparedStatement ps = c.prepareStatement(sql.toString());
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				tweet = new Tweet();
				tweet.setId(rs.getInt("id"));
				tweet.setConteudo(rs.getString("conteudo"));

				Calendar data = new GregorianCalendar();
				data.setTime(rs.getTimestamp("data_postagem"));
				tweet.setData(data);
				
				Usuario user = new Usuario();
				user.setId(rs.getInt("id_usuario"));
				user.setNome(rs.getString("nome"));
				tweet.setUsuario(user);
			}
			rs.close();
			ps.close();
			
			return tweet;
			
		}catch (SQLException e) {
			throw new ErroAoConsultarBaseException("Ocorreu um erro ao consultar tweet", e);
		}
	}
	
	public List<Tweet> listarTodos(){
		
		return null;
	}

}
