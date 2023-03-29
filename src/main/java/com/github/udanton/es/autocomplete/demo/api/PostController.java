package com.github.udanton.es.autocomplete.demo.api;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.util.ObjectBuilder;
import com.github.udanton.es.autocomplete.demo.config.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/client/posts")
@RequiredArgsConstructor
public class PostController {

    private static final String INDEX_NAME = "posts";
    private static final String FUZZINESS = "AUTO:3,7";
    private final ElasticsearchClient elasticsearchClient;

    @GetMapping("/search")
    public List<Post> getPosts(@RequestParam String query, @RequestParam(defaultValue = "name") String field) {
        try {
            if (!List.of("name", "description", "category").contains(field))
                throw new RuntimeException(String.format("Search field: %s not found", field));
            SearchResponse<Post> searchResponse = elasticsearchClient.search(SearchRequest.of(buildSearchRequest(field, query)), Post.class);
            return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Cannot search data [query: {}, fields: {}, error: {}]", query, field, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> buildSearchRequest(String fieldName, String searchText) {
        return builder -> builder.index(INDEX_NAME)
            .query(qBuilder ->
                qBuilder.match(mBuilder ->
                    mBuilder.field(fieldName).query(searchText).fuzziness(FUZZINESS)));
    }

}
