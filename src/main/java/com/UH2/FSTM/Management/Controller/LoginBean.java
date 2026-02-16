package com.UH2.FSTM.Management.Controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
//import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.http.HttpSession;

import com.UH2.FSTM.Configuration.JWT.JWTUtil;
//import com.UH2.FSTM.Management.Dao.LoginDao;
import com.UH2.FSTM.Management.Dao.UserDao;
import com.UH2.FSTM.Management.Dto.LoginDTO;

@Named(value = "login")
@RequestScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;

   // @Inject
  //  private LoginDao _loginDao;
    
    private String _email;
    private String _password;
    
    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    public String loginAction() {

        if (_email == null || _password == null) {
            addMessage("Erreur", "Veuillez remplir tous les champs.");
            return null;
        }
        
        boolean validatePwd = false ;
        LoginDTO userDto = UserDao.findByEmail(0, _email) ;
        
        if(userDto != null )
        	validatePwd = BCrypt.checkpw(_password, userDto.getPassword()) ;
        
        if (userDto != null && validatePwd) {
        	
        	 // generate Token
            String token = JWTUtil.generateToken(userDto.get_username(), userDto.get_role());
            
            // Stockage du token en session
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                                               .getExternalContext()
                                               .getSession(true);
             
             session.setAttribute("AUTH_TOKEN", token);  
             
             // affichage token
            System.out.println("Token generé  : " + token);
            
            // Redirection vers l'accueil
            return "HomeAdmin/showEmprunts?faces-redirect=true";
        } else {
        	
            // Message d'erreur JSF
            addMessage("Échec", "Email ou mot de passe incorrect.");
            return null; // Reste sur la page de login
        }
    }


    public String getEmail() { return _email; }
    public void setEmail(String email) { this._email = email; }

    public String getPassword() { return _password; }
    public void setPassword(String password) { this._password = password; }
}