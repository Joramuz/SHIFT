package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import io.qameta.allure.Flaky;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTest extends DuckActionsClient {

    DuckCreate duck1 = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
    DuckCreate duck2 = new DuckCreate().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingsState.ACTIVE);
    DuckCreate duck3 = new DuckCreate().color("green").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
    DuckCreate duck4 = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("meow").wingsState(WingsState.ACTIVE);
    DuckCreate duck5 = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.FIXED);

    @Flaky
    @Test(description = "Создание утки с material = rubber ", dataProvider = "duckList")
    @CitrusTest
    @CitrusParameters({"payload", "response", "runner"})
    public void createMaterialRubber(DuckCreate payload, DuckCreate response, @Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        createDuck(runner, payload);
        validateResponseWithExtractId(runner, response, HttpStatus.OK);
        validateDuckInDatabase(runner, "${duckId}", payload.color(), Double.toString(payload.height()), payload.material(), payload.sound(), payload.wingsState().toString());
    }

    @DataProvider(name = "duckList")
    public Object[][] DuckProvider() {
        return new Object[][]{
                {duck1, duck1, null},
                {duck2, duck2, null},
                {duck3, duck3, null},
                {duck4, duck4, null},
                {duck5, duck5, null},
        };
    }
}
