package br.com.dextra.dextranet.persistencia;

import br.com.dextra.dextranet.grupo.Grupo;
import br.com.dextra.dextranet.grupo.GrupoRepository;
import br.com.dextra.dextranet.grupo.Membro;
import br.com.dextra.dextranet.grupo.MembroRepository;
import br.com.dextra.dextranet.usuario.Usuario;
import br.com.dextra.dextranet.usuario.UsuarioRepository;

public class DadosUtils {
	private static GrupoRepository grupoRepositorio = new GrupoRepository();
	private static UsuarioRepository usuarioRepositorio = new UsuarioRepository();
	private static MembroRepository membroRepository = new MembroRepository();
	
	
	public static Grupo criaGrupoComOsIntegrantes(Boolean isInfra, String nomeDoGrupo,
			Boolean addProprietarioComoMembro, Usuario... integrantes) {
		Grupo novoGrupo = new Grupo(nomeDoGrupo, nomeDoGrupo, integrantes[0].getUsername());
		novoGrupo.setInfra(isInfra);
		novoGrupo = grupoRepositorio.persiste(novoGrupo);
	
		int cont = 0;
		for (Usuario integrante : integrantes) {
			if (!addProprietarioComoMembro && cont == 0) {
				cont++;
				continue;
			} else {
				membroRepository.persiste(new Membro(integrante.getId(), novoGrupo.getId(), integrante.getNome(),
						integrante.getUsername()));
				cont++;
			}
		}

		return novoGrupo;
	}

	public static Usuario criaUsuario(String username, Boolean isAtivo) {
		Usuario novoUsuario = new Usuario(username);
		novoUsuario.setAtivo(isAtivo);
		novoUsuario = usuarioRepositorio.persiste(novoUsuario);
		return novoUsuario;
	}
}
