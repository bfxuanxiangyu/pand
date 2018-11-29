package com.weeds.pand.utils.officeutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.google.common.collect.Maps;
import com.weeds.pand.utils.PandStringUtils;


public class QuestionBankPanduanUtils {
	
	public static void main(String[] args) {
		
		readWord("D:\\opt/word/test1.docx");
	}
	
	/**
	 * 读取word文件内容
	 * 
	 * @param path
	 * @return buffer
	 */
 
	public static String readWord(String path) {
		String buffer = "";
		try {
			if (path.endsWith(".doc")) {
				InputStream is = new FileInputStream(new File(path));
				WordExtractor ex = new WordExtractor(is);
				buffer = ex.getText();
				ex.close();
			} else if (path.endsWith("docx")) {
				OPCPackage opcPackage = POIXMLDocument.openPackage(path);
				POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
				buffer = extractor.getText();
				replaceBlank(buffer);
				extractor.close();
			} else {
				System.out.println("此文件不是word文件！");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return buffer;
	}
	
	
	public static String replaceBlank(String str) throws Exception{
		String dest = "";
		if (str!=null) {
			String[] split = str.split("\n");
			int index = 0;
			String answer="",id="",question="",questionSql="",checkSql="";
			FileWriter fwquestion = new FileWriter(new File("d://opt//word//question.sql"),true);
			BufferedWriter bwq = new BufferedWriter(fwquestion);
			FileWriter fwcheck = new FileWriter(new File("d://opt//word//questionCheck.sql"),true);
			BufferedWriter bwc = new BufferedWriter(fwcheck);
			for (String string : split) {
				string = string.replace(" ", "");
				if(PandStringUtils.isNotBlank(string)){
					System.out.println(string);
					if(string.contains("（") && string.contains("）")){
						answer = string.substring(string.indexOf("（")+1,string.indexOf("）"));
						question = string.replace(answer, " ");
						id = PandStringUtils.getUUID();
						questionSql = "insert into question_bank (id,content,test_type) values ('"+id+"','"+question+"',"+1+");";
						bwq.write(questionSql);
						bwq.newLine();
						String baseSql = "insert into question_bank_check (id,question_id,check_name,check_content,check_order,check_flag) values ";
						System.out.println(questionSql);
						if(answer.equals("对")){
							checkSql =baseSql+  "('"+PandStringUtils.getUUID()+"','"+id+"','','对',"+index+",0);";
							bwc.write(checkSql);
							bwc.newLine();
							System.out.println(checkSql);
							checkSql =baseSql+  "('"+PandStringUtils.getUUID()+"','"+id+"','','错',"+index+",1);";
							bwc.write(checkSql);
							bwc.newLine();
							System.out.println(checkSql);
						}else{
							checkSql =baseSql+  "('"+PandStringUtils.getUUID()+"','"+id+"','','对',"+index+",1);";
							bwc.write(checkSql);
							bwc.newLine();
							System.out.println(checkSql);
							checkSql =baseSql+  "('"+PandStringUtils.getUUID()+"','"+id+"','','错',"+index+",0);";
							bwc.write(checkSql);
							bwc.newLine();
							System.out.println(checkSql);
						}
					}
					index=index+1;
				}
			}
			bwq.close();
			bwc.close();
			fwquestion.close();
			fwcheck.close();
			System.out.println("共"+index/2+"题");
		}
		return dest;
	}

	public static Map<String, String> getQuestion(String str){
		Map<String, String> map = Maps.newHashMap();
		if(str.contains("（") && str.contains("）")){
			String answer = str.substring(str.indexOf("（")+1,str.indexOf("）"));
			map.put("answer", answer);
			map.put("question", str.replace(answer, " "));
			map.put("id", PandStringUtils.getUUID());
		}
		return map;
	}
	public static void getQuestionCheck(String content,String answer,String questionId,FileWriter fwcheck){
		int index = 1;
		String sql = "insert into question_bank_check (id,question_id,check_name,check_content,check_order,check_flag) values ";
		systemSql(content, questionId, sql, answer, "A","B", index++,fwcheck);
		systemSql(content, questionId, sql, answer, "B","C", index++,fwcheck);
		systemSql(content, questionId, sql, answer, "C","D", index++,fwcheck);
		systemSql(content, questionId, sql, answer, "D",null, index++,fwcheck);
	}
	public static void systemSql(String content,String questionId,String baseSql,String answer,String type,String nextType,int index,FileWriter fwcheck){
		int checkFlag=1;
		if(!content.contains(type)){
			return;
		}
		if(answer.equals(type)){
			checkFlag=0;
		}
		String checkContent = null;
		if(nextType==null){
			checkContent = content.substring(content.indexOf(type)+1);
		}else{
			checkContent = content.substring(content.indexOf(type)+1, content.indexOf(nextType));
		}
		String sql =baseSql+  "('"+PandStringUtils.getUUID()+"','"+questionId+"','"+type+"','"+checkContent+"',"+index+","+checkFlag+");";
		try {
			fwcheck.write(sql);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sql);
	}

}
