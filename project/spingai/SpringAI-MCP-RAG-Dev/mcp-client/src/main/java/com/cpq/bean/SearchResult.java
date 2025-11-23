package com.cpq.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private String title;
    private String url;
    private String content;
    private double score;

}
