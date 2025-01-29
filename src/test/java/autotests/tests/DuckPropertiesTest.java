package autotests.tests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;

import java.util.concurrent.atomic.AtomicInteger;

public class DuckPropertiesTest extends DuckActionsClient {

    @Test(description = " ID - целое четное число. Материал wood ")
    @CitrusTest
    public void propertiesEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        do {
            createDuck(runner, "yellow", 0.15, "wood", "quack", "FIXED");
            extractor(runner);
            runner.$(a -> {
                id.set(Integer.parseInt(a.getVariable("duckId")));
            });
        } while (id.get() % 2 != 0);
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

    @Test(description = "ID - целое нечетное число. Материал rubber ")
    @CitrusTest
    public void propertiesNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        do {
            createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
            extractor(runner);
            runner.$(a -> {
                id.set(Integer.parseInt(a.getVariable("duckId")));
            });
        } while (id.get() % 2 != 1);
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 0.15 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "FIXED"
                + "\"\n" + "}", HttpStatus.OK);
        delete(runner,"${duckId}" );
    }
}