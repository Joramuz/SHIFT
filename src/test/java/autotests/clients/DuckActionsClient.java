package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.DuckUpdate;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;
    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Метод получения id уточки")
    public void extractor(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    @Step("Метод получения свойств уточки")
    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    @Step("Метод проверки ответа")
    public void validateResponseJson(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
                .body(new ClassPathResource(responseMessage)));
    }

    @Step("Метод проверки ответа")
    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    @Step("Метод проверки ответа")
    public void validateResponse(TestCaseRunner runner, Object body, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Метод проверки ответа и получения id")
    public void validateResponseWithExtractId(TestCaseRunner runner, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    @Step("Метод проверки ответа и получения id")
    public void validateResponseWithExtractId(TestCaseRunner runner, Object body, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Метод создания уточки")
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

    @Step("Метод создания уточки")
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Метод удаления уточки")
    public void delete(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    @Step("Метод 'кря'")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    @Step("Метод 'лететь'")
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    @Step("Метод 'плыть'")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    @Step("Метод обновления свойств уточки")
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

    @Step("Метод обновления свойств уточки")
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

    @Step("Метод получения и записи всех id")
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

    @Step("Метод получения всех id")
    public void getAllIds(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    @Step("Метод изменения базы данных")
    public void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(
                sql(testDb)
                        .statement(sql));
    }

    @Step("Метод проверки свойств в базе данных")
    public void validateDuckInDatabase(TestCaseRunner runner, String id, String color, String height,
                                       String material, String sound, String wingsState) {
        runner.$(
                query(testDb)
                        .statement("SELECT * FROM DUCK WHERE ID=" + id)
                        .validate("COLOR", color)
                        .validate("HEIGHT", height)
                        .validate("MATERIAL", material)
                        .validate("SOUND", sound)
                        .validate("WINGS_STATE", wingsState));
    }

    @Step("Метод получения рандомного id")
    public String randomId(int value) {
        Random random = new Random();
        int randomInt;
        do {
            randomInt = random.nextInt();
        } while (randomInt % 2 != value);
        String id = Integer.toString(randomInt);
        return id;
    }

    @Step("Метод получения рандомного id")
    public String randomId() {
        Random random = new Random();
        int randomInt = random.nextInt();
        String id = Integer.toString(randomInt);
        return id;
    }
}
