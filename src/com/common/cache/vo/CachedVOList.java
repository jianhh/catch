package com.common.cache.vo;

import java.util.ArrayList;

import com.common.cache.CachedVO;


public class CachedVOList implements CachedVO
{
    
    private ArrayList voList;

    
    public ArrayList getVoList()
    {
    
        return voList;
    }

    
    public void setVoList(ArrayList voList)
    {
    
        this.voList = voList;
    }
    
}
