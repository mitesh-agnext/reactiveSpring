package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux() {
        webTestClient.get().uri("/flux").exchange().expectStatus().isOk().expectBodyList(Integer.class).hasSize(3);
    }

    @Test
    void flux_2(){
        Flux<Integer> flux = webTestClient.get().uri("/flux").exchange().expectStatus().is2xxSuccessful()
                .returnResult(Integer.class).getResponseBody();
        StepVerifier.create(flux).expectSubscription().expectNext(1,2,3).verifyComplete();
    }


    @Test
    void flux_3(){
        webTestClient.get().uri("/flux").exchange().expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class).consumeWith(listEntityExchangeResult -> {
                    List<Integer> responseBody = listEntityExchangeResult.getResponseBody();
                    assert (Objects.requireNonNull(responseBody).size() == 3);
                });
    }

    @Test
    void helloWorldMono() {
        webTestClient.get().uri("/mono").exchange().expectStatus().is2xxSuccessful()
                .expectBody(String.class).consumeWith(str -> {
                        String testString = str.getResponseBody();
                    assert testString.equalsIgnoreCase("Hello World");
                });
    }

    @Test
    void stream() {
        Flux<Long> flux = webTestClient.get().uri("/stream").exchange().expectStatus().is2xxSuccessful()
                .returnResult(Long.class).getResponseBody();

        StepVerifier.create(flux).expectNext(0L,1L,2L,3L).thenCancel().verify();
    }

}
