package kz.das.dasaccounting.core.ui.extensions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile_history.view.*
import kz.das.dasaccounting.R


fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

@BindingAdapter("app:setVisibility")
fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun TextView.setHtmlText(htmlText: String) {
    text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

@SuppressWarnings("deprecation")
fun returnSpanned(html: String?): Spanned {
    return when {
        html == null -> {
            SpannableString("")
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        }
        else -> {
            Html.fromHtml(html)
        }
    }
}


@BindingAdapter("app:phoneNumberText")
fun TextView.phoneText(phoneNumberText: String?) {
    this.text = phoneNumberText?.toUIFormattedPhone()
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

fun Context.getDimension(value: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, this.resources.displayMetrics).toInt()
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


// Image View extensions
@BindingAdapter("app:avatar")
fun ImageView.setAvatar(url: String?) {
    Picasso.get()
            .load("https://app.dasagro.kz$url")
            .error(R.drawable.avatar_placeholder)
            .placeholder(R.drawable.avatar_placeholder)
            .into(this)
}

@BindingAdapter("app:productImageUrl")
fun ImageView.setImage(url: String?) {
    Picasso.get()
            .load("https://app.dasagro.kz$url")
            .error(R.drawable.image_placeholder)
            .placeholder(R.drawable.image_placeholder)
            .into(this)
}

@BindingAdapter("app:setUriImage")
fun ImageView.setUriImage(uri: Uri?) {
    this.setImageURI(uri)
}

fun String.generateQR(): Bitmap? {
    val str = this

    val result: BitMatrix = try {
        MultiFormatWriter().encode(
            str,
            BarcodeFormat.QR_CODE, 400, 400, null
        )
    } catch (iae: IllegalArgumentException) {
        // Unsupported format
        return null
    }
    val barcodeEncoder = BarcodeEncoder()
    return barcodeEncoder.createBitmap(result)
}


