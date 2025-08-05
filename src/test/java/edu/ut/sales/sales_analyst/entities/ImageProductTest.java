package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.ImageProduct;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageProductTest {

    @Test
    void testNoArgsConstructor() {
        ImageProduct imageProduct = new ImageProduct();
        assertNotNull(imageProduct);
    }

    @Test
    void testAllArgsConstructor() {
        String imageId = "IMG001";
        String imageUrl = "https://example.com/image.jpg";
        Product product = new Product();

        ImageProduct imageProduct = new ImageProduct(imageId, imageUrl, product);

        assertEquals(imageId, imageProduct.getImageId());
        assertEquals(imageUrl, imageProduct.getImageUrl());
        assertEquals(product, imageProduct.getProduct());
    }

    @Test
    void testGetterSetter() {
        ImageProduct imageProduct = new ImageProduct();

        String imageId = "IMG002";
        String imageUrl = "https://example.com/banner.jpg";
        Product product = new Product();

        imageProduct.setImageId(imageId);
        imageProduct.setImageUrl(imageUrl);
        imageProduct.setProduct(product);

        assertEquals(imageId, imageProduct.getImageId());
        assertEquals(imageUrl, imageProduct.getImageUrl());
        assertEquals(product, imageProduct.getProduct());
    }
}