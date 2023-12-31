package com.sample_project.AdminApp.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDetailsDto implements Serializable {
	private static final long serialVersionUID = 1L;
	Long id;
	String name;
	String userName;
	String email;
	AddressDto address;
	String phone;
	CompanyDto company;

	public String get(String id) {
		return id;
	}
}
