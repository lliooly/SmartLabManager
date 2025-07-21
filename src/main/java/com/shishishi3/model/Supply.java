package com.shishishi3.model;

public class Supply {

    private int id;
    private String name;
    private String description;
    private int quantityOnHand; // 当前库存数量
    private int reorderLevel;   // 库存预警阈值
    private String unit;        // 单位 (如: ml, g, 个)

    public Supply() {}

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(int quantityOnHand) { this.quantityOnHand = quantityOnHand; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}