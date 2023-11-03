package com.electronicstore.service.serviceimpl;


import com.electronicstore.dto.PageableResponse;
import com.electronicstore.dto.ProductDto;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.helper.Helper;
import com.electronicstore.model.Category;
import com.electronicstore.model.Product;
import com.electronicstore.repository.CategoryRepository;
import com.electronicstore.repository.ProductRepository;
import com.electronicstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;





    @Override
    public ProductDto create(ProductDto productDto) {
        try{
            Product product = mapper.map(productDto, Product.class);
            //product id
            String productId = UUID.randomUUID().toString();
            product.setProductId(productId);
            //added
            product.setAddedDate(new Date());
            Product savedProduct = productRepository.save(product);
            ProductDto dto = mapper.map(savedProduct, ProductDto.class);
            return new Response<>("Product saved Successfully", "1", dto).getData();

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in create of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        try {
            logger.info("Inside update of ProductServiceImpl");
            Optional<Product> findById = productRepository.findById(productId);
            if(findById.isPresent()){
                Product product = findById.get();
                product.setTitle(productDto.getTitle());
                product.setDescription(productDto.getDescription());
                product.setPrice(productDto.getPrice());
                product.setDiscountedPrice(productDto.getDiscountedPrice());
                product.setQuantity(productDto.getQuantity());
                product.setLive(productDto.isLive());
                product.setStock(productDto.isStock());
                product.setProductImageName(productDto.getProductImageName());

                Product updateProduct = productRepository.save(product);
                ProductDto dto = mapper.map(updateProduct, ProductDto.class);

                return new Response<>("Product updated", "1", dto).getData();

            }
            throw new IdNotFoundException("Product not found for given Id");

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in update of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public Object delete(String productId) {
        try{
            logger.info("Inside delete of ProductServiceImpl");
            Optional<Product> findById = productRepository.findById(productId);
            if(findById.isEmpty()){
                throw  new IdNotFoundException("ProductId doesn't exist");
            }
            Product product = findById.get();
            productRepository.delete(product);
            return new Response<>("Product deleted successfully", "1", product);
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in delete of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public ProductDto get(String productId) {
        try{
            logger.info("Inside delete of ProductServiceImpl");
            Optional<Product> findById = productRepository.findById(productId);
            if(findById.isEmpty()){
                throw  new IdNotFoundException("ProductId doesn't exist");
            }
            Product product = findById.get();
            ProductDto map = mapper.map(product, ProductDto.class);

            return new Response<>("Product fetched successfully", "1",map).getData();

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in get of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        try{
            logger.info("Inside getAll of ProductServiceImpl");
            Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
            Page<Product> page = productRepository.findAll(pageable);
            PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
            return new Response<>("Product fetched successfully", "1",response).getData();

        }catch (Exception e) {
            String errorMsg = MessageFormat.format("Exception caught in getAll of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        try{
            logger.info("Inside getAllLive of ProductServiceImpl");
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Product> page = productRepository.findByLiveTrue(pageable);
            PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
            return new Response<>("Product fetched successfully", "1",response).getData();

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getAllLive of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        try{
            logger.info("Inside searchByTitle of ProductServiceImpl");
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
            PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
            return new Response<>("Product fetched successfully", "1",response).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in searchByTitle of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        try{
            logger.info("Inside createWithCategory of ProductServiceImpl");
            Optional<Category> findById=categoryRepository.findById(categoryId);
            if(findById.isPresent()){
                Category category = findById.get();

                Product product = mapper.map(productDto, Product.class);
                //product id
                String productId = UUID.randomUUID().toString();
                product.setProductId(productId);
                //added
                product.setAddedDate(new Date());
                product.setCategory(category);
                Product savedProduct = productRepository.save(product);
                ProductDto dto = mapper.map(savedProduct, ProductDto.class);
                return new Response<>("Product saved Successfully", "1", dto).getData();
            }
            throw new IdNotFoundException("Category not found for given Id");
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in createWithCategory of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        try{
            Optional<Product> findByProductId =productRepository.findById(productId);
            Optional<Category> findByCategoryId = categoryRepository.findById(categoryId);
            if(findByProductId.isPresent() && findByCategoryId.isPresent()){
                Product product = findByProductId.get();
                Category category = findByCategoryId.get();
                product.setCategory(category);
                Product savedProduct = productRepository.save(product);
                ProductDto dto = mapper.map(savedProduct, ProductDto.class);
                return new Response<>("Product updated Successfully", "1", dto).getData();
            }
            throw new IdNotFoundException("Category & Product not found for given Id");

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in updateCategory of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        try{
            Optional<Category> findByCategoryId = categoryRepository.findById(categoryId);
            if(findByCategoryId.isPresent()){
                Category category = findByCategoryId.get();
                Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
                Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
                Page<Product> page = productRepository.findByCategory(category, pageable);
                return Helper.getPageableResponse(page, ProductDto.class);
            }
            throw new IdNotFoundException("Category not found for given Id");

        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getAllOfCategory of ProductServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }


    }

}
