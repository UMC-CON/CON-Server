package com.umc.cons.share.util;

import com.umc.cons.share.domain.entity.Share;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScoredShare {
    private final Share share;
    private final double score;

}
