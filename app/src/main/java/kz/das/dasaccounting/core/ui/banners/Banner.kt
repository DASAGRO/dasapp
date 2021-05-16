package kz.das.dasaccounting.core.ui.banners

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kz.das.dasaccounting.R
import kz.das.dasaccounting.databinding.LayoutBannerBinding
import java.lang.ref.WeakReference
import kotlin.math.abs

private const val DEFAULT_OFFSET_TO_DISMISS_PX = 300
private const val DEFAULT_SCREEN_RESOLUTION_PX = 1080
private const val MAX_ALPHA = 255

class Banner(
    private val builder: Builder
) : LifecycleObserver {

    //region Vars
    private var popupWindow: PopupWindow? = null
    private var destroyed: Boolean = false
    private var isShowing = false

    private var weakReference: WeakReference<View>? = null

    private var offsetToDismissPx = DEFAULT_OFFSET_TO_DISMISS_PX
    private var screenWidthPx = DEFAULT_SCREEN_RESOLUTION_PX

    @ColorInt
    private var backgroundColorInt: Int = 0
    //endregion

    sealed class Duration(val ms: Long) {
        object SHORT : Duration(2000L)
        object LONG : Duration(3500L)
        object INDEFINITE : Duration(0L)
        class CUSTOM(@IntRange(from = 3500L) val durationMs: Long) : Duration(durationMs)
    }

    enum class Type {
        SUCCESS,
        AWAIT,
        ERROR
    }

    fun show(activity: Activity) {
        val rootView = activity.findViewById<View>(android.R.id.content)
        val width = rootView?.width
        if (width != null) {
            offsetToDismissPx = (width / 4)
            screenWidthPx = width
        }
        show(rootView)
    }

    fun show(view: View) {
        if (!this.destroyed && !this.isShowing) {
            isShowing = true
            val bannerBinding = LayoutBannerBinding.inflate(LayoutInflater.from(view.context))
            setupTypeBackground(bannerBinding)
            setupTypeIcon(bannerBinding)
            setupContent(bannerBinding)

            popupWindow = PopupWindow(
                bannerBinding.root,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                false
            )
            popupWindow?.animationStyle = R.style.bannerTopAnimation
            builder.lifecycleOwner.lifecycle.addObserver(this)

            if (builder.autoDismissDuration != Duration.INDEFINITE) {
                dismissWithDelay(builder.autoDismissDuration.ms)
            }

            view.post {
                popupWindow?.showAtLocation(view, Gravity.TOP, 0, 0)
            }
            setupSwipeToDismiss(bannerBinding.root)
        }
    }

    //region Support
    private fun setupTypeBackground(binding: LayoutBannerBinding) {
        with(binding) {
            val backgroundColor = when (builder.type) {
                Type.SUCCESS -> R.color.purple_700
                Type.AWAIT -> R.color.purple_700
                Type.ERROR -> R.color.purple_700
            }
            backgroundColorInt = ContextCompat.getColor(this.root.context, backgroundColor)
            clSimpleBannerRoot.setBackgroundColor(backgroundColorInt)
        }
    }

    private fun setupTypeIcon(binding: LayoutBannerBinding) {
        with(binding) {
            val iconResId = when (builder.type) {
                Type.SUCCESS -> R.drawable.ic_banner_success
                Type.AWAIT -> R.drawable.ic_banner_await
                Type.ERROR -> R.drawable.ic_banner_error
            }
            ivBannerType.setImageResource(iconResId)
        }
    }

    private fun setupContent(binding: LayoutBannerBinding) {
        with(binding) {
            tvBannerDescription.text = builder.text
            tvBannerTitle.text = builder.getTitle(binding.root.context)
        }
    }

    private fun setupSwipeToDismiss(bannerContentView: View) {
        //COLORS OF BANNER BACKGROUND
        val colorRedOfBackground = Color.red(backgroundColorInt)
        val colorGreenOfBackground = Color.green(backgroundColorInt)
        val colorBlueOfBackground = Color.blue(backgroundColorInt)

        weakReference = WeakReference(bannerContentView)
        bannerContentView.setOnTouchListener(object : View.OnTouchListener {
            private var lastAction = 0
            private var initialX = weakReference?.get()?.x ?: 0f
            private var initialTouchX = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {

                        initialTouchX = event.rawX

                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (lastAction == MotionEvent.ACTION_MOVE) {
                            val diffX = event.rawX - initialTouchX
                            when {
                                diffX > offsetToDismissPx -> {
                                    dismiss()
                                }
                                diffX < -offsetToDismissPx -> {
                                    dismiss()
                                }
                                else -> {
                                    weakReference?.get()?.x = initialX
                                    weakReference?.get()
                                        ?.findViewById<ConstraintLayout>(R.id.clSimpleBannerRoot)
                                        ?.setBackgroundColor(backgroundColorInt)
                                }
                            }
                        }

                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val diffX = event.rawX - initialTouchX
                        weakReference?.get()?.x = initialX + diffX
                        lastAction = event.action

                        val percentRatio = abs(diffX / screenWidthPx)
                        val alpha = (MAX_ALPHA * percentRatio).toInt()
                        weakReference?.get()
                            ?.findViewById<ConstraintLayout>(R.id.clSimpleBannerRoot)
                            ?.setBackgroundColor(
                                Color.argb(MAX_ALPHA - alpha, colorRedOfBackground, colorGreenOfBackground, colorBlueOfBackground)
                            )

                        return true
                    }

                }
                return false
            }
        })
    }

    private fun dismissWithDelay(delayMs: Long) {
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
        }, delayMs)
    }

    private fun dismiss() {
        if (this.isShowing) {
            this.isShowing = false
            popupWindow?.dismiss()
            weakReference?.clear()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        destroyed = true
        dismiss()
    }
    //endregion

    class Builder(
        var lifecycleOwner: LifecycleOwner
    ) {
        private var title: CharSequence? = ""
        var text: CharSequence? = ""
            get() = if (field == null) "" else field
        var autoDismissDuration: Duration = Duration.LONG
        private var dismissWhenClicked: Boolean = false
        var type: Type = Type.SUCCESS

        fun setTitle(title: CharSequence?): Builder = apply {
            this@Builder.title = title
        }

        fun setText(text: CharSequence?): Builder = apply {
            this@Builder.text = text
        }

        fun setAutoDismissDuration(duration: Duration): Builder = apply {
            this@Builder.autoDismissDuration = duration
        }

        fun setDismissWhenClicked(dismissWhenClicked: Boolean): Builder = apply {
            this@Builder.dismissWhenClicked = dismissWhenClicked
        }

        fun setType(type: Type): Builder = apply {
            this@Builder.type = type
        }

        fun getTitle(context: Context): CharSequence {
            title?.let {
                return it
            }
            return when (type) {
                Type.SUCCESS -> context.getString(R.string.common_banner_success)
                Type.ERROR -> context.getString(R.string.common_banner_error)
                Type.AWAIT -> context.getString(R.string.common_banner_await)
            }
        }

        fun build(): Banner = Banner(this@Builder)
    }
}