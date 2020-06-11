package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.ProductCo;
import com.springbootcamp.springsecurity.co.ProductUpdateBySellerCo;
import com.springbootcamp.springsecurity.co.ProductVariationCo;
import com.springbootcamp.springsecurity.co.ProductVariationUpdateCo;
import com.springbootcamp.springsecurity.dtos.*;
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
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.*;

@Log4j2
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

    @Autowired
    AuditLogsMongoDBService auditService;

    ModelMapper modelMapper = new ModelMapper();

    //===============================Get a  Product by seller================================================================

    public ProductDto getProductByid(long productId, Principal principal) {

        log.info("inside getProductById method");

        if (!productRepository.findById(productId).isPresent()) {
            throw new ProductDoesNotExistException("Product not available with given product Id");
        }
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();
            ProductDto productDto = new ProductDto(product.getBrand(), product.getDescription(),
                    product.getId(), product.getName(), product.getSeller().getCompanyName(), product.getIsCancelable(), product.getIsReturnable());

            auditService.readObject("Product", product.getId(), principal.getName());
            return productDto;
        } else
            throw new ProductDoesNotExistException("Product not available with given product Id");


    }


    //===============================Get all Products===========================================================

    public List<ProductDto> getAllProducts(Principal principal) {

        log.info("inside getAllProducts method");

        List<ProductDto> productDtoList = new ArrayList<>();
        Iterable<Product> productIterable = productRepository.findAll();

        productIterable.forEach(product -> productDtoList.add(new ProductDto(product.getBrand(),
                product.getDescription(), product.getId(),
                product.getName(), product.getSeller().getCompanyName(),
                product.getIsCancelable(), product.getIsReturnable())));


        auditService.readAllObjects("Product", principal.getName());
        return productDtoList;

    }

    //===============================Add a new Product by seller================================================================

    public ResponseEntity addNewProduct(ProductCo productCo, Principal principal) {

        log.info("inside addNewProduct method");

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
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(false);
        product.setIsDeleted(false);
        product.setSeller(seller);


        emailService.mailNotificationAdminNewProductAdd();
        productRepository.save(product);

        log.info("Product added successfully by seller");


        auditService.saveNewObject("Product", product.getId(), principal.getName());

        return new ResponseEntity("Product added successfully.Product will  be activated  soon by admin,", HttpStatus.CREATED);

    }

    //===========================================to activate product ===========================================================

    public ResponseEntity activateProduct(Long productId, Principal principal) {

        log.info("inside activateProduct method");

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();
            //            Product product = productRepository.findById(productId).get();
            if (product.getIsActive())
                throw new ResourceAlreadyExistException("Product with mentioned id is already activated.");

            product.setIsActive(true);
            String email = product.getSeller().getEmail();
            emailService.mailNotificationSellerProductActivate(email, product);
            productRepository.save(product);

            log.info("Product activated successfully by seller");

            auditService.activateObject("Product", product.getId(), principal.getName());
            return new ResponseEntity("Product is activated successfully.Email is triggered to seller.", HttpStatus.OK);

        } else
            throw new ResourceNotFoundException("Product is not found with mentioned productId.Please enter existing productId.");

    }

    //===========================================to deactivate product ===========================================================

    public ResponseEntity deactivateProduct(Long productId, Principal principal) {

        log.info("inside deactivateProduct method");

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (!product.getIsActive())
                throw new ResourceAlreadyExistException("Product with mentioned id is already deactivated.");
            product.setIsActive(false);
            String email = product.getSeller().getEmail();
            emailService.mailNotificationSellerProductDeactivate(email, product);
            productRepository.save(product);
            auditService.deactivateObject("Product", product.getId(), principal.getName());
            log.info("Product deactivated successfully by seller");
            return new ResponseEntity("Product is deactivated successfully.Email is triggered to seller.", HttpStatus.OK);

        } else
            throw new ResourceNotFoundException("Product is not found with mentioned productId.Please enter existing productId.");

    }


    //=================================================View  Product-Variant By seller Account==================================


    public ProductVariantDto getProductVariantBySeller(Long productVariantId, Principal principal) {

        log.info("inside getProductVariant method");

        Optional<ProductVariation> optionalProductVariation = variationRepository.findById(productVariantId);

        if (optionalProductVariation.isPresent()) {

            ProductVariation productVariation = optionalProductVariation.get();
            ProductVariantDto productVariantDto = new ProductVariantDto();
            productVariantDto.setProductVariantId(productVariation.getId());
            productVariantDto.setProductId(productVariation.getProduct().getId());
            productVariantDto.setBrand(productVariation.getProduct().getBrand());
            productVariantDto.setProductName(productVariation.getProduct().getName());
            productVariantDto.setMetaData(productVariation.getMetaData());
            productVariantDto.setPrice(productVariation.getPrice());
            productVariantDto.setQuantityAvailable(productVariation.getQuantityAvailable());
            productVariantDto.setActive(productVariation.getIsActive());
            auditService.readObject("Product", productVariation.getId(), principal.getName());
            return productVariantDto;
        } else
            throw new ResourceNotFoundException("Product Variant is not found with mentioned productVariantId.Please enter existing productVariantId.");

    }

    //=================================================View all product Variants of a product By seller Account==================================


    public ResponseEntity viewAllProductVariationsBySeller(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Long productId, Principal principal) {

        log.info("inside viewAllProductVariationsBySeller method");

        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");

        Product product = productRepository.findById(productId).get();

        String loggedInSeller = principal.getName();
        String productSeller = product.getSeller().getEmail();

        if (!loggedInSeller.equalsIgnoreCase(productSeller))
            throw new ResourceNotAccessibleException("Not Authorized to access other seller's products.");

        if (product.getIsDeleted())
            throw new ResourceNotFoundException("Product with mentioned ProductId has been deleted by admin.");

        ProductVariantDto productVariantDto = new ProductVariantDto();
        List<ProductVariantDto> productVariantDtoList = new ArrayList<>();

        List<ProductVariation> productVariations = product.getProductVariationList();

        Iterator<ProductVariation> iterator = productVariations.iterator();

        while (iterator.hasNext()) {

            ProductVariation currentVariation = iterator.next();

            productVariantDto = new ProductVariantDto(product.getId(), product.getBrand(), product.getName(),
                    currentVariation.getId(), currentVariation.getMetaData(), currentVariation.getIsActive(),
                    currentVariation.getQuantityAvailable(), currentVariation.getPrice());

            productVariantDtoList.add(productVariantDto);

        }
        if (productVariantDtoList.size() < 1)
            throw new ResourceNotFoundException("No product variation available for given product.");

        auditService.readAllObjects("ProductVariation", principal.getName());
        return new ResponseEntity(productVariantDtoList, null, HttpStatus.OK);


    }

    //=================================================Add  new Product-Variant By seller Account==================================


    public ResponseEntity addNewProductVariant(Long productId, ProductVariationCo productVariationCo, Principal principal) {

        log.info("inside addNewProductVariant method");

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();

            if (!product.getIsActive())
                throw new ResourceNotFoundException("Product with mentioned ProductId is not active.");
            if (product.getIsDeleted())
                throw new ResourceNotFoundException("Product with mentioned ProductId is deleted.");

            if (product.getIsActive() && (!product.getIsDeleted())) {
                ProductVariation productVariation = new ProductVariation();

                productVariation.setPrice(productVariationCo.getPrice());
                productVariation.setQuantityAvailable(productVariationCo.getQuantityAvailable());
                productVariation.setIsActive(true);
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
                        log.info("Product variant is added successfully by seller");
                        auditService.saveNewObject("ProductVariation", productVariation.getId(), principal.getName());

                    }
                }
            }
            return new ResponseEntity("Product variation added successfully.", HttpStatus.CREATED);
        } else
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");


    }


    //=====================================================Update a Product Variant  By seller ===============================================


    public ResponseEntity updateProductVariantBySeller(Long variationId, Principal principal, ProductVariationUpdateCo variationUpdateCo) {

        log.info("inside updateProductVariantBySeller method");

        Optional<ProductVariation> optionalProductVariation = variationRepository.findById(variationId);
        if (optionalProductVariation.isPresent()) {

            ProductVariation productVariation = optionalProductVariation.get();

            Product product = productVariation.getProduct();

            String productSeller = productVariation.getProduct().getSeller().getEmail();

            String loggedInSeller = principal.getName();

            if (!productSeller.equalsIgnoreCase(loggedInSeller))
                throw new ResourceNotAccessibleException("Not Authorized  to update other seller's product variant.");

            if (!product.getIsActive())
                throw new ResourceNotFoundException("Product with mentioned productVariantId is not activated.Cannot update the product variant.");

            if (product.getIsDeleted())
                throw new ResourceNotFoundException("Product with mentioned productVariantId is deleted.Cannot update the product variant.");

            else {

                Map<String, String> metaDataOld = productVariation.getMetaData();
                Map<String, String> metaDataNew = variationUpdateCo.getMetaData();

                if (metaDataNew.equals(metaDataOld))
                    throw new ResourceAlreadyExistException("ProductVariation already exist.Kindly update with unique metaData values.");

                if (metaDataNew != null)
                    productVariation.setMetaData(metaDataNew);

                if (variationUpdateCo.getPrice() != null && variationUpdateCo.getPrice() > 0) {
                    productVariation.setPrice(variationUpdateCo.getPrice());
                }

                if (variationUpdateCo.getQuantityAvailable() != null && variationUpdateCo.getQuantityAvailable() > 0)
                    productVariation.setQuantityAvailable(variationUpdateCo.getQuantityAvailable());

                if (variationUpdateCo.getIsActive() != null)
                    productVariation.setIsActive(variationUpdateCo.getIsActive());

                variationRepository.save(productVariation);

                log.info("Product variant is updated successfully by seller");

                auditService.updateObject("ProductVariation", productVariation.getId(), principal.getName());
                return new ResponseEntity("Product with productVariantId : " + variationId + " is updated successfully.", HttpStatus.OK);
            }
        } else
            throw new ResourceNotFoundException("Product variant with mentioned ProductVariantId is not exist.");

    }


    //=================================================View a Product By seller Account==================================

    public ProductSellerDto viewProductBySeller(Long productId, Principal principal) {

        log.info("inside viewProductBySeller method");

        String sellerLoggedIn = principal.getName();
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();

            String sellerUserName = product.getSeller().getEmail();

            if (!sellerUserName.equalsIgnoreCase(sellerLoggedIn))
                throw new ResourceNotAccessibleException("Not Authorized  to view other seller's product.");

            if (product.getIsDeleted())
                throw new ResourceNotFoundException("Product with mentioned ProductId is deleted.Kindly enter existing productId.");

            Category category = product.getCategory();

            ProductSellerDto productSellerDto = new ProductSellerDto(product.getId(), product.getName(), product.getBrand(),
                    product.getDescription(), product.getSeller().getCompanyName(), category.getName(),
                    product.getIsActive(), product.getIsCancelable(), product.getIsReturnable());

            auditService.readObject("Product", product.getId(), principal.getName());
            return productSellerDto;
        } else
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");

    }

    //=================================================Delete a Product By seller Account==================================

    public ResponseEntity deleteProductBySeller(Long productId, Principal principal) {

        log.info("inside deleteProductBySeller method");

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {

            String loggedInSeller = principal.getName();
            Product product = optionalProduct.get();
            String sellerOfProduct = product.getSeller().getEmail();

            if (product.getIsDeleted())
                throw new ResourceNotFoundException("Product with mentioned productId is already deleted.");

            if (!product.getIsActive())
                throw new ResourceNotFoundException("Product with mentioned productId is not active.");

            if (!loggedInSeller.equalsIgnoreCase(sellerOfProduct))
                throw new ResourceNotAccessibleException("Not Authorized  to delete other seller's product.");

            if (product.getIsActive() && (!product.getIsDeleted())) {

                if (loggedInSeller.equalsIgnoreCase(sellerOfProduct)) {
                    product.setIsDeleted(true);
                    product.setIsActive(false);
                }
            }
            productRepository.save(product);

            auditService.deleteObject("Product", product.getId(), principal.getName());
            log.info("Product is deleted successfully by seller");

            return new ResponseEntity("Product deleted with mentioned productId.", HttpStatus.OK);

        } else
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");
    }

    //=================================================View all products By seller Account==================================

    @Cacheable(cacheNames = "getAllProductsBySeller")
    public List<ProductSellerDto> viewAllProductsBySeller(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Principal principal) {

        log.info("inside viewAllProductsBySeller method");

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

            if ((!currentProduct.getIsDeleted()) && (sellerName.equalsIgnoreCase(loggedInSeller))) {

                productSellerDto = new ProductSellerDto(currentProduct.getId(), currentProduct.getName(),
                        currentProduct.getBrand(), currentProduct.getDescription(), currentProduct.getSeller().getCompanyName(),
                        category.getName(), currentProduct.getIsActive(), currentProduct.getIsCancelable(), currentProduct.getIsReturnable());

                productSellerDtoList.add(productSellerDto);
            }
        }
        auditService.readAllObjects("Product", principal.getName());

        return productSellerDtoList;
        //  return new ResponseEntity(productSellerDtoList, null, HttpStatus.OK);

    }


    //=================================================Update a product By seller =========================================================

    public ResponseEntity updateProductBySeller(ProductUpdateBySellerCo productUpdateBySellerCo, Long productId, Principal principal) {

        log.info("inside updateProductBySeller method");

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            String productSeller = product.getSeller().getEmail();
            String loggedInSeller = principal.getName();
            if (!productSeller.equalsIgnoreCase(loggedInSeller))
                throw new ResourceNotAccessibleException("Not Authorized  to update other seller's product.");

            if (product.getIsDeleted())
                throw new ResourceNotFoundException("Product with mentioned productId is already deleted.Cannot update.");

            if (!product.getIsActive())
                throw new ResourceNotFoundException("Product with mentioned productId is not activated.Kindly wait to get activated by admin to update.");

            else {
                String productNewName = productUpdateBySellerCo.getProductName();
                if (productRepository.findByName(productNewName).isPresent())
                    throw new ResourceAlreadyExistException("Product with " + productNewName + " is already exist. Kindly update with another name.");

                if (productUpdateBySellerCo.getProductName() != null)
                    product.setName(productUpdateBySellerCo.getProductName());
                if (productUpdateBySellerCo.getBrandName() != null)
                    product.setBrand(productUpdateBySellerCo.getBrandName());
                if (productUpdateBySellerCo.getDescription() != null)
                    product.setDescription(productUpdateBySellerCo.getDescription());
                if (productUpdateBySellerCo.getIsCancellable() != null)
                    product.setIsCancelable(productUpdateBySellerCo.getIsCancellable());
                if (productUpdateBySellerCo.getIsReturnable() != null)
                    product.setIsReturnable(productUpdateBySellerCo.getIsReturnable());

                productRepository.save(product);
                auditService.updateObject("Product", product.getId(), principal.getName());
                log.info("Product updated successfully by seller");
                return new ResponseEntity("Product with productId : " + productId + " is updated successfully.", HttpStatus.OK);
            }
        } else
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");
    }


    //=================================================Get a product By Customer =========================================================

    public ProductCustomerDto getProductByCustomer(Long productId, Principal principal) {

        log.info("inside getProductByCustomer method");
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getIsDeleted())
                throw new ResourceNotFoundException("Product with mentioned productId is deleted.");

            if (!product.getIsActive())
                throw new ResourceNotFoundException("Product with mentioned productId is not activated.");

            else {
                ProductCustomerDto productCustomerDto = modelMapper.map(product, ProductCustomerDto.class);
                auditService.readObject("Product", product.getId(), principal.getName());
                return productCustomerDto;
            }
        } else
            throw new ResourceNotFoundException("Product with mentioned ProductId is not exist.");
    }


    //=================================================Get a product By Customer =========================================================
    @Cacheable(cacheNames = "getAllProductsByCustomer")
    public List<ProductCustomerDto> getAllProductsByCustomer(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Long categoryId, Principal principal) {

        log.info("inside getAllProductsByCustomer method");

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            if (!category.getSubCategory().isEmpty())
                throw new RuntimeException("Mentioned categoryId is not a leaf node of category.Please enter a leaf node of category.");

            Sort.Direction sortingDirection = sortDirection.get().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

            List<Product> productList = productRepository.findByCategory(categoryId, PageRequest.of(page.get(), contentSize.get(), sortingDirection, sortProperty.get()));

            Type listType = new TypeToken<List<ProductCustomerDto>>() {
            }.getType();
            List<ProductCustomerDto> productCustomerDtoList = modelMapper.map(productList, listType);
            auditService.readAllObjects("Product", principal.getName());
            return productCustomerDtoList;
        } else
            throw new ResourceNotFoundException("Category Does not exist with mentioned categoryId");
    }

    //=================================================View a Product By Admin Account==================================

    public ProductAdminDto viewProductByAdmin(Long productId, Principal principal) {

        log.info("inside viewProductByAdmin method");

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ProductAdminDto productAdminDto = modelMapper.map(product, ProductAdminDto.class);
            auditService.readObject("Product", product.getId(), principal.getName());
            return productAdminDto;
        } else
            throw new ResourceNotFoundException("Product does not exist with mentioned productId.");
    }


    //=================================================View a all products By Admin Account==================================

    @Cacheable(cacheNames = "getAllProductsByAdmin")
    public List<ProductAdminDto> getAllProductsByAdmin(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Principal principal) {

        log.info("inside getAllProductsByAdmin method");


        Sort.Direction sortingDirection = sortDirection.get().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Product> productList = productRepository.findAll(PageRequest.of(page.get(), contentSize.get(), sortingDirection, sortProperty.get()));

        if (productList.getTotalElements() < 1)
            throw new ResourceNotFoundException("No products available in product List.");


        List<Product> products = productList.getContent();

        Type listType = new TypeToken<List<ProductAdminDto>>() {
        }.getType();

        List<ProductAdminDto> productAdminDtoList = modelMapper.map(products, listType);

        auditService.readAllObjects("Product", principal.getName());

        return productAdminDtoList;
        //   return new ResponseEntity(productAdminDtoList, null, HttpStatus.OK);

    }

    //=================================================Get similar products By Customer =========================================================

    public ResponseEntity getSimilarProductsByCustomer(Optional<Integer> page, Optional<Integer> contentSize, Optional<String> sortProperty, Optional<String> sortDirection, Long productId, Principal principal) {

        log.info("inside getSimilarProductsByCustomer method");


        if (!productRepository.findById(productId).isPresent())
            throw new ResourceNotFoundException("Product with mentioned productId is not exist.");

        Sort.Direction sortingDirection = sortDirection.get().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Product product = productRepository.findById(productId).get();
        List<Product> similarProducts = productRepository.findByCategory(product.getCategory().getId(), PageRequest.of(page.get(), contentSize.get(), sortingDirection, sortProperty.get()));

        Type listType = new TypeToken<List<ProductCustomerDto>>() {
        }.getType();


        List<ProductCustomerDto> similarProductDtoList = modelMapper.map(similarProducts, listType);

        auditService.readAllObjects("Product", principal.getName());

        return new ResponseEntity(similarProductDtoList, null, HttpStatus.OK);

    }
}
