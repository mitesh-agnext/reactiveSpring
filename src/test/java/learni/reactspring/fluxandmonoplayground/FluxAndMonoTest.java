package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred"))).log();
        stringFlux.subscribe(System.out::println, System.err::println);
    }

    @Test
    public void fluxTestElements_WithoutError() {

        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring").log();
        Flux<String> exceptionFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occured"))).log();

        StepVerifier.create(stringFlux).expectNext("Spring").expectNext("Spring Boot").expectNext("Reactive Spring")
                .verifyComplete();

        StepVerifier.create(exceptionFlux).expectNextCount(3)
                .expectError(RuntimeException.class).verify();

    }


    @Test
    public void monoTest(){
        Mono<String> stringMono = Mono.just("Spring");
        StepVerifier.create(stringMono.log()).expectNext("Spring").verifyComplete();

    }


    @Test
    public void monoTest_Error(){
        Mono<String> stringMono = Mono.error(new RuntimeException("Exception Occurred"));
        StepVerifier.create(stringMono.log()).verifyError();

    }
}


