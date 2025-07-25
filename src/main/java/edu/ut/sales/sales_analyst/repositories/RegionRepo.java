package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepo extends JpaRepository<Region, String> {
    Region findByRegionName(String regionName);
    Region findByRegionId(String regionId);
}
