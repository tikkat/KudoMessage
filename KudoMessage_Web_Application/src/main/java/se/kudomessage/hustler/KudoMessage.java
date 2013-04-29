package se.kudomessage.hustler;

public class KudoMessage {

    protected String id,
            content,
            origin,
            receiver;

    public KudoMessage(String content, String origin, String receiver) {
        this.content = content;
        this.origin = origin;
        this.receiver = receiver;
    }

    public KudoMessage(String id, String content, String origin, String receiver) {
        this.id = id;
        this.content = content;
        this.origin = origin;
        this.receiver = receiver;
    }

    public String toString() {
        String jsonString = "{\"protocol\":\"SMS\"";
        if (id != null) {
            jsonString = jsonString + ",\"id\":\"" + this.id + "\"";
        }
        if (origin != null) {
            jsonString = jsonString + ",\"origin\":\"" + this.origin + "\"";
        }
        if (receiver != null) {
            jsonString = jsonString + ",\"receiver\":\"" + this.receiver + "\"";
        }
        if (content != null) {
            jsonString = jsonString + ",\"content\":\"" + this.content + "\"";
        }
        jsonString = jsonString + "}";

        return jsonString;
    }
}