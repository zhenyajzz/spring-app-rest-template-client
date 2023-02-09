package com.example.springappresttemplateclient.controller;

import com.example.springappresttemplateclient.model.Movie;
import com.example.springappresttemplateclient.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@RestController
public class ClientWebController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/template/movie/images")
    public List<String> getProductList() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        List<Movie> listMovies = restTemplate.exchange("https://howtodoandroid.com/movielist.json", HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        List<String> getImages = listMovies.stream().map(x -> x.getImageUrl()).toList();
        return getImages;

    }

    @RequestMapping(value = "/template/movie")
    public String findAllMovieDetails() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange("https://howtodoandroid.com/movielist.json",
                        HttpMethod.GET,
                        entity,
                        String.class)
                .getBody();
    }

    @RequestMapping(value = "/template/products")
    public List<Product> findAllDetails() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange("http://localhost:8080/products", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Product>>() {
                })
                .getBody();
    }

    @RequestMapping(value = "/template/products", method = RequestMethod.POST)
    public String createDetails(@RequestBody Product product) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Product> entity = new HttpEntity<>(product, headers);

        return restTemplate.exchange("http://localhost:8080/products"
                , HttpMethod.POST, entity, String.class).getBody();
    }

    @RequestMapping(value = "/template/products/{id}", method = RequestMethod.PUT)
    public String changeProduct(@PathVariable int id, @RequestBody Product product) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Product> entity = new HttpEntity<>(product, headers);

        return restTemplate.exchange("http://localhost:8080/products/" + id, HttpMethod.PUT, entity, String.class).getBody();
    }

    @RequestMapping(value = "/template/products/{id}", method = RequestMethod.DELETE)
    public String deleteProduct(@PathVariable int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Product> entity = new HttpEntity<Product>(headers);

        return restTemplate.exchange(
                "http://localhost:8080/products/" + id, HttpMethod.DELETE, entity, String.class).getBody();
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("file", getFileResource(file));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity entity = new HttpEntity<>(multiValueMap, headers);

        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8080/upload", HttpMethod.POST, entity, Object.class);

        return response;

    }

    public Resource getFileResource(MultipartFile multipartFile) throws IOException {

        Path tempFile = Files.createTempFile("SAVE", ".txt");
        Files.write(tempFile, multipartFile.getBytes());

        File fileToSend = tempFile.toFile();
        return new FileSystemResource(fileToSend);

    }
}
