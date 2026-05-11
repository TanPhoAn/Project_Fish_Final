package com.phuoctan;

import com.phuoctan.entity.Cart_item;
import com.phuoctan.entity.Order_item;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @BeanMapping(ignoreUnmappedSourceProperties = "cart")
    @Mapping(target="id", ignore=true)
    Order_item cartItemToOrderItem(Cart_item cartItem);
}
