package com.wooftown.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConverterController {

    @Autowired
    ConverterService converterService;

    @GetMapping("/help")
    String help() throws IOException {
        URL url = Resources.getResource("static/help");
        return Resources.toString(url, StandardCharsets.UTF_8);
    }


    @PostMapping("/convert")
    ResponseEntity<Response> convert(@RequestParam(value = "string", required = false) String string,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam(value = "source", required = false) Format sourceFormat,
                                     @RequestParam(value = "target", required = false) List<Format> targetFormats) {
        Response response = new Response();

        if (file == null && string == null) {
            response.setMessage("No input provided! get /api/help for more info");
            return ResponseEntity.badRequest().body(response);
        }
        if (sourceFormat == null || targetFormats == null || targetFormats.isEmpty()) {
            response.setMessage("No formats specified! get /api/help for more info");
            return ResponseEntity.badRequest().body(response);
        }

        JsonNode node;
        try {
            String sourceString = string;
            if (file != null) {
                sourceString = new String(file.getBytes());
            }

            node = converterService.read(sourceString, sourceFormat);
        } catch (IOException e) {
            response.setMessage("Invalid input! get /api/help for more info");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            for (Format f : targetFormats) {
                response.getResult().put(f, converterService.convert(node, f));
            }
        } catch (JsonProcessingException e) {
            response.setMessage("Can't convert your input! get /api/help for more info");
            response.setResult(null);
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }


}
