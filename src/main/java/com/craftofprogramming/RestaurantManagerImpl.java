package com.craftofprogramming;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantManagerImpl implements RestaurantManager {
    private Map<String, Restaurant> restaurants = new HashMap<>();

    @Override
    public String addRestaurant(String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures) {
        validateInputs(name, address, capacity, open, close, closures);
        Restaurant restaurant = new Restaurant(name, address, capacity, open, close, closures);
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant.getId();
    }

    @Override
    public String modifyRestaurant(String restaurantId, String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures) {
        removeRestaurant(restaurantId);
        return addRestaurant(name, address, capacity, open, close, closures);
    }

    @Override
    public void removeRestaurant(String restaurantId) {
        restaurants.remove(restaurantId);
    }

    @Override
    public Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }

    private void validateInputs(String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures) {
        if (name == null) {
            throw new NullPointerException("Name parameter cannot be null");
        }
        if (address == null) {
            throw new NullPointerException("Address parameter cannot be null");
        }
        if (open == null) {
            throw new NullPointerException("Open time parameter cannot be null");
        }
        if (close == null) {
            throw new NullPointerException("Close time parameter cannot be null");
        }
        if (closures == null) {
            throw new NullPointerException("Closures parameter cannot be null");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number");
        }
        if (open.isAfter(close) || open.plusHours(8).isAfter(close)) {
            throw new IllegalArgumentException(String.format("The restaurant must be open for at least 8 hours and the close time must be strictly after open: open=%s, close=%s",
                    open, close));
        }
        if (closures.stream().anyMatch(date -> date.isBefore(LocalDate.now()))) {
            throw new IllegalArgumentException(String.format("Closures must be in the future: closures=%s", closures));
        }
    }
}