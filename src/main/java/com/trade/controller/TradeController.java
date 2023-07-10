package com.trade.controller;

import com.trade.vo.TradeVo;
import com.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @GetMapping
    public Mono<List<TradeVo>> getAll() throws URISyntaxException {
        return tradeService.getTrades();
    }
}
