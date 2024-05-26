package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.OrderProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Services.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ProductController {
//    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ProductService productService;

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public ModelAndView addProduct(@ModelAttribute("product") @Valid Product product,
                                   BindingResult result,
                                   @RequestParam("imageFile") MultipartFile imageFile) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.setViewName("/shop/addProduct");
            return modelAndView;
        }else {//
            productService.addProduct(product);//
            modelAndView.setViewName("redirect:/shop/products");//
        }//
        return modelAndView;//
    }

//        if (!imageFile.isEmpty()) {
//            try {
//                String fileName = UUID.randomUUID().toString() + "-" + imageFile.getOriginalFilename();
//                Path path = Paths.get(UPLOAD_DIR + fileName);
//                Files.createDirectories(path.getParent());
//
//                Files.write(path, imageFile.getBytes());
//
//                product.setImageUrl("/" + UPLOAD_DIR + fileName);
//            } catch (IOException e) {
//                result.rejectValue("imageUrl", "error.product", "Неуспешно качване на файла");
//                modelAndView.setViewName("addProduct");
//                return modelAndView;
//            }
//        }
//
//        productService.addProduct(product);
//        modelAndView.setViewName("redirect:/shop/products");
//        return modelAndView;
//    }

    @GetMapping("/products")
    public String getProducts(@RequestParam(defaultValue = "name") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDirection,
                              Model model) {
        List<Product> products = productService.getSortedProducts(sortBy, sortDirection);
        model.addAttribute("products", products);
        model.addAttribute("sortDirection", sortDirection);
        return "products";
    }

    @PostMapping("/delete/{productId}")
    private ModelAndView deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
        return new ModelAndView("redirect:/shop/products");
    }

    @GetMapping("/editProduct/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/editProduct/update/{id}")
    public ModelAndView updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product) {
        productService.updateProduct(id, product);
        return new ModelAndView("redirect:/shop/products");
    }

    @PostMapping("/products")
    public String searchProducts(
            @RequestParam(name = "searchById", required = false) Long searchById,
            @RequestParam(name = "searchByName", required = false) String searchByName,
            @RequestParam(name = "searchByQuantity", required = false) Integer searchByQuantity,
            @RequestParam(name = "price-min", required = false) BigDecimal priceMin,
            @RequestParam(name = "price-max", required = false) BigDecimal priceMax,
            Model model) {
        if (priceMin != null) {
            priceMin = priceMin.setScale(2, RoundingMode.HALF_UP);
        }
        if (priceMax != null) {
            priceMax = priceMax.setScale(2, RoundingMode.HALF_UP);
        }
        List<Product> products = productService.searchProducts(searchById, searchByName, searchByQuantity,priceMin, priceMax);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/all")
    public String showShop(Model model, String keyword, OrderProduct orderProduct, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Product> products = productService.findAllAvailableQuantity(keyword);
        model.addAttribute("products", products);
        model.addAttribute("user", user);
        model.addAttribute("items", orderProduct.getQuantity());
        return "all";
    }
}
