package com.UH2.FSTM.Management.Dto;

public class LoginDTO {

	 private String  _username ;
	 private String _email ;
	 private String _password ;
	 private String _role ;
	 private int _id ;
	   
	 public LoginDTO(int id , String username, String email  ,String role , String password ) {
		this._id = id ;
		this._username = username;
		this._email = email ;
		this._password = password ;
		this._role = role;
	}

	 public int getId()
	 {
		 return _id ;
	 }
	 
	 public void setId(int id)
	 {
		 this._id = id ;
	 }
	 
	 public String get_username() {
		 return _username;
	 }

	 public void set_username(String _username) {
		 this._username = _username;
	 }

	 public String get_role() {
		 return _role;
	 }

	 public void set_role(String _role) {
		 this._role = _role;
	 }
	 
	 public String getEmail()
	 {
		 return _email ;
	 }
	 
	 public void setEmail(String p_email)
	 {
		 _email = p_email ;
	 }
	 
	 public String getPassword()
	 {
		 return _password ;
	 }
	 
	 public void setPassword(String p_pwd)
	 {
		 _password = p_pwd ;
	 }

	 
	 
}
