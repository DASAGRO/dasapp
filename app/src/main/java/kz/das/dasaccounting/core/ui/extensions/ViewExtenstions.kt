package kz.das.dasaccounting.core.ui.extensions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.google.zxing.WriterException
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

@BindingAdapter("app:isGone")
fun View.setViewGone(isGone: Boolean) {
    this.visibility = if (isGone) View.GONE else View.VISIBLE
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


fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    this.layoutParams = menuLayoutParams
}

fun Activity.setBackgroundColor(colorId: Int) {
    this.window.decorView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, colorId))
}

fun Activity.setBackground(drawableId: Int) {
    this.window.decorView.background = ContextCompat.getDrawable(this, drawableId)
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

fun View.animateRepeatPulse(
    scaleXRatio: Float = 0.6f,
    scaleYRatio: Float = 0.6f,
    pulseAnimDuration: Long = 400,
    repeatCount: Int = 5
) {
    if (this.visibility == View.VISIBLE) {
        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this, PropertyValuesHolder.ofFloat("scaleX", scaleXRatio),
            PropertyValuesHolder.ofFloat("scaleY", scaleYRatio)
        )
        scaleDown.duration = pulseAnimDuration
        scaleDown.repeatCount = repeatCount
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.start()
    }
}

fun View.rotateAnimation(
    pulseAnimDuration: Long = 3000,
    repeatCount: Int = 6) {
    this@rotateAnimation.animate().rotationBy(360f).setDuration(pulseAnimDuration)
        .setInterpolator(LinearInterpolator())
        .start()
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
    var bitmap: Bitmap? = null

    val qrgEncoder = QRGEncoder(this, null, QRGContents.Type.TEXT, 300)
    qrgEncoder.colorBlack = Color.BLACK
    qrgEncoder.colorWhite = Color.WHITE
    try {
        // Getting QR-Code as Bitmap
        bitmap = qrgEncoder.bitmap
        // Setting Bitmap to ImageView
    } catch (e: WriterException) {
        e.printStackTrace()
    }

    return bitmap

//    val result: BitMatrix = try {
//        MultiFormatWriter().encode(
//            str,
//            BarcodeFormat.QR_CODE, 400, 400, null
//        )
//    } catch (iae: IllegalArgumentException) {
//        // Unsupported format
//        return null
//    }
//    val barcodeEncoder = BarcodeEncoder()
//    return barcodeEncoder.createBitmap(result)
}


