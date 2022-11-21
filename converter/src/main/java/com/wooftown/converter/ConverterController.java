package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ConverterController {

    @PostMapping("/toXML")
    ResponseEntity<String> convertToXML(@RequestParam("file") MultipartFile yamlFile) {
        String yaml;
        try {
            yaml = new String(yamlFile.getBytes());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        ObjectMapper yamlMapper = new YAMLMapper();


        String xml;
        try {
            JsonNode node = yamlMapper.readTree(yaml);
            ObjectMapper xmlMapper = new XmlMapper();
            xml = xmlMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(xml);
    }

    @PostMapping("/toYaml")
    ResponseEntity<String> convertToYaml(@RequestParam("file") MultipartFile xmlFile) {
        String xml;
        try {
            xml = new String(xmlFile.getBytes());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        ObjectMapper xmlMapper = new XmlMapper();


        String yaml;
        try {
            JsonNode node = xmlMapper.readTree(xml);
            ObjectMapper yamlMapper = new YAMLMapper();
            yaml = yamlMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(yaml);
    }


}
