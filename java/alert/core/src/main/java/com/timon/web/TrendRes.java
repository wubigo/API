package com.timon.web;

import lombok.Data;

import java.util.List;

@Data
public class TrendRes {
    long hour;
    List<LevelWithCount> levelWithCounts;

    class LevelWithCount{

    }
}
