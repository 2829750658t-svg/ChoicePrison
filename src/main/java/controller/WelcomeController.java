package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.AnimationUtils;
import java.io.IOException;

public class WelcomeController {

    @FXML private Button startBtn;

    public void initialize() {
        // 页面加载时给按钮一个滑入动画
        AnimationUtils.fadeInButtons(startBtn);
    }

    @FXML
    private void handleStart() {
        try {
            // 1. 加载答题页 FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mbti_view.fxml"));
            Parent root = loader.load();

            // 2. 获取当前的窗口 (Stage)
            Stage stage = (Stage) startBtn.getScene().getWindow();

            // 3. 替换当前场景的内容
            stage.getScene().setRoot(root);

            // 4. 淡入效果
            root.setOpacity(0);
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(500), root);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("跳转失败，请检查 /view/mbti_view.fxml 路径是否正确");
        }
    }
}