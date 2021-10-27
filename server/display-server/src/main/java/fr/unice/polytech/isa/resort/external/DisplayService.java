package fr.unice.polytech.isa.resort.external;

import fr.unice.polytech.isa.common.entities.resort.DisplayPanel;
import fr.unice.polytech.isa.common.exceptions.ExternalServiceException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;

public class DisplayService {
    private final String url;

    public DisplayService(String host, String port) {
        this.url = "http://" + host + ":" + port;
    }

    public void createDisplayPanel(DisplayPanel displayPanel) throws ExternalServiceException {
        JSONObject createRequest = new JSONObject().put("Text", displayPanel.getId());
        try {
            String response = WebClient
                    .create(url)
                    .path("/signs/add")
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .post(createRequest.toString(), String.class);

            displayPanel.setExternalId(Integer.parseInt(response));
        } catch (Exception e) {
            throw new ExternalServiceException("display", url + "/signs/add", e);
        }
    }

    public String getDisplayedText(int externalId) throws ExternalServiceException {
        return (String) getDisplayPanel(externalId).get("Text");
    }

    public boolean getDisplayPanelState(int externalId) throws ExternalServiceException {
        return (boolean) getDisplayPanel(externalId).get("IsOn");
    }

    public void modifyDisplayedText(int externalId, String message) throws ExternalServiceException {
        JSONObject response = getDisplayPanel(externalId);

        if (message.isEmpty()) {
            if (response.get("IsOn").equals(true)) { //The message is empty and the display panel is on, we should turn it off
                switchDisplayPanelState(externalId);
            }
        } else {
            if (response.get("IsOn").equals(false)) { //The message is not empty and the display panel is off, we should turn it on
                switchDisplayPanelState(externalId);
            }
            sendModificationRequest(externalId, message);
        }
    }

    private void sendModificationRequest(int externalId, String message) throws ExternalServiceException {
        JSONObject modifyTextRequest = new JSONObject().put("Text", message);
        try {
            WebClient
                    .create(url)
                    .path("/signs/" + externalId + "/display")
                    .accept(MediaType.WILDCARD)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .post(modifyTextRequest.toString(), Boolean.class);
        } catch (Exception e) {
            throw new ExternalServiceException("display", url + "/signs/" + externalId + "/display", e);
        }
    }

    private void switchDisplayPanelState(int externalId) throws ExternalServiceException {
        try {
            WebClient
                    .create(url)
                    .path("/signs/" + externalId + "/switch")
                    .accept(MediaType.WILDCARD)
                    .get(String.class);
        } catch (Exception e) {
            throw new ExternalServiceException("display", url + "/signs/" + externalId + "/switch", e);
        }
    }

    private JSONObject getDisplayPanel(int externalId) throws ExternalServiceException {
        JSONObject response;
        try {
            response = new JSONObject(
                    WebClient
                            .create(url)
                            .path("/signs/" + externalId)
                            .accept(MediaType.WILDCARD)
                            .get(String.class));
        }
        catch (Exception e) {
            throw new ExternalServiceException("display", url + "/signs/" + externalId, e);
        }
        return response;
    }
}
