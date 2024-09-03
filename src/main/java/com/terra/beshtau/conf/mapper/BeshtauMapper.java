package com.terra.beshtau.conf.mapper;

import com.terra.beshtau.conf.data.BeshtauEntity;
import com.terra.beshtau.conf.dto.ForeignData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;


@Component
public class BeshtauMapper {

    public BeshtauEntity mapToEntity(ForeignData foreignData) {
        BeshtauEntity entity = new BeshtauEntity();
        Optional.ofNullable(foreignData.getName()).ifPresent(entity::setName);
        Optional.ofNullable(foreignData.getFullName()).ifPresent(entity::setFullName);
        Optional.ofNullable(foreignData.getOs()).ifPresent(entity::setOs);
        Optional.ofNullable(foreignData.getCategory()).ifPresent(entity::setCategory);
        Optional.ofNullable(foreignData.getAmount()).map(this::mapAmount).ifPresent(entity::setAmount);
        return entity;
    }


    private BigDecimal mapAmount(String amount) {
        try {
            return new BigDecimal(amount).setScale(2);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
