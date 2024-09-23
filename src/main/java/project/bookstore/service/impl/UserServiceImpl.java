package project.bookstore.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.exception.RegistrationException;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.Role;
import project.bookstore.model.RoleName;
import project.bookstore.model.User;
import project.bookstore.repository.role.RoleRepository;
import project.bookstore.repository.user.UserRepository;
import project.bookstore.service.UserService;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(requestDto.getEmail() + "allready exists!");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole(RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }
}
