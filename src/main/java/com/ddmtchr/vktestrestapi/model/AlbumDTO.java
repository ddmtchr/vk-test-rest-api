package com.ddmtchr.vktestrestapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private Long id;
    private Long userId;
    private String title;
}
