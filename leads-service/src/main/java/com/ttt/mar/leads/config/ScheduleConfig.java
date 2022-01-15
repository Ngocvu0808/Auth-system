package com.ttt.mar.leads.config;

import com.ttt.mar.leads.entities.Campaign;
import com.ttt.mar.leads.entities.CampaignStatus;
import com.ttt.mar.leads.entities.Project;
import com.ttt.mar.leads.entities.ProjectStatus;
import com.ttt.mar.leads.filter.CampaignsFilter;
import com.ttt.mar.leads.filter.ProjectFilter;
import com.ttt.mar.leads.repositories.CampaignRepository;
import com.ttt.mar.leads.repositories.ProjectRepository;
import com.ttt.mar.leads.utils.DateUtil;
import java.text.ParseException;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleConfig {

  private final CampaignRepository campaignRepository;
  private final ProjectRepository projectRepository;

  private final CampaignsFilter campaignFilter;
  private final ProjectFilter projectFilter;

  public ScheduleConfig(CampaignRepository campaignRepository, ProjectRepository projectRepository,
      CampaignsFilter campaignFilter, ProjectFilter projectFilter) {
    this.campaignRepository = campaignRepository;
    this.projectRepository = projectRepository;
    this.campaignFilter = campaignFilter;
    this.projectFilter = projectFilter;
  }

  @Scheduled(cron = "${schedule.campaign.expired}")
  public void ScheduleExpiredCampaign() throws ParseException {
    Long currentDate = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    List<Campaign> campaignExpiredList = campaignRepository.findAll(campaignFilter.filterExpired(
        CampaignStatus.ACTIVE, currentDate));
    if (!campaignExpiredList.isEmpty()) {
      campaignExpiredList.forEach(campaign -> campaign.setStatus(CampaignStatus.DEACTIVE));
      campaignRepository.saveAll(campaignExpiredList);
    }
  }

  @Scheduled(cron = "${schedule.project.expired:}")
  public void ScheduleExpiredProject() throws ParseException {
    Long currentDate = DateUtil.getOnlyDateFromTimeStamp(System.currentTimeMillis());
    List<Project> projectExpiredList = projectRepository.findAll(projectFilter.filterExpired(
        ProjectStatus.ACTIVE, currentDate));
    if (!projectExpiredList.isEmpty()) {
      projectExpiredList.forEach(project -> project.setStatus(ProjectStatus.DEACTIVE));
      projectRepository.saveAll(projectExpiredList);
    }
  }
}
