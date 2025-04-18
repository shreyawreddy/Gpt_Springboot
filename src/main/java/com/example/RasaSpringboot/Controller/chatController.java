package com.example.RasaSpringboot.Controller;

import com.example.RasaSpringboot.DTO.ChatRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")

// we use it to avoid cors error
@CrossOrigin(origins = "*")

public class chatController {

    // it is the Rasa url that is used to train the chat bot
    private final String RASA_URL = "http://localhost:5005/webhooks/rest/webhook";

    @PostMapping("/chat")

    //ChatRequest is the data transformer object  that we use to get the request it has message
    // check sample json to understand
    public ResponseEntity<String> chatWithBot(@RequestBody ChatRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // it has 2 parameters sender and message
        Map<String, Object> rasaPayload = new HashMap<>();
        rasaPayload.put("sender", "user");  // it can be any unique identifier
        rasaPayload.put("message", request.getMessage());

       // Wraps the payload in an HttpEntity so it can be sent as a proper HTTP request body.
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(rasaPayload);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(RASA_URL, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
