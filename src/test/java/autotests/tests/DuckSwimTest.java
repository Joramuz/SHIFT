package autotests.tests;

import autotests.clients.DuckActionsClient;
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
}
