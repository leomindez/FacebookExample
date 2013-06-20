package com.lionlab.routours.fragment;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.angelhack.routours.R;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class LoginFragment extends SherlockFragment implements
		Session.StatusCallback {

	private UiLifecycleHelper lifecycleHelper;
	private LoginButton loginButton;
	private Button postButton;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup group = (ViewGroup) inflater.inflate(
				R.layout.layout_fragment_main, container, false);

		loginButton = (LoginButton) group.findViewById(R.id.login_button);
		postButton = (Button) group.findViewById(R.id.button1);
		return group;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lifecycleHelper = new UiLifecycleHelper(getActivity(), this);
		lifecycleHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionState(session, session.getState(), null);

		}

		lifecycleHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		lifecycleHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		lifecycleHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);
		}
		loginButton.setFragment(this);
		loginButton.setPublishPermissions(PERMISSIONS);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				publishStory();
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lifecycleHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		lifecycleHelper.onSaveInstanceState(outState);
	}

	private void onSessionState(Session session, SessionState sessionState,
			Exception exception) {
		if (sessionState.isOpened()) {
			if (pendingPublishReauthorization
					&& sessionState.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				pendingPublishReauthorization = false;
				publishStory();
			}
			postButton.setVisibility(View.VISIBLE);
			Log.i("LoginFragment", "Log in....");
		} else if (sessionState.isClosed()) {
			Log.i("LoginFragment", "Log out....");
			postButton.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void call(Session session, SessionState state, Exception exception) {
		onSessionState(session, state, exception);
	}

	private void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);

			}

			Bundle postParams = new Bundle();
			postParams.putString("name", "RouTours");
			postParams.putString("link", "");
			postParams.putString("picture", "");
			postParams.putString("captions", "");
			postParams.putString("message",
					"¡Lo importante no es llegar, si no disfrutar el camino!");

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getActivity().getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					} else {
						// Toast.makeText(getActivity().getApplicationContext(),
						// postId, Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
}
