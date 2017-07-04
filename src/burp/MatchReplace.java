package burp;

public class MatchReplace {

	private final String match;
	private final String replace;
	
	public MatchReplace(String match, String replace) {
		super();
		this.match = match;
		this.replace = replace;
	}
	
	public String getMatch() {
		return match;
	}

	public String getReplace() {
		return replace;
	}
	
}
