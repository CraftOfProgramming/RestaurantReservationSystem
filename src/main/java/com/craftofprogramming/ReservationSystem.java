package com.craftofprogramming;

import java.time.LocalDateTime;

public interface ReservationSystem {
    String addReservation(String restaurantId, String name, String email, String phone, int partySize, LocalDateTime dateTime);
    String modifyReservation(String reservationId, String restaurantId, String name, String email, String phone, int partySize, LocalDateTime dateTime);
    void cancelReservation(String reservationId);
}
