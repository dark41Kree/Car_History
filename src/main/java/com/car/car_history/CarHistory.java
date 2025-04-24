package com.car.car_history;

public class CarHistory {
    private String type;
    private String date;
    private String description;

    public CarHistory(String type, String date, String description) {
        this.type = type;
        this.date = date;
        this.description = description;
    }

    public String getType() { return type; }
    public String getDate() { return date; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Type: " + type + ", Date: " + date + ", Description: " + description;
    }
}

