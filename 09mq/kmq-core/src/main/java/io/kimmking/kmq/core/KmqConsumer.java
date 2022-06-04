package io.kimmking.kmq.core;

public class KmqConsumer<T> {

    private final KmqBroker broker;

    private Kmq kmq;

    private String consumerId;

    private int offset = 0;

    private int committedOffset = 0;

    public KmqConsumer(KmqBroker broker) {
        this.broker = broker;
    }

    public KmqConsumer(KmqBroker broker, String consumerId) {
        this.broker = broker;
        this.consumerId = consumerId;
    }

    public void subscribe(String topic) {
        this.kmq = this.broker.findKmq(topic);
        if (null == kmq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public KmqMessage<T> poll(long timeout) {
        KmqMessage<T> message = null;
        long start = System.currentTimeMillis();
        while (message == null) {
            if (System.currentTimeMillis() - start > timeout) {
                // timeout
                break;
            }
            message = kmq.poll(consumerId, offset);
        }
        // 自动提交位移
        if (message != null && this.commit()) {
            committedOffset = offset;
        }
        offset += 1;
        return message;
    }

    public boolean commit() {
        return kmq.commit(consumerId, offset);
    }

    public void seek(int offset) {
        this.offset = offset;
    }

}
