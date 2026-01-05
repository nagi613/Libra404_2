package com.example.libra404.model;

import java.sql.Date;

import lombok.Data;

@Data
public class DispKasidasi {
    private int boid;
    private Date boday;
    private Date reday;
    private String title;
    private String photo;
}
