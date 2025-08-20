package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.NewMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ImageOfNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.NewUpdateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.NewResponse;
import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.ImageNew;
import edu.ut.sales.sales_analyst.model.entities.New;
import edu.ut.sales.sales_analyst.repositories.CategoryRepo;
import edu.ut.sales.sales_analyst.repositories.ImageNewRepo;
import edu.ut.sales.sales_analyst.repositories.NewRepo;
import edu.ut.sales.sales_analyst.services.cloundinary.ImageUploadService;
import edu.ut.sales.sales_analyst.services.impl.INewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewService implements INewService {

    private final NewRepo newRepo;
    private final ImageNewRepo imageNewRepo;
    private final NewMapper newMapper;
    private final ImageUploadService imageUploadService;
    private final CategoryRepo categoryRepo;

    public NewService(NewRepo newsRepository, NewMapper newMapper, ImageNewRepo imageRepo,
                      ImageUploadService imageUploadService, CategoryRepo categoryRepo) {
        this.newRepo = newsRepository;
        this.newMapper = newMapper;
        this.imageNewRepo = imageRepo;
        this.imageUploadService = imageUploadService;
        this.categoryRepo = categoryRepo;
    }


    //Upload ảnh, lưu vào DB, và gắn vào đối tượng News.
    private void handleImageUploadAndAttachToNews(New news, List<ImageOfNewCreateRequest> imageRequests) {
        if (imageRequests == null || imageRequests.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_NOT_FOUND);
        }

        List<MultipartFile> files = imageRequests.stream()
                .map(ImageOfNewCreateRequest::getFile)
                .collect(Collectors.toList());

        List<String> imageUrls;
        try {
            imageUrls = imageUploadService.uploadNewsImages(files, news.getNewId());
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        List<ImageNew> imageEntities = new ArrayList<>();
        for (int i = 0; i < imageRequests.size(); i++) {
            ImageOfNewCreateRequest imageRequest = imageRequests.get(i);
            String imageUrl = imageUrls.get(i);

            ImageNew imageEntity = new ImageNew();
            imageEntity.setTypeContent(imageRequest.getTypeContent());
            imageEntity.setImageUrl(imageUrl);
            imageEntity.setNews(news);

            imageEntities.add(imageEntity);
        }
        imageNewRepo.saveAll(imageEntities);
        news.setImages(imageEntities);
    }



    @Override
    public NewResponse createNews(NewCreateRequest request) {
        if (newRepo.existsByTitle(request.getTitle())) {
            throw new AppException(ErrorCode.NEWS_ALREADY_EXISTS);
        }

        if (request.getCategoryId() == null || request.getCategoryId().isBlank()) {
            throw new AppException(ErrorCode.CATEGORY_REQUIRED);
        }

        Category category = categoryRepo.findByCategoryId(request.getCategoryId());
        if (category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        New news = new New();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setCategory(category);
        news.setType(request.getType());
        newRepo.save(news);

        handleImageUploadAndAttachToNews(news, request.getImages());

        return newMapper.ToNewResponse(newRepo.save(news));
    }


    @Override
    @Transactional(readOnly = true)
    public NewResponse getNews(String newId) {
        New news = newRepo.findByNewId(newId);
        if (news == null) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }
        return newMapper.ToNewResponse(news);
    }

    @Override
    public Page<NewResponse> getAllNews(Pageable pageable) {
        Page<New> newsPage = newRepo.findAll(pageable);
        if (newsPage.isEmpty()) {
            throw new AppException(ErrorCode.NEWS_LIST_EMPTY);
        }
        return newsPage.map(newMapper::ToNewResponse);
    }

    @Override
    public NewResponse updateNews(String newId, NewUpdateRequest request) {
        New news = newRepo.findByNewId(newId);
        if (news == null) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            news.setTitle(request.getTitle());
        }

        if (request.getContent() != null && !request.getContent().isBlank()) {
            news.setContent(request.getContent());
        }

        if (request.getCategoryId() != null && !request.getCategoryId().isBlank()) {
            Category category = categoryRepo.findByCategoryId(request.getCategoryId());
            if (category == null) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }
            news.setCategory(category);
        }

        if (request.getType() != null) {
            news.setType(request.getType());
        }

        // Cập nhật ảnh nếu có gửi lên
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            imageNewRepo.deleteByNews(news);
            handleImageUploadAndAttachToNews(news, request.getImages());
        }

        news.setUpdateAt(LocalDateTime.now());

        return newMapper.ToNewResponse(newRepo.save(news));
    }


    @Override
    @Transactional
    public Boolean deleteNews(String newId) {
        New news = newRepo.findByNewId(newId);
        if (news == null) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }

        imageNewRepo.deleteByNews(news);
        newRepo.delete(news);
        return true;
    }

    @Override
    public Page<NewResponse> getNewsByCategory(String categoryId, Pageable pageable){
        Category category = categoryRepo.findByCategoryId(categoryId);
        if (category == null) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Page<New> news = newRepo.findByCategory(category, pageable);

        if (news.isEmpty()) {
            throw new AppException(ErrorCode.NEWS_LIST_EMPTY);
        }

        return news.map(newMapper::ToNewResponse);
    }
}
