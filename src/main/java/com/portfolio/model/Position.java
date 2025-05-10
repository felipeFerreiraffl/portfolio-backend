package com.portfolio.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "position")
public class Position {
    private String id;
    private String name;
    private PositionAbbr abbr;
    private String desc;
}
