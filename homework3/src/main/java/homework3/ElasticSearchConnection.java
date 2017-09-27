package homework3;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSearchConnection {

	public static void createMapping() {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", true)
				.build();

		try {
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

			String json = "{";
			json += "\"properties\" : {";
			json += "\"text\" : {";
			json += "\"type\" : \"string\",";
			json += "\"index\" : \"not_analyzed\"";
			json += "},";
			json += "\"sentiment\" : {";
			json += "\"type\" : \"string\",";
			json += "\"index\" : \"not_analyzed\"";
			json += "},";
			json += "\"postDate\" : {";
			json += "\"type\" : \"date\"";
			json += "},";
			json += "\"location\" : {";
			json += "\"type\" : \"geo_point\"";
			json += "}";
			json += "}";
			json += "}";

			String index = "twitter";
			String type = "tweets";
			boolean exists = client.admin().indices().prepareExists(index).execute().actionGet().isExists();
			if (!exists) {
				client.admin().indices().prepareCreate(index).addMapping(type, json).execute().actionGet();
			}
			client.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		createMapping();
	}
}
