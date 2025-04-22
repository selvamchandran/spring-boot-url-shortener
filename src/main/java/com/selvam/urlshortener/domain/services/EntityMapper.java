package com.selvam.urlshortener.domain.services;

import com.selvam.urlshortener.domain.entities.ShortUrl;
import com.selvam.urlshortener.domain.entities.User;
import com.selvam.urlshortener.domain.models.ShortUrlDto;
import com.selvam.urlshortener.domain.models.UserDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EntityMapper {
    public ShortUrlDto toShortUrlDto(ShortUrl shortUrl){
        UserDto userDto = null;
        if(shortUrl.getCreatedBy()!=null){
            userDto = toUserDto(shortUrl.getCreatedBy());
        }

        return new ShortUrlDto(
                shortUrl.getId(),
                shortUrl.getShortKey(),
                shortUrl.getOriginalUrl(),
                userDto,
                shortUrl.getPrivate(),
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt(),
                shortUrl.getExpiresAt()
                );

    }

    private UserDto toUserDto(User user) {
        return new UserDto(user.getId(),user.getName());
    }

}
