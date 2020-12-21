package UI.textUI;

import exception.IllegalInputException;
import presenter.seletable.Selectable;

import java.util.*;
import java.util.stream.Stream;

/**
 * A Selection is a useful utility to prompt the user to select
 * something from a Selectable.
 *
 * @param <E> any class
 */
public class Selection<E> implements Iterable<String>{
	
	private final Map<Integer, E>     map;
	private final List<SelectionUnit> selectionUnits;
	private       int                 maxIndexLength;
	
	public Selection(){
		this.map = new HashMap<>();
		this.selectionUnits = new ArrayList<>();
	}
	
	public Selection(String name, Selectable<E> selectable){
		this();
		addUnit(name, selectable);
	}
	
	/**
	 * Add a Selection unit which correspond to a Selectable.
	 */
	public void addUnit(String name, Selectable<E> selection){
		// starting from
		int i = map.size() + 1;
		for(E e : selection.getSelection())
			map.put(i++, e);
		// update the max length
		maxIndexLength = String.valueOf(i - 1).length();
		selectionUnits.add(new SelectionUnit(name, selection));
	}
	
	/**
	 * Check if there has any selection.
	 */
	public boolean hasSelection(){
		return ! map.isEmpty();
	}
	
	/**
	 * Process a input, if the user wants to abort, returns null.
	 */
	public E processInput(String input) throws IllegalInputException{
		// handle illegal inputs
		if(input == null)
			throw new IllegalInputException("Null Input");
		if(input.equals(""))
			return null;
		if(! input.matches("\\d+"))
			throw new IllegalInputException(input, "Not a number!");
		int selected = Integer.parseInt(input);
		if(! map.containsKey(selected))
			throw new IllegalInputException(input, "Out of bound!");
		return map.get(selected);
	}
	
	@Override
	public Iterator<String> iterator(){
		if(selectionUnits.isEmpty())
			return Stream.of("Nothing to Select!").iterator();
		Stream.Builder<String> selection = Stream.builder();
		for(SelectionUnit unit : selectionUnits){
			selection.add(unit.getName());
			if(unit.getSelections().size() == 0)
				selection.add("<Nothing>");
			else
				unit.getSelections()
				    .forEach(e -> {
					    selection.add(String.format(
							    "%" + maxIndexLength + "d : %s",
							    findIndex(e),
							    unit.getDescription(e)
					    ));
				    });
		}
		return selection.build().iterator();
	}
	
	private int findIndex(E e){
		return map.entrySet()
		          .stream().filter(entry -> entry.getValue().equals(e)).findAny()
		          .orElseThrow(() -> new IllegalArgumentException("Doesn't Exist!"))
		          .getKey();
	}
	
	private class SelectionUnit{
		
		private final String        name;
		private final Selectable<E> selectable;
		
		public SelectionUnit(String name, Selectable<E> selectable){
			this.name = name;
			this.selectable = selectable;
		}
		
		public String getName(){
			return name;
		}
		
		public List<E> getSelections(){
			return selectable.getSelection();
		}
		
		public String getDescription(E e){
			return selectable.getDescription(e);
		}
	}
}
