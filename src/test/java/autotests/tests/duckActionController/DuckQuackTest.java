package autotests.tests.duckActionController;

import autotests.payloads.DuckCreate;
import autotests.payloads.DuckQuack;
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
import autotests.clients.DuckActionsClient;
import java.util.concurrent.atomic.AtomicInteger;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/quack")
public class DuckQuackTest extends DuckActionsClient {

    @Flaky
    @Test(description = "Корректный чётный id , корректный звук ")
    @CitrusTest
    public void quackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        int count;
        do{
            id.set(Integer.parseInt(randomId(0)));
            count = countIdInDB(runner,id);
        } while (count !=0);
        runner.variable("duckId",id);;
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'rubber', 'quack','ACTIVE');");
        duckQuack(runner, "${duckId}", "1", "2");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack-quack\"\n" + "}", HttpStatus.OK);
    }

    @Test(description = "Корректный нечётный id, корректный звук ")
    @CitrusTest
    public void quackNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        int count;
        do{
            id.set(Integer.parseInt(randomId(1)));
            count = countIdInDB(runner,id);
        } while (count !=0);
        runner.variable("duckId",id);
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'rubber', 'quack','ACTIVE');");
        DuckQuack quack = new DuckQuack().sound("quack");
        duckQuack(runner, "${duckId}", "1", "1");
        validateResponse(runner, quack, HttpStatus.OK);
    }
}