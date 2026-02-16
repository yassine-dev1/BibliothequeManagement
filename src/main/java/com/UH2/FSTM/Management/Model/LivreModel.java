package com.UH2.FSTM.Management.Model;


import java.util.Date ;

public class LivreModel {
	
	private String _isbn ;
	private String _titre ;
	private String _auteur ;
	private String _categorie ;
	private int _exemplaires ;
	private int _disponibles ;
	private String _image ;
	private Date _dateAjout ;
	
	public LivreModel() {} 
	
	public LivreModel(String _isbn, String _titre, String _auteur, int _exemplaires, int _disponibles, String _image,
			Date _dateAjout) {
		super();
		this._isbn = _isbn;
		this._titre = _titre;
		this._auteur = _auteur;
		this._exemplaires = _exemplaires;
		this._disponibles = _disponibles;
		this._image = _image;
		this._dateAjout = _dateAjout;
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

	public String getAuteur() {
		return _auteur;
	}

	public void setAuteur(String _auteur) {
		this._auteur = _auteur;
	}

	public int getExemplaires() {
		return _exemplaires;
	}

	public void setExemplaires(int _exemplaires) {
		this._exemplaires = _exemplaires;
	}

	public int getDisponibles() {
		return _disponibles;
	}

	public void setDisponibles(int _disponibles) {
		this._disponibles = _disponibles;
	}

	public String getImage() {
		return _image;
	}

	public void setImage(String _image) {
		this._image = _image;
	}

	public Date getDateAjout() {
		return _dateAjout;
	}

	public void setDateAjout(Date _dateAjout) {
		this._dateAjout = _dateAjout;
	}
	
	public String getCategorie()
	{
		return _categorie ;
	}
	
	public void setCategorie(String cat)
	{
		_categorie = cat ;
	}
	
	
	

}
