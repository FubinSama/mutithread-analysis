package com.wfb.mutithreadanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean {
    private String code;
    private String msg;

    public static ResponseBean error(String msg) {
        return new ResponseBean("error", msg);
    }

    public static ResponseBean success() {
        return new ResponseBean("success", "");
    }
}
