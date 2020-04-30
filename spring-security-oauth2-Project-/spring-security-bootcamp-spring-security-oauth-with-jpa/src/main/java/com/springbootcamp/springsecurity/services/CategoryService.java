package com.springbootcamp.springsecurity.services;
import com.springbootcamp.springsecurity.co.CategoryCO;
import com.springbootcamp.springsecurity.co.CategoryUpdateCO;
import com.springbootcamp.springsecurity.co.MetaDataFieldValueCo;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.entities.CategoryMetaDataField;
import com.springbootcamp.springsecurity.entities.CategoryMetadataCompositeKey;
import com.springbootcamp.springsecurity.entities.CategoryMetadataFieldValues;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.exceptions.DuplicateValueException;
import com.springbootcamp.springsecurity.exceptions.MetaDatafieldAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.MetaDataFieldRepository;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import com.springbootcamp.springsecurity.repositories.MetaDataFieldValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    MetaDataFieldRepository categoryMetaDataFieldRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MetaDataFieldValuesRepository metaDataFieldValuesRepository;



    public String setToStringConverter(Set<String> inputSet){
        String resultString=String.join(",",inputSet);
        return resultString;
    }
    public Set<String> stringToSetConverter(String inputString){
        Set<String> stringSet=new HashSet<String>(Arrays.asList(inputString.split(",")));
        return stringSet;
    }




    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addMetaDataField(String fieldName) {
        if (!(categoryMetaDataFieldRepository.findByFieldName(fieldName) == null))
            throw new MetaDatafieldAlreadyExistException("MetaData field " + fieldName + " already exist.");

        CategoryMetaDataField metaDataField = new CategoryMetaDataField(fieldName);
        categoryMetaDataFieldRepository.save(metaDataField);
        return new ResponseEntity<String>("MetaData field " + fieldName + " is added.", HttpStatus.CREATED);
    }


    //===============================================To get a metadata field list======================================================================


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

    //===============================================To Add a new category======================================================================


    @Secured("ROLE_ADMIN")
    public ResponseEntity addNewCategory(CategoryCO categoryCO) {
        if (categoryCO.getParentId() == null) {
            if (categoryRepository.findByName(categoryCO.getCategoryName()) == null) {

                Category category = new Category();
                category.setName(categoryCO.getCategoryName());

                category.setId(categoryCO.getParentId());
                categoryRepository.save(category);
                return new ResponseEntity("New category " + categoryCO.getCategoryName() + " is Added as a Root category because it has null Id. ", HttpStatus.CREATED);
            } else
                throw new ResourceAlreadyExistException("Category with " + categoryCO.getCategoryName() + " already exist.");

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
    //===============================================To get all categories======================================================================


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

            if (currentCategory.getParentCategory() == null) {
                categoryDTO.setParentId(null);
            } else
                categoryDTO.setParentId(currentCategory.getParentCategory().getId());

            categoryDTOList.add(categoryDTO);
        }

        return categoryDTOList;
    }

    //===============================================To get a category======================================================================


    public List<CategoryDTO> getCategory(Long id) {
        if (!categoryRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("Category does not exist with given category Id.");
        Category categoryList = categoryRepository.findById(id).get();
        if (categoryList.getSubCategory().isEmpty())
            throw new ResourceNotFoundException("No subcategory,since,given category is leaf node of a category.");
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        List<Category> subCategories = categoryList.getSubCategory();
        subCategories.forEach(category -> categoryDTOList
                .add(new CategoryDTO(category.getName(), category.getId(), id, category.getParentCategory().getName())));
        return categoryDTOList;
    }


    //===============================================To update a category======================================================================

    public ResponseEntity updateCategory(Long id, CategoryUpdateCO categoryUpdateCO) {

        if (!categoryRepository.findById(id).isPresent())
            throw new ResourceNotFoundException("Category does not exist with given Category Id.");

        if (categoryRepository.findById(id).isPresent()) {
            String categoryName = categoryUpdateCO.getCategoryName();
            Optional<Category> category = categoryRepository.findByName(categoryName);

            if (category.isPresent()) {
                throw new ResourceAlreadyExistException("Category with given name is already exist kindly retry with another category name.");
            } else {
                Category category1 = categoryRepository.findById(id).get();
                category1.setName(categoryName);
                categoryRepository.save(category1);
            }
        }
        return new ResponseEntity("Category details updated Successfully.", HttpStatus.OK);

    }

    public ResponseEntity addMetaDataValues( MetaDataFieldValueCo metaDataFieldValueCo) {

        Long categoryId= metaDataFieldValueCo.getCategoryId();
        Long metaDataFieldId= metaDataFieldValueCo.getFieldId();
        if (categoryRepository.findById(categoryId).isPresent() && categoryMetaDataFieldRepository.findById(metaDataFieldId).isPresent()) {

            Category category=categoryRepository.findById(categoryId).get();

            CategoryMetaDataField metaDataField=categoryMetaDataFieldRepository.findById(metaDataFieldId).get();
            CategoryMetadataCompositeKey  compositeKey=new CategoryMetadataCompositeKey(categoryId,metaDataFieldId);
            CategoryMetadataFieldValues metadataFieldValues=new CategoryMetadataFieldValues();


            String fieldValues=metaDataFieldValueCo.getFieldValues();
            List<String> stringList=new ArrayList<>(Arrays.asList(fieldValues.split(",")));
            Set<String> stringSet=stringToSetConverter(fieldValues);
            String filteredValues=setToStringConverter(stringSet);
            if (stringSet.size()!=stringList.size())
                throw new DuplicateValueException("Duplicate values exist in Metadata field Values.");

            metadataFieldValues.setFieldValues(filteredValues);
            metadataFieldValues.setCompositeKey(compositeKey);
            metadataFieldValues.setCategory(category);
            metadataFieldValues.setCategoryMetaDataField(metaDataField);

            metaDataFieldValuesRepository.save(metadataFieldValues);
            return new ResponseEntity("MetaData Field Values are successfully added.",HttpStatus.CREATED);
        }

        else
            throw  new ResourceNotFoundException("Please enter a valid Combination of CategoryId and MetaData FieldId.");
    }




    public ResponseEntity updateMetaDataValues(MetaDataFieldValueCo metaDataFieldValueCo) {

        Long categoryId=metaDataFieldValueCo.getCategoryId();
        Long metaDataFieldId=metaDataFieldValueCo.getFieldId();
        CategoryMetadataCompositeKey compositeKey=new CategoryMetadataCompositeKey(categoryId,metaDataFieldId);

//        CategoryMetadataFieldValues categoryMetaDataFieldValues= metaDataFieldValuesRepository.findById(compositeKey).get();

        if(!metaDataFieldValuesRepository.findById(compositeKey).isPresent())
            throw new ResourceNotFoundException("Please enter a valid Combination of CategoryId and MetaData FieldId.");

        else {
            CategoryMetadataFieldValues categoryMetaDataFieldValues= metaDataFieldValuesRepository.findById(compositeKey).get();

            String metaDataFieldValueCO=metaDataFieldValueCo.getFieldValues();
           String fieldValues= categoryMetaDataFieldValues.getFieldValues();
           String appendResult=metaDataFieldValueCO.concat(fieldValues);

            List<String> stringList=new ArrayList<>(Arrays.asList(appendResult.split(",")));

            Set<String> stringSet=stringToSetConverter(appendResult);
            String filteredValues=setToStringConverter(stringSet);
            if (stringList.size()!=stringSet.size())
                throw new DuplicateValueException("Duplicate values exist in Metadata field Values existing values and present values after addition.");

            categoryMetaDataFieldValues.setFieldValues(filteredValues);
            metaDataFieldValuesRepository.save(categoryMetaDataFieldValues);

        }
        return new ResponseEntity("MetaData Field Values are updated Successfully.",HttpStatus.OK);


    }




}
