package com.ttt.mar.notify.service.iface.template;

import com.ttt.mar.notify.dto.template.TemplateSemanticResponse;
import com.ttt.mar.notify.entities.template.TemplateSemanticType;
import java.util.List;

public interface TemplateSemanticService {

  List<TemplateSemanticResponse> getTemplateSemanticByType(TemplateSemanticType type);
}
