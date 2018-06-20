package com.ar.aws.s3;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ar.common.logging.ARLog;

/********************************
 * <pre>
 * 아마존 S3 연결을 관리하는 클래스.
 * </pre>
 * @autho Bak Sang Heon
 *
 ********************************/
public class S3Manager {
	private static ARLog log = ARLog.getLogger(S3Manager.class);
	
	/***********************************
	 * <pre>
	 * AmazonS3에 연결된 인스턴스를 반환한다.
	 * </pre>
	 * @param bucket
	 * @return
	 ***********************************/
	public static S3Manager getS3(String bucket) {
		S3Manager s3 = null;
		
		try {
			s3 = new S3Manager(bucket);
		} catch (Exception e) {
			log.error(e); 
		} // try
		
		return s3;
	} // getS3()
	
	private AmazonS3 _s3;
	private String _bucket;

	/***************************
	 * <pre>
	 * 생성자.
	 * AmazonS3 인스턴스를 생성한다.
	 * </pre>
	 * @param bucket
	 ***************************/
	public S3Manager(String bucket) {
		
		try {
			this._bucket = bucket;
			this._s3 = new AmazonS3Client(new PropertiesCredentials(S3Manager.class.getResourceAsStream("/AwsCredentials.properties")));
			this._s3.createBucket(bucket);
		} catch (Exception e) {
			log.error(e);
		} // try
		
	} // Constructor

	/***********************************
	 * <pre>
	 * 해당 파일을 AmazonS3에 업로드한다.
	 * 결과로 업로드한 파일의 key를 반환한다.
	 * </pre>
	 * @param file
	 * @return
	 ***********************************/
	public String putFile(File file) {
		String filekey = UUID.randomUUID().toString();
		
		PutObjectRequest request = new PutObjectRequest(this._bucket, filekey, file);
		PutObjectResult result = this._s3.putObject(request);
		
		return filekey;
	} // putFile()
	
	/**********************************
	 * <pre>
	 * S3에서 Key에 해당하는 파일을 가져온다.
	 * </pre>
	 * @param filekey
	 * @return
	 **********************************/
	public File getFile(String filekey) {
		GetObjectRequest request = new GetObjectRequest(this._bucket, filekey);
		S3Object object = this._s3.getObject(request);
		S3ObjectInputStream in = object.getObjectContent();
		BufferedOutputStream out = null;
		File file = null;
		try {
			file = File.createTempFile(filekey, null);
			out = new BufferedOutputStream(new FileOutputStream(file));
			
			byte[] buffer = new byte[256];
			while(true) {
				int read = in.read(buffer);
				if (read <= 0) break;
				out.write(buffer, 0, read);
			} // while
			
			out.flush();
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (in != null) try { in.close(); } catch (Exception e) { log.error(e); }
			if (out != null) try { out.close(); } catch (Exception e) { log.error(e); }
		} // try
		
		return file;
	} // getFile()

	/*************************************
	 * <pre>
	 * Amazon S3에 있는 해당 파일을 삭제한다.
	 * </pre>
	 * @param filekey
	 *************************************/
	public void deleteFile(String filekey) {
		DeleteObjectRequest request = new DeleteObjectRequest(this._bucket, filekey);
		this._s3.deleteObject(request);
	} // deleteFile()
	
	/*************************
	 * <pre>
	 * AmazonS3 연결을 종료한다.
	 * </pre>
	 *************************/
	public void close() {
		this._s3 = null;
		this._bucket = null;
	} // close()
} // class S3Manager

