package org.onLineShop.service;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.onLineShop.dao.OrdeRepository;
import org.onLineShop.dao.PaymentRepository;
import org.onLineShop.entity.EnumStatus;
import org.onLineShop.entity.Orde;
import org.onLineShop.entity.Payment;
import org.onLineShop.service.from.Body;
import org.onLineShop.service.from.StatusPayment;
import org.onLineShop.service.from.TokemAcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class IPaymentServiceImpl implements IPaymentService{
	@Autowired
	public OrdeRepository ordeRepository;
	@Autowired
	public PaymentRepository paymentRepository;
	private static final String apiUser = "fa6b47b3-983c-4b2a-a45e-0be7a024bf2d";
	private static final String subscriptionkey="f2d15abe04884cbfad32eeafc2fa1db2";
	private static final String apiKey ="fa56dcab5053485a86af42d361feb5fd";
	public UUID generatorsUuidVersion4() {
		return Generators.randomBasedGenerator().generate();
	}
	public HttpClient buildHttpClient() {
		return HttpClients.createDefault();
	}
	public URI uriConnection(String lienHttp) throws Exception {
		URIBuilder builder = new URIBuilder(lienHttp);
		return builder.build();
	}
	public StatusPayment getStatusPayment(String ReferenceId,String AcessToken) throws Exception, JsonMappingException, IOException   {
		URI uri = uriConnection("https://sandbox.momodeveloper.mtn.com/collection/v1_0/requesttopay/"+ReferenceId);
		HttpGet request = new HttpGet(uri);
		request.setHeader("Authorization", "Bearer "+AcessToken);
		request.setHeader("X-Reference-Id", ReferenceId);
		request.setHeader("X-Target-Environment", "sandbox");
		request.setHeader("Ocp-Apim-Subscription-Key", subscriptionkey);
		HttpResponse response = buildHttpClient().execute(request);
		if(response.getStatusLine().getStatusCode()==200) {
			String StatusJson= EntityUtils.toString(response.getEntity());
			ObjectMapper objectMapper = new ObjectMapper();
			StatusPayment Status =objectMapper.readValue(StatusJson, StatusPayment.class);
			return Status;
		}
		return null;
	}
	
	public String getToken() throws Exception	{
		URI uri = uriConnection("https://sandbox.momodeveloper.mtn.com/collection/token/");
		HttpPost request = new HttpPost(uri);
		String encode = Base64.getEncoder().encodeToString((apiUser+":"+apiKey).getBytes("UTF-8"));
		request.setHeader("Authorization","Basic "+encode );
		request.setHeader("Ocp-Apim-Subscription-Key",subscriptionkey );
		HttpResponse response = buildHttpClient().execute(request);
		if(response.getStatusLine().getStatusCode()==200) {
			String token = EntityUtils.toString(response.getEntity());
			ObjectMapper objectMapper = new ObjectMapper();
			TokemAcess tokem =objectMapper.readValue(token, TokemAcess.class);
			return tokem.getAccessToken();
		}
		return null;
    
	}
	public StatusPayment RequestMTNPayment(Body bodyClient,long id) throws Exception {
		String ReferenceId = generatorsUuidVersion4().toString();
		String AcessToken = getToken();
		if(AcessToken!=null) {
			URI uri = uriConnection("https://sandbox.momodeveloper.mtn.com/collection/v1_0/requesttopay");
			HttpPost request = new HttpPost(uri);
			request.setHeader("Authorization", "Bearer "+AcessToken);
			//request.setHeader("X-Callback-Url", "");
			request.setHeader("X-Reference-Id",ReferenceId);
			request.setHeader("X-Target-Environment", "sandbox");
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionkey);
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData = objectMapper.writeValueAsString(bodyClient);
			StringEntity reqEntity = new StringEntity(jsonData);
			request.setEntity(reqEntity);
			HttpResponse response = buildHttpClient().execute(request);
			if(response.getStatusLine().getStatusCode()==202) {
				if(ordeRepository.findById(id).isPresent()) {
					StatusPayment status = getStatusPayment(ReferenceId,AcessToken);
					if(status.getStatus().compareTo("SUCCESSFUL")==0){
						Payment payment = new Payment(); 
						Orde orde = ordeRepository.findById(id).get();
						orde.setStatus(EnumStatus.PAID);
						payment.setOrde(orde);
						payment.setPaymentDate(new Date());
						payment.setPaymentOperator("Mtn");
						payment.setPaymentRef(status.getFinancialTransactionId());
						ordeRepository.save(orde);
						paymentRepository.save(payment);
						return status;
					}
					
				}
        	 
			}
		}
		return null;
	}
	    
}

