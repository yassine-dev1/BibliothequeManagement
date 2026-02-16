package com.UH2.FSTM.Management.Model;

import java.io.Serializable;
import java.util.Date;

public class EmpruntModel implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    private int idEmprunt;
    private String isbn;
    private int idUser;
    private Date dateSortie;
    private Date dateRetourMax;
    private Date dateRetourReelle;
    private String statut; 

    private String userName;   // Nom + Prénom de l'adhérent
    private String livreTitre; // Titre du livre emprunté


    public EmpruntModel() {
    }

    // --- Getters et Setters ---

    public int getIdEmprunt() { return idEmprunt; }
    public void setIdEmprunt(int idEmprunt) { this.idEmprunt = idEmprunt; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public Date getDateSortie() { return dateSortie; }
    public void setDateSortie(Date dateSortie) { this.dateSortie = dateSortie; }

    public Date getDateRetourMax() { return dateRetourMax; }
    public void setDateRetourMax(Date dateRetourMax) { this.dateRetourMax = dateRetourMax; }

    public Date getDateRetourReelle() { return dateRetourReelle; }
    public void setDateRetourReelle(Date dateRetourReelle) { this.dateRetourReelle = dateRetourReelle; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getLivreTitre() { return livreTitre; }
    public void setLivreTitre(String livreTitre) { this.livreTitre = livreTitre; }
}
