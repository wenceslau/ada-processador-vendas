package com.ada.vendas.viacep;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ViaCep {

    public static String consultarCep(String cep) {

        String url = "https://viacep.com.br/ws/" + cep + "/json";

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static Endereco converterToEndereco(String json) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, Endereco.class);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
