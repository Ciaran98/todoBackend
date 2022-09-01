package ciaran.application.service;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;

@Service
public class jwtUserService {

    public static String createToken(String email, String id)
            throws IOException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";
        Properties props = new Properties();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        props.load(new FileInputStream(appConfigPath));
        String jwtSecret = props.getProperty("JWT_SECRET");
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setId("Hi")
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 3600000))
                .claim("email", email)
                .claim("id", id)
                .signWith(signingKey, signatureAlgorithm);
        return builder.compact();
    }

    public static Claims consumeToken(String token) throws IOException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";
        Properties props = new Properties();
        props.load(new FileInputStream(appConfigPath));
        String jwtSecret = props.getProperty("JWT_SECRET");
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }
}
