package castle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import map.Item;
import map.Person;
import map.Room;

public class Game {
    private Room currentRoom;
    private Person currentPerson;
    private ArrayList<Item> bag = new ArrayList<>();
    private int bagSizeLimit = 4;
    private HashMap<String, Handler> handlers = new HashMap<>();
        
    public Game() 
    {
    	createHandlers();
        createRooms();
    }

    private void createHandlers() {
    	
    	handlers.put("bye", new Handler() {

			@Override
			public boolean isBye() {
				return true;
			}

			@Override
			public void helpInfo() {
				System.out.println("bye：退出游戏");
			}
    		
    	});
    	
    	handlers.put("help", new Handler() {

			@Override
			public void doCmd(String word) {
				if ( word.equals("")){
					StringBuffer sb = new StringBuffer();
					for ( String str : handlers.keySet()){
						sb.append(str);
						sb.append(' ');
					}
			        System.out.println("迷路了吗？");
			        showPrompt();
			        System.out.println("你可以做的命令有：" + sb.toString());
			        System.out.println("若需查看命令的详细信息，请输入'help 命令'。");
			        System.out.println("如'help help'");
				}
				else {
					Handler handler = handlers.get(word);
					if(handler != null)
						handler.helpInfo();
					else{
						System.out.println("命令不存在");
					}
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("help：显示帮助信息");
				System.out.println("help 命令：显示该命令的帮助信息");
				System.out.println("如'help go'");
			}    		
			
    	});
    	
    	handlers.put("go", new Handler() {

			@Override
			public void doCmd(String word) {
				goRoom(word);
			}

			@Override
			public void helpInfo() {
				System.out.println("go 方向：向相应方向前进");
				System.out.println("如'go east'");
			}   		
			
    	});
    	
    	handlers.put("talk", new Handler(){

			@Override
			public void doCmd(String word) {
				currentPerson = currentRoom.getPerson(Integer.parseInt(word));
				System.out.println(currentPerson + "：");
				currentPerson.speak();
			}

			@Override
			public void helpInfo() {
				System.out.println("talk 人物编号：和相应人物对话");
				System.out.println("如'talk 1'");
			}
    		
    	});
        	
    	handlers.put("pick", new Handler() {
    		
    		@Override
    		public void doCmd(String word) {
    			try{
    				int index = Integer.parseInt(word);
    				Item item = currentRoom.getItem(index);
    				Item newItem = item.get();
    				addInBag(newItem);
    				if (newItem == item){
    					currentRoom.removeItem(index);
    				}
    			} catch (NullPointerException e){
    				System.out.println("输入编号有误");
    			}  
    		}

			@Override
			public void helpInfo() {
				System.out.println("pick 物品编号：捡起物品，放入背包");
				System.err.println("如'pick 1'");
			}
    		
    	});
    	
    	handlers.put("bag", new Handler(){

			@Override
			public void doCmd(String word) {

		       	StringBuffer sb = new StringBuffer();
		    	for ( int i = 0; i < bag.size(); i++ ){
		    		sb.append("[");
		    		sb.append( i );
		    		sb.append("]");
		    		sb.append(bag.get(i));
		    		sb.append(' ');
		    	}
		    	String str = sb.toString();
		    	if ( str.equals(""))
		    		System.out.println("空");
		    	else
		    		System.out.println(str);
			}

			@Override
			public void helpInfo() {
				System.out.println("bag：查看背包");
			}
    		
    	});
    	
    	handlers.put("give", new Handler(){

			@Override
			public void doCmd(String word) {
				try {
					int index = Integer.parseInt(word);
					Item item = getOutOfBag(index);
					if ( !currentPerson.receive(item) ){
						addInBag(item);
					}
				} catch(IndexOutOfBoundsException e){
					System.out.println("输入编号有误");
				}
			}

			@Override
			public void helpInfo() {
				System.out.println("give 物品编号：将相应物品交给当前对话对象");
				System.out.println("如'give 1'");
			}
			
    	});   	
    	
	}

	private void createRooms()
    {
    	Room outside, lobby, pub, study, bedroom, storage, stairs;
      
        //	制造房间
        outside = new Room("城堡外");
        lobby = new Room("大堂");
        pub = new Room("小酒吧");
        storage = new Room("储藏室");
        study = new Room("书房");
        bedroom = new Room("卧室");
        stairs = new Room("楼梯");
        
        //	初始化房间的出口
        outside.setExit("north", lobby);
        lobby.setDoubleExit("east", study);
        lobby.setDoubleExit("north", stairs);
        lobby.setDoubleExit("west", pub);
        study.setDoubleExit("north", bedroom);
        stairs.setDoubleExit("west", storage);        
        
        //  初始化房间內的物品
        outside.addItems(new Item("花盆"));
        lobby.addItems(new Item("金币"), new Item("金币"), new Item("大门"));
        lobby.addPersons(new Person("管家") {

			@Override
			public void speak() {
				System.out.println("哼哼哼，德古拉堡都敢随便乱闯。");
				System.out.println("今天就让你有来无回！");
			}
        	
        });

        pub.addItems(new Item("金币"));        
        pub.addPersons(new Person("店主"){

			@Override
			public void speak() {
				System.out.println("买酒吗？");
			}

			@Override
			public boolean receive(Item item) {
				if (item.toString().equals("金币")){
					System.out.println("这是你的酒");
					addInBag(new Item("酒"));
					return true;
				}
				else{
					System.out.println("我不要");
					return false;
				}
			}
			
        });
        
        pub.addPersons(new Person("酒客"){

			@Override
			public void speak() {
				System.out.println("哎呀，要是能再来一瓶就好了");
			}

			@Override
			public boolean receive(Item item) {
				if(item.toString().equals("酒")){
					System.out.println("书房里总是藏着些小秘密");
					return true;
				}
				else{
					System.out.println("我不要");
					return false;
				}
			}
			
        });
        
        study.addItems(new Item("书架") {

			@Override
			public Item get() {
				return new Item("一张写着“晚餐放在储藏室”的备忘便笺");
			}
        	
        }, new Item("金币"));
        
        storage.addItems(new Item("冷藏的红酒"));
        storage.addPersons(new Person("少女"){

			@Override
			public void speak() {
				System.out.println("什么？你要和我一起走？");
			}

			@Override
			public boolean receive(Item item) {
				if (item.toString().equals("花盆")){
					System.out.println("哦这个啊~这个里面放着大门钥匙");
					addInBag(new Item("大门钥匙"));
					return true;
				}
				else if (item.toString().equals("冷藏的红酒")){
					System.out.println("偷偷告诉你，卧室的床底下有暗门哦");
					return true;
				}
				else{
					System.out.println("我不要");
					return false;
				}
			}
        });
        
        bedroom.addItems(new Item("床"));
        
        
        
        currentRoom = outside;  //	从城堡门外开始
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("欢迎来到城堡！");
        System.out.println("这仍然是一个很无聊的游戏。");
        System.out.println("如果需要帮助，请输入 'help' 。");
        System.out.println();
        showPrompt();
    }

    public void goRoom(String direction) 
    {
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) {
            System.out.println("一头撞在了墙上");
        }
        else {
            currentRoom = nextRoom;
            showPrompt();
        }
    }
    
    private void addInBag( Item item){
		if ( bag.size() <= bagSizeLimit && item != null){		
			bag.add(item);
			System.out.println("获得\t" + item); 			
		}    			
		else if (item != null){
			System.out.println("背包已满");
		}
    }
    
    private Item getOutOfBag (int index){
    	Item item = bag.remove(index);
		System.out.println("失去\t" + item);
		return item;
    }
    
    public void showPrompt(){
        System.out.println("你在" + currentRoom + "，这里有");
        String str = null;
   
        if ( !(str = currentRoom.showItems()).equals("") ){
        	System.out.println("物品\t" + str);
        }
        if ( !(str = currentRoom.showPersons()).equals("") ){
        	System.out.println("人物\t" + str);
        }
        if ( !(str = currentRoom.showExits()).equals("") ){
        	System.out.println("出口\t" + str);
        }
    }
	
    public void play(){
		Scanner in = new Scanner(System.in);
        while ( true ) {
    		String line = in.nextLine();
    		String[] words = line.split(" ");
    		Handler handler = handlers.get(words[0]);
    		String value = "";
    		if ( words.length > 1 )
    			value = words[1];
    		if ( handler !=null ) {
    			handler.doCmd(value);
    			if ( handler.isBye() ) 
    				break;
    		}
    		else{
    			System.out.println("做不到哟");
    		}
        }
        in.close();
    }
    
	public static void main(String[] args) {
		Game game = new Game();
		game.printWelcome();
		game.play();
		
        System.out.println("-游戏结束-");
	}

}
