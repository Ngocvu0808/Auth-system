package com.ttt.mar.leads.service;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.dto.DistributeByProjectResponse;
import com.ttt.mar.leads.dto.ProjectDistributeResponseDto;
import com.ttt.mar.leads.entities.ApiMethod;
import com.ttt.mar.leads.entities.DistributeApiStatus;
import com.ttt.mar.leads.service.iface.ProjectDistributeService;
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
@RunWith(MockitoJUnitRunner.class)
public class ProjectDistributeServiceTest {

  @Mock
  private ProjectDistributeService projectDistributeService;

  private final Integer projectId = 1;

  @Test
  public void findDistributeNotExistProjectTest() throws ResourceNotFoundException {
    List<DistributeByProjectResponse> distributeByProjectResponses = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      DistributeByProjectResponse response = new DistributeByProjectResponse();
      response.setId(i);
      response.setName("Lead-Source" + i);
      distributeByProjectResponses.add(response);
    }
    when(projectDistributeService
        .findDistributeNotExistProject(projectId, DistributeApiStatus.ACTIVE))
        .thenReturn(distributeByProjectResponses);
    List<DistributeByProjectResponse> responseResults = projectDistributeService
        .findDistributeNotExistProject(projectId, DistributeApiStatus.ACTIVE);

    Assertions.assertEquals(responseResults.size(), distributeByProjectResponses.size());
  }

  @Test
  public void deleteProjectDistributeTest()
      throws OperationNotImplementException, ResourceNotFoundException {
    Integer projectSourceId = 1;
    Integer userId = 1;
    when(projectDistributeService.deleteProjectDistribute(userId, projectId, projectSourceId))
        .thenReturn(true);
    Boolean result = projectDistributeService
        .deleteProjectDistribute(userId, projectId, projectSourceId);
    Assertions.assertEquals(result, true);
  }

  @Test
  public void getProjectDistributeFilterTest() throws ResourceNotFoundException {
    List<ProjectDistributeResponseDto> projectDistributeResponseDtos = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      ProjectDistributeResponseDto distributeResponseDto = new ProjectDistributeResponseDto();
      distributeResponseDto.setId(i + 1);
      distributeResponseDto.setName("name " + (i + 1));
      distributeResponseDto.setMethod(ApiMethod.GET);
      distributeResponseDto.setStatus(DistributeApiStatus.ACTIVE);
      distributeResponseDto.setUrl("url: " + (i + 1));
      projectDistributeResponseDtos.add(distributeResponseDto);
    }
    DataPagingResponse<ProjectDistributeResponseDto> data = new DataPagingResponse<>();
    data.setList(projectDistributeResponseDtos);
    data.setCurrentPage(1);
    data.setNum(projectDistributeResponseDtos.size());
    data.setTotalPage(1);

    when(projectDistributeService.getProjectDistributeFilter(projectId, 1, 10, ""))
        .thenReturn(data);

    DataPagingResponse<ProjectDistributeResponseDto> resp = projectDistributeService
        .getProjectDistributeFilter(projectId, 1, 10, "");

    Assertions.assertEquals(resp.getNum(), 5);
    Assertions.assertEquals(resp.getTotalPage(), 1);
  }
}
