//Connor Dawkins | 16/10/2020

public class Products 
{
	private int productID;
	private String productName;
	private int productCost;
	private boolean inStock;
	private String type;
	private String subType;
	
	public Products(String[] data) 
	{
		this.productID = Integer.parseInt(data[0]);
		this.productName = data[1];
		this.productCost = Integer.parseInt(data[2]);
		this.inStock = Boolean.parseBoolean(data[3]);
		this.type = data[4];
		this.subType = data[5];
	}

	public int getProductID() {return this.productID;}
	public void setProductID(int id) {this.productID = id;}
	
	public String getProductName() {return this.productName;}
	public void setProductName(String productName) {this.productName = productName;}
	
	public int getProductCost() {return this.productCost;}
	public void setProductCost(int productCost) {this.productCost = productCost;}
	
	public boolean getInStock() {return this.inStock;}
	public void setInStock(boolean inStock) {this.inStock = inStock;}
	
	public String getType() {return this.type;}
	public void setType(String type) {this.type = type;}
	
	public String getSubType() {return this.subType;}
	public void setSubType(String subType) {this.subType = subType;}
}