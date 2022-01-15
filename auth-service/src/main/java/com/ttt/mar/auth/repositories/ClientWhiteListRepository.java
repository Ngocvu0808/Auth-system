package com.ttt.mar.auth.repositories;


import com.ttt.mar.auth.entities.application.Client;
import com.ttt.mar.auth.entities.application.ClientWhiteList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bontk
 * @created_date 05/06/2020
 */
public interface ClientWhiteListRepository extends JpaRepository<ClientWhiteList, Integer> {

  Optional<ClientWhiteList> findByIpAndClientIdAndIsDeletedFalse(String ip, Integer clientId);

  List<ClientWhiteList> findAllByClientAndIsDeletedFalse(Client client);

}
