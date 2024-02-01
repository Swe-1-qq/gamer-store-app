package com.swe1qq.gamestore.appui;

import com.swe1qq.gamestore.domain.contract.AuthService;
import com.swe1qq.gamestore.domain.contract.SignUpService;
import com.swe1qq.gamestore.domain.dto.UserAddDto;
import com.swe1qq.gamestore.domain.exception.AuthException;
import com.swe1qq.gamestore.domain.impl.GamerStoreView;
import com.swe1qq.gamestore.persistence.entity.impl.User;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import static com.swe1qq.gamestore.appui.AuthView.AuthMenu.EXIT;
import static com.swe1qq.gamestore.appui.AuthView.AuthMenu.SIGN_IN;
import static com.swe1qq.gamestore.appui.AuthView.AuthMenu.SIGN_UP;

public final class AuthView implements Renderable {

    private final AuthService authService;
    private final SignUpService signUpService;
    private final GamerStoreView gamerStoreView;

    public AuthView(AuthService authService, SignUpService signUpService, GamerStoreView gamerStoreView) {
        this.authService = authService;
        this.signUpService = signUpService;
        this.gamerStoreView = gamerStoreView;
    }

    public User getAuthenticatedUser() {
        User authUser = authService.getUser();
        User signUpUser = signUpService.getAuthenticatedUser();

        if (authUser != null) {
            return authUser;
        } else if (signUpUser != null) {
            return signUpUser;
        }

        return null;
    }

    private void process(AuthMenu selectedItem) throws IOException {
        ConsolePrompt prompt = new ConsolePrompt();
        PromptBuilder promptBuilder = prompt.getPromptBuilder();

        switch (selectedItem) {
            case SIGN_IN -> {
                promptBuilder.createInputPrompt()
                        .name("username")
                        .message("Впишіть ваш логін: ")
                        .addPrompt();
                promptBuilder.createInputPrompt()
                        .name("password")
                        .message("Впишіть ваш пароль: ")
                        .mask('*')
                        .addPrompt();

                var result = prompt.prompt(promptBuilder.build());
                var usernameInput = (InputResult) result.get("username");
                var passwordInput = (InputResult) result.get("password");

                try {
                    boolean authenticate = authService.authenticate(
                            usernameInput.getInput(),
                            passwordInput.getInput()
                    );

                    if (authenticate) {
                        gamerStoreView.setUser(getAuthenticatedUser());
                        System.out.printf("Юзера %s аутентифіковано %n", usernameInput);
                        gamerStoreView.render();
                    }

                } catch (AuthException e) {
                    System.err.println(e.getMessage());
                }

            }
            case SIGN_UP -> {
                promptBuilder.createInputPrompt()
                        .name("username")
                        .message("Впишіть ваш логін: ")
                        .addPrompt();
                promptBuilder.createInputPrompt()
                        .name("password")
                        .message("Впишіть ваш пароль: ")
                        .mask('*')
                        .addPrompt();
                promptBuilder.createInputPrompt()
                        .name("email")
                        .message("Впишіть ваш email: ")
                        .addPrompt();
                promptBuilder.createInputPrompt()
                        .name("day")
                        .message("Впишіть день народження: ")
                        .addPrompt();
                promptBuilder.createInputPrompt()
                        .name("month")
                        .message("Впишіть місяць народження: ")
                        .addPrompt();
                promptBuilder.createInputPrompt()
                        .name("year")
                        .message("Впишіть рік народження: ")
                        .addPrompt();

                var result = prompt.prompt(promptBuilder.build());
                var usernameInput = (InputResult) result.get("username");
                var passwordInput = (InputResult) result.get("password");
                var emailInput = (InputResult) result.get("email");
                var dayInput = (InputResult) result.get("day");
                var monthInput = (InputResult) result.get("month");
                var yearInput = (InputResult) result.get("year");

                var date = LocalDate.of(Integer.parseInt(yearInput.getInput()),
                        Integer.parseInt(monthInput.getInput()),
                        Integer.parseInt(dayInput.getInput()));

                var avatar = "Images/default/default-avatar.jpg";

                try {
                    var userDto = new UserAddDto(UUID.randomUUID(), usernameInput.getInput(), passwordInput.getInput(), emailInput.getInput(), date, avatar);
                    signUpService.signUp(userDto, () -> getCodeInput(prompt));

                    System.out.printf("Юзера %s створено", usernameInput.getInput());

                    gamerStoreView.setUser(getAuthenticatedUser());
                    gamerStoreView.render();
                } catch (AuthException e) {
                    System.err.println(e.getMessage());
                }

            }
            case EXIT -> System.out.close();
            default -> {

            }
        }
    }

    private String getCodeInput(ConsolePrompt prompt) {
        try {
            PromptBuilder promptBuilder;
            promptBuilder = prompt.getPromptBuilder();

            promptBuilder.createInputPrompt()
                    .name("code")
                    .message("Впишіть код надісланий на email: ")
                    .addPrompt();

            var codeResult = prompt.prompt(promptBuilder.build());
            var codeInput = (InputResult) codeResult.get("code");

            return codeInput.getInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt();
        PromptBuilder promptBuilder = prompt.getPromptBuilder();

        promptBuilder.createListPrompt()
                .name("auth-menu")
                .message("Локальний блог")
                .newItem(SIGN_IN.toString()).text(SIGN_IN.getName()).add()
                .newItem(SIGN_UP.toString()).text(SIGN_UP.getName()).add()
                .newItem(EXIT.toString()).text(EXIT.getName()).add()
                .addPrompt();

        var result = prompt.prompt(promptBuilder.build());
        ListResult resultItem = (ListResult) result.get("auth-menu");

        AuthMenu selectedItem = AuthMenu.valueOf(resultItem.getSelectedId());
        process(selectedItem);
    }

    enum AuthMenu {
        SIGN_IN("Авторизація"),
        SIGN_UP("Створити обліковий аккаунт"),
        EXIT("Вихід");

        private final String name;

        AuthMenu(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
