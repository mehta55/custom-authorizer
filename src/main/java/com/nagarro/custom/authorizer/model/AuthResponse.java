package com.nagarro.custom.authorizer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthResponse {

	// IAM Policy Constants
	public static final String VERSION = "Version";
	public static final String STATEMENT = "Statement";
	public static final String EFFECT = "Effect";
	public static final String ACTION = "Action";
	public static final String NOT_ACTION = "NotAction";
	public static final String RESOURCE = "Resource";
	public static final String NOT_RESOURCE = "NotResource";
	public static final String CONDITION = "Condition";

	private String principalId;
	private Map<String, Object> policyDocument;
	private Map<String, String> context;

	transient PolicyDocument policyDocumentObject;

	public AuthResponse(String principalId, PolicyDocument policyDocumentObject, Map<String, String> context) {
		this.principalId = principalId;
		this.policyDocumentObject = policyDocumentObject;
		this.context = context;
	}

	public AuthResponse() {
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public Map<String, String> getContext() {
		return context;
	}

	public void setContext(Map<String, String> context) {
		this.context = context;
	}

	/**
	 * IAM Policies use capitalized field names, but Lambda by default will
	 * serialize object members using camel case
	 *
	 * This method implements a custom serializer to return the IAM Policy as a
	 * well-formed JSON document, with the correct field names
	 *
	 * @return IAM Policy as a well-formed JSON document
	 */
	public Map<String, Object> getPolicyDocument() {
		Map<String, Object> serializablePolicy = new HashMap<>();
		serializablePolicy.put(VERSION, policyDocumentObject.Version);
		Statement[] statements = policyDocumentObject.getStatement();
		Map<String, Object>[] serializableStatementArray = new Map[statements.length];
		for (int i = 0; i < statements.length; i++) {
			Map<String, Object> serializableStatement = new HashMap<>();
			Statement statement = statements[i];
			serializableStatement.put(EFFECT, statement.Effect);
			serializableStatement.put(ACTION, statement.Action);
			serializableStatement.put(RESOURCE, statement.getResource());
			serializableStatement.put(CONDITION, statement.getCondition());
			serializableStatementArray[i] = serializableStatement;
		}
		serializablePolicy.put(STATEMENT, serializableStatementArray);
		return serializablePolicy;
	}

	public void setPolicyDocument(PolicyDocument policyDocumentObject) {
		this.policyDocumentObject = policyDocumentObject;
	}

	/**
	 * PolicyDocument represents an IAM Policy, specifically for the
	 * execute-api:Invoke action in the context of a API Gateway Authorizer
	 *
	 * Initialize the PolicyDocument with the region where the RestApi is
	 * configured, the AWS Account ID that owns the RestApi, the RestApi identifier
	 * and the Stage on the RestApi that the Policy will apply to
	 */

	public enum HttpMethod {
		GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, ALL

	}

}