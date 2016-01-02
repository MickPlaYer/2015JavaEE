package viewmodel;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;


public class HelloModel {
	private String name;
	
	@NotEmpty(message="{error.empty}")
	@Pattern(regexp="[a-zA-Z\u4E00-\u9FFF]+", message="{error.charOnly}")
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
