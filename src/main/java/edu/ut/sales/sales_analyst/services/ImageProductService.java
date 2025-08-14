package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ImageProductMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ImageProductCreationRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageProductResponse;
import edu.ut.sales.sales_analyst.model.entities.ImageProduct;
import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.repositories.ImageProductRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.services.cloundinary.ImageUploadService;
import edu.ut.sales.sales_analyst.services.impl.IImageProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageProductService implements IImageProductService {
    ImageProductRepo imageProductRepo;
    ProductRepo productRepo;
    ImageProductMapper imageProductMapper;
    ImageUploadService imageUploadService;

    @Override
    public ImageProductResponse create(ImageProductCreationRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        String imageUrl;
        try {
            imageUrl = imageUploadService.uploadSingleNewsImage(request.getImage(), product.getProductId());
        }
        catch (IOException ex) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        ImageProduct imageProduct = ImageProduct.builder()
                .product(product)
                .imageUrl(imageUrl)
                .build();
        return imageProductMapper.toImageProductResponse(imageProductRepo.save(imageProduct));
    }

    @Override
    public void delete(String id) {
        ImageProduct imageProduct = imageProductRepo.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.IMAGE_PRODUCT_NOT_FOUND));
        imageProductRepo.delete(imageProduct);
    }

    @Override
    public ImageProductResponse getById(String id) {
        ImageProduct imageProduct = imageProductRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_PRODUCT_NOT_FOUND));
        return imageProductMapper.toImageProductResponse(imageProduct);
    }

    @Override
    public Page<ImageProductResponse> getAll(Pageable pageable) {
        Page<ImageProduct> imageNewPage = imageProductRepo.findAll(pageable);
        if (imageNewPage.isEmpty()) {
            throw new AppException(ErrorCode.IMAGE_PRODUCT_LIST_EMPTY);
        }
        return imageNewPage.map(imageProductMapper::toImageProductResponse);
    }

    @Override
    public ImageProductResponse update(String id, ImageProductCreationRequest request) {
        ImageProduct imageProduct = imageProductRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_PRODUCT_NOT_FOUND));

        if (request.getProductId() != null) {
            Product product = productRepo.findById(request.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            imageProduct.setProduct(product);
        }

        if (request.getImage() != null && !request.getImage().isEmpty()){
            String imageUrl;
            try {
                imageUrl = imageUploadService.uploadSingleNewsImage(request.getImage(), request.getProductId());
            }
            catch (IOException ex) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
            imageProduct.setImageUrl(imageUrl);
        }

        return imageProductMapper.toImageProductResponse(imageProductRepo.save(imageProduct));
    }

    @Override
    public Page<ImageProductResponse> getByProductId(String productId, Pageable pageable) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Page<ImageProduct> imageProducts = imageProductRepo.findByProduct(product, pageable);
        if (imageProducts.isEmpty()) {
            throw new AppException(ErrorCode.IMAGE_PRODUCT_LIST_EMPTY);
        }

        return imageProducts.map(imageProductMapper::toImageProductResponse);
    }
}
