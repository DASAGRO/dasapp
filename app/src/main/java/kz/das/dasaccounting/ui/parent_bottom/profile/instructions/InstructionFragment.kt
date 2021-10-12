package kz.das.dasaccounting.ui.parent_bottom.profile.instructions

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_instruction.*
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.extensions.zoomAnimation
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInstructionBinding
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileVM
import kz.das.dasaccounting.ui.parent_bottom.showBottomNavMenu
import org.koin.android.viewmodel.ext.android.viewModel

class InstructionFragment : BaseFragment<ProfileVM, FragmentInstructionBinding>() {

    private val instructionImage = listOf(
        R.drawable.img_instruction_1,
        R.drawable.img_instruction_2,
        R.drawable.img_instruction_3,
        R.drawable.img_instruction_4
    )

    companion object {
        fun getScreen() = DasAppScreen(InstructionFragment())
    }

    override val mViewModel: ProfileVM by viewModel()

    override fun getViewBinding() = FragmentInstructionBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewBinding.run {
            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }

            btnBefore.setOnClickListener {
                vpInstruction.currentItem = getItem(-1)
            }

            btnNext.setOnClickListener {
                val current = getItem(+1)
                if (current < instructionImage.size) {
                    vpInstruction.currentItem = current
                }
            }

            vpInstruction.adapter = InstructionsAdapter(instructionImage)
            vpInstruction.addOnPageChangeListener(vpInstructionsPageChangeListener)
        }
    }

    private var vpInstructionsPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        mViewBinding.btnBefore.zoomAnimation(400, false)
                        mViewBinding.btnNext.zoomAnimation(400, true)
                    }
                    instructionImage.size - 1 -> {
                        mViewBinding.btnBefore.zoomAnimation(400, true)
                        mViewBinding.btnNext.zoomAnimation(400, false)
                    }
                    else -> {
                        mViewBinding.btnBefore.zoomAnimation(400, true)
                        mViewBinding.btnNext.zoomAnimation(400, true)
                    }
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        }

    private fun getItem(i: Int): Int {
        return mViewBinding.vpInstruction.currentItem + i
    }

    override fun onDestroy() {
        showBottomNavMenu()
        super.onDestroy()
    }

}