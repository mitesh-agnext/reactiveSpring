package learni.reactspring.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("alex", "ben", "chloe"));
        return stringFlux;
    }

    public Mono<String> namesMono() {
        Mono<String> mono = Mono.just("alex");
        return mono;
    }

    public Mono<List<String>> namesMono_flatMap(int stingLength) {
        Mono<List<String>> mono =
                Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > stingLength).flatMap(this::splitStringMono);
        return mono;
    }

    public Flux<String> namesMono_flatMapMany(int stingLength) {

        return Mono.just("alex").map(String::toUpperCase).filter(s -> s.length() > stingLength)
                .flatMapMany(this::splitStringWithDelay);

    }


    public Mono<List<String>> splitStringMono(String name){
        int random = new Random().nextInt(1000);
        String[] split = name.split("");
        List<String> strings = Arrays.asList(split);

        return Mono.just(strings);
    }

    public Flux<String> nameFlux_map() {
        return namesFlux().map(String::toUpperCase).log();
    }

    public Flux<String> namesFlux_filter(int length) {
        return namesFlux().map(String::toUpperCase).filter(s -> s.length() > length).log();
    }

    public Flux<String> namesFlux_map(int length) {
        return namesFlux().map(String::toUpperCase).filter(s -> s.length() > length).flatMap(str -> splitString(str));
    }

    public Flux<String> namesFlux_flatMap_Asynch(int length) {
        return namesFlux().map(String::toUpperCase).filter(s -> s.length() > length)
                .flatMap(str -> splitStringWithDelay(str));
    }

    public Flux<String> namesFlux_flatMap_concatMap(int length) {
        return namesFlux().map(String::toUpperCase).filter(s -> s.length() > length)
                .concatMap(str -> splitStringWithDelay(str)).log();
    }

    public Flux<String> splitString(String name) {
        String[] split = name.split("");
        return Flux.fromArray(split);
    }

    public Flux<String> splitStringWithDelay(String name) {
        int delay = new Random().nextInt(1000);
        String[] split = name.split("");
        return Flux.fromArray(split).delayElements(Duration.ofMillis(delay));
    }

   /* private Mono<List<String>> splitStringMono(String name){
        int delay = new Random().nextInt(1000);
    }*/

    public Flux<String> namesFlux_transform(int length) {
        Function<Flux<String>,Flux<String>> filterMap =
                name ->name.map(String::toUpperCase).filter(s -> s.length() > 3);
        return namesFlux().transform(filterMap).flatMap(str -> splitStringWithDelay(str)).switchIfEmpty(Flux.just(
                "default").transform(filterMap));
    }


    //Concat and Concat With - Sequences


    public Flux<String> explore_concat(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        return Flux.concat(flux1,flux2);
    }

    public Flux<String> explore_concatWith(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        return flux1.concatWith(flux2);
    }
    public Flux<String> explore_concatWith_Mono(){
        Mono<String> mono1 = Mono.just("A");
        Mono<String> mono2 = Mono.just("D");
        return mono1.concatWith(mono2);
    }

    //Merge and Merge WIth - Intervined

    public Flux<String> explore_Merge(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        Flux<String> merge = Flux.merge(flux1, flux2);
        merge.log();
        return merge;
    }

    public Flux<String> explore_MergeWith_Mono(){
        Mono<String> mono1 = Mono.just("A");
        Mono<String> mono2 = Mono.just("D");
        return mono1.mergeWith(mono2);
    }


    public Flux<String> explore_MergeSequential(){
        Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofMillis(125));
        Flux<String> merge = Flux.mergeSequential(flux1, flux2);
        merge.log();
        return merge;
    }

    public Flux<String> explore_Zip(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        return Flux.zip(flux1, flux2, (f1, f2) -> f1 + f2);
    }

    public Flux<String> explore_ZipWith(){
        Flux<String> flux1 = Flux.just("A","B","C");
        Flux<String> flux2 = Flux.just("D","E","F");
        Flux<String> flux3 = Flux.just("1","2","3");
        Flux<String> _3flux = Flux.zip(flux1, flux2, flux3).map(t3 -> t3.getT1() + t3.getT2() + t3.getT3());
        Flux<String> _2flux = Flux.zip(flux1, flux2).map(t2 -> t2.getT1() + t2.getT2());
        return _3flux.mergeWith(_2flux);
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux_map(3).subscribe(name -> System.out.println(name));
    }
}
