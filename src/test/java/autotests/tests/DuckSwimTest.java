package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.DuckSwim;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckSwimTest extends DuckActionsClient {

    @Test(description = " утка с существующим id плавает")
    @CitrusTest
    public void swimExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        extractor(runner);
        DuckSwim swim = new DuckSwim().message("I'm swimming");
        duckSwim(runner, "${duckId}");
        validateResponse(runner, swim, HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = " утка с несуществующим id плавает")
    @CitrusTest
    public void swimNonexistentDuck(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        extractor(runner);
        delete(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"@ignore@\"\n" + "}", HttpStatus.BAD_REQUEST);
    }

}
