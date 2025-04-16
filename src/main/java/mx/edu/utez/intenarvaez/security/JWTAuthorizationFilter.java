package mx.edu.utez.intenarvaez.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.intenarvaez.services.security.repository.IJWTUtilityService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final IJWTUtilityService jwtUtilityService;

    public JWTAuthorizationFilter(IJWTUtilityService jwtUtilityService) {
        this.jwtUtilityService = jwtUtilityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            JWTClaimsSet claim = jwtUtilityService.parseJWT(token);


            List<String> roles = (List<String>) claim.getClaim("roles");


            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();



            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(claim.getSubject(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | ParseException | JOSEException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
