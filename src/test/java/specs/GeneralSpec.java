package specs;

import helpers.CustomAllureListener;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class GeneralSpec {
    public static final RequestSpecification requestSpecificationWithAuth = with()
            .filter(CustomAllureListener.withCustomTemplates())
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
            .log().all()
            .contentType(JSON);

    public static final RequestSpecification requestSpecificationWithoutAuth = with()
            .filter(CustomAllureListener.withCustomTemplates())
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .log().all()
            .contentType(JSON);

    public static final ResponseSpecification responseSpecification200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseSpecification201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseSpecification403 = new ResponseSpecBuilder()
            .expectStatusCode(403)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseSpecification404 = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseSpecification405 = new ResponseSpecBuilder()
            .expectStatusCode(405)
            .log(ALL)
            .build();

    public static final ResponseSpecification responseSpecification500 = new ResponseSpecBuilder()
            .expectStatusCode(500)
            .log(ALL)
            .build();

}