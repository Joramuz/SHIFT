package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.DuckFly;
import autotests.payloads.WingsState;
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
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        extractor(runner);
        DuckFly fly = new DuckFly().message("I'm flying");
        duckFly(runner, "${duckId}");
        validateResponse(runner, fly, HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void flyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.FIXED);
        createDuck(runner, duck);
        extractor(runner);
        duckFly(runner, "${duckId}");
        validateResponseJson(runner, "duckFlyTest/fixedWingsTest.json", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "Существующий id с крыльями в неопределенном состоянии ")
    @CitrusTest
    public void flyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.UNDEFINED);
        createDuck(runner, duck);
        extractor(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"@ignore@\"\n" + "}", HttpStatus.BAD_REQUEST);
        delete(runner, "${duckId}");
    }
}
