package me.potato.redisgeospatialdemo.geo.controller;

import lombok.RequiredArgsConstructor;
import me.potato.redisgeospatialdemo.geo.dto.Restaurant;
import me.potato.redisgeospatialdemo.geo.service.RestaurantLocatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantLocatorService service;

    @GetMapping("{zip}")
    public Flux<Restaurant> getRestaurants(@PathVariable String zip) {
        return this.service.getRestaurants(zip);
    }
}
