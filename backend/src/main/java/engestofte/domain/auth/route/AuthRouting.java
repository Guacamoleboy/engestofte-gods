package engestofte.domain.auth.route;

import engestofte.domain.auth.controller.AuthController;
import engestofte.domain.auth.service.AuthService;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthRouting {

	// Attributes
	private final AuthController authController;

	// _________________________________________________________________________________________________________________

	public AuthRouting(EntityManagerFactory entityManagerFactory) {
		this.authController = new AuthController(new AuthService(entityManagerFactory));
	}

	// _________________________________________________________________________________________________________________

	public EndpointGroup routes() {
		return () -> path("/auth", () -> {
			post("/register", authController::register);
			post("/login", authController::login);
			post("/refresh", authController::refresh);
		});
	}
}
