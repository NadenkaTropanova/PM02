package com.salesregister.service;

import com.salesregister.domain.Operation;
import com.salesregister.repository.OperationRepository;
import com.salesregister.request.OperationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final UserService userService;

    @Autowired
    public OperationService(OperationRepository operationRepository, UserService userService) {
        this.operationRepository = operationRepository;
        this.userService = userService;
    }

    public List<Operation> getExcursionsForCurrentUser() {
        return operationRepository.findByUser(userService.getCurrentUser());
    }

    public void addOperation(OperationRequest request) {
        Operation operation = new Operation();
        operation.setId(null);
        operation.setDate(new Date());
        operation.setName(request.getName());
        operation.setAmount(request.getAmount());
        operation.setDescription(request.getDescription());
        operation.setOperation(request.getOperation());
        operation.setPrice(request.getPrice());
        operation.setUser(userService.getCurrentUser());

        operationRepository.save(operation);
    }

    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }
}
