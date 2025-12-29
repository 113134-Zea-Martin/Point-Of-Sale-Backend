package com.scaffold.template.services.impl;

import com.scaffold.template.config.BrandMapper;
import com.scaffold.template.dtos.BrandDto;
import com.scaffold.template.dtos.CreateBrandRequest;
import com.scaffold.template.entities.BrandEntity;
import com.scaffold.template.repositories.BrandRepository;
import com.scaffold.template.services.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository,
                            BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public BrandEntity getBrandById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El ID de la marca no puede ser nulo");
        }

        return brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada con ID: " + id));
    }

    @Override
    public List<BrandDto> getAllBrands() {
        List<BrandEntity> brandEntities = brandRepository.findAll();
        return brandEntities.stream()
                .map(brandMapper::toDto)
                .toList();
    }

    @Override
    public BrandDto createBrand(CreateBrandRequest createBrandRequest) {
        if (createBrandRequest == null || createBrandRequest.getName() == null || createBrandRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca no puede ser nulo o vacío");
        }

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName(createBrandRequest.getName());

        BrandEntity savedEntity = brandRepository.save(brandEntity);
        return brandMapper.toDto(savedEntity);
    }
}
