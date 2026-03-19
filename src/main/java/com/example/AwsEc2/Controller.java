package com.example.AwsEc2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class Controller {

    @Value("${gemini.api.key}")
    private String apikey;
    private final RestTemplate restTemplate=new  RestTemplate();
    @Autowired
    private UserREpo userRepo;
    @GetMapping("/")
    public String geeting(){
        return "hello world";
    }
    @GetMapping("/gemini/{msg}")
    public String generated(@PathVariable String msg) {

         String GEMINI_URL =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key="+apikey;

        Map<String, Object> text = new HashMap<>();
        text.put("text", msg);

        Map<String, Object> parts = new HashMap<>();
        parts.put("parts", List.of(text));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(parts));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(GEMINI_URL, request, Map.class);

        List candidates = (List) response.getBody().get("candidates");
        Map candidate = (Map) candidates.get(0);
        Map content = (Map) candidate.get("content");
        List partsList = (List) content.get("parts");
        Map firstPart = (Map) partsList.get(0);

        return firstPart.get("text").toString();
    }
    @GetMapping("/db/load")
    public String load(){

        List<Users> users = Arrays.asList(
                new Users(null,"Alice"),
                new Users(null,"Bob"),
                new Users(null,"Charlie"),
                new Users(null,"David")
        );

        userRepo.saveAll(users);

        return "Demo data loaded successfully";
    }
    @GetMapping("/db/get")
    public List<Users> get(){
        return userRepo.findAll();
    }
}
