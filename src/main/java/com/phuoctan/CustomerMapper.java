package com.phuoctan;

import com.phuoctan.dto.CustomerDTO;
import com.phuoctan.dto.registerFormDTO;
import com.phuoctan.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerDTO customerTocustomerDTO(Customer customer);
    registerFormDTO customerToregisterFormDTO(Customer customer);
    Customer toEntity(registerFormDTO registerFormDTO);
}
