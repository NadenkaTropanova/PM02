package com.salesregister.repository;

import com.salesregister.domain.Operation;
import com.salesregister.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByUser(User user);
}
