package model;

public class ItemBoxModel
{
	private int id;
	private int itemId;
	private int boxId;
	private int amount;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

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

	public void addAmount(int amount)
	{
		this.amount += amount;
	}
}
