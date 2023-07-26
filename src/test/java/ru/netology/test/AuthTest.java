package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] [name=\"login\"]").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] [name=\"password\"]").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $("[id=\"root\"]").shouldHave(Condition.text("  Личный кабинет")).shouldBe(Condition.visible);
    }

    @SneakyThrows
    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=\"login\"] [name=\"login\"]").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] [name=\"password\"]").setValue(notRegisteredUser.getPassword());
        $(".button__text").click();
        $("[data-test-id=\"error-notification\"]").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
//        Thread.sleep(100000);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] [name=\"login\"]").setValue(blockedUser.getLogin());
        $("[data-test-id=\"password\"] [name=\"password\"]").setValue(blockedUser.getPassword());
        $(".button__text").click();
        $("[data-test-id=\"error-notification\"]").shouldHave(Condition.text("Ошибка! Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=\"login\"] [name=\"login\"]").setValue(wrongLogin);
        $("[data-test-id=\"password\"] [name=\"password\"]").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $("[data-test-id=\"error-notification\"]").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=\"login\"] [name=\"login\"]").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] [name=\"password\"]").setValue(wrongPassword);
        $(".button__text").click();
        $("[data-test-id=\"error-notification\"]").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}
