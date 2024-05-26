package com.example.RailingShop.Services;

import com.example.RailingShop.Entities.DeletedProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import com.example.RailingShop.Exceptions.ResourceNotFoundException;
import com.example.RailingShop.Repositories.DeletedProductRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DeletedProductRepository deletedProductRepository;

    public List<Product> getSortedProducts(String sortBy, String sortDirection) {
        List<Product> products = productRepository.findAll();

        Comparator<Product> comparator = switch (sortBy) {
            case "name" -> Comparator.comparing(Product::getName);
            case "price" -> Comparator.comparing(Product::getPrice);
            default -> null;
        };

        if (comparator != null) {
            if ("desc".equals(sortDirection)) {
                comparator = comparator.reversed();
            }
            products.sort(comparator);
        }
        return products;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + productId));
        DeletedProduct deletedProduct = new DeletedProduct(product);
        deletedProductRepository.save(deletedProduct);
        productRepository.delete(product);
    }

    public void updateProduct(Long id, Product product) {
        if (!id.equals(product.getId())) {
            product.setId(id);
        }

        productRepository.save(product);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }

    public List<Product> searchProducts(Long id, String name, Integer quantity, BigDecimal priceMin, BigDecimal priceMax) {
        if (priceMin != null) {
            priceMin = priceMin.setScale(2, RoundingMode.HALF_UP);
        }
        if (priceMax != null) {
            priceMax = priceMax.setScale(2, RoundingMode.HALF_UP);
        }

        return productRepository.searchProducts(id, name, quantity, priceMin, priceMax);
    }

    public void save(Product product) {
        productRepository.save(product);
    }


    public List<Product> findAllAvailableQuantity(String keyword) {
        List<Product> availableProducts = new ArrayList<>();

        if (keyword != null) {
            List<Product> searchResults = productRepository.findByDescriptionContainingIgnoreCase(keyword);

            for (Product product : searchResults) {
                if (product.getQuantity() > 0) {
                    availableProducts.add(product);
                }
            }
        } else {
            List<Product> allProducts = productRepository.findAll();

            for (Product product : allProducts) {
                if (product.getQuantity() > 0) {
                    availableProducts.add(product);
                }
            }
        }

        return availableProducts;
    }
}
