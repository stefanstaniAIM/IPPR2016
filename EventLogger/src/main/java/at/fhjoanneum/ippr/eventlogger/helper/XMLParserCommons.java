package at.fhjoanneum.ippr.eventlogger.helper;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

/**
 * Created by Matthias on 11.08.2017.
 */
public class XMLParserCommons {

    public static HashMap<String, String> getTransitionNameIdMap(final Element net) {
        final HashMap<String, String> transitions = new HashMap<>();
        final NodeList transitionNodes = net.getElementsByTagName("transition");
        for (int i = 0; i < transitionNodes.getLength(); i++) {
            final Element transitionNode = (Element) transitionNodes.item(i);
            final String id = transitionNode.getAttributes().getNamedItem("id").getNodeValue();
            final NodeList nameNodes = transitionNode.getElementsByTagName("name");

            if (nameNodes.getLength() > 0) {
                final Node nameNode = nameNodes.item(0);
                final String name = ((Element)nameNode).getElementsByTagName("text").item(0).getTextContent();

                transitions.put(name, id);
            }
        }

        return transitions;
    }

}
