package com.scaffold.template.services;

import com.scaffold.template.dtos.CashRegisterClosureResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface CashRegisterClosureService {

    CashRegisterClosureResponseDto closeCashRegister(LocalDate date, Long userId);

    Page<CashRegisterClosureResponseDto> getCashRegisterClosures(LocalDate fromDate, LocalDate toDate, Pageable pageable);

}
