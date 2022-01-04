package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> mergedFlux = flux1.mergeWith(flux2);

        StepVerifier.create(mergedFlux.log()).expectSubscription().expectNext("A","B","C","D","E","F").verifyComplete();


    }

    @Test
    public void combineUsingMerge_withDelay(){
        VirtualTimeScheduler.getOrSet();
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> mergedFlux = flux1.mergeWith(flux2);

        StepVerifier.withVirtualTime(() -> mergedFlux.log()).expectSubscription().thenAwait(Duration.ofSeconds(1)).expectNextCount(6).verifyComplete();
/*
        StepVerifier.create(mergedFlux.log()).expectSubscription().expectNextCount(6)
                //.expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();*/

    }

    @Test
    public void combineUsingConCat(){
        VirtualTimeScheduler.getOrSet();
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergedFlux = Flux.concat(flux1,flux2);


        StepVerifier.withVirtualTime(() -> mergedFlux.log()).expectSubscription().thenAwait(Duration.ofSeconds(1))
               // .expectNextCount(6)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingZip(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
        Flux<String> mergedFlux = Flux.zip(flux1,flux2,(t1,t2) -> t1.concat(t2));

        StepVerifier.create(mergedFlux.log()).expectSubscription()
                // .expectNextCount(6)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }
}
