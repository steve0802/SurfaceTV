package com.steve.surfacetv.datasource.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Drama")
public class DramaPo {
    @PrimaryKey()
    private int drama_id;

    private String name;
    private int total_views;
    private String created_at;
    private String thumb;
    private double rating;

    public DramaPo(int drama_id, String name, int total_views, String created_at, String thumb, double rating) {
        this.drama_id = drama_id;
        this.name = name;
        this.total_views = total_views;
        this.created_at = created_at;
        this.thumb = thumb;
        this.rating = rating;
    }

    public int getDrama_id() {
        return drama_id;
    }

    public void setDrama_id(int drama_id) {
        this.drama_id = drama_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_views() {
        return total_views;
    }

    public void setTotal_views(int total_views) {
        this.total_views = total_views;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
