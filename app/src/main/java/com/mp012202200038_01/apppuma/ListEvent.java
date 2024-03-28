package com.mp012202200038_01.apppuma;

public class ListEvent {
    String key, title, content, day, month, desc, time;

    public ListEvent() {

    }

//    Getters

    public String getKey() {
        return key;
    }
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDay() {
        return day;
    }
    public String getMonth() { return month; }
    public String getDesc() {
        return desc;
    }
    public String getTime() {
        return time;
    }

//    Setters
    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
