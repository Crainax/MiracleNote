package com.ruffneck.cloudnote.models.note;

/**
 * Created by 佛剑分说 on 2015/12/6.
 */
public class NoteBook {

    private int id;
    private String name;
    private String color;
    private String detail;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
