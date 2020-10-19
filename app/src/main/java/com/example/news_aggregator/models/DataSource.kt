package com.example.news_aggregator.models

import com.example.news_aggregator.models.DummyData

class DataSource {
    companion object {

        fun createDataSet(): ArrayList<DummyData> {
            val list = ArrayList<DummyData>()
            list.add(
                DummyData(
                    "UK Trade Talks",
                    "You made it to the end of the course!\r\n\r\nNext we'll be building the REST API!",
                    "https://images.pexels.com/photos/275496/pexels-photo-275496.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                    "Sally",
                    "UK welcomes EU promise to 'intensify' trade talks",
                    "BBC"
                )
            )
            list.add(
                DummyData(
                    "Covid Update",
                    "The REST API course is complete. You can find the videos here: https://codingwithmitch.com/courses/build-a-rest-api/.",
                    "https://images.pexels.com/photos/3970332/pexels-photo-3970332.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "mitch",
                    "New areas currently in lockdown",
                    "BBC"
                )
            )

            list.add(
                DummyData(
                    "Premier League",
                    "Justin has been producing online courses for YouTube, Udemy, and his website CodingForEntrepreneurs.com for over 5 years.",
                    "https://images.pexels.com/photos/159400/television-camera-men-outdoors-ballgame-159400.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "John",
                    "The Premier League will still go on",
                    "BBC"
                )
            )
            list.add(
                DummyData(
                    "Covid 19 stats",
                    "Vasiliy has been a freelance android developer for several years. He also has some of the best android development courses I've had the pleasure of taking on Udemy.com.",
                    "https://images.pexels.com/photos/3970333/pexels-photo-3970333.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Steven",
                    "New numbers for cases",
                    "BBC"
                )
            )
            list.add(
                DummyData(
                    "Freelance Android Developer, Donn Felker",
                    "Freelancing as an Android developer with Donn Felker.\\r\\n\\r\\nDonn is also:\\r\\n\\r\\n1) Founder of caster.io\\r\\n\\r\\n2) Co-host of the fragmented podcast (fragmentedpodcast.com).",
                    "https://images.pexels.com/photos/4560150/pexels-photo-4560150.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Richelle",
                    "Article number 1",
                    "BBC"
                )
            )
            list.add(
                DummyData(
                    "Work Life Balance for Software Developers",
                    "What kind of hobbies do software developers have? It sounds like many software developers don't have a lot of hobbies and choose to focus on work. Is that a good idea?",
                    "https://images.pexels.com/photos/3856050/pexels-photo-3856050.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Jessica",
                    "Article number 1",
                    "BBC"
                )
            )
            list.add(
                DummyData(
                    "Full Stack Web Developer - Nicholas Olsen",
                    "In this podcast I interviewed the Fullsnack Developer (AKA Nicholas Olsen).\\r\\n\\r\\nNicholas is many things. What I mean by that is, he's good at many things.\\r\\n\\r\\n1. He’s an entrepreneur\\r\\n\\r\\n2. Web developer\\r\\n\\r\\n3. Artist\\r\\n\\r\\n4. Graphic designer\\r\\n\\r\\n5. Musician (drums)\\r\\n\\r\\n6. Professional BodyBuilder.",
                    "https://images.pexels.com/photos/4127694/pexels-photo-4127694.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Guy",
                    "Article number 1",
                    "BBC"
                )
            )
            list.add(
                DummyData(
                    "Javascript Expert - Wes Bos",
                    "Interviewing a web developer, javascript expert, entrepreneur, freelancer, podcaster, and much more.",
                    "https://images.pexels.com/photos/4524369/pexels-photo-4524369.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                    "Ruby",
                    "Article number 1",
                    "BBC"
                )
            )
            return list
        }

    }
}