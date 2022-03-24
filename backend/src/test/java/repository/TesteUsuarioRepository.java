package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TesteUsuarioRepository {
	
	@EJB
	private UsuarioRepository usuarioRepository;

	@Before
	public void setUp() {
		
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
		//this.usuarioRepository = new UsuarioRepository();
	}
	
	@Test
	public void testa_se_usuario_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		Usuario user = new Usuario();
		user.setNome("Usuario Teste de Unidade");
		this.usuarioRepository.inserir(user);
		
		Usuario inserido = this.usuarioRepository.consultar(user.getId());
		
		assertThat(user.getId()).isGreaterThan(0);
		assertThat(inserido).isNotNull();
		assertThat(inserido.getId()).isEqualTo(user.getId());
		assertThat(inserido.getNome()).isEqualTo(user.getNome());
	}
	
	@Test
	public void testa_consultar_usuario() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		int idConsulta = 1;
		
		Usuario user = this.usuarioRepository.consultar(idConsulta);
		
		assertThat(user).isNotNull();
		assertThat(user.getNome()).isEqualTo("Hallan");
		assertThat(user.getId()).isEqualTo(idConsulta);
	}
	
	@Test
	public void testa_se_usuario_foi_atualizado() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		int idParaAtualizar = 1;
		String nomeParaAtualizar = "Felipe";
		
		Usuario user = new Usuario();
		user.setId(idParaAtualizar);
		user.setNome(nomeParaAtualizar);
		this.usuarioRepository.atualizar(user);
		
		Usuario usuarioAtualizado = this.usuarioRepository.consultar(user.getId());
		
		assertThat(usuarioAtualizado.getNome()).isEqualTo(user.getNome());
		
	}

	@Test
	public void testa_se_usuario_foi_removido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {
		
		int idParaRemover = 6;
		
		Usuario user = this.usuarioRepository.consultar(idParaRemover);
		
		assertThat(user).isNotNull();
		
		this.usuarioRepository.remover(user);
		
		Usuario usuarioRemovido = this.usuarioRepository.consultar(idParaRemover);
		
		assertThat(usuarioRemovido).isNull();
	}
	
	@Test
	public void testa_se_todos_os_usuarios_foram_listados() throws Exception {
		
		List<Usuario> usuarios = this.usuarioRepository.listarTodos();
		
		assertThat(usuarios).isNotNull()
						    .isNotEmpty()
						    .hasSize(3)
						    .extracting("nome")
						    .containsExactlyInAnyOrder("Hallan", "Brayan", "Vinicius");
	}
}
