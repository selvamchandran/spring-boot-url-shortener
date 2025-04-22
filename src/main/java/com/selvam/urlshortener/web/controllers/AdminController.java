package com.selvam.urlshortener.web.controllers;

import com.selvam.urlshortener.ApplicationProperties;
import com.selvam.urlshortener.domain.models.PagedResult;
import com.selvam.urlshortener.domain.models.ShortUrlDto;
import com.selvam.urlshortener.domain.services.ShortUrlService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties properties;

    public AdminController(ShortUrlService shortUrlService, ApplicationProperties properties) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "1") Integer page,
                            Model model) {
        PagedResult<ShortUrlDto> shortUrls = shortUrlService.findAllShortUrls(page,properties.pageSize());
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("paginationUrl", "/admin/dashboard");
        return "admin-dashboard";
    }
}
