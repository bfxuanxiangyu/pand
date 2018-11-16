package com.weeds.pand.service.system.domain;

import java.util.Map;

public class Page {
	private int pageIndex = 1;// 页码
	private int pageSize = 20;// 每页长度

	// 分页查询开始记录位置
	private int begin;
	// 分页查看下结束位置
	private int end;
	// 查询结果总记录数
	private int count;
	// 当前页码
	private int current;
	// 总共页数
	private int total;

	public Page() {
	}

	public Page( int pageIndex, int pageSize) {
		if (pageSize > 0)
			this.pageSize = pageSize;
		this.begin = (pageIndex - 1) * pageSize;
		this.end = pageSize;
		this.current = pageIndex;
	}

	/**
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(int begin) {
		this.begin = begin;
		if (this.pageSize != 0) {
			this.current = (int) Math.floor((this.begin * 1.0d) / this.pageSize) + 1;
		}
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
		this.total = (int) Math.floor((this.count * 1.0d) / this.pageSize);
		if (this.count % this.pageSize != 0) {
			this.total++;
		}
	}

	/**
	 * @return the current
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * @param current
	 *            the current to set
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		if (total == 0) {
			return 1;
		}
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	/**
	 * 获取分页参数
	 * @param parameters
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Map<String, Object> getPageMap(Map<String, Object> parameters,Integer pageIndex,Integer pageSize){
		if(pageIndex<=1){
			pageIndex = 0;
		}else{
			pageIndex = pageIndex-1;
		}
		parameters.put("begin", pageIndex * pageSize);
		parameters.put("end", pageSize);
		return parameters;
	}

}

