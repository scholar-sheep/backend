package sheep.user.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    private static final Algorithm algorithm = Algorithm.HMAC256("scholar-never-sheep");

    private static final JWTVerifier verifier = JWT.require(algorithm).withIssuer("scholarsheep.top").build();

    public static String generatorToken(int userId) {
        return JWT.create()
                .withIssuer("scholarsheep.top")
                .withIssuedAt(new Date())
                .withClaim("UserId",userId)
                .sign(algorithm);
    }

    public static boolean verifyToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int parseUserId(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("UserId").asInt();
        } catch (Exception e) {
            return -1;
        }
    }
}
