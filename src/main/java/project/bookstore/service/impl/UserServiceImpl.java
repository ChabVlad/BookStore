package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.dto.user.UserResponseDto;
import project.bookstore.exception.RegistrationException;
import project.bookstore.mapper.UserMapper;
import project.bookstore.model.User;
import project.bookstore.repository.user.UserRepo;
import project.bookstore.service.UserService;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepo userRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsUserByEmail(requestDto.getEmail())) {
            throw new RegistrationException(requestDto.getEmail() + "allready exists!");
        }
        User user = userMapper.toModel(requestDto);
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }
}
