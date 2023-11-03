package com.electronicstore.service.serviceimpl;



import com.electronicstore.dto.CategoryDto;
import com.electronicstore.dto.PageableResponse;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.helper.Helper;
import com.electronicstore.model.Category;
import com.electronicstore.repository.CategoryRepository;
import com.electronicstore.service.CategoryService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;



    @Autowired
    private ModelMapper mapper;

    @Override
    public  Response<CategoryDto> create(CategoryDto categoryDto) {
        try{
            Category category = mapper.map(categoryDto, Category.class);
            category.setCategoryId(UUID.randomUUID().toString());
            Category savedCategory = categoryRepository.save(category);
            CategoryDto dto = mapper.map(savedCategory, CategoryDto.class);
            Response<CategoryDto> response = new Response<>("Category saved Successfully", "1", dto);
            return response;
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in create of CategoryServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }


    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        try{
            logger.info("Inside update of CategoryServiceImpl");
            //get category of given id
            Optional<Category>findById = categoryRepository.findById(categoryId);
            if(findById.isPresent()){
                Category category = findById.get();

                //update category details
                category.setTitle(categoryDto.getTitle());
                category.setDescription(categoryDto.getDescription());
                category.setCoverImage(categoryDto.getCoverImage());
                Category updatedCategory = categoryRepository.save(category);
                CategoryDto dto = mapper.map(updatedCategory, CategoryDto.class);

                return new Response<>("Category updated", "1", dto).getData();
            }
            throw new IdNotFoundException("Category not found for given Id");
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in update of CategoryServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public Object delete(String categoryId) {
        try{
            logger.info("Inside delete of CategoryServiceImpl");
            //get category of given id
            Optional<Category>findById = categoryRepository.findById(categoryId);
            if(findById.isEmpty()){
                throw  new IdNotFoundException("CategoryId doesn't exist");
            }
            Category category = findById.get();
            categoryRepository.delete(category);
            return new Response<>("Category deleted successfully", "1", category);
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in delete of CategoryServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        try{
            logger.info("Inside getAll of CategoryServiceImpl");
            Sort sort=(sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
            Page<Category> page = categoryRepository.findAll(pageable);
            PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
            Response<PageableResponse<CategoryDto>> response1 = (Response<PageableResponse<CategoryDto>>) new Response<>("Category fetched successfully", "1", response);
            return response1.getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getAll of CategoryServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public CategoryDto get(String categoryId) {
        try{
            logger.info("Inside get of CategoryServiceImpl");
            //get category of given id
            Optional<Category>findById = categoryRepository.findById(categoryId);
            if(findById.isEmpty()){
                throw  new IdNotFoundException("CategoryId doesn't exist");
            }
            Category category = findById.get();
            CategoryDto dto = mapper.map(category, CategoryDto.class);

            return new Response<>("Category fetched successfully", "1",dto).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in get of CategoryServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public List<CategoryDto> searchTitle(String keyword) {
        try {
            logger.info("Inside searchTitle of CategoryServiceImpl");
            List<Category> title = categoryRepository.findByTitleContaining(keyword);
            List<CategoryDto> dtoList = title.stream().map(titles -> mapper.map(title, CategoryDto.class)).collect(Collectors.toList());
            return new Response<>("Category search successfully by Title", "1",dtoList).getData();


        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in searchUser of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }



}
