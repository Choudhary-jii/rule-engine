// DTO CLass

package com.zeotap.ruleengine.model;


import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private int age;
    private String department;
    private Double  income;
    private Double spend;
}
