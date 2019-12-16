package org.rpi;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.rpi.model.Lamp;
import org.rpi.model.LampState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Slf4j
@Getter
@Setter
@RestController
public class LampController {

    @Value("${userId}")
    private String user;

    @Value("${bridgeIp}")
    private String philipsHueBridgeIpAdress;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/lamps")
    public ResponseEntity<Map<String, Lamp>> getAllLamps() {
        final String lightUrl = "http://" + philipsHueBridgeIpAdress + "/api/" + user + "/lights";
        ParameterizedTypeReference<Map<String, Lamp>> responseType = new ParameterizedTypeReference<>() {};
        final ResponseEntity<Map<String, Lamp>> response = restTemplate.exchange(lightUrl, HttpMethod.GET, null, responseType);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/lamp/{id}")
    public ResponseEntity switchStateLamp(@PathVariable("id") String id, @RequestParam("state") boolean state) {
        final String lightUrl = "http://" + philipsHueBridgeIpAdress + "/api/" + user + "/lights/" + id + "/state";
        try {
            restTemplate.put(lightUrl, new LampState(state));
            return ResponseEntity.ok(id + " " + (state ? "light" : "not light"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
