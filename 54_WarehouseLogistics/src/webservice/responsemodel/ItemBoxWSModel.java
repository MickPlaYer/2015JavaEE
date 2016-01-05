package webservice.responsemodel;

public class ItemBoxWSModel
{
	private int itemId;
	private int boxId;
	private int amount;
	
	public int getItemId()
	{
		return itemId;
	}
	
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

	public int getBoxId()
	{
		return boxId;
	}

	public void setBoxId(int boxId)
	{
		this.boxId = boxId;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount(int amount)
	{
		this.amount = amount;
	}
}
