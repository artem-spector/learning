package com.artem.learningclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.artem.client.JSONUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseSelectionActivity extends ActionBarActivity {

    private JSONUtil jsonUtil = new JSONUtil();
    private List<String> courseNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView courseView = (ListView) findViewById(R.id.listView2);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNames);
        courseView.setAdapter(adapter);
        retrieveCourses();
    }

    private void retrieveCourses() {
        Intent intent = getIntent();
        String studentStr = intent.getStringExtra(StudentSelectionActivity.CHOSEN_STUDENT);
        try {
            Map student = jsonUtil.unmarshalValue(studentStr, Map.class);
            Map courses = (Map) student.get("courses");
            for (Object course : courses.values()) {
                courseNames.add((String) ((Map)course).get("course_name"));
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Failed to get student data", e);
        }

    }

}
