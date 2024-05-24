package com.blo.security6.controller;

import com.blo.security6.dto.request.ApiResponse;
import com.blo.security6.dto.request.AuthenticationRequest;
import com.blo.security6.dto.request.IntrospectRequest;
import com.blo.security6.dto.response.AuthenzicationReponse;
import com.blo.security6.dto.response.IntrospectReponse;
import com.blo.security6.service.AuthenticationSevice;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("auth")
public class Authenzication {

    AuthenticationSevice authenticationSevice;



    @PostMapping("token")
    ApiResponse<AuthenzicationReponse> login(@RequestBody AuthenticationRequest request){
       var result =  authenticationSevice.authentication(request);
        return ApiResponse.<AuthenzicationReponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("introspect")
    ApiResponse<IntrospectReponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result =  authenticationSevice.introspect(request);
        return ApiResponse.<IntrospectReponse>builder()
                .result(result)
                .build();
    }
}
