package com.github.eybv.messenger.api.error;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ErrorInfo {

    String message;

    @Singular
    List<String> errors;

}
