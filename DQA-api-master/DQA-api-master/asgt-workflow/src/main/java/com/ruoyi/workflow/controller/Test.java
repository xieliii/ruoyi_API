package com.ruoyi.workflow.controller;

import com.ruoyi.common.core.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Axel on 2023/5/31 19:29
 *
 * @author Axel
 */
@RestController
@RequestMapping("/workflow")
public class Test {
    @GetMapping("/test")
    public R<Object> test(){
        return R.ok("test");
    }
}
