package com.ar.web.struts.result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.ar.common.collection.QMap;
import com.ar.common.config.ARConfig;
import com.ar.common.logging.ARLog;
import com.opensymphony.xwork2.ActionInvocation;

public class ExcelDownResult extends StrutsResultSupport {
	private static final long serialVersionUID = 2905557018000969908L;
	private static String templatePath = ARConfig.getInstance().getValue("ar.property.xls.template.path");
	private static String temporaryPath = ARConfig.getInstance().getValue("ar.property.xls.temporary.path");
	private ARLog log = ARLog.getLogger(ExcelDownResult.class);

	@Override
	protected void doExecute(String location, ActionInvocation invocation) throws Exception {
		HttpServletRequest req = null;
		HttpServletResponse res = null;
		try {
			req = ServletActionContext.getRequest();
			res = ServletActionContext.getResponse();

			QMap box = (QMap) req.getAttribute("params");
			// 템플릿 파일명
			String templateFileName = box.getString("templateFileName");
			// 출력될 파일 명
			String outputFileName = box.getString("outputFileName");

			if (templatePath == null || templatePath.trim().equals("")) {
				// throw new CanException("템플릿 주소가 잘못되었습니다.");
			} else if (templateFileName == null
					|| templateFileName.trim().equals("")) {
				// throw new CanException("템플릿 명이 잘못되었습니다.");
			} else if (outputFileName == null
					|| outputFileName.trim().equals("")) {
				// throw new CanException("출력 파일명이 잘못되었습니다.");
			} // if

			String key = Long.toString((Math.round(System.currentTimeMillis()
					+ (Math.random() * 100))))
					+ "_" + outputFileName;

			String inputFileName = templatePath + "/" + templateFileName;
			String tempFileName = temporaryPath + "/" + key;

			log.debug("XLS Input File : " + inputFileName);
			log.debug("XLS Temp File : " + tempFileName);
			log.debug("XLS Output File : " + outputFileName);
			log.debug("XLS Parameter : " + box.toString());
			log.debug("Temp File Name : " + key);

			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS(inputFileName, box, tempFileName);

			File tempFile = null;
			try {
				tempFile = new File(tempFileName);
				if (!tempFile.exists()) {
					// throw new CanException("임시파일이 생성되지 않았습니다.");
				} else if (!tempFile.canRead()) {
					// throw new CanException("임시파일을 읽을 수 없습니다.");
				} // if

				FileInputStream in = new FileInputStream(tempFile);
				OutputStream out = res.getOutputStream();

				res.setHeader("Content-disposition", "attachment;filename="
						+ new String(outputFileName.getBytes("euc-kr"),
								"8859_1"));
				res.setContentType("application/vnd.ms-excel");

				try {
					byte[] buffer = new byte[1024];
					while (in.read(buffer) != -1) {
						out.write(buffer);
					} // while
					out.flush();
				} catch (IOException e) {
					log.error("임시파일 입출력 실패 : " + e.getMessage(), e);
				} finally {
					out.close();
					in.close();
				} // try

			} catch (Exception e) {
				log.error("임시파일 처리 실패 : " + e.getMessage(), e);
			} finally {
				// 임시파일 삭제
				if (tempFile != null) {
					tempFile.delete();
				} // if
			} // try
		} catch (Exception e) {
			log.error("XLS 출력 실패 : " + e.getMessage(), e);
		} finally {
			// res.sendRedirect("issueAndSendForm");
		}// try
	} // doExecute()
} // class ExcelDownResult
