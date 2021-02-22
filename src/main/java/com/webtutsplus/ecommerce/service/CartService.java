package com.webtutsplus.ecommerce.service;

import com.webtutsplus.ecommerce.dto.CartDto;
import com.webtutsplus.ecommerce.dto.ProductDto;
import com.webtutsplus.ecommerce.model.*;
import com.webtutsplus.ecommerce.repository.CartRepository;
import com.webtutsplus.ecommerce.repository.WishListRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public static Cart getCartFromDto(CartDto cartDto, Product product,int userId) {
        Cart cart = new Cart(cartDto, product,userId);
        return cart;
    }

    public void addToCart(CartDto cartDto, Product product,int userId) {
        Cart cart = getCartFromDto(cartDto, product,userId);
        cartRepository.save(cart);
    }

    public CartCost listCartItems(int user_id) {
        List<Cart> cartList = cartRepository.findAllByUserIdOrderByCreatedDateDesc(user_id);
        List<CartDto> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartDto cartDto = getDtoFromCart(cart);
            cartItems.add(cartDto);
        }
        double totalCost = 0;
        for (CartDto cartDto:cartItems){
            totalCost += (cartDto.getProduct().getPrice()* cartDto.getQuantity());
        }
        CartCost cartCost = new CartCost(cartItems,totalCost);
        return cartCost;
    }


    public static CartDto getDtoFromCart(Cart cart) {
        CartDto cartDto = new CartDto(cart);
        return cartDto;
    }

    public void updateCartItem(int itemId, CartDto cartDto, Product product, int userId, int quantity) {
        Cart cart = getCartFromDto(cartDto, product);
        cart.setQuantity(quantity);
        cart.setId(itemId);
        cart.setUserId(userId);
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }
    public static Cart getCartFromDto(CartDto cartDto, Product product) {
        Cart cart = new Cart(cartDto, product);
        return cart;
    }

    public String deleteCartItem(int id,int userId){
        if(cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return "Success";
        }
        return "Failure";
    }

}

