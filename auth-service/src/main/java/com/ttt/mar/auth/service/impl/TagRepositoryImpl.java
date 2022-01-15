package com.ttt.mar.auth.service.impl;

import com.ttt.mar.auth.dto.tag.TagResponseDto;
import com.ttt.mar.auth.entities.service.Tag;
import com.ttt.mar.auth.mapper.TagMapper;
import com.ttt.mar.auth.repositories.TagRepository;
import com.ttt.mar.auth.service.iface.TagService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagRepositoryImpl implements TagService {

  @Autowired
  private TagRepository tagRepository;

  @Autowired
  private TagMapper tagMapper;


  @Override
  public List<TagResponseDto> getTags() {
    List<Tag> tags = tagRepository.findAll();
    List<TagResponseDto> tagResponseDtos = new ArrayList<>();
    tags.forEach(tag -> tagResponseDtos.add(tagMapper.toDto(tag)));
    return tagResponseDtos;
  }
}
