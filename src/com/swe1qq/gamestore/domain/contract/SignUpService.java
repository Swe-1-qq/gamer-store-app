package com.swe1qq.gamestore.domain.contract;

import com.swe1qq.gamestore.domain.dto.UserAddDto;
import com.swe1qq.gamestore.persistence.entity.impl.User;

import java.util.function.Supplier;

public interface SignUpService {

    void signUp(UserAddDto userAddDto, Supplier<String> waitForUserInput);

    User getAuthenticatedUser();
}
