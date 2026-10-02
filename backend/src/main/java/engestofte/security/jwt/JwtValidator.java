package engestofte.security.jwt;

import engestofte.exception.ApiException;
import engestofte.util.ContextHelper;
import io.javalin.http.Context;

public class JwtValidator {

    // Attributes

    // _________________________________________________________________________________________________________________

    public static boolean isValid(Context ctx) {
        String token = ContextHelper.extractBearerToken(ctx);
        return token != null && JwtUtil.isValid(token);
    }

    // _________________________________________________________________________________________________________________

    public static boolean hasRole(Context ctx, String role) {
        String token = ContextHelper.extractBearerToken(ctx);
        if (!JwtUtil.isAccessTokenValid(token)) {
            throw new ApiException(401, "Invalid access token");
        }
        return role.equals(JwtService.getClaimRole(token));
    }

}