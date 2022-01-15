package com.ttt.mar.auth.service.iface;

import com.ttt.mar.auth.dto.tag.TagResponseDto;
import java.util.List;

public interface TagService {

   List<TagResponseDto> getTags();
}
