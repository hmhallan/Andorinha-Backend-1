package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;

import javax.ejb.EJB;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import model.Tweet;
import model.Usuario;
import model.exceptions.ErroAoConectarNaBaseException;
import model.exceptions.ErroAoConsultarBaseException;
import runner.AndorinhaTestRunner;
import runner.DatabaseHelper;

@RunWith(AndorinhaTestRunner.class)
public class TesteTweetRepository {
	
	//private static final int ID_TWEET_CONSULTA = 1;
	private static final int ID_USUARIO_CONSULTA = 1;
	private static final long DELTA_MILIS = 500;
	
	@EJB
	private TweetRepository tweetRepository;
	@EJB
	private UsuarioRepository usuarioRepository;
	
	@Before
	public void setUp() {
		
		DatabaseHelper.getInstance("andorinhaDS").execute("dataset/andorinha.xml", DatabaseOperation.CLEAN_INSERT);
		
		//this.usuarioRepository = new UsuarioRepository();
		//this.tweetRepository = new TweetRepository();
	}
	
	@Test
	public void testa_se_tweet_foi_inserido() throws ErroAoConectarNaBaseException, ErroAoConsultarBaseException {

		Usuario user = this.usuarioRepository.consultar(ID_USUARIO_CONSULTA);
		String conteudoTweet = "Conteudo Teste 01";
		
		Tweet tweet = new Tweet();
		tweet.setConteudo(conteudoTweet);
		tweet.setUsuario(user);
		this.tweetRepository.inserir(tweet);
		
		
		assertThat(tweet.getId()).isGreaterThan(0);
		
		Tweet inserido = this.tweetRepository.consultar(tweet.getId());
		
		assertThat(inserido).isNotNull();
		assertThat(inserido.getConteudo()).isEqualTo(tweet.getConteudo());
		assertThat(Calendar.getInstance().getTime()).isCloseTo(inserido.getData().getTime(), DELTA_MILIS);
	}

}
