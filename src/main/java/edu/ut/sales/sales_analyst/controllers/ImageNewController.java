package edu.ut.sales.sales_analyst.controllers;

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
        ImageNewCreateRequest request = new ImageNewCreateRequest();
        request.setTypeContent(typeContent);
        request.setNewId(newId);
        request.setFile(file);

        ImageNewResponse response = imageNewService.create(request);
        return new ResponseAPI<>("Create image for news successfully", HttpStatus.CREATED, response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseAPI<ImageNewResponse> updateImageNew(
            @PathVariable String id,
            @RequestParam(required = false) String typeContent,
            @RequestParam(required = false) String newId,
            @RequestPart(required = false) MultipartFile file
    ) {
        ImageNewCreateRequest request = new ImageNewCreateRequest();
        request.setTypeContent(typeContent);
        request.setNewId(newId);
        request.setFile(file);

        ImageNewResponse response = imageNewService.update(id, request);
        return new ResponseAPI<>("Update image for news successfully", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> deleteImageNew(@PathVariable String id) {
        imageNewService.delete(id);
        return new ResponseAPI<>("Delete image successfully", HttpStatus.OK, true);
    }

    @GetMapping("/{id}")
    public ResponseAPI<ImageNewResponse> getImageNewById(@PathVariable String id) {
        ImageNewResponse response = imageNewService.getById(id);
        return new ResponseAPI<>("Get image successfully", HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseAPI<List<ImageNewResponse>> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
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
    }

    @GetMapping("/by-news")
    public ResponseAPI<List<ImageNewResponse>> getImagesByNewsId(
            @RequestParam String newsId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImageNewResponse> imagePage = imageNewService.getByNewsId(newsId, pageable);

        PageMeta meta = PageMeta.builder()
                .page(imagePage.getNumber())
                .size(imagePage.getSize())
                .totalElements(imagePage.getTotalElements())
                .totalPages(imagePage.getTotalPages())
                .last(imagePage.isLast())
                .build();

        return new ResponseAPI<>("Get images by news ID successfully", HttpStatus.OK, imagePage.getContent(), meta);
    }

}
