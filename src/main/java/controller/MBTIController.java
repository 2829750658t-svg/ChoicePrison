package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.AnimationUtils;
import logic.*;

public class MBTIController {

    // --- 答题页组件 ---
    @FXML private VBox mainContainer; // 对应 FXML 中最外层的那个大 VBox ID
    @FXML private VBox questionBox;
    @FXML private VBox optionBox;
    @FXML private Label questionLabel;
    @FXML private Button btn1, btn2, btn3, btnBack;
    @FXML private Pane progressBar;

    // --- 逻辑变量 ---
    private Element element = new Element();
    private Calculation calc = new Calculation();
    private List<Question> all_questions = new ArrayList<>();
    private int current_index = 0;
    private List<String> choiceHistory = new ArrayList<>();

    public void initialize() {
        // 数据库加载部分保持不变...
        loadDatabase();
    }

    private void loadDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:my_questions.db");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mbti_questions");
            while (rs.next()) {
                all_questions.add(new Question(
                        rs.getInt("id"), rs.getString("content"),
                        rs.getString("opt1"), rs.getString("opt2"), rs.getString("opt_mid"),
                        rs.getString("dims1"), rs.getString("dims2"), rs.getString("dim_mid")
                ));
            }
            rs.close(); stmt.close(); conn.close();
            if (!all_questions.isEmpty()) showQuestion();
        } catch (Exception e) {
            e.printStackTrace();
            questionLabel.setText("数据库加载失败！");
        }
    }

    @FXML public void handleOptionA() { nextStep(all_questions.get(current_index).dims1); }
    @FXML public void handleOptionB() { nextStep(all_questions.get(current_index).dims2); }
    @FXML public void handleOptionC() { nextStep(all_questions.get(current_index).dim_mid); }
    @FXML public void handleOption1() { lastStep(); }

    private void nextStep(String dim) {
        choiceHistory.add(dim);
        element.addScore(dim);

        if (current_index < all_questions.size() - 1) {
            current_index++;
            showQuestion();
        } else {
            // 答题结束，计算结果
            ResultInfo result = calc.calculate(element);
            if (result != null) {
                showResultPage(result); // 调用动态加载方法
            }
            progressBar.setPrefWidth(640.0);
        }
    }

    /**
     * 【核心改进：动态加载结果页】
     */
    private void showResultPage(ResultInfo result) {
        try {
            // 1. 加载独立的结果页 FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/result_view.fxml"));
            VBox resultNode = loader.load();

            // 2. 通过 ID 在 resultNode 里找到组件并赋值
            // lookup 的 ID 必须和 result_view.fxml 里的 fx:id 一致，且前面加 #
            ImageView resImg = (ImageView) resultNode.lookup("#resultImageView");
            Label resTitle = (Label) resultNode.lookup("#resultTitleLabel");
            Label resRatio = (Label) resultNode.lookup("#ratioLabel");
            Label resIntro = (Label) resultNode.lookup("#introLabel");

            // 3. 填充数据
            if (resTitle != null) resTitle.setText("你是：" + result.type + " " + result.title);
            if (resIntro != null) resIntro.setText(result.intro);
            if (resRatio != null) {
                resRatio.setText(String.format("E-I: %.0f%% | S-N: %.0f%% | T-F: %.0f%% | J-P: %.0f%%",
                        result.scoreE * 100, result.scoreS * 100, result.scoreT * 100, result.scoreP * 100));
            }

            // 4. 加载图片 (PNG/JPG)
            if (resImg != null && result.imgPath != null) {
                String path = result.imgPath.startsWith("/") ? result.imgPath : "/" + result.imgPath;
                var stream = getClass().getResourceAsStream(path);
                if (stream != null) {
                    resImg.setImage(new Image(stream));
                }
            }

            // 5. 清空主容器，放入结果页
            mainContainer.getChildren().clear();
            mainContainer.getChildren().add(resultNode);

            // 淡入动画
            resultNode.setOpacity(0);
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(800), resultNode);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("无法加载结果页 FXML，请检查路径！");
        }
    }

    public void lastStep() {
        if (current_index > 0 && !choiceHistory.isEmpty()) {
            String lastDim = choiceHistory.remove(choiceHistory.size() - 1);
            element.deductScore(lastDim);
            current_index--;
            showQuestion();
        }
    }

    private void showQuestion() {
        if (all_questions.isEmpty()) return;
        Question q = all_questions.get(current_index);
        questionLabel.setText(q.id + ". " + q.content);
        btn1.setText(q.opt1);
        btn2.setText(q.opt2);
        btn3.setText(q.opt_mid);

        double targetWidth = ((double) (current_index + 1) / all_questions.size()) * 650.0;
        AnimationUtils.animateProgressBar(progressBar, targetWidth);

        // 滑入效果
        AnimationUtils.fadeInButtons(btn1, btn2, btn3);
    }
}