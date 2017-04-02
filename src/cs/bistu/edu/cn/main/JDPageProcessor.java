package cs.bistu.edu.cn.main;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import cs.bistu.edu.cn.cellphone.Cellphone;
import cs.bistu.edu.cn.util.JDItemProcessor;
import cs.bistu.edu.cn.util.JDJsonProcessor;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author Vinci
 * @since 2017-3-17
 */

public class JDPageProcessor implements PageProcessor {
	// 入口地址
	private static final String START_PAGE = "http://list.jd.com/list.html?cat=9987,653,655&page=1&go=0&JL=6_0_0";
	// 需要抓取页数
	private static final int pageCounts = 1;
	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCycleRetryTimes(3).setSleepTime(300).setTimeOut(10000);
	// 日志文件
	private static final Logger log = Logger.getLogger(JDPageProcessor.class);
	// CellPhone 对象链表
	private static LinkedList<Cellphone> cellphones = new LinkedList<>();

	public static LinkedList<Cellphone> getCellphones() {
		return cellphones;
	}

	public static void setCellphones(LinkedList<Cellphone> cellphones) {
		JDPageProcessor.cellphones = cellphones;
	}

	public Site getSite() {
		return site;
	}

	/**
	 * 处理页面方法 包括对cell phone 各项属性的赋值
	 */
	public void process(Page page) {
		log.info("抓取页面：" + page.getUrl()); // 日志
		/*
		 * 首页目录下商品信息示例(简化)：
		 * 
		 * <div class="p-img"> <a target="_blank"
		 * href="http://item.jd.com/2938299.html"> </div>
		 *
		 * <div class="p-name"> <a target="_blank" title="" href=
		 * "http://item.jd.com/10941037481.html"> <em>vivo X9 全网通 4GB+64GB
		 * 移动联通电信4G手机 双卡双待 玫瑰金</em> <i class="promo-words"></i> </a> </div>
		 * 
		 * <a class="J_focus" data-sku="2938299" href="">
		 */

		// 需要注意的问题就是 ID 价格 以及 链接 等 都得对齐
		// 获取目录页下所有商品ID
		List<String> ids = page.getHtml().xpath("//div[@class='p-focus']/a/@data-sku").all();

		// 获取目录页下所有商品URL
		List<String> urls = page.getHtml().xpath("//div[@class='p-img']/a[@target='_blank']/@href").all();

		// 获取目录页下所有商品名称
		List<String> names = page.getHtml().xpath("//div[@class='p-name']/a/em/text()").all();

		// 获取目录下所有商品的价格信息
		// 调用makePriceJsonUrl方法拼凑出带有当前静态页面所有商品价格的URL，然后调用另外一个爬虫对这个URL进行JSON解析，返回价格链表
		List<String> prices = JDJsonProcessor.running(makePriceJsonUrl(ids));

		// 获取目录下所有商品的好评率
		List<String> goodRates = JDJsonProcessor.running(makeCommentJsonUrl(ids));

		// 获取目录下所有商品的评价数
		List<String> commentCounts = JDJsonProcessor.getCommentCounts();

		// 商品链接链表传入JDItemProcessor，获得评论以及参数信息
		JDItemProcessor.running(urls);
		List<String> parameters = JDItemProcessor.getParameters();
		List<String> comments = JDItemProcessor.getComments();

		// 设置cell phone的各项属性
		for (int i = 0; i < ids.size(); i++) {
			Cellphone cellphone = new Cellphone();
			cellphone.setId(ids.get(i));
			cellphone.setUrl(urls.get(i));
			cellphone.setName(names.get(i));
			cellphone.setPrice(prices.get(i));
			cellphone.setGoodRate(goodRates.get(i));
			cellphone.setCommentCount(commentCounts.get(i));
			cellphone.setParameter(parameters.get(i));
			cellphone.setComment(comments.get(i));
			cellphones.add(cellphone);
		}
		// 数据持久化
		page.putField("cellphones", cellphones);

		/*
		 * 获取下一页地址并放入TargetRequest()
		 */
		// 当前页小于最大需要抓取的页面数就继续向下抓

		for (int nextPageNum = 2; nextPageNum <= pageCounts; nextPageNum++) {
			// 拼凑下一页地址(只需要把开始页面的 “page=1” 换成 “page=nextPageNum”)
			String nextPageUrl = START_PAGE.replace("page=1", "page=" + nextPageNum);

			page.addTargetRequest(nextPageUrl); // 将待抓取页面放入到Target里面
		}
		System.out.println("\n----------------------------------------------------------------------\n");
	}

	/**
	 * 传入ID拼凑出含有商品价格信息的JSON地址
	 * 
	 */

	public String makePriceJsonUrl(List<String> ids) {
		StringBuffer sb = new StringBuffer();
		for (String id : ids) {
			sb.append("J_" + id + ",");
		}
		String substring = sb.substring(0, sb.length() - 1); // 去掉最后一个逗号
		return "http://p.3.cn/prices/mgets?skuIds=" + substring;
	}

	/**
	 * 传入ID拼凑出含有商品评论信息的JSON地址
	 * 
	 */
	public String makeCommentJsonUrl(List<String> ids) {
		StringBuffer sb = new StringBuffer();
		for (String id : ids) {
			sb.append(id + ",");
		}
		String substring = sb.substring(0, sb.length() - 1);
		return "http://club.jd.com/comment/productCommentSummaries.action?my=pinglun&referenceIds=" + substring;
	}

	public static void main(String[] args) {
		System.out.println("\n--------------------------程序开始------------------------------------\n");

		Spider.create(new JDPageProcessor()).addUrl(START_PAGE).addPipeline(new JsonFilePipeline("./JD")).run();

		System.out.println("\n--------------------------结果展示------------------------------------\n");
		
		for (int i = 0; i < cellphones.size(); i++) {
			System.out.println("\nID:" + cellphones.get(i).getId() + "\nName:" + cellphones.get(i).getName()
					+ "\nPrice:" + cellphones.get(i).getPrice() + "\nURL:" + cellphones.get(i).getUrl() + "\nGoodRate:"
					+ cellphones.get(i).getGoodRate() + "\nCommentCount:" + cellphones.get(i).getCommentCount()
					+ "\nParameter:" + cellphones.get(i).getParameter());
			System.out.println("\nComments:");
			System.out.println(cellphones.get(i).getComment());
		}
		System.out.println("\n--------------------------统计数据------------------------------------\n");
		
		System.out.println("获得手机总数：" + cellphones.size() + "\n");
	}
}
