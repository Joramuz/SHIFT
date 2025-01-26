package autotests.tests;
import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;



public class DuckUpdateTest extends DuckActionsClient {

    @Test(description = "Изменяем цвет и высоту утки ")
    @CitrusTest
    public void updateColorAndHeightDuck(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        createDuck(runner, "green", 0.15, "rubber", "quack", "ACTIVE");
        extractor(runner);
        runner.$(a -> {
            id.set(Integer.parseInt(a.getVariable("duckId")));
        });
        duckUpdate(runner, "${duckId}", "0.1", "yellow", "rubber", "quack");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = " +Integer.toString(id.get())+" is updated\"\n" + "}", HttpStatus.OK);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.1 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }

    @Test(description = "Изменяем цвет и звук утки ")
    @CitrusTest
    public void updateColorAndSoundDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        extractor(runner);
        duckUpdate(runner, "${duckId}", "0.15", "green", "rubber", "meow");
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
