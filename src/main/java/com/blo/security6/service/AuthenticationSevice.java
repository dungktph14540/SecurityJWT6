package com.blo.security6.service;

import com.blo.security6.dto.request.AuthenticationRequest;
import com.blo.security6.dto.request.IntrospectRequest;
import com.blo.security6.dto.request.UserCreationRequest;
import com.blo.security6.dto.response.AuthenzicationReponse;
import com.blo.security6.dto.response.IntrospectReponse;
import com.blo.security6.dto.response.UserResponse;
import com.blo.security6.entity.Users;
import com.blo.security6.exception.AppException;
import com.blo.security6.exception.ErrorCode;
import com.blo.security6.repository.RepositoryUsers;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationSevice {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    public IntrospectReponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();


        var veritified= signedJWT.verify(verifier);

        return IntrospectReponse.builder().vaild(veritified && expityTime.after(new Date())).build() ;
    }

    RepositoryUsers repositoryUsers;

    public AuthenzicationReponse authentication(AuthenticationRequest request){
        var user = repositoryUsers.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USERNAME_INVALID));
        PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authentication = passwordEncoder.matches(request.getPassword(),user.getPassword());
        if(!authentication)
            throw new AppException(ErrorCode.UNAUTHORIZED);
            var token  = generateToken(user);
            return AuthenzicationReponse.builder().token(token).authenzication(true).build();

    }
    String generateToken(Users users){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(users.getEmail())
                .issuer("devesior.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",buildScope(users))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();

        }  catch (JOSEException e)  {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }

    }
    private String buildScope(Users users){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(users.getRole()))
            users.getRole().forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
