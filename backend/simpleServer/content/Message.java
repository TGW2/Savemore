package simpleServer.content;

public class Message {
    private long offset;
    private String topic;
    private String message;

    public long getOffset() {
        return offset;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public Message(long offset, String topic, String message) {
        this.offset = offset;
        this.topic = topic;
        this.message = message;
    }


}
