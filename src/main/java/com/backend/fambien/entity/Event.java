package com.backend.fambien.entity;

import com.backend.fambien.enums.EventType;

import java.util.List;

public class Event {
    private String id;
    private String title;
    private Famo famo;
    // change later to objects that store s3 key and url
    private List<String> images;
    private EventType type;
    // implement comment later
    //private List<Comment>

}
