package application;

public class Class {
    private String day;
    private String start;
    private String end;
    private String room;
    private int userId;

    public Class(String day, String start, String end, String room, int userId) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.room = room;
        this.userId = userId;
    }

    public String getDay() {
        return day;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getRoom() {
        return room;
    }

    public int getUserId() {
        return userId;
    }
}

