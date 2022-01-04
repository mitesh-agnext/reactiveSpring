package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void testTransformUsingMap(){
        Flux<String> namesFlux = Flux.fromIterable(names);
        Flux<String> upperCaseFlux = namesFlux.map(str -> str.toUpperCase()).log();

        StepVerifier.create(upperCaseFlux).expectNext("ADAM", "ANNA", "JACK", "JENNY").verifyComplete();
    }


    //Operation needs to be executed on every element and it is returing flux of another elements.
    @Test
    public void testTransforUsingFlatMap(){
        Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")).
                flatMap(s ->{
                    return Flux.fromIterable(convertToList(s));
        }).log();

        StepVerifier.create(namesFlux).expectNextCount(12).verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"newValue");

    }

    @Test
    public void testTransforUsingFlatMapUsingParaller(){
        Flux<String> namesFlux =
                Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")).window(2).flatMap( (str  -> {
                    return str.map(this::convertToList).subscribeOn(parallel());
                })).flatMap(fluxList -> Flux.fromIterable(fluxList)).log();
        StepVerifier.create(namesFlux).expectNextCount(12).verifyComplete();
    }


    @Test
    public void testTransforUsingFlatMapUsingParallel_maintain_order_wtihConcatMap(){
        Flux<String> namesFlux =
                Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")).window(2).concatMap( (str  -> {
                    return str.map(this::convertToList).subscribeOn(parallel());
                })).flatMap(fluxList -> Flux.fromIterable(fluxList)).log();
        StepVerifier.create(namesFlux).expectNextCount(12).verifyComplete();
    }


    @Test
    public void testTransforUsingFlatMapUsingParallel_maintain_order_wtihFlatMapSequential(){
        Flux<String> namesFlux =
                Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")).window(2).flatMapSequential( (str  -> {
                    return str.map(this::convertToList).subscribeOn(parallel());
                })).flatMap(fluxList -> Flux.fromIterable(fluxList)).log();
        StepVerifier.create(namesFlux).expectNextCount(12).verifyComplete();
    }


}



