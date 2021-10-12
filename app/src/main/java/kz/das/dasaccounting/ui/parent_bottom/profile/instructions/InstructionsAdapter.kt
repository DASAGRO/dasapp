package kz.das.dasaccounting.ui.parent_bottom.profile.instructions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kz.das.dasaccounting.R

class InstructionsAdapter(var images: List<Int>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount() = images.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context)
            .inflate(R.layout.item_vp_instruction, container, false)

        val mainImageView = itemView.findViewById(R.id.ivMain) as ImageView
        mainImageView.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                images[position]
            )
        )

        val viewPager = container as? ViewPager
        viewPager?.addView(itemView)
        return itemView
    }
}