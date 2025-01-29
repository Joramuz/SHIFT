package autotests.tests;

import autotests.payloads.DuckCreate;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;
import java.util.concurrent.atomic.AtomicInteger;

public class DuckDeleteTest extends DuckActionsClient {

    @Test(description = "Удаление существующей утки ")
    @CitrusTest
    public void deleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        extractor(runner);
        runner.$(a -> {
            id.set(Integer.parseInt(a.getVariable("duckId")));
        });
        delete(runner, "${duckId}");
        validateResponseJson(runner, "duckDeleteTest/successfulDelete.json", HttpStatus.OK);
        String ducksIds = extractAllIds(runner);
        if (ducksIds.contains(Integer.toString(id.get()))) throw new AssertionError("Duck has not been deleted");
    }
}
