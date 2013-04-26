package br.com.dextra.dextranet.conteudo.post.comentario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.dextra.dextranet.conteudo.Conteudo;
import br.com.dextra.dextranet.utils.ConteudoHTML;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class Comentario extends Conteudo {

	private String postId;

	public Comentario(String postId, String username, String conteudo) {
		super(username);
		this.postId = postId;
		this.conteudo = new ConteudoHTML(conteudo).removeJavaScript();
	}

	@SuppressWarnings("unchecked")
	public Comentario(Entity entity) {
		super((String) entity.getProperty(ComentarioFields.usuario.name()));
		this.id = (String) entity.getProperty(ComentarioFields.id.name());
		this.conteudo = ( (Text) entity.getProperty(ComentarioFields.conteudo.name()) ).getValue();
		this.quantidadeDeCurtidas = (Long) entity.getProperty(ComentarioFields.quantidadeDeCurtidas.name());
		this.usuariosQueCurtiram = (List<String>) entity.getProperty(ComentarioFields.usuariosQueCurtiram.name());
		this.dataDeCriacao = (Date) entity.getProperty(ComentarioFields.dataDeCriacao.name());
		this.postId = (String) entity.getProperty(ComentarioFields.postId.name());

		if (this.usuariosQueCurtiram == null) {
			this.usuariosQueCurtiram = new ArrayList<String>();
		}
	}

	public String getPostId() {
		return postId;
	}

	@Override
	public Entity toEntity() {
		Entity entidade = new Entity(this.getKey(this.getClass()));

		entidade.setProperty(ComentarioFields.id.name(), this.id);
		entidade.setProperty(ComentarioFields.conteudo.name(), new Text(this.conteudo));
		entidade.setProperty(ComentarioFields.quantidadeDeCurtidas.name(), this.quantidadeDeCurtidas);
		entidade.setProperty(ComentarioFields.usuariosQueCurtiram.name(), this.usuariosQueCurtiram);
		entidade.setProperty(ComentarioFields.usuario.name(), this.usuario);
		entidade.setProperty(ComentarioFields.usuarioMD5.name(), this.usuarioMD5);
		entidade.setProperty(ComentarioFields.dataDeCriacao.name(), this.dataDeCriacao);
		entidade.setProperty(ComentarioFields.postId.name(), this.postId);

		return entidade;
	}

}
