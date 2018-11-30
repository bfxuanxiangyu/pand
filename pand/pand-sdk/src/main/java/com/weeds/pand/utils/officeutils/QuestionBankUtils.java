package com.weeds.pand.utils.officeutils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import com.google.common.collect.Maps;
import com.weeds.pand.utils.PandStringUtils;


public class QuestionBankUtils {
	
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
			String answer="",id="",question="",questionSql="";
			FileWriter fwquestion = new FileWriter(new File("d://opt//word//question.sql"),true);
			BufferedWriter bwq = new BufferedWriter(fwquestion);
			FileWriter fwcheck = new FileWriter(new File("d://opt//word//questionCheck.sql"),true);
			BufferedWriter bwc = new BufferedWriter(fwcheck);
			for (String string : split) {
				string = string.replaceAll(" ", "");
				if(PandStringUtils.isNotBlank(string)){
					if(index%2==0){
						Map<String, String> map = getQuestion(string);
						id = map.get("id");
						answer = map.get("answer");
						question = map.get("question");
						questionSql = "insert into question_bank (id,content,test_type) values ('"+id+"','"+question+"',"+2+");";
						bwq.write(questionSql);
						bwq.newLine();
						System.out.println(question+",答案："+answer+",sql="+questionSql);
					}else{
						getQuestionCheck(string, answer, id,bwc);
//						System.out.println("答案："+string);
					}
					index = index+1;
				}
			}
			bwc.close();
			bwq.close();
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
	public static void getQuestionCheck(String content,String answer,String questionId,BufferedWriter bwc){
		int index = 1;
		String sql = "insert into question_bank_check (id,question_id,check_name,check_content,check_order,check_flag) values ";
		systemSql(content, questionId, sql, answer, "A","B", index++,bwc);
		systemSql(content, questionId, sql, answer, "B","C", index++,bwc);
		systemSql(content, questionId, sql, answer, "C","D", index++,bwc);
		systemSql(content, questionId, sql, answer, "D",null, index++,bwc);
	}
	public static void systemSql(String content,String questionId,String baseSql,String answer,String type,String nextType,int index,BufferedWriter bwc){
		int checkFlag=1;
		if(!content.contains(type)){
			return;
		}
		if(answer.equals(type)){
			checkFlag=0;
		}
		String checkContent = null;
		if(nextType==null || !content.contains(nextType)){
			checkContent = content.substring(content.indexOf(type)+1);
		}else{
			checkContent = content.substring(content.indexOf(type)+1, content.indexOf(nextType));
		}
		String sql =baseSql+  "('"+PandStringUtils.getUUID()+"','"+questionId+"','"+type+"','"+checkContent+"',"+index+","+checkFlag+");";
		try {
			bwc.write(sql);
			bwc.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sql);
	}

}
