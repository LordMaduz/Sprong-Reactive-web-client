package com.trade.service;

import com.trade.util.WebClientUtil;
import com.trade.vo.ResponseVo;
import com.trade.vo.TradeVo;
import com.trade.model.entity.Trade;
import com.trade.repo.TradeRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepo tradeRepo;
    private final WebClientUtil webClientUtil;

    public Mono<List<TradeVo>> getTrades() throws URISyntaxException {
        Mono<List<Trade>> tradeMono = tradeRepo.findAll().collectList();

        Mono<ResponseVo> authMono = webClientUtil.getResponse(
                "TMOP_SG_ADMIN", "ViewTrade", "TradeCollection",
                "trade-service").bodyToMono(ResponseVo.class);


        List<TradeVo> tradeVoList = new ArrayList<>();
        return tradeMono.flatMap(list -> {
            return authMono.flatMap(responseVo -> {
                list.forEach(element -> {
                    TradeVo v0 = new TradeVo();
                    v0.setTradeId(element.getTradeId());
                    if (responseVo.getResult().isAllow()) {
                        v0.setTradeInfo(element.getTradeInfo());
                    }
                    tradeVoList.add(v0);
                });
                return Mono.fromCallable(() ->
                        tradeVoList
                );
            });
        });
    }
}
