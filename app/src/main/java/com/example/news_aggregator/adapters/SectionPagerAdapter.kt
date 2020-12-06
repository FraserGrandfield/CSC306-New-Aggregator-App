package com.example.news_aggregator.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
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
class SectionsPagerAdapter(fm: AppCompatActivity) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return TAB_TITLES.size
    }

    override fun createFragment(position: Int): Fragment {
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
}