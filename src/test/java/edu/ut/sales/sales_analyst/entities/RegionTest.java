package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Product;
import edu.ut.sales.sales_analyst.model.entities.Region;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegionTest {

    @Test
    void testNoArgsConstructor() {
        Region region = new Region();
        assertNotNull(region);
    }

    @Test
    void testAllArgsConstructor() {
        String regionId = "R001";
        String regionName = "Hà Nội";
        List<Product> products = List.of(new Product());

        Region region = new Region(regionId, regionName, products);

        assertEquals(regionId, region.getRegionId());
        assertEquals(regionName, region.getRegionName());
        assertEquals(products, region.getProducts());
    }

    @Test
    void testBuilder() {
        String regionId = "R002";
        String regionName = "TP.HCM";

        Region region = Region.builder()
                .regionId(regionId)
                .regionName(regionName)
                .products(List.of())
                .build();

        assertEquals(regionId, region.getRegionId());
        assertEquals(regionName, region.getRegionName());
        assertNotNull(region.getProducts());
    }

    @Test
    void testGetterSetter() {
        Region region = new Region();

        region.setRegionId("R003");
        region.setRegionName("Đà Nẵng");
        List<Product> productList = List.of(new Product());
        region.setProducts(productList);

        assertEquals("R003", region.getRegionId());
        assertEquals("Đà Nẵng", region.getRegionName());
        assertEquals(productList, region.getProducts());
    }
}