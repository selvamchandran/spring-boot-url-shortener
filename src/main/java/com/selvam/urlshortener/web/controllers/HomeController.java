package com.selvam.urlshortener.web.controllers;

import com.selvam.urlshortener.ApplicationProperties;
import com.selvam.urlshortener.domain.entities.ShortUrl;
import com.selvam.urlshortener.domain.entities.User;
import com.selvam.urlshortener.domain.exceptions.ShortUrlNotFoundException;
import com.selvam.urlshortener.domain.models.CreateShortUrlCmd;
import com.selvam.urlshortener.domain.models.PagedResult;
import com.selvam.urlshortener.domain.models.ShortUrlDto;
import com.selvam.urlshortener.domain.services.ShortUrlService;
import com.selvam.urlshortener.web.dtos.CreateShortUrlForm;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties properties;
    private final SecurityUtils securityUtils;

    public HomeController(ShortUrlService shortUrlService, ApplicationProperties properties, SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "1") Integer page,
            Model model) {
        addShortUrlsDataToModel(model,page);
        model.addAttribute("paginationUrl", "/");
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm("", false,null));
        return "index";
    }

    private void addShortUrlsDataToModel(Model model,Integer page) {
        PagedResult<ShortUrlDto> shortUrls = shortUrlService.findAllPublicShortUrls(page,properties.pageSize());
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
    }

    @PostMapping("/short-urls")
    public String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            addShortUrlsDataToModel(model,1);
            return "index";
        }
        try {
            Long userId = securityUtils.getCurrentUserId();
            CreateShortUrlCmd createShortUrlCmd = new CreateShortUrlCmd(
                    form.originalUrl(),
                    form.isPrivate(),
                    form.expirationInDays(),
                    userId);
            ShortUrlDto shortUrl = shortUrlService.createShortUrl(createShortUrlCmd);
            redirectAttributes.addFlashAttribute("successMessage", "Short URL created successfully "+
                    properties.baseUrl()+"/s/"+shortUrl.shortKey());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create Short URL");
        }

        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    public String redirectToOriginalUrl(@PathVariable String shortKey){
        Long userId = securityUtils.getCurrentUserId();
        Optional<ShortUrlDto> shortUrlDtoOptional = shortUrlService.accessShortUrl(shortKey,userId);
        if (shortUrlDtoOptional.isEmpty()){
            throw new ShortUrlNotFoundException("Invalid short key: "+shortKey);
        }
        ShortUrlDto shortUrlDto = shortUrlDtoOptional.get();
        return "redirect:"+shortUrlDto.originalUrl();
    }

    @GetMapping("/login")
    String loginForm()
    {
        return "login";
    }


    @GetMapping("/my-urls")
    public String showUrls(
        @RequestParam(defaultValue = "1") int page,
        Model model
    ){
        var currentUserId = securityUtils.getCurrentUserId();
        PagedResult<ShortUrlDto> shortUrls =
                shortUrlService.getUserShortUrls(currentUserId, page, properties.pageSize());
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("paginationUrl", "my-urls");
        return "my-urls";
    }

    @PostMapping("delete-urls")
    @PreAuthorize("hasAnyRole('USER','MODERATOR')")
    public String deleteUrls(
            @RequestParam(value="ids", required = false) List<Long>
    ids, RedirectAttributes redirectAttributes){
        if(ids == null || ids.isEmpty()){
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "No URLs selected for deletion");
            return "redirect:/my-urls";
        }
        try{
            var currentUserId = securityUtils.getCurrentUserId();
            shortUrlService.deleteShortUrls(ids, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Selected URLs have been deleted successfully");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error deleting URLs " + e.getMessage());
        }
        return "redirect:/my-urls";
    }
















}
