package com.wimir.bae.domain.plan.controller;

import com.wimir.bae.domain.plan.service.PlanService;
import com.wimir.bae.global.jwt.JwtGlobalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("plan")
public class PlanController {

    private final JwtGlobalService jwtGlobalService;
    private final PlanService planService;


}
