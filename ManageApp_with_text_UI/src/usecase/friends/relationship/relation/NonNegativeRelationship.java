package usecase.friends.relationship.relation;

/**
 * A Non-negative relationship is a relationship that don't allows negative integer.
 */
public class NonNegativeRelationship implements Relationship{
	
	private int layer;
	
	public NonNegativeRelationship(int initialValue){
		this.layer = initialValue;
	}
	
	@Override
	public int getLayer(){
		return layer;
	}
	
	@Override
	public void increase(){
		applyOffset(1);
	}
	
	@Override
	public void decrease(){
		applyOffset(- 1);
	}
	
	/**
	 * {@inheritDoc}
	 * If the offset makes the layer becomes negative, set it to 0.
	 */
	@Override
	public void applyOffset(int offset){
		if(layer + offset < 0)
			// layer + offset < 0 <=> layer < - offset
			layer = 0;
		else
			layer += offset;
	}
}
