package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
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
import io.qameta.allure.Flaky;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTest extends DuckActionsClient {

    @Flaky
    @Test(description = "Создание утки с material = rubber ")
    @CitrusTest
    public void createMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        validateResponseWithExtractId(runner, duck, HttpStatus.OK);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.15", "rubber", "quack", "ACTIVE");
    }

    @Flaky
    @Test(description = "Создание утки с material = wood ")
    @CitrusTest
    public void createMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        validateResponseWithExtractId(runner, duck, HttpStatus.OK);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.15", "wood", "quack", "ACTIVE");
    }
}
