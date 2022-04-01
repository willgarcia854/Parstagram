package com.example.parstagram.fragments

import android.util.Log
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {
    override fun queryPosts(){
        // specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all the Post objects that are in our server
        query.include(Post.KEY_USER)
        // Only return posts from current user (signed in)
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // return the posts in descending order, so newer posts will appear first
        query.addDescendingOrder("createdAt")

        // Only return the most recent 20 posts
        query.setLimit(20)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    // something has went wrong
                    Log.e(MainActivity.TAG, "Error fetching posts")
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(
                                MainActivity.TAG, "Post:" + post.getDescription()+" , username: " +
                                        post.getUser()?.username)
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }

                }
            }

        })
    }

}