package utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimationUtils {

    // 顶部进度条平移动画
    public static void animateProgressBar(Pane bar, double targetWidth) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400),
                        new KeyValue(bar.prefWidthProperty(), targetWidth))
        );
        timeline.play();
    }

    // 按钮组阶梯式滑入
    public static void fadeInButtons(Node... nodes) {
        Timeline timeline = new Timeline();
        double delay = 0;

        for (Node node : nodes) {
            // 初始状态：向右偏移且透明
            node.setTranslateX(50);
            node.setOpacity(0);

            // 每一个按钮比上一个晚 100ms 启动，形成阶梯感
            KeyFrame kf = new KeyFrame(Duration.millis(300 + delay),
                    new KeyValue(node.translateXProperty(), 0),
                    new KeyValue(node.opacityProperty(), 1)
            );
            timeline.getKeyFrames().add(kf);
            delay += 100;
        }
        timeline.play();
    }
}