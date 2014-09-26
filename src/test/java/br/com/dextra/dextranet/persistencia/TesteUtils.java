package br.com.dextra.dextranet.persistencia;

import gapi.GoogleAPI;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import br.com.dextra.dextranet.grupo.Grupo;
import br.com.dextra.dextranet.grupo.GrupoRepository;
import br.com.dextra.dextranet.grupo.Membro;
import br.com.dextra.dextranet.grupo.MembroRepository;
import br.com.dextra.dextranet.grupo.ServicoGrupo;
import br.com.dextra.dextranet.grupo.ServicoGrupoRepository;
import br.com.dextra.dextranet.grupo.servico.Servico;
import br.com.dextra.dextranet.grupo.servico.ServicoRepository;
import br.com.dextra.dextranet.grupo.servico.google.Aprovisionamento;
import br.com.dextra.dextranet.usuario.Usuario;
import br.com.dextra.dextranet.usuario.UsuarioRepository;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.admin.directory.model.Group;
import com.google.api.services.admin.directory.model.Member;

public class TesteUtils {
	private static GrupoRepository grupoRepositorio = new GrupoRepository();
	private static UsuarioRepository usuarioRepositorio = new UsuarioRepository();
	private static MembroRepository membroRepository = new MembroRepository();
	private static ServicoRepository servicoRepository = new ServicoRepository();
	private static ServicoGrupoRepository servicoGrupoRepository = new ServicoGrupoRepository();
	private static Aprovisionamento aprovisionamento = null;
	
	private static Servico getServico() {
		List<Servico> servicos = servicoRepository.lista();
		if (servicos.size() == 0) {
			Servico servico = new Servico("Grupo Email");
			servico = servicoRepository.persiste(servico);
			return servico;
		}
		return servicos.get(0);
	}

	public static Aprovisionamento getAprovisionamento() {
		if (aprovisionamento == null) {
			return new Aprovisionamento();
		}
		
		return aprovisionamento;
	}

	public static Grupo criaGrupoComOsIntegrantes(String emailGrupo,
			Boolean isInfra, String nomeDoGrupo,
			Boolean addProprietarioComoMembro, Usuario... integrantes) {
		Grupo novoGrupo = new Grupo(nomeDoGrupo, nomeDoGrupo,
				integrantes[0].getUsername());
		novoGrupo.setInfra(isInfra);
		novoGrupo = grupoRepositorio.persiste(novoGrupo);

		Servico servico = getServico();
		ServicoGrupo servicoGrupo = new ServicoGrupo(servico.getId(),
				novoGrupo.getId(), emailGrupo);
		servicoGrupo = servicoGrupoRepository.persiste(servicoGrupo);

		novoGrupo.setServicoGrupos(Arrays.asList(servicoGrupo));

		int cont = 0;
		for (Usuario integrante : integrantes) {
			if (!addProprietarioComoMembro && cont == 0) {
				cont++;
				continue;
			} else {
				membroRepository.persiste(new Membro(integrante.getId(),
						novoGrupo.getId(), integrante.getNome(), integrante
								.getUsername()));
				cont++;
			}
		}

		return novoGrupo;
	}

	public static void removerUsuario(String username) {
		usuarioRepositorio.remove(getUsuario(username).getId());
	}
	
	public static Usuario getUsuario(String username) {
		return usuarioRepositorio.obtemPorUsername(username);
	}
	
	public static Usuario criaUsuario(String username, Boolean isAtivo) {
		Usuario novoUsuario = new Usuario(username);
		novoUsuario.setAtivo(isAtivo);
		novoUsuario = usuarioRepositorio.persiste(novoUsuario);
		return novoUsuario;
	}

	public static void removerGrupoGoogle(String emailGrupo) throws IOException,
			GeneralSecurityException, URISyntaxException {
		try {
			getAprovisionamento().googleAPI().group().delete(emailGrupo);
		} catch (GoogleJsonResponseException e) {
		}
	}
	
	public static Group criarGrupoGoogle(String emailGrupo)
			throws IOException, GeneralSecurityException, URISyntaxException {
		String nomeGrupo = "Grupo 1";
		String descricaoGrupo = "Grupo descrição";
		
		GoogleAPI googleAPI = getAprovisionamento().googleAPI();
		Group group = new Group();
		group.setName(nomeGrupo);
		group.setEmail(emailGrupo);
		group.setDescription(descricaoGrupo);
		Group groupRetorno = googleAPI.group().create(group);
		return groupRetorno;
	}
	
	public static void adicionarMembroGrupoGoogle(List<String> emailMembros, String emailGrupo) throws IOException, GeneralSecurityException, URISyntaxException {
		GoogleAPI googleAPI = getAprovisionamento().googleAPI();
		Group group = googleAPI.group().getGroup(emailGrupo);
		
		for (String email : emailMembros) {
			Member membro = new Member();
			membro.setEmail(email);
			googleAPI.group().addMemberGroup(group, membro);
		}
	}
}
