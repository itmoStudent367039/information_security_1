package se.ifmo.information_security_1.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import se.ifmo.information_security_1.service.PersonDetailsService;

import java.io.IOException;

@Order(2)
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PersonDetailsService service;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, PersonDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException, JWTVerificationException, UsernameNotFoundException {
        String jwt = extractJwtFromHeader(request);
        if (StringUtils.hasText(jwt)) {
            setAuthenticationIfTokenIsValid(jwt, response);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationIfTokenIsValid(String jwt, HttpServletResponse response) throws IOException {
        try {
            String email = getEmailFromJwt(jwt);
            UserDetails userDetails = service.loadUserByUsername(email);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                this.authenticateUser(userDetails);
            }
        } catch (UsernameNotFoundException | JWTVerificationException e) {
            response.sendError(401);
            System.err.println(
                    this.getClass().getName()
                            + "catch exception - "
                            + e.getClass()
                            + ": "
                            + e.getMessage()
            );
        }
    }

    private String extractJwtFromHeader(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.split(" ")[1].trim();
        }
        return null;
    }

    private String getEmailFromJwt(String jwt) throws JWTVerificationException {
        return jwtUtil.validateTokenAndRetrieveClaim(jwt);
    }

    private void authenticateUser(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
