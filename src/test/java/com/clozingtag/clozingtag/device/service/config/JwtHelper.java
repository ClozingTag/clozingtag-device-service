package com.clozingtag.clozingtag.device.service.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAKey;
import java.util.Date;
import java.util.List;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class JwtHelper {

    private static final String KEY_ID = "clozingtag";

    public static String createJwt(String subject, List<String> roles, String issuer) throws Exception {
        String privateKeyContent = readPrivateKeyFromResource();
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Algorithm algorithm = Algorithm.RSA256((RSAKey) privateKey);
        return JWT.create()
                .withKeyId(KEY_ID)
                .withSubject(subject)
                .withClaim("roles", roles)
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm);
    }

    private static String readPrivateKeyFromResource() throws IOException {

        ClassPathResource resource = new ClassPathResource("private_key.pem");
        String key = new String(Objects.requireNonNull(resource.getInputStream()).readAllBytes(), StandardCharsets.UTF_8);
        return key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }

}