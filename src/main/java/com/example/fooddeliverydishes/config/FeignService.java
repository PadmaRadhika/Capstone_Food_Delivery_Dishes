package com.example.fooddeliverydishes.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.fooddeliverydishes.model.Restaurant;



@FeignClient(name="FOODDELIVERYRESTAURANTS", url= "http://localhost:8082/api/restaurants")
public interface FeignService {
	@GetMapping("/fetchRestaurantId")
	ResponseEntity<Integer> getRestaurantIdByName(@RequestParam String restaurantName) ;
	
	@GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id);
}
