package com.ar.aws.sqs;

import java.util.List;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.ar.common.logging.ARLog;

/*******************************
 * <pre>
 * 아마존 S3 서비스를 관리하는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *******************************/
public class SQSManager {
	private static ARLog log = ARLog.getLogger(SQSManager.class);
	
	/*************************************
	 * <pre>
	 * SQSManager 인스턴스를 생성하여 반환한다.
	 * </pre>
	 * @return
	 *************************************/
	public static SQSManager getSQS(String endpoint, String queuename) {
		SQSManager sqs = null;
		
		try {
			sqs = new SQSManager(endpoint, queuename);
		} catch (Exception e) {
			log.error(e);
		} // try
		
		return sqs;
	} // getSQS()
	
	private String _endpoint;
	private String _queuename;
	private AmazonSQS _sqs;
	private String _queueURL;
	
	/********************************
	 * <pre>
	 * 생성자.
	 * AmazonSQS에 대한 연결을 처리한다.
	 * </pre>
	 * @param endpoint
	 * @param queuename
	 ********************************/
	private SQSManager(String endpoint, String queuename) {
		this._endpoint = endpoint;
		this._queuename = queuename;
		try {
			this.connect();
			/*
			_sqs = new AmazonSQSClient(new PropertiesCredentials(SQSManager.class.getResourceAsStream("/AwsCredentials.properties")));
			_sqs.setEndpoint(endpoint);
			
			CreateQueueRequest request = new CreateQueueRequest(queuename);
			_queueURL = _sqs.createQueue(request).getQueueUrl();
			*/
		} catch (Exception e) {
			log.error(e);
		} // try
		
	} // Constructor()
	
	/************************
	 * <pre>
	 * AmazonSQS 에 연결한다.
	 * </pre>
	 ************************/
	private void connect() {
		try {
			_sqs = new AmazonSQSClient(new PropertiesCredentials(SQSManager.class.getResourceAsStream("/AwsCredentials.properties")));
			_sqs.setEndpoint(_endpoint);
			
			CreateQueueRequest request = new CreateQueueRequest(_queuename);
			_queueURL = _sqs.createQueue(request).getQueueUrl();
		} catch (Exception e) {
			log.error(e);
		} // try
	} // connect()
	
	/*******************************
	 * <pre>
	 * 현재 연결된 AmazonSQS를 반환한다.
	 * </pre>
	 * @return
	 *******************************/
	public AmazonSQS getSQSConnect() throws Exception {
		if (this._sqs == null) throw new Exception("SQS already closed");
		return this._sqs;
	} // getSQSConnect()
	
	/*************************************
	 * <pre>
	 * 현재 연결에 대한 Queue URL을 반환한다.
	 * </pre>
	 * @return
	 *************************************/
	public String getQueueURL() throws Exception {
		if (this._sqs == null) throw new Exception("SQS already closed");
		return this._queueURL;
	} // getQueueURL()

	/********************
	 * <pre>
	 * SQS로 메세지를 보낸다.
	 * </pre>
	 * @param message
	 ********************/
	public String sendMessage(String message) throws Exception {
		if (this._sqs == null) throw new Exception("SQS already closed");
		SendMessageRequest request = new SendMessageRequest(this._queueURL, message);
		SendMessageResult result = this._sqs.sendMessage(request);
		String messageId = result.getMessageId();
		return messageId;
	} // sendMessage()

	/*****************************************
	 * <pre>
	 * 현재 연결된 SQS로 부터 단일 메세지를 수신한다.
	 * </pre>
	 * @return
	 *****************************************/
	public Message receiveMessage() throws Exception {
		if (this._sqs == null) throw new Exception("SQS already closed");
		ReceiveMessageRequest request = new ReceiveMessageRequest(this._queueURL);
		request.setWaitTimeSeconds(10);
		request.setMaxNumberOfMessages(1);
		ReceiveMessageResult result = this._sqs.receiveMessage(request);
		List<Message> messages = result.getMessages();
		if (messages.size() > 0)
			return messages.get(0);
		else
			return null;
	} // receiveMessage()
	
	/***************************
	 * <pre>
	 * 해당 SQS 메세지를 삭제한다.
	 * </pre>
	 * @param message
	 ***************************/
	public void deleteMessage(Message message) throws Exception {
		if (this._sqs == null) throw new Exception("SQS already closed");
		String receiptHandle = message.getReceiptHandle();
		DeleteMessageRequest request = new DeleteMessageRequest(this._queueURL, receiptHandle);
		this._sqs.deleteMessage(request);
	} // deleteMessage()
	
	/********************************
	 * <pre>
	 * Amazon SQS에 대한 연결을 종료한다.
	 * </pre>
	 ********************************/
	public void close() {
		this._sqs.shutdown();
		this._sqs = null;
		this._queueURL = null;
	} // close()
	
	public boolean isClosed() {
		if (this._sqs == null || this._queueURL == null) {
			return true;
		} else {
			return false;
		} // if
	} // isClosed()
	
	/***************
	 * <pre>
	 * 재연결 한다.
	 * </pre>
	 ***************/
	public void reconnect() {
		try {
			// 기존 연결을 닫는다.
			if (this._sqs != null) {
				this._sqs.shutdown();
				this._sqs = null;
			} // if
			this._queueURL = null;
		} catch (Exception e) {
			log.error(e);
		} // try
		
		this.connect();
	} // reconnect()
	
} // class SQSManager
