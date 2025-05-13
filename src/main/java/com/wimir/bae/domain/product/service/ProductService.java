package com.wimir.bae.domain.product.service;


import com.wimir.bae.domain.common.sub.mapper.CommonSubMapper;
import com.wimir.bae.domain.company.dto.CompanyInfoDTO;
import com.wimir.bae.domain.company.mapper.CompanyMapper;
import com.wimir.bae.domain.contract.service.ContractService;
import com.wimir.bae.domain.inventory.service.InventoryProductService;
import com.wimir.bae.domain.product.dto.ProductInfoDTO;
import com.wimir.bae.domain.product.dto.ProductModDTO;
import com.wimir.bae.domain.product.dto.ProductRegDTO;
import com.wimir.bae.domain.product.dto.ProductSearchDTO;
import com.wimir.bae.domain.product.mapper.ProductMapper;
import com.wimir.bae.domain.productCompany.dto.ProductCompanyRegDTO;
import com.wimir.bae.domain.productCompany.service.ProductCompanyService;
import com.wimir.bae.domain.user.dto.UserLoginDTO;
import com.wimir.bae.global.exception.CustomRuntimeException;
import com.wimir.bae.global.utils.CommonUtil;
import com.wimir.bae.global.utils.PagingUtil;
import com.wimir.bae.global.utils.SortUtil;
import com.wimir.bae.global.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductMapper productMapper;
//    private final CompanyMapper companyMapper;
    private final CommonSubMapper commonSubMapper;

    private final InventoryProductService inventoryProductService;
    private final ProductCompanyService productCompanyService;

    @Value("${bae.company.wimir}")
    private String WIMIR;
    @Value("${bae.process.inHouse}")
    private String inHouse;
    @Value("${bae.excel.processType}")
    private String mainProcessType;

    private final String lowProductCode = "A";
    private final String lowProductName = "B";
    private final String lowAssetTypeFlag = "C";
    private final String lowProcessTypeKey = "D";
    private final String lowProductTypeKey = "E";
    private final String lowSafetyQuantity = "F";
    private final String lowUnitKey = "G";
    private final String lowStandardPrice = "H";

    // 품목 생성
    public void createProduct(UserLoginDTO userLoginDTO, ProductRegDTO regDTO) {
        
        // 품번 중복 확인
        validateProductCode(regDTO.getProductCode());

//        // 업체 조달, 납부 확인
//        CompanyInfoDTO companyInfo = companyMapper.getCompanyInfo(regDTO.getCompanyKey());
//        if (companyInfo == null) {
//            throw new CustomRuntimeException("존재하지 않는 거래처입니다. 존재하는 거래처를 선택 후 재시도 해주세요.");
//        }
//        String companyTypeFlag = companyInfo.getCompanyTypeFlag();
//        String newAssetTypeFlag = regDTO.getAssetTypeFlag();
//
//        if (companyTypeFlag.equals("O") && !newAssetTypeFlag.equals("M")) {
//            throw new CustomRuntimeException("조달 업체에는 자재만 등록할 수 있습니다.");
//        } else if (companyTypeFlag.equals("S") && !newAssetTypeFlag.equals("F")) {
//            throw new CustomRuntimeException("납품 업체에는 완제품만 등록할 수 있습니다.");
//        }
        
        // 품목 생성
        productMapper.createProduct(regDTO);
        
        // 사내생산일 경우 업체 자동으로 WIMIR 매핑
        if(inHouse.equals(regDTO.getProcessTypeKey())) {
            ProductCompanyRegDTO productCompanyRegDTO = new ProductCompanyRegDTO();
            productCompanyRegDTO.setProductKey(regDTO.getProductKey());
            productCompanyRegDTO.setCompanyKey(WIMIR);
            productCompanyService.createProductCompany(userLoginDTO, productCompanyRegDTO);
        }

        // 품목 등록 시 초기 재고 설정
        inventoryProductService.setInitialInventoryByProduct(regDTO.getProductKey());
    }

    // 품목 목록 조회
    @Transactional(readOnly = true)
    public List<ProductInfoDTO> getProductList(ProductSearchDTO searchDTO) {

        searchDTO.setOffset(
                PagingUtil.getPagingOffset(
                        searchDTO.getPage(),
                        searchDTO.getRecord()
                )
        );

        searchDTO.setSort(SortUtil.getDBSortStr(searchDTO.getSort()));

        return Optional.ofNullable(productMapper.getProductList(searchDTO))
                .orElse(Collections.emptyList());
    }

    // 품목 수정
    public void updateProduct(UserLoginDTO userLoginDTO, ProductModDTO modDTO) {
        
        ProductInfoDTO productInfoDTO = productMapper.getProductInfo(modDTO.getProductKey());

        // 품목 존재 여부 확인
        validateProductExists(productInfoDTO);
        
        // 품번 중복 확인
        String oldProductCode = productInfoDTO.getProductCode();
        String newProductCode = modDTO.getProductCode();
        if(!oldProductCode.equals(newProductCode)) {
            validateProductCode(newProductCode);
        }

        productMapper.updateProduct(modDTO);

    }

    // 품목 삭제
    public void deleteProduct(UserLoginDTO userLoginDTO, List<String> productKeyList) {

//        for(String productKey : productKeyList) {
//            ProductInfoDTO productInfoDTO = productMapper.getProductInfo(productKey);
//
//            // 품목 존재 여부 확인
//            validateProductExists(productInfoDTO);
//
//            productMapper.deleteProduct(productKey);
//
//        }

        List<ProductInfoDTO> productInfoDTOList = productMapper.getProductInfoList(productKeyList);

        // 유효성 검증
        if(productInfoDTOList.size() != productKeyList.size()) {
            throw new CustomRuntimeException("존재하지 않는 품목이 포함되어 있습니다.");
        }

        productMapper.deleteProductList(productKeyList);
        
    }

    private void validateProductExists(ProductInfoDTO productInfoDTO) {
        if(productInfoDTO == null) {
            throw new CustomRuntimeException("존재하지 않는 품목입니다. 다시 확인해 주세요.");
        }
    }

    private void validateProductCode(String ProductCode) {
        if(productMapper.isProductExist(ProductCode)) {
            throw new CustomRuntimeException("이미 존재하는 품번입니다.");
        }
    }

    // 엑셀 템플릿 업로드
    public void uploadProductTemplate(UserLoginDTO userLoginDTO, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new CustomRuntimeException("업로드할 파일이 없습니다. 다시 확인해 주세요.");
        }

        String fileName = file.getOriginalFilename();
        ValidationUtil.isValidFileName(fileName);

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!List.of("xlsx", "xls").contains(extension)) {
            throw new CustomRuntimeException("지원하지 않는 확장자입니다. 확장자는 xlsx, xls만 가능합니다.");
        }

        try (InputStream inputStream = file.getInputStream()) {

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            if(sheet.getPhysicalNumberOfRows() <= 1) {
                throw new CustomRuntimeException("등록할 행이 없습니다.");
            }

            boolean isFirstRow = true;

            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // 첫 번째 행은 헤더이므로 건너뜀
                if(isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                // 메모가 적혀있는 부분도 셀 개수를 세어서 헤더 개수만 정확하게 세어지지 않음.
//                if(row.getPhysicalNumberOfCells() != 8){
//                    throw new CustomRuntimeException("열 개수가 일치하지 않습니다."+row.getPhysicalNumberOfCells());
//                }

                ProductRegDTO productRegDTO = new ProductRegDTO();

                int rowIndex = row.getRowNum() + 1;

                for(Cell cell : row) {

                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case 0: // 품번
                            productRegDTO.setProductCode(getProductCode(cell, rowIndex));
                            break;
                        case 1: // 품명
                            productRegDTO.setProductName(getProductName(cell, rowIndex));
                            break;
                        case 2: // 완제품/자재 구분
                            productRegDTO.setAssetTypeFlag(getAssetTypeFlag(cell, rowIndex));
                            break;
                        case 3: // 공정 유형
                            productRegDTO.setProcessTypeKey(getProcessTypeKey(cell, rowIndex));
                            break;
                        case 4: // 분류
                            productRegDTO.setProductTypeKey(getProductTypeKey(cell, rowIndex));
                            break;
                        case 5: // 안전재고
                            productRegDTO.setSafetyQuantity(
                                    getSafetyQuantity(cell, rowIndex, productRegDTO.getAssetTypeFlag()));
                            break;
                        case 6: // 단위
                            productRegDTO.setUnitKey(
                                    getUnitKey(cell, rowIndex, productRegDTO.getAssetTypeFlag()));
                            break;
                        case 7: // 단가
                            productRegDTO.setStandardPrice(
                                    getStandardPrice(cell, rowIndex, productRegDTO.getAssetTypeFlag()));
                            break;
                        default:
                            break;
                    }
                }

                // 행 업로드
                productMapper.createProduct(productRegDTO);
                inventoryProductService.setInitialInventoryByProduct(productRegDTO.getProductKey());
            }

        } catch (IOException e) {
            throw new CustomRuntimeException("엑셀 불러오기에 실패했습니다.");
        }

    }

    // 컬럼: 품번
    private String getProductCode(Cell cell, int rowIndex) {

        String productCode = CommonUtil.getCellValueAsString(cell);

        if(productCode == null || productCode.isBlank())
            throw new CustomRuntimeException(
                    "품번은 비어있을 수 없습니다. " + lowProductCode + rowIndex + " 셀을 확인해 주세요."
            );
        if(productCode.length() > 50)
            throw new CustomRuntimeException(
                    "품번은 50자를 넘을 수 없습니다. " + lowProductCode + rowIndex + " 셀을 확인해 주세요."
            );
        if(productMapper.isProductExist(productCode))
            throw new CustomRuntimeException(
                    "이미 존재하는 품번입니다. " + lowProductCode + rowIndex + " 셀을 확인해 주세요."
            );

        return productCode;
    }

    // 컬럼 : 품명
    private String getProductName(Cell cell, int rowIndex) {

        String productName = CommonUtil.getCellValueAsString(cell);

        if (productName == null || productName.isEmpty())
            throw new CustomRuntimeException(
                    "품명은 비어 있을 수 없습니다. " + lowProductName + rowIndex + " 셀을 확인해 주세요."
            );
        if (productName.length() > 50)
            throw new CustomRuntimeException(
                    "품명은 50자를 넘을 수 없습니다. " + lowProductName + rowIndex + " 셀을 확인해 주세요."
            );

        return productName;
    }

    // 컬럼 : 완제품/자재 구분
    private String getAssetTypeFlag(Cell cell, int rowIndex) {

        String assetType = CommonUtil.getCellValueAsString(cell);

        if (assetType == null || assetType.isEmpty())
            throw new CustomRuntimeException(
                    "품목 구분은 비어 있을 수 없습니다. " + lowAssetTypeFlag + rowIndex + " 셀을 확인해 주세요."
            );
        if (assetType.equals("완제품"))
            return "F";
        else if (assetType.equals("자재"))
            return "M";
        else
            throw new CustomRuntimeException(
                    "품목은 완제품, 자재 중에 한글로 정확히 기입해야합니다. " + lowAssetTypeFlag + rowIndex + " 셀을 확인해 주세요."
            );
    }

    // 컬럼 : 공정 유형
    private String getProcessTypeKey(Cell cell, int rowIndex) {

        String processTypeName = CommonUtil.getCellValueAsString(cell);

        if (processTypeName == null || processTypeName.isEmpty())
            throw new CustomRuntimeException(
                    "공정 유형은 비어 있을 수 없습니다. " + lowProcessTypeKey + rowIndex + " 셀을 확인해 주세요."
            );

        String processTypeKey = productMapper.getProductProcessKey(processTypeName, mainProcessType);

        if (productMapper.isProductProcessExist(processTypeKey) < 1) {
            throw new CustomRuntimeException(
                    "존재하지 않는 공정 유형입니다. " + lowProcessTypeKey + rowIndex + " 셀을 확인해 주세요."
            );
        }

        return processTypeKey;
    }

    // 컬럼 : 분류
    private String getProductTypeKey(Cell cell, int rowIndex) {

        String productType = CommonUtil.getCellValueAsString(cell);

        if (productType == null || productType.isEmpty())
            throw new CustomRuntimeException(
                    "분류는 비어 있을 수 없습니다. " + lowProductTypeKey + rowIndex + " 셀을 확인해 주세요."
            );
        if (commonSubMapper.getProductTypeCount(productType) > 1)
            throw new CustomRuntimeException(
                    "공통 코드에서 분류명이 중복되어 있어 확인이 필요합니다. " + lowProductTypeKey + rowIndex + " 셀을 확인해 주세요."
            );

        String productTypeKey = commonSubMapper.getProductTypeKey(productType);
        if (productTypeKey == null)
            throw new CustomRuntimeException(
                    "존재하지 않는 분류명입니다. " + lowProductTypeKey + rowIndex + " 셀을 확인해 주세요."
            );

        return productTypeKey;
    }

    // 컬럼 : 안전재고
    private String getSafetyQuantity(Cell cell, int rowIndex, String assetTypeFlag) {

        String quantity = CommonUtil.getCellValueAsString(cell);

        if (quantity == null || quantity.isEmpty())
            throw new CustomRuntimeException(
                    "올바른 안전 재고를 입력해 주시기 바랍니다. " + lowSafetyQuantity + rowIndex + " 셀을 확인해 주세요."
            );
        if (assetTypeFlag.equals("F")) {
            // 완제품 안전 재고 소수점 버림
            quantity = quantity.replaceAll("\\..*", "");
            if (!quantity.matches("^[1-9]\\d*$"))
                throw new CustomRuntimeException(
                        "완제품 안전재고는 1이상 정수만 가능합니다. " + lowSafetyQuantity + rowIndex + " 셀을 확인해 주세요."
                );
        } else {
            if (!quantity.matches("^([1-9]\\d*)(\\.\\d+)?$"))
                throw new CustomRuntimeException(
                        "자재 안전재고는 1이상 실수만 가능합니다. " + lowSafetyQuantity + rowIndex + " 셀을 확인해 주세요."
                );
        }

        return quantity;
    }

    // 컬럼 : 단위
    private String getUnitKey(Cell cell, int rowIndex, String assetTypeFlag) {

        String unitName = CommonUtil.getCellValueAsString(cell);

        if (unitName == null || unitName.isEmpty())
            throw new CustomRuntimeException(
                    "단위는 비어 있을 수 없습니다. " + lowUnitKey + rowIndex + " 셀을 확인해 주세요."
            );
        if (commonSubMapper.getUnitCount(unitName) > 1)
            throw new CustomRuntimeException(
                    "공통 코드에서 단위명이 중복되어 있어 확인이 필요합니다. " + lowUnitKey + rowIndex + " 셀을 확인해 주세요."
            );
        String unitKey = commonSubMapper.getUnitKey(unitName);
        if (unitKey == null)
            throw new CustomRuntimeException(
                    "존재하지 않는 단위입니다. " + lowUnitKey + rowIndex + " 셀을 확인해 주세요."
            );

        return unitKey;
    }

    // 컬럼 : 단가
    private String getStandardPrice(Cell cell, int rowIndex, String assetTypeFlag) {

        String standardPrice = CommonUtil.getCellValueAsString(cell);

        if (standardPrice == null || standardPrice.isEmpty())
            throw new CustomRuntimeException(
                    "단가는 비어 있을 수 없습니다. " + lowStandardPrice + rowIndex + " 셀을 확인해 주세요."
            );

        return standardPrice;
    }
}
