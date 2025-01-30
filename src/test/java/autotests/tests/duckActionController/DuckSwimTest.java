package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.DuckSwim;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends DuckActionsClient {

    @Flaky
    @Test(description = " утка с существующим id плавает")
    @CitrusTest
    public void swimExistingDuck(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId",randomId());
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'rubber', 'quack','ACTIVE');");
        DuckSwim swim = new DuckSwim().message("I'm swimming");
        duckSwim(runner, "${duckId}");
        validateResponse(runner, swim, HttpStatus.OK);
    }

    @Flaky
    @Test(description = " утка с несуществующим id плавает")
    @CitrusTest
    public void swimNonexistentDuck(@Optional @CitrusResource TestCaseRunner runner) {
        duckSwim(runner, "0");
        validateResponse(runner, "{\n" + "  \"message\": \"@ignore@\"\n" + "}", HttpStatus.BAD_REQUEST);
    }
}
