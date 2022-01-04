package learni.reactspring.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorServiceTest = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.namesFlux();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("alex", "ben", "chloe").verifyComplete();
    }

    @Test
    void namesMono() {
        Mono<String> stringMono = fluxAndMonoGeneratorServiceTest.namesMono();
        StepVerifier.create(stringMono).expectSubscription().expectNext("alex").verifyComplete();
    }

    @Test
    void nameFlux_map() {
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.nameFlux_map();
        Flux<String> stringFlux1 = fluxAndMonoGeneratorServiceTest.namesFlux();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("ALEX","BEN","CHLOE").verifyComplete();
        StepVerifier.create(stringFlux1).expectSubscription().expectNext("alex", "ben", "chloe").verifyComplete();
    }

    @Test
    void namesFlux_flatMap_concatMap() {
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.namesFlux_flatMap_concatMap(3);
        StepVerifier.create(stringFlux).expectSubscription().expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        List<String> stList = Arrays.asList("A", "L", "E", "X");
        Mono<List<String>> listMono =
                fluxAndMonoGeneratorServiceTest.namesMono_flatMap(3);
        StepVerifier.create(listMono).expectSubscription().expectNext(stList).verifyComplete();
    }

    @Test
    void namesMono_flatMap_Many() {
        List<String> stList = Arrays.asList("A", "L", "E", "X");

        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.namesMono_flatMapMany(3);
        StepVerifier.create(stringFlux).expectSubscription().expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void exploreConcat(){
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.explore_concat();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("A","B","C","D","E","F").verifyComplete();
    }

    @Test
    void exploreConcatWithMono(){
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.explore_concatWith_Mono();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("A","D").verifyComplete();
    }

    @Test
    void exploreMergeWithMono(){
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.explore_MergeWith_Mono();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("A","D").verifyComplete();
    }

    @Test
    void exploreMergeWIthFlux(){
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.explore_Merge();
        StepVerifier.create(stringFlux.log()).expectSubscription().expectNext("A","D","B","E","C","F").verifyComplete();
    }

    @Test
    void exploreMergeSequentialWIthFlux(){
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.explore_MergeSequential();
        StepVerifier.create(stringFlux.log()).expectSubscription().expectNext("A","B","C","D","E","F").verifyComplete();
    }

    @Test
    void exploreZip(){
        Flux<String> stringFlux = fluxAndMonoGeneratorServiceTest.explore_Zip();
        StepVerifier.create(stringFlux.log()).expectSubscription().expectNext("AD","BE","CF").verifyComplete();
    }
}
