package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.ImageNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.NewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseAPI<NewResponse> createNews(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String categoryId,
            @RequestParam List<String> typeContents,
            @RequestPart List<MultipartFile> files
    ) {
        try {
            List<ImageNewCreateRequest> images = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                ImageNewCreateRequest image = new ImageNewCreateRequest();
                image.setTypeContent(typeContents.get(i));
                image.setFile(files.get(i));
                images.add(image);
            }

            NewCreateRequest request = new NewCreateRequest();
            request.setTitle(title);
            request.setContent(content);
            request.setCategoryId(categoryId);
            request.setImages(images);

            System.out.println("🟡 Request gửi vào Service:");
            System.out.println(request);

            NewResponse response = newService.createNews(request);
            return new ResponseAPI<>("Create news successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


//    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseAPI<NewResponse> createNews(
//            @RequestPart("title") String title,
//            @RequestPart("content") String content,
//            @RequestPart("categoryId") String categoryId,
//            @RequestPart("images") List<ImageNewCreateRequest> images
//    ) {
//        try {
//            NewCreateRequest request = new NewCreateRequest();
//            request.setTitle(title);
//            request.setContent(content);
//            request.setImages(images);
//            request.setCategoryId(categoryId);
//
//            NewResponse response = newService.createNews(request);
//            return new ResponseAPI<>("Create news successfully", HttpStatus.CREATED, response);
//        } catch (AppException e) {
//            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
//    }


    @GetMapping
    public ResponseAPI<Page<NewResponse>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<NewResponse> response = newService.getAllNews(pageable);
            return new ResponseAPI<>("Get all news successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<NewResponse> getNews(@PathVariable String id) {
        try {
            NewResponse response = newService.getNews(id);
            return new ResponseAPI<>("Get news successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseAPI<NewResponse> updateNews(
            @PathVariable String id,
            @RequestPart(required = false) String title,
            @RequestPart(required = false) String content,
            @RequestPart(required = false, name = "categoryId") String categoryId,
            @RequestPart(required = false) List<ImageNewCreateRequest> images
    ) {
        try {
            NewUpdateRequest request = new NewUpdateRequest();
            request.setTitle(title);
            request.setContent(content);
            request.setImages(images);
            request.setCategoryId(categoryId);

            NewResponse response = newService.updateNews(id, request);
            return new ResponseAPI<>("Update news successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> deleteNews(@PathVariable String id) {
        try {
            boolean deleted = newService.deleteNews(id);
            if (deleted) {
                return new ResponseAPI<>("Delete news successfully", HttpStatus.OK, true);
            } else {
                return new ResponseAPI<>("Delete news failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
