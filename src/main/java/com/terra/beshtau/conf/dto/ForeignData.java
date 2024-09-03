package com.terra.beshtau.conf.dto;

import com.poiji.annotation.ExcelCellName;

public class ForeignData {
    @ExcelCellName("name")
    private String name;
    @ExcelCellName("full_name")
    private String fullName;
    @ExcelCellName("category")
    private String category;
    @ExcelCellName("os")
    private String os;
    @ExcelCellName("amount")
    private String amount;

    public ForeignData() {
    }

//    public ForeignData(String name, String fullName, String category, String os, String amount) {
//        this.name = name;
//        this.fullName = fullName;
//        this.category = category;
//        this.os = os;
//        this.amount = amount;
//    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
