package com.tafh.myfin_app.category.controller;

import com.tafh.myfin_app.category.service.CategoryService;
import com.tafh.myfin_app.category.dto.CategoryRequest;
import com.tafh.myfin_app.category.dto.CategoryResponse;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.MetaMapper;
import com.tafh.myfin_app.common.dto.MetaResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final MetaMapper metaMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseHelper.created(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories(
            @RequestParam(required = false) CategoryType type,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        Page<CategoryResponse> page = categoryService.getCategories(type, keyword, pageable);
        MetaResponse meta = metaMapper.buildMetaResponse(page);
        List<CategoryResponse> categories = page.getContent();

        return ResponseHelper.ok(categories, meta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable String id) {
        return ResponseHelper.ok(categoryService.getCategory(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody CategoryRequest request
    ) {
        return ResponseHelper.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        categoryService.deleteById(id);
        return ResponseHelper.ok(null, "Category has been deleted");
    }
}
