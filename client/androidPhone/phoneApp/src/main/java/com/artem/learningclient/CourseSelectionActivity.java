package com.artem.learningclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.artem.client.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseSelectionActivity extends ActionBarActivity {

    public static final String STUDENT_ID = "studentId";
    public static final String COURSE_ID = "courseId";

    private JSONUtil jsonUtil = new JSONUtil();
    private ArrayAdapter<String> adapter;

    private String studentId;
    private Map courses;
    private List<String> courseNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView courseView = (ListView) findViewById(R.id.listView2);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNames);
        courseView.setAdapter(adapter);

        courseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseCourse((int) id);
            }
        });

        showCourses();
    }

    private void showCourses() {
        Intent intent = getIntent();
        String studentStr = intent.getStringExtra(StudentSelectionActivity.CURRENT_STUDENT);
        try {
            Map student = jsonUtil.unmarshalValue(studentStr, Map.class);
            studentId = (String) student.get("_id");
            courses = (Map) student.get("courses");
            for (Object course : courses.values()) {
                courseNames.add((String) ((Map) course).get("course_name"));
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Failed to get student data", e);
        }
    }

    private void chooseCourse(int idx) {
        String name = courseNames.get(idx);
        String courseId = null;
        for (Object course : courses.values()) {
            if (name.equals(((Map) course).get("course_name"))) {
                courseId = (String) ((Map) course).get("course_id");
                break;
            }
        }

        if (courseId != null) {
            Intent intent = new Intent(this, LessonActivity.class);
            intent.putExtra(STUDENT_ID, studentId);
            intent.putExtra(COURSE_ID, courseId);
            startActivity(intent);
        } else {
            Log.e(getClass().getSimpleName(), "Course not found: " + name);
        }

    }
}
