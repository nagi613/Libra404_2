package com.example.libra404.model;

import lombok.Data;

@Data
public class Book {
    private int honid;
    private String title;
    private int cid;
    private int wid;
    private int puid;
    private String synopsis;
    private String photo;
    private boolean botype;
    private boolean distype;

    // 以下の項目を追加
    private String wname;
    private String puname;
    private String category;
}
