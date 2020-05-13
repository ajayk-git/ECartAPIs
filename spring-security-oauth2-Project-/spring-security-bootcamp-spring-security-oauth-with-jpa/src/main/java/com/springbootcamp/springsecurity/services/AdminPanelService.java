package com.springbootcamp.springsecurity.services;
import com.springbootcamp.springsecurity.dtos.CategoryAdminPanelDto;
import com.springbootcamp.springsecurity.dtos.ProductAdminPanelDto;
import com.springbootcamp.springsecurity.dtos.ProductCountAdminPanelDto;
import com.springbootcamp.springsecurity.dtos.UserAdminPanelDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdminPanelService {

    public static Integer totalUsers=0;
    public static Integer totalCategories=0;

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
        categoryList.size();

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

    public List<ProductAdminPanelDto> getAllProducts() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);

        criteriaQuery.multiselect(productRoot.get("id"), productRoot.get("name"),productRoot.get("isActive"));

        Query<Object[]> userQuery = (Query<Object[]>) entityManager.createQuery(criteriaQuery);

        List<Object[]> productList = userQuery.getResultList();

        List<ProductAdminPanelDto> productAdminPanelDtoList = new ArrayList<>();

        for (Object[] objects : productList) {
            ProductAdminPanelDto productAdminPanelDto = new ProductAdminPanelDto();
            productAdminPanelDto.setId((Long) objects[0]);
            productAdminPanelDto.setProductName((String) objects[1]);
            productAdminPanelDto.setIsActive((Boolean) objects[2]);
            productAdminPanelDtoList.add(productAdminPanelDto);
        }

        return productAdminPanelDtoList;
    }


    public List<ProductCountAdminPanelDto> getTotalNumberOfProductCount(){

        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery=criteriaBuilder.createQuery(Object[].class);
        Root<ProductVariation> productVariationRoot=criteriaQuery.from(ProductVariation.class);

        criteriaQuery.multiselect(productVariationRoot.get("product"),
                criteriaBuilder.sum(productVariationRoot.get("quantityAvailable"))).groupBy(productVariationRoot.get("product"));
        Query<Object[]>productQuery= (Query<Object[]>) entityManager.createQuery(criteriaQuery);

        List<Object[]>productList=productQuery.getResultList();

        List<ProductCountAdminPanelDto> productCountAdminPanelDtoList=new ArrayList<>();

        for (Object[] objects : productList) {
            ProductCountAdminPanelDto productAdminPanelDto = new ProductCountAdminPanelDto();
            productAdminPanelDto.setProduct((Product) objects[0]);
            productAdminPanelDto.setQuantityAvailable((Long) objects[1]);
            productCountAdminPanelDtoList.add(productAdminPanelDto);
        }

        return productCountAdminPanelDtoList;

    }


}
