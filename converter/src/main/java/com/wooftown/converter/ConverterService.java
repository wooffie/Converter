package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConverterService {

    Map<Format, ObjectMapper> mappers;

    ConverterService() {
        mappers = new HashMap<>();
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
        xmlMapper.configure(ToXmlGenerator.Feature.UNWRAP_ROOT_OBJECT_NODE, true);
        mappers.put(Format.XML, xmlMapper);
        mappers.put(Format.YAML, new YAMLMapper());
        mappers.put(Format.JSON, new JsonMapper());
    }

    JsonNode read(String string, Format format) throws JsonProcessingException {
        if (format == Format.XML) {
            if (string.startsWith("<?xml")) {
                string = string.substring(string.indexOf("?>") + 2);
            }
            string = "<tag>" + string + "</tag>";
        }
        return mappers.get(format).readTree(string);
    }

    String convert(JsonNode node, Format format) throws JsonProcessingException {
        return mappers.get(format).writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }


}
