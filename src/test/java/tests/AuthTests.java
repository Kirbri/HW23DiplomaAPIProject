package tests;

import models.auth.GenerateTokenLoginRequest;
import models.auth.TokenResponse;
import models.auth.UnsuccessfulTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.GeneralSpec.*;

@DisplayName("Проверка авторизации через создание токена")
@Tag("auth_api")
public class AuthTests extends TestBase {

    @Test
    @DisplayName("Successful creates a new auth token")
    public void successfulCreateTokenTest() {
        GenerateTokenLoginRequest requestData = step("Подготовить данные для создания токена", () -> {
            GenerateTokenLoginRequest data = new GenerateTokenLoginRequest();
            data.setUsername("admin");
            data.setPassword("password123");
            return data;
        });

        TokenResponse responseData = step("Отправить запрос на создание токена аутентификации", () ->
                given(requestSpecificationWithoutAuth)
                        .body(requestData)
                        .when()
                        .post("/auth")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(TokenResponse.class));

        step("Проверить данных в ответе", () -> {
            assertThat(responseData.getToken()).isNotNull();
        });
    }

    @Test
    @DisplayName("Unsuccessful creates a new auth token with code 200")
    public void unsuccessfulCreateTokenBadCredentialsTest() {
        GenerateTokenLoginRequest requestData = step("Подготовить данные для создания токена", () -> {
            GenerateTokenLoginRequest data = new GenerateTokenLoginRequest();
            data.setUsername("");
            data.setPassword("");
            return data;
        });
        UnsuccessfulTokenResponse responseData = step("Отправить запрос на создание токена аутентификации", () ->
                given(requestSpecificationWithoutAuth)
                        .body(requestData)
                        .when()
                        .post("/auth")
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(UnsuccessfulTokenResponse.class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.getReason()).isEqualTo("Bad credentials");
        });
    }
}
