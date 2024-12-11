package com.example.fooddeliverydishes.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.fooddeliverydishes.dao.DishRepository;
import com.example.fooddeliverydishes.model.Dish;
import com.example.fooddeliverydishes.model.DishDTO;


@Service
public class DishServiceImpl implements DishService{
	@Autowired
	private DishRepository dishRepository;
	
	@Override
	public Dish addDish(DishDTO dishDTO, ResponseEntity<Integer> response) {
		// TODO Auto-generated method stub
		Dish dish = getDishObject(dishDTO, response.getBody());
		return dishRepository.save(dish);
	}

	@Override
	public DishDTO editDish(Integer id, DishDTO updateData, int restaurantId) {
		// TODO Auto-generated method stub
		Dish dish = getDishObject(updateData, restaurantId);		 		
		Dish updatedDish = dishRepository.findById(id).map(existingDish -> {
		    // Update the fields (only the ones provided)
		    if (dish.getName() != null) {            	
		        existingDish.setName(dish.getName());
		    }
		    if (dish.getCuisine() != null) {            	
		        existingDish.setCuisine(dish.getCuisine());
		    }
		    if (dish.getDescription() != null) {            	
		        existingDish.setDescription(dish.getDescription());
		    }
		    if (dish.getPrice() != 0) {            	
		        existingDish.setPrice(dish.getPrice());
		    }
		    if (dish.getRestaurant_id() != null) {            	
		        existingDish.setRestaurant_id(dish.getRestaurant_id());
		    }		    
		    // Save the updated dish
		    return dishRepository.save(existingDish);  // Save and return the updated dish
		}).orElse(null);  // If not found, return null or handle appropriately
		return getDishDTO(updatedDish, updateData.getRestaurantName());
	}

	@Override
	public void deleteDish(Integer id) {
		// TODO Auto-generated method stub
		dishRepository.deleteById(id);
	}

	@Override
	public Optional<Dish> getDishById(Integer id) {
		// TODO Auto-generated method stub
		return dishRepository.findById(id);
	}

	@Override
	public List<Dish> getAllDishes() {
		// TODO Auto-generated method stub
		return new ArrayList<Dish>((Collection<? extends Dish>) dishRepository.findAll());
	}

	@Override
	public long getDishesCount() {
		// TODO Auto-generated method stub
		return dishRepository.count();
	}
	@Override
	public DishDTO getDishDTO(Dish dish, String restaurantName) {
		DishDTO dishDTO = new DishDTO();
		dishDTO.setId(dish.getId());
		dishDTO.setName(dish.getName());
		dishDTO.setDescription(dish.getDescription());
		dishDTO.setCuisine(dish.getCuisine());
		dishDTO.setPrice(dish.getPrice());
		dishDTO.setRestaurantName(restaurantName);
		return dishDTO;
	}
	
	@Override
	public Dish getDishObject(DishDTO dishDTO, int restaurantId) {
		Dish dish = new Dish();
		dish.setName(dishDTO.getName());
		dish.setDescription(dishDTO.getDescription());
		dish.setCuisine(dishDTO.getCuisine());
		dish.setPrice(dishDTO.getPrice());
		dish.setRestaurant_id(restaurantId);
		return dish;
	}

	@Override
	public List<Dish> getDishesByRestaurantId(int restaurantId) {
		// TODO Auto-generated method stub
		return dishRepository.findByRestaurantId(restaurantId);		
		
	}

}
