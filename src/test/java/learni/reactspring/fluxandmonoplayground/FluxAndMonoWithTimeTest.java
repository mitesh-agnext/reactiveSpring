package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {


    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)).log();

        infiniteFlux.subscribe((e) -> System.out.println("Value is : " + e));

        Thread.sleep(20000);

    }


    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200)).take(3).log();

        /*infiniteFlux.subscribe((e) -> System.out.println("Value is : " + e));

        Thread.sleep(20000);*/

        StepVerifier.create(finiteFlux).expectSubscription().expectNext(0l, 1l, 2l).verifyComplete();

    }
}
