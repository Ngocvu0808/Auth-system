package com.ttt.mar.leads.service;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.dto.LeadSourceByProjectResponse;
import com.ttt.mar.leads.dto.ProjectSourceResponse;
import com.ttt.mar.leads.entities.LeadSourceStatus;
import com.ttt.mar.leads.mapper.LeadSourceMapper;
import com.ttt.mar.leads.service.iface.ProjectSourceService;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author KietDT
 * @created_date 28/04/2021
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectSourceServiceTest {

  @Mock
  private ProjectSourceService projectSourceService;

  @Mock
  private LeadSourceMapper leadSourceMapper;

  private final Integer projectId = 1;

  @Test
  public void findLeadSourceNotExistProjectTest() throws ResourceNotFoundException {
    List<LeadSourceByProjectResponse> leadSourceByProjectResponses = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      LeadSourceByProjectResponse response = new LeadSourceByProjectResponse();
      response.setId(i);
      response.setName("Lead-Source" + i);
      leadSourceByProjectResponses.add(response);
    }
    when(projectSourceService.findLeadSourceNotExistProject(projectId, LeadSourceStatus.ACTIVE))
        .thenReturn(leadSourceByProjectResponses);
    List<LeadSourceByProjectResponse> responseResults = projectSourceService
        .findLeadSourceNotExistProject(projectId, LeadSourceStatus.ACTIVE);

    Assertions.assertEquals(leadSourceByProjectResponses.size(), responseResults.size());
  }

  @Test
  public void deleteProjectSourceTest()
      throws OperationNotImplementException, ResourceNotFoundException {
    Integer projectSourceId = 1;
    Integer userId = 1;
    when(projectSourceService.deleteProjectSource(userId, projectId, projectSourceId))
        .thenReturn(true);
    Boolean result = projectSourceService.deleteProjectSource(userId, projectId, projectSourceId);
    Assertions.assertEquals(result, true);
  }

  @Test
  public void getProjectSourceFilterTest() throws ResourceNotFoundException {
    List<ProjectSourceResponse> projectSourceResponses = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ProjectSourceResponse projectSourceResponse = new ProjectSourceResponse();
      projectSourceResponse.setId(i + 1);
      projectSourceResponse.setSource("Source " + (i + 1));
      projectSourceResponse.setNameSource("Name-Source " + (i + 1));
      projectSourceResponse.setStatus(LeadSourceStatus.ACTIVE);
      projectSourceResponse.setUtmSource("utm-source " + (i + 1));
      projectSourceResponses.add(projectSourceResponse);
    }
    DataPagingResponse<ProjectSourceResponse> data = new DataPagingResponse<>();
    data.setList(projectSourceResponses);
    data.setCurrentPage(1);
    data.setNum(projectSourceResponses.size());
    data.setTotalPage(1);

    when(projectSourceService.getProjectSourceFilter(projectId, 1, 10, ""))
        .thenReturn(data);

    DataPagingResponse<ProjectSourceResponse> resp = projectSourceService
        .getProjectSourceFilter(projectId, 1, 10, "");

    Assertions.assertEquals(resp.getNum(), 5);
    Assertions.assertEquals(resp.getTotalPage(), 1);
  }
}
