package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAll();
}

