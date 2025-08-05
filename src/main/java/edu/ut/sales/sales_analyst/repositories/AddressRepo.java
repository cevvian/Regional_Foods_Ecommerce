package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Address;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer> {
    Address findByAddressId(String addressId);
    Page<Address> findAllByUser_UserId(Pageable pageable, String userId);
    Boolean existsByUser_UserIdAndAddressLineAndProvinceAndPhone(
            String userId,
            String addressLine,
            String province,
            String phone
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.userId = :userId")
    void updateDefaultAddressToFalse(@Param("userId") String userId);
}
