package com.ViviEstu.security;

import com.ViviEstu.model.entity.User;
import com.ViviEstu.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getName().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(),
                user.getContrasenia(),
                user.getActive(),
                true, true, true,
                authorities
        );
    }
}