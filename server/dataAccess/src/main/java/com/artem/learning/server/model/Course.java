package com.artem.learning.server.model;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
public interface Course {

    String getId();

    String getDisplayName();

    Lesson prepareNewLesson(Student student);
}
