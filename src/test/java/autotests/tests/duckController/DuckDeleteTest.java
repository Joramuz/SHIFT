package autotests.tests.duckController;

import autotests.payloads.DuckCreate;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;
import java.util.concurrent.atomic.AtomicInteger;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/delete")
public class DuckDeleteTest extends DuckActionsClient {

    @Test(description = "Удаление существующей утки ")
    @CitrusTest
    public void deleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        runner.variable("duckId",randomId());
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'rubber', 'quack','ACTIVE');");
        runner.$(a -> {
            id.set(Integer.parseInt(a.getVariable("duckId")));
        });
        delete(runner, "${duckId}");
        validateResponseJson(runner, "duckDeleteTest/successfulDelete.json", HttpStatus.OK);
        String ducksIds = extractAllIds(runner);
        if (ducksIds.contains(Integer.toString(id.get()))) throw new AssertionError("Duck has not been deleted");
    }
}
