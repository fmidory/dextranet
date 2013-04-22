package br.com.dextra.dextranet.post;

import java.text.ParseException;

import br.com.dextra.dextranet.persistencia.Conteudo;
import br.com.dextra.dextranet.persistencia.ConteudoIndexavel;
import br.com.dextra.dextranet.post.comment.Comment;
import br.com.dextra.dextranet.utils.Converters;
import br.com.dextra.dextranet.utils.Data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.gson.JsonObject;

public class Post extends Conteudo implements ConteudoIndexavel {

	private String titulo;

	private String dataDeAtualizacao;

	private PostRepository postRepository;

	public Post(String titulo, String conteudo, String usuario) {
		this(titulo, conteudo, usuario, new Data().pegaData());
	}

	public Post(String titulo, String conteudo, String usuario, String dataDeAtualizacao) {
		super(usuario, conteudo);
		this.titulo = titulo;
		this.dataDeAtualizacao = dataDeAtualizacao;
		this.comentarios = 0;
		this.likes = 0;
		this.postRepository = new PostRepository();
	}

	public Post(Entity postEntity) {
		this.id = (String) postEntity.getProperty(PostFields.ID.getField());
		this.titulo = (String) postEntity.getProperty(PostFields.TITULO.getField());
		this.conteudo = Converters.converterGAETextToString(postEntity);
		this.dataDeCriacao = (String) postEntity.getProperty(PostFields.DATA.getField());
		this.dataDeAtualizacao = (String) postEntity.getProperty(PostFields.DATA_DE_ATUALIZACAO.getField());
		this.usuario = (String) postEntity.getProperty(PostFields.USUARIO.getField());
		this.comentarios = ((Long) postEntity.getProperty(PostFields.COMENTARIO.getField())).intValue();
		this.likes = ((Long) postEntity.getProperty(PostFields.LIKES.getField())).intValue();
		this.userLikes = (String) postEntity.getProperty(PostFields.USER_LIKE.getField());
		this.postRepository = new PostRepository();
	}

	public String getTitulo() {
		return this.titulo;
	}

	public String getConteudo() {
		return this.conteudo;
	}

	public String getDataDeCriacao() {
		return this.dataDeCriacao;
	}

	public String getDataDeAtualizacao() {
		return this.dataDeAtualizacao;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public int getComentarios() {
		return this.comentarios;
	}

	public int getLikes() {
		return this.likes;
	}

	public String getUserLikes() {
		return this.userLikes;
	}

	public void comentar(Comment comment) throws EntityNotFoundException, ParseException {

		postRepository.alteraEntity(comment);

		this.comentarios++;
		this.dataDeAtualizacao = comment.getDataDeCriacao();

	}

	protected void atualizaConteudoDepoisDaCurtida(String usuario) throws EntityNotFoundException {
		this.dataDeAtualizacao = new Data().pegaData();
		new PostRepository().registrarCurtida(this, usuario);
	}

	@Override
	public Entity toEntity() {
		Entity entidade = new Entity(this.getKey(this.getClass()));

		entidade.setProperty(PostFields.ID.getField(), this.id);
		entidade.setProperty(PostFields.TITULO.getField(), this.titulo);
		entidade.setProperty(PostFields.CONTEUDO.getField(), new Text(this.conteudo));
		entidade.setProperty(PostFields.USUARIO.getField(), this.usuario);
		entidade.setProperty(PostFields.COMENTARIO.getField(), this.comentarios);
		entidade.setProperty(PostFields.LIKES.getField(), this.likes);
		entidade.setProperty(PostFields.DATA.getField(), this.dataDeCriacao);
		entidade.setProperty(PostFields.DATA_DE_ATUALIZACAO.getField(), this.dataDeAtualizacao);
		entidade.setProperty(PostFields.USER_LIKE.getField(), this.userLikes);

		return entidade;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty(PostFields.ID.getField(), this.id);
		json.addProperty(PostFields.TITULO.getField(), this.titulo);
		json.addProperty(PostFields.CONTEUDO.getField(), this.conteudo);
		json.addProperty(PostFields.USUARIO.getField(), this.usuario);
		json.addProperty(PostFields.COMENTARIO.getField(), this.comentarios);
		json.addProperty(PostFields.LIKES.getField(), this.likes);
		json.addProperty(PostFields.DATA.getField(), this.dataDeCriacao);
		json.addProperty(PostFields.DATA_DE_ATUALIZACAO.getField(), this.dataDeAtualizacao);
		json.addProperty(PostFields.USER_LIKE.getField(), this.userLikes);

		return json;
	}

	public Document toDocument() {
		Document document = Document
				.newBuilder()
				.setId(id)
				.addField(Field.newBuilder().setName(PostFields.TITULO.getField()).setText(titulo))
				.addField(Field.newBuilder().setName(PostFields.CONTEUDO.getField()).setHTML(conteudo))
				.addField(Field.newBuilder().setName(PostFields.USUARIO.getField()).setText(usuario))
				.addField(
						Field.newBuilder().setName(PostFields.DATA_DE_ATUALIZACAO.getField())
								.setText(dataDeAtualizacao))
				.addField(Field.newBuilder().setName(PostFields.ID.getField()).setText(id)).build();

		return document;
	}

	@Override
	protected void atualizaConteudoDepoisDaDescurtida(String login) throws EntityNotFoundException {
		postRepository.registrarDescurtida(this, login);
	}

}
