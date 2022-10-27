package me.potato.redisgeospatialdemo.geo.service;

import lombok.extern.slf4j.Slf4j;
import me.potato.redisgeospatialdemo.geo.dto.GeoLocation;
import me.potato.redisgeospatialdemo.geo.dto.Restaurant;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
@Service
public class RestaurantLocatorService {
    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    public RestaurantLocatorService(RedissonReactiveClient client) {
        this.geo = client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        this.map = client.getMap("usa", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    public Flux<Restaurant> getRestaurants(final String zipCode) {
        return this.map.get(zipCode)
                .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude()).radius(10, GeoUnit.KILOMETERS))
                .flatMap(geo::search)
                .flatMapIterable(Function.identity());
    }
}
