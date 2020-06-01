package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.CartProductVariation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartProductVariationRepository extends CrudRepository<CartProductVariation,Long> {

    List<CartProductVariation> findByCart(Cart cart);

    @Query(value = "select * from CartProductVariation where CartId=:cartId AND ProductVariationId=:productId",nativeQuery = true)
    CartProductVariation findByProductVariantAndCart(Long productId,Long cartId);

    @Modifying
    @Query(value = " delete from CartProductVariation  where CartId=:cartId AND ProductVariationId=:productVariationId",nativeQuery = true)
    void deleteByCartIdAndProductVariationId(Long cartId, Long productVariationId);

    @Query(value = "select * from CartProductVariation where isWishListItem= true",nativeQuery = true)
    List<CartProductVariation> findByIsWishListItem();

    @Query(value = "select * from CartProductVariation where CartId=:cartId AND isWishListItem= true",nativeQuery = true)
    List<CartProductVariation> findByCartIdAndWishListProducts(Long cartId);



    @Transactional
    @Modifying
    @Query(value = " delete from CartProductVariation  where CartId=:cartId AND ProductVariationId=:productVariationId",nativeQuery = true)
    void deleteByCartProductVariation(Long cartId, Long productVariationId);
}
