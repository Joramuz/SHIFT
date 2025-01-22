package autotests.tests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import autotests.clients.DuckActionsClient;
import java.util.concurrent.atomic.AtomicInteger;

public class DuckQuackTest extends DuckActionsClient {

    @Test(description = "Корректный чётный id , корректный звук ")
    @CitrusTest
    public void quackEvenId(@Optional @CitrusResource TestCaseRunner runner) {

        AtomicInteger id = new AtomicInteger();

        while (true) {
            createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
            extractor(runner);
            runner.$(a -> {
                id.set(Integer.parseInt(a.getVariable("duckId")));
            });

            if (id.get() % 2 == 0)
                break;
        }

        duckQuack(runner, "${duckId}","1","2");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack-quack\"\n" + "}", HttpStatus.OK);
        delete(runner, "${duckId}");
    }
    @Test(description = "Корректный нечётный id, корректный звук ")
    @CitrusTest
    public void quackNotEvenId(@Optional @CitrusResource TestCaseRunner runner) {

        AtomicInteger id = new AtomicInteger();

        while (true) {
            createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
            extractor(runner);
            runner.$(a -> {
                id.set(Integer.parseInt(a.getVariable("duckId")));
            });

            if (id.get() % 2 == 1)
                break;
        }

        duckQuack(runner, "${duckId}","1","1");
        validateResponse(runner, "{\n" + "  \"sound\": \"quack\"\n" + "}", HttpStatus.OK);
        delete(runner,"${duckId}" );
    }

}