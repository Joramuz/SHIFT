package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.DuckUpdate;
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
import java.util.concurrent.atomic.AtomicInteger;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends DuckActionsClient {

    @Test(description = "меняем цвет и высоту утки ")
    @CitrusTest
    public void updateColorAndHeightDuck(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        int count;
        do{
            id.set(Integer.parseInt(randomId()));
            count = countIdInDB(runner,id);
        } while (count !=0);
        runner.variable("duckId",id);
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'green', 0.15, 'rubber', 'quack','ACTIVE');");
        DuckUpdate duckUpdate = new DuckUpdate().color("yellow").height(0.1).material("rubber").sound("quack");
        duckUpdate(runner, "${duckId}", duckUpdate);
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = " + Integer.toString(id.get()) + " is updated\"\n" + "}", HttpStatus.OK);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.1", "rubber", "quack", "ACTIVE");
    }

    @Flaky
    @Test(description = "меняем цвет и звук утки ")
    @CitrusTest
    public void updateColorAndSoundDuck(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        int count;
        do{
            id.set(Integer.parseInt(randomId()));
            count = countIdInDB(runner,id);
        } while (count !=0);
        runner.variable("duckId",id);
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'rubber', 'quack','ACTIVE');");
        duckUpdate(runner, "${duckId}", 0.15, "green", "rubber", "meow");
        validateResponse(runner, "{\n" + "@ignore@" + "}", HttpStatus.BAD_REQUEST);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "0.15", "rubber", "quack", "ACTIVE");
    }
}
