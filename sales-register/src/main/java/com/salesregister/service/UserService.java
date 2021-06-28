package com.salesregister.service;

import com.salesregister.domain.User;
import com.salesregister.repository.UserRepository;
import com.salesregister.request.AuthenticationRequest;
import com.salesregister.request.RegistrationRequest;
import com.salesregister.service.exception.AuthenticationException;
import com.salesregister.service.exception.UserAlreadyExistsException;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private User user;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(@NonNull RegistrationRequest request) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setId(null);
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);

        this.user = userRepository.save(user);
    }

    @Transactional
    public void authenticate(@NonNull AuthenticationRequest request) throws AuthenticationException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException());

        boolean matched = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!matched) {
            throw new AuthenticationException();
        }

        this.user = user;
    }

    public User getCurrentUser() {
        return user;
    }

    public void logout() {
        user = null;
    }
}
