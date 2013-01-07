package br.com.dextra.dextranet.entidade;

import java.util.UUID;

import br.com.dextra.utils.Data;
import br.com.dextra.utils.JsonUtil;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Entidade {

	protected String id;
	protected String usuario;
	protected String conteudo;
	protected String dataDeCriacao;
	protected int comentarios;
	protected int likes;

	public Entidade(String usuario, String conteudo) {
		this.id = UUID.randomUUID().toString();
		this.conteudo = conteudo;
		this.usuario = usuario;
		this.dataDeCriacao = new Data().pegaData();
		this.comentarios = 0;
		this.likes = 0;
	}

	public Entidade() {

	}

	public String getId() {
		return id;
	}

	public String toJson() {
		return JsonUtil.stringify(this);
	}

	public Key getKey() {
		return KeyFactory.createKey(this.getClass().getName(), this.getId());
	}
}
