package com.ttt.mar.leads.service;

import static org.mockito.Mockito.when;

import com.ttt.mar.leads.dto.ProjectUserResponseDto;
import com.ttt.mar.leads.service.iface.ProjectUserService;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.IdentifyBlankException;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import com.ttt.rnd.lib.entities.UserStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)

public class ProjectUserServiceTest {

  @Mock
  ProjectUserService projectUserService;

  @Test
  public void findProjectUserNotExistProject()
      throws OperationNotImplementException, ResourceNotFoundException {
    List<ProjectUserResponseDto> res = new ArrayList<ProjectUserResponseDto>();
    for (int i = 0; i < 3; i++) {
      ProjectUserResponseDto p1 = new ProjectUserResponseDto();
      p1.setId(i + 1000);
      p1.setName("nambn" + i);
      p1.setUsername("nambn" + i);
      p1.setRoleName("test");
      p1.setStatus(UserStatus.ACTIVE);
      res.add(p1);
    }
    DataPagingResponse<ProjectUserResponseDto> data = new DataPagingResponse<>();
    data.setList(res);
    data.setCurrentPage(1);
    data.setNum(res.size());
    data.setTotalPage(1);
    when(projectUserService.getProjectUserFilter(1, 1, 10, ""))
        .thenReturn(data);

    DataPagingResponse<ProjectUserResponseDto> resp = projectUserService
        .getProjectUserFilter(1, 1, 10, "");
    Assertions.assertEquals(resp.getNum(), 3);
  }

  @Test
  public void deleteUserOfProjectTest()
      throws OperationNotImplementException, ResourceNotFoundException, IdentifyBlankException {
    Integer userId = 1;
    Integer idProject = 1;
    Integer idProjectUser = 1;

    when(projectUserService.deleteUserOfProject(userId, idProject, idProjectUser)).thenReturn(true);
    Boolean result = projectUserService.deleteUserOfProject(userId, idProject, idProjectUser);
    Assertions.assertEquals(result, true);
  }
}
