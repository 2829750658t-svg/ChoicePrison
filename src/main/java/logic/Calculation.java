// logic/Calculation.java
package logic;

import java.sql.*;

public class Calculation {
    public ResultInfo calculate(Element element) {
        StringBuilder type = new StringBuilder();
        var scores = element.scoreMap;

        // 1. 计算类型字符串 (保持不变)
        type.append(scores.get('E') >= scores.get('I') ? "E" : "I");
        type.append(scores.get('S') >= scores.get('N') ? "S" : "N");
        type.append(scores.get('T') >= scores.get('F') ? "T" : "F");
        type.append(scores.get('J') >= scores.get('P') ? "J" : "P");

        ResultInfo info = new ResultInfo();
        info.type = type.toString();

        // 2. 计算比例 (核心修复：强制 double 运算并防止除以 0)
        // 计算每个维度中“第一个字母”所占的比例
        info.scoreE = calculateRatio(scores.get('E'), scores.get('I'));
        info.scoreS = calculateRatio(scores.get('S'), scores.get('N'));
        info.scoreT = calculateRatio(scores.get('T'), scores.get('F'));
        info.scoreP = calculateRatio(scores.get('J'), scores.get('P')); // 计算 J 的占比

        // 3. 从数据库查询详细内容
        // 注意：这里使用了 try-with-resources 自动关闭资源
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:my_questions.db")) {
            // 使用 UPPER 确保大小写不敏感匹配
            String sql = "SELECT * FROM mbti_results WHERE UPPER(type) = UPPER(?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, info.type);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        info.title = rs.getString("title");
                        info.intro = rs.getString("intro");
                        info.imgPath = rs.getString("img_path");

                        // 调试信息：如果控制台能看到这两行，说明数据库读取成功
                        System.out.println("数据库匹配成功: " + info.type + " - " + info.title);
                        System.out.println("图片路径: " + info.imgPath);
                    } else {
                        System.err.println("数据库中未找到类型: " + info.type);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("数据库查询出错: " + e.getMessage());
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 安全计算比例的方法
     * @param a 第一个维度的分数 (如 E)
     * @param b 第二个维度的分数 (如 I)
     * @return 比例 (0.0 - 1.0)
     */
    private double calculateRatio(double a, double b) {
        double total = a + b;
        if (total == 0) return 0.5; // 如果没有数据，默认 50%
        return a / total;           // 由于 a 和 total 已经是 Double，这里会自动进行浮点运算
    }
}