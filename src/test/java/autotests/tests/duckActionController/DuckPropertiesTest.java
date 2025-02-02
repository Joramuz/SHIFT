package autotests.tests.duckActionController;

import autotests.payloads.DuckCreate;
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
@Feature("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends DuckActionsClient {

    @Flaky
    @Test(description = " ID - целое четное число. Материал wood ")
    @CitrusTest
    public void propertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        int count;
        do{
            id.set(Integer.parseInt(randomId(0)));
            count = countIdInDB(runner,id);
        } while (count !=0);
        runner.variable("duckId",id);
        runner.$(
                doFinally().actions(context ->
                        databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        databaseUpdate(runner,
                "insert into DUCK (id, color, height, material, sound, wings_state)\n" +
                        "values (${duckId}, 'yellow', 0.15, 'wood', 'quack','ACTIVE');");
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "wood" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
    }

    @Flaky
    @Test(description = "ID - целое нечетное число. Материал rubber ")
    @CitrusTest
    public void propertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {
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
                        "values (${duckId}, 'yellow', 0.15, 'wood', 'quack','ACTIVE');");
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "wood" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
    }
}