package engestofte.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseDTO {

	// Expected JSON Output
	// ____________________
	//
	//		{
	//			"access_token": "...",
	//			"refresh_token": "...",
	//			"account": {
	//				"full_name": "Alex Morgan",
	//				"email_redacted": "ale....@example.com",
	//				"role": "CUSTOMER"
	//			}
	//		}
	//
	// ____________________
	// Tested: NO
	// Last Tested: N/A

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("account")
	private AccountResponseDTO account;
}
