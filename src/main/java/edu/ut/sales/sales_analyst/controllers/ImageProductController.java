package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ImageNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ImageProductCreationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageNewResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageProductResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.ImageProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/image-product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ImageProductController {
    ImageProductService imageProductService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<ImageProductResponse> createImage(
            @RequestParam String productId,
            @RequestPart MultipartFile file
    ) {
        ImageProductCreationRequest request = ImageProductCreationRequest.builder()
                .productId(productId)
                .image(file)
                .build();

        ImageProductResponse response = imageProductService.create(request);
        return new ResponseAPI<>("Create image for product successfully", HttpStatus.CREATED, response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<ImageProductResponse> updateImage(
            @PathVariable String id,
            @RequestParam(required = false) String productId,
            @RequestPart(required = false) MultipartFile file
    ) {
        ImageProductCreationRequest request = ImageProductCreationRequest.builder()
                .productId(productId)
                .image(file)
                .build();

        ImageProductResponse response = imageProductService.update(id, request);
        return new ResponseAPI<>("Update image for product successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseAPI<Boolean> deleteImage(@PathVariable String id) {
        imageProductService.delete(id);
        return new ResponseAPI<>("Delete image successfully", HttpStatus.OK, true);
    }

    @GetMapping("/{id}")
    public ResponseAPI<ImageProductResponse> getImageNewById(@PathVariable String id) {
        ImageProductResponse response = imageProductService.getById(id);
        return new ResponseAPI<>("Get image successfully", HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseAPI<List<ImageProductResponse>> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImageProductResponse> imagePage = imageProductService.getAll(pageable);

        PageMeta meta = PageMeta.builder()
                .page(imagePage.getNumber())
                .size(imagePage.getSize())
                .totalElements(imagePage.getTotalElements())
                .totalPages(imagePage.getTotalPages())
                .last(imagePage.isLast())
                .build();

        return new ResponseAPI<>("Get all images successfully", HttpStatus.OK, imagePage.getContent(), meta);
    }

    @GetMapping("/by-product")
    public ResponseAPI<List<ImageProductResponse>> getImagesByNewsId(
            @RequestParam String productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImageProductResponse> imagePage = imageProductService.getByProductId(productId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(imagePage.getNumber())
                .size(imagePage.getSize())
                .totalElements(imagePage.getTotalElements())
                .totalPages(imagePage.getTotalPages())
                .last(imagePage.isLast())
                .build();

        return new ResponseAPI<>("Get images by product ID successfully", HttpStatus.OK, imagePage.getContent(), meta);
    }
}
