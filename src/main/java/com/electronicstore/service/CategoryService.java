package com.electronicstore.service;

import com.electronicstore.dto.CategoryDto;
import com.electronicstore.dto.PageableResponse;
import com.electronicstore.exceptions.Response;

import java.util.List;

public interface CategoryService {
    //create
    Response<CategoryDto> create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //delete
    Object delete(String categoryId);

    //get all
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category detail
    CategoryDto get(String categoryId);

    //search Title
    List<CategoryDto> searchTitle(String keyword);
}
