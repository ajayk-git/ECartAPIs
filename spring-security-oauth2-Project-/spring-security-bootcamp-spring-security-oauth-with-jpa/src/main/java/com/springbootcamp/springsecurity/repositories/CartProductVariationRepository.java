package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.CartProductVariation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductVariationRepository extends CrudRepository<CartProductVariation,Long> {

    List<CartProductVariation> findByCart(Cart cart);

    @Query(value = "select * from CartProductVariation where CartId=:cartId AND ProductVariationId=:productId",nativeQuery = true)
    CartProductVariation findByProductVariantAndCart(Long productId,Long cartId);

//    @Query(value = " delete from CartProductVariation where id=:id",nativeQuery = true)
//    void deleteByCartIdAndProductId(Long id);

    @Modifying
    @Query(value = " delete from CartProductVariation  where CartId=:cartId AND ProductVariationId=:productVariationId",nativeQuery = true)
    void deleteByCartIdAndProductVariationId(Long cartId, Long productVariationId);
}
