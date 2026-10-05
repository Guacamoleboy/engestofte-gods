package engestofte.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engestofte.domain.role.enums.RoleName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"full_name": "Alex Morgan",
	//			"email_redacted": "ale....@example.com",
	//			"role": "CUSTOMER"
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("full_name")
	private String fullName;

	@JsonProperty("email_redacted")
	private String emailRedacted;

	@JsonProperty("role")
	private RoleName role;
}
