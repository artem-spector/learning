package com.artem.learningclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.artem.client.HttpResponse;
import com.artem.client.RequestBuilder;
import com.artem.client.ResponseListener;
import com.artem.client.ServerConnection;

public class LessonActivity extends ActionBarActivity {

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

    private void startLesson() {
        Intent intent = getIntent();
        studentId = intent.getStringExtra(CourseSelectionActivity.STUDENT_ID);
        courseId = intent.getStringExtra(CourseSelectionActivity.COURSE_ID);

        RequestBuilder request = RequestBuilder.post("/app/students/" + studentId).param("courseId", courseId).onResponse(new ResponseListener() {
            @Override
            public void onResponse(HttpResponse response) {
                lessonId = response.headers.get("Location").get(0);
                Log.i(LessonActivity.class.getSimpleName(), "Lesson created: " + lessonId);
            }
        });
        conn.send(request);
    }

}
