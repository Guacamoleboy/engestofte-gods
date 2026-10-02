package engestofte.security.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Date;

public class JwtService extends JwtUtil {

    // Attributes
    protected static final String CLAIM_FIRST_NAME = "first_name";
    protected static final String CLAIM_TYPE = "type";
    protected static final String CLAIM_ROLE = "role";

    // _________________________________________________________________________________________________________________

	public static String generateAccessToken(Integer accountId, String firstName, String role) {
		return Jwts.builder()
				.setSubject(accountId.toString())
				.claim(CLAIM_FIRST_NAME, firstName)
				.claim(CLAIM_TYPE, "access")
				.claim(CLAIM_ROLE, role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    // _________________________________________________________________________________________________________________

	public static String generateRefreshToken(Integer accountId) {
		return Jwts.builder()
				.setSubject(accountId.toString())
                .claim(CLAIM_TYPE, "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    // _________________________________________________________________________________________________________________

	public static Integer getClaimAccountId(String token) {
        return Integer.valueOf(getClaims(token).getSubject());
    }

    // _________________________________________________________________________________________________________________

    public static String getClaimFirstName(String token) {
        return getClaims(token).get(CLAIM_FIRST_NAME, String.class);
    }

    // _________________________________________________________________________________________________________________

    public static String getClaimRole(String token) {
        return getClaims(token).get(CLAIM_ROLE, String.class);
    }

    // _________________________________________________________________________________________________________________

    public static String getClaimTokenType(String token) {
        return getClaims(token).get(CLAIM_TYPE, String.class);
    }

}