package com.swe1qq.gamestore.domain.contract;

import com.swe1qq.gamestore.persistence.entity.impl.User;

public interface AuthService {

    boolean authenticate(String username, String password);

    boolean isAuthenticated();

    User getUser();

    void logout();
}
