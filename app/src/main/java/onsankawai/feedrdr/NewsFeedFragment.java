package onsankawai.feedrdr;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ListView;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import origamilabs.library.views.StaggeredGridView;

/**
 * Created by Streamphony on 13/05/2015.
 */
public class NewsFeedFragment extends Fragment {
    public static final String ARG_FEED_URL = "feed_url";
    public NewsFeedFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        WebView webview = (WebView)rootView.findViewById(R.id.newsFeedWebView);

        webview.loadUrl(getArguments().getString(ARG_FEED_URL));

        Fragment commentFragment = CommentFragment.newInstance(CommentFragment.MODE_VIEW);
        commentFragment.getArguments().putString(ARG_FEED_URL, getArguments().getString(ARG_FEED_URL));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.commentContainer, commentFragment).commit();

        return rootView;
    }

    public static class CommentFragment extends Fragment {
        private static final String ARG_FRAG_MODE = "fragment_type";
        private static final int MODE_VIEW = 0;
        private static final int MODE_SUBMIT = 1;

        public static CommentFragment newInstance(int mode) {
            CommentFragment fragment = new CommentFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_FRAG_MODE, mode);
            fragment.setArguments(args);
            return fragment;
        }

        public CommentFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if(getArguments().getInt(ARG_FRAG_MODE) == MODE_VIEW) {
                rootView = inflater.inflate(R.layout.fragment_comment, container, false);

                ListView commentList = (ListView)rootView.findViewById(R.id.comment_list);
                CommentAdapter adapter = new CommentAdapter(this.getActivity(),R.layout.row_comment);
                commentList.setAdapter(adapter);
            }
            /*
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final StaggeredGridView gridView = (StaggeredGridView) rootView.findViewById(R.id.staggeredGridView1);

            StaggeredAdapter adapter = new StaggeredAdapter(this.getActivity(), R.layout.grid_staggered);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
                @Override
                public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
                    NewsFeed feed = (NewsFeed)gridView.getAdapter().getItem(position);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setTitle(feed.getTitle());

                    NewsFeedFragment fragment = new NewsFeedFragment();
                    Bundle args = new Bundle();
                    args.putString(NewsFeedFragment.ARG_FEED_URL, feed.getSourceUrl());
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            adapter.switchSection(getArguments().getInt(ARG_SECTION_NUMBER));

            //adapter.notifyDataSetChanged();


            */
            return rootView;
        }

        private void addComment(Comment cmt) {

        }
    }

}
