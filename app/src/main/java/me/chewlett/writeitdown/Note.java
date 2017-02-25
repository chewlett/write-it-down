package me.chewlett.writeitdown;

/**
 * Created by Curtis Hewlett on 2/24/2017.
 */

public class Note {
    private double date;
    private String title;
    private String subject;
    private String body;

    public Note(){}

    public Note(double date, String title, String subject, String body) {
        setDate(date);
        setTitle(title);
        setSubject(subject);
        setBody(body);
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject != null && !subject.isEmpty()){
            this.subject = subject;
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body != null && !body.isEmpty()) {
            this.body = body;
        }
    }
}
