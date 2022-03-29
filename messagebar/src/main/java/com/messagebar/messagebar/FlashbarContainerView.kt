package com.messagebar.messagebar

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.HapticFeedbackConstants.VIRTUAL_KEY
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout
import com.messagebar.messagebar.Flashbar.Companion.DURATION_INDEFINITE
import com.messagebar.messagebar.Flashbar.DismissEvent
import com.messagebar.messagebar.Flashbar.DismissEvent.*
import com.messagebar.messagebar.Flashbar.Vibration.DISMISS
import com.messagebar.messagebar.SwipeDismissTouchListener.DismissCallbacks
import com.messagebar.messagebar.anim.FlashAnim
import com.messagebar.messagebar.anim.FlashAnimBarBuilder
import com.messagebar.messagebar.anim.FlashAnimIconBuilder
import com.messagebar.messagebar.util.NavigationBarPosition.*
import com.messagebar.messagebar.util.afterMeasured
import com.messagebar.messagebar.util.getNavigationBarPosition
import com.messagebar.messagebar.util.getNavigationBarSizeInPx
import com.messagebar.messagebar.util.getRootView

/**
 * Container withView matching the height and width of the parent to hold a FlashbarView.
 * It will occupy the entire screens size but will be completely transparent. The
 * FlashbarView inside is the only visible component in it.
 */
internal class FlashbarContainerView(context: Context)
    : RelativeLayout(context), DismissCallbacks {

    private val dismissRunnable = Runnable { dismissInternal(TIMEOUT) }

    internal lateinit var parentFlashbar: Flashbar

    private lateinit var flashbarView: FlashbarView

    private lateinit var enterAnimBuilder: FlashAnimBarBuilder
    private lateinit var exitAnimBuilder: FlashAnimBarBuilder
    private lateinit var vibrationTargets: List<Flashbar.Vibration>

    private var onBarShowListener: Flashbar.OnBarShowListener? = null
    private var onBarDismissListener: Flashbar.OnBarDismissListener? = null
    private var onTapOutsideListener: Flashbar.OnTapListener? = null
    private var overlayColor: Int? = null
    private var iconAnimBuilder: FlashAnimIconBuilder? = null

    private var duration = DURATION_INDEFINITE
    private var isBarShowing = false
    private var isBarShown = false
    private var isBarDismissing = false
    private var barDismissOnTapOutside: Boolean = false
    private var showOverlay: Boolean = false
    private var overlayBlockable: Boolean = false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                val rect = Rect()
                flashbarView.getHitRect(rect)

                // Checks if the tap was outside the bar
                if (!rect.contains(event.x.toInt(), event.y.toInt())) {
                    onTapOutsideListener?.onTap(parentFlashbar)

                    if (barDismissOnTapOutside) {
                        dismissInternal(TAP_OUTSIDE)
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onSwipe(isSwiping: Boolean) {
        isBarDismissing = isSwiping
        if (isSwiping) {
            onBarDismissListener?.onDismissing(parentFlashbar, true)
        }
    }

    override fun onDismiss(view: View) {
        removeCallbacks(dismissRunnable)

        (parent as? ViewGroup)?.removeView(this@FlashbarContainerView)
        isBarShown = false

        flashbarView.stopIconAnimation()

        if (vibrationTargets.contains(DISMISS)) {
            performHapticFeedback(VIRTUAL_KEY)
        }

        onBarDismissListener?.onDismissed(parentFlashbar, SWIPE)
    }

    internal fun attach(flashbarView: FlashbarView) {
        this.flashbarView = flashbarView
    }

    internal fun construct() {
        isHapticFeedbackEnabled = true

        if (showOverlay) {
            setBackgroundColor(overlayColor!!)

            if (overlayBlockable) {
                isClickable = true
                isFocusable = true
            }
        }

        addView(flashbarView)
    }

    internal fun addParent(flashbar: Flashbar) {
        this.parentFlashbar = flashbar
    }

    internal fun adjustOrientation(activity: Activity) {
        val flashbarContainerViewLp = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        var resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

        val navigationBarPosition = activity.getNavigationBarPosition()
        var navigationBarSize = 0

        if (resourceId > 0) {
            navigationBarSize = resources.getDimensionPixelSize(resourceId);
        }

        when (navigationBarPosition) {
            LEFT -> flashbarContainerViewLp.leftMargin = navigationBarSize
            RIGHT -> flashbarContainerViewLp.rightMargin = navigationBarSize
            BOTTOM -> flashbarContainerViewLp.bottomMargin = navigationBarSize
        }

        layoutParams = flashbarContainerViewLp

    }


    internal fun show(activity: Activity) {
        if (isBarShowing || isBarShown) return

        val activityRootView = activity.getRootView() ?: return

        // Only add the withView to the parent once
        if (this.parent == null) activityRootView.addView(this)

        activityRootView.afterMeasured {
            val enterAnim = enterAnimBuilder.withView(flashbarView).build()
            enterAnim.start(object : FlashAnim.InternalAnimListener {
                override fun onStart() {
                    isBarShowing = true
                    onBarShowListener?.onShowing(parentFlashbar)
                }

                override fun onUpdate(progress: Float) {
                    onBarShowListener?.onShowProgress(parentFlashbar, progress)
                }

                override fun onStop() {
                    isBarShowing = false
                    isBarShown = true

                    flashbarView.startIconAnimation(iconAnimBuilder)

                    if (vibrationTargets.contains(Flashbar.Vibration.SHOW)) {
                        performHapticFeedback(VIRTUAL_KEY)
                    }

                    onBarShowListener?.onShown(parentFlashbar)
                }
            })

            handleDismiss()
        }
    }

    internal fun dismiss() {
        dismissInternal(MANUAL)
    }

    internal fun isBarShowing() = isBarShowing

    internal fun isBarShown() = isBarShown

    internal fun setDuration(duration: Long) {
        this.duration = duration
    }

    internal fun setBarShowListener(listener: Flashbar.OnBarShowListener?) {
        this.onBarShowListener = listener
    }

    internal fun setBarDismissListener(listener: Flashbar.OnBarDismissListener?) {
        this.onBarDismissListener = listener
    }

    internal fun setBarDismissOnTapOutside(dismiss: Boolean) {
        this.barDismissOnTapOutside = dismiss
    }

    internal fun setOnTapOutsideListener(listener: Flashbar.OnTapListener?) {
        this.onTapOutsideListener = listener
    }

    internal fun setOverlay(overlay: Boolean) {
        this.showOverlay = overlay
    }

    internal fun setOverlayColor(color: Int) {
        this.overlayColor = color
    }

    internal fun setOverlayBlockable(blockable: Boolean) {
        this.overlayBlockable = blockable
    }

    internal fun setEnterAnim(builder: FlashAnimBarBuilder) {
        this.enterAnimBuilder = builder
    }

    internal fun setExitAnim(builder: FlashAnimBarBuilder) {
        this.exitAnimBuilder = builder
    }

    internal fun enableSwipeToDismiss(enable: Boolean) {
        this.flashbarView.enableSwipeToDismiss(enable, this)
    }

    internal fun setVibrationTargets(targets: List<Flashbar.Vibration>) {
        this.vibrationTargets = targets
    }

    internal fun setIconAnim(builder: FlashAnimIconBuilder?) {
        this.iconAnimBuilder = builder
    }

    private fun handleDismiss() {
        if (duration != DURATION_INDEFINITE) {
            postDelayed(dismissRunnable, duration)
        }
    }

    private fun dismissInternal(event: DismissEvent) {
        if (isBarDismissing || isBarShowing || !isBarShown) {
            return
        }

        removeCallbacks(dismissRunnable)

        val exitAnim = exitAnimBuilder.withView(flashbarView).build()
        exitAnim.start(object : FlashAnim.InternalAnimListener {
            override fun onStart() {
                isBarDismissing = true
                onBarDismissListener?.onDismissing(parentFlashbar, false)
            }

            override fun onUpdate(progress: Float) {
                onBarDismissListener?.onDismissProgress(parentFlashbar, progress)
            }

            override fun onStop() {
                isBarDismissing = false
                isBarShown = false

                if (vibrationTargets.contains(DISMISS)) {
                    performHapticFeedback(VIRTUAL_KEY)
                }

                onBarDismissListener?.onDismissed(parentFlashbar, event)

                post { (parent as? ViewGroup)?.removeView(this@FlashbarContainerView) }
            }
        })
    }
}
