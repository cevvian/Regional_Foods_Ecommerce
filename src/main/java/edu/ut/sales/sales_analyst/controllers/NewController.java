package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ImageOfNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.NewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<NewResponse> createNews(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String categoryId,
            @RequestParam List<String> typeContents,
            @RequestPart List<MultipartFile> files
    ) {
        List<ImageOfNewCreateRequest> images = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            ImageOfNewCreateRequest image = new ImageOfNewCreateRequest();
            image.setTypeContent(typeContents.get(i));
            image.setFile(files.get(i));
            images.add(image);
        }

        NewCreateRequest request = new NewCreateRequest();
        request.setTitle(title);
        request.setContent(content);
        request.setCategoryId(categoryId);
        request.setImages(images);

        NewResponse response = newService.createNews(request);
        return new ResponseAPI<>("Create news successfully", HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseAPI<List<NewResponse>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewResponse> newsPage = newService.getAllNews(pageable);

        PageMeta meta = PageMeta.builder()
                .page(newsPage.getNumber())
                .size(newsPage.getSize())
                .totalElements(newsPage.getTotalElements())
                .totalPages(newsPage.getTotalPages())
                .last(newsPage.isLast())
                .build();

        return new ResponseAPI<>("Get all news successfully", HttpStatus.OK, newsPage.getContent(), meta);
    }

    @GetMapping("/{id}")
    public ResponseAPI<NewResponse> getNews(@PathVariable String id) {
        NewResponse response = newService.getNews(id);
        return new ResponseAPI<>("Get news successfully", HttpStatus.OK, response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<NewResponse> updateNews(
            @PathVariable String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false, name = "categoryId") String categoryId,
            @RequestParam(required = false) List<String> typeContents,
            @RequestPart(required = false) List<MultipartFile> files
    ) {
        List<ImageOfNewCreateRequest> images = new ArrayList<>();

        if (files != null && typeContents != null && files.size() == typeContents.size()) {
            for (int i = 0; i < files.size(); i++) {
                ImageOfNewCreateRequest image = new ImageOfNewCreateRequest();
                image.setTypeContent(typeContents.get(i));
                image.setFile(files.get(i));
                images.add(image);
            }
        }

        NewUpdateRequest request = new NewUpdateRequest();
        request.setTitle(title);
        request.setContent(content);
        request.setCategoryId(categoryId);
        request.setImages(images);

        NewResponse response = newService.updateNews(id, request);
        return new ResponseAPI<>("Update news successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<Boolean> deleteNews(@PathVariable String id) {
        boolean deleted = newService.deleteNews(id);
        if (deleted) {
            return new ResponseAPI<>("Delete news successfully", HttpStatus.OK, true);
        } else {
            throw new RuntimeException("Delete news failed");
        }
    }

    @GetMapping("/by-category")
    public ResponseAPI<List<NewResponse>> getAllNewsByCategory(
            @RequestParam String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewResponse> newsPage = newService.getNewsByCategory(categoryId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(newsPage.getNumber())
                .size(newsPage.getSize())
                .totalElements(newsPage.getTotalElements())
                .totalPages(newsPage.getTotalPages())
                .last(newsPage.isLast())
                .build();

        return new ResponseAPI<>("Get news by category successfully", HttpStatus.OK, newsPage.getContent(), meta);
    }

}
