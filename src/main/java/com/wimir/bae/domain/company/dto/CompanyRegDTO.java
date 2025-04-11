package com.wimir.bae.domain.company.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRegDTO {

    @NotBlank
    @Pattern(regexp = "^[OS]$")
    private String companyTypeFlag;

    @NotBlank
    @Size(max = 50)
    private String companyName;

    @Size(max = 50)
    private String companyRegNumber;

    @Size(max = 50)
    private String ceoName;

    @Size(max = 100)
    private String address;

    @Size(max = 200)
    private String note;

}
