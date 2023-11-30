package com.callthirdpartyapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
	
	@NotNull(message = "Name should not be null")
	private String name;
	
	@Email(message = "please enter valid email")
	private String username;
	
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$",
			message = "Min 1 uppercase letter.\r\n"
					+ "Min 1 lowercase letter.\r\n"
					+ "Min 1 special character.\r\n"
					+ "Min 1 number.\r\n"
					+ "Min 8 characters."
			)
	private String password;
	


}
