package cs.bistu.edu.cn.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class JDJsonProcessor implements PageProcessor {

	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCycleRetryTimes(3).setSleepTime(300).setTimeOut(10000);
	// cell phone 价格信息
	private static List<String> prices = new ArrayList<>();

	// cell phone 好评信息
	private static List<String> goodRates = new ArrayList<>();
	// cell phone 评价数量信息
	private static List<String> commentCounts = new ArrayList<>();

	public static List<String> getCommentCounts() {
		return commentCounts;
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	/*
	 * 其中一个对象的示例 {"id":"J_2967929","p":"2499.00","m":"3222.00","op":"2499.00"}
	 * 
	 */
	@Override
	public void process(Page page) {
		String json = page.getRawText();
		// 初始化prices链表
		prices = new ArrayList<>();
		// 初始化好评链表
		goodRates = new ArrayList<>();
		// 初始化评价数链表
		commentCounts = new ArrayList<>();
		if (!json.contains("CommentsCount")) {
			// 转化为JSON对象数组
			JSONArray jsonArray = JSONObject.parseArray(json);

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject priceJsonObject = jsonArray.getJSONObject(i);
				String price = priceJsonObject.getString("p"); // 得到P属性值（价格）
				prices.add(price);
			}
		} else {
			JSONObject jsonObj = JSONObject.parseObject(json);
			// 取json中“CommentsCount”对象数组里面的对象
			JSONArray jsonArray = jsonObj.getJSONArray("CommentsCount");

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject commentJsonObject = jsonArray.getJSONObject(i);
				String goodRate = commentJsonObject.getString("GoodRate"); // 得到GoodRate
				String commentCount = commentJsonObject.getString("CommentCount");
				goodRates.add(goodRate);
				commentCounts.add(commentCount);
			}
		}
	}

	public static List<String> running(String url) {
		Spider.create(new JDJsonProcessor()).addUrl(url).run();
		if (!prices.isEmpty())
			return prices;
		else
			return goodRates;
	}

	public static void main(String[] args) {

		running("http://club.jd.com/comment/productCommentSummaries.action?my=pinglun&referenceIds=2967929,3133857,3158054,3652063,3129274,3888284,4239612,2249594,2888226,4435312,3553539,3296817,3367822,2402694,4510588,3322621,2321852,3379941,10941037481,2938299,1217500,3355175,3749093,3899582,4207732,3726830,3169118,3888216,2600210,3234250,2600240,2600129,2122925,10532651102,3556582,4461470,4139518,4371862,10932167751,11193025109,10533056347,11284596060,3789624,3103965,10532418387,2805806,2839509,10829282573,10834639654,10829330789,2712431,10921539206,10075216445,3418906,10941082986,10927332921,10931044507,10358053505,2949157,1614749621");

	}
}
