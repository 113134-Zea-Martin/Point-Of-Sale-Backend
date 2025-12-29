package com.scaffold.template.services;

import com.scaffold.template.dtos.BrandDto;
import com.scaffold.template.dtos.CreateBrandRequest;
import com.scaffold.template.entities.BrandEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface BrandService {

    BrandEntity getBrandById(Long id);

    List<BrandDto> getAllBrands();
    BrandDto createBrand(CreateBrandRequest createBrandRequest);


}
