package com.viewscenes.storage.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Storage implements Serializable {

    private Integer id;
    private String commodityCode;
    private String name;
    private Integer count;
}
