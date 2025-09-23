package se.ifmo.information_security_1.model;

import java.io.Serial;
import java.io.Serializable;

public record LoginResponseDto(String accessToken) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3306581419511760609L;
}
