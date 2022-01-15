package com.ttt.mar.auth.repositories;

import com.ttt.rnd.lib.entities.LogRequest;
import com.ttt.rnd.lib.repositories.LogRequestRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author bontk
 * @created_date 20/08/2020
 */
public interface LogRequestRepositoryExtended extends LogRequestRepository,
    JpaSpecificationExecutor<LogRequest> {

  List<LogRequest> findAllByTokenAndAuthType(String token, String authType);
}
