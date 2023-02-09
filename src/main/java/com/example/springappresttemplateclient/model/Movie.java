package com.example.springappresttemplateclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private String category;

    private String imageUrl;

    private String name;

    private String desc;
}