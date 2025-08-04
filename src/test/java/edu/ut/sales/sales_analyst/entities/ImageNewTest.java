package edu.ut.sales.sales_analyst.entities;


import edu.ut.sales.sales_analyst.model.entities.ImageNew;
import edu.ut.sales.sales_analyst.model.entities.New;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageNewTest {

    @Test
    void testNoArgsConstructor() {
        ImageNew imageNew = new ImageNew();
        assertNotNull(imageNew);
    }

    @Test
    void testAllArgsConstructor() {
        String imageId = "IMG001";
        String typeContent = "thumbnail";
        String imageUrl = "https://example.com/image.jpg";
        New news = new New();

        ImageNew imageNew = new ImageNew(imageId, typeContent, imageUrl, news);

        assertEquals(imageId, imageNew.getImageId());
        assertEquals(typeContent, imageNew.getTypeContent());
        assertEquals(imageUrl, imageNew.getImageUrl());
        assertEquals(news, imageNew.getNews());
    }

    @Test
    void testGetterSetter() {
        ImageNew imageNew = new ImageNew();

        String imageId = "IMG002";
        String typeContent = "banner";
        String imageUrl = "https://example.com/banner.jpg";
        New news = new New();

        imageNew.setImageId(imageId);
        imageNew.setTypeContent(typeContent);
        imageNew.setImageUrl(imageUrl);
        imageNew.setNews(news);

        assertEquals(imageId, imageNew.getImageId());
        assertEquals(typeContent, imageNew.getTypeContent());
        assertEquals(imageUrl, imageNew.getImageUrl());
        assertEquals(news, imageNew.getNews());
    }
}