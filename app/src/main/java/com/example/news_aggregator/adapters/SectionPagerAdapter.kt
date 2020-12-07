package com.example.news_aggregator.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.news_aggregator.R
import com.example.news_aggregator.fragments.ForYou
import com.example.news_aggregator.fragments.Local
import com.example.news_aggregator.fragments.Popular

/**
 * Fragment page adapter that returns a fragment corresponding to
 * one of the tabs.
 * @constructor
 */
class SectionsPagerAdapter(fm: AppCompatActivity) : FragmentStateAdapter(fm) {

    /**
     * Get the number of fragments.
     * @return Int
     */
    override fun getItemCount(): Int {
        return 3
    }

    /**
     * Creating the fragment depending on the position.
     * @param position Int
     * @return Fragment
     */
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment = ForYou.newInstance()
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