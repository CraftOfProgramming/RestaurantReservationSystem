package com.craftofprogramming;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private int capacity;
    private LocalTime open;
    private LocalTime close;
    private List<LocalDate> closures;

    public Restaurant(String name, String address, int capacity, LocalTime open, LocalTime close, List<LocalDate> closures) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.open = open;
        this.close = close;
        this.closures = closures;
    }

    public boolean isReservationDateTimeValid(LocalDateTime dateTime, int durationInHours) {
        if (dateTime == null) {
            throw new NullPointerException("DateTime parameter cannot be null");
        }
        if (durationInHours <= 0) {
            throw new IllegalArgumentException("Duration must be a positive number");
        }
        if (closures.stream().anyMatch(date -> date.equals(dateTime.toLocalDate()))) {
            return false;
        }
        LocalTime reservationTime = dateTime.toLocalTime();
        LocalTime reservationEndTime = reservationTime.plusHours(durationInHours);
        return !reservationTime.isBefore(open) && !reservationEndTime.isAfter(close);
    }

    public int getCapacity() {
        return capacity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}