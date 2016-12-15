package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import gui.Message;


// This is simulated message server
public class MessageServer implements Iterable<Message>{
	
	private Map<Integer,List<Message>> messages;
	private List<Message> selected;
	
	public MessageServer() {
		messages= new TreeMap<Integer,List<Message>>();
		
		//dummy messages
		selected = new ArrayList<Message>();
		
		List<Message> list = new ArrayList<Message>();
		
		list.add(new Message("Cat is missing have you seen it?", "No I did not"));
		list.add(new Message("See ya?", "Yep :)"));
		
		messages.put(0, list);
		
		
		list = new ArrayList<Message>();
		
		list.add(new Message("Dinner?", "No, thanks."));
		list.add(new Message("Hungry?", "Yes."));
		list.add(new Message("Hungry?", "Yes."));
		
		messages.put(1, list);
		
	}
	
	public void setSelectedServers(Set<Integer> servers){
		selected.clear();
		
		//checki if servers from 'messages' are on the 'selected' list
		for(Integer id: servers){
			if(messages.containsKey(id)){
				selected.addAll(messages.get(id));
			}
		}
	}
	
	public int getMessageCount(){
		return selected.size();
	}

	@Override
	public Iterator<Message> iterator() {
		// TODO Auto-generated method stub
		return new MessageIterator(selected);
	}

}


class MessageIterator implements Iterator{

	private Iterator<Message> iterator;
	
	public MessageIterator(List<Message> messages){
		iterator = messages.iterator();
	}
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return iterator.hasNext();
	}

	@Override
	public Object next() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		
		}
		
		return iterator.next();
	}
	
	public void remove(){
		iterator.remove();
	}
	
}