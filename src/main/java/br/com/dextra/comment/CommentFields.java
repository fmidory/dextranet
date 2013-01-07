package br.com.dextra.comment;

public enum CommentFields {

	TEXT("text"),AUTOR("autor"),DATE("date"),ID("id"),POSTID("postID");

	private String value;

	CommentFields(String value) {
		this.value = value;
	}

	public String getField() {
		return value;
	}
}