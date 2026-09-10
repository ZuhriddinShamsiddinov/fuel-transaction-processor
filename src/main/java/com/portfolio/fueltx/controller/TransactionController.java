package com.portfolio.fueltx.controller;

import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import com.portfolio.fueltx.model.dto.PageResponse;
import com.portfolio.fueltx.model.dto.TransactionReviewRequest;
import com.portfolio.fueltx.service.TransactionProcessorService;
import com.portfolio.fueltx.service.TransactionQueryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.controller
 * @since 2026-09-10T12:20:00
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionQueryService queryService;
    private final TransactionProcessorService processorService;

    public TransactionController(
            TransactionQueryService queryService,
            TransactionProcessorService processorService) {
        this.queryService = queryService;
        this.processorService = processorService;
    }

    @GetMapping
    public Mono<PageResponse<FuelTransactionResponse>> list(
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        log.info("transactions.list driverId={} from={} to={} page={} size={}",
                driverId, from, to, page, size);
        return queryService.findPage(driverId, from, to, page, size);
    }

    @GetMapping("/{id:[0-9a-fA-F\\-]{36}}")
    public Mono<FuelTransactionResponse> getById(@PathVariable UUID id) {
        log.info("transactions.get id={}", id);
        return queryService.findById(id);
    }

    @PutMapping("/{id:[0-9a-fA-F\\-]{36}}/review")
    public Mono<FuelTransactionResponse> review(
            @PathVariable UUID id,
            @Valid @RequestBody TransactionReviewRequest request) {
        log.info("transactions.review id={} decision={}", id, request.getDecision());
        return processorService.review(id, request);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<Flux<String>> export(
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        log.info("transactions.export driverId={} from={} to={}", driverId, from, to);
        Flux<String> csv = queryService.exportCsv(driverId, from, to)
                .map(line -> line + "\n");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header("Content-Disposition", "attachment; filename=fuel-transactions.csv")
                .body(csv);
    }
}
