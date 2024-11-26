package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findAllByManagerIsNull();
}
