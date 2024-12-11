package com.example.fooddeliverydishes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.example.fooddeliverydishes.model.Dish;
import com.example.fooddeliverydishes.model.DishDTO;


public interface DishService {
	public Dish addDish(DishDTO dishDTO, ResponseEntity<Integer> response);
	public DishDTO editDish(Integer id, DishDTO updateData, int restaurantId);
	public void deleteDish(Integer id);
	public Optional<Dish> getDishById(Integer id);
	public List<Dish> getAllDishes();
	public long getDishesCount();
	public DishDTO getDishDTO(Dish dish, String restaurantName);
	public Dish getDishObject(DishDTO dishDTO, int restaurantId);
	public List<Dish> getDishesByRestaurantId(int restaurantId);
}
