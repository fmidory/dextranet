package br.com.dextra.dextranet.grupo;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import br.com.dextra.dextranet.usuario.Usuario;
import br.com.dextra.dextranet.usuario.UsuarioRepository;
import br.com.dextra.teste.TesteIntegracaoBase;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class GrupoRSTest extends TesteIntegracaoBase {
	private GrupoRepository repositorioGrupo = new GrupoRepository();
	private MembroRepository repositorioMembro = new MembroRepository();
	private UsuarioRepository usuarioRepository = new UsuarioRepository();
	private GrupoRS rest = new GrupoRSFake();

	private static final String USUARIO_LOGADO = "usuarioDextranet";

	@After
	public void removeDadosInseridos() {
		this.limpaGrupoInseridos(repositorioGrupo);
		this.limpaMembroInseridos(repositorioMembro);
	}

	@Test
	public void testeAdicionarGrupo() throws EntityNotFoundException {
		String nome = "Grupo A";
		String descricao = "Grupo teste";
		Usuario usuario = new Usuario("JoaoDextrano");
		usuario = usuarioRepository.persiste(usuario);
		UsuarioMembro uMembro = new UsuarioMembro(usuario.getId(), usuario.getNome());

		ArrayList<UsuarioMembro> uMembros = new ArrayList<UsuarioMembro>();
		uMembros.add(uMembro);
	//	Response response = rest.adicionar(nome, descricao, uMembros);

		//Assert.assertEquals(response.getStatus(), 200);
	}


	public void testeRemoverGrupo() {
		String nome = "Grupo A";
		String descricao = "Grupo teste";
		Usuario usuario = new Usuario("JoaoDextrano");
		usuario = usuarioRepository.persiste(usuario);
		GrupoRSFake grupoRS = new GrupoRSFake();
		Grupo grupo = new Grupo(nome, descricao, grupoRS.obtemUsuarioLogado());
		grupo = repositorioGrupo.persiste(grupo);
		Membro membro = repositorioMembro.persiste(new Membro(usuario.getId(), grupo.getId()));
		//TODO:CONSTRUCAO
	}

	public class GrupoRSFake extends GrupoRS {
		@Override
		protected String obtemUsuarioLogado() {
			return USUARIO_LOGADO;
		}
	}
}
