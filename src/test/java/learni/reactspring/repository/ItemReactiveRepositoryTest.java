package learni.reactspring.repository;

import learni.reactspring.ItemReactiveRepository;
import learni.reactspring.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DirtiesContext
class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", BigDecimal.valueOf(400)),
            new Item(null, "LG TV", BigDecimal.valueOf(400)), new Item(null, "PHILIPS TV", BigDecimal.valueOf(400)),
            new Item(null, "Apple TV", BigDecimal.valueOf(400)),
            new Item("Sample0011", "BOSE TV", BigDecimal.valueOf(400)));

    @BeforeEach
    public void setup() {
        Flux<Item> allItemsInReposity = itemReactiveRepository.findAll();
        allItemsInReposity.subscribe(flValue -> System.out.println("FLVALUE IS : " + flValue));
        itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted Item is :" + item);
                })).blockLast();
    }

    @Test
    public void getAllItems() {
        Flux<Item> allItems = itemReactiveRepository.findAll();
        StepVerifier.create(allItems).expectSubscription().expectNextCount(5).verifyComplete();
    }

    @Test
    public void getItemById() {
        Mono<Item> itemById = itemReactiveRepository.findById("Sample0011");
        StepVerifier.create(itemById).expectSubscription()
                .expectNextMatches(item -> item.getDescription().equalsIgnoreCase("BOSE TV")).verifyComplete();
    }

    @Test
    public void findItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung TV").log("ItemByDescription :"))
                .expectSubscription().expectNextCount(1).verifyComplete();
    }

    @Test
    public void saveItem(){
        Item newItem = new Item("NITEM123","Google Home",new BigDecimal("30.00"));
        Mono<Item> savedItem = itemReactiveRepository.save(newItem);
        StepVerifier.create(savedItem).expectSubscription().expectNextMatches(item-> item !=null && item.getId().equalsIgnoreCase(
                "NITEM123")).verifyComplete();
    }

    @Test
    public void updateItem() {
        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("Samsung TV").map(item -> {
            item.setPrice(new BigDecimal("520"));
            return item;
        }).flatMap(item -> {
            return itemReactiveRepository.save(item);
        });
        StepVerifier.create(updatedItem).expectSubscription()
                .expectNextMatches(item -> (item.getPrice().compareTo(new BigDecimal("520")) == 0)).verifyComplete();
    }

    @Test
    public void deleteItemById(){
        Mono<Void> deletedItem = itemReactiveRepository.findById("Sample0011").map(Item::getId).flatMap(id -> {
            return itemReactiveRepository.deleteById(id);
        });

        StepVerifier.create(deletedItem.log()).expectSubscription().verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll()).expectSubscription().expectNextCount(4).verifyComplete();
    }


}
