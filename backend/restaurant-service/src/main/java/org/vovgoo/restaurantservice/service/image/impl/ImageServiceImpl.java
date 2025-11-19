package org.vovgoo.restaurantservice.service.image.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.vovgoo.restaurantservice.config.image.ImgbbProperties;
import org.vovgoo.restaurantservice.exception.custom.image.ImageReadException;
import org.vovgoo.restaurantservice.exception.custom.image.ImageUploadException;
import org.vovgoo.restaurantservice.service.image.ImageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImgbbProperties imgbbProperties;
    private final ObjectMapper objectMapper;

    private WebClient webClient;

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder()
                .baseUrl(imgbbProperties.getEndpoint())
                .build();
    }

    public String uploadImage(MultipartFile file) {

        try {
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("image", resource);

            String response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("key", imgbbProperties.getApiKey())
                            .build())
                    .bodyValue(builder.build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            String url = root.path("data").path("image").path("url").asText();

            if (url == null || url.isEmpty()) {
                throw new ImageUploadException();
            }

            return url;

        } catch (IOException e) {
            throw new ImageReadException();
        } catch (RuntimeException e) {
            throw new ImageUploadException();
        }
    }
}