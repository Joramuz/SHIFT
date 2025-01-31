package autotests;

import autotests.payloads.DuckUpdate;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient yellowDuckService;
    @Autowired
    protected SingleConnectionDataSource testDb;

    protected void sendGetRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(
                http()
                        .client(URL)
                        .send()
                        .get(path));
    }

    protected void sendPostRequest(TestCaseRunner runner, HttpClient URL, String path, Object body) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, HttpClient URL, String path, String id) {
        runner.$(http().client(URL)
                .send()
                .delete(path)
                .queryParam("id", id));
    }

    protected void sendPutRequest(TestCaseRunner runner, HttpClient URL, String path, DuckUpdate body, String id) {
        runner.$(http().client(URL)
                .send()
                .put(path)
                .queryParam("color", body.color())
                .queryParam("height", Double.toString(body.height()))
                .queryParam("id", id)
                .queryParam("material", body.material())
                .queryParam("sound", body.sound()));
    }

    protected void sendPutRequest(TestCaseRunner runner, HttpClient URL, String path, String id, double height, String color, String material, String sound) {
        runner.$(http().client(URL)
                .send()
                .put(path)
                .queryParam("color", color)
                .queryParam("height", Double.toString(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound));
    }

    protected void responseValidation(TestCaseRunner runner, HttpClient URL, Object body, HttpStatus httpStatus) {
        runner.$(http().client(URL)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    protected void responseValidation(TestCaseRunner runner, HttpClient URL, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    protected void responseValidationJson(TestCaseRunner runner, HttpClient URL, String responseMessage, HttpStatus httpStatus) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
                .body(new ClassPathResource(responseMessage)));
    }
}
