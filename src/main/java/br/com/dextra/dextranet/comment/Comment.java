package br.com.dextra.dextranet.comment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import br.com.dextra.dextranet.persistencia.Entidade;
import br.com.dextra.dextranet.utils.Converters;
import br.com.dextra.dextranet.utils.IndexFacade;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;

public class Comment extends Entidade {

	private String idReference;

	private boolean tree;

	public Comment(String text, String autor, String id, boolean arvore)
			throws FileNotFoundException, PolicyException, ScanException,
			IOException {
		super(autor, text);
		removeJS();
		this.idReference = id;
		this.tree = arvore;
	}

	public Comment(Entity commentEntity) {

		this.usuario = (String) commentEntity.getProperty(CommentFields.USUARIO
				.getField());
		this.conteudo = new Converters()
				.converterGAETextToString(commentEntity);
		this.id=(String) commentEntity.getProperty(CommentFields.ID.getField());

		this.dataDeCriacao=(String) commentEntity.getProperty(CommentFields.DATA_DE_CRIACAO.getField());

		this.comentarios=((Long)  commentEntity.getProperty(CommentFields.COMENTARIOS.getField())).intValue();
		this.likes=((Long)  commentEntity.getProperty(CommentFields.LIKES.getField())).intValue();
		this.idReference = (String) commentEntity
				.getProperty(CommentFields.ID_REFERENCE.getField());
		this.tree = new Converters().toBoolean(String.valueOf(commentEntity
				.getProperty(CommentFields.TREE.getField())));
	}

	public String getText() {
		return this.conteudo;
	}

	public String getIdReference() {
		return this.idReference;
	}

	public boolean isTree() {
		return this.tree;
	}

	public String getAutor() {
		return this.usuario;
	}

	public String getDataDeCriacao() {
		return this.dataDeCriacao;
	}

	private void removeJS() throws PolicyException, ScanException,
			FileNotFoundException, IOException {
		Properties properties = new Properties();

		properties.load(new FileInputStream(
				"src/main/resources/config.properties"));

		AntiSamy as = new AntiSamy();
		Policy policy = null;

		policy = Policy.getInstance(properties
				.getProperty("antisamy.policyXML"));

		this.conteudo = as.scan(this.conteudo, policy).getCleanHTML();

	}

	public Entity toEntity() {
		Entity entidade = new Entity(this.getKey());

		entidade.setProperty(CommentFields.ID.getField(), this.id);
		entidade.setProperty(CommentFields.ID_REFERENCE.getField(),
				this.idReference);
		entidade.setProperty(CommentFields.CONTEUDO.getField(), new Text(
				this.conteudo));
		entidade.setProperty(CommentFields.USUARIO.getField(), this.usuario);
		entidade.setProperty(CommentFields.COMENTARIOS.getField(),
				this.comentarios);
		entidade.setProperty(CommentFields.LIKES.getField(), this.likes);
		entidade.setProperty(CommentFields.DATA_DE_CRIACAO.getField(),
				this.dataDeCriacao);
		entidade.setProperty(CommentFields.TREE.getField(), this.tree);

		return entidade;
	}

	public Document toDocument() {

		Document document = Document.newBuilder().setId(id).addField(
				Field.newBuilder().setName(CommentFields.CONTEUDO.getField())
						.setText(this.conteudo)).addField(
				Field.newBuilder().setName(
						CommentFields.ID_REFERENCE.getField()).setText(
						this.idReference)).addField(
				Field.newBuilder().setName(CommentFields.TREE.getField())
						.setText(String.valueOf(this.tree))).addField(
				Field.newBuilder().setName(CommentFields.ID.getField())
						.setText(this.id)).build();

		IndexFacade.getIndex(Comment.class.getName()).add(document);
		return document;
	}

}