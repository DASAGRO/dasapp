package kz.das.dasaccounting.core.ui.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter


fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun TextView.setHtmlText(htmlText: String) {
    text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

@BindingAdapter("app:animScaleXRatio", "app:animScaleYRatio", "app:pulseAnimDuration")
fun View.animateInfinitePulse(
    scaleXRatio: Float = 0.6f,
    scaleYRatio: Float = 0.6f,
    pulseAnimDuration: Int
) {
    if (this.visibility == View.VISIBLE) {
        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this, PropertyValuesHolder.ofFloat("scaleX", scaleXRatio),
            PropertyValuesHolder.ofFloat("scaleY", scaleYRatio)
        )
        scaleDown.duration = 500
        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.start()
    }
}

@BindingAdapter("app:animZoomDuration", "app:animZoomVisible")
fun View.zoomAnimation(duration: Long, visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
        this.alpha = 0.0f
        this.scaleX = 0.0f
        this.scaleY = 0.0f
        this.animate()
            .alpha(1.0f)
            .scaleX(1.0f).scaleY(1.0f)
            .setDuration(duration)
            .start()
    } else {
        this.visibility = View.GONE
        this.alpha = 1.0f
        this.scaleX = 1.0f
        this.scaleY = 1.0f
        this.animate()
            .alpha(0.0f)
            .scaleX(0.0f).scaleY(0.0f)
            .setDuration(duration)
            .start()
    }
}


fun View.expand() {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (this.parent as View).width,
        View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        0,
        View.MeasureSpec.UNSPECIFIED
    )
    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            this@expand.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            this@expand.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = 300
    this.startAnimation(a)
}

fun View.collapse() {
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
                this@collapse.visibility = View.GONE
            } else {
                this@collapse.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                this@collapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = 300
    this.startAnimation(a)
}


fun View.expand(duration: Long, onViewCompletion: OnViewCompletionListener?) {
    val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        (this.parent as View).width,
        View.MeasureSpec.EXACTLY
    )
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        0,
        View.MeasureSpec.UNSPECIFIED
    )
    this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = this.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    this.visibility = View.VISIBLE
    onViewCompletion?.onComplete()

    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            this@expand.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            this@expand.requestLayout()
            onViewCompletion?.onCompletion()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = duration
    this.startAnimation(a)
}


fun View.collapse(duration: Long, onViewCompletion: OnViewCompletionListener?) {
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
                this@collapse.visibility = View.GONE
                onViewCompletion?.onComplete()
            } else {
                onViewCompletion?.onCompletion()
                this@collapse.layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                this@collapse.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = duration
    this.startAnimation(a)
}

interface OnViewCompletionListener {
    fun onComplete()
    fun onCompletion()
}