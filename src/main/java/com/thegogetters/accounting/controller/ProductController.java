package com.thegogetters.accounting.controller;

import com.thegogetters.accounting.dto.ProductDTO;
import com.thegogetters.accounting.enums.ProductUnit;
import com.thegogetters.accounting.service.CategoryService;
import com.thegogetters.accounting.service.CompanyService;
import com.thegogetters.accounting.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CompanyService companyService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CompanyService companyService, CategoryService categoryService) {
        this.productService = productService;
        this.companyService = companyService;
        this.categoryService = categoryService;
    }

    //display all products in the product_list page.
    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProductsByCompany());
//        model.addAttribute("products", productService.listAllProducts());
        return "product/product-list";
    }

    //create new product
    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("newProduct", new ProductDTO());
        model.addAttribute("categories", categoryService.listCategories());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        return "product/product-create";
    }

    //save new created product
    @PostMapping("/create")
    public String insertProduct(@Valid @ModelAttribute("newProduct") ProductDTO productDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.listAllProducts());
            model.addAttribute("companies", companyService.listAll());
            model.addAttribute("categories", categoryService.listCategories());
            return "product/product-create";
        }
        productService.save(productDTO);
        return "redirect:/products/list";
    }

    //get product by Id for update
    @GetMapping("/update/{id}")
    public String getUpdateProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        model.addAttribute("categories", categoryService.listCategories());
//        model.addAttribute("categories", categoryService.findById(id));
        return "product/product-update";
    }

    //update product by Id
    @PostMapping("/update/{id}")
    public String updateProduct(@ModelAttribute("product") ProductDTO product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            model.addAttribute("categories", categoryService.listCategories());
            return "product/product-update";
        }
        productService.update(product);
        return "redirect:/products/list";
    }

    //delete product by Id
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (productService.isInStock(id)) {
            redirectAttributes.addFlashAttribute("error", "This product is still in stock, so you can not delete");
            return "redirect:/products/list";
        }
        productService.deleteById(id);
        return "redirect:/products/list";
    }
}
