package cs.bistu.edu.cn.cellphone;

/**
 * 京东商城手机产品信息 商品名称、商品价格、商品总评价数、商品好评度、商品介绍、商品URL
 */
public class Cellphone {
	private String id; // 商品ID
	private String url; // 商品URL
	private String name; // 商品名称
	private String price; // 商品价格
	private String goodRate; // 好评率
	private String parameter; // 参数
	private String commentCount;
	private String comment; // 商品评论

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getGoodRate() {
		return goodRate;
	}

	public void setGoodRate(String goodRate) {
		this.goodRate = goodRate;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
