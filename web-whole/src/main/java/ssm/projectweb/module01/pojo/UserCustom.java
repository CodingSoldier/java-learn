package ssm.projectweb.module01.pojo;

public class UserCustom extends User {

	private String note;

	private String note2;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote2() {
		return note2;
	}

	public void setNote2(String note2) {
		this.note2 = note2;
	}

	@Override
	public String toString() {
		return "UserCustom{" +
				"note='" + note + '\'' +
				", note2='" + note2 + '\'' +
				'}';
	}
}
