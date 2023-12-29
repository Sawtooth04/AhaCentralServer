package com.sawtooth.ahacentralserver.services.security;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenticationProvider implements AuthenticationProvider {
    private final IStorage storage;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerAuthenticationProvider(IStorage storage, PasswordEncoder passwordEncoder) {
        this.storage = storage;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Customer customer = storage.GetRepository(ICustomerRepository.class).Get(authentication.getName());
            UserDetails userDetails = User.builder()
                    .username(customer.name())
                    .password(customer.passwordHash())
                    .build();
            if (passwordEncoder.matches((CharSequence) authentication.getCredentials(), customer.passwordHash()))
                return new UsernamePasswordAuthenticationToken(userDetails, customer.passwordHash(), userDetails.getAuthorities());
        }
        catch (InstantiationException e) {
            throw new UsernameNotFoundException(authentication.getName());
        }
        throw new UsernameNotFoundException(authentication.getName());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
