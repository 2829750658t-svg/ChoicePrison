import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// JavaFX 程序入口类必须继承 Application
public class MainApp extends Application {

    // JavaFX 启动时自动调用，stage 是主窗口
    @Override
    public void start(Stage stage) throws Exception {
        // 【核心修改】将路径指向你的欢迎页面
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/view/welcome_view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("TYQ-MBTI专业测试 (Vision: 2.0)");
        stage.setScene(scene);

        // 窗口大小固定
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        // 启动 JavaFX 应用：
        // 1. 初始化
        // 2. 创建 Stage
        // 3. 调用 start(stage)
        launch(args);
    }
}