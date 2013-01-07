package br.com.dextra.dextranet.post;

import java.util.Date;

import br.com.dextra.dextranet.entidade.Entidade;
import br.com.dextra.persistencia.PostFields;

import com.google.appengine.api.datastore.Entity;

public class Post extends Entidade {

	private String titulo;

	private Date dataDeAtualizacao;

	private int comentario;

	private int likes;


	public Post(String titulo, String conteudo,String usuario) {
		this.geraId();
		this.titulo = titulo;
		this.conteudo = conteudo;
		this.dataDeCriacao = new Date();
		this.dataDeAtualizacao = this.dataDeCriacao;
		this.usuario = usuario;
		this.comentario = 0;
		this.likes=0;
	}

	public Post(Entity postEntity) {

		this.id = (String) postEntity.getProperty(PostFields.ID.getField());
		this.titulo = (String) postEntity.getProperty(PostFields.TITULO.getField());
		this.conteudo = (String) postEntity.getProperty(PostFields.CONTEUDO.getField());
		this.dataDeCriacao = (Date) postEntity.getProperty(PostFields.DATA.getField());
		this.dataDeAtualizacao = (Date) postEntity.getProperty(PostFields.DATA_DE_ATUALIZACAO.getField());
		this.usuario = (String) postEntity.getProperty(PostFields.USUARIO.getField());
		this.comentario = (Integer) postEntity.getProperty(PostFields.COMENTARIO.getField());
		this.likes = (Integer) postEntity.getProperty(PostFields.LIKES.getField());

	}

	public String getTitulo() {
		return titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public Date getDataDeCriacao() {
		return dataDeCriacao;
	}

	public Date getDataDeAtualizacao() {
		return dataDeAtualizacao;
	}

	public String getUsuario() {
		return usuario;
	}

	public int getComentarios() {
		return comentario;
	}

	public int getLikes() {
		return likes;
	}



}
