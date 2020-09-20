package com.codebase.inmateapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel {

        @SerializedName("payload")
        @Expose
        private Payload payload;

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }

public class Message {

    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("message")
    @Expose
    private String message;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

public class Payload {

    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;
    @SerializedName("success")
    @Expose
    private boolean success = false;
    @SerializedName("error")
    @Expose
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }}
}
