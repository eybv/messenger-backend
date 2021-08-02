package com.github.eybv.messenger.application.service.auth;

import com.github.eybv.messenger.application.data.AccessToken;

import java.util.Optional;

public interface AuthenticationService {

    Optional<AccessToken> authenticate(String email, String password);

}
