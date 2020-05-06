package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.ProductCo;
import com.springbootcamp.springsecurity.co.ProductUpdateBySellerCo;
import com.springbootcamp.springsecurity.co.ProductVariationCo;
import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.dtos.ProductSellerDto;
import com.springbootcamp.springsecurity.dtos.ProductVariantDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.exceptions.ProductDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotAccessibleException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import com.springbootcamp.springsecurity.repositories.ProductRepository;
import com.springbootcamp.springsecurity.repositories.ProductVariationRepository;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ProductVariationRepository variationRepository;
    @Autowired
    EmailService emailService;

    public ProductDto getProductByid(long id) {

        if (!productRepository.findById(id).isPresent()) {
            throw new ProductDoesNotExistException("Product not available with given product Id");
        }
        Product product = productRepository.findById(id).get();
        ProductDto productDto = new ProductDto(product.getBrand(), product.getDescription(),
                product.getId(), product.getName(), product.getSeller().getCompanyName(), product.isCancelable(), product.isReturnable());

        return productDto;
    }

    public List<ProductDto> getAllProducts() {
        List<ProductDto> productDtoList = new ArrayList<>();
        Iterable<Product> productIterable = productRepository.findAll();

        productIterable.forEach(product -> productDtoList.add(new ProductDto(product.getBrand(),
                product.getDescription(), product.getId(),
                product.getName(), product.getSeller().getCompanyName(),
                product.isCancelable(), product.isReturnable())));

        return productDtoList;

    }


    @Secured("ROLE_SELLER")
    //===============================Add a new Product by seller================================================================
    public ResponseEntity addNewProduct(ProductCo productCo, Principal principal) {

        String productNameCo = productCo.getProductName();
        String brandNameCo = productCo.getBrandName();
        Long categoryIdCo = productCo.getCategoryId();
        Seller seller = sellerRepository.findByEmail(principal.getName());


        if (productRepository.findByName(productNameCo).isPresent())
            throw new ResourceAlreadyExistException("Product already exist.A product can be sold by single seller.Kindly register different product.  ");


        Optional<Category> category = categoryRepository.findById(categoryIdCo);

        if (!category.isPresent())
            throw new ResourceNotFoundException("Mentioned Category not found.Kindly enter correct category.");
        else {
            if (!category.get().getSubCategory().isEmpty())
                throw new RuntimeException("Mentioned categoryId is not a leaf categoryId.Kindly enter a leaf categoryId ");
        }
        Product product = new Product();
        product.setBrand(brandNameCo);
        product.setName(productNameCo);
        product.setCategory(category.get());
        product.setDescription(productCo.getDescription());
        product.setReturnable(false);
        product.setCancelable(false);
        product.setActive(false);
        product.setDeleted(false);
        product.setSeller(seller);


        emailService.mailNotificationAdminNewProductAdd();
        productRepository.save(product);
        return new ResponseEntity("Product added successfully.Product will  be activated  soon by admin,", HttpStatus.CREATED);

    }

    //===========================================to activate product ===========================================================

    public ResponseEntity activateProduct(Long productId) {
        if (!productRepository.findById(productId).isPresent()) {
            throw new ResourceNotFoundException("Product is not found with mentioned productId.Please enter existing productId.");
        } else {
            Product product = productRepository.findById(productId).get();
            if (product.isActive())
                throw new RuntimeException("Product with mentioned id is already activated.");

            product.setActive(true);
            String email = product.getSeller().getEmail();
            emailService.mailNotificationSellerProductActivate(email, product);
            productRepository.save(product);

        }
        return new ResponseEntity("Product is activated successfully.Email is triggered to seller.", HttpStatus.OK);
    }

    //===========================================to deactivate product ===========================================================


    public ResponseEntity deactivateProduct(Long productId) {
        if (!productRepository.findById(productId).isPresent()) {
            throw new ResourceNotFoundException("Product is not found with mentioned productId.Please enter existing productId.");
        } else {
            Product product = productRepository.findById(productId).get();
            if (!product.isActive())
                throw new RuntimeException("Product with mentioned id is already deactivated.");
            product.setActive(false);
            String email = product.getSeller().getEmail();
            emailService.mailNotificationSellerProductDeactivate(email, product);
            productRepository.save(product);
        }
        return new ResponseEntity("Product is deactivated successfully.Email is triggered to seller.", HttpStatus.OK);
    }


    //=================================================View  Product-Variant By seller Account==================================


    public ProductVariantDto getProductVariant(Long productVariantId) {

        if (!variationRepository.findById(productVariantId).isPresent())
            throw new ResourceNotFoundException("Product Variant is not found with mentioned productVariantId.Please enter existing productVariantId.");
        ProductVariation productVariation = variationRepository.findById(productVariantId).get();

        ProductVariantDto productVariantDto = new ProductVariantDto();
        productVariantDto.setProductVariantId(productVariation.getId());
        productVariantDto.setProductId(productVariation.getProduct().getId());
        productVariantDto.setBrand(productVariation.getProduct().getBrand());
        productVariantDto.setProductName(productVariation.getProduct().getName());
        productVariantDto.setMetaData(productVariation.getMetaData());
        productVariantDto.setPrice(productVariation.getPrice());
        productVariantDto.setQuantityAvailable(productVariation.getQuantityAvailable());
        productVariantDto.setActive(productVariation.isActive());
        return productVariantDto;

    }
    //=================================================View all product Variants of a product By seller Account==================================


    public ResponseEntity viewAllProductVariationsBySeller(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Long productId, Principal principal) {

        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");

        Product product = productRepository.findById(productId).get();

        String loggedInSeller = principal.getName();
        String productSeller = product.getSeller().getEmail();

        if (!loggedInSeller.equalsIgnoreCase(productSeller))
            throw new ResourceNotAccessibleException("Not Authorized to access other seller's products.");

        if (product.isDeleted())
            throw new ResourceNotFoundException("Product with mentioned ProductId has been deleted by admin.");

        ProductVariantDto productVariantDto = new ProductVariantDto();
        List<ProductVariantDto> productVariantDtoList = new ArrayList<>();

        List<ProductVariation> productVariations = product.getProductVariationList();

        Iterator<ProductVariation> iterator = productVariations.iterator();

        while (iterator.hasNext()) {

            ProductVariation currentVariation = iterator.next();

            productVariantDto = new ProductVariantDto(product.getId(), product.getBrand(), product.getName(),
                    currentVariation.getId(), currentVariation.getMetaData(), currentVariation.isActive(),
                    currentVariation.getQuantityAvailable(), currentVariation.getPrice());

            productVariantDtoList.add(productVariantDto);

        }
        if (productVariantDtoList.size() < 1)
            throw new ResourceNotFoundException("No product variation available for given product.");

        return new ResponseEntity(productVariantDtoList, null, HttpStatus.OK);


    }

    //=================================================Add  new Product-Variant By seller Account==================================


    @Secured("ROLE_SELLER")
    public ResponseEntity addNewProductVariant(Long productId, ProductVariationCo productVariationCo) {
        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");
        Product product = productRepository.findById(productId).get();
        if (!product.isActive())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not active.");
        if (product.isDeleted())
            throw new ResourceNotFoundException("Product with mentioned ProductId is deleted.");

        if (product.isActive() && (!product.isDeleted())) {
            ProductVariation productVariation = new ProductVariation();

            productVariation.setPrice(productVariationCo.getPrice());
            productVariation.setQuantityAvailable(productVariationCo.getQuantityAvailable());
            productVariation.setActive(true);
            productVariation.setProduct(product);

            List<ProductVariation> variationList = product.getProductVariationList();

            Iterator<ProductVariation> iterator = variationList.iterator();
            while (iterator.hasNext()) {
                ProductVariation currentVariation = iterator.next();
                if (productVariationCo.getMetaData().equals(currentVariation.getMetaData()))
                    throw new ResourceAlreadyExistException("Product Variation Already exist.");
                else {
                    productVariation.setMetaData(productVariationCo.getMetaData());
                    variationRepository.save(productVariation);
                }
            }
        }
        return new ResponseEntity("Product variation added successfully.", HttpStatus.CREATED);
    }

    //=================================================View a Product By seller Account==================================

    @Secured("ROLE_SELLER")
    public ProductSellerDto viewProductBySeller(Long productId, Principal principal) {

        String sellerLoggedIn = principal.getName();
        ProductSellerDto productSellerDto = new ProductSellerDto();

        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");


        Product product = productRepository.findById(productId).get();
        String sellerUserName = product.getSeller().getEmail();

        if (!sellerUserName.equalsIgnoreCase(sellerLoggedIn))
            throw new ResourceNotAccessibleException("Not Authorized  to view other seller's product.");

        if (product.isDeleted())
            throw new ResourceNotFoundException("Product with mentioned ProductId is deleted.Kindly enter existing productId.");

        Category category = product.getCategory();

        productSellerDto = new ProductSellerDto(product.getId(), product.getName(), product.getBrand(),
                product.getDescription(), product.getSeller().getCompanyName(), category.getName(),
                product.isActive(), product.isCancelable(), product.isReturnable());

        return productSellerDto;
    }

    //=================================================Delete a Product By seller Account==================================


    public ResponseEntity deleteProductBySeller(Long productId, Principal principal) {

        String loggedInSeller = principal.getName();

        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");

        Product product = productRepository.findById(productId).get();
        String sellerOfProduct = product.getSeller().getEmail();


        if (product.isDeleted())
            throw new ResourceNotFoundException("Product with mentioned productId is already deleted.");

        if (!product.isActive())
            throw new ResourceNotFoundException("Product with mentioned productId is not active.");

        if (!loggedInSeller.equalsIgnoreCase(sellerOfProduct))
            throw new ResourceNotAccessibleException("Not Authorized  to delete other seller's product.");

        if (product.isActive() && (!product.isDeleted())) {

            if (loggedInSeller.equalsIgnoreCase(sellerOfProduct)) {
                product.setDeleted(true);
                product.setActive(false);
            }
        }
        productRepository.save(product);
        return new ResponseEntity("Product deleted with mentioned productId.", HttpStatus.OK);
    }

    //=================================================View all products By seller Account==================================

    public ResponseEntity viewAllProductsBySeller(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Principal principal) {

        Sort.Direction sortingDirection = sortDirection.get().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String loggedInSeller = principal.getName();

        Page<Product> products = productRepository.findAll(PageRequest.of(page.get(), contentSize.get(), sortingDirection, sortProperty.get()));

        if (products.getTotalElements() < 1)
            throw new ResourceNotFoundException("No products available in product List.");

        ProductSellerDto productSellerDto = new ProductSellerDto();
        List<ProductSellerDto> productSellerDtoList = new ArrayList<>();

        Iterator<Product> productIterator = products.iterator();
        while (productIterator.hasNext()) {

            Product currentProduct = productIterator.next();
            Category category = currentProduct.getCategory();
            String sellerName = currentProduct.getSeller().getEmail();

            if ((!currentProduct.isDeleted()) && (sellerName.equalsIgnoreCase(loggedInSeller))) {

                productSellerDto = new ProductSellerDto(currentProduct.getId(), currentProduct.getName(),
                        currentProduct.getBrand(), currentProduct.getDescription(), currentProduct.getSeller().getCompanyName(),
                        category.getName(), currentProduct.isActive(), currentProduct.isCancelable(), currentProduct.isReturnable());

                productSellerDtoList.add(productSellerDto);
            }
        }
        return new ResponseEntity(productSellerDtoList, null, HttpStatus.OK);

    }

    //=================================================Update a product By seller =========================================================

    public ResponseEntity updateProductBySeller(ProductUpdateBySellerCo productUpdateBySellerCo, Long productId, Principal principal) {

        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");

        else {

            Product product = productRepository.findById(productId).get();

            String productSeller = product.getSeller().getEmail();
            String loggedInSeller = principal.getName();

            if (!productSeller.equalsIgnoreCase(loggedInSeller))
                throw new ResourceNotAccessibleException("Not Authorized  to update other seller's product.");

            if (product.isDeleted())
                throw new ResourceNotFoundException("Product with mentioned productId is already deleted.Cannot update.");

            if (!product.isActive())
                throw new ResourceNotFoundException("Product with mentioned productId is not activated.Kindly wait to get activated by admin to update.");

            else {
                String productNewName = productUpdateBySellerCo.getProductName();
                if (productRepository.findByName(productNewName).isPresent())
                    throw new ResourceAlreadyExistException("Product with " + productNewName + " is already exist. Kindly uodate with another name.");

                if (productUpdateBySellerCo.getProductName()!=null)
                product.setName(productUpdateBySellerCo.getProductName());
                if (productUpdateBySellerCo.getBrandName()!=null)
                product.setBrand(productUpdateBySellerCo.getBrandName());
                if (productUpdateBySellerCo.getDescription()!=null)
                product.setDescription(productUpdateBySellerCo.getDescription());
                product.setCancelable(productUpdateBySellerCo.isCancellable());
                product.setReturnable(productUpdateBySellerCo.isReturnable());

                productRepository.save(product);

                return new ResponseEntity("Product with productId : " + productId + " is updated successfully.", HttpStatus.OK);
            }

        }
    }
}
