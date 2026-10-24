package com.pulse.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pulse.asset.entity.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset,Long>, JpaSpecificationExecutor<Asset>{

	
}
