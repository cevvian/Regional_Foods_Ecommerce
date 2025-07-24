package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.ImageNewMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.ImageNewCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ImageNewResponse;
import edu.ut.sales.sales_analyst.model.entities.ImageNew;
import edu.ut.sales.sales_analyst.model.entities.New;
import edu.ut.sales.sales_analyst.repositories.ImageNewRepo;
import edu.ut.sales.sales_analyst.repositories.NewRepo;
import edu.ut.sales.sales_analyst.services.cloundinary.ImageUploadService;
import edu.ut.sales.sales_analyst.services.impl.IImageNewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageNewService implements IImageNewService {

    private final ImageNewRepo imageNewRepo;
    private final NewRepo newRepo;
    private final ImageNewMapper imageNewMapper;
    private final ImageUploadService imageUploadService;

    public ImageNewService(ImageNewRepo imageNewRepository, NewRepo newRepository,
                           ImageNewMapper mapper, ImageUploadService imageUploadService) {
        this.imageNewRepo = imageNewRepository;
        this.newRepo = newRepository;
        this.imageNewMapper = mapper;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public ImageNewResponse create(ImageNewCreateRequest request) {
        New news = newRepo.findByNewId(request.getNewId());
        if (news == null) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }

        ImageNew imageNew = new ImageNew();
        imageNew.setNews(news);
        imageNew.setTypeContent(request.getTypeContent());

        String imageUrl;
        try {
            imageUrl = imageUploadService.uploadSingleNewsImage(request.getFile(), news.getNewId());
        }
        catch (IOException ex) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        imageNew.setImageUrl(imageUrl);
        imageNewRepo.save(imageNew);

        return imageNewMapper.toImageNewResponse(imageNew);
    }

    @Override
    public ImageNewResponse update(String id, ImageNewCreateRequest request) {
        ImageNew imageNew = imageNewRepo.findByImageId(id);

        if (imageNew == null) {
            throw new AppException(ErrorCode.IMAGENEW_NOT_FOUND);
        }

        if (request.getTypeContent() != null) imageNew.setTypeContent(request.getTypeContent());
        if (request.getNewId() != null) {
            New news = newRepo.findByNewId(request.getNewId());
            if (news == null) {
                throw new AppException(ErrorCode.NEWS_NOT_FOUND);
            }
            imageNew.setNews(news);
        }

        if (request.getFile() != null && !request.getFile().isEmpty()){
            String imageUrl;
            try {
                imageUrl = imageUploadService.uploadSingleNewsImage(request.getFile(), request.getNewId());
            }
            catch (IOException ex) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
            imageNew.setImageUrl(imageUrl);
        }

        imageNewRepo.save(imageNew);

        return imageNewMapper.toImageNewResponse(imageNew);
    }

    @Override
    public void delete(String id) {
        ImageNew imageNew = imageNewRepo.findByImageId(id);

        if (imageNew == null) {
            throw new AppException(ErrorCode.IMAGENEW_NOT_FOUND);
        }

        imageNewRepo.delete(imageNew);
    }

    @Override
    public ImageNewResponse getById(String id) {
        ImageNew imageNew = imageNewRepo.findByImageId(id);

        if (imageNew == null) {
            throw new AppException(ErrorCode.IMAGENEW_NOT_FOUND);
        }

        return imageNewMapper.toImageNewResponse(imageNew);
    }

    @Override
    public Page<ImageNewResponse> getAll(Pageable pageable) {
        Page<ImageNew> imageNewPage = imageNewRepo.findAll(pageable);
        if (imageNewPage.isEmpty()) {
            throw new AppException(ErrorCode.NEWS_LIST_EMPTY);
        }

        return imageNewPage.map(imageNewMapper::toImageNewResponse);
    }

    @Override
    public Page<ImageNewResponse> getByNewsId(String newsId, Pageable pageable) {

        New news = newRepo.findByNewId(newsId);
        if (news == null) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }

        Page<ImageNew> imageNewPage = imageNewRepo.findByNews(news, pageable);
        if (imageNewPage.isEmpty()) {
            throw new AppException(ErrorCode.NEWS_LIST_EMPTY);
        }

        return imageNewPage.map(imageNewMapper::toImageNewResponse);
    }
}
