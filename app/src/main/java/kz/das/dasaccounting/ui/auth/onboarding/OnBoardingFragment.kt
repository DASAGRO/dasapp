package kz.das.dasaccounting.ui.auth.onboarding

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.returnSpanned
import kz.das.dasaccounting.core.ui.extensions.zoomAnimation
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentOnboardingBinding
import kz.das.dasaccounting.ui.Screens
import org.koin.android.viewmodel.ext.android.viewModel

class OnBoardingFragment: BaseFragment<OnBoardingVM, FragmentOnboardingBinding>() {

    private var layouts: IntArray = IntArray(5)
    private var dots: Array<TextView>? = null

    companion object {
        fun getScreen() = DasAppScreen(OnBoardingFragment())
    }

    override val mViewModel: OnBoardingVM by viewModel()

    override fun getViewBinding() = FragmentOnboardingBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        layouts = intArrayOf(
            R.layout.board_screen_one,
            R.layout.board_screen_two,
            R.layout.board_screen_three,
            R.layout.board_screen_four,
            R.layout.board_screen_five
        )

        mViewBinding.apply {

            addBottomDots(0)

            vpOnBoarding.adapter = MyViewPagerAdapter()
            vpOnBoarding.addOnPageChangeListener(vpOnBoardingPageChangeListener)

            tvSkip.setOnClickListener {
                vpOnBoarding.currentItem = layouts.size - 1
            }

            btnNext.setOnClickListener {
                val current = getItem(+1)
                if (current < layouts.size) {
                    vpOnBoarding.currentItem = current
                }
            }

            btnStart.setOnClickListener {
                mViewModel.getUserRole()?.let { userRole ->
                    Screens.getRoleScreens(userRole)?.let { screen -> requireRouter().newRootScreen(screen) }
                }
                //requireRouter().newRootChain(OfficeBottomNavigationFragment.getScreen())
            }
        }

    }


    private fun addBottomDots(currentPage: Int) {
        dots = Array(layouts.size) { TextView(requireContext()) }
        val colorsActive = ContextCompat.getColor(requireContext(), R.color.purple_700)
        val colorsInactive = ContextCompat.getColor(requireContext(), R.color.color_grey_text)
        mViewBinding.dotsLayout.removeAllViews()

        for (i in dots!!.indices) {
            dots!![i] = TextView(requireContext())
            dots!![i].text = returnSpanned("&#8226;")
            dots!![i].textSize = 40f
            //dots!![i].layoutParams = dotParam
            dots!![i].setTextColor(colorsInactive)
            mViewBinding.dotsLayout.addView(dots!![i])
        }
        if (dots!!.isNotEmpty()) dots!![currentPage].setTextColor(colorsActive)
    }


    private fun getItem(i: Int): Int {
        return mViewBinding.vpOnBoarding.currentItem + i
    }


    private var vpOnBoardingPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                if (position == layouts.size - 1) {
                    mViewBinding.btnStart.zoomAnimation(400, true)
                    mViewBinding.btnNext.zoomAnimation(400, false)
                    mViewBinding.tvSkip.zoomAnimation(400, false)
                } else {
                    mViewBinding.btnStart.zoomAnimation(400, false)
                    mViewBinding.btnNext.zoomAnimation(400, true)
                    mViewBinding.tvSkip.zoomAnimation(400, true)
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        }


    /**
     * View pager sliding adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = this@OnBoardingFragment.layoutInflater.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View?
            container.removeView(view)
        }
    }

}