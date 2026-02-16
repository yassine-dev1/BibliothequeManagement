package com.UH2.FSTM.Management.Dto;


public class LivreDTO {
	
	private String _isbn ;
	private String _titre ;
	private String _categorie ;
	private String _image ;
	
	public LivreDTO(String _isbn, String _titre, String _categorie, String _image) {
		super();
		this._isbn = _isbn;
		this._titre = _titre;
		this._categorie = _categorie;
		this._image = _image;
	}

	public String getIsbn() {
		return _isbn;
	}

	public void setIsbn(String _isbn) {
		this._isbn = _isbn;
	}

	public String getTitre() {
		return _titre;
	}

	public void setTitre(String _titre) {
		this._titre = _titre;
	}

	public String getCategorie() {
		return _categorie;
	}

	public void setCategorie(String _categorie) {
		this._categorie = _categorie;
	}

	public String getImage() {
		return _image;
	}

	public void setImage(String _image) {
		this._image = _image;
	}
     
	
    
}
