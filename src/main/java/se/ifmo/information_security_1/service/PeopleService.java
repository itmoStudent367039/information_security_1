package se.ifmo.information_security_1.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.information_security_1.entity.Person;
import se.ifmo.information_security_1.entity.PersonRepository;
import se.ifmo.information_security_1.model.LoginRequestDto;
import se.ifmo.information_security_1.model.LoginResponseDto;
import se.ifmo.information_security_1.model.PersonDetails;
import se.ifmo.information_security_1.model.PersonInfoDto;
import se.ifmo.information_security_1.security.JwtUtil;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PersonRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public PeopleService(PersonRepository peopleRepository,
                         PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        Person person = getAuthenticatedPersonOrThrowException(requestDto);
        String accessToken = jwtUtil.generateToken(person.getUsername());
        return new LoginResponseDto(accessToken);
    }

    @Transactional
    public void save(String username, String password) {
        peopleRepository.save(toModel(username, password));
    }

    public List<PersonInfoDto> getPeopleInfo() {
        return peopleRepository.findAll()
                .stream()
                .map(this::toModel)
                .toList();
    }

    public PersonInfoDto getPersonInfo(PersonDetails personDetails) {
        return peopleRepository.findByUsername(personDetails.getUsername())
                .map(this::toModel)
                .orElseThrow(IllegalStateException::new);
    }

    private PersonInfoDto toModel(Person entity) {
        return new PersonInfoDto(entity.getUsername());
    }

    private Person toModel(String username, String password) {
        return new Person(passwordEncoder.encode(password), username);
    }

    private Person getAuthenticatedPersonOrThrowException(LoginRequestDto requestDto) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password())
        );
        return ((PersonDetails) auth.getPrincipal()).getPerson();
    }
}
