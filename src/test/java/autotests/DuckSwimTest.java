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

public class DuckSwimTest extends TestNGCitrusSpringSupport {
    @Test(description = " утка с существующим id плавает")
    @CitrusTest
    public void swimExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        extractor(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I'm swimming\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }


    @Test(description = " утка с несуществующим id плавает")
    @CitrusTest
   public void swimNonexistentDuck(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "0");
        validateResponse(runner, "{\n" + "  \"message\": \"@ignore@\"\n" + "}", HttpStatus.BAD_REQUEST);
    }

    public static void extractor(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/swim")
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

    public void delete (TestCaseRunner runner, String id){
        runner.$(http().client("http://localhost:2222")
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }
}
