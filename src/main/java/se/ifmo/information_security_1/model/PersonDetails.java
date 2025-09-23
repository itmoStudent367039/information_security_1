package se.ifmo.information_security_1.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.ifmo.information_security_1.entity.Person;

import java.io.Serial;
import java.util.Collection;

import static java.util.Collections.emptyList;

public class PersonDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = -9199349035778755336L;

    private transient final Person person;

    public PersonDetails(Person person) {
        this.person = createDefCopy(person);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return emptyList();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Person getPerson() {
        return createDefCopy(person);
    }

    private Person createDefCopy(Person orig) {
        if (orig == null) {
            return null;
        }
        return new Person(orig.getPassword(), orig.getUsername());
    }
}
