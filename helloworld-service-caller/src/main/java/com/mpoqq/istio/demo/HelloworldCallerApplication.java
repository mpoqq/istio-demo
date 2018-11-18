package com.mpoqq.istio.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HelloworldCallerApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloworldCallerApplication.class, args);
    }

    @RestController
    public static class HelloworldCallerController {
        private static final Logger log = LoggerFactory.getLogger(HelloworldCallerController.class);

        @Value("${version:1.0}")
        public String version;

        @Value("${endpoint:http://helloworld-service:8080/hello/world}")
        private String endpoint;

        @Autowired
        private RestTemplate restTemplate;

        @GetMapping("/caller")
        public ResponseEntity<Map<String, Object>> hello() throws UnknownHostException {
            log.info("Received caller request");
            Map<String, Object> response = new HashMap<>();

            String hostname = InetAddress.getLocalHost().getHostName();

            ResponseEntity<Map> received = restTemplate
                    .getForEntity(URI.create(endpoint), Map.class);

            response.put("version", version);
            response.put("hostname", hostname);
            response.put("called", endpoint);
            response.put("received", received);
            return ResponseEntity.ok(response);
        }
    }
}
