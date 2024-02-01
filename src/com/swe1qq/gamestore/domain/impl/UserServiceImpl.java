package com.swe1qq.gamestore.domain.impl;

import org.mindrot.bcrypt.BCrypt;
import com.swe1qq.gamestore.domain.contract.UserService;
import com.swe1qq.gamestore.domain.dto.UserAddDto;
import com.swe1qq.gamestore.domain.exception.EntityNotFoundException;
import com.swe1qq.gamestore.domain.exception.SignUpException;
import com.swe1qq.gamestore.persistence.entity.impl.User;
import com.swe1qq.gamestore.persistence.repository.contracts.UserRepository;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

final class UserServiceImpl
        extends GenericServiceImpl<User>
        implements UserService {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Такого користувача не існує."));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new EntityNotFoundException("Такого користувача не існує."));
    }

    @Override
    public Set<User> getAll() {
        return getAll(u -> true);
    }

    @Override
    public Set<User> getAll(Predicate<User> filter) {
        return new TreeSet<>(userRepository.findAll(filter));
    }

    @Override
    public User add(User entity) {
        throw new NotImplementedException(
                "Помилка архітектури, так як ми не використовували DTO та маппінг. "
                        + "Прошу використовувати User add(UserAddDto userAddDto) версію.");
    }

    @Override
    public User add(UserAddDto userAddDto) {
        try {
            String avatar = ImageUtil.copyToImages(userAddDto.avatarPath());
            var user = new User(userAddDto.getId(),
                    BCrypt.hashpw(userAddDto.rawPassword(), BCrypt.gensalt()),
                    userAddDto.email(),
                    userAddDto.birthday(),
                    userAddDto.username(),
                    avatar,
                    userAddDto.role());
            userRepository.add(user);
            return user;
        } catch (IOException e) {
            throw new SignUpException("Помилка при збереженні аватара користувача: %s"
                    .formatted(e.getMessage()));
        }
    }
}
