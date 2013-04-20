package se.kudomessage.torsken;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class KudoMessage {
	protected String id, 
		content, 
		origin, 
		receiver;
	
	public KudoMessage(){
		//Empty message
	}

	public KudoMessage(String id, String content, String origin, String receiver){
		this.id = id;
		this.content = content;
		this.origin = origin;
		this.receiver = receiver;
	}
	
	public KudoMessage(String id, String content, String origin){
		this.id = id;
		this.content = content;
		this.origin = origin;
	}
        
        public KudoMessage ( String content, String receiver ) {
            this.content = content;
            this.receiver = receiver;
        }
	
	public KudoMessage(String id){
		this.id = id;
	}

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getOrigin() {
        return origin;
    }

    public String getReceiver() {
        return receiver;
    }
	
}