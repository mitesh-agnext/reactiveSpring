package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandline(){

        Flux<String> stringFlux = Flux.just("A","B","C").concatWith(Flux.error(new RuntimeException("Exception "
                + "Occurend"))).concatWith(Flux.just("D")).onErrorResume( (e) -> {
            System.out.println("Exception is :" + e);
            return Flux.just("default","default1");
        });

        StepVerifier.create(stringFlux.log()).expectSubscription().expectNext("A","B","C")
                //.expectError(RuntimeException.class)
                .expectNext("default","default1")
                .verifyComplete();


    }
}
