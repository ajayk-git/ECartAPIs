package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.CategoryCO;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.entities.CategoryMetaDataField;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.exceptions.MetaDatafieldAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.CategoryMetaDataFieldRepository;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addMetaDataField(String fieldName) {
        if (!(categoryMetaDataFieldRepository.findByFieldName(fieldName) == null))
            throw new MetaDatafieldAlreadyExistException("MetaData field " + fieldName + " already exist.");

        CategoryMetaDataField metaDataField = new CategoryMetaDataField(fieldName);
        categoryMetaDataFieldRepository.save(metaDataField);
        return new ResponseEntity<String>("MetaData field " + fieldName + " is added.", HttpStatus.CREATED);
    }





    @Secured("ROLE_ADMIN")
    public List<CategoryMetaDataFieldDTO> getAllMetaDataFieldList() {
        if (categoryMetaDataFieldRepository.findAll() == null)
            throw new ResourceNotFoundException("No Category MetaData fields exist in database.");
        else {
            Iterable<CategoryMetaDataField> categoryMetaDataFieldIterable = categoryMetaDataFieldRepository.findAll();
            List<CategoryMetaDataFieldDTO> categoryMetaDataFieldDTOList = new ArrayList<>();
            categoryMetaDataFieldIterable.forEach(categoryMetaDataField -> categoryMetaDataFieldDTOList.
                    add(new CategoryMetaDataFieldDTO(categoryMetaDataField.getId(), categoryMetaDataField.getFieldName())));

            return categoryMetaDataFieldDTOList;
        }
    }





    @Secured("ROLE_ADMIN")
    public ResponseEntity addNewCategory(CategoryCO categoryCO) {
        if (categoryCO.getParentId() == null) {
            if(categoryRepository.findByName(categoryCO.getCategoryName())==null) {

                Category category = new Category();
                category.setName(categoryCO.getCategoryName());

                category.setId(categoryCO.getParentId());
                categoryRepository.save(category);
                return new ResponseEntity("New category " + categoryCO.getCategoryName() + " is Added as a Root category because it has null Id. ", HttpStatus.CREATED);
            }
            else
                throw new ResourceAlreadyExistException("Category with " + categoryCO.getCategoryName()+ " already exist.");

        }

        if (categoryCO.getParentId() != null) {
            if (!categoryRepository.findById(categoryCO.getParentId()).isPresent()) {
                throw new ResourceNotFoundException("Category not exist with given Category Parent Id.");
            }
        }
        String categoryName = categoryCO.getCategoryName();

        if (categoryRepository.findByName(categoryName) != null) {
            throw new ResourceAlreadyExistException("Category with " + categoryName + " already exist.");
        }
        Category subCategory = new Category();
        Category parentCategory = categoryRepository.findById(categoryCO.getParentId()).get();


        subCategory.setName(categoryCO.getCategoryName());
        subCategory.setParentCategory(parentCategory);
        parentCategory.addCategory(subCategory);
        categoryRepository.save(subCategory);

        return new ResponseEntity("New category " + categoryName + " is added having Parent id is : " + categoryCO.getParentId() + ".", HttpStatus.CREATED);


    }


    @Secured("ROLE_ADMIN")
    public List<CategoryDTO> getAllCategories() {

        List<Category> categoryList = categoryRepository.findAll();

        if (categoryList == null)
            throw new ResourceNotFoundException("Category List is not available.");

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        Iterator<Category> iterator = categoryList.iterator();

        while (iterator.hasNext()) {

            Category currentCategory = iterator.next();

            CategoryDTO categoryDTO = new CategoryDTO();

            categoryDTO.setCategoryName(currentCategory.getName());

            categoryDTO.setParentCategoryName(currentCategory.getName());
            categoryDTO.setCategoryId(currentCategory.getId());

            if (currentCategory.getParentCategory() ==null) {
                categoryDTO.setParentId(null);
            }
            else
                categoryDTO.setParentId(currentCategory.getParentCategory().getId());

            categoryDTOList.add(categoryDTO);
        }

        return categoryDTOList;
    }


    public List<CategoryDTO> getCategory(Long id) {
        if(categoryRepository.findById(id)==null)
            throw new ResourceNotFoundException("Category does not exist with given category Id.");
        Category categoryList=categoryRepository.findById(id).get();
        if (categoryList.getSubCategory()==null)
            throw new ResourceNotFoundException("No subcategory,since,given category is leaf node of a category.");
        List<CategoryDTO> categoryDTOList=new ArrayList<>();
        List<Category> subCategories=categoryList.getSubCategory();
        subCategories.forEach(category ->categoryDTOList
                .add(new CategoryDTO(category.getName(),category.getId(),id,category.getParentCategory().getName())));
        return  categoryDTOList;
    }
}
