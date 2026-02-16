package com.UH2.FSTM.Management.Controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import com.UH2.FSTM.Management.Dao.EmpruntDao;
import com.UH2.FSTM.Management.Dao.UserDao;
import com.UH2.FSTM.Management.Dto.UserDTO;
import com.UH2.FSTM.Management.Dto.LoginDTO;
import com.UH2.FSTM.Management.Model.User;

@Named(value = "userBean")
@ViewScoped // Important pour garder l'état lors de l'édition/suppression
public class UserManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<UserDTO> _users;
    private User _selectedUser = new User();
    private boolean _editMode = false;
    private boolean _showModal = false; // Contrôle l'affichage
    
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
        _users = UserDao.getAll(); 
    }

    public void prepareCreate() {
        _selectedUser = new User();
        _editMode = false;
        _showModal = true;
    }

    public void prepareEdit(UserDTO user) {
       _selectedUser.setEmail(user.getEmail());
       _selectedUser.setFirstname(user.getFirstName());
       _selectedUser.setLastname(user.getLastName());
       _selectedUser.setId(user.getUserID());
       _editMode = true;
       _showModal = true;
    }

    public String deleteUser(int id) {
    	
    	if (EmpruntDao.verifyAdheronEmprunt(id) )
    	{
    		addMessage("echec", "Suppression impossible : cet adhérent possède des emprunts actifs.");
            return null  ;
    	}
    	UserDao.deleteById(id);
        init(); 
       return null; 
    }
    
    public void save()
    {
		LoginDTO v_existUserWithEmail ;
		UserDTO v_existUserWithUName ;
		String v_email = _selectedUser.getEmail() ;
		String v_userName = _selectedUser.getUsername() ;
		int v_id = _selectedUser.getId() ;
		System.out.print(v_id);
		v_existUserWithEmail = UserDao.findByEmail(v_id , v_email );
		v_existUserWithUName = UserDao.findByUserName(v_id, v_userName) ;
		
		//check already exist email and username in DB
		if( v_existUserWithEmail != null || v_existUserWithUName != null  )
		{
			addMessage("echec" , "email or username déja exist");
            return ;
		}
		
		if(!_editMode)
		{
			UserDao.create(_selectedUser);
			addMessage("Succès" , "creation est bien fais");
		}else
		{
			UserDao.update(_selectedUser);
			addMessage("Succès" , "edit est bien fais");
		}
		
    	closeModal();
    	init();
    	return ;
    }
    public void closeModal() { _showModal = false; }

    // --- Getters ---
    
    public List<UserDTO> getUsers() {
        return _users;
    }

	public User getSelectedUser() {
		return _selectedUser;
	}

	public void setSelectedUser(User _selectedUser) {
		this._selectedUser = _selectedUser;
	}

	public boolean getEditMode() {
		return _editMode;
	}

	public void setEditMode(boolean _editMode) {
		this._editMode = _editMode;
	}

	public boolean getShowModal() {
		return _showModal;
	}

	public void setShowModal(boolean _showModal) {
		this._showModal = _showModal;
	}
}