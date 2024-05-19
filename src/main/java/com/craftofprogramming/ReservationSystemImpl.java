package com.craftofprogramming;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationSystemImpl implements ReservationSystem {
    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final Pattern phonePattern = Pattern.compile("^\\+1\\s\\([0-9]{3}\\)\\s[0-9]{3}-[0-9]{4}$");
    private Map<String, TreeSet<Reservation>> reservationsByTime = new HashMap<>();
    private Map<String, Reservation> reservationsById = new HashMap<>();
    private RestaurantManager restaurantManager;
    private Matcher emailMatcher;
    private Matcher phoneMatcher;

    public ReservationSystemImpl(RestaurantManager restaurantManager) {
        this.restaurantManager = restaurantManager;
    }

    @Override
    public String addReservation(String restaurantId, String name, String email, String phone, int partySize, LocalDateTime dateTime) {
        validateInputs(restaurantId, name, email, phone, partySize, dateTime);
        Restaurant restaurant = restaurantManager.getRestaurant(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException(String.format("No restaurant found with id: %s", restaurantId));
        }
        if (!restaurant.isReservationDateTimeValid(dateTime, 2)) {
            throw new IllegalArgumentException(String.format("Reservation cannot be made on the given date and time: %s", dateTime));
        }
        if (isCapacityExceeded(restaurant, partySize, dateTime)) {
            throw new IllegalArgumentException("Reservation cannot be made as it exceeds the restaurant's capacity");
        }
        Reservation reservation = new Reservation(restaurantId, name, email, phone, partySize, dateTime);
        reservationsByTime.computeIfAbsent(restaurantId, k -> new TreeSet<>()).add(reservation);
        reservationsById.put(reservation.getId(), reservation);
        return reservation.getId();
    }

    @Override
    public String modifyReservation(String reservationId, String restaurantId, String name, String email, String phone, int partySize, LocalDateTime dateTime) {
        cancelReservation(reservationId);
        return addReservation(restaurantId, name, email, phone, partySize, dateTime);
    }

    @Override
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservationsById.get(reservationId);
        if (reservation != null) {
            reservationsByTime.get(reservation.getRestaurantId()).remove(reservation);
            reservationsById.remove(reservationId);
        }
    }

    private void validateInputs(String restaurantId, String name, String email, String phone, int partySize, LocalDateTime dateTime) {
    if (restaurantId == null) {
        throw new IllegalArgumentException("RestaurantId parameter cannot be null");
    }
    if (name == null) {
        throw new IllegalArgumentException("Name parameter cannot be null");
    }
    if (email == null) {
        throw new IllegalArgumentException("Email parameter cannot be null");
    } else {
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException(String.format("Invalid email: %s", email));
        }
    }
    if (phone == null) {
        throw new IllegalArgumentException("Phone parameter cannot be null");
    } else {
        if (!phonePattern.matcher(phone).matches()) {
            throw new IllegalArgumentException(String.format("Invalid US phone number: %s", phone));
        }
    }
    if (partySize <= 0) {
        throw new IllegalArgumentException("Party size must be a positive number");
    }
    if (dateTime == null) {
        throw new IllegalArgumentException("DateTime parameter cannot be null");
    }
}

    private boolean isCapacityExceeded(Restaurant restaurant, int partySize, LocalDateTime dateTime) {
        TreeSet<Reservation> reservations = reservationsByTime.get(restaurant.getId());
        if (reservations == null) {
            return false;
        }
        LocalTime startTime = dateTime.toLocalTime();
        LocalTime endTime = Reservation.getEndTimeFrom(dateTime);
        int totalPartySize;
        List<Reservation> overlappingReservations = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getStartTime().isAfter(endTime)) {
                break;
            }
            if(r.intersectsWith(startTime, endTime)) {
                overlappingReservations.add(r);
            }
        }
        for (int i = 0; i < overlappingReservations.size(); i++) {
            Reservation r1 = overlappingReservations.get(i);
            totalPartySize = r1.getPartySize() + partySize;
            for (int j = i+1; j < overlappingReservations.size(); j++) {
                Reservation r2 = overlappingReservations.get(j);
                if (r2.intersectsWith(r1)) {
                    totalPartySize += r2.getPartySize();
                }
                if (totalPartySize > restaurant.getCapacity()) {
                    return true;
                }
            }
        }
        return false;
    }
}