package com.ecomm.ayush.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecomm.ayush.model.Category;


public interface CategoryRepo extends JpaRepository<Category, Long> {
	
	public Category findByName(String name);

	@Query("Select c from Category c where c.name=:name AND c.parentCategory.name=:parentCategoryName")
	public Category findByNameAndParant(@Param("name") String name, @Param("parentCategoryName")String parentCategoryName);
}
