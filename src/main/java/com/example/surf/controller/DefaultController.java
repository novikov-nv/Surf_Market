package com.example.surf.controller;

import com.example.surf.entity.Product;
import com.example.surf.entity.ProductType;
import com.example.surf.repository.ProductRepository;
import com.example.surf.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {
    @Autowired
    private final ProductTypeRepository productTypeRepository;
    @Autowired
    private final ProductRepository productRepository;

    public DefaultController(ProductTypeRepository productTypeRepository, ProductRepository productRepository) {
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        Iterable<ProductType> types= productTypeRepository.findAll();
        Map<ProductType, List<Product>> map = new HashMap<>();
        types.forEach(type-> map.put(type,productRepository.findByProductType(type)));
        model.addAttribute("map",map);
        return "index";
    }

    @GetMapping("/product")
    public String product(@RequestParam("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/productTypeList")
    public String productTypeList(Model model) {
        Iterable<ProductType> types = productTypeRepository.findAll();
        model.addAttribute("types", types);
        return "productTypeList";
    }

    @GetMapping("productTypeList/add")
    public String productTypeListAdd(Model model) {
        ProductType productType = new ProductType();
        model.addAttribute("productType", productType);
        return "productTypeForm";
    }

    @PostMapping("productTypeList/add")
    public String productTypeListAddSubmit(@ModelAttribute ProductType productType, Model model){
        productTypeRepository.save(productType);
        model.addAttribute("types", productTypeRepository.findAll());
        return "productTypeList";
    }

    @GetMapping("/productTypeList/delete/{productTypeId}")
    public String productTypeListDelete(@PathVariable("productTypeId") long id, Model model) {
        productTypeRepository.deleteById(id);
        model.addAttribute("types", productTypeRepository.findAll());
        return "productTypeList";
    }

    @GetMapping("/productTypeList/edit/{productTypeId}")
    public String productTypeListEdit(@PathVariable("productTypeId") long id, Model model) {
        ProductType productType = productTypeRepository.findById(id).orElse(null);
        model.addAttribute("productType", productType);
        return "productTypeForm";
    }
}
