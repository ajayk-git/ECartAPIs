package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.AuditHistoryService;
import com.springbootcamp.springsecurity.AuditLogsMongoDBService;
import com.springbootcamp.springsecurity.co.CategoryCO;
import com.springbootcamp.springsecurity.co.CategoryUpdateCO;
import com.springbootcamp.springsecurity.co.MetaDataFieldValueCo;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.dtos.CategorySellerDto;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@Log4j2
public class CategoryService {

    @Autowired
    AuditLogsMongoDBService auditService;

    @Autowired
    MetaDataFieldRepository categoryMetaDataFieldRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MetaDataFieldValuesRepository metaDataFieldValuesRepository;


    public String setToStringConverter(Set<String> inputSet) {
        String resultString = String.join(",", inputSet);
        return resultString;
    }

    public Set<String> stringToSetConverter(String inputString) {
        Set<String> stringSet = new HashSet<String>(Arrays.asList(inputString.split(",")));
        return stringSet;
    }


    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addMetaDataField(String fieldName, Principal principal) {

        log.info("inside addMetaDataField method");


        if (!(categoryMetaDataFieldRepository.findByFieldName(fieldName) == null)) {
            log.warn("MetaDataFieldAlreadyExistException Occurred");
            throw new MetaDatafieldAlreadyExistException("MetaData field " + fieldName + " already exist.");
        }
        CategoryMetaDataField metaDataField = new CategoryMetaDataField(fieldName);
        categoryMetaDataFieldRepository.save(metaDataField);

        auditService.saveNewObject("CategoryMetaDataField", metaDataField.getId(), principal.getName());

        log.info("CategoryMetaDataField Added successfully");
        return new ResponseEntity<String>("MetaData field " + fieldName + " is added.", HttpStatus.CREATED);
    }


    //===============================================To get a metadata field list======================================================================


    @Secured("ROLE_ADMIN")
    public List<CategoryMetaDataFieldDTO> getAllMetaDataFieldList(Principal principal) {

        log.info("inside getAllMetaDataFieldList method");

        if (categoryMetaDataFieldRepository.findAll() == null) {
            log.warn("ResourceNotFoundException Occurred");
            throw new ResourceNotFoundException("No Category MetaData fields exist in database.");
        } else {
            Iterable<CategoryMetaDataField> categoryMetaDataFieldIterable = categoryMetaDataFieldRepository.findAll();
            List<CategoryMetaDataFieldDTO> categoryMetaDataFieldDTOList = new ArrayList<>();
            categoryMetaDataFieldIterable.forEach(categoryMetaDataField -> categoryMetaDataFieldDTOList.
                    add(new CategoryMetaDataFieldDTO(categoryMetaDataField.getId(), categoryMetaDataField.getFieldName())));

            auditService.readAllObjects("CategoryMetaDataField", principal.getName());
            return categoryMetaDataFieldDTOList;
        }
    }

    //===============================================To Add a new category======================================================================


    @Secured("ROLE_ADMIN")
    public ResponseEntity addNewCategory(CategoryCO categoryCO, Principal principal) {

        log.info("inside addNewCategory method");

        if (categoryCO.getParentId() == null) {
            if (categoryRepository.findByName(categoryCO.getCategoryName()) == null) {

                Category category = new Category();
                category.setName(categoryCO.getCategoryName());

                category.setId(categoryCO.getParentId());
                categoryRepository.save(category);

                auditService.saveNewObject("Category", category.getId(), principal.getName());

                return new ResponseEntity("New category " + categoryCO.getCategoryName() + " is Added as a Root category because it has null Id. ", HttpStatus.CREATED);
            } else {
                if (categoryRepository.findByName(categoryCO.getCategoryName()).isPresent())
                    throw new ResourceAlreadyExistException("Category with Category Name " + categoryCO.getCategoryName() + " already exist.");
            }
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

        auditService.saveNewObject("Category", subCategory.getId(), principal.getName());
        log.info("Category Added successfully");

        return new ResponseEntity("New category " + categoryName + " is added having Parent id is : " + categoryCO.getParentId() + ".", HttpStatus.CREATED);


    }
    //===============================================To get all categories======================================================================


    @Secured("ROLE_ADMIN")
    public List<CategoryDTO> getAllCategories(Principal principal) {

        log.info("inside getAllCategories method");

        List<Category> categoryList = categoryRepository.findAll();

        if (categoryList == null) {
            log.warn("ResourceNotFoundException Occurred");
            throw new ResourceNotFoundException("Category List is not available.");
        }
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

        auditService.readAllObjects("Category", principal.getName());

        return categoryDTOList;
    }

    //===============================================To get a category======================================================================


    public List<CategoryDTO> getCategory(Long id, Principal principal) {

        log.info("inside getCategory method");


        if (!categoryRepository.findById(id).isPresent()) {
            log.warn("ResourceNotFoundException Occurred");
            throw new ResourceNotFoundException("Category does not exist with given category Id.");

        }
        Category categoryList = categoryRepository.findById(id).get();
        if (categoryList.getSubCategory().isEmpty())
            throw new ResourceNotFoundException("No subcategory,since,given category is leaf node of a category.");
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        List<Category> subCategories = categoryList.getSubCategory();
        subCategories.forEach(category -> categoryDTOList
                .add(new CategoryDTO(category.getName(), category.getId(), id, category.getParentCategory().getName())));

        auditService.readObject("Category", categoryList.getId(), principal.getName());

        return categoryDTOList;
    }


    //===============================================To update a category======================================================================

    public ResponseEntity updateCategory(Long id, CategoryUpdateCO categoryUpdateCO, Principal principal) {

        log.info("inside updateCategory method");

        if (!categoryRepository.findById(id).isPresent()) {
            log.warn("ResourceNotFoundException Occurred");
            throw new ResourceNotFoundException("Category does not exist with given Category Id.");
        }
        if (categoryRepository.findById(id).isPresent()) {
            String categoryName = categoryUpdateCO.getCategoryName();
            Optional<Category> category = categoryRepository.findByName(categoryName);

            if (category.isPresent()) {
                throw new ResourceAlreadyExistException("Category with given name is already exist kindly retry with another category name.");
            } else {
                Category category1 = categoryRepository.findById(id).get();
                category1.setName(categoryName);
                categoryRepository.save(category1);

                auditService.updateObject("Category", category1.getId(), principal.getName());
            }
        }
        log.info("Category updated successfully");
        return new ResponseEntity("Category details updated Successfully.", HttpStatus.OK);

    }

    public ResponseEntity addMetaDataValues(MetaDataFieldValueCo metaDataFieldValueCo, Principal principal) {

        log.info("inside addMetaDataValues method");

        Long categoryId = metaDataFieldValueCo.getCategoryId();
        Long metaDataFieldId = metaDataFieldValueCo.getFieldId();
        if (categoryRepository.findById(categoryId).isPresent() && categoryMetaDataFieldRepository.findById(metaDataFieldId).isPresent()) {

            Category category = categoryRepository.findById(categoryId).get();

            CategoryMetaDataField metaDataField = categoryMetaDataFieldRepository.findById(metaDataFieldId).get();
            CategoryMetadataCompositeKey compositeKey = new CategoryMetadataCompositeKey(categoryId, metaDataFieldId);
            CategoryMetadataFieldValues metadataFieldValues = new CategoryMetadataFieldValues();


            String fieldValues = metaDataFieldValueCo.getFieldValues();
            List<String> stringList = new ArrayList<>(Arrays.asList(fieldValues.split(",")));
            Set<String> stringSet = stringToSetConverter(fieldValues);
            String filteredValues = setToStringConverter(stringSet);
            if (stringSet.size() != stringList.size())
                throw new DuplicateValueException("Duplicate values exist in Metadata field Values.");

            metadataFieldValues.setFieldValues(filteredValues);
            metadataFieldValues.setCompositeKey(compositeKey);
            metadataFieldValues.setCategory(category);
            metadataFieldValues.setCategoryMetaDataField(metaDataField);

            metaDataFieldValuesRepository.save(metadataFieldValues);

            auditService.saveNewObject("CategoryMetaDataFieldValues", metaDataField.getId(), principal.getName());

            log.info("CategoryMetaDataFieldValues added successfully");
            return new ResponseEntity("MetaData Field Values are successfully added.", HttpStatus.CREATED);
        } else
            throw new ResourceNotFoundException("Please enter a valid Combination of CategoryId and MetaData FieldId.");
    }

    //===========================================Update MetaDataFiledValues======================================================

    @Secured("ROLE_ADMIN")
    public ResponseEntity updateMetaDataValues(MetaDataFieldValueCo metaDataFieldValueCo, Principal principal) {

        log.info("inside updateMetaDataValues method");


        Long categoryId = metaDataFieldValueCo.getCategoryId();
        Long metaDataFieldId = metaDataFieldValueCo.getFieldId();
        CategoryMetadataCompositeKey compositeKey = new CategoryMetadataCompositeKey(categoryId, metaDataFieldId);

//        CategoryMetadataFieldValues categoryMetaDataFieldValues= metaDataFieldValuesRepository.findById(compositeKey).get();

        if (!metaDataFieldValuesRepository.findById(compositeKey).isPresent())
            throw new ResourceNotFoundException("Please enter a valid Combination of CategoryId and MetaData FieldId.");

        else {
            CategoryMetadataFieldValues categoryMetaDataFieldValues = metaDataFieldValuesRepository.findById(compositeKey).get();

            String metaDataFieldValueCO = metaDataFieldValueCo.getFieldValues();
            String fieldValues = categoryMetaDataFieldValues.getFieldValues();
            String appendResult = metaDataFieldValueCO.concat(fieldValues);

            List<String> stringList = new ArrayList<>(Arrays.asList(appendResult.split(",")));

            Set<String> stringSet = stringToSetConverter(appendResult);
            String filteredValues = setToStringConverter(stringSet);
            if (stringList.size() != stringSet.size())
                throw new DuplicateValueException("Duplicate values exist in Metadata field Values existing values and present values after addition.");

            categoryMetaDataFieldValues.setFieldValues(filteredValues);
            metaDataFieldValuesRepository.save(categoryMetaDataFieldValues);

            auditService.updateObject("MetaDataFiledValues", metaDataFieldId, principal.getName());

        }
        log.info("CategoryMetaDataFieldValues updated successfully");

        return new ResponseEntity("MetaData Field Values are updated Successfully.", HttpStatus.OK);
    }

//===================================to get all categories by seller=======================================================

    @Secured("ROLE_SELLER")
    public List<CategorySellerDto> viewAllCategoriesBySeller(Principal principal) {

        log.info("inside viewAllCategoriesBySeller method");

        List<Category> categoryList = categoryRepository.findAll();

        List<CategorySellerDto> sellerDtoList = new ArrayList<>();

        Iterator<Category> iterator = categoryList.iterator();
        while (iterator.hasNext()) {

            Category currentCategory = iterator.next();
            List<Category> subcategories = currentCategory.getSubCategory();

            if (currentCategory.getSubCategory().isEmpty()) {
                List<String> valuesList = new ArrayList<>();
                List<String> fieldList = new ArrayList<>();

                List<CategoryMetadataFieldValues> metadataFieldValues = metaDataFieldValuesRepository.findByCategoryId(currentCategory.getId());

                metadataFieldValues.forEach(categoryMetadataFieldValues -> fieldList.add(categoryMetadataFieldValues.getCategoryMetaDataField().getFieldName()));
                metadataFieldValues.forEach(categoryMetadataFieldValues -> valuesList.add(categoryMetadataFieldValues.getFieldValues()));

                CategorySellerDto categorySellerDto = new CategorySellerDto(currentCategory.getId(),
                        currentCategory.getName(), fieldList, valuesList);

                sellerDtoList.add(categorySellerDto);
            }
        }
        auditService.readAllObjects("Category", principal.getName());

        return sellerDtoList;
    }


    //===================================to get all categories by Customer=======================================================

    @Secured("ROLE_USER")
    public List<CategoryDTO> getCategoriesByCustomer(Long categoryId, Principal principal) {

        log.info("inside getCategoriesByCustomer method");

        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        if (categoryId != null) {

            if (!categoryRepository.findById(categoryId).isPresent())
                throw new ResourceNotFoundException("Category does not exist with Mentioned categoryId. ");

            Category category = categoryRepository.findById(categoryId).get();
            List<Category> subCategories = category.getSubCategory();
            if (subCategories.isEmpty())
                throw new ResourceNotFoundException("Since mentioned categoryId is leaf category so no subcategories.");
            subCategories.forEach(category1 -> categoryDTOList
                    .add(new CategoryDTO(category1.getName(), category1.getId(), categoryId, category1.getParentCategory().getName())));
            return categoryDTOList;
        }
        List<Category> parentCategories = categoryRepository.findByRootCategory();
        parentCategories.forEach(category -> categoryDTOList.add(new CategoryDTO(category.getName(), category.getId())));

        auditService.readAllObjects("Category", principal.getName());
        return categoryDTOList;

    }

}

