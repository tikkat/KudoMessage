package se.kudomessage.torsken;

import javax.faces.bean.ManagedBean;

@ManagedBean
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
}