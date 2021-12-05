package com.taskreminder.es;

import com.google.common.base.CaseFormat;
import com.taskreminder.constants.Constants;
import com.taskreminder.dto.SearchCriteriaDTO;
import org.elasticsearch.index.query.*;

import java.util.List;

public class SearchQueryConverter {

    static String getDocumentFieldName(String field){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
    }

    public static BoolQueryBuilder getQuery(List<SearchCriteriaDTO> searchCriteria) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        for (SearchCriteriaDTO criteria : searchCriteria) {
            getQueryBasedOnCriteria(query, getDocumentFieldName(criteria.getField()), criteria);
        }
        //logger.info("Running ES search query: {}", query.toString().replaceAll("\\s+", " "));
        return query;
    }

    private static BoolQueryBuilder getQueryBasedOnCriteria(BoolQueryBuilder query, String field, SearchCriteriaDTO criteria) {
        if (Constants.EQUAL_AND_NEGATION_OPERATORS.contains(criteria.getOperator()) || Constants.AFFIX_OPERATORS.contains(criteria.getOperator())) {
            query = getFieldsQuery(query, field, criteria, criteria.getOperator());
        } else if(Constants.EXISTS_OPERATORS.contains(criteria.getOperator())){
            query.must(new ExistsQueryBuilder(field));
        } else if(Constants.NOT_EXISTS_OPERATORS.contains(criteria.getOperator())){
            query.mustNot(new ExistsQueryBuilder(field));
        }
        else {
            query = getRangeQuery(query, criteria, field);
        }
        return query;
    }

    private static BoolQueryBuilder getFieldsQuery(BoolQueryBuilder query, String field, SearchCriteriaDTO criteria, String operator)  {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (Constants.LIST_QUERIES.contains(field)) {
            boolQuery = getListQuery(boolQuery, field, criteria, operator);
        }  else {
            boolQuery = getItemQuery(boolQuery, field, criteria, operator);
        }
        return query.must(boolQuery);
    }

    private static BoolQueryBuilder getListQuery(BoolQueryBuilder boolQuery, String field, SearchCriteriaDTO criteria, String operator) {
        boolQuery = getBoolQuery(boolQuery, QueryBuilders.termsQuery(getFieldName(field), criteria.getValues()), operator);
        return boolQuery;
    }

    private static BoolQueryBuilder getItemQuery(BoolQueryBuilder boolQuery, String field, SearchCriteriaDTO criteria, String operator)  {
        for (String value : criteria.getValues()) {
            boolQuery = getBoolQueryForValue(boolQuery, field, value, operator);
        }
        return boolQuery;
    }

    private static  BoolQueryBuilder getBoolQueryForValue(BoolQueryBuilder boolQuery, String field, String value, String operator)  {
        if (operator.equals("prefix")){
            boolQuery = getBoolQuery(boolQuery, QueryBuilders.matchPhrasePrefixQuery(field, value), operator);
        } else {
            boolQuery = getBoolQuery(boolQuery, QueryBuilders.termsQuery(getFieldName(field), value), operator);
        }
        return  boolQuery;
    }

    public static BoolQueryBuilder getBoolQuery(BoolQueryBuilder boolQuery, QueryBuilder termQuery, String operator) {
        if (operator.equals("=") || operator.equals("prefix")) {
            return boolQuery.should(termQuery);
        } else {
            return boolQuery.mustNot(termQuery);
        }
    }

    public static String getFieldName(String field) {
        boolean isNotKeywordField = Constants.ONLY_STRING_FIELDS.contains(field) || Constants.DATE_FIELDS.contains(field);
        return isNotKeywordField ? field : field + ".keyword";
    }

    private static BoolQueryBuilder getRangeQuery(BoolQueryBuilder boolQuery, SearchCriteriaDTO criteria, String field) {
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(field);
        if (criteria.getOperator().equals(">")) {
            rangeQuery = rangeQuery.from(criteria.getValues().get(0));
        } else {
            rangeQuery = rangeQuery.to(criteria.getValues().get(0));
        }
        return boolQuery.filter(rangeQuery);
    }

}
