package org.sopt.artoo.model;

import lombok.Data;
import org.sopt.artoo.dto.Artwork;
import org.sopt.artoo.dto.Display;

import java.util.List;

@Data
public class DisplayApplyRes {
    private List<Display> displays;
    private List<Artwork> artworks;
}
