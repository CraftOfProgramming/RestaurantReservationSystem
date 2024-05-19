package com.craftofprogramming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantManagerImplTest {
    private RestaurantManagerImpl restaurantManager;

    @BeforeEach
    void setUp() {
        restaurantManager = new RestaurantManagerImpl();
    }

    @Test
    void addRestaurantWithValidInputsReturnsRestaurantId() {
        String id = restaurantManager.addRestaurant("Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1)));
        assertNotNull(id);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputsForValidation")
    void validateInputsWithInvalidInputsThrowsIllegalArgumentException(Class<Exception> exceptionClass, String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures) {
        assertThrows(exceptionClass, () -> restaurantManager.addRestaurant(name, address, capacity, open, close, closures));
    }

    private static Stream<Arguments> provideInvalidInputsForValidation() {
        return Stream.of(
                Arguments.of(NullPointerException.class, null, "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1))),
                Arguments.of(NullPointerException.class, "Test Restaurant", null, 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1))),
                Arguments.of(IllegalArgumentException.class, "Test Restaurant", "123 Test St", 0, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1))),
                Arguments.of(NullPointerException.class, "Test Restaurant", "123 Test St", 50, null, LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1))),
                Arguments.of(NullPointerException.class, "Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), null, Arrays.asList(LocalDate.now().plusDays(1))),
                Arguments.of(NullPointerException.class, "Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), null),
                Arguments.of(IllegalArgumentException.class, "Test Restaurant", "123 Test St", 50, LocalTime.of(20, 0), LocalTime.of(8, 0), Arrays.asList(LocalDate.now().plusDays(1))),
                Arguments.of(IllegalArgumentException.class, "Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().minusDays(1)))
        );
    }

    @Test
    void modifyRestaurantWithValidInputsReturnsNewRestaurantId() {
        String oldId = restaurantManager.addRestaurant("Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1)));
        String newId = restaurantManager.modifyRestaurant(oldId, "Modified Restaurant", "456 Modified St", 100, LocalTime.of(10, 0), LocalTime.of(22, 0), Arrays.asList(LocalDate.now().plusDays(2)));
        assertNotNull(newId);
        assertNotEquals(oldId, newId);
    }

    @Test
    void removeRestaurantWithExistingRestaurantRemovesRestaurant() {
        String id = restaurantManager.addRestaurant("Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1)));
        restaurantManager.removeRestaurant(id);
        assertNull(restaurantManager.getRestaurant(id));
    }

    @Test
    void getRestaurantWithExistingRestaurantReturnsRestaurant() {
        String id = restaurantManager.addRestaurant("Test Restaurant", "123 Test St", 50, LocalTime.of(8, 0), LocalTime.of(20, 0), Arrays.asList(LocalDate.now().plusDays(1)));
        Restaurant restaurant = restaurantManager.getRestaurant(id);
        assertNotNull(restaurant);
        assertEquals("Test Restaurant", restaurant.getName());
    }
}