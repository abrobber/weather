package com.abrobber.weather;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WeatherRepository extends CrudRepository<Weather, Long> {
	
	//public List<Expense> findByItem(String item);
        
        public List<Weather> findByName(String name);
	
	@Query("SELECT e FROM Weather e WHERE e.temp >= :temp")
	public List<Weather> listItemsWithTempOver(@Param("temp") float temp);
}
