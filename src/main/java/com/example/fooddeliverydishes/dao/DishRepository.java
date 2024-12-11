package com.example.fooddeliverydishes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.fooddeliverydishes.model.Dish;

public interface DishRepository extends CrudRepository<Dish, Integer>{
	@Query("SELECT d FROM Dish d WHERE d.restaurant_id = :restaurantId")
    List<Dish> findByRestaurantId(@Param("restaurantId") int restaurantId);
}
