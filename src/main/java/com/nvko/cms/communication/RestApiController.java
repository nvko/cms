package com.nvko.cms.communication;

import com.nvko.cms.CmsService;
import com.nvko.cms.configuration.ConfigDetectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/**")
public class RestApiController {

    private final CmsService service;
    private final ConfigDetectService configDetectService;

    @PostMapping
    public void handleRequest(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
        final String requestPath = request.getRequestURI();
        service.handleRequest(requestPath, payload);
    }

}
