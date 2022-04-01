package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter

    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // this is where we setup our views and click listeners
        postsRecyclerView=view.findViewById<RecyclerView>(R.id.postRecyclerView)

        //Steps to populate the RecyclerView
        //1. Create a layout for each row in the list
        //2. Create a data source for each row (This is the Post Class)
        //3. Create adapter that will bridge data and row layout
        //4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter
        //5. Set layout manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        queryPosts()
    }
    // Query for all posts in our server
    open fun queryPosts(){
        // specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //find all the Post objects that are in our server
        query.include(Post.KEY_USER)
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

    // Clean all elements of the recycler

    fun clear() {
        allPosts.clear()
        adapter.notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Post>) {
        queryPosts()
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "FeedFragment"
    }
}