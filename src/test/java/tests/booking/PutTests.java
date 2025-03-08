package tests.booking;

import api.CreateBooking;
import models.booking.GetBookingRequest;
import models.booking.GetBookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.GeneralSpec.*;

@DisplayName("Проверка обновления данных в бронировании")
@Tag("booking_api")
public class PutTests extends TestBase {
    CreateBooking createBooking = new CreateBooking();

    @Test
    @DisplayName("Successful updates a current booking")
    public void successfulUpdateBookingByIdTest() {
        GetBookingResponse testDataResponse = createBooking.successfulCreateBooking();
        GetBookingRequest testDataRequest = createBooking.successfulDataForCreateBooking();

        GetBookingRequest responseData = step("Отправить запрос на обновление данных по бронированию", () ->
                given(requestSpecificationWithAuth)
                        .body(testDataRequest)
                        .when()
                        .put("/booking/" + testDataResponse.getBookingid())
                        .then()
                        .spec(responseSpecification200)
                        .extract().as(GetBookingRequest.class));

        step("Проверить данные в ответе", () -> {
            assertThat(responseData.getFirstname()).isEqualTo(testDataRequest.getFirstname());
            assertThat(responseData.getLastname()).isEqualTo(testDataRequest.getLastname());
            assertThat(responseData.getTotalprice()).isEqualTo(testDataRequest.getTotalprice());
            assertThat(responseData.getBookingdates().getCheckin()).isEqualTo(testDataRequest.getBookingdates().getCheckin());
            assertThat(responseData.getBookingdates().getCheckout()).isEqualTo(testDataRequest.getBookingdates().getCheckout());
            assertThat(responseData.isDepositpaid()).isEqualTo(testDataRequest.isDepositpaid());
            assertThat(responseData.getAdditionalneeds()).isEqualTo(testDataRequest.getAdditionalneeds());
        });
    }

    @Test
    @DisplayName("Unsuccessful updates a current booking")
    public void unsuccessfulUpdateBookingById405Test() {
        GetBookingRequest testData = createBooking.successfulDataForCreateBooking();
        step("Отправить запрос на удаление конкретного бронирования по идентификатору бронирования", () ->
                given(requestSpecificationWithAuth)
                        .body(testData)
                        .when()
                        .put("/booking/" + -1)
                        .then()
                        .spec(responseSpecification405));
    }

    @Test
    @DisplayName("Unsuccessful updates a current booking")
    public void unsuccessfulUpdateBookingById403Test() {
        GetBookingRequest testData = createBooking.successfulDataForCreateBooking();
        step("Отправить запрос на удаление конкретного бронирования по идентификатору бронирования", () ->
                given(requestSpecificationWithoutAuth)
                        .body(testData)
                        .when()
                        .put("/booking/" + 324)
                        .then()
                        .spec(responseSpecification403));
    }
}