package com.wimir.bae.domain.planTotal.controller;

import com.wimir.bae.domain.planTotal.service.PlanTotalService;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("plan/result")
public class PlanTotalController {

    private final JwtGlobalService jwtGlobalService;
    private final PlanTotalService planTotalService;
}
