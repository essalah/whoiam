package com.elhachmi.portfolio.resume.repository;

import com.elhachmi.portfolio.resume.entity.PlanEntitlement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanEntitlementRepository extends JpaRepository<PlanEntitlement, String> {
    Optional<PlanEntitlement> findByPlanKey(String planKey);

    /**
     * Serializes free-resume creation for Version 0. This deliberately locks the
     * global FREE plan row; move to an owner/account-scoped lock when creation
     * throughput makes global serialization a bottleneck.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entitlement from PlanEntitlement entitlement where entitlement.planKey = :planKey")
    Optional<PlanEntitlement> findByPlanKeyForUpdate(@Param("planKey") String planKey);
}
