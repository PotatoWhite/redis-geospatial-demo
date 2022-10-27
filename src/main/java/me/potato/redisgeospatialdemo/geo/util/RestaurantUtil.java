package me.potato.redisgeospatialdemo.geo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.potato.redisgeospatialdemo.geo.dto.Restaurant;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {
    public static List<Restaurant> getRestaurants() {
        var mapper = new ObjectMapper();
        var stream = RestaurantUtil.class.getClassLoader().getResourceAsStream("restaurants.json");

        try {
            return mapper.readValue(stream, new TypeReference<List<Restaurant>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
