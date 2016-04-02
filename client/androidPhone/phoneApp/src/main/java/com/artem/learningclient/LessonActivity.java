package com.artem.learningclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.artem.client.HttpResponse;
import com.artem.client.JSONUtil;
import com.artem.client.RequestBuilder;
import com.artem.client.ResponseListener;
import com.artem.client.ServerConnection;
import com.artem.server.api.drawing.DrawingRawData;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Map;

public class LessonActivity extends ActionBarActivity {

    private JSONUtil jsonUtil = new JSONUtil();
    private ServerConnection conn;

    private String studentId;
    private String courseId;
    private String lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        conn = ServerConnection.getConnection(this);
        startLesson();
    }

    public void okPressed(View view) {
        DrawingRawData data = getDrawingView().getData();
        try {
            submitTrialData(jsonUtil.marshal(data));
        } catch (JsonProcessingException e) {
            Log.e(LessonActivity.class.getSimpleName(), "Fail to marshal drawing", e);
        }
    }

    public void cancelPressed(View view) {
        getDrawingView().clear();
    }

    private void startLesson() {
        Intent intent = getIntent();
        studentId = intent.getStringExtra(CourseSelectionActivity.STUDENT_ID);
        courseId = intent.getStringExtra(CourseSelectionActivity.COURSE_ID);

        RequestBuilder request = RequestBuilder.post("/app/students/" + studentId).param("courseId", courseId).onResponse(new ResponseListener() {
            @Override
            public void onResponse(HttpResponse response) {
                lessonId = response.headers.get("Location").get(0);
                Log.d(LessonActivity.class.getSimpleName(), "Lesson created: " + lessonId);
                submitTrialData(null);
            }
        });
        conn.send(request);
    }

    private void submitTrialData(String dataStr) {
        RequestBuilder req = RequestBuilder.post("/app/lesson/" + lessonId);
        if (dataStr != null) req.param("trialResponse", dataStr);
        req.onResponse(new ResponseListener() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.status != 200) {
                    Log.e(LessonActivity.class.getSimpleName(), "submitTrialData failed: " + response);
                    return;
                }

                try {
                    Map serverData = jsonUtil.unmarshalValue(response.body, Map.class);
                    final Map nextTrial = (Map) serverData.get("nextTrial");
                    String trialFeedback = (String) serverData.get("trialFeedback");
                    final String lessonFeedback = (String) serverData.get("lessonFeedback");

                    AsyncTask<Void, Void, Void> nextTask = lessonFeedback != null
                            ? showMessageAndDo(lessonFeedback, 2, call(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }))
                            : call(new Runnable() {
                        @Override
                        public void run() {
                            startTrial(nextTrial);
                        }
                    });

                    showMessageAndDo(trialFeedback, 2, nextTask).execute();
                } catch (IOException e) {
                    Log.e(LessonActivity.class.getSimpleName(), "Failed to read trial data.", e);
                }
            }
        });
        conn.send(req);
    }

    private void startTrial(Map trialData) {
        String stimulus = (String) trialData.get("stimulus");
        getDrawingView().clear();
        getStimulusView().setText(stimulus);
    }

    private TextView getStimulusView() {
        return (TextView) findViewById(R.id.stimulus);
    }

    private DrawingSurfaceView getDrawingView() {
        return (DrawingSurfaceView) findViewById(R.id.drawingArea);
    }

    private AsyncTask<Void, Void, Void> showMessageAndDo(final String message, final int waitSec, final AsyncTask<Void, Void, Void> nextTask) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                if (message != null)
                    getStimulusView().setText(message);
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (waitSec > 0)
                    try {
                        Thread.sleep(waitSec * 1000);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (nextTask != null) nextTask.execute();
            }
        };
    }

    private AsyncTask<Void, Void, Void> call(final Runnable runnable) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                runnable.run();
            }
        };
    }
}
