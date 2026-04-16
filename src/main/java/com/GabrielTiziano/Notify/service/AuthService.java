package com.GabrielTiziano.Notify.service;

import com.GabrielTiziano.Notify.repository.ClientAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final ClientAppRepository clientAppRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientAppRepository.findUserByEmail(username).orElseThrow(()->new UsernameNotFoundException("Usuário ou senha incorretos."));
    }
}
