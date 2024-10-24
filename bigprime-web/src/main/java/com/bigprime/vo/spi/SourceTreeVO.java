package com.bigprime.vo.spi;

import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceTreeVO {
    private String type;

    private String name;

    private List<Object> children;

    private Long databaseId;

    private Long id;
}
