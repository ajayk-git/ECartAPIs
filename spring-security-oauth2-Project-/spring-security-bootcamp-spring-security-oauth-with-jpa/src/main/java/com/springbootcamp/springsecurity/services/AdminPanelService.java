package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.CategoryAdminPanelDto;
import com.springbootcamp.springsecurity.dtos.UserAdminPanelDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdminPanelService {

    @Autowired
    CategoryRepository categoryRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<CategoryAdminPanelDto> getAllCategories() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Category> categoryRoot = criteriaQuery.from(Category.class);

        criteriaQuery.multiselect(categoryRoot.get("id"), categoryRoot.get("name"));

        Query<Object[]> categoryQuery = (Query<Object[]>) entityManager.createQuery(criteriaQuery);

        List<Object[]> categoryList = categoryQuery.getResultList();

        List<CategoryAdminPanelDto> categoryAdminPanelDtos = new ArrayList<>();
        for (Object[] objects : categoryList) {
            CategoryAdminPanelDto categoryAdminPanelDto = new CategoryAdminPanelDto();
            categoryAdminPanelDto.setId((Long) objects[0]);
            categoryAdminPanelDto.setName((String) objects[1]);
            categoryAdminPanelDtos.add(categoryAdminPanelDto);

        }
        return categoryAdminPanelDtos;

    }

    public List<UserAdminPanelDto> getAllUsers() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.multiselect(userRoot.get("id"), userRoot.get("email"), userRoot.get("isActive"));

        Query<Object[]> userQuery = (Query<Object[]>) entityManager.createQuery(criteriaQuery);

        List<Object[]> userList = userQuery.getResultList();

        List<UserAdminPanelDto> userAdminPanelDtoList = new ArrayList<>();
        for (Object[] objects : userList) {
            UserAdminPanelDto userAdminPanelDto = new UserAdminPanelDto();
            userAdminPanelDto.setId((Long) objects[0]);
            userAdminPanelDto.setEmail((String) objects[1]);
            userAdminPanelDto.setIsActive((Boolean) objects[2]);
            userAdminPanelDtoList.add(userAdminPanelDto);
        }

        return userAdminPanelDtoList;
    }


}
