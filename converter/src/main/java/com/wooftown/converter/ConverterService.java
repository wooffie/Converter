package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConverterService {

    Map<Format, ObjectMapper> mappers;

    ConverterService() {
        mappers = new HashMap<>();
        mappers.put(Format.XML, new XmlMapper());
        mappers.put(Format.YAML, new YAMLMapper());
        mappers.put(Format.JSON, new JsonMapper());
    }

    JsonNode read(String string, Format format) throws JsonProcessingException {
        return mappers.get(format).readTree(string);
    }

    String convert(JsonNode node, Format format) throws JsonProcessingException {
        return mappers.get(format).writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }



}
