package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void fluxUsingIterable() {
        Flux<String> stringFlux = Flux.fromIterable(names);
        StepVerifier.create(stringFlux).expectNext("adam", "anna", "jack", "jenny").verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] name = new String[] { "adam", "anna", "jack", "jenny" };
        Flux<String> stringFlux = Flux.fromArray(name);
        StepVerifier.create(stringFlux).expectNext("adam", "anna", "jack", "jenny").verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> nameFlux = Flux.fromStream(names.stream());
        StepVerifier.create(nameFlux).expectNext("adam", "anna", "jack", "jenny").verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log()).verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Supplier<String> stringSupplier = () ->  "adam";
        Mono<String> stringMonog = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(stringMonog.log()).expectNext("adam").verifyComplete();
    }

    @Test
    public void fluxUsingRange(){
        Flux<Integer> integerFlux = Flux.range(1, 5);
    }
}
