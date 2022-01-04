package learni.reactspring.controller.v1;

import learni.reactspring.ItemReactiveRepository;
import learni.reactspring.constants.ItemConstants;
import learni.reactspring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @BeforeEach
    public void setup() {
        itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(data())).flatMap(itemReactiveRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted item is : " + item);
                })).blockLast();

    }

    public List<Item> data() {
        Item item1 = new Item(null, "Samsung TV", new BigDecimal("300"));
        Item item2 = new Item(null, "LG TV", new BigDecimal("400"));
        Item item3 = new Item(null, "Apple HeadPhones", new BigDecimal("200"));
        Item item4 = new Item(null, "Apple Watch", new BigDecimal("100"));
        Item item5 = new Item(null, "Google Home Mini", new BigDecimal("100"));
        Item item6 = new Item("ABC", "Bose Headphone", new BigDecimal("230"));
        return Arrays.asList(item1, item2, item3, item4, item5, item6);
    }

    @Test
    public void getAllItemsTest() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1).exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON).expectBodyList(Item.class).hasSize(6);
    }

    @Test
    public void getAllItemsTest_approach3() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1).exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON).expectBodyList(Item.class).hasSize(6).consumeWith(response -> {
                    List<Item> itemsList = response.getResponseBody();
                    itemsList.forEach(item -> assertTrue(item.getId() != null));
                });
    }

    @Test
    public void getAllItemsTestApproach2() {
        Flux<Item> itemsFlux = webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).returnResult(Item.class).getResponseBody();

        StepVerifier.create(itemsFlux).expectSubscription().expectNextCount(6).verifyComplete();
    }

    @Test
    public void getItem() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody()
                .jsonPath("$.price", new BigDecimal("230"));
    }

    @Test
    public void getItem_NOT_FOUND() {
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "DEF")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNotFound();

    }

    @Test
    public void createItem() {
        Item item = new Item(null, "Pixel 4a", new BigDecimal("199.9"));

        webTestClient.post().uri(ItemConstants.ITEM_END_POINT_V1).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class).exchange().expectStatus().isCreated().expectBody().jsonPath("$.id")
                .isNotEmpty().jsonPath("$.description").isEqualTo("Pixel 4a").jsonPath("$.price")
                .isEqualTo(new BigDecimal("199.9"));
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody(Void.class);
    }

    @Test
    public void updateItemPrice() {
        Item item = new Item(null, "Pixel 4a", new BigDecimal("199.9"));
        webTestClient.put().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class).exchange().expectStatus().isOk().expectBody().jsonPath("$.price",
                        new BigDecimal("199.9"));
    }


    @Test
    public void updateItemWithInvalidID() {
        Item item = new Item(null, "Pixel 4a", new BigDecimal("199.9"));
        webTestClient.put().uri(ItemConstants.ITEM_END_POINT_V1.concat("/{id}"), "DEF")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class).exchange().expectStatus().isNotFound();
    }
}
