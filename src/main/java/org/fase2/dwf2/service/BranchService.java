package org.fase2.dwf2.service;

import org.fase2.dwf2.entities.Branch;
import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.enums.Role;
import org.fase2.dwf2.repository.IBranchRepository;
import org.fase2.dwf2.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BranchService {

    private final IBranchRepository branchRepository;
    private final IUserRepository userRepository;

    public BranchService(IBranchRepository branchRepository, IUserRepository userRepository) {
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Branch assignManagerToBranch(Long branchId, Long managerId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        if (!manager.getRole().equals(Role.GERENTE_SUCURSAL)) {
            throw new IllegalArgumentException("User is not a GERENTE_SUCURSAL");
        }

        branch.setManager(manager);
        return branchRepository.save(branch); // Save the branch with the new manager
    }

    @Transactional(readOnly = true)
    public List<Branch> getUnassignedBranches() {
        return branchRepository.findAllByManagerIsNull();
    }
}
