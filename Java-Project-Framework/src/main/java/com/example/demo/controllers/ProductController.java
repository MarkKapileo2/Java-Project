package com.example.demo.controllers;

import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.domain.Part;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    private final PartService partService;


    public ProductController(ProductService productService, PartService partService) {
        this.productService = productService;
        this.partService = partService;
    }

    @GetMapping("/products/buy/{id}")
    public String buyNow(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id.intValue());

        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/mainscreen";
        }

        if (product.getInv() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Purchase failed. Product is out of stock.");
            return "redirect:/mainscreen";
        }


        for (Part part : product.getParts()) {
            int remaining = part.getInv() - 1;
            if (remaining < part.getMin()) {
                redirectAttributes.addFlashAttribute("error",
                        "Cannot purchase. Part '" + part.getName() + "' inventory would drop below minimum.");
                return "redirect:/mainscreen";
            }
        }


        for (Part part : product.getParts()) {
            part.setInv(part.getInv() - 1);


            partService.save(part);
        }

        product.setInv(product.getInv() - 1);
        productService.save(product);

        redirectAttributes.addFlashAttribute("message", "Successfully purchased: " + product.getName());
        return "redirect:/mainscreen";
    }
}
