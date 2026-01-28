    package com.phuoctan;

    import org.mapstruct.Mapper;
    import org.mapstruct.Mappings;

    @Mapper
    public interface CustomerMapper {

        CustomerDTO customerTocustomerDTO(Customer customer);
        registerFormDTO customerToregisterFormDTO(Customer customer);

        Customer toEntity(registerFormDTO registerFormDTO);
    }
