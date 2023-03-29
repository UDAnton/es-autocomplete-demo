package com.github.udanton.es.autocomplete.demo.api;

import java.io.StringReader;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ElasticsearchClient elasticsearchClient;
    private final ObjectMapper objectMapper;

    @GetMapping("/indices/{indexName}")
    public String getIndex(@PathVariable String indexName) {
        try {
            var indexResponse = elasticsearchClient.indices().get(GetIndexRequest.of(builder -> builder.index(indexName)));
            IndexState indexState = indexResponse.result().get(indexName);
            return JsonpUtils.toString(indexState, new JacksonJsonpMapper(), new StringBuilder()).toString();
        } catch (Exception e) {
            log.error("Cannot get index [index name: {} error: {}]", indexName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/indices/{indexName}")
    public void createIndex(@PathVariable String indexName, @RequestBody String indexConfiguration) {
        try {
            var indexConfigurationReader = new StringReader(indexConfiguration);
            var createIndexRequest = CreateIndexRequest.of(builder -> builder.index(indexName)
                .withJson(indexConfigurationReader));
            elasticsearchClient.indices().create(createIndexRequest);
        } catch (Exception e) {
            log.error("Cannot create index [index name: {}, config: {}, error: {}]", indexName, indexConfiguration, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/indices/{indexName}")
    public void deleteIndex(@PathVariable String indexName) {
        try {
            var deleteIndexRequest = DeleteIndexRequest.of(builder -> builder.index(indexName));
            elasticsearchClient.indices().delete(deleteIndexRequest);
        } catch (Exception e) {
            log.error("Cannot delete index [index name: {} error: {}]", indexName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
