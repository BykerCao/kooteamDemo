package cn.cao.vo;

public class MessageVO {
	private Integer msgId;
	private String msgData;
	public Integer getMsgId() {
		return msgId;
	}
	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}
	public String getMsgData() {
		return msgData;
	}
	public void setMsgData(String msgData) {
		this.msgData = msgData;
	}
	public MessageVO(Integer msgId, String msgData) {
		super();
		this.msgId = msgId;
		this.msgData = msgData;
	}
	
	@Override
	public String toString() {
		return "MessageVO [msgId=" + msgId + ", msgData=" + msgData + ", getMsgId()=" + getMsgId() + ", getMsgData()="
				+ getMsgData() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	//静态new一个自己，再外面调用不用new
	public static MessageVO CreateMessageVO(Integer msgId,String msgData) {
		return new MessageVO(msgId,msgData);
	}
	
}
