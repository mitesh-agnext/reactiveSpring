package learni.reactspring.controller;

import learni.reactspring.model.TestModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux.just(1, 2, 3, 4);
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFluxStream() {
        return Flux.just(1, 2, 3, 4);
    }

    @GetMapping(value = "/fluxnonstopstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnFluxNonStopStream() {
        return Flux.interval(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/mono")
    public Mono<Integer> getMono() {
        return Mono.just(1).log();
    }

    @PostMapping("/testMapping")
    public TestModel TestMapping(@RequestBody TestModel mytestModel){
        return  mytestModel;
    }

}
