package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {


    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> mergedFlux = Flux.just("A","B","C","D","E","F");
        //Flux<String> flux2 = Flux.just("D","E","F");
        //Flux<String> mergedFlux = flux1.mergeWith(flux2).delayElements(Duration.ofSeconds(1));


        mergedFlux.subscribe(s -> System.out.println("Subscriber 1 : " + s));

        Thread.sleep(10000);

        mergedFlux.subscribe(s -> System.out.println("Subscriber 2 : " +s ));

        Thread.sleep(5000);
    }

        @Test
        public void hotPublisherTest() throws InterruptedException {
        Flux<String> mergedFlux = Flux.just("A","B","C","D","E","F");

            ConnectableFlux<String> connectableFlux = mergedFlux.publish();
            connectableFlux.connect();

            connectableFlux.subscribe(s -> System.out.println("sub1 :" + s));
            Thread.sleep(3000);
            connectableFlux.subscribe(s -> System.out.println("sub2 :" + s)); // does not emit the values from
            // beginning.

            Thread.sleep(4000);
    }
}
