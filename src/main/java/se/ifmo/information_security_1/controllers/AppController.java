package se.ifmo.information_security_1.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.information_security_1.model.LoginRequestDto;
import se.ifmo.information_security_1.model.LoginResponseDto;
import se.ifmo.information_security_1.model.PersonDetails;
import se.ifmo.information_security_1.model.PersonInfoDto;
import se.ifmo.information_security_1.service.PeopleService;

import java.util.List;

import static org.springframework.http.HttpStatusCode.valueOf;
import static org.springframework.http.ResponseEntity.*;

@RestController
public class AppController {

    private final PeopleService peopleService;

    public AppController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginDTO,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            return badRequest().build();
        }
        return buildLoginResponseDtoIfPersonAuthenticated(loginDTO);
    }

    private ResponseEntity<LoginResponseDto> buildLoginResponseDtoIfPersonAuthenticated(LoginRequestDto loginDTO) {
        try {
            return ok(peopleService.login(loginDTO));
        } catch (AuthenticationException e) {
            return status(valueOf(401)).build();
        }
    }

    @GetMapping("/api/data")
    public ResponseEntity<List<PersonInfoDto>> getPeople() {
        var people = peopleService.getPeopleInfo();
        return ok(people);
    }

    @GetMapping("/api/profile")
    public ResponseEntity<PersonInfoDto> getPersonInfo(@AuthenticationPrincipal PersonDetails details) {
        try {
            var person = peopleService.getPersonInfo(details);
            return ok(person);
        } catch (IllegalStateException e) {
            return status(500).build();
        }
    }
}
