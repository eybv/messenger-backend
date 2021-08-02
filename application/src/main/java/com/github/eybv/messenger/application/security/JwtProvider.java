package com.github.eybv.messenger.application.security;

import java.util.Map;
import java.util.Optional;

public interface JwtProvider {

    String createToken(String subject, Map<String, String> claims);

    Optional<String> getSubjectFromToken(String token);

}
