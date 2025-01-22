package autotests.tests;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;

import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckFlyTest extends DuckActionsClient {

    @Test(description = "Существующий id с активными крыльями ")
    @CitrusTest
    public void flyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        extractor(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I am flying :)\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void flyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        extractor(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"I can not fly :C\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии ")
    @CitrusTest
    public void flyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "UNDEFINED");
        extractor(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"Wings are not detected :(\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

}
