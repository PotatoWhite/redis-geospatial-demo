package me.potato.redisgeospatialdemo.geo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.potato.redisgeospatialdemo.geo.dto.GeoLocation;
import me.potato.redisgeospatialdemo.geo.dto.Restaurant;
import me.potato.redisgeospatialdemo.geo.util.RestaurantUtil;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataSetupService implements CommandLineRunner {
    private final RedissonReactiveClient            client;
    private       RGeoReactive<Restaurant>          geo;
    private       RMapReactive<String, GeoLocation> map;

    @Override
    public void run(String... args) throws Exception {
        geo = client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        map = client.getMap("usa", new TypedJsonJacksonCodec(String.class, GeoLocation.class));

        var mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(restaurant -> this.geo.add(restaurant.getLongitude(), restaurant.getLatitude(), restaurant).thenReturn(restaurant))
                .flatMap(restaurant -> this.map.fastPut(restaurant.getZip(), GeoLocation.of(restaurant.getLongitude(), restaurant.getLatitude())))
                .doFinally(s -> log.info("Done adding restaurants {}", s))
                .subscribe();
    }
}
