package com.example.news_aggregator.models

class DataSource {
    companion object {

        fun createDataSet(): ArrayList<ArrayList<DummyData>> {
            var forYouList = ArrayList<DummyData>()
            var trendingList = ArrayList<DummyData>()

            val list = ArrayList<ArrayList<DummyData>>()
            forYouList.add(
                    DummyData(
                        "UK Trade Talks",
                        "https://images.pexels.com/photos/275496/pexels-photo-275496.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                        "Sam",
                        "UK welcomes EU promise to 'intensify' trade talks",
                        "BBC"
                    )
                    )
            forYouList.add(
                DummyData(
                    "Covid Update",
                    "https://images.pexels.com/photos/3970332/pexels-photo-3970332.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "James",
                    "New areas currently in lockdown",
                    "BBC"
                )
            )

            forYouList.add(
                DummyData(
                    "Premier League",
                    "https://images.pexels.com/photos/159400/television-camera-men-outdoors-ballgame-159400.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "John",
                    "The Premier League will still go on",
                    "BBC"
                )
            )
            forYouList.add(
                DummyData(
                    "Covid 19 stats",
                    "https://images.pexels.com/photos/3970333/pexels-photo-3970333.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Steven",
                    "New numbers for cases",
                    "BBC"
                )
            )
            forYouList.add(
                DummyData(
                    "No Exams",
                    "https://images.pexels.com/photos/4560150/pexels-photo-4560150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Luke",
                    "A-Level students will no longer have any exams",
                    "BBC"
                )
            )
            forYouList.add(
                DummyData(
                    "iPhone 12",
                    "https://images.pexels.com/photos/3856050/pexels-photo-3856050.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Guy",
                    "Details on new iPhone",
                    "BBC"
                )
            )
            forYouList.add(
                DummyData(
                    "Californian fires",
                    "https://images.pexels.com/photos/4127694/pexels-photo-4127694.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Laurence",
                    "Fires in California continue to burn",
                    "BBC"
                )
            )
            forYouList.add(
                DummyData(
                    "NHS Track and Trace app",
                    "https://images.pexels.com/photos/4524369/pexels-photo-4524369.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Leon",
                    "Reviewers says the new app is useless",
                    "BBC"
                )
            )

            trendingList.add(
                DummyData(
                    "UK Trade Talks",
                    "https://images.pexels.com/photos/275496/pexels-photo-275496.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "Sam",
                    "UK welcomes EU promise to 'intensify' trade talks",
                    "BBC"
                )
            )
            trendingList.add(
                DummyData(
                    "Covid Update",
                    "https://images.pexels.com/photos/3970332/pexels-photo-3970332.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "James",
                    "New areas currently in lockdown",
                    "BBC"
                )
            )

            trendingList.add(
                DummyData(
                    "Premier League",
                    "https://images.pexels.com/photos/159400/television-camera-men-outdoors-ballgame-159400.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "John",
                    "The Premier League will still go on",
                    "BBC"
                )
            )
            trendingList.add(
                DummyData(
                    "Covid 19 stats",
                    "https://images.pexels.com/photos/3970333/pexels-photo-3970333.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Steven",
                    "New numbers for cases",
                    "BBC"
                )
            )
            trendingList.add(
                DummyData(
                    "No Exams",
                    "https://images.pexels.com/photos/4560150/pexels-photo-4560150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Luke",
                    "A-Level students will no longer have any exams",
                    "BBC"
                )
            )
            trendingList.add(
                DummyData(
                    "iPhone 12",
                    "https://images.pexels.com/photos/3856050/pexels-photo-3856050.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Guy",
                    "Details on new iPhone",
                    "BBC"
                )
            )
            trendingList.add(
                DummyData(
                    "Californian fires",
                    "https://images.pexels.com/photos/4127694/pexels-photo-4127694.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Laurence",
                    "Fires in California continue to burn",
                    "BBC"
                )
            )
            trendingList.add(
                DummyData(
                    "NHS Track and Trace app",
                    "https://images.pexels.com/photos/4524369/pexels-photo-4524369.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Leon",
                    "Reviewers says the new app is useless",
                    "BBC"
                )
            )

            list.add(forYouList)
            list.add(trendingList)
            return list
        }

    }
}