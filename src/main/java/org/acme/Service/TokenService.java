package org.acme.Service;


import org.acme.Utils.TokenUtils;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;
import org.jose4j.jwt.JwtClaims;

import javax.enterprise.context.RequestScoped;
import java.util.Arrays;

@RequestScoped
public class TokenService {
    private static final Logger logger = Logger.getLogger(TokenService.class);
    public String generateToken(String email , String username, Long id) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer("Munkhtulga");
            jwtClaims.setJwtId("a-123");
            jwtClaims.setSubject(email);
            jwtClaims.setClaim(Claims.upn.name(), email);
            jwtClaims.setClaim(Claims.preferred_username.name(), username);
            jwtClaims.setClaim(Claims.birthdate.name(), id);
            jwtClaims.setClaim(Claims.groups.name(), Arrays.asList(TokenUtils.ROLE_USER));
            jwtClaims.setAudience("using-jwt");
            jwtClaims.setExpirationTimeMinutesInTheFuture(1);
            String token = TokenUtils.generateTokenString(jwtClaims);
            logger.infov("jwt={0}", token);
            return token;
        }
        catch (Exception e){
            e.getMessage();
            throw new RuntimeException(e);
        }
    }
}
