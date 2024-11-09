package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.RegisterRequestDto;
import org.fase2.dwf2.dto.UserDto;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;

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

    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUserByEmail(String email, RegisterRequestDto registerRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Field[] fields = RegisterRequestDto.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(registerRequest);
                    if (value != null) {
                        Field userField = User.class.getDeclaredField(field.getName());
                        userField.setAccessible(true);
                        userField.set(user, value);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException("Error updating user field", e);
                }
            }
            User updatedUser = userRepository.save(user);
            return mapToDto(updatedUser);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserProfile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return mapToDto(optionalUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
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
