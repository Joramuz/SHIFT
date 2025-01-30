package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.DuckFly;
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
@Feature("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends DuckActionsClient {

    @Flaky
    @Test(description = "Существующий id с активными крыльями ")
    @CitrusTest
    public void flyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId",randomId());
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'wood', 'quack','ACTIVE');");
        DuckFly fly = new DuckFly().message("I'm flying");
        duckFly(runner, "${duckId}");
        validateResponse(runner, fly, HttpStatus.OK);
    }

    @Flaky
    @Test(description = "Существующий id со связанными крыльями")
    @CitrusTest
    public void flyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId",randomId());
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'wood', 'quack','FIXED');");
        duckFly(runner, "${duckId}");
        validateResponseJson(runner, "duckFlyTest/fixedWingsTest.json", HttpStatus.OK);
    }

    @Flaky
    @Test(description = "Существующий id с крыльями в неопределенном состоянии ")
    @CitrusTest
    public void flyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId",randomId());
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'wood', 'quack','UNDEFINED');");
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\n" + "  \"message\": \"@ignore@\"\n" + "}", HttpStatus.BAD_REQUEST);
    }
}
