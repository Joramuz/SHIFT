package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreateTest extends TestNGCitrusSpringSupport {
    @Test(description = "Создание утки с material = rubber ")
    @CitrusTest
    public void createMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        validateResponseCreate(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
        duckProperties(runner, "${duckId}");
        validateResponseProperties(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
    }

    @Test(description = "Создание утки с material = wood ")
    @CitrusTest
    public void createMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
        validateResponseCreate(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "wood" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
        duckProperties(runner, "${duckId}");
        validateResponseProperties(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "wood" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
    }

    public void validateResponseCreate(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(httpStatus)
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    public void validateResponseProperties(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + color + "\",\n"
                        + "  \"height\": " + height + ",\n"
                        + "  \"material\": \"" + material + "\",\n"
                        + "  \"sound\": \"" + sound + "\",\n"
                        + "  \"wingsState\": \"" + wingsState
                        + "\"\n" + "}"));
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

}
