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
import project.bookstore.repository.role.RoleRepo;
import project.bookstore.repository.user.UserRepo;
import project.bookstore.service.UserService;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepo userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepo roleRepo;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        Role role = roleRepo.findByRole(RoleName.ROLE_ADMIN);
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(requestDto.getEmail() + "allready exists!");
        }
        User user = userMapper.toModel(requestDto);

        user.setPassword(encoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }
}
