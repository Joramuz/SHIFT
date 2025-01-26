package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckCreateTest extends DuckActionsClient {

    @Test(description = "Создание утки с material = rubber ")
    @CitrusTest
    public void createMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        validateResponseWithExtractId(runner, duck, HttpStatus.OK);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, duck, HttpStatus.OK);
    }

    @Test(description = "Создание утки с material = wood ")
    @CitrusTest
    public void createMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        validateResponseWithExtractId(runner, duck, HttpStatus.OK);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, duck, HttpStatus.OK);
    }

}
