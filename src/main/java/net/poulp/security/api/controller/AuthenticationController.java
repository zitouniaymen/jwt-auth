package net.poulp.security.api.controller;

import net.poulp.security.model.AuthenticationRequest;
import net.poulp.security.model.JwtTokens;
import net.poulp.security.model.RefreshToken;
import net.poulp.security.service.AuthenticationService;
import net.poulp.security.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping(value = {"/auth"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        Authentication authentication = authenticationService.authenticate(authenticationRequest);

        if(authentication != null && authentication.isAuthenticated()) {
            JwtTokens tokens = jwtTokenService.createTokens(authentication);
            return ResponseEntity.ok().body(tokens);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }

}