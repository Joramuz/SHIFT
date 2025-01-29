package autotests.tests;

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
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        extractor(runner);
        runner.$(a -> {
            id.set(Integer.parseInt(a.getVariable("duckId")));
        });
        delete(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck is deleted\"\n" + "}", HttpStatus.OK);
        String ducksIds = extractAllIds(runner);
        if (ducksIds.contains(Integer.toString(id.get()))) throw new AssertionError("Duck has not been deleted");
    }
}
