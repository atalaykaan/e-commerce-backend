package com.atalaykaan.e_commerce_backend.domain.product.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class ESUtil {

    public static Query createMatchQuery() {

        return Query.of(q -> q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForProductName(String productName) {

        return () -> Query.of(q -> q.match(buildMatchQueryForProductName(productName)));
    }

    private static MatchQuery buildMatchQueryForProductName(String productName) {

        return new MatchQuery.Builder()
                .field("name")
                .query(productName)
                .build();
    }
}
