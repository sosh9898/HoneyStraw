package com.jyoung.honeystraw.model;

import java.util.List;

/**
 * Created by jyoung on 2017. 8. 15..
 */

public class UserPage {
    public ResultData result;

    public class ResultData{
        public User userInfo;
        public List<Cover> tipList;
    }
}
