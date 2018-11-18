package com.mpoqq.istio.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HelloworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }

    @RestController
    public static class HelloworldController {
        private static final Logger log = LoggerFactory.getLogger(HelloworldController.class);

        @Value("${version:1.0}")
        public String version;

        private boolean failVar;

        @GetMapping("/hello/{name}")
        public ResponseEntity<Map<String, String>> hello(@Value("${greeting:Hello $name from $hostname with $version}") String greetingTemplate, @PathVariable String name) throws UnknownHostException {
            log.info("Recevied hello request for {}", name);
            if (failVar) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
            Map<String, String> response = new HashMap<>();

            String hostname = InetAddress.getLocalHost().getHostName();
            String greeting = greetingTemplate
                    .replaceAll("\\$name", name)
                    .replaceAll("\\$hostname", hostname)
                    .replaceAll("\\$version", version);

            response.put("greeting", greeting);
            response.put("version", version);
            response.put("hostname", hostname);

            return ResponseEntity.ok(response);
        }

        @PostMapping("fail/{fail}")
        public ResponseEntity fail(@PathVariable String fail) {
            log.info("Set failVar to {}", fail);
            this.failVar = fail.equals("true");
            return ResponseEntity.ok().build();
        }
    }
}
