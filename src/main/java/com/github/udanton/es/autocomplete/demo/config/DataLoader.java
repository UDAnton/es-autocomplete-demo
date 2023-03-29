package com.github.udanton.es.autocomplete.demo.config;

import java.io.InputStream;
import java.util.UUID;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final ElasticsearchClient elasticsearchClient;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) {
        try {
            boolean isIndexExisted = elasticsearchClient.indices()
                .exists(ExistsRequest.of(builder -> builder.index("posts")))
                .value();
            if (!isIndexExisted) {
                createIndex();
                createData();
            } else {
                log.info("Products index was existed");
            }
        } catch (Exception e) {
            log.error("Cannot prepare data for test [error: {}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createIndex() {
        try {
            InputStream input = this.getClass().getResourceAsStream("/data/index.json");
            CreateIndexRequest createIndexRequest = CreateIndexRequest.of(builder -> builder.index("posts").withJson(input));
            elasticsearchClient.indices().create(createIndexRequest);
            log.info("Posts index was created");
        } catch (Exception e) {
            log.error("Cannot create index for test [error: {}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createData() {
        try {
            InputStream input = this.getClass().getResourceAsStream("/data/data.json");
            Blog data = objectMapper.readValue(input, Blog.class);
            for(Post post : data.getPosts()) {
                elasticsearchClient.index(i -> i.index("posts").id(UUID.randomUUID().toString()).document(post));
            }
        } catch (Exception e) {
            log.error("Cannot create data for test [error: {}]", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
