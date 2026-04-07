package com.phuoctan.service;

import com.phuoctan.entity.Customer;
import com.phuoctan.entity.customerRequest;
import com.phuoctan.repository.customerRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class customerRequestService {
    private final customerRequestRepository customerRequestRepository;

    public customerRequestService(customerRequestRepository customerRequestRepository) {
        this.customerRequestRepository = customerRequestRepository;
    }

    public void saveRequest(customerRequest customerRequest){
        customerRequestRepository.save(customerRequest);
    }

}
