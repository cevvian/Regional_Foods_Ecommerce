//package edu.ut.sales.sales_analyst.entities;
//
//import edu.ut.sales.sales_analyst.model.entities.Category;
//import edu.ut.sales.sales_analyst.model.entities.ImageNew;
//import edu.ut.sales.sales_analyst.model.entities.New;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class NewTest {
//
//    @Test
//    void testNoArgsConstructor() {
//        New news = new New();
//        assertNotNull(news);
//    }
//
//    @Test
//    void testAllArgsConstructor() {
//        String newId = "N001";
//        String title = "Bản tin công nghệ";
//        String content = "Nội dung chi tiết về công nghệ mới.";
//        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 10, 0);
//        LocalDateTime updatedAt = LocalDateTime.of(2023, 1, 2, 12, 0);
//        Category category = new Category();
//        ImageNew image = new ImageNew();
//        New news = new New(newId, title, content, createdAt, updatedAt, category, Collections.singletonList(image));
//
//        assertEquals(newId, news.getNewId());
//        assertEquals(title, news.getTitle());
//        assertEquals(content, news.getContent());
//        assertEquals(createdAt, news.getCreateAt());
//        assertEquals(updatedAt, news.getUpdateAt());
//        assertEquals(category, news.getCategory());
//        assertEquals(1, news.getImages().size());
//    }
//
//    @Test
//    void testGetterSetter() {
//        New news = new New();
//        String newId = "N002";
//        String title = "Tin tức khuyến mãi";
//        String content = "Thông tin chi tiết về chương trình khuyến mãi.";
//        LocalDateTime createdAt = LocalDateTime.of(2023, 2, 1, 9, 30);
//        LocalDateTime updatedAt = LocalDateTime.of(2023, 2, 2, 14, 15);
//        Category category = new Category();
//        ImageNew image = new ImageNew();
//
//        news.setNewId(newId);
//        news.setTitle(title);
//        news.setContent(content);
//        news.setCreateAt(createdAt);
//        news.setUpdateAt(updatedAt);
//        news.setCategory(category);
//        news.setImages(Collections.singletonList(image));
//
//        assertEquals(newId, news.getNewId());
//        assertEquals(title, news.getTitle());
//        assertEquals(content, news.getContent());
//        assertEquals(createdAt, news.getCreateAt());
//        assertEquals(updatedAt, news.getUpdateAt());
//        assertEquals(category, news.getCategory());
//        assertEquals(1, news.getImages().size());
//    }
//
//    @Test
//    void testDefaultTimestamps() {
//        New news = new New();
//        assertNotNull(news.getCreateAt());
//        assertNotNull(news.getUpdateAt());
//        assertTrue(news.getCreateAt().isBefore(LocalDateTime.now().plusSeconds(1)));
//        assertTrue(news.getUpdateAt().isBefore(LocalDateTime.now().plusSeconds(1)));
//    }
//}