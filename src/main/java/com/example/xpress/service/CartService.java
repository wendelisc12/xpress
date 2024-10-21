package com.example.xpress.service;

import com.example.xpress.entities.Cart;
import com.example.xpress.entities.CartItem;
import com.example.xpress.entities.Product;
import com.example.xpress.repository.CartItemRepository;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Cart addProductToCart(Long cartId, Long productId, int quantity){
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found."));

        boolean isProductExistsOnCart = false;

        for(CartItem item : cart.getItems()){
            if(item.getProduct().getId().equals(productId)){
                if(quantity > product.getQttStock()){
                    throw new RuntimeException("Requested quantity exceeds available stock.");
                }else{
                    item.setQuantity(item.getQuantity() + quantity);
                    productService.downQttStock(product, quantity);
                    productRepository.save(product);
                }
                cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
                item.setPrice(item.getPrice() + (product.getPrice() * quantity));

                cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
                cartRepository.save(cart);
                isProductExistsOnCart = true;
                break;
            }
        }

        if(!isProductExistsOnCart){
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setPrice(product.getPrice() * quantity);
            if(quantity > product.getQttStock()){
                throw new RuntimeException("Requested quantity exceeds available stock.");
            }else{
                newCartItem.setQuantity(quantity);
                productService.downQttStock(product, quantity);
                productRepository.save(product);
            }

            cart.getItems().add(newCartItem);
            cart.setTotalPrice(cart.getTotalPrice() + newCartItem.getPrice());
            cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
            cartRepository.save(cart);
        }

        return cart;
    }
}