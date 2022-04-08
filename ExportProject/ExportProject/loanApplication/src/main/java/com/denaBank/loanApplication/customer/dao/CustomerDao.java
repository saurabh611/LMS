package com.denaBank.loanApplication.customer.dao;


import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

@Entity
public class CustomerDao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	@NotNull
	private String userName;
	@NotNull
	private String password;
	@NotNull
	private String role;
	@NotNull
	@javax.validation.constraints.NotNull(message = "Contact can not be null")
	@Email(message = "Email must be in valid form")
	private String email;
	private LocalDate dateOfBirth;
	@javax.validation.constraints.NotNull(message = "Gender can not be null")
	private String gender;
	@javax.validation.constraints.NotNull(message = "Contact can not be null")
	@Size(min = 10,max=10,message = "Contact number must be 10 digit")
	private String contact;
//	@NotNull
	@javax.validation.constraints.NotNull(message = "Address can not be null")
	private String address;
	
	public CustomerDao() {}

	public CustomerDao(String userName, String password, String role, String email, LocalDate dateOfBirth,
			String gender, String contact, String address) {
		super();
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.contact = contact;
		this.address = address;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

}
