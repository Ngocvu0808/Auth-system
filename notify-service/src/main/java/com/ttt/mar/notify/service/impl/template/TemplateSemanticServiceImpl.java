package com.ttt.mar.notify.service.impl.template;


import com.ttt.mar.notify.dto.template.TemplateSemanticResponse;
import com.ttt.mar.notify.entities.template.TemplateSemantic;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;
import com.ttt.mar.notify.mapper.template.TemplateSemanticMapper;
import com.ttt.mar.notify.repositories.template.TemplateSemanticRepository;
import com.ttt.mar.notify.service.iface.template.TemplateSemanticService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TemplateSemanticServiceImpl implements TemplateSemanticService {

  private final TemplateSemanticRepository templateSemanticRepository;
  private final TemplateSemanticMapper templateSemanticMapper;

  public TemplateSemanticServiceImpl(TemplateSemanticRepository templateSemanticRepository,
      TemplateSemanticMapper templateSemanticMapper) {
    this.templateSemanticRepository = templateSemanticRepository;
    this.templateSemanticMapper = templateSemanticMapper;
  }

  /**
   * apiNote API get list template semantic
   *
   * @param type type of template semantic
   * @return List Template semantic
   */
  @Override
  public List<TemplateSemanticResponse> getTemplateSemanticByType(TemplateSemanticType type) {
    List<TemplateSemantic> listTemplateByType = templateSemanticRepository
        .getListTemplateSemantic(type);
    List<TemplateSemanticResponse> listDataResponse = new ArrayList<>();
    if (listTemplateByType != null && !listTemplateByType.isEmpty()) {
      listDataResponse = listTemplateByType.stream()
          .map(templateSemanticMapper::toDto).collect(Collectors.toList());
    }
    return listDataResponse;
  }
}
