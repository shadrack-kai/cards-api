package com.logicea.cards.security;

import com.logicea.cards.config.HeaderWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        final String authToken = getAuthTokenFromHeader(request);
        if (authToken != null && tokenProvider.validateJwtToken(authToken)) {
            final String email = tokenProvider.getEmailFromJwtToken(authToken);
            /*
                Get roles from JWT token and set UserDetails - This will DB call when userDetailsService is not used
             */
            final List<SimpleGrantedAuthority> authorities = tokenProvider.getRolesFromToken(authToken).stream()
                    .map(SimpleGrantedAuthority::new).toList();
            final UserDetails userDetails = new User(email, "{}", authorities);
            final UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            HeaderWrapper headerWrapper = new HeaderWrapper(request);
            headerWrapper.addHeader("x-user-id", String.valueOf(tokenProvider.getUserIdFromToken(authToken)));
            headerWrapper.addHeader("x-user-roles", String.valueOf(tokenProvider.getRolesFromToken(authToken)));
            filterChain.doFilter(headerWrapper, response);
        } else filterChain.doFilter(request, response);

    }

    private String getAuthTokenFromHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}