package se.ifmo.information_security_1.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serial;
import java.io.Serializable;

public record LoginRequestDto(
        @NotBlank @Pattern(regexp = "^[a-zA-Z0-9_]+$") String username,
        @NotBlank @Pattern(regexp = "^[a-zA-Z0-9_]+$") String password
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6058077600555796507L;
}
