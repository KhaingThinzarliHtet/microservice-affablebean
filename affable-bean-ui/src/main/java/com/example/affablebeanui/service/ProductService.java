package com.example.affablebeanui.service;

import com.example.affablebeanui.entity.Product;
import com.example.affablebeanui.entity.Products;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.tags.EditorAwareTag;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    public static final int DELIVERY_CHANGE = 3;
    //    @Value("${backend.url}")
//    private String baseUrl;
    private final CartService cartService;


    private List<Product> products;
    private RestTemplate restTemplate = new RestTemplate();

    record TransferData(String to_email, String from_email, double amount){}

    public ResponseEntity saveCartItem(){
        return restTemplate.getForEntity("http://localhost:8070/transport/cart/save", String.class);
    }

    public ResponseEntity transfer(String to_email, String from_email, double amount){
        var data = new TransferData(to_email, from_email, amount+ DELIVERY_CHANGE);
        return restTemplate.postForEntity("http://localhost:8095/account/transfer", data, String.class);
    }


    public List<Product> findProductByCategory(int categoryId){
        return products.stream()
                .filter(p -> p.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
    }

    public Product purchaseProduct(int id){
        Product product = findProduct(id);
        cartService.addToCart(product);
        return product;
    }

    private Product findProduct(int id){
        return products.stream()
                .filter(p -> p.getId() == id)
                .findAny()
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ProductService(final CartService cartService){
        this.cartService = cartService;
        var productResponseEntity = restTemplate.getForEntity(  "http://localhost:8090/backend/products", Products.class);
        if(productResponseEntity.getStatusCode().is2xxSuccessful()){
            products = productResponseEntity.getBody().getProducts();
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    public List<Product> showAllProducts(){
        return products;
    }
}
