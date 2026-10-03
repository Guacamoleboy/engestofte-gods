package engestofte.domain.auth.controller;

import engestofte.domain.auth.dto.request.LoginRequestDTO;
import engestofte.domain.auth.dto.request.RefreshTokenRequestDTO;
import engestofte.domain.auth.dto.request.RegisterRequestDTO;
import engestofte.domain.auth.service.AuthService;
import engestofte.util.TryCatchHelper;
import io.javalin.http.Context;

public class AuthController {

	// Attributes
	private final AuthService authService;

	// _________________________________________________________________________________________________________________

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// _________________________________________________________________________________________________________________

	public void register(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> authService.register(context.bodyAsClass(RegisterRequestDTO.class)), "Account registered");
	}

	// _________________________________________________________________________________________________________________

	public void login(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> authService.login(context.bodyAsClass(LoginRequestDTO.class)), "Login successful");
	}

	// _________________________________________________________________________________________________________________

	public void refresh(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> authService.refresh(context.bodyAsClass(RefreshTokenRequestDTO.class)), "Token refreshed");
	}
}
