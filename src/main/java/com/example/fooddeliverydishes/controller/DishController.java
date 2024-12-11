package com.example.fooddeliverydishes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddeliverydishes.config.FeignService;
import com.example.fooddeliverydishes.model.Dish;
import com.example.fooddeliverydishes.model.DishDTO;
import com.example.fooddeliverydishes.model.Restaurant;
import com.example.fooddeliverydishes.service.DishService;

import feign.FeignException;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Allow CORS from Angular app
@RequestMapping(value = "/api/dishes")
public class DishController {
	@Autowired
	private DishService dishService;
	@Autowired
	private FeignService feignService;
	
	@PostMapping("/add")
    public ResponseEntity<Dish> addDish(@RequestBody DishDTO dishDTO) {		
		String restaurantName = dishDTO.getRestaurantName();
		try {
			ResponseEntity<Integer> response = feignService.getRestaurantIdByName(restaurantName);		
			Dish addedDish= dishService.addDish(dishDTO,response );
			return ResponseEntity.status(HttpStatus.CREATED).body(addedDish);
		} catch (FeignException.NotFound e) {
	        // Handle the 404 Not Found error from Feign
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    } catch (FeignException e) {
	        // Handle other Feign exceptions (e.g., 400, 500 errors)
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	                             
	    } catch (Exception e) {
	        // Handle any other exceptions
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	                             
	    }
    }
		
	@GetMapping("/all")
	public ResponseEntity<List<DishDTO>> getAllDishes() {
		List<Dish> dishList = new ArrayList<Dish>();
		List<DishDTO> dishDTOList = new ArrayList<DishDTO>();
		dishList = dishService.getAllDishes();
		if(dishList.size() > 0) {			
			for(Dish dish:dishList) {
				DishDTO dishDTO =  getDishDTOObject(dish);
				dishDTOList.add(dishDTO);				
			}
		}
		return ((dishDTOList != null && !dishDTOList.isEmpty()) ? ResponseEntity.ok(dishDTOList)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<DishDTO> getDishById(@PathVariable Integer id) {		
        Optional<Dish> dish = dishService.getDishById(id);
        return dish.map(d -> ResponseEntity.ok(getDishDTOObject(d))) // map the Dish to DishDTO and return ResponseEntity
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // return NOT_FOUND if Optional is empty
    }
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<Dish> deleteDish(@PathVariable Integer id) {
		Optional<Dish> deletedDish= dishService.getDishById(id);
		dishService.deleteDish(id);
		return deletedDish.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
	@GetMapping("/count")
	public ResponseEntity<Long> getDishesCount(){
		long count = dishService.getDishesCount();
		return ResponseEntity.ok(count);
	}
	@PutMapping("/edit/{id}")
    public ResponseEntity<DishDTO> updateDish(@PathVariable Integer id, @RequestBody DishDTO dishDTO) {
		String restaurantName = dishDTO.getRestaurantName();
		try {
			ResponseEntity<Integer> response = feignService.getRestaurantIdByName(restaurantName);
			int restaurantId = response.getBody();
			DishDTO updatedDish= dishService.editDish(id, dishDTO, restaurantId);
			return( updatedDish == null ?  ResponseEntity.notFound().build() : ResponseEntity.ok(updatedDish));
		}catch (FeignException.NotFound e) {
	        // Handle the 404 Not Found error from Feign
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    } catch (FeignException e) {
	        // Handle other Feign exceptions (e.g., 400, 500 errors)
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	                             
	    } catch (Exception e) {
	        // Handle any other exceptions
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	                             
	    }
    }
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<List<Dish>> getDishesByRestaurantId(@PathVariable Integer id){
		List<Dish> dishList= dishService.getDishesByRestaurantId(id);
		return ((dishList != null && !dishList.isEmpty()) ? ResponseEntity.ok(dishList)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	private DishDTO getDishDTOObject(Dish dish) {
		ResponseEntity<Restaurant> response = feignService.getRestaurantById(dish.getRestaurant_id());
		Restaurant restaurant = response.getBody();
		String restaurantName = restaurant.getName();
		DishDTO dishDTO = dishService.getDishDTO(dish, restaurantName);
		return dishDTO;
	}
}
