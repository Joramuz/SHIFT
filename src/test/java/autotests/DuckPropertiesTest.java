package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckPropertiesTest extends TestNGCitrusSpringSupport {
    @Test(description = " ID - целое четное число. Материал wood ")
    @CitrusTest
    public void propertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        do {
            createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
            extractor(runner);
            runner.$(a -> {
                id.set(Integer.parseInt(a.getVariable("duckId")));
            });

        } while (id.get() % 2 != 0);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "FIXED"
                + "\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "ID - целое нечетное число. Материал rubber ")
    @CitrusTest
    public void propertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        do {
            createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
            extractor(runner);
            runner.$(a -> {
                id.set(Integer.parseInt(a.getVariable("duckId")));
            });

        } while (id.get() % 2 != 1);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "FIXED"
                + "\"\n" + "}", HttpStatus.OK);
        delete(runner,"${duckId}" );
    }

    public static void extractor(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
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

    public void delete(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }
}