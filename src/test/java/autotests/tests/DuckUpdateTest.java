package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DuckCreate;
import autotests.payloads.DuckUpdate;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;


public class DuckUpdateTest extends DuckActionsClient {

    @Test(description = "меняем цвет и высоту утки ")
    @CitrusTest
    public void updateColorAndHeightDuck(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        DuckCreate duck = new DuckCreate().color("green").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        extractor(runner);
        runner.$(a -> {
            id.set(Integer.parseInt(a.getVariable("duckId")));
        });
        DuckUpdate duckUpdate = new DuckUpdate().color("yellow").height(0.1).material("rubber").sound("quack");
        duckUpdate(runner, "${duckId}", duckUpdate);
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = " + Integer.toString(id.get()) + " is updated\"\n" + "}", HttpStatus.OK);
        DuckUpdate duckUpdated = new DuckUpdate().color("yellow").height(0.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, duckUpdated, HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "меняем цвет и звук утки ")
    @CitrusTest
    public void updateColorAndSoundDuck(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duck = new DuckCreate().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        extractor(runner);
        duckUpdate(runner, "${duckId}", 0.15, "green", "rubber", "meow");
        validateResponse(runner, "{\n" + "@ignore@" + "}", HttpStatus.BAD_REQUEST);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "FIXED"
                + "\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

}
