package com.example.news_aggregator.adapters

import android.content.Context
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.news_aggregator.R
import com.example.news_aggregator.fragments.ForYou
import com.example.news_aggregator.fragments.Local
import com.example.news_aggregator.fragments.Popular

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment = ForYou.newInstance()
        when (position) {
            0 -> {
                fragment = ForYou.newInstance()
            }
            1 -> {
                fragment = Popular.newInstance()
            }
            2 -> {
                fragment = Local.newInstance()
            }
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}