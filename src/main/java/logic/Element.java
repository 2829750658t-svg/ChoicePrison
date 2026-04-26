package logic;

import java.util.HashMap;
import java.util.Map;

// 计分器：记录各维度得分
public class Element {

    public Map<Character, Double> scoreMap = new HashMap<>();

    // 构造方法：初始化维度为 0
    public Element() {
        char[] dims = "EISNTFJP".toCharArray();
        for (char c : dims) {
            scoreMap.put(c, 0.0);
        }
    }

    // 加分
    public void addScore(String singleDim) {

        // 防止空值
        if (singleDim == null || singleDim.isEmpty()) return;

        // string转char
        char key = singleDim.charAt(0);

        // 获取当前分数
        double currentScore = scoreMap.getOrDefault(key, 0.0);

        // 更新分数
        scoreMap.put(key, currentScore + 1);
    }

    // 减分
    public void deductScore(String singleDim){
        // 防止空值
        if (singleDim == null || singleDim.isEmpty()) return;

        // string转char
        char key = singleDim.charAt(0);

        // 获取当前分数
        double currentScore = scoreMap.getOrDefault(key, 0.0);

        // 更新分数
        scoreMap.put(key, currentScore - 1);

    }
}