package com.swe1qq.gamestore.persistence.repository.contracts;

import com.swe1qq.gamestore.persistence.entity.impl.User;
import com.swe1qq.gamestore.persistence.repository.Repository;
import java.util.Optional;

/**
 * Ми визначаємо з яким ентіті працювати (убираємо дженерік). Ми додаємо "кастомні" методі, які
 * актульні лише для цієї сутності!
 */
public interface UserRepository extends Repository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
