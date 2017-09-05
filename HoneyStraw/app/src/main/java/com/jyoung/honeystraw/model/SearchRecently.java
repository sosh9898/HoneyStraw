package com.jyoung.honeystraw.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 8. 18..
 */

public class SearchRecently {
    int num;
    @SerializedName("search_type")
    int searchType;
    @SerializedName("search_result")
    String searchResult;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public SearchRecently(int searchType) {
        this.searchType = searchType;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }
}
