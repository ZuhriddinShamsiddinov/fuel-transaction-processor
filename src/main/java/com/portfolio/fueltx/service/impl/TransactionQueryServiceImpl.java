package com.portfolio.fueltx.service.impl;

import com.portfolio.fueltx.exception.ResourceNotFoundException;
import com.portfolio.fueltx.mapper.FuelTransactionMapper;
import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import com.portfolio.fueltx.model.dto.PageResponse;
import com.portfolio.fueltx.repository.FuelTransactionRepository;
import com.portfolio.fueltx.service.TransactionQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.impl
 * @since 2026-09-10T12:55:00
 */
@Service
public class TransactionQueryServiceImpl implements TransactionQueryService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private final FuelTransactionRepository repository;
    private final FuelTransactionMapper mapper;

    public TransactionQueryServiceImpl(FuelTransactionRepository repository, FuelTransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<FuelTransactionResponse> findById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Transaction not found: " + id)))
                .map(mapper::toResponse);
    }

    @Override
    public Mono<PageResponse<FuelTransactionResponse>> findPage(
            String driverId,
            Instant from,
            Instant to,
            Integer page,
            Integer size) {
        int pageNumber = page == null || page < 0 ? DEFAULT_PAGE : page;
        int pageSize = size == null || size < 1 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        long offset = (long) pageNumber * pageSize;

        Mono<Long> totalMono = repository.countFiltered(driverId, from, to);
        Mono<java.util.List<FuelTransactionResponse>> contentMono = repository
                .findFiltered(driverId, from, to, pageSize, offset)
                .map(mapper::toResponse)
                .collectList();

        return Mono.zip(contentMono, totalMono)
                .map(tuple -> {
                    long total = tuple.getT2();
                    int totalPages = pageSize == 0 ? 0 : (int) Math.ceil((double) total / pageSize);
                    return PageResponse.<FuelTransactionResponse>builder()
                            .content(tuple.getT1())
                            .page(pageNumber)
                            .size(pageSize)
                            .totalElements(total)
                            .totalPages(totalPages)
                            .build();
                });
    }

    @Override
    public Flux<String> exportCsv(String driverId, Instant from, Instant to) {
        Flux<String> header = Flux.just(
                "id,externalTransactionId,driverId,vehicleId,merchantName,merchantLocation,"
                        + "gallons,pricePerGallon,totalAmount,transactionTimestamp,status");

        Flux<String> rows = repository.findForExport(driverId, from, to)
                .map(tx -> String.join(",",
                        csv(tx.getId()),
                        csv(tx.getExternalTransactionId()),
                        csv(tx.getDriverId()),
                        csv(tx.getVehicleId()),
                        csv(tx.getMerchantName()),
                        csv(tx.getMerchantLocation()),
                        csv(tx.getGallons()),
                        csv(tx.getPricePerGallon()),
                        csv(tx.getTotalAmount()),
                        csv(tx.getTransactionTimestamp()),
                        csv(tx.getStatus())));

        return header.concatWith(rows);
    }

    private String csv(Object value) {
        if (value == null) {
            return "";
        }
        String raw = String.valueOf(value);
        if (raw.contains(",") || raw.contains("\"") || raw.contains("\n")) {
            return "\"" + raw.replace("\"", "\"\"") + "\"";
        }
        return raw;
    }
}
