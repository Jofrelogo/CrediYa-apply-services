package com.crediya.apply.usecase.apply;

import com.crediya.apply.model.apply.Apply;
import com.crediya.apply.model.apply.gateways.ApplyRepository;
import com.crediya.apply.model.common.PageQuery;
import com.crediya.apply.model.common.PageResponse;
import reactor.core.publisher.Mono;
import java.util.List;

public class ListApplyUseCase {

    private final ApplyRepository applyRepository;


    public ListApplyUseCase(ApplyRepository applyRepository) {
        this.applyRepository = applyRepository;
    }

    public Mono<PageResponse<Apply>> listForReviewPaged(List<String> statuses, PageQuery pageQuery) {
        return applyRepository.count(statuses)
                .flatMap(total ->
                        applyRepository.findByStateIn(statuses, pageQuery)
                                .collectList()
                                .map(list -> new PageResponse<>(list, pageQuery.getPage(), pageQuery.getSize(), total))
                );
    }

}
