package com.example.fooddeliverydishes.model;

import lombok.Data;

@Data
public class DishDTO {
	private Integer id;
	private String name;
	private String description;
	private double price;
	private String cuisine;
	private String restaurantName;

}
