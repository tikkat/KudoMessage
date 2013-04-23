/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.hustler;

public class KudoMessage {
	protected String id, 
		content, 
		origin, 
		receiver;
        
        public KudoMessage (String content, String origin, String receiver) {
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