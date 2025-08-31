/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.menu;

import com.formdev.flatlaf.util.Animator;
import java.util.HashMap;
import java.util.Map;

/**
 * Class quản lý hiệu ứng animation cho menu
 * @author PhamTra
 */
public final class MenuAnimation {
    private static final Map<MenuItem, Animator> animators = new HashMap<>();
    private static final int ANIMATION_DURATION = 400;
    private static final int ANIMATION_RESOLUTION = 1;

    private MenuAnimation() {
        // Private constructor để ngăn tạo instance
    }

    public static void animate(MenuItem menu, boolean show) {
        // Dừng animation đang chạy nếu có
        Animator currentAnimator = animators.get(menu);
        if (currentAnimator != null && currentAnimator.isRunning()) {
            currentAnimator.stop();
        }

        menu.setMenuShow(show);

        Animator animator = new Animator(ANIMATION_DURATION);
        animator.addTarget(new Animator.TimingTarget() {
            @Override
            public void timingEvent(float fraction) {
                menu.setAnimate(show ? fraction : 1f - fraction);
                menu.revalidate();
            }
        });

        animator.setResolution(ANIMATION_RESOLUTION);
        animator.setInterpolator(f -> (float) (1 - Math.pow(1 - f, 3)));
        animator.start();

        animators.put(menu, animator);
    }
}
