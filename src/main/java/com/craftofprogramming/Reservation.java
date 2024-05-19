package com.craftofprogramming;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Reservation implements Comparable<Reservation> {
    private static final int DEFAULT_RESERVATION_DURATION_IN_HOURS = 2;
    private String id;
    private String restaurantId;
    private String name;
    private String email;
    private String phone;
    private int partySize;
    private LocalDateTime dateTime;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Reservation(String restaurantId, String name, String email, String phone, int partySize, LocalDateTime dateTime) {
        this.id = UUID.randomUUID().toString();
        this.restaurantId = restaurantId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.startTime = dateTime.toLocalTime();
        this.endTime = this.startTime.plusHours(DEFAULT_RESERVATION_DURATION_IN_HOURS);
    }

    public static LocalTime getEndTimeFrom(LocalDateTime dateTime) {
        return dateTime.toLocalTime().plusHours(DEFAULT_RESERVATION_DURATION_IN_HOURS);
    }

    // getters and setters

    @Override
    public int compareTo(Reservation other) {
        return this.dateTime.compareTo(other.dateTime);
    }

    public String getId() {
        return id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public boolean intersectsWith(Reservation other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    public boolean intersectsWith(LocalTime startTime, LocalTime endTime) {
        return this.startTime.isBefore(endTime) && startTime.isBefore(this.endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getPartySize() {
        return this.partySize;
    }
}