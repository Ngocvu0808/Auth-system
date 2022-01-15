package com.ttt.mar.auth.mapper;

import com.ttt.mar.auth.dto.appservice.ServiceCustomDto;
import com.ttt.mar.auth.dto.service.CustomServiceDto;
import com.ttt.mar.auth.dto.service.ServiceRequestDto;
import com.ttt.mar.auth.dto.service.ServiceResponseDto;
import com.ttt.mar.auth.entities.service.Service;
import com.ttt.mar.auth.entities.service.ServiceTag;
import com.ttt.mar.auth.entities.service.Tag;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", uses = {SystemMapper.class, ExternalApiMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ServiceMapper {

  public abstract ServiceCustomDto toDto(Service api);

  public abstract CustomServiceDto toCustomDto(Service api);

  public abstract Service fromDto(ServiceCustomDto apiDto);


  public abstract Service fromServiceRequestDto(ServiceRequestDto serviceRequestDto);

  @Mapping(target = "nameSystem", source = "system.name")
  @Mapping(target = "creatorName", source = "creatorUser.username")
  public abstract ServiceResponseDto toServiceResponseDto(Service service);

  @AfterMapping
  public void afterMapping(@MappingTarget ServiceResponseDto serviceResponseDto, Service service) {
    Set<String> tags = new HashSet<>();
    List<ServiceTag> serviceTags = service.getServiceTags();
    if (serviceTags != null) {
      List<ServiceTag> listServiceTagFilter = serviceTags.stream()
          .filter(serviceTag -> !serviceTag.getIsDeleted()).collect(Collectors.toList());

      listServiceTagFilter.forEach(serviceTag -> {
        Tag tag = serviceTag.getTag();
        if (tag != null) {
          tags.add(tag.getTag());
        }
      });
    }
    serviceResponseDto.setTags(tags);
  }


  @Mapping(target = "code", source = "code", ignore = true)
  public abstract void updateModel(@MappingTarget Service service,
      ServiceRequestDto serviceRequestDto);

}
