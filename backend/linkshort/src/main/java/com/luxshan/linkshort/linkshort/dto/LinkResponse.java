package com.luxshan.linkshort.linkshort.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkResponse {
    String shortCode;
    String shortUrl;
    String originalUrl;
    String createdAt;
}
