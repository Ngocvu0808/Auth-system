package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.tag.CustomTagResponseDto;
import com.ttt.mar.auth.dto.tag.TagResponseDto;
import com.ttt.mar.auth.entities.service.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class TagMapper {

  public abstract TagResponseDto toDto(Tag tag);

  public abstract CustomTagResponseDto toCustomTag(Tag tag);
}
