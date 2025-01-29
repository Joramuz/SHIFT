package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.DuckUpdate;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;

    public void extractor(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void validateResponseJson(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
                .body(new ClassPathResource(responseMessage)));
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    public void validateResponse(TestCaseRunner runner, Object body, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    public void validateResponseWithExtractId(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    public void validateResponseWithExtractId(TestCaseRunner runner, Object body, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
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

    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    public void delete(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    public void duckUpdate(TestCaseRunner runner, String id, DuckUpdate body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", body.color())
                .queryParam("height", Double.toString(body.height()))
                .queryParam("id", id)
                .queryParam("material", body.material())
                .queryParam("sound", body.sound()));
    }

    public void duckUpdate(TestCaseRunner runner, String id, double height, String color, String material, String sound) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", Double.toString(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound));
    }

    public String extractAllIds(TestCaseRunner runner) {
        final String[] ducksIds = {""};
        getAllIds(runner);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$", "body")));
        runner.$(a -> {
            ducksIds[0] = (a.getVariable("body"));
        });
        return ducksIds[0];
    }

    public void getAllIds(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/getAllIds"));
    }
}
