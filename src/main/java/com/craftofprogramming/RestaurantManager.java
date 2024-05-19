package com.craftofprogramming;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RestaurantManager {
    String addRestaurant(String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures);
    String modifyRestaurant(String restaurantId, String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures);
    void removeRestaurant(String restaurantId);

    Restaurant getRestaurant(String restaurantId);
}
