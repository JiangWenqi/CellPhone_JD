package cs.bistu.edu.cn.util;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 * 京东具体商品页面处理办法
 * 
 */

public class JDItemProcessor implements PageProcessor {
	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCycleRetryTimes(5).setSleepTime(100).setTimeOut(10000);
	// 手机参数
	private static List<String> parameters = new ArrayList<>();
	// 手机评论
	private static List<String> comments = new ArrayList<>();

	public static List<String> getParameters() {
		return parameters;
	}

	public static List<String> getComments() {
		return comments;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		// 参数是由两个部分组成的
		String parameter1 = page.getHtml().xpath("//ul[@class='parameter1 p-parameter-list']/li/div/p/text()").all()
				.toString();
		String parameter2 = page.getHtml().xpath("//ul[@class='parameter2 p-parameter-list']/li/text()").all()
				.toString();
		// 把两部分参数相加并去除开头和结尾的“[]”
		String parameter = parameter1.substring(1, parameter1.length() - 1) + "\n"
				+ parameter2.substring(1, parameter2.length() - 1);
		parameters.add(parameter);
		
		String comment = page.getHtml().xpath("//div[@class='comment-content']/text()").all().toString();
		comments.add(comment);
	}

	public static void running(List<String> itemUrls) {
		Spider.create(new JDItemProcessor()).startUrls(itemUrls).run();
	}

	public static void main(String[] args) {

	}

}
