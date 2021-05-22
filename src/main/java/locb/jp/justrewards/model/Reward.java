package locb.jp.justrewards.model;

public class Reward {
	private String nick;
	private String sender;
	private String type;
	private Long time;
	private int pay;
	private String group;
	private String comment;
	private int status;
	
	public Reward(
			String nick,
			String sender,
			String type,
			Long time,
			int pay,
			String group,
			String comment,
			int status) 
	{
		this.setNick(nick);
		this.setSender(sender);
		this.setType(type);
		this.setTime(time);
		this.setPay(pay);
		this.setGroup(group);
		this.setComment(comment);
		this.setStatus(status);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPay() {
		return pay;
	}

	public void setPay(int pay) {
		this.pay = pay;
	}

	

}
