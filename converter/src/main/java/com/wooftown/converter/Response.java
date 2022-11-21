package com.wooftown.converter;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Response {

    String message;

    Map<Format, String> result = new HashMap<>();
}
