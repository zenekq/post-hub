package com.posthub.iam.controller;

import com.posthub.iam.utils.StackInfo;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/iamServiceInner")
@RequiredArgsConstructor
public class InnerController {

    @Hidden
    @GetMapping("/healthCheck")
    public ResponseEntity<Void> healthCheck() {

        log.trace(StackInfo.getCurrentMethodLogTemplate(), StackInfo.getMethodName());

        return ResponseEntity.ok().build();
    }

}
