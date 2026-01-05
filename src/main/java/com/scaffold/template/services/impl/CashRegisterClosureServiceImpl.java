package com.scaffold.template.services.impl;

import com.scaffold.template.config.CashRegisterClosureMapper;
import com.scaffold.template.dtos.CashRegisterClosureResponseDto;
import com.scaffold.template.entities.CashRegisterClosureEntity;
import com.scaffold.template.entities.SaleEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.CashRegisterClosureRepository;
import com.scaffold.template.repositories.SaleRepository;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.CashRegisterClosureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CashRegisterClosureServiceImpl implements CashRegisterClosureService {

    private final CashRegisterClosureRepository cashRegisterClosureRepository;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final CashRegisterClosureMapper cashRegisterClosureMapper;

    public CashRegisterClosureServiceImpl(CashRegisterClosureRepository cashRegisterClosureRepository,
                                          SaleRepository saleRepository,
                                          UserRepository userRepository,
                                          CashRegisterClosureMapper cashRegisterClosureMapper) {
        this.cashRegisterClosureRepository = cashRegisterClosureRepository;
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.cashRegisterClosureMapper = cashRegisterClosureMapper;
    }


    @Override
    public CashRegisterClosureResponseDto closeCashRegister(LocalDate date, Long userId) {
        if (date == null || userId == null) {
            throw new IllegalArgumentException("Date and User ID must not be null");
        }
        if (cashRegisterClosureRepository.existsByDate(date)) {
            throw new IllegalStateException("La caja ya ha sido cerrada para la fecha: " + date);
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        List<SaleEntity> sales = saleRepository.findByDateTimeBetween(startOfDay, endOfDay);

        // Aquí se realizarían los cálculos necesarios para el cierre de caja
        double cash = 0;
        double debit = 0;
        double credit = 0;
        double mp = 0;

        for (SaleEntity sale : sales) {
            switch (sale.getPaymentType()) {
                case CASH -> cash += sale.getTotal();
                case DEBIT_CARD -> debit += sale.getTotal();
                case CREDIT_CARD -> credit += sale.getTotal();
                case MERCADO_PAGO -> mp += sale.getTotal();
            }
        }

        CashRegisterClosureEntity closure = new CashRegisterClosureEntity();
        closure.setDate(date);
        closure.setCashTotal(cash);
        closure.setDebitTotal(debit);
        closure.setCreditTotal(credit);
        closure.setMercadoPagoTotal(mp);
        closure.setTotalSales(cash + debit + credit + mp);
        closure.setTotalOperations(sales.size());
        closure.setClosedAt(LocalDateTime.now());

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + userId));
        closure.setClosedBy(user);

        cashRegisterClosureRepository.save(closure);

        return cashRegisterClosureMapper.toDto(closure);

    }

    @Override
    public Page<CashRegisterClosureResponseDto> getCashRegisterClosures(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        log.info("Obteniendo cierres de caja desde {} hasta {}", fromDate, toDate);

        Page<CashRegisterClosureEntity> closuresPage;

        if (fromDate == null && toDate == null) {
            closuresPage = cashRegisterClosureRepository.findAll(pageable);
        } else if (fromDate == null) {
            // Sólo filtro por toDate
            closuresPage = cashRegisterClosureRepository.findAllByDateLessThanEqual(toDate, pageable);
        } else if (toDate == null) {
            // Sólo filtro por fromDate
            closuresPage = cashRegisterClosureRepository.findAllByDateGreaterThanEqual(fromDate, pageable);
        } else {
            // Ambos presentes
            closuresPage = cashRegisterClosureRepository.findAllByDateBetween(fromDate, toDate, pageable);
        }


        log.info("Paginación solicitada: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        log.info("Cierres de caja encontrados: {}", closuresPage.getTotalElements());
        return closuresPage.map(cashRegisterClosureMapper::toDto);
    }
}
