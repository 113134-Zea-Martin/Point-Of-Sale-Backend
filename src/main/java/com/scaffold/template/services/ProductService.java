package com.scaffold.template.services;

import com.scaffold.template.dtos.ProductCreateDTO;
import com.scaffold.template.dtos.ProductResponseDTO;
import com.scaffold.template.dtos.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<ProductResponseDTO> findAllProducts();

    ProductResponseDTO findProductByBarcode(String barcode);

    ProductResponseDTO createProduct(ProductCreateDTO productCreateRequestDTO);

    ProductResponseDTO updateProduct(Long id, ProductUpdateDTO productUpdateRequestDTO);

    Page<ProductResponseDTO> findProducts(String barcode, String name, String brand, String category, Boolean active, Pageable pageable);
}
