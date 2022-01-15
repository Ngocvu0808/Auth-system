package com.ttt.mar.leads.service.iface;

import com.ttt.mar.leads.dto.OfferRequestDto;
import com.ttt.mar.leads.dto.OfferResponseDto;
import com.ttt.mar.leads.dto.OfferResponseListDto;
import com.ttt.mar.leads.dto.OfferUpdateRequestDto;
import com.ttt.rnd.common.dto.DataPagingResponse;
import com.ttt.rnd.common.exception.OperationNotImplementException;
import com.ttt.rnd.common.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface OfferService {

  Integer createOffer(Integer userId, OfferRequestDto dto)
      throws OperationNotImplementException, ResourceNotFoundException;

  Integer updateOffer(Integer userId, Integer offerId, OfferUpdateRequestDto dto)
      throws ResourceNotFoundException, OperationNotImplementException;

  OfferResponseDto getbyId(Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;

  @Transactional
  Boolean deleteOffer(Integer userId, Integer id)
      throws ResourceNotFoundException, OperationNotImplementException;


  DataPagingResponse<OfferResponseListDto> getAll(Integer page, Integer limit, Long fromDate,
      Long toDate, String search, String sort);

}
