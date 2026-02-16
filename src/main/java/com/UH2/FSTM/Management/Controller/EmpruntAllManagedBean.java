package com.UH2.FSTM.Management.Controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.UH2.FSTM.Management.Dao.EmpruntDao;
import com.UH2.FSTM.Management.Model.EmpruntModel;

@Named("empruntAllBean")
@ViewScoped
public class EmpruntAllManagedBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<EmpruntModel> _emprunts;
    private Integer _idAdheron = null;
    private String _fullNameAdherent = null ;
    
    private void chargeEmprunts(Integer idUser)
    {
    	 if(_idAdheron != null)
            _emprunts = EmpruntDao.getAllEmpruntsByUserId(_idAdheron);
    	 else
    		 _emprunts = EmpruntDao.getAllEmprunts() ;     
    }
    
    private void addMessage(String summary, String detail )  {
    	
    	if(summary.equals("echec"))
    		FacesContext.getCurrentInstance().addMessage(null, 
    				new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    	else
    		FacesContext.getCurrentInstance().addMessage(null, 
    				new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }	

    @PostConstruct
    public void init() {

        //  --- Récupérer paramètre IdAdheron depuis URL ---
    	
        Map<String, String> params =
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getRequestParameterMap();

        String idParam = params.get("idAdheron");
        _fullNameAdherent = params.get("nameAdherant");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                _idAdheron = Integer.parseInt(idParam);
                chargeEmprunts(_idAdheron) ; // All Emprunts
                
            } catch (NumberFormatException e) {
            	_idAdheron = null ;
            	chargeEmprunts(null) ;
            }
        } else {
        	   _idAdheron = null ;
        	   chargeEmprunts(null) ; // All Emprunts
        }   
    }

    // 🔹 Supprimer un emprunt
    public String deleteEmprunt(int id) throws SQLException {

    try
    {
        boolean deleted = EmpruntDao.deleteEmpruntById(id);

        if (deleted) {

        	addMessage("success" , "Emprunt supprimé avec succès");

            // Rafraîchir liste
            chargeEmprunts(_idAdheron) ;
            
        } else {
        	
        	addMessage("echec" , "Erreur lors de la suppression" );
        }
    }catch(Exception e)
    {
    	e.printStackTrace();
    }
        return null ;
    }
    
    
    public boolean estEnRetard(Date dateRetourMax) {
        if (dateRetourMax == null) return false;
        return dateRetourMax.before(new Date()); // Compare avec la date d'aujourd'hui
    }

    public List<EmpruntModel> getEmprunts() {
        return _emprunts;
    }

    public Integer getIdAdheron() {
        return _idAdheron;
    }
    
    public String getFullNameAdherent()
    {
    	return _fullNameAdherent ;
    }
}

