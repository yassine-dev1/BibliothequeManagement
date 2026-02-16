package com.UH2.FSTM.Management.Controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import com.UH2.FSTM.Management.Dao.EmpruntDao;
import com.UH2.FSTM.Management.Dao.LivreDao;
import com.UH2.FSTM.Management.Model.LivreModel;

@Named("LivreManagedBean")
@ViewScoped // Important pour garder l'état de la modal et du livre sélectionné
public class LivreManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<LivreModel> livres;
	private List<String> categoriesDisponibles;
    private LivreModel selectedLivre;
    
    private boolean showModal = false;
    private boolean editMode = false;
    private String searchCategory;

    @PostConstruct
    public void init() {
        loadLivres();
        loadCategories();
    }

    public void loadLivres() {
        livres = LivreDao.findAll();
    }
    
    public void loadCategories() {
        this.categoriesDisponibles = LivreDao.findAllCategories();
    }

    public void filterByCategory() {
        if (searchCategory != null && !searchCategory.isEmpty()) {
            livres = LivreDao.findByCategory(searchCategory);
        } else {
            loadLivres();
        }
    }

    //  ---  Préparer la creation  ---
    public void prepareCreate() {
        selectedLivre = new LivreModel();
        selectedLivre.setExemplaires(1); 
        selectedLivre.setDisponibles(1);
        editMode = false;
        showModal = true;
    }

    //  ---  Préparer la modification ---
    public void prepareEdit(LivreModel livre) {
        this.selectedLivre = livre;
        editMode = true;
        showModal = true;
    }

    // --- Sauvegarder --- 
    public void save() {
        boolean success;
        if (editMode) {
            success = LivreDao.update(selectedLivre);
        } else {
            success = LivreDao.create(selectedLivre);
        }

        if (success) {
            addMessage(FacesMessage.SEVERITY_INFO, "Succès", "Livre enregistré avec succès.");
            closeModal();
            init(); // reload Livres
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement.");
        }
        // return ;
    }

    // -- delete Livre ---
    public String deleteLivre(String isbn) {
    	
    	if(EmpruntDao.verifyBookEmprunt(isbn))
    	{
    		addMessage(FacesMessage.SEVERITY_ERROR , "error", "Suppression impossible : ce livre possède des emprunts actifs.");
    		return null ;
    	}
        if (LivreDao.delete(isbn)) {
            addMessage(FacesMessage.SEVERITY_INFO, "Supprimé", "Le livre a été retiré de la bibliothèque.");
            init();
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Impossible de supprimer ce livre.");
        }
        return null ;
    }

    public void closeModal() {
        showModal = false;
        selectedLivre = null;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // --- Getters et Setters ---
    public List<LivreModel> getLivres() { return livres; }
   
    public List<String> getCategoriesDisponibles() { return categoriesDisponibles; }
    
    public LivreModel getSelectedLivre() { return selectedLivre; }
    
    public void setSelectedLivre(LivreModel selectedLivre) { this.selectedLivre = selectedLivre; }
    
    public boolean isShowModal() { return showModal; }
    
    public boolean isEditMode() { return editMode; }
    
    public String getSearchCategory() { return searchCategory; }
    
    public void setSearchCategory(String searchCategory) { this.searchCategory = searchCategory; }
}