package com.UH2.FSTM.Management.Dto;

public class UserDTO {
	
	private String firstName ;
	private String lastName ;
	private String email ;
	private String UserName ;
	private int UserID ;
	
	
	public UserDTO() {}

	public UserDTO(String firstName, String lastName, String email, String username, int userID) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.UserID = userID;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getUserName() {
		return UserName;
	}



	public void setUserName(String username) {
		UserName = username;
	}



	public int getUserID() {
		return UserID;
	}



	public void setUserID(int userID) {
		UserID = userID;
	}



	@Override
	public String toString() {
		return "UserDTO [email=" + email + ", Username=" + UserName + ", UserID=" + UserID + "]";
	}
	
	
	

}
