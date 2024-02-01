package com.swe1qq.gamestore;

import com.swe1qq.gamestore.appui.AuthView;
import com.swe1qq.gamestore.domain.contract.AuthService;
import com.swe1qq.gamestore.domain.contract.SignUpService;
import com.swe1qq.gamestore.domain.impl.GamerStoreView;
import com.swe1qq.gamestore.domain.impl.ServiceFactory;
import com.swe1qq.gamestore.persistence.repository.RepositoryFactory;
import jline.TerminalFactory;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

final class Startup {

    static void init() {
        RepositoryFactory jsonRepositoryFactory = RepositoryFactory
                .getRepositoryFactory(RepositoryFactory.JSON);
        ServiceFactory serviceFactory = ServiceFactory.getInstance(jsonRepositoryFactory);
        AuthService authService = serviceFactory.getAuthService();
        GamerStoreView gamerStoreService = serviceFactory.getGamerStoreView();
        SignUpService signUpService = serviceFactory.getSignUpService();

        //===
        AnsiConsole.systemInstall();                                      // #1

        try {
            AuthView authView = new AuthView(authService, signUpService, gamerStoreService);
            authView.render();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                TerminalFactory.get().restore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //===

        // Цей рядок, має бути обовязково в кінці метода main!!!!
        jsonRepositoryFactory.commit();
    }
}
