package logic;

public class ResultInfo {
    public String type;    // 如 "INFP"
    public String title;   // 如 "调停者"
    public String intro;   // 详细介绍
    public String imgPath; // 图片路径

    // 新增：四个维度的百分比 (0.0 到 1.0)
    public double scoreE, scoreS, scoreT, scoreP;
}
