package learni.reactspring.initialize;

import learni.reactspring.ItemReactiveRepository;
import learni.reactspring.document.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    public List<Item> data(){
        Item item1 = new Item(null,"Samsung TV",new BigDecimal("300"));
        Item item2 = new Item(null,"LG TV",new BigDecimal("400"));
        Item item3 = new Item(null,"Apple HeadPhones",new BigDecimal("200"));
        Item item4 = new Item(null,"Apple Watch",new BigDecimal("100"));
        Item item5 = new Item(null,"Google Home Mini",new BigDecimal("100"));
        Item item6 = new Item(null,"Bose Headphone",new BigDecimal("230"));
        return Arrays.asList(item1,item2,item3,item4,item5,item6);
    }

    private void initialDataSetup() {
        itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(data())).flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("Item Inserted from CommandLineRunner :" + item));

    }
}
