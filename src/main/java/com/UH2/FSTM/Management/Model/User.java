package com.UH2.FSTM.Management.Model;

public class User {
	private int Id ;
	private String Firstname ;
	private String Lastname ;
	private String Username ;
	private String email ;
	private String password ;

	// default constructor
	public User() {}

	// constructor using fields
	public User(int id, String firstname, String lastname, String username, String email, String password) {
		this.Id = id;
		this.Firstname = firstname;
		this.Lastname = lastname;
		this.Username = username;
		this.email = email;
		this.password = password;
	}
	// Getters and Setters for all attributes class
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getFirstname() {
		return Firstname;
	}
	public void setFirstname(String firstname) {
		Firstname = firstname;
	}
	public String getLastname() {
		return Lastname;
	}
	public void setLastname(String lastname) {
		Lastname = lastname;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [Id=" + Id + ", Firstname=" + Firstname + ", Lastname=" + Lastname + ", Username=" + Username
				+ ", email=" + email + ", password=" + password + "] \n";
	}

   

}
