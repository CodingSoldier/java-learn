package ssm.tomcatwebsocket.b_chatroom;

public class ContentVo {
	private String sendMessage;
	private String checkedUserName;
	private String type;
	public String getSendMessage() {
		return sendMessage;
	}
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
	public String getCheckedUserName() {
		return checkedUserName;
	}
	public void setCheckedUserName(String checkedUserName) {
		this.checkedUserName = checkedUserName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
