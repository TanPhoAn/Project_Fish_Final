package com.phuoctan.repository;

import com.phuoctan.entity.customerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface customerRequestRepository extends JpaRepository<customerRequest, Integer> {

}
