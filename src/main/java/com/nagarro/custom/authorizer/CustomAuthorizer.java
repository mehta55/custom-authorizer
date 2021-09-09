package com.nagarro.custom.authorizer;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nagarro.custom.authorizer.model.APIGatewayProxyRequestEvent;
import com.nagarro.custom.authorizer.model.AuthResponse;
import com.nagarro.custom.authorizer.model.AuthResponse.HttpMethod;
import com.nagarro.custom.authorizer.model.PolicyDocument;

public class CustomAuthorizer implements RequestHandler<APIGatewayProxyRequestEvent, AuthResponse> {

	public AuthResponse handleRequest(APIGatewayProxyRequestEvent input, Context context) {

		String principalId = "apigateway.amazonaws.com";
		String jwt = input.getHeaders().get("Authorization");

		String region = input.getMethodArn().split(":")[3];
		APIGatewayProxyRequestEvent.ProxyRequestContext proxyRequestContext = input.getRequestContext();
		String awsAccountId = proxyRequestContext.getAccountId();
		String restApiId = proxyRequestContext.getApiId();
		String stage = proxyRequestContext.getStage();
		String method = proxyRequestContext.getHttpMethod();
		String resourcePath = proxyRequestContext.getResourcePath();

		Map<String, String> cntxt = new HashMap<>();
		cntxt.put("userid", "sahli123");
		cntxt.put("role", "super-user");
		cntxt.put("body", input.getBody());
		cntxt.put("query_params", "size : " + input.getQueryStringParameters().size() + "values : "
				+ input.getQueryStringParameters().toString());
		cntxt.put("path_params",
				"size : " + input.getPathParameters().size() + "values : " + input.getPathParameters().toString());

		AuthResponse authResponse = null;
		PolicyDocument policyDocument = null;

		switch (jwt) {
		case "AllowOne":
			policyDocument = PolicyDocument.getAllowOnePolicy(region, awsAccountId, restApiId, stage,
					HttpMethod.valueOf(method), resourcePath);
			authResponse = new AuthResponse(principalId, policyDocument, cntxt);
			break;
		case "DenyOne":
			policyDocument = PolicyDocument.getDenyOnePolicy(region, awsAccountId, restApiId, stage,
					HttpMethod.valueOf(method), resourcePath);
			authResponse = new AuthResponse(principalId, policyDocument, cntxt);
			break;
		case "AllowAll":
			policyDocument = PolicyDocument.getAllowAllPolicy(region, awsAccountId, restApiId, stage);
			authResponse = new AuthResponse(principalId, policyDocument, cntxt);
			break;
		default:
			policyDocument = PolicyDocument.getDenyAllPolicy(region, awsAccountId, restApiId, stage);
			authResponse = new AuthResponse(principalId, policyDocument, cntxt);
			break;
		}

		return authResponse;

	}
}
