package com.diana_ukrainsky.twitflick.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diana_ukrainsky.twitflick.R;
import com.diana_ukrainsky.twitflick.adapter.ReviewAdapter;
import com.diana_ukrainsky.twitflick.callbacks.Callback_setMyFriends;
import com.diana_ukrainsky.twitflick.callbacks.Callback_setReviews;
import com.diana_ukrainsky.twitflick.callbacks.Callback_setUsername;
import com.diana_ukrainsky.twitflick.data.ReviewsDao;
import com.diana_ukrainsky.twitflick.databinding.FragmentFriendsFeedBinding;
import com.diana_ukrainsky.twitflick.logic.DatabaseManager;
import com.diana_ukrainsky.twitflick.models.CurrentUser;
import com.diana_ukrainsky.twitflick.models.GeneralUser;
import com.diana_ukrainsky.twitflick.models.ReviewData;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFeedFragment extends Fragment {
    private MaterialTextView friendsFeedFragment_TXT_noReviews;
    //Friends Feed Activity
    private RecyclerView recyclerView;
    private ReviewAdapter reviewsAdapter;

    private FragmentFriendsFeedBinding binding;
    private View view;

    private ReviewsDao reviewsDao;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFeedFragment newInstance(String param1, String param2) {
        FriendsFeedFragment fragment = new FriendsFeedFragment ();
        Bundle args = new Bundle ();
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        initData();
        setReviewsUI();

        if (getArguments () != null) {
            mParam1 = getArguments ().getString (ARG_PARAM1);
            mParam2 = getArguments ().getString (ARG_PARAM2);
        }
    }

    private void initData() {
        reviewsDao = new ReviewsDao ();
    }

    private void setReviewsUI() {
        DatabaseManager.getInstance ().getUsernameFromId (DatabaseManager.getInstance ().getFirebaseUser ().getUid (),new Callback_setUsername () {
            @Override
            public void setUsername(String username) {
                if(username!=null) {
                    CurrentUser.getInstance ().setUsername (username);
                    getMyFriendsList ();
                }
            }
        });
    }

    private void getMyFriendsList() {
        DatabaseManager.getInstance ().getFriendsList (new Callback_setMyFriends () {
            @Override
            public void setMyFriendsList(List<GeneralUser> myFriendsList) {
                if(myFriendsList != null)
                    setMyFriendsReviewsUI(myFriendsList);
            }
        });
    }

    private void setMyFriendsReviewsUI(List<GeneralUser> myFriendsList) {
        DatabaseManager.getInstance ().getReviewsList (myFriendsList,new Callback_setReviews () {
            @Override
            public void setReviewList(List<ReviewData> reviewList) {
                if(reviewList.isEmpty ())
                    friendsFeedFragment_TXT_noReviews.setVisibility (View.VISIBLE);
                else
                {
                    friendsFeedFragment_TXT_noReviews.setVisibility (View.INVISIBLE);
                }
                reviewsAdapter = new ReviewAdapter (reviewList,getContext ());
                recyclerView.setAdapter (reviewsAdapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends_feed, container, false);
        view = binding.getRoot();

        findViews();
        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager (getActivity ()));
    }

    private void findViews() {
        friendsFeedFragment_TXT_noReviews = view.findViewById (R.id.friendsFeedFragment_TXT_noReviews);
        recyclerView = view.findViewById (R.id.friendsFeedFragment_RV_recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart ();
        initData ();
    }
}