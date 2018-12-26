package org.sopt.artoo.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.mapper.ArtworkMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArtworkService {

    private final ArtworkMapper artworkMapper;

    public ArtworkService(final ArtworkMapper artworkMapper) {
        this.artworkMapper = artworkMapper;
    }
}
