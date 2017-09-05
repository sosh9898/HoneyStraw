package com.jyoung.honeystraw.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jyoung on 2017. 8. 16..
 */

public class CommentResult {
    public ResultData result;

    public class ResultData{
        public List<CommentList> commentList;
    }
}
