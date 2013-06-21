package br.com.dextra.dextranet.grupo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.dextra.dextranet.grupo.UsuarioMembro;
import br.com.dextra.dextranet.rest.config.Application;
import br.com.dextra.dextranet.seguranca.AutenticacaoService;

import com.google.appengine.api.datastore.EntityNotFoundException;

@Path("/grupo")
public class GrupoRS {
	GrupoRepository repositorio = new GrupoRepository();
	MembroRepository repositorioMembro = new MembroRepository();

	@Path("/")
	@GET
	@Produces(Application.JSON_UTF8)
	public Response listar() throws EntityNotFoundException {
		List<Grupo> grupos = repositorio.lista();
		List<Grupo> gruposRetorno = new ArrayList<Grupo>();
		for (Grupo grupo : grupos) {
			Grupo grupoRetorno = new Grupo(grupo.getNome(), grupo.getDescricao(), grupo.getProprietario());
			grupoRetorno.setId(grupo.getId());
			grupoRetorno.setMembros(repositorioMembro.obtemPorIdGrupo(grupo.getId()));
			gruposRetorno.add(grupoRetorno);
		}

		return Response.ok().entity(gruposRetorno).build();
	}

	@Path("/")
	@PUT
	@Produces(Application.JSON_UTF8)
	@Consumes(Application.JSON_UTF8)
	public Response adicionar(GrupoJSON grupo) {
		System.out.println("Teste");
//		Grupo grupo = new Grupo(nome, descricao, obtemUsuarioLogado());
//		repositorio.persiste(grupo);

//		for (UsuarioMembro usuarioMembro : usuarios) {
//			Membro membro = new Membro(usuarioMembro.getId(), grupo.getId());
//			repositorioMembro.persiste(membro);
//		}

		return Response.ok().build();
	}

	@Path("/{id}")
	@PUT
	@Produces(Application.JSON_UTF8)
	public Response atualizar(@PathParam("id") String id, @FormParam("nome") String nome, @FormParam("descricao") String descricao) throws EntityNotFoundException {
		String usuarioLogado = obtemUsuarioLogado();
		Grupo grupo = repositorio.obtemPorId(id);
		if (usuarioLogado.equals(grupo.getProprietario())) {
			grupo = grupo.preenche(nome, descricao, usuarioLogado);
			repositorio.persiste(grupo);

//			adicionaNovosMembros(usuarios, grupo.getId());

			return Response.ok().entity(grupo).build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@Path("/{id}")
	@DELETE
	@Produces(Application.JSON_UTF8)
	public Response deletar(@PathParam("id") String id) throws EntityNotFoundException {
		String usuarioLogado = obtemUsuarioLogado();
		Grupo grupo = repositorio.obtemPorId(id);

		if (usuarioLogado.equals(grupo.getProprietario())) {
			List<Membro> obtemPorIdGrupo = repositorioMembro.obtemPorIdGrupo(id);
			for (Membro membro : obtemPorIdGrupo) {
				repositorioMembro.remove(membro.getId());
			}

			repositorio.remove(id);
			return Response.ok().build();
		} else {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	protected String obtemUsuarioLogado() {
		return AutenticacaoService.identificacaoDoUsuarioLogado();
	}

	private void adicionaNovosMembros(List<UsuarioMembro> usuarios, String idGrupo) throws EntityNotFoundException {
		for (UsuarioMembro usuario : usuarios) {
			Membro membroNovo;
			List<Membro> membros = repositorioMembro.obtemPorIdGrupo(idGrupo);
			for (Membro membro : membros) {
				if (!membro.getId().equals(usuario)) {
					membroNovo = new Membro(usuario.getId(), idGrupo);
					repositorioMembro.persiste(membroNovo);
				}
			}
		}
	}
}