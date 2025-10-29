package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("""
        SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(s.id, s.date, s.amount, s.seller.name)
        FROM Sale s
        WHERE s.date BETWEEN :minDate AND :maxDate
          AND UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
        """)
    Page<SaleReportDTO> searchReport(
    	@Param("minDate") LocalDate minDate,
		@Param("maxDate") LocalDate maxDate,
		@Param("name") String name,
		Pageable pageable
    );

    @Query("""
        SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(s.seller.name, SUM(s.amount))
        FROM Sale s
        WHERE s.date BETWEEN :minDate AND :maxDate
        GROUP BY s.seller.name
        """)
    List<SaleSummaryDTO> searchSummary(
    	@Param("minDate") LocalDate minDate,
    	@Param("maxDate") LocalDate maxDate
    );
}
