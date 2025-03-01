package com.springbootcamp.springsecurity.controllers;

import com.springbootcamp.springsecurity.GlobalVariables;
import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.ProductService;
import com.springbootcamp.springsecurity.services.SellerService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Api(value = "Seller Rest Controller performs operations related to Seller")
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    SellerService sellerService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;




    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To view a seller's profile .")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/profile")
    public SellerDto viewSellerProfile(Principal principal) {
        return sellerService.viewSellerProfile(principal.getName(),principal);
    }

    //==============================================get seller Address==================================================================

    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To view seller's  address,")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/address")
    public AddressDto getAddressSeller(Principal principal){
        return sellerService.getAddressSeller(principal.getName(),principal);
    }

    //==================================update seller password===========================================================

    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To update seller's password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PatchMapping("/password")
    public ResponseEntity<String> updateCustomerPassword(@Valid @RequestBody PasswordUpdateCO passwordUpdateCO, Principal principal){
        return sellerService.updateSellerPassword(passwordUpdateCO,principal.getName(),principal);
    }

    //=================================================Update Profile  of seller Account==================================

    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To update seller's  profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PatchMapping("/profile")
    public ResponseEntity<String> updateCustomerProfile(Principal principal, @RequestBody SellerProfileUpdateCO sellerProfileUpdateCO){
        return sellerService.upadteSellerProfile(principal.getName(),sellerProfileUpdateCO,principal);
    }

    //=================================================Update Address  of seller Account==================================

    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To update seller's  Address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateSellerAddress(@PathVariable("id") Long id,
                                                      @RequestBody  AddressCO addressCO,Principal principal){
        return sellerService.updateSellerAddress(addressCO,id,principal.getName(),principal);
    }

    //=================================================Add a new Product By seller Account==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To Add new product by seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping("/product")
    public ResponseEntity addNewProduct(@Valid @RequestBody ProductCo productCo,Principal principal){
        return productService.addNewProduct(productCo,principal);
    }

    //=================================================View Category(ies) by seller==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "View Category(ies) by seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/categories")
    public List<CategorySellerDto> viewAllCategoriesBySeller(Principal principal){
        return categoryService.viewAllCategoriesBySeller(principal);
    }

    //=================================================View  Product-Variant By seller Account==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "View Product Variant by seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/product/variation/{productVariantId}")
    public ProductVariantDto getProductVariantBySeller(@PathVariable(name = "productVariantId") Long productVariantId,Principal principal){
        return productService.getProductVariantBySeller(productVariantId,principal);
    }

    //=================================================View all product Variants of a product By seller Account==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "View all product Variants of a product By seller.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/product/{productId}/variation")
    public ResponseEntity viewAllProductVariationsBySeller(@RequestParam(value = "page",defaultValue = GlobalVariables.DEFAULT_PAGE_OFFSET)Optional<Integer> page,
                                                           @RequestParam(value = "size",defaultValue = GlobalVariables.DEFAULT_PAGE_SIZE) Optional<Integer> contentSize,
                                                           @RequestParam(value = "sort",defaultValue = GlobalVariables.DEFAULT_SORT_PROPERTY)Optional<String> sortProperty,
                                                           @RequestParam(value = "direction",defaultValue = GlobalVariables.DEFAULT_SORT_DIRECTION)Optional<String> sortDirection,
                                                           @PathVariable(name = "productId") Long productId,Principal principal){

        return productService.viewAllProductVariationsBySeller(page,contentSize,sortProperty,sortDirection,productId,principal);
    }


    //=================================================Add a new  Product-Variant By seller Account==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To add a new  Product-Variant By seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping("/product/variation/{productId}")
    public ResponseEntity addNewProductVariant(@PathVariable(name = "productId") Long productId,Principal principal,
                                               @Valid @RequestBody ProductVariationCo productVariationCo){
        return productService.addNewProductVariant(productId,productVariationCo,principal);
    }



    //=====================================================Update a Product Variant  By seller ===============================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To update a Product Variant  By seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PatchMapping("product/variation/{variationId}")
    public ResponseEntity updateProductVariantBySeller(@PathVariable(name = "variationId") Long variationId,Principal principal,
                                                      @Valid @RequestBody ProductVariationUpdateCo variationUpdateCo){

        return productService.updateProductVariantBySeller(variationId,principal,variationUpdateCo);
    }


    //=================================================View a Product By seller Account==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To View a Product By seller.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/product/{productId}")
    public ProductSellerDto viewProductBySeller(@PathVariable(name = "productId") Long productId,Principal principal){
        return productService.viewProductBySeller(productId,principal);
    }

    //=================================================View all products By seller Account==================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To View all products By seller Account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/product")
    public List<ProductSellerDto> viewAllProductsBySeller(@RequestParam(value = "page",defaultValue = GlobalVariables.DEFAULT_PAGE_OFFSET)Optional<Integer> page,
                                                  @RequestParam(value = "size",defaultValue = GlobalVariables.DEFAULT_PAGE_SIZE) Optional<Integer> contentSize,
                                                  @RequestParam(value = "sort",defaultValue = GlobalVariables.DEFAULT_SORT_PROPERTY)Optional<String> sortProperty,
                                                  @RequestParam(value = "direction",defaultValue = GlobalVariables.DEFAULT_SORT_DIRECTION)Optional<String> sortDirection,
                                                  Principal principal){
        return productService.viewAllProductsBySeller(page,contentSize,sortProperty,sortDirection,principal);
    }

    //=================================================Update a product By seller =========================================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To Update a product By seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping("/product/{productId}")
    public ResponseEntity updateProductBySeller(@PathVariable(name = "productId") Long productId,Principal principal,
                                                @RequestBody ProductUpdateBySellerCo productUpdateBySellerCo){
        return productService.updateProductBySeller(productUpdateBySellerCo,productId,principal);
    }

    //=================================================Delete a Product By seller Account=====================================================
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_SELLER")
    @ApiOperation(value = "To Delete a Product By seller Account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping("/product/{productId}")
    public ResponseEntity deleteProductBySeller(@PathVariable(name = "productId") Long productId,Principal principal){
        return productService.deleteProductBySeller(productId,principal);
    }


}
