package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImplementation implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Map: Product to ProductDTO
    private ProductDTO productToProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setPrice(product.getPrice());
        productDTO.setDiscount(product.getDiscount());
        productDTO.setSpecialPrice(product.getSpecialPrice());

        return productDTO;
    }

    // Map: Cart to CartDTO
    private CartDTO cartToCartDTO(Cart cart){
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        return cartDTO;
    }

    // Map CartDTO to Cart
    private Cart cartDtoToCart(CartDTO cartDTO){
        Cart cart = new Cart();
        cart.setCartId(cartDTO.getCartId());
        cart.setTotalPrice(cartDTO.getTotalPrice());
        return cart;
    }

    @Transactional
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // existing cart or create new one
        Cart cart = createCart();

        // retrieve product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // perform validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if(cartItem != null){
            throw new APIException("Product "+ product.getProductName()+" already exist in the cart");
        }

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName()+" is not available");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("Please, make an order of the product "+ product.getProductName()
            + " less than or equal to the quantity: "+product.getQuantity());
        }

        // create cart item
        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        // save cart item
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity() - quantity);

        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice() * quantity);
        cart.getCartItemList().add(newCartItem);
        cartRepository.save(cart);
        productRepository.save(product);

        //return updated cart
        CartDTO cartDTO =  cartToCartDTO(cart);

        // getting the product list from cartItem and then mapping the product to productDTO
        List<CartItem> cartItems = cart.getCartItemList();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                        .map(item -> {
                            ProductDTO map = productToProductDTO(item.getProduct());
                            map.setQuantity(item.getQuantity());
                            return map;
                        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.isEmpty()){
            throw new APIException("No Cart Exist");
        }

        List<CartDTO> cartDTOList = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = cartToCartDTO(cart);
                    List<ProductDTO> productDTOList = cart.getCartItemList().stream()
                            .map(cartItem -> productToProductDTO(cartItem.getProduct())).toList();
                    cartDTO.setProducts(productDTOList);
                    return cartDTO;
                }).toList();

        return cartDTOList;
    }

    @Transactional
    @Override
    public CartDTO getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartDTO cartDTO = cartToCartDTO(cart);
        List<ProductDTO> productDTOList = cart.getCartItemList().stream()
                .map(cartItem -> productToProductDTO(cartItem.getProduct())).toList();
        cartDTO.setProducts(productDTOList);

        return cartDTO;
    }

    private Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null){
            return userCart;
        }
        Cart cart = new Cart();
        cart.setUser(authUtil.loggedInUser());
        cart.setTotalPrice(0.0);
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
