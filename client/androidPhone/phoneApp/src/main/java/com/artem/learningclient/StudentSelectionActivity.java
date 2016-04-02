package com.artem.learningclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.artem.client.HttpResponse;
import com.artem.client.JSONUtil;
import com.artem.client.RequestBuilder;
import com.artem.client.ResponseListener;
import com.artem.client.ServerConnection;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentSelectionActivity extends ActionBarActivity {

    public static final String CURRENT_STUDENT = "student";

    private JSONUtil jsonUtil = new JSONUtil();

    private List<Map> students;
    private List<String> studentNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_selection);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseStudent((int) id);
            }
        });

        retrieveStudents();
    }

    private void retrieveStudents() {
        ServerConnection conn = ServerConnection.getConnection(this);
        RequestBuilder req = RequestBuilder.get("/app/students").onResponse(new ResponseListener() {
            @Override
            public void onResponse(HttpResponse response) {
                studentNames.clear();
                if (response.status == 200) {
                    try {
                        students = jsonUtil.unmarshalList(response.body, Map.class);
                        for (Map student : students)
                            studentNames.add(displayName(student));
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        conn.send(req);
    }

    private void chooseStudent(int idx) {
        String name  = studentNames.get(idx);
        Map chosen = null;
        for (Map student : students) {
            if (name.equals(displayName(student))) {
                chosen = student;
                break;
            }
        }

        try {
            String studentStr = jsonUtil.marshal(chosen);
            Intent intent = new Intent(this, CourseSelectionActivity.class);
            intent.putExtra(CURRENT_STUDENT, studentStr);
            startActivity(intent);
        } catch (JsonProcessingException e) {
            Log.e(getClass().getSimpleName(), "Invalid student record ", e);
        }
    }

    private String displayName(Map student) {
        String firstName = (String) student.get("firstName");
        String lastName = (String) student.get("lastName");
        return firstName + " " + lastName;
    }
}
