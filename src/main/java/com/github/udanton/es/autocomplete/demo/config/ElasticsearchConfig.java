package com.github.udanton.es.autocomplete.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestClient esRestClient(@Value("${elasticsearch.host}") String host,
                                   @Value("${elasticsearch.port}") int port) {
        Header acceptHeader = new BasicHeader("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        Header contentType = new BasicHeader("Content-Type", "application/vnd.elasticsearch+json;compatible-with=7");
        return RestClient
            .builder(new HttpHost(host, port))
            .setDefaultHeaders(new Header[]{acceptHeader, contentType})
            .build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient esRestClient) {
        return new RestClientTransport(esRestClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
        return new ElasticsearchClient(elasticsearchTransport);
    }

}
