package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.ImageNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageNewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.ImageNewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/image-news")
@RequiredArgsConstructor
public class ImageNewController {

    private final ImageNewService imageNewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseAPI<ImageNewResponse> createImageNew(
            @RequestParam String typeContent,
            @RequestParam String newId,
            @RequestPart MultipartFile file
    ) {
        try {
            ImageNewCreateRequest request = new ImageNewCreateRequest();
            request.setTypeContent(typeContent);
            request.setNewId(newId);
            request.setFile(file);

            ImageNewResponse response = imageNewService.create(request);
            return new ResponseAPI<>("Create image for news successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseAPI<ImageNewResponse> updateImageNew(
            @PathVariable String id,
            @RequestParam(required = false) String typeContent,
            @RequestParam(required = false) String newId,
            @RequestPart(required = false) MultipartFile file
    ) {
        try {
            ImageNewCreateRequest request = new ImageNewCreateRequest();
            request.setTypeContent(typeContent);
            request.setNewId(newId);
            request.setFile(file);

            ImageNewResponse response = imageNewService.update(id, request);
            return new ResponseAPI<>("Update image for news successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> deleteImageNew(@PathVariable String id) {
        try {
            imageNewService.delete(id);
            return new ResponseAPI<>("Delete image successfully", HttpStatus.OK, true);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<ImageNewResponse> getImageNewById(@PathVariable String id) {
        try {
            ImageNewResponse response = imageNewService.getById(id);
            return new ResponseAPI<>("Get image successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping
    public ResponseAPI<List<ImageNewResponse>> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ImageNewResponse> imagePage = imageNewService.getAll(pageable);

            PageMeta meta = PageMeta.builder()
                    .page(imagePage.getNumber())
                    .size(imagePage.getSize())
                    .totalElements(imagePage.getTotalElements())
                    .totalPages(imagePage.getTotalPages())
                    .last(imagePage.isLast())
                    .build();

            return new ResponseAPI<>("Get all images successfully", HttpStatus.OK, imagePage.getContent(), meta);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    @GetMapping("/by-news")
    public ResponseAPI<Page<ImageNewResponse>> getImagesByNewsId(
            @RequestParam String newsId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ImageNewResponse> response = imageNewService.getByNewsId(newsId, pageable);
            return new ResponseAPI<>("Get images by news ID successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
