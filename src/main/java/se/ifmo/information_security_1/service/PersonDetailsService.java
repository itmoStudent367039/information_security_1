package se.ifmo.information_security_1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ifmo.information_security_1.entity.Person;
import se.ifmo.information_security_1.entity.PersonRepository;
import se.ifmo.information_security_1.model.PersonDetails;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MSG = "User with email %s not found";

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository peopleRepository) {
        this.personRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }
        return new PersonDetails(person.get());
    }
}
