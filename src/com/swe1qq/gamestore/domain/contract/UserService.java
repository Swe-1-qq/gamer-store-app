package com.swe1qq.gamestore.domain.contract;

import com.swe1qq.gamestore.domain.Service;
import com.swe1qq.gamestore.persistence.entity.impl.User;
import com.swe1qq.gamestore.domain.dto.UserAddDto;

public interface UserService extends Service<User> {

    User getByUsername(String username);

    User getByEmail(String email);

    User add(UserAddDto userAddDto);
}
