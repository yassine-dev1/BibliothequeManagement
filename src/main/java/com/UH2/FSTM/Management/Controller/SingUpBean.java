package com.UH2.FSTM.Management.Controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;

import com.UH2.FSTM.Management.Dao.UserDao;
import com.UH2.FSTM.Management.Model.User;

@Named(value = "singUp")
@RequestScoped
public class SingUpBean implements Serializable { 
    private static final long serialVersionUID = 1L;

    private User _user = new User(); // Initialisation pour la formulaire
    private String _cPassword;
    private String _messageErreur;

    public String registration() { // Changement en String pour la navigation
        
        if (_user != null) {
        	
            // Comparaison correcte des mots de passe
            if (_user.getPassword() != null && _user.getPassword().equals(_cPassword)) {
            	
            	if( UserDao.findByEmail(-1 , _user.getEmail()) != null )
            	{
            	  _messageErreur = "email est déja exist !!"; 
            	  return null ;
	            }
            	
            	if(UserDao.findByUserName(-1 , _user.getUsername()) != null)
            	{
            	  _messageErreur = "Nom d'utilisateur est déja exist !!"; 
            	  return null ;
	            }  
            	
            	//System.out.print(_user.getEmail() + _user.getUsername());
        	    
            	// create New Adheron
                if (UserDao.create(_user)) {
                	
                    // Redirection avec paramètre email
                    return "login?faces-redirect=true&email=" + _user.getEmail();
                } else {
                    _messageErreur = "erreur lors de la creation !!" ;
                }
                
            } else {
                _messageErreur = "mot de passe et la Confirmmation mot de passe est non comptaible !!";
            }
        }
        
        // Rester sur la même page en cas d'erreur
        return null;
    }

    public User getUser() { return _user; }
    public void setUser(User user) { this._user = user; }

    public String getcPassword() { return _cPassword; }
    public void setcPassword(String cPassword) { this._cPassword = cPassword; }

    public String getMessageErreur() { return _messageErreur; }
    public void setMessageErreur(String messageErreur) { this._messageErreur = messageErreur; }
}