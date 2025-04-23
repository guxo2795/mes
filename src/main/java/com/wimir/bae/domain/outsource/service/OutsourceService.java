package com.wimir.bae.domain.outsource.service;

import com.wimir.bae.domain.contract.dto.ContractInfoDTO;
import com.wimir.bae.domain.contract.mapper.ContractMapper;
import com.wimir.bae.domain.outsource.dto.*;
import com.wimir.bae.domain.outsource.mapper.OutsourceMapper;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OutsourceService {

    private final OutsourceMapper outsourceMapper;
    private final ContractMapper contractMapper;

    public void createOutsource(UserLoginDTO userLoginDTO, OutsourceRegDTO outsourceRegDTO) {

        if(outsourceMapper.isExistOutsource(outsourceRegDTO.getContractKey(), outsourceRegDTO.getProductKey())){
            throw new CustomRuntimeException("이미 등록된 외주 품목입니다.");
        }
        
        // 수주에 포함된 완제품 하나만 반환(LIMIT 1) => 최적화
        ContractInfoDTO contractInfo = contractMapper.getContractAndItemInfo(outsourceRegDTO.getContractKey());

        if(contractInfo == null){
            throw new CustomRuntimeException("수주가 존재하지 않거나 예기치 못한 오류가 발생하였습니다.");
        }
        if("0".equals(contractInfo.getIsCompleted())) {
            throw new CustomRuntimeException("실행전 수주는 외주를 등록할 수 없습니다.");
        }
        if ("1".equals(contractInfo.getIsCompleted())) {
            throw new CustomRuntimeException("종결된 수주에는 외주를 등록할 수 없습니다.");
        }

        // 품목 존재 확인
        Optional.ofNullable(contractInfo.getProductKey())
                .filter(key -> !key.isEmpty())
                .orElseThrow(() -> new CustomRuntimeException("존재하지 않는 품목입니다."));

        // 수주에 등록된 완제품은 등록 못하게
        // ??
        // 완제품이 외주 생산일 경우에는?
        if (contractInfo.getProductKey().equals(outsourceRegDTO.getProductKey())) {
            throw new CustomRuntimeException("수주의 완제품은 등록 하지 못합니다. 수주의 외주 품목을 등록해 주세요.");
        }

        outsourceMapper.createOutsource(outsourceRegDTO);
    }

    public void updateOutsource(UserLoginDTO userLoginDTO, OutsourceUpdateDTO outsourceUpdateDTO) {

        OutsourceSearchInfoDTO outsourceSearchInfoDTO = outsourceMapper.searchOutsourceInfo(outsourceUpdateDTO.getOutsourceKey());

        if (outsourceSearchInfoDTO == null) {
            throw new CustomRuntimeException("존재하지 않는 외주입니다.");
        }
        if("C".equals(outsourceSearchInfoDTO.getOutsourceState())){
            throw new CustomRuntimeException("이미 완료된 외주는 수정할 수 없습니다.");
        }

        boolean quantityFlag = false;
        if(!outsourceUpdateDTO.getQuantity().isEmpty()){
            quantityFlag = true;
            if("O".equals(outsourceSearchInfoDTO.getOutsourceState())){
                throw new CustomRuntimeException("이미 출고 완료되었거나 일부 출고된 외주는 수량을 수정할 수 없습니다.");
            }
        }

        if(outsourceUpdateDTO.getOutboundDateTime() == null || outsourceUpdateDTO.getInboundEstDate() == null || !quantityFlag){
            throw new CustomRuntimeException("수정 사항을 입력해주세요.");
        } else {
            outsourceMapper.updateOutsource(outsourceUpdateDTO);
        }

    }

    public void deleteOutsource(UserLoginDTO userLoginDTO, List<String> outsourceKeyList) {

        for(String outsourceKey : outsourceKeyList){
            OutsourceSearchInfoDTO outsourceSearchInfoDTO = outsourceMapper.searchOutsourceInfo(outsourceKey);
            if(outsourceSearchInfoDTO == null){
                throw new CustomRuntimeException("존재하지 않는 외주입니다.");
            }
            if(outsourceSearchInfoDTO.getOutsourceState().equals("C")){
                throw new CustomRuntimeException("이미 완료된 외주는 삭제할 수 없습니다.");
            }
            if(outsourceSearchInfoDTO.getOutsourceState().equals("O")){
                throw new CustomRuntimeException("이미 출고 완료되었거나 일부 출고된 외주는 삭제할 수 없습니다.");
            }
            outsourceMapper.deleteOutsource(outsourceKey);
        }
    }

    @Transactional(readOnly = true)
    public List<OutsourceContractListDTO> getOutsourceContractList() {

        // 실행중인 수주 리스트(품목이 존재하지 않아도 수주가 실행만 되어있으면 일단 포함)
        List<ContractInfoDTO> contractExecutedList = contractMapper.getStartContractInfoList();

        List<OutsourceContractListDTO> outsourceContractList = new ArrayList<>();
        for (ContractInfoDTO executedList : contractExecutedList) {
            // 외주 생산인 품목 정보 list
            List<OutsourceItemDTO> outsourceItemDTOList = outsourceMapper.getOutsourceItemList(executedList.getContractCode(), "14");

            if (outsourceItemDTOList == null || outsourceItemDTOList.isEmpty()) {
                continue;
            }

            OutsourceContractListDTO outsourceContractListDTO = new OutsourceContractListDTO();
            outsourceContractListDTO.setContractInfo(executedList);
            outsourceContractListDTO.setOutsourceItemList(outsourceItemDTOList);
            outsourceContractList.add(outsourceContractListDTO);
        }
        return outsourceContractList;
    }

    @Transactional(readOnly = true)
    public List<OutsourceIncomingListDTO> getOutsourceIncomingList() {

        // 외주 등록된 모든 품목 리스트
        // 쿼리 이해 잘 안됨
        List<OutsourceIncomingStateDTO> outsourceIncomingStateDTOList = outsourceMapper.getOutsourceAllState();

        // 반환할 리스트
        List<OutsourceIncomingListDTO> outsourceIncomingList = new ArrayList<>();
        for(OutsourceIncomingStateDTO incomingStateDTO : outsourceIncomingStateDTOList){
            // OutsourceIncomingStateDTO
            OutsourceIncomingListDTO outsourceIncomingListDTO = new OutsourceIncomingListDTO();
            outsourceIncomingListDTO.setContractInfo(incomingStateDTO);

            // List<OutsourceIncomingDTO>
            outsourceIncomingListDTO.setOutsourceIncomingList(outsourceMapper.getOutsourceCreateAllList());
            outsourceIncomingList.add(outsourceIncomingListDTO);
        }
        return outsourceIncomingList;
    }
}
