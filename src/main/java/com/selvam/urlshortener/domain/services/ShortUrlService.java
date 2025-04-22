package com.selvam.urlshortener.domain.services;

import com.selvam.urlshortener.ApplicationProperties;
import com.selvam.urlshortener.domain.entities.ShortUrl;
import com.selvam.urlshortener.domain.models.CreateShortUrlCmd;
import com.selvam.urlshortener.domain.models.PagedResult;
import com.selvam.urlshortener.domain.models.ShortUrlDto;
import com.selvam.urlshortener.domain.repositories.ShortUrlRepository;
import com.selvam.urlshortener.domain.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;
    private final UserRepository userRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, EntityMapper entityMapper, ApplicationProperties properties, UserRepository userRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.entityMapper = entityMapper;
        this.properties = properties;
        this.userRepository = userRepository;
    }

    public PagedResult<ShortUrlDto> findAllPublicShortUrls(int pageNo, int pageSize) {
        Pageable pageable = getPageable(pageNo, pageSize);
//        Page<ShortUrl> shortUrl = shortUrlRepository.findAll(pageable);

        Page<ShortUrlDto> shortUrlDtoPage = shortUrlRepository.findPublicShortUrls(pageable)
                .map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlDtoPage);
    }

    private static Pageable getPageable(int pageNo, int pageSize) {
        pageNo = pageNo > 1 ? pageNo - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return pageable;
    }

    @Transactional
    public ShortUrlDto createShortUrl(CreateShortUrlCmd createShortUrlCmd) {
        if (properties.validateOriginalUrl()) {
            boolean urlExists = UrlExistenceValidator.isUrlExists(createShortUrlCmd.originalUrl());
            if (!urlExists) {
                throw new RuntimeException("Invalid URL " + createShortUrlCmd.originalUrl());
            }
        }
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(createShortUrlCmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        if (createShortUrlCmd.userId() == null) {
            shortUrl.setCreatedBy(null);
            shortUrl.setPrivate(false);
            shortUrl.setExpiresAt(LocalDateTime.now().plusDays(properties.defaultExpiryInDays()));
        } else {
            shortUrl.setCreatedBy(userRepository.findById(createShortUrlCmd.userId()).orElseThrow());
            shortUrl.setPrivate(createShortUrlCmd.isPrivate() != null ? createShortUrlCmd.isPrivate() : null);
            shortUrl.setExpiresAt(
                    createShortUrlCmd.expirationInDays() != null
                            ? LocalDateTime.now().plusDays(createShortUrlCmd.expirationInDays()) : null);

        }
        shortUrl.setClickCount(0L);
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrlRepository.save(shortUrl);
        return entityMapper.toShortUrlDto(shortUrl);
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = generateSecureRandomKey();
        } while (shortUrlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final int SHORT_KEY_LENGTH = 6;
    public static final SecureRandom RANDOM = new SecureRandom();

    public static String generateSecureRandomKey() {
        StringBuilder stringBuilder = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

    @Transactional
    public Optional<ShortUrlDto> accessShortUrl(String shortKey, Long userId) {
        Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByShortKey(shortKey);
        if (shortUrlOptional.isEmpty()) {
            return Optional.empty();
        }
        ShortUrl shortUrl = shortUrlOptional.get();
        if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }
        if (shortUrl.getPrivate() != null && shortUrl.getPrivate()
                && shortUrl.getCreatedBy() != null
                && !Objects.equals(shortUrl.getCreatedBy().getId(), userId)) {
            return Optional.empty();
        }
        /* if(shortUrl.getPrivate()!=null && shortUrl.getCreatedBy()!=null &&
                shortUrl.getCreatedBy().getId()!=null){
            return Optional.empty();
        }*/
        //Increment click count
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl);
        return shortUrlOptional.map(entityMapper::toShortUrlDto);
    }

    public PagedResult<ShortUrlDto> getUserShortUrls(Long userId, int page, int pageSize) {
        Pageable pageable = getPageable(page, pageSize);
        var shortUrlDtoPage = shortUrlRepository.findByCreatedById(userId, pageable)
                .map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlDtoPage);
    }
    @Transactional
    public void deleteShortUrls(List<Long> ids, Long userId) {
        if(ids!=null && !ids.isEmpty() && userId !=null) {
            shortUrlRepository.deleteByIdInAndCreatedById(ids,userId);
        }
    }

    public PagedResult<ShortUrlDto> findAllShortUrls(int pageNo, int pageSize) {
        Pageable pageable = getPageable(pageNo, pageSize);
        Page<ShortUrlDto> shortUrlDtoPage = shortUrlRepository.findAllShortUrls(pageable)
                .map(entityMapper::toShortUrlDto);
        return PagedResult.from(shortUrlDtoPage);

    }
}
