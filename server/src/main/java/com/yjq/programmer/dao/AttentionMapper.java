package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Attention;
import com.yjq.programmer.domain.AttentionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttentionMapper {
    int countByExample(AttentionExample example);

    int deleteByExample(AttentionExample example);

    int deleteByPrimaryKey(String id);

    int insert(Attention record);

    int insertSelective(Attention record);

    List<Attention> selectByExample(AttentionExample example);

    Attention selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Attention record, @Param("example") AttentionExample example);

    int updateByExample(@Param("record") Attention record, @Param("example") AttentionExample example);

    int updateByPrimaryKeySelective(Attention record);

    int updateByPrimaryKey(Attention record);
}
