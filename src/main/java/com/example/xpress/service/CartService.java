package com.example.xpress.service;

import com.example.xpress.entities.Cart;
import com.example.xpress.entities.CartItem;
import com.example.xpress.entities.Product;
import com.example.xpress.entities.Users;
import com.example.xpress.repository.CartItemRepository;
import com.example.xpress.repository.CartRepository;
import com.example.xpress.repository.ProductRepository;
import com.example.xpress.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Iterator;
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
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    public Cart addProductToCart(String token, Long productId, int quantity){
        Users user = tokenService.getUserByToken(token);
        if(user == null){
            throw new RuntimeException("User not found");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found."));
        boolean isProductExistsOnCart = false;

        for(CartItem item : user.getCart().getItems()){
            if(item.getProduct().getId().equals(productId)){
                if(quantity > product.getQttStock()){
                    throw new RuntimeException("Requested quantity exceeds available stock.");
                }else{
                    item.setQuantity(item.getQuantity() + quantity);
                }
                user.getCart().setTotalPrice(user.getCart().getTotalPrice() + (product.getPrice() * quantity));
                item.setPrice(item.getPrice() + (product.getPrice() * quantity));

                user.getCart().setTotalQuantity(user.getCart().getTotalQuantity() + quantity);
                cartRepository.save(user.getCart());
                isProductExistsOnCart = true;
                break;
            }
        }

        if(!isProductExistsOnCart){
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(user.getCart());
            newCartItem.setPrice(product.getPrice() * quantity);
            if(quantity > product.getQttStock()){
                throw new RuntimeException("Requested quantity exceeds available stock.");
            }else{
                newCartItem.setQuantity(quantity);
            }

            user.getCart().getItems().add(newCartItem);
            user.getCart().setTotalPrice(user.getCart().getTotalPrice() + newCartItem.getPrice());
            user.getCart().setTotalQuantity(user.getCart().getTotalQuantity() + quantity);
            cartRepository.save(user.getCart());
        }

        return user.getCart();
    }

    public CartItem removeItemFromCart(String token,Long pId , int qttRemove) {
        Users user = tokenService.getUserByToken(token);
        CartItem modifiedCartItem = null;

        if(qttRemove < 0){
            throw new RuntimeException("the quantity to remove cannot be less than 0");
        }

        List<CartItem> cartItems = user.getCart().getItems();
        Iterator<CartItem> iterator = cartItems.iterator();

        while(iterator.hasNext()){
            CartItem cartItem = iterator.next();

            if(cartItem.getProduct().getId().equals(pId)){
                int currentQtt = cartItem.getQuantity();

                if(qttRemove >= currentQtt){
                    iterator.remove();
                }else{
                    cartItem.setQuantity(currentQtt - qttRemove);
                    cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
                }

                modifiedCartItem = cartItem;
            }
        }

        int totalQuantity = 0;
        double totalPrice = 0.0;

        for(CartItem cartItem : user.getCart().getItems()){
            totalQuantity += cartItem.getQuantity();
            totalPrice += cartItem.getPrice();
        }

        user.getCart().setTotalPrice(totalPrice);
        user.getCart().setTotalQuantity(totalQuantity);

        userRepository.save(user);
        cartRepository.save(user.getCart());

        if(modifiedCartItem != null){
            return modifiedCartItem;
        }

        throw new RuntimeException("Product not found in the cart");
    }
}