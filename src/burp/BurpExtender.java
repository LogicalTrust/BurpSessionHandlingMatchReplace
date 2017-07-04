package burp;

import java.awt.Component;
import java.util.List;

public class BurpExtender implements IBurpExtender, ISessionHandlingAction, ITab
{
	private IExtensionHelpers helpers;
	private MatchReplaceConfigurationPanel configuration;

	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		callbacks.registerSessionHandlingAction(this);
		helpers = callbacks.getHelpers();
		configuration = new MatchReplaceConfigurationPanel();
		callbacks.customizeUiComponent(configuration);
		callbacks.addSuiteTab(this);
	}

	@Override
	public String getActionName() {
		return "Match and replace";
	}

	@Override
	public void performAction(IHttpRequestResponse currentRequest, IHttpRequestResponse[] macroItems) {
		String request = helpers.bytesToString(currentRequest.getRequest());
		List<MatchReplace> data = configuration.getData();
		for (MatchReplace element : data) {
			request = request.replaceAll(element.getMatch(), element.getReplace());
		}
		currentRequest.setRequest(helpers.stringToBytes(request));
	}

	@Override
	public String getTabCaption() {
		return "Match and replace";
	}

	@Override
	public Component getUiComponent() {
		return configuration;
	}
}