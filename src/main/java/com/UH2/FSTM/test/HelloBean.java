package com.UH2.FSTM.test;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "helloBean")
@RequestScoped
public class HelloBean implements Serializable {
	
    private static final long serialVersionUID = 1L;
    private String nom; // La donnée saisie par l'utilisateur
    private String message;

    public void direBonjour() {
        this.message = "Salut, " + nom + " ! Bienvenue sur JSF.";
    }

    // Getters et Setters (indispensables pour que JSF accède aux données)
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getMessage() { return message; }
}
