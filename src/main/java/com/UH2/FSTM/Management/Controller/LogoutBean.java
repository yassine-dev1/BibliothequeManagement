package com.UH2.FSTM.Management.Controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "logout")
@RequestScoped
public class LogoutBean implements Serializable{
	private final static long serialVersionUID = 1l ;
	
	public String logout()
	{
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces-redirect=true" ;
	}

}
