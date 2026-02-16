package com.UH2.FSTM.Management.Controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.UH2.FSTM.Management.Dao.*;
import com.UH2.FSTM.Management.Model.EmpruntModel;
import com.UH2.FSTM.Management.Dto.*;

@Named("EmpruntManagedBean")
@ViewScoped
public class EmpruntManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<EmpruntModel> empruntsEnCours;
	private List<UserDTO> listeAdherents; // Supposons que vous avez UserDTO
	private List<LivreDTO> listeLivresDispo;

	private int selectedAdherentId;
	private String selectedIsbn;
	private boolean showModal = false;


    @PostConstruct
    public void init() {
        refreshList();
    }

    public void refreshList() {
        this.empruntsEnCours = EmpruntDao.getEmpruntsEnCours();
    }
    
              // --- prapare New emprunt ---
     public void prepareNouveauEmprunt() {
        this.selectedAdherentId = 0;
        this.selectedIsbn = null;
        this.showModal = true;
        this.listeAdherents   = UserDao.FindAllAherons() ;
        this.listeLivresDispo = LivreDao.findDispLivres() ;
    }
     
               // --- enregistre Emprunt ---
     public void enregistrerEmprunt() {
    	    if (EmpruntDao.emprunterLivre(selectedAdherentId, selectedIsbn)) {
    	        addMessage(FacesMessage.SEVERITY_INFO, "Succès", "Emprunt enregistré avec succès.");
    	        showModal = false;
    	        refreshList();
    	    } else {
    	        addMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Impossible de créer l'emprunt (vérifiez le stock).");
    	    }
    	}

             //--- Méthode pour enregistrer le retour d'un livre ---
    public void marquerCommeRendu(EmpruntModel emprunt) {
        boolean success = EmpruntDao.retournerLivre(emprunt.getIdEmprunt(), emprunt.getIsbn());
        
        if (success) {
            addMessage(FacesMessage.SEVERITY_INFO, "Succès", "Le livre '" + emprunt.getLivreTitre() + "' a été rendu.");
            refreshList(); // Actualise la Liste des emprunt
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Impossible d'enregistrer le retour.");
        }
    }

                //  --- vérifier le retard ---
    public boolean estEnRetard(Date dateRetourMax) {
        if (dateRetourMax == null) return false;
        return dateRetourMax.before(new Date()); // Compare avec la date d'aujourd'hui
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public List<EmpruntModel> getEmpruntsEnCours() { return empruntsEnCours; }
    public void setEmpruntsEnCours(List<EmpruntModel> empruntsEnCours) { this.empruntsEnCours = empruntsEnCours; }

	public List<UserDTO> getListeAdherents() {
		return listeAdherents;
	}

	public void setListeAdherents(List<UserDTO> listeAdherents) {
		this.listeAdherents = listeAdherents;
	}

	public List<LivreDTO> getListeLivresDispo() {
		return listeLivresDispo;
	}

	public void setListeLivresDispo(List<LivreDTO> listeLivresDispo) {
		this.listeLivresDispo = listeLivresDispo;
	}

	public int getSelectedAdherentId() {
		return selectedAdherentId;
	}

	public void setSelectedAdherentId(int selectedAdherentId) {
		this.selectedAdherentId = selectedAdherentId;
	}

	public String getSelectedIsbn() {
		return selectedIsbn;
	}

	public void setSelectedIsbn(String selectedIsbn) {
		this.selectedIsbn = selectedIsbn;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}
    
}