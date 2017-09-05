package com.jyoung.honeystraw.model;

import java.util.List;

/**
 * Created by jyoung on 2017. 8. 17..
 */

public class ResultSearch {
    public ResultData result;

    public class ResultData{
        public String message;
        public List<Cover> coverList;
    }
}
