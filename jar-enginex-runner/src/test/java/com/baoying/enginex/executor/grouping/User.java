package com.baoying.enginex.executor.grouping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private Role role;
}
