package br.com.dextra.dextranet.microblog;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dextra.dextranet.persistencia.EntidadeOrdenacao;
import br.com.dextra.dextranet.usuario.Usuario;
import br.com.dextra.dextranet.usuario.UsuarioRepository;
import br.com.dextra.dextranet.utils.TimeMachine;
import br.com.dextra.teste.TesteIntegracaoBase;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query.SortDirection;

public class MicroBlogRepositoryTest extends TesteIntegracaoBase {

    private final Usuario usuario = new Usuario("autor");
    public UsuarioRepository usuarioRepository = new UsuarioRepository();
    private MicroBlogRepository repository = new MicroBlogRepository();

    @Before
    public void criaUsuario() {
        usuarioRepository.persiste(usuario);
    }

    @After
    public void removeDados() throws EntityNotFoundException {
        this.limpaMicroPostsInseridos(repository);
        this.limpaUsuariosInseridos(usuarioRepository);
    }

    @Test
    public void removerMicroPost() {
        MicroPost micropost = new MicroPost("micromessage", usuario);
        repository.salvar(micropost);

        assertEquals(1, repository.buscarMicroPosts().size());

        repository.remove(micropost.getId());

        assertEquals(0, repository.buscarMicroPosts().size());
    }

    @Test
    public void micropostar() {
        MicroPost micropost = new MicroPost("micromessage", usuario);
        repository.salvar(micropost);

        assertEquals(1, repository.buscarMicroPosts().size());
        MicroPost post = repository.buscarMicroPosts().get(0);
        assertEquals("micromessage", post.getTexto());
        assertEquals("autor", post.getAutor().getUsername());
    }

    @Test
    public void testOrdemPosts() {
        TimeMachine timeMachine = new TimeMachine();
        Date hoje = timeMachine.inicioDoDia(new Date());
        Date diasAtras3 = timeMachine.diasParaAtras(hoje, 3);
        Date diasAtras10 = timeMachine.diasParaAtras(hoje, 10);

        MicroPost microPost1 = new MicroPost("micromessa1", usuario, diasAtras10);
        repository.salvar(microPost1);
        MicroPost microPost2 = new MicroPost("micromessa2", usuario, diasAtras3);
        repository.salvar(microPost2);
        MicroPost microPost3 = new MicroPost("micromessa3", usuario, hoje);
        repository.salvar(microPost3);

        EntidadeOrdenacao ordem = new EntidadeOrdenacao(MicroBlogFields.DATA.getField(), SortDirection.DESCENDING);
        List<MicroPost> buscarMicroPosts = repository.lista(ordem);
        assertEquals(3, buscarMicroPosts.size());
        assertEquals("micromessa3", buscarMicroPosts.get(0).getTexto());
        assertEquals("micromessa2", buscarMicroPosts.get(1).getTexto());
        assertEquals("micromessa1", buscarMicroPosts.get(2).getTexto());
    }
}
