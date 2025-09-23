package se.ifmo.information_security_1.model;

import org.springframework.web.util.HtmlUtils;

import java.io.Serial;
import java.io.Serializable;

import static java.util.Objects.isNull;

public record PersonInfoDto(String username) implements Serializable {

    @Serial
    private static final long serialVersionUID = -8352305294193857802L;

    @Override
    public String username() {
        return isNull(username) ? null : HtmlUtils.htmlEscape(username);
    }
}
