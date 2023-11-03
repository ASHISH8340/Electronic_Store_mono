package com.electronicstore.controller;



import com.electronicstore.dto.CategoryDto;
import com.electronicstore.dto.ImageResponse;
import com.electronicstore.dto.PageableResponse;
import com.electronicstore.dto.ProductDto;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.service.CategoryService;
import com.electronicstore.service.FileService;
import com.electronicstore.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductService productService;

    @Value("${category.image.path}")
    private String imageUploadPath;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Response<CategoryDto>> create(@Valid @RequestBody CategoryDto categoryDto){
        logger.info("Request for create :{}", categoryDto);
        return new ResponseEntity<Response<CategoryDto>>(categoryService.create(categoryDto), HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(@PathVariable String categoryId,@Valid @RequestBody CategoryDto categoryDto){
        if(categoryId == null){
            throw new IdNotFoundException("Kindly provide categoryId");
        }
        logger.info("updating Category and passing ok status");
        return new ResponseEntity<>(categoryService.update(categoryDto,categoryId),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> delete(@PathVariable String categoryId){
        if(categoryId == null){
            throw new IdNotFoundException("Kindly provide categoryId");
        }
        logger.info("Request for delete category:{}",categoryId);
        return new ResponseEntity<>(categoryService.delete(categoryId),HttpStatus.GONE);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        logger.info("Request for get all category");
        return new ResponseEntity<>(categoryService.getAll(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

        @GetMapping("/{categoryId}")
        public ResponseEntity<CategoryDto> get(@PathVariable String categoryId){
            logger.info("Request for get category By Id !!");
            return new ResponseEntity<>(categoryService.get(categoryId),HttpStatus.OK);

        }


    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("categoryImage") MultipartFile image, @PathVariable String categoryId
    ){
        logger.info("Request for uploadCategoryImage");
        String imageName = fileService.uploadFile(image, imageUploadPath);
        CategoryDto category = categoryService.get(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto = categoryService.update(category, categoryId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image Successfully uploaded !!").success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //Server Category Image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.get(categoryId);
        logger.info("Category image name:{}",category.getCoverImage());

        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto dto
    ) {
        ProductDto productWithCategory = productService.createWithCategory(dto, categoryId);
        return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
    }


    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryOfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ) {
        if(categoryId  == null && productId == null){
            throw new IdNotFoundException("Kindly provide categoryId & productId");
        }
        logger.info("updating Category, Product and passing ok status");
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }


    //get products of categories
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {

        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }




}
