package learni.reactspring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonotFilterTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest(){
        Flux<String> nameFlux = Flux.fromIterable(names).filter(str -> str.startsWith("a")).log();

        StepVerifier.create(nameFlux).expectNext("adam","anna").verifyComplete();

    }
}
