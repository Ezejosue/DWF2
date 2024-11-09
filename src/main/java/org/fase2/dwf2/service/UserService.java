package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.RegisterRequestDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDto> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::mapToDto);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUserByEmail(String email, RegisterRequestDto registerRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(registerRequest.getName());
            user.setDui(registerRequest.getDui());
            user.setPassword(registerRequest.getPassword());
            user.setRole(registerRequest.getRole());
            User updatedUser = userRepository.save(user);
            return mapToDto(updatedUser);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void deleteUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public UserDto getUserProfile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return mapToDto(optionalUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto save(RegisterRequestDto registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setDui(registerRequest.getDui());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDui(user.getDui());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDui(user.getDui());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }
}
